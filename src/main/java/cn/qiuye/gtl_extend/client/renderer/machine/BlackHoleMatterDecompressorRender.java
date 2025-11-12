package cn.qiuye.gtl_extend.client.renderer.machine;

import cn.qiuye.gtl_extend.GTL_Extend;
import cn.qiuye.gtl_extend.common.machine.multiblock.electric.BlackHoleMatterDecompressor;

import org.gtlcore.gtlcore.GTLCore;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiController;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiPart;
import com.gregtechceu.gtceu.client.renderer.machine.IControllerRenderer;
import com.gregtechceu.gtceu.client.renderer.machine.WorkableCasingMachineRenderer;

import com.lowdragmc.lowdraglib.client.bakedpipeline.FaceQuad;
import com.lowdragmc.lowdraglib.client.model.ModelFactory;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

public class BlackHoleMatterDecompressorRender extends WorkableCasingMachineRenderer implements IControllerRenderer {

    private static final ResourceLocation SPACE_MODEL = GTL_Extend.id("obj/space");
    private static final ResourceLocation BLACKHOLE_MODEL = GTL_Extend.id("obj/black_hole");

    public BlackHoleMatterDecompressorRender() {
        super(GTLCore.id("block/dimension_connection_casing"),
                GTCEu.id("block/multiblock/cosmos_simulation"));
    }

    private static void renderBlackHole(float tick, PoseStack poseStack, MultiBufferSource buffer) {
        Domain.pushPose(tick, poseStack, buffer, BLACKHOLE_MODEL);
    }

    /**
     *
     * @param blockEntity     .
     * @param partialTicks    .
     * @param poseStack       .
     * @param buffer          .
     * @param combinedLight   .
     * @param combinedOverlay .
     */
    @Override
    @OnlyIn(Dist.CLIENT)
    public void render(BlockEntity blockEntity,
                       float partialTicks,
                       PoseStack poseStack,
                       MultiBufferSource buffer,
                       int combinedLight,
                       int combinedOverlay) {
        if (blockEntity instanceof IMachineBlockEntity machineBlockEntity &&
                machineBlockEntity.getMetaMachine() instanceof BlackHoleMatterDecompressor machine &&
                machine.isActive()) {
            float tick = machine.getOffsetTimer() + partialTicks;
            adjustPositionByFacing(machine.getFrontFacing(), poseStack);
            renderBlackHole(tick, poseStack, buffer);
            renderOuterSpaceShell(poseStack, buffer);
            poseStack.popPose();
        }
    }

    // 根据机器朝向调整位置
    @OnlyIn(Dist.CLIENT)
    private void adjustPositionByFacing(Direction facing, PoseStack poseStack) {
        Domain.Facing(facing, poseStack);
    }

    private void renderOuterSpaceShell(PoseStack poseStack, MultiBufferSource buffer) {
        Domain.SpaceShell(poseStack, buffer, SPACE_MODEL);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void onAdditionalModel(Consumer<ResourceLocation> registry) {
        super.onAdditionalModel(registry);
        registry.accept(BLACKHOLE_MODEL);
    }

    /**
     * @param quads       .
     * @param machine     .
     * @param part        .
     * @param frontFacing .
     * @param side        .
     * @param rand        .
     * @param modelFacing .
     * @param modelState  .
     */
    @Override
    @OnlyIn(Dist.CLIENT)
    public void renderPartModel(List<BakedQuad> quads,
                                IMultiController machine,
                                IMultiPart part,
                                Direction frontFacing,
                                @Nullable Direction side,
                                RandomSource rand,
                                Direction modelFacing,
                                ModelState modelState) {
        if (side != null && modelFacing != null) {
            quads.add(FaceQuad.bakeFace(modelFacing, ModelFactory.getBlockSprite(GTCEu.id("block/casings/hpca/high_power_casing")),
                    modelState));
        }
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean hasTESR(BlockEntity blockEntity) {
        return true;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean isGlobalRenderer(BlockEntity blockEntity) {
        return true;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public int getViewDistance() {
        return 1024;
    }
}
