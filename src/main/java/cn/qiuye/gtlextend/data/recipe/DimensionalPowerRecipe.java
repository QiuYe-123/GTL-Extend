package cn.qiuye.gtlextend.data.recipe;

import org.gtlcore.gtlcore.common.data.GTLMaterials;
import org.gtlcore.gtlcore.utils.Registries;

import net.minecraft.data.recipes.FinishedRecipe;

import java.util.function.Consumer;

import static cn.qiuye.gtlextend.common.data.GTL_Extend_RecipeTypes.DIMENSIONALPOWER_RECIPES;

public class DimensionalPowerRecipe {

    public static void init(Consumer<FinishedRecipe> provider) {
        DIMENSIONALPOWER_RECIPES.recipeBuilder("dimensional_power")
                .inputItems(Registries.getItem("avaritia:singularity"))
                .inputFluids(GTLMaterials.ExcitedDtec.getFluid(1000))
                .outputItems(Registries.getItem("kubejs:infinity_antimatter_fuel_rod"))
                .outputFluids(GTLMaterials.DimensionallyTranscendentResplendentCatalyst.getFluid(1000))
                .duration(200)
                .save(provider);
    }
}
