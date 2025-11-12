package cn.qiuye.gtl_extend.common.machine.generator;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.TickableSubscription;
import com.gregtechceu.gtceu.api.machine.TieredEnergyMachine;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableEnergyContainer;

import com.mojang.blaze3d.MethodsReturnNonnullByDefault;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

import javax.annotation.ParametersAreNonnullByDefault;

import static com.gregtechceu.gtceu.api.GTValues.V;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class MagicEnergyMachine extends TieredEnergyMachine {

    @Nullable
    protected TickableSubscription energySubs;

    public MagicEnergyMachine(IMachineBlockEntity holder, int tier, Object... args) {
        super(holder, tier, args);
        this.energySubs = subscribeServerTick(energySubs, this::checkEnergy);
    }

    protected void checkEnergy() {
        if (getOffsetTimer() % 20 == 0 && !Objects.requireNonNull(getLevel()).getEntitiesOfClass(EndCrystal.class, AABB.ofSize(new Vec3(getPos().getX(), getPos().getY() + 1, getPos().getZ()), 1, 1, 1), e -> true).isEmpty()) {
            energyContainer.addEnergy(V[getTier() + 6]);
        }
    }

    @Override
    protected NotifiableEnergyContainer createEnergyContainer(Object... args) {
        long tierVoltage = V[getTier()];
        return NotifiableEnergyContainer.emitterContainer(this,
                tierVoltage * 2048L, tierVoltage, getMaxInputOutputAmperage());
    }

    @Override
    protected boolean isEnergyEmitter() {
        return true;
    }

    @Override
    protected long getMaxInputOutputAmperage() {
        return 64L;
    }
}
