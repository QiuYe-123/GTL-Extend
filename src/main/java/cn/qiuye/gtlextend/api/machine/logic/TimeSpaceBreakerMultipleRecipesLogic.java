package cn.qiuye.gtlextend.api.machine.logic;

import cn.qiuye.gtlextend.api.machine.IThreadModifierParallelMachine;
import cn.qiuye.gtlextend.api.recipe.NoEnergyGTRecipe;
import cn.qiuye.gtlextend.api.recipe.NoEnergyGTRecipeBuilder;
import cn.qiuye.gtlextend.common.record.ParallelData;
import cn.qiuye.gtlextend.utils.CommonUtils;

import org.gtlcore.gtlcore.api.machine.trait.ILockRecipe;
import org.gtlcore.gtlcore.api.machine.trait.IRecipeStatus;
import org.gtlcore.gtlcore.api.recipe.IGTRecipe;
import org.gtlcore.gtlcore.api.recipe.IParallelLogic;
import org.gtlcore.gtlcore.api.recipe.RecipeResult;
import org.gtlcore.gtlcore.api.recipe.RecipeRunnerHelper;

import com.gregtechceu.gtceu.api.capability.recipe.FluidRecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.ItemRecipeCapability;
import com.gregtechceu.gtceu.api.machine.feature.IRecipeLogicMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableElectricMultiblockMachine;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.content.Content;
import com.gregtechceu.gtceu.api.recipe.content.ContentModifier;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.longs.LongArrayList;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import lombok.Getter;
import net.minecraft.nbt.CompoundTag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

import static org.gtlcore.gtlcore.api.recipe.RecipeRunnerHelper.handleRecipeInput;
import static org.gtlcore.gtlcore.api.recipe.RecipeRunnerHelper.matchRecipeInput;

@Getter
public class TimeSpaceBreakerMultipleRecipesLogic extends RecipeLogic implements ILockRecipe, IRecipeStatus {

    protected final Predicate<IRecipeLogicMachine> beforeWorking;

    protected final IThreadModifierParallelMachine parallel;

    protected final BiPredicate<CompoundTag, IRecipeLogicMachine> dataCheck;

    public TimeSpaceBreakerMultipleRecipesLogic(IThreadModifierParallelMachine machine) {
        this(machine, null, null);
    }

    public TimeSpaceBreakerMultipleRecipesLogic(IThreadModifierParallelMachine machine, BiPredicate<CompoundTag, IRecipeLogicMachine> dataCheck, Predicate<IRecipeLogicMachine> beforeWorking) {
        super((IRecipeLogicMachine) machine);
        this.parallel = machine;
        this.dataCheck = dataCheck;
        this.beforeWorking = beforeWorking;
    }

    @Override
    public WorkableElectricMultiblockMachine getMachine() {
        return (WorkableElectricMultiblockMachine) super.getMachine();
    }

    @Override
    public void findAndHandleRecipe() {
        this.lastRecipe = null;
        this.setRecipeStatus(null);
        GTRecipe match = this.getRecipe();
        if (match != null) {
            RecipeResult.of(this.machine, RecipeResult.SUCCESS);
            if (RecipeRunnerHelper.matchRecipeOutput(this.machine, match)) {
                this.setupRecipe(match);
            }
        }
    }

    @Nullable
    protected GTRecipe getRecipe() {
        if (!checkBeforeWorking()) return null;

        final var parallelData = calculateParallels();
        if (parallelData == null) return null;

        final var itemOutputs = new ObjectArrayList<Content>();
        final var fluidOutputs = new ObjectArrayList<Content>();

        int index = 0;

        for (var r : parallelData.recipeList) {

            final long p = parallelData.parallels[index++];
            if (p > 1) {
                r = r.copy(ContentModifier.multiplier(p), false);
            }
            IGTRecipe.of(r).setRealParallels(p);

            r = IParallelLogic.getRecipeOutputChance(machine, r);
            if (matchRecipeInput(machine, r) && handleRecipeInput(machine, r)) {
                var item = r.outputs.get(ItemRecipeCapability.CAP);
                if (item != null) itemOutputs.addAll(item);
                var fluid = r.outputs.get(FluidRecipeCapability.CAP);
                if (fluid != null) fluidOutputs.addAll(fluid);
            }
        }

        if (itemOutputs.isEmpty() && fluidOutputs.isEmpty()) {
            if (getRecipeStatus() == null || getRecipeStatus().isSuccess()) RecipeResult.of(this.machine, RecipeResult.FAIL_FIND);
            return null;
        }
        return buildRecipe(itemOutputs, fluidOutputs, parallel.getExtendlDuration());
    }

    @Nullable
    protected ParallelData calculateParallels() {
        var recipes = this.lookupRecipeIterator();
        int length = recipes.size();
        if (length == 0) return null;

        long totalParallel = (long) this.getParallel().getMaxParallel() * this.parallel.getExtendlThread();
        long remaining = totalParallel;
        long[] parallels = new long[length];
        int index = 0;
        var recipeList = new ObjectArrayList<GTRecipe>(length);
        var remainingWants = new LongArrayList(length);
        var remainingIndices = new IntArrayList(length);

        for (var r : recipes) {
            if (r == null) continue;
            long p = getMaxParallel(r, totalParallel);
            if (p <= 0) continue;
            recipeList.add(r);
            long allocated = Math.min(p, totalParallel / length);
            parallels[index] = allocated;
            long want = p - allocated;
            if (want > 0) {
                remainingWants.add(want);
                remainingIndices.add(index);
            }
            remaining -= allocated;
            index++;
        }

        if (recipeList.isEmpty()) return null;

        return CommonUtils.getParallelData(remaining, parallels, remainingWants, remainingIndices, recipeList);
    }

    protected @NotNull Set<GTRecipe> lookupRecipeIterator() {
        if (this.isLock()) {
            if (this.getLockRecipe() == null) this.setLockRecipe(machine.getRecipeType().getLookup()
                    .find(machine, this::checkRecipe));
            else if (!checkRecipe(this.getLockRecipe())) return Collections.emptySet();
            return Collections.singleton(this.getLockRecipe());
        } else {
            var iterator = machine.getRecipeType().getLookup().getRecipeIterator(machine, this::checkRecipe);
            var recipeSet = new ObjectOpenHashSet<GTRecipe>();
            while (iterator.hasNext()) recipeSet.add(iterator.next());
            recipeSet.remove(null);
            return recipeSet;
        }
    }

    protected boolean checkBeforeWorking() {
        if (!machine.hasProxies()) return false;
        return this.beforeWorking == null || this.beforeWorking.test(machine);
    }

    protected long getMaxParallel(GTRecipe recipe, long limit) {
        return IParallelLogic.getMaxParallel(this.machine, recipe, limit);
    }

    private boolean checkRecipe(GTRecipe recipe) {
        return true;
    }

    protected @NotNull NoEnergyGTRecipe buildRecipe(@NotNull List<Content> item, @NotNull List<Content> fluid, int duration) {
        return NoEnergyGTRecipeBuilder
                .ofRaw()
                .output(ItemRecipeCapability.CAP, item)
                .output(FluidRecipeCapability.CAP, fluid)
                .duration(duration)
                .buildRawRecipe();
    }
}
