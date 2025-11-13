package cn.qiuye.gtlextend.data.recipe;

import cn.qiuye.gtlextend.common.data.GTL_Extend_Materials;
import cn.qiuye.gtlextend.config.GTLExtendConfigHolder;

import org.gtlcore.gtlcore.common.data.GTLMaterials;
import org.gtlcore.gtlcore.utils.Registries;

import com.gregtechceu.gtceu.api.data.chemical.material.Material;

import com.lowdragmc.lowdraglib.side.fluid.FluidStack;

import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.item.ItemStack;

import java.util.function.Consumer;

import static cn.qiuye.gtlextend.common.data.GTL_Extend_Materials.ETERNALBLUEDREAM;
import static cn.qiuye.gtlextend.common.data.GTL_Extend_RecipeTypes.VOID_PUMP_RECIPES;
import static com.gregtechceu.gtceu.api.GTValues.*;
import static com.gregtechceu.gtceu.common.data.GTMaterials.*;
import static com.gregtechceu.gtceu.common.data.GTRecipeTypes.EXTRACTOR_RECIPES;

public class VoidPumpRecipe {

    public static void init(Consumer<FinishedRecipe> provider) {
        ordinary(provider);
        if (GTLExtendConfigHolder.INSTANCE.enableGeneralAEManufacturingMachine) {
            VOID_PUMP_RECIPES.recipeBuilder("gtl_extend_fluid_fluix1")
                    .circuitMeta(32)
                    .outputFluids(GTL_Extend_Materials.FLUIXCRYSTAL.getFluid(1000))
                    .duration(500)
                    .EUt(VA[LuV])
                    .addData("CRTier", 2)
                    .save(provider);

            EXTRACTOR_RECIPES.recipeBuilder("gtl_extend_fluid_fluix")
                    .inputItems(Registries.getItem("ae2:fluix_dust"))
                    .outputFluids(GTL_Extend_Materials.FLUIXCRYSTAL.getFluid(144))
                    .duration(200)
                    .EUt(V[LV])
                    .save(provider);
        }
        if (GTLExtendConfigHolder.INSTANCE.enableInfinityDreamAndDreamHostCrafting) {
            VOID_PUMP_RECIPES.recipeBuilder("eternal_blue_dream_vein_fluid")
                    .circuitMeta(31)
                    .outputFluids(ETERNALBLUEDREAM.getFluid(1000))
                    .duration(500)
                    .EUt(VA[LuV])
                    .addData("CRTier", 2)
                    .save(provider);
        }
        VOID_PUMP_RECIPES.recipeBuilder("gtl_extend_fluid_fluix")
                .notConsumable(new ItemStack(Registries.getItem("kubejs:dust_cryotheum"), 64))
                .outputFluids(fluidGelid())
                .duration(500)
                .EUt(VA[LuV])
                .addData("CRTier", 2)
                .save(provider);
    }

    private static FluidStack fluidGelid() {
        return FluidStack.create(Registries.getFluid("kubejs:gelid_cryotheum"), 10000);
    }

    private static void ordinary(Consumer<FinishedRecipe> provider) {
        Object[][] recipes = {
                { "oil", 1, Oil },
                { "fluid_hafnium", 2, Hafnium },
                { "fluid_radon", 3, Radon },
                { "fluid_deuterium", 4, Deuterium },
                { "fluid_sulfuric_acid", 5, SulfuricAcid },
                { "fluid_neon", 6, Neon },
                { "fluid_krypton", 7, Krypton },
                { "fluid_xenon", 8, Xenon },
                { "fluid_coal_gas", 9, CoalGas },
                { "fluid_hydrochloric_acid", 10, HydrochloricAcid },
                { "fluid_chlorine", 11, Chlorine },
                { "fluid_fluorine", 12, Fluorine },
                { "unknowwater", 13, GTLMaterials.UnknowWater },
                { "fluid_nitric_acid", 14, NitricAcid }
        };

        for (Object[] recipe : recipes) {
            String name = (String) recipe[0];
            int circuit = (int) recipe[1];
            Material fluid = (Material) recipe[2];

            VOID_PUMP_RECIPES.recipeBuilder("gtl_extend_" + name)
                    .circuitMeta(circuit)
                    .outputFluids(fluid.getFluid(100_000))
                    .duration(500)
                    .EUt(V[EV])
                    .addData("CRTier", 1)
                    .save(provider);
        }
    }
}
