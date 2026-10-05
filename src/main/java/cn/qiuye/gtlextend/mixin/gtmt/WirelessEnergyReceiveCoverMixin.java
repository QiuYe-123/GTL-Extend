package cn.qiuye.gtlextend.mixin.gtmt;

import org.gtlcore.gtlcore.common.machine.multiblock.part.NeutronAcceleratorPartMachine;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.capability.GTCapabilityHelper;
import com.gregtechceu.gtceu.api.capability.ICoverable;
import com.gregtechceu.gtceu.api.capability.IEnergyContainer;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.cover.CoverBehavior;
import com.gregtechceu.gtceu.api.cover.CoverDefinition;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.common.machine.electric.BatteryBufferMachine;
import com.gregtechceu.gtceu.common.machine.electric.HullMachine;
import com.gregtechceu.gtceu.common.machine.multiblock.part.EnergyHatchPartMachine;

import com.hepdd.gtmthings.api.misc.WirelessEnergyManager;
import com.hepdd.gtmthings.common.cover.WirelessEnergyReceiveCover;
import net.minecraft.core.Direction;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.UUID;

@Mixin(value = WirelessEnergyReceiveCover.class, priority = 114514, remap = false)
public abstract class WirelessEnergyReceiveCoverMixin extends CoverBehavior {

    @Shadow
    private UUID uuid;

    @Shadow
    @Final
    private long energyPerTick;

    @Shadow
    @Final
    private int tier;

    @Shadow
    @Final
    private int amperage;

    @Shadow
    private long machineMaxEnergy;

    private WirelessEnergyReceiveCoverMixin(CoverDefinition definition, ICoverable coverHolder, Direction attachedSide) {
        super(definition, coverHolder, attachedSide);
    }

    @Inject(method = "canAttach", at = @At("TAIL"), cancellable = true)
    private void gtlextend$canAttach(CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValue())
            return;
        MetaMachine machine = MetaMachine.getMachine(this.coverHolder.getLevel(), this.coverHolder.getPos());
        if (machine instanceof EnergyHatchPartMachine energyHatch && energyHatch.energyContainer.getHandlerIO() == IO.IN)
            cir.setReturnValue(energyHatch.getTier() >= this.tier);
    }

    @Shadow
    protected abstract void updateCoverSub();

    /**
     * @author
     * @reason
     */
    @Overwrite
    private void updateEnergy() {
        if (this.uuid == null) return;
        IEnergyContainer energyContainer = GTCapabilityHelper.getEnergyContainer(coverHolder.getLevel(), coverHolder.getPos(), attachedSide);
        if (energyContainer != null) {
            MetaMachine machine = MetaMachine.getMachine(coverHolder.getLevel(), coverHolder.getPos());
            if (machine instanceof BatteryBufferMachine || machine instanceof HullMachine || (machine instanceof NeutronAcceleratorPartMachine neutronAccelerator && neutronAccelerator.isWorkingEnabled()) || machine instanceof EnergyHatchPartMachine) {
                long changeStored = Math.min(energyContainer.getEnergyCapacity() - energyContainer.getEnergyStored(), this.energyPerTick);
                if (changeStored <= 0L)
                    return;

                if (!WirelessEnergyManager.addEUToGlobalEnergyMap(this.uuid, -changeStored, machine))
                    return;

                energyContainer.acceptEnergyFromNetwork(machine instanceof EnergyHatchPartMachine energyHatch ? energyHatch.getFrontFacing() : null, GTValues.V[this.tier], this.amperage);
            } else {
                long changeStored = Math.min(this.machineMaxEnergy - energyContainer.getEnergyStored(), this.energyPerTick);
                if (changeStored <= 0L)
                    return;

                if (!WirelessEnergyManager.addEUToGlobalEnergyMap(this.uuid, -changeStored, machine))
                    return;

                energyContainer.addEnergy(changeStored);
            }
        }

        this.updateCoverSub();
    }
}
