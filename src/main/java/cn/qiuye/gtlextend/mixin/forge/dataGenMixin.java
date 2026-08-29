package cn.qiuye.gtlextend.mixin.forge;

import cn.qiuye.gtlextend.data.GTLEXDatagen;

import net.minecraftforge.data.event.GatherDataEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GatherDataEvent.DataGeneratorConfig.class)
public class dataGenMixin {

    @Inject(method = "runAll", at = @At("HEAD"), remap = false, require = 1)
    private void injectProvider(CallbackInfo ci) {
        GTLEXDatagen.initPost();
    }
}
