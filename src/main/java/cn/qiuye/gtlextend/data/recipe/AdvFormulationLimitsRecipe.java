package cn.qiuye.gtlextend.data.recipe;

import cn.qiuye.gtlextend.api.registries.GetRegistries;

import org.gtlcore.gtlcore.common.data.GTLMaterials;
import org.gtlcore.gtlcore.utils.Registries;

import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.data.recipe.VanillaRecipeHelper;

import net.minecraft.data.recipes.FinishedRecipe;

import java.util.function.Consumer;

import static cn.qiuye.gtlextend.common.data.GTL_Extend_Blocks.DIMENSION_CORE;
import static cn.qiuye.gtlextend.common.data.GTL_Extend_Blocks.VOID_WORLD_BLOCK;
import static com.gregtechceu.gtceu.api.GTValues.*;
import static com.gregtechceu.gtceu.common.data.GTMaterials.*;
import static com.gregtechceu.gtceu.common.data.GTRecipeTypes.ASSEMBLY_LINE_RECIPES;

public class AdvFormulationLimitsRecipe {

    public static void init(Consumer<FinishedRecipe> provider) {
        VanillaRecipeHelper.addShapedRecipe(provider, true, "void_world_block",
                VOID_WORLD_BLOCK.asStack(),
                "AAA",
                "ABA",
                "AAA",
                'A', GetRegistries.getItem("minecraft:obsidian"),
                'B', GetRegistries.getItem("minecraft:ender_pearl"));

        ASSEMBLY_LINE_RECIPES.recipeBuilder("dimension_core")
                .inputItems(VOID_WORLD_BLOCK, 64)
                .inputItems(Registries.getItem("gtceu:assembler_module"), 32)
                .inputItems(Registries.getItem("gtceu:resource_collection"), 32)
                .inputItems(Registries.getItem("gtceu:large_void_miner"), 32)
                .inputItems(Registries.getItem("gtceu:large_greenhouse"), 16)
                .inputItems(Registries.getItem("gtceu:large_incubator"), 16)
                .inputFluids(Dubnium.getFluid(16000))
                .inputFluids(Titanium.getFluid(16000))
                .inputFluids(GTLMaterials.MutatedLivingSolder.getFluid(16000))
                .inputFluids(SolderingAlloy.getFluid(16000))
                .outputItems(DIMENSION_CORE)
                .duration(1200)
                .EUt(V[ZPM])
                .stationResearch(b -> b.researchStack(VOID_WORLD_BLOCK.asStack())
                        .dataStack(GTItems.TOOL_DATA_MODULE.asStack())
                        .EUt(VA[ZPM])
                        .CWUt(1024))
                .save(provider);
    }
}
