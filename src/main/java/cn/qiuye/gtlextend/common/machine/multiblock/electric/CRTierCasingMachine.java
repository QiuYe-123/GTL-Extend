package cn.qiuye.gtlextend.common.machine.multiblock.electric;

import cn.qiuye.gtlextend.config.GTLExtendConfigHolder;

import org.gtlcore.gtlcore.common.data.GTLRecipeModifiers;

import com.gtladd.gtladditions.api.machine.IWirelessThreadModifierParallelMachine;
import com.gtladd.gtladditions.api.machine.feature.IThreadModifierPart;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableElectricMultiblockMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableMultiblockMachine;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;

import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class CRTierCasingMachine extends WorkableElectricMultiblockMachine implements IWirelessThreadModifierParallelMachine {

    protected @Nullable IThreadModifierPart threadPartMachine = null;

    public void setThreadPartMachine(@Nullable IThreadModifierPart threadPartMachine) {
        this.threadPartMachine = threadPartMachine;
    }

    public int getAdditionalThread() {
        if (GTLExtendConfigHolder.INSTANCE.ThreadsADD()) {
            return threadPartMachine != null ? threadPartMachine.getThreadCount() : 0;
        } else return Integer.MAX_VALUE;
    }

    public static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            CRTierCasingMachine.class, WorkableMultiblockMachine.MANAGED_FIELD_HOLDER);

    private final String tierType;

    @Persisted
    private int tier = 0;

    public CRTierCasingMachine(IMachineBlockEntity holder, String tierType, Object... args) {
        super(holder, args);
        this.tierType = tierType;
    }

    @Override
    public void onStructureFormed() {
        super.onStructureFormed();
        tier = getMultiblockState().getMatchContext().get(tierType);
    }

    @Override
    public void onStructureInvalid() {
        super.onStructureInvalid();
        this.threadPartMachine = null;
        tier = 0;
    }

    @Override
    public void onPartUnload() {
        super.onPartUnload();
        this.threadPartMachine = null;
    }

    @Override
    public boolean beforeWorking(@Nullable GTRecipe recipe) {
        if (recipe != null && recipe.data.contains(tierType) && recipe.data.getInt(tierType) > tier) {
            getRecipeLogic().interruptRecipe();
            return false;
        }
        return super.beforeWorking(recipe);
    }

    @Override
    public void addDisplayText(List<Component> textList) {
        super.addDisplayText(textList);
        if (!this.isFormed) return;
        textList.add(Component.translatable("gtceu.casings.tier", tier));
    }

    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    @Override
    public int getMaxParallel() {
        return GTLRecipeModifiers.getHatchParallel(this);
    }
}
