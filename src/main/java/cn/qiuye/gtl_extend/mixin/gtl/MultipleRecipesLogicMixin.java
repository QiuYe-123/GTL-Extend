package cn.qiuye.gtl_extend.mixin.gtl;

import cn.qiuye.gtl_extend.config.GTLExtendConfigHolder;

import org.gtlcore.gtlcore.api.machine.multiblock.ParallelMachine;
import org.gtlcore.gtlcore.api.machine.trait.ILockRecipe;
import org.gtlcore.gtlcore.api.machine.trait.IRecipeCapabilityMachine;
import org.gtlcore.gtlcore.api.recipe.IGTRecipe;
import org.gtlcore.gtlcore.api.recipe.IParallelLogic;
import org.gtlcore.gtlcore.api.recipe.RecipeRunnerHelper;
import org.gtlcore.gtlcore.common.machine.trait.MultipleRecipesLogic;

import com.gregtechceu.gtceu.api.capability.recipe.EURecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.FluidRecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.ItemRecipeCapability;
import com.gregtechceu.gtceu.api.machine.feature.IRecipeLogicMachine;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMaintenanceMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableElectricMultiblockMachine;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.RecipeHelper;
import com.gregtechceu.gtceu.api.recipe.content.Content;
import com.gregtechceu.gtceu.api.recipe.content.ContentModifier;
import com.gregtechceu.gtceu.data.recipe.builder.GTRecipeBuilder;

import net.minecraft.nbt.CompoundTag;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.function.BiPredicate;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.spongepowered.asm.mixin.*;

@Mixin(value = MultipleRecipesLogic.class, priority = 9999)
public class MultipleRecipesLogicMixin extends RecipeLogic implements ILockRecipe {

    @Mutable
    @Final
    @Shadow(remap = false)
    private final ParallelMachine parallel;
    @Mutable
    @Final
    @Shadow(remap = false)
    private final BiPredicate<CompoundTag, IRecipeLogicMachine> dataCheck;
    @Mutable
    @Shadow(remap = false)
    private double reductionRatio;
    @Unique
    private static final long MAX_THREADS = Integer.MAX_VALUE - 1;

    @Unique
    private Integer gtl_extend$MaxThreads() {
        long var;
        if (GTLExtendConfigHolder.INSTANCE.max_threads_bool) {
            var = GTLExtendConfigHolder.INSTANCE.max_threads;
        } else {
            var = Integer.MAX_VALUE - 1;
        }
        int var2 = (int) var;
        return var2;
    }

    public MultipleRecipesLogicMixin(ParallelMachine machine) {
        this(machine, null);
    }

    public MultipleRecipesLogicMixin(ParallelMachine machine, BiPredicate<CompoundTag, IRecipeLogicMachine> dataCheck) {
        this(machine, dataCheck, 1.0F, 1.0F);
    }

    public MultipleRecipesLogicMixin(ParallelMachine machine, BiPredicate<CompoundTag, IRecipeLogicMachine> dataCheck, double reductionEUt, double reductionDuration) {
        super((IRecipeLogicMachine) machine);
        this.parallel = machine;
        this.dataCheck = dataCheck;
        this.reductionRatio = reductionEUt * reductionDuration;
    }

    @Shadow(remap = false)
    public WorkableElectricMultiblockMachine getMachine() {
        return (WorkableElectricMultiblockMachine) super.getMachine();
    }

    @Shadow(remap = false)
    protected double getEuMultiplier() {
        IMaintenanceMachine maintenanceMachine = ((IRecipeCapabilityMachine) this.parallel).getMaintenanceMachine();
        return maintenanceMachine != null ? (double) maintenanceMachine.getDurationMultiplier() * this.reductionRatio : this.reductionRatio;
    }

    @Shadow(remap = false)
    protected double getTotalEuOfRecipe(GTRecipe recipe) {
        return (double) (RecipeHelper.getInputEUt(recipe) * (long) recipe.duration);
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    private GTRecipe getRecipe() {
        if (!this.machine.hasProxies()) {
            return null;
        } else {
            long maxEUt = this.getMachine().getOverclockVoltage();
            if (maxEUt <= 0L) {
                return null;
            } else {
                Iterator<GTRecipe> iterator = this.lookupRecipeIterator();
                GTRecipe output = GTRecipeBuilder.ofRaw().buildRawRecipe();
                output.outputs.put(ItemRecipeCapability.CAP, new ObjectArrayList<>());
                output.outputs.put(FluidRecipeCapability.CAP, new ObjectArrayList<>());
                long totalEu = 0L;
                long remain = (long) this.parallel.getMaxParallel() * gtl_extend$MaxThreads();
                double euMultiplier = this.getEuMultiplier();

                while (remain > 0L && iterator.hasNext()) {
                    GTRecipe match = iterator.next();
                    if (match != null) {
                        long p = IParallelLogic.getMaxParallel(this.machine, match, remain);
                        if (p > 0L) {
                            if (p > 1L) {
                                match = match.copy(ContentModifier.multiplier((double) p), false);
                            }

                            ((IGTRecipe) match).setRealParallels(p);
                            match = IParallelLogic.getRecipeOutputChance(this.machine, match);
                            remain -= p;
                            if (RecipeRunnerHelper.handleRecipeInput(this.machine, match)) {
                                totalEu += (long) (this.getTotalEuOfRecipe(match) * euMultiplier);
                                List<Content> item = match.outputs.get(ItemRecipeCapability.CAP);
                                if (item != null) {
                                    output.outputs.get(ItemRecipeCapability.CAP).addAll(item);
                                }

                                List<Content> fluid = match.outputs.get(FluidRecipeCapability.CAP);
                                if (fluid != null) {
                                    output.outputs.get(FluidRecipeCapability.CAP).addAll(fluid);
                                }
                            }

                            if (totalEu / maxEUt > 10000L) {
                                break;
                            }
                        }
                    }
                }

                if (output.outputs.get(ItemRecipeCapability.CAP).isEmpty() && output.outputs.get(FluidRecipeCapability.CAP).isEmpty()) {
                    return null;
                } else {
                    double d = (double) totalEu / (double) maxEUt;
                    long eut = d > (double) 20.0F ? maxEUt : (long) ((double) maxEUt * d / (double) 20.0F);
                    output.tickInputs.put(EURecipeCapability.CAP, List.of(new Content(eut, 10000, 10000, 0, null, null)));
                    output.duration = Math.min((int) Math.max(d, 1.0F), 20);
                    IGTRecipe.of(output).setHasTick(true);
                    return output;
                }
            }
        }
    }

    @Shadow(remap = false)
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

    @Shadow(remap = false)
    private boolean checkRecipe(GTRecipe recipe) {
        return RecipeRunnerHelper.matchRecipe(this.machine, recipe) && recipe.data.getInt("euTier") <= this.getMachine().getTier() && recipe.checkConditions(this).isSuccess() && (this.dataCheck == null || this.dataCheck.test(recipe.data, this.machine));
    }
}
