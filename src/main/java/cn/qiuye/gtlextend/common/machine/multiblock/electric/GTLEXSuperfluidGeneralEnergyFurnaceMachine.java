package cn.qiuye.gtlextend.common.machine.multiblock.electric;

import org.gtlcore.gtlcore.common.machine.trait.MultipleRecipesLogic;

import com.gtladd.gtladditions.api.machine.IWirelessThreadModifierParallelMachine;
import com.gtladd.gtladditions.api.machine.feature.IThreadModifierPart;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableElectricMultiblockMachine;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;

import net.minecraft.MethodsReturnNonnullByDefault;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class GTLEXSuperfluidGeneralEnergyFurnaceMachine extends WorkableElectricMultiblockMachine implements IWirelessThreadModifierParallelMachine {

    protected @Nullable IThreadModifierPart threadPartMachine = null;

    public void setThreadPartMachine(@Nullable IThreadModifierPart threadPartMachine) {
        this.threadPartMachine = threadPartMachine;
    }

    public int getAdditionalThread() {
        return Integer.MAX_VALUE;
    }

    public GTLEXSuperfluidGeneralEnergyFurnaceMachine(IMachineBlockEntity holder, Object... args) {
        super(holder, args);
    }

    @Override
    protected RecipeLogic createRecipeLogic(Object @NotNull... args) {
        return new MultipleRecipesLogic(this);
    }

    @Override
    public MultipleRecipesLogic getRecipeLogic() {
        return (MultipleRecipesLogic) super.getRecipeLogic();
    }

    @Override
    public int getMaxParallel() {
        return Integer.MAX_VALUE;
    }

    @Override
    public void onStructureInvalid() {
        super.onStructureInvalid();
        this.threadPartMachine = null;
    }

    @Override
    public void onPartUnload() {
        super.onPartUnload();
        this.threadPartMachine = null;
    }
}
