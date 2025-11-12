package cn.qiuye.gtl_extend.client.renderer.machine;

import org.gtlcore.gtlcore.client.ClientUtil;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.data.ModelData;
import org.joml.Quaternionf;

public class Domain {

    static void Facing(Direction facing, PoseStack poseStack) {
        double x = 0.5, y = 30.5, z = 0.5;
        switch (facing) {
            case NORTH -> z = 5.5;
            case SOUTH -> z = -5.5;
            case WEST -> x = 5.5;
            case EAST -> x = -5.5;
        }
        poseStack.pushPose();
        poseStack.translate(x, y, z);
    }

    static void pushPose(float tick, PoseStack poseStack, MultiBufferSource buffer, ResourceLocation Model) {
        poseStack.pushPose();
        poseStack.scale(25.0F, 25.0F, 25.0F);
        poseStack.mulPose(new Quaternionf().fromAxisAngleDeg(0F, 1F, 0F, (tick * 2) % 360F));
        ClientUtil.modelRenderer().renderModel(
                poseStack.last(),
                buffer.getBuffer(RenderType.translucent()),
                null,
                ClientUtil.getBakedModel(Model),
                1.0F, 1.0F, 1.0F,
                LightTexture.FULL_BRIGHT,
                OverlayTexture.NO_OVERLAY,
                ModelData.EMPTY,
                RenderType.translucent());
        poseStack.popPose();
    }

    static void SpaceShell(PoseStack poseStack, MultiBufferSource buffer, ResourceLocation spaceModel) {
        float scale = 0.01F * 17.5F;
        poseStack.pushPose();
        poseStack.scale(scale, scale, scale);
        ClientUtil.modelRenderer().renderModel(
                poseStack.last(),
                buffer.getBuffer(RenderType.solid()),
                null,
                ClientUtil.getBakedModel(spaceModel),
                1.0F, 1.0F, 1.0F,
                LightTexture.FULL_BRIGHT,
                OverlayTexture.NO_OVERLAY,
                ModelData.EMPTY,
                RenderType.solid());
        poseStack.popPose();
    }
}
