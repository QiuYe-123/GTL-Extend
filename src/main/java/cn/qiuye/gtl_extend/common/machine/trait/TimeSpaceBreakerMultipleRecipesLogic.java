package cn.qiuye.gtl_extend.common.machine.trait;

import cn.qiuye.gtl_extend.api.machine.IThreadModifierParallelMachine;
import cn.qiuye.gtl_extend.api.recipe.NoEnergyGTRecipeBuilder;

import org.gtlcore.gtlcore.api.machine.multiblock.NoEnergyMultiblockMachine;
import org.gtlcore.gtlcore.api.machine.trait.ILockRecipe;
import org.gtlcore.gtlcore.api.machine.trait.IRecipeStatus;
import org.gtlcore.gtlcore.api.recipe.IGTRecipe;
import org.gtlcore.gtlcore.api.recipe.IParallelLogic;
import org.gtlcore.gtlcore.api.recipe.RecipeResult;
import org.gtlcore.gtlcore.api.recipe.RecipeRunnerHelper;

import com.gregtechceu.gtceu.api.capability.recipe.FluidRecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.ItemRecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.RecipeCapability;
import com.gregtechceu.gtceu.api.machine.feature.IRecipeLogicMachine;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.content.Content;
import com.gregtechceu.gtceu.api.recipe.content.ContentModifier;
import com.gregtechceu.gtceu.api.recipe.ingredient.SizedIngredient;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.Reference2ReferenceOpenHashMap;
import lombok.Getter;
import lombok.val;
import net.minecraft.nbt.CompoundTag;

import java.util.*;
import java.util.function.BiPredicate;

@Getter
public class TimeSpaceBreakerMultipleRecipesLogic extends RecipeLogic implements ILockRecipe, IRecipeStatus {

    private final IThreadModifierParallelMachine parallel;

    private final BiPredicate<CompoundTag, IRecipeLogicMachine> dataCheck;

    public TimeSpaceBreakerMultipleRecipesLogic(IThreadModifierParallelMachine machine) {
        this(machine, null);
    }

    public TimeSpaceBreakerMultipleRecipesLogic(IThreadModifierParallelMachine machine, BiPredicate<CompoundTag, IRecipeLogicMachine> dataCheck) {
        super((IRecipeLogicMachine) machine);
        this.parallel = machine;
        this.dataCheck = dataCheck;
    }

    @Override
    public NoEnergyMultiblockMachine getMachine() {
        return (NoEnergyMultiblockMachine) super.getMachine();
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

    private GTRecipe getRecipe() {
        if (!machine.hasProxies()) return null;

        final var iterator = lookupRecipeIterator();
        final var itemInputs = new ObjectArrayList<Content>();
        final var fluidInputs = new ObjectArrayList<Content>();
        final var itemOutputs = new ObjectArrayList<Content>();
        final var fluidOutputs = new ObjectArrayList<Content>();

        long remain = (long) this.parallel.getMaxParallel() * this.parallel.getExtendlThread();

        while (remain > 0 && iterator.hasNext()) {
            GTRecipe match = iterator.next();
            if (match == null) continue;
            long p = IParallelLogic.getMaxParallel(machine, match, remain);
            if (p <= 0) continue;

            if (p > 1) {
                match = match.copy(ContentModifier.multiplier(p), false);
            }
            IGTRecipe.of(match).setRealParallels(p);

            match = IParallelLogic.getRecipeOutputChance(machine, match);
            var inputs = modifyInputContents(match.inputs);
            var outputs = modifyOutputContents(match.outputs);
            if (RecipeRunnerHelper.handleRecipeInput(machine, match)) {
                remain -= p;
                var initem = inputs.get(ItemRecipeCapability.CAP);
                if (initem != null) itemInputs.addAll(initem);
                var influid = inputs.get(FluidRecipeCapability.CAP);
                if (influid != null) fluidInputs.addAll(influid);
                var item = outputs.get(ItemRecipeCapability.CAP);
                if (item != null) itemOutputs.addAll(item);
                var fluid = outputs.get(FluidRecipeCapability.CAP);
                if (fluid != null) fluidOutputs.addAll(fluid);
            }
        }

        if (itemOutputs.isEmpty() && fluidOutputs.isEmpty()) {
            if (getRecipeStatus() == null || getRecipeStatus().isSuccess()) RecipeResult.of(this.machine, RecipeResult.FAIL_FIND);
            return null;
        }

        return NoEnergyGTRecipeBuilder
                .ofRaw()
                .input(ItemRecipeCapability.CAP, itemInputs)
                .input(FluidRecipeCapability.CAP, fluidInputs)
                .output(ItemRecipeCapability.CAP, itemOutputs)
                .output(FluidRecipeCapability.CAP, fluidOutputs)
                .duration(this.parallel.getExtendlDuration())
                .buildRawRecipe();
    }

    private static Reference2ReferenceOpenHashMap<RecipeCapability<?>, List<Content>> modifyInputContents(Map<RecipeCapability<?>, List<Content>> before) {
        val after = new Reference2ReferenceOpenHashMap<RecipeCapability<?>, List<Content>>();
        for (Map.Entry<RecipeCapability<?>, List<Content>> entry : before.entrySet()) {
            RecipeCapability<?> cap = entry.getKey();
            List<Content> contentList = entry.getValue();

            if (cap == ItemRecipeCapability.CAP) {
                List<Content> copyList = new ArrayList<>(contentList.size());
                for (Content content : contentList) {
                    // 假设 SizedIngredient 有 getItems() 方法，返回数组或列表
                    if (content.getContent() instanceof SizedIngredient sizedIngredient) {
                        Object firstItem = sizedIngredient.getItems()[0]; // 或者 getItems().get(0)

                        if (contentList.contains(firstItem)) {
                            copyList.add(content.copy(ItemRecipeCapability.CAP, ContentModifier.multiplier(100)));
                        } else {
                            copyList.add(content);
                        }
                    } else {
                        copyList.add(content);
                    }
                }
                after.put(cap, copyList);
            } else {
                after.put(cap, contentList);
            }
        }
        return after;
    }

    private static Reference2ReferenceOpenHashMap<RecipeCapability<?>, List<Content>> modifyOutputContents(Map<RecipeCapability<?>, List<Content>> before) {
        val after = new Reference2ReferenceOpenHashMap<RecipeCapability<?>, List<Content>>();
        for (Map.Entry<RecipeCapability<?>, List<Content>> entry : before.entrySet()) {
            RecipeCapability<?> cap = entry.getKey();
            List<Content> contentList = entry.getValue();
            List<Content> copyList = new ArrayList<>(contentList.size());

            if (cap == ItemRecipeCapability.CAP) {
                for (Content content : contentList) {
                    if (content.getContent() instanceof SizedIngredient sizedIngredient) {
                        Object firstItem = sizedIngredient.getItems()[0];

                        if (contentList.contains(firstItem)) {
                            copyList.add(content);
                        } else {
                            copyList.add(content.copy(ItemRecipeCapability.CAP, ContentModifier.multiplier(100)));
                        }
                    } else {
                        copyList.add(content.copy(ItemRecipeCapability.CAP, ContentModifier.multiplier(100)));
                    }
                }
            } else {
                for (Content content : contentList) {
                    copyList.add(content.copy(cap, ContentModifier.multiplier(100)));
                }
            }
            after.put(cap, copyList);
        }
        return after;
    }

    private Iterator<GTRecipe> lookupRecipeIterator() {
        if (this.isLock()) {
            if (this.getLockRecipe() == null) {
                this.setLockRecipe(this.machine.getRecipeType().getLookup().find(this.machine, this::checkRecipe));
            } else if (!this.checkRecipe(this.getLockRecipe())) {
                return Collections.emptyIterator();
            }
            return Collections.singleton(this.getLockRecipe()).iterator();
        } else {
            return this.machine.getRecipeType().getLookup().getRecipeIterator(this.machine, this::checkRecipe);
        }
    }

    private boolean checkRecipe(GTRecipe recipe) {
        return true;
    }
}
