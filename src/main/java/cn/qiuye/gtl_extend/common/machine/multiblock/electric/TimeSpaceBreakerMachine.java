package cn.qiuye.gtl_extend.common.machine.multiblock.electric;

import org.gtlcore.gtlcore.common.machine.trait.MultipleRecipesLogic;

import com.gregtechceu.gtceu.api.machine.ConditionalSubscriptionHandler;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableElectricMultiblockMachine;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.common.data.GTRecipeModifiers;

import com.gtladd.gtladditions.api.machine.IWirelessThreadModifierParallelMachine;
import com.gtladd.gtladditions.api.machine.feature.IThreadModifierPart;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TimeSpaceBreakerMachine extends WorkableElectricMultiblockMachine implements IWirelessThreadModifierParallelMachine {

    protected ConditionalSubscriptionHandler StartupSubs;

    protected @Nullable IThreadModifierPart threadPartMachine = null;

    public void setThreadPartMachine(@Nullable IThreadModifierPart threadPartMachine) {
        this.threadPartMachine = threadPartMachine;
    }

    public int getAdditionalThread() {
        return threadPartMachine != null ? threadPartMachine.getThreadCount() : 0;
    }

    public TimeSpaceBreakerMachine(IMachineBlockEntity holder, Object... args) {
        super(holder, args);
        this.StartupSubs = new ConditionalSubscriptionHandler(this, this::StartupUpdate, this::isFormed);
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

    @Nullable
    public GTRecipe recipeModifier(MetaMachine machine, @NotNull GTRecipe recipe) {
        if (machine instanceof TimeSpaceBreakerMachine TimeSpaceBreakerMachine) {
            {

                GTRecipe modifiedRecipe = recipe.copy();
                modifiedRecipe.duration = 1;
                modifiedRecipe.outputs.clear();
                modifiedRecipe.outputs.putAll(recipe.outputs);

                // 应用精确并行处理并返回结果
                return GTRecipeModifiers.accurateParallel(
                        this, // 传入当前实例
                        modifiedRecipe,
                        Integer.MAX_VALUE,
                        false).getFirst();
            }
        }
        return null;
    }

    // 电路配置更新逻辑
    protected void StartupUpdate() {}

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
