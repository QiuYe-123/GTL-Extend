package cn.qiuye.gtlextend.mixin.gtl;

import cn.qiuye.gtlextend.api.machine.logic.MultipleRecipesLogic;

import org.gtlcore.gtlcore.api.recipe.BatchProcessing;

import com.gregtechceu.gtceu.api.machine.feature.IRecipeLogicMachine;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = BatchProcessing.class, remap = false, priority = 9999)
public abstract class BatchProcessingMixin {

    @Inject(method = "isCrossRecipeParallel", at = @At("HEAD"), cancellable = true)
    private static void gtlextend$detectMutableCrossRecipeParallel(
                                                                   IRecipeLogicMachine machine,
                                                                   CallbackInfoReturnable<Boolean> cir) {
        if (machine.getRecipeLogic() instanceof MultipleRecipesLogic) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "canConfigureBatchProcessing", at = @At("HEAD"), cancellable = true)
    private static void gtlextend$hideBatchConfiguratorForMutableCrossRecipeParallel(
                                                                                     IRecipeLogicMachine machine,
                                                                                     CallbackInfoReturnable<Boolean> cir) {
        if (machine.getRecipeLogic() instanceof MultipleRecipesLogic) {
            cir.setReturnValue(false);
        }
    }
}
