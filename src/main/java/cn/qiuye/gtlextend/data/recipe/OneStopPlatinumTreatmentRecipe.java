package cn.qiuye.gtlextend.data.recipe;

import org.gtlcore.gtlcore.common.data.GTLMaterials;
import org.gtlcore.gtlcore.utils.Registries;

import com.gregtechceu.gtceu.api.fluids.store.FluidStorageKeys;

import net.minecraft.data.recipes.FinishedRecipe;

import java.util.function.Consumer;

import static cn.qiuye.gtlextend.common.data.GTL_Extend_RecipeTypes.PLATINUM_BASE_DPROCESSING_HUB_RECIPES;
import static com.gregtechceu.gtceu.api.GTValues.LuV;
import static com.gregtechceu.gtceu.api.GTValues.VA;
import static com.gregtechceu.gtceu.common.data.GTMaterials.*;

public class OneStopPlatinumTreatmentRecipe {

    public static void init(Consumer<FinishedRecipe> provider) {
        PLATINUM_BASE_DPROCESSING_HUB_RECIPES.recipeBuilder("platinum_treatment")
                .inputItems(Registries.getItem("gtceu:platinum_group_sludge_dust"), 5000)
                .inputItems(Registries.getItem("gtceu:sulfur_dust"), 139)
                .inputFluids(Hydrogen.getFluid(625000))
                .inputFluids(Oxygen.getFluid(1111000))
                .inputFluids(Chlorine.getFluid(125000))
                .inputFluids(Fluorine.getFluid(1000))
                .outputItems(Registries.getItem("gtceu:platinum_dust"), 555)
                .outputItems(Registries.getItem("gtceu:palladium_dust"), 555)
                .outputItems(Registries.getItem("gtceu:ruthenium_dust"), 555)
                .outputItems(Registries.getItem("gtceu:iridium_dust"), 278)
                .outputItems(Registries.getItem("gtceu:osmium_dust"), 278)
                .outputItems(Registries.getItem("gtceu:rhodium_dust"), 417)
                .outputFluids(Hydrogen.getFluid(25000))
                .outputFluids(Chlorine.getFluid(56000))
                .outputFluids(Water.getFluid(62500))
                .duration(200)
                .EUt(VA[LuV])
                .save(provider);

        PLATINUM_BASE_DPROCESSING_HUB_RECIPES.recipeBuilder("platinum_treatment_1")
                .inputItems(Registries.getItem("gtceu:platinum_group_sludge_dust"), 5000)
                .inputItems(Registries.getItem("gtceu:sulfur_dust"), 139)
                .inputFluids(Hydrogen.getFluid(625000))
                .inputFluids(Oxygen.getFluid(1111000))
                .inputFluids(Chlorine.getFluid(125000))
                .inputFluids(Argon.getFluid(FluidStorageKeys.PLASMA, 1000))
                .outputItems(Registries.getItem("gtceu:platinum_dust"), 833)
                .outputItems(Registries.getItem("gtceu:palladium_dust"), 833)
                .outputItems(Registries.getItem("gtceu:ruthenium_dust"), 833)
                .outputItems(Registries.getItem("gtceu:iridium_dust"), 417)
                .outputItems(Registries.getItem("gtceu:osmium_dust"), 417)
                .outputItems(Registries.getItem("gtceu:rhodium_dust"), 625)
                .outputFluids(Hydrogen.getFluid(37500))
                .outputFluids(Chlorine.getFluid(84000))
                .outputFluids(Water.getFluid(93750))
                .duration(200)
                .EUt(VA[LuV])
                .save(provider);

        PLATINUM_BASE_DPROCESSING_HUB_RECIPES.recipeBuilder("platinum_treatment_2")
                .inputItems(Registries.getItem("gtceu:platinum_group_sludge_dust"), 5000)
                .inputItems(Registries.getItem("gtceu:sulfur_dust"), 139)
                .inputFluids(Hydrogen.getFluid(625000))
                .inputFluids(Oxygen.getFluid(1111000))
                .inputFluids(Chlorine.getFluid(125000))
                .inputFluids(Iron.getFluid(FluidStorageKeys.PLASMA, 1000))
                .outputItems(Registries.getItem("gtceu:platinum_dust"), 555 * 2)
                .outputItems(Registries.getItem("gtceu:palladium_dust"), 555 * 2)
                .outputItems(Registries.getItem("gtceu:ruthenium_dust"), 555 * 2)
                .outputItems(Registries.getItem("gtceu:iridium_dust"), 278 * 2)
                .outputItems(Registries.getItem("gtceu:osmium_dust"), 278 * 2)
                .outputItems(Registries.getItem("gtceu:rhodium_dust"), 417 * 2)
                .outputFluids(Hydrogen.getFluid(25000 * 2))
                .outputFluids(Chlorine.getFluid(56000 * 2))
                .outputFluids(Water.getFluid(62500 * 2))
                .duration(200)
                .EUt(VA[LuV])
                .save(provider);

        PLATINUM_BASE_DPROCESSING_HUB_RECIPES.recipeBuilder("platinum_treatment_3")
                .inputItems(Registries.getItem("gtceu:platinum_group_sludge_dust"), 5000)
                .inputItems(Registries.getItem("gtceu:sulfur_dust"), 139)
                .inputFluids(Hydrogen.getFluid(625000))
                .inputFluids(Oxygen.getFluid(1111000))
                .inputFluids(Chlorine.getFluid(125000))
                .inputFluids(Nickel.getFluid(FluidStorageKeys.PLASMA, 1000))
                .outputItems(Registries.getItem("gtceu:platinum_dust"), 555 * 3)
                .outputItems(Registries.getItem("gtceu:palladium_dust"), 555 * 3)
                .outputItems(Registries.getItem("gtceu:ruthenium_dust"), 555 * 3)
                .outputItems(Registries.getItem("gtceu:iridium_dust"), 278 * 3)
                .outputItems(Registries.getItem("gtceu:osmium_dust"), 278 * 3)
                .outputItems(Registries.getItem("gtceu:rhodium_dust"), 417 * 3)
                .outputFluids(Hydrogen.getFluid(25000 * 3))
                .outputFluids(Chlorine.getFluid(56000 * 3))
                .outputFluids(Water.getFluid(62500 * 3))
                .duration(200)
                .EUt(VA[LuV])
                .save(provider);

        PLATINUM_BASE_DPROCESSING_HUB_RECIPES.recipeBuilder("platinum_treatment_4")
                .inputItems(Registries.getItem("gtceu:platinum_group_sludge_dust"), 5000)
                .inputItems(Registries.getItem("gtceu:sulfur_dust"), 139)
                .inputFluids(Hydrogen.getFluid(625000))
                .inputFluids(Oxygen.getFluid(1111000))
                .inputFluids(Chlorine.getFluid(125000))
                .inputFluids(GTLMaterials.DegenerateRhenium.getFluid(FluidStorageKeys.PLASMA, 1000))
                .outputItems(Registries.getItem("gtceu:platinum_dust"), 555 * 5)
                .outputItems(Registries.getItem("gtceu:palladium_dust"), 555 * 5)
                .outputItems(Registries.getItem("gtceu:ruthenium_dust"), 555 * 5)
                .outputItems(Registries.getItem("gtceu:iridium_dust"), 278 * 5)
                .outputItems(Registries.getItem("gtceu:osmium_dust"), 278 * 5)
                .outputItems(Registries.getItem("gtceu:rhodium_dust"), 417 * 5)
                .outputFluids(Hydrogen.getFluid(25000 * 5))
                .outputFluids(Chlorine.getFluid(56000 * 5))
                .outputFluids(Water.getFluid(62500 * 5))
                .duration(200)
                .EUt(VA[LuV])
                .save(provider);
    }
}
