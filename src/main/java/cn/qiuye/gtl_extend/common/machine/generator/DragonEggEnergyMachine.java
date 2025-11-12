package cn.qiuye.gtl_extend.common.machine.generator;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.TickableSubscription;
import com.gregtechceu.gtceu.api.machine.TieredEnergyMachine;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableEnergyContainer;

import com.mojang.blaze3d.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DragonEggBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

import javax.annotation.ParametersAreNonnullByDefault;

import static com.gregtechceu.gtceu.api.GTValues.VEX;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class DragonEggEnergyMachine extends TieredEnergyMachine {

    @Nullable
    protected TickableSubscription energySubs;

    public DragonEggEnergyMachine(IMachineBlockEntity holder, int tier, Object... args) {
        super(holder, tier, args);
        this.energySubs = subscribeServerTick(energySubs, this::checkEnergy);
    }

    protected void checkEnergy() {
        if (getOffsetTimer() % 20 == 0) {
            Level level = Objects.requireNonNull(getLevel());
            BlockPos posAbove = getPos().above(); // 获取当前位置上方的位置
            BlockState blockStateAbove = level.getBlockState(posAbove); // 获取该位置的方块状态
            if (blockStateAbove.getBlock() instanceof DragonEggBlock) {
                energyContainer.addEnergy(VEX[getTier() + 6]);
            }
        }
    }

    @Override
    protected NotifiableEnergyContainer createEnergyContainer(Object... args) {
        long tierVoltage = VEX[getTier()];
        return NotifiableEnergyContainer.emitterContainer(this,
                tierVoltage * 16384L, tierVoltage, getMaxInputOutputAmperage());
    }

    @Override
    protected boolean isEnergyEmitter() {
        return true;
    }

    @Override
    protected long getMaxInputOutputAmperage() {
        return 256L;
    }
}
