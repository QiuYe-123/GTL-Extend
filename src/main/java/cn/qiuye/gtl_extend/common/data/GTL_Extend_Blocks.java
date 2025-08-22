package cn.qiuye.gtl_extend.common.data;

import cn.qiuye.gtl_extend.GTL_Extend;

import com.gregtechceu.gtceu.api.item.tool.GTToolType;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import static cn.qiuye.gtl_extend.api.registries.GTLEXRegistration.REGISTRATE;

import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.nullness.NonNullSupplier;

public class GTL_Extend_Blocks {

    public static Map<Integer, Supplier<Block>> crmap = new HashMap<>();

    /**
     * 创建带有自定义属性的技术类方块（如机器外壳）
     */
    public static final BlockEntry<Block> DIMENSION_CORE = createCasingBlock(
            "dimension_core",
            GTL_Extend.id("block/dimension_core"),
            () -> RenderType::solid,
            () -> Blocks.NETHERITE_BLOCK,
            crmap, 2);
    public static final BlockEntry<Block> VOID_WORLD_BLOCK = createCasingBlock(
            "void_world_block",
            GTL_Extend.id("block/void_world_block"),
            () -> RenderType::cutoutMipped,
            () -> Blocks.NETHERITE_BLOCK,
            crmap, 1);

    public static void init() {}

    /**
     * @param name           .
     * @param texture        .
     * @param renderType     .
     * @param baseProperties .
     * @return 方块注册对象
     */
    public static BlockEntry<Block> createCasingBlock(
                                                      String name,
                                                      ResourceLocation texture,
                                                      Supplier<Supplier<RenderType>> renderType,
                                                      NonNullSupplier<? extends Block> baseProperties) {                              // 新增参数：层级标识)
        BlockEntry<Block> blockEntry = REGISTRATE.block(name, Block::new)
                .initialProperties(baseProperties)
                .properties(p -> p
                        .strength(4.0f, 1000.0f)
                        .sound(SoundType.METAL)
                        .noOcclusion()
                        .isValidSpawn((state, level, pos, entity) -> false)
                        .requiresCorrectToolForDrops())
                .addLayer(renderType)
                .blockstate((ctx, prov) -> prov.simpleBlock(ctx.getEntry(), prov.models().getExistingFile(
                        GTL_Extend.id("block/" + ctx.getName()) // 直接加载自定义模型
                )))
                .tag(GTToolType.WRENCH.harvestTags.get(0), BlockTags.MINEABLE_WITH_PICKAXE)
                .item(BlockItem::new)
                .model((ctx, prov) -> prov.withExistingParent(ctx.getName(), "gtl_extend:block/" + name))
                .build()
                .register();
        REGISTRATE.setCreativeTab(blockEntry, GTL_Extend_CreativeModeTabs.BLOCKS_ITEM);
        return blockEntry;
    }

    /**
     *
     * @param name           .
     * @param texture        .
     * @param renderType     .
     * @param baseProperties .
     * @param targetMap      .
     * @param tier           .
     * @return 方块注册对象
     */
    public static BlockEntry<Block> createCasingBlock(
                                                      String name,
                                                      ResourceLocation texture,
                                                      Supplier<Supplier<RenderType>> renderType,
                                                      NonNullSupplier<? extends Block> baseProperties,
                                                      Map<Integer, Supplier<Block>> targetMap,
                                                      int tier) {
        BlockEntry<Block> blockEntry = createCasingBlock(name, texture, renderType, baseProperties);
        targetMap.put(tier, blockEntry);
        return blockEntry;
    }
}
