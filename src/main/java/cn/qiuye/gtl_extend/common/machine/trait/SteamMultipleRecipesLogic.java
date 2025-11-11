package cn.qiuye.gtl_extend.common.machine.trait;

import cn.qiuye.gtl_extend.api.machine.IThreadModifierParallelMachine;

import org.gtlcore.gtlcore.api.machine.trait.ILockRecipe;
import org.gtlcore.gtlcore.api.machine.trait.IRecipeCapabilityMachine;
import org.gtlcore.gtlcore.api.machine.trait.IRecipeStatus;
import org.gtlcore.gtlcore.api.recipe.IGTRecipe;
import org.gtlcore.gtlcore.api.recipe.RecipeResult;

import com.gregtechceu.gtceu.api.capability.recipe.EURecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.FluidRecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.ItemRecipeCapability;
import com.gregtechceu.gtceu.api.machine.feature.IRecipeLogicMachine;
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

import static org.gtlcore.gtlcore.api.recipe.IParallelLogic.getMaxParallel;
import static org.gtlcore.gtlcore.api.recipe.IParallelLogic.getRecipeOutputChance;
import static org.gtlcore.gtlcore.api.recipe.RecipeRunnerHelper.*;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import lombok.Getter;

@Getter
public class SteamMultipleRecipesLogic extends RecipeLogic implements ILockRecipe, IRecipeStatus {

    private final IThreadModifierParallelMachine parallel;

    private final BiPredicate<CompoundTag, IRecipeLogicMachine> dataCheck;

    private final double reductionRatio;

    public SteamMultipleRecipesLogic(IThreadModifierParallelMachine machine) {
        this(machine, null);
    }

    public SteamMultipleRecipesLogic(IThreadModifierParallelMachine machine, BiPredicate<CompoundTag, IRecipeLogicMachine> dataCheck) {
        this(machine, dataCheck, 1.0, 1.0);
    }

    public SteamMultipleRecipesLogic(IThreadModifierParallelMachine machine, BiPredicate<CompoundTag, IRecipeLogicMachine> dataCheck, double reductionEUt, double reductionDuration) {
        super((IRecipeLogicMachine) machine);
        this.parallel = machine;
        this.dataCheck = dataCheck;
        this.reductionRatio = reductionEUt * reductionDuration;
    }

    @Override
    public WorkableElectricMultiblockMachine getMachine() {
        return (WorkableElectricMultiblockMachine) super.getMachine();
    }

    @Override
    public void findAndHandleRecipe() {
        lastRecipe = null;
        setRecipeStatus(null);
        var match = getRecipe();
        if (match != null) {
            RecipeResult.of(machine, RecipeResult.SUCCESS);
            if (matchRecipeOutput(machine, match)) {
                setupRecipe(match);
            }
        }
    }

    protected double getTotalEuOfRecipe(GTRecipe recipe) {
        return RecipeHelper.getInputEUt(recipe) * recipe.duration;
    }

    protected double getEuMultiplier() {
        var maintenanceMachine = ((IRecipeCapabilityMachine) parallel).getMaintenanceMachine();
        return maintenanceMachine != null ?
                maintenanceMachine.getDurationMultiplier() * this.reductionRatio :
                this.reductionRatio;
    }

    private GTRecipe getRecipe() {
        if (!machine.hasProxies()) return null;
        long maxEUt = getMachine().getOverclockVoltage();
        if (maxEUt <= 0) return null;
        var iterator = lookupRecipeIterator();
        GTRecipe output = GTRecipeBuilder.ofRaw().buildRawRecipe();
        output.outputs.put(ItemRecipeCapability.CAP, new ObjectArrayList<>());
        output.outputs.put(FluidRecipeCapability.CAP, new ObjectArrayList<>());
        double totalEu = 0;
        long remain = this.parallel.getExtendlThread();
        double euMultiplier = getEuMultiplier();

        while (remain > 0 && iterator.hasNext()) {
            var match = iterator.next();
            if (match == null) continue;
            var p = getMaxParallel(machine, match, remain);
            if (p <= 0) continue;
            else if (p > 1) match = match.copy(ContentModifier.multiplier(p), false);
            ((IGTRecipe) match).setRealParallels(p);
            match = getRecipeOutputChance(machine, match);
            if (handleRecipeInput(machine, match)) {
                remain -= p;
                totalEu += getTotalEuOfRecipe(match) * euMultiplier;
                var item = match.outputs.get(ItemRecipeCapability.CAP);
                if (item != null) output.outputs.get(ItemRecipeCapability.CAP).addAll(item);
                var fluid = match.outputs.get(FluidRecipeCapability.CAP);
                if (fluid != null) output.outputs.get(FluidRecipeCapability.CAP).addAll(fluid);
            }
            if (totalEu / maxEUt > 20 * 500) break;
        }
        if (output.outputs.get(ItemRecipeCapability.CAP).isEmpty() &&
                output.outputs.get(FluidRecipeCapability.CAP).isEmpty()) {
            if (getRecipeStatus() == null || getRecipeStatus().isSuccess()) RecipeResult.of(this.machine, RecipeResult.FAIL_FIND);
            return null;
        }
        var d = totalEu / maxEUt;
        long eut = d > 20 ? maxEUt : (long) (maxEUt * d / 20);
        output.tickInputs.put(EURecipeCapability.CAP,
                List.of(new Content(eut, 10000, 10000, 0, null, null)));
        output.duration = (int) Math.max(d, 20);
        IGTRecipe.of(output).setHasTick(true);
        return output;
    }

    private Iterator<GTRecipe> lookupRecipeIterator() {
        if (this.isLock()) {
            if (this.getLockRecipe() == null) {
                this.setLockRecipe(machine.getRecipeType().getLookup()
                        .find(machine, this::checkRecipe));
            } else if (!checkRecipe(this.getLockRecipe())) return Collections.emptyIterator();
            return Collections.singleton(this.getLockRecipe()).iterator();
        } else return machine.getRecipeType().getLookup().getRecipeIterator(machine, this::checkRecipe);
    }

    private boolean checkRecipe(GTRecipe recipe) {
        return matchRecipe(machine, recipe) &&
                IGTRecipe.of(recipe).getEuTier() <= getMachine().getTier() &&
                recipe.checkConditions(this).isSuccess() &&
                (dataCheck == null || dataCheck.test(recipe.data, machine));
    }

    @Override
    public void onRecipeFinish() {
        machine.afterWorking();
        if (lastRecipe != null) {
            handleRecipeOutput(this.machine, lastRecipe);
        }
        var match = getRecipe();
        if (match != null) {
            if (matchRecipeOutput(machine, match)) {
                setupRecipe(match);
                return;
            }
        }
        setStatus(Status.IDLE);
        progress = 0;
        duration = 0;
    }
}
