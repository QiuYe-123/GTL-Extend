package cn.qiuye.gtlextend.common.machine.multiblock.electric;

import cn.qiuye.gtlextend.api.machine.IThreadModifierParallelMachine;
import cn.qiuye.gtlextend.common.machine.trait.TimeSpaceBreakerMultipleRecipesLogic;

import org.gtlcore.gtlcore.api.machine.multiblock.NoEnergyMultiblockMachine;

import com.gregtechceu.gtceu.api.machine.ConditionalSubscriptionHandler;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;

import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;

import net.minecraft.MethodsReturnNonnullByDefault;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class TimeSpaceBreakerMachine extends NoEnergyMultiblockMachine implements IThreadModifierParallelMachine {

    public static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            TimeSpaceBreakerMachine.class, NoEnergyMultiblockMachine.MANAGED_FIELD_HOLDER);

    protected ConditionalSubscriptionHandler StartupSubs;

    public TimeSpaceBreakerMachine(IMachineBlockEntity holder, Object... args) {
        super(holder, args);
        this.StartupSubs = new ConditionalSubscriptionHandler(this, this::StartupUpdate, this::isFormed);
    }

    private void StartupUpdate() {}

    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    @Override
    public RecipeLogic createRecipeLogic(Object @NotNull... args) {
        return new TimeSpaceBreakerMultipleRecipesLogic(this);
    }

    @Override
    public int getMaxParallel() {
        return Integer.MAX_VALUE;
    }

    @Override
    public void onStructureInvalid() {
        super.onStructureInvalid();
    }

    @Override
    public void onPartUnload() {
        super.onPartUnload();
    }

    /**
     * @return .
     */
    @Override
    public int getExtendlDuration() {
        return 5;
    }

    /**
     * @return .
     */
    @Override
    public int getExtendlThread() {
        return Integer.MAX_VALUE;
    }
}
