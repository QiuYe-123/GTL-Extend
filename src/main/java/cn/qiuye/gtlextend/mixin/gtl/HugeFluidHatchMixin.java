package cn.qiuye.gtlextend.mixin.gtl;

import org.gtlcore.gtlcore.common.machine.multiblock.part.HugeFluidHatchPartMachine;

import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableFluidTank;
import com.gregtechceu.gtceu.common.machine.multiblock.part.FluidHatchPartMachine;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = HugeFluidHatchPartMachine.class, remap = false)
public abstract class HugeFluidHatchMixin extends FluidHatchPartMachine {

    private HugeFluidHatchMixin(IMachineBlockEntity holder, int tier, IO io, long initialCapacity, int slots, Object... args) {
        super(holder, tier, io, initialCapacity, slots, args);
    }

    @Inject(
            method = "createTank",
            at = @At("HEAD"),
            cancellable = true,
            remap = false)
    protected void modify_createTank(long initialCapacity, int slots, Object[] args, CallbackInfoReturnable<NotifiableFluidTank> cir) {
        cir.setReturnValue(new NotifiableFluidTank((HugeFluidHatchPartMachine) (Object) this, slots, 2147483647L, this.io, IO.BOTH));
    }
}
