package cn.qiuye.gtlextend.data.recipe;

import cn.qiuye.gtlextend.GTL_Extend;

import com.gtladd.gtladditions.common.items.GTLAddItems;

import com.gregtechceu.gtceu.common.data.GTItems;

import appeng.core.definitions.AEItems;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.level.block.Blocks;

import java.util.function.Consumer;

import static cn.qiuye.gtlextend.common.data.GTL_Extend_RecipeTypes.STABLE_SPACETIME_PRODUCTION_RECIPES;
import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.*;
import static com.gregtechceu.gtceu.common.data.GTMaterials.Stone;
import static org.gtlcore.gtlcore.api.data.tag.GTLTagPrefix.*;
import static org.gtlcore.gtlcore.common.data.GTLMaterials.*;

public class StableSpacetimeProduction {

    public static void init(Consumer<FinishedRecipe> provider) {
        STABLE_SPACETIME_PRODUCTION_RECIPES.recipeBuilder(GTL_Extend.id("ae_singularity_compression"))
                .inputItems(Blocks.COBBLESTONE.asItem(), 256000)
                .outputItems(AEItems.SINGULARITY.stack())
                .duration(1)
                .save(provider);
        STABLE_SPACETIME_PRODUCTION_RECIPES.recipeBuilder(GTL_Extend.id("bedrock_dust_compression"))
                .inputItems(dust, Stone, 131072)
                .outputItems(dust, Bedrock)
                .duration(320)
                .save(provider);
        STABLE_SPACETIME_PRODUCTION_RECIPES.recipeBuilder(GTL_Extend.id("magmatter_block_solidification"))
                .notConsumable(GTItems.SHAPE_MOLD_BLOCK)
                .inputFluids(Magmatter.getFluid(129600))
                .outputItems(block, Magmatter)
                .duration(20)
                .save(provider);
        STABLE_SPACETIME_PRODUCTION_RECIPES.recipeBuilder(GTL_Extend.id("magmatter_rod_extrusion"))
                .notConsumable(GTItems.SHAPE_EXTRUDER_ROD)
                .inputItems(ingot, Magmatter, 50)
                .outputItems(rod, Magmatter)
                .duration(200)
                .save(provider);
        STABLE_SPACETIME_PRODUCTION_RECIPES.recipeBuilder(GTL_Extend.id("astral_array_compression"))
                .inputItems(GTLAddItems.INSTANCE.getBLACK_HOLE_SEED(), 144)
                .inputItems(nanoswarm, Eternity, 64)
                .inputItems(nanoswarm, SpaceTime, 64)
                .inputItems(Blocks.REPEATING_COMMAND_BLOCK.asItem(), 64)
                .inputItems(GTLAddItems.INSTANCE.getASTRAL_ARRAY(), 2150400)
                .inputFluids(Miracle.getFluid(576000))
                .outputItems(GTLAddItems.INSTANCE.getCOMPRESSED_ASTRAL_ARRAY(), 21)
                .duration(600)
                .save(provider);
    }
}
