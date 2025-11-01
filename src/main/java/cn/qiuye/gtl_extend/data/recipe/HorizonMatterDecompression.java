package cn.qiuye.gtl_extend.data.recipe;

import org.gtlcore.gtlcore.common.data.GTLMaterials;
import org.gtlcore.gtlcore.utils.Registries;

import com.gregtechceu.gtceu.api.fluids.store.FluidStorageKeys;

import net.minecraft.data.recipes.FinishedRecipe;

import java.util.function.Consumer;

import static cn.qiuye.gtl_extend.common.data.GTL_Extend_RecipeTypes.HORIZON_MATTER_DECOMPRESSION_RECIPES;
import static com.gregtechceu.gtceu.common.data.GTMaterials.*;
import static org.gtlcore.gtlcore.common.data.GTLMaterials.*;

public class HorizonMatterDecompression {

    public static void init(Consumer<FinishedRecipe> provider) {
        HORIZON_MATTER_DECOMPRESSION_RECIPES.recipeBuilder("horizon_matter_decompression")
                .inputItems(Registries.getItem("kubejs:quantum_chromodynamic_charge"))
                .inputFluids(GTLMaterials.CosmicElement.getFluid(1024000))
                .outputFluids(Argon.getFluid(FluidStorageKeys.PLASMA, 131072000),
                        Helium.getFluid(FluidStorageKeys.PLASMA, 131072000),
                        Iron.getFluid(FluidStorageKeys.PLASMA, 131072000),
                        Nickel.getFluid(FluidStorageKeys.PLASMA, 131072000),
                        Nitrogen.getFluid(FluidStorageKeys.PLASMA, 131072000),
                        Oxygen.getFluid(FluidStorageKeys.PLASMA, 131072000),
                        Silver.getFluid(FluidStorageKeys.PLASMA, 131072000),
                        Adamantium.getFluid(FluidStorageKeys.PLASMA, 131072000),
                        Vibranium.getFluid(FluidStorageKeys.PLASMA, 131072000),
                        Chaos.getFluid(FluidStorageKeys.PLASMA, 131072000),
                        Mithril.getFluid(FluidStorageKeys.PLASMA, 131072000),
                        Crystalmatrix.getFluid(FluidStorageKeys.PLASMA, 131072000),
                        Echoite.getFluid(FluidStorageKeys.PLASMA, 131072000),
                        Legendarium.getFluid(FluidStorageKeys.PLASMA, 131072000),
                        DraconiumAwakened.getFluid(FluidStorageKeys.PLASMA, 131072000),
                        Starmetal.getFluid(FluidStorageKeys.PLASMA, 131072000),
                        Orichalcum.getFluid(FluidStorageKeys.PLASMA, 131072000),
                        Infuscolium.getFluid(FluidStorageKeys.PLASMA, 131072000),
                        Enderium.getFluid(FluidStorageKeys.PLASMA, 131072000),
                        DegenerateRhenium.getFluid(FluidStorageKeys.PLASMA, 131072000),
                        HeavyQuarkDegenerateMatter.getFluid(FluidStorageKeys.PLASMA, 131072000),
                        MetastableHassium.getFluid(FluidStorageKeys.PLASMA, 131072000),
                        QuantumChromodynamicallyConfinedMatter.getFluid(FluidStorageKeys.PLASMA, 131072000),
                        AstralTitanium.getFluid(FluidStorageKeys.PLASMA, 131072000),
                        CelestialTungsten.getFluid(FluidStorageKeys.PLASMA, 131072000),
                        Quasifissioning.getFluid(FluidStorageKeys.PLASMA, 131072000),
                        Flyb.getFluid(FluidStorageKeys.PLASMA, 131072000),
                        TaraniumRichLiquidHelium4.getFluid(FluidStorageKeys.PLASMA, 131072000),
                        QuarkGluon.getFluid(FluidStorageKeys.PLASMA, 131072000),
                        DenseNeutron.getFluid(FluidStorageKeys.PLASMA, 131072000),
                        HighEnergyQuarkGluon.getFluid(FluidStorageKeys.PLASMA, 131072000),
                        RawStarMatter.getFluid(FluidStorageKeys.PLASMA, 131072000),
                        CosmicMesh.getFluid(FluidStorageKeys.PLASMA, 131072000),
                        ActiniumSuperhydride.getFluid(FluidStorageKeys.PLASMA, 131072000),
                        CosmicNeutronium.getFluid(131072000),
                        Eternity.getFluid(131072000))
                .duration(1200)
                .save(provider);
    }
}
