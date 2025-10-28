package cn.qiuye.gtl_extend.common.machine.multiblock.electric;

import org.gtlcore.gtlcore.common.machine.trait.MultipleRecipesLogic;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableElectricMultiblockMachine;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;

import com.gtladd.gtladditions.api.machine.IWirelessThreadModifierParallelMachine;
import com.gtladd.gtladditions.api.machine.feature.IThreadModifierPart;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlatinumBasedRocessingHub extends WorkableElectricMultiblockMachine implements IWirelessThreadModifierParallelMachine {

    protected @Nullable IThreadModifierPart threadPartMachine = null;

    public void setThreadPartMachine(@Nullable IThreadModifierPart threadPartMachine) {
        this.threadPartMachine = threadPartMachine;
    }

    public int getAdditionalThread() {
        return threadPartMachine != null ? threadPartMachine.getThreadCount() : 0;
    }

    public PlatinumBasedRocessingHub(IMachineBlockEntity holder, Object... args) {
        super(holder, args);
    }

    @Override
    protected @NotNull RecipeLogic createRecipeLogic(Object @NotNull... args) {
        return new MultipleRecipesLogic(this);
    }

    @NotNull
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
