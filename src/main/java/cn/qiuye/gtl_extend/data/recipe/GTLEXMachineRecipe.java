package cn.qiuye.gtl_extend.data.recipe;

import cn.qiuye.gtl_extend.api.registries.GetRegistries;
import cn.qiuye.gtl_extend.common.data.GTL_Extend_Blocks;
import cn.qiuye.gtl_extend.common.data.GTL_Extend_Item;
import cn.qiuye.gtl_extend.common.data.machines.MultiBlockMachineA;
import cn.qiuye.gtl_extend.config.GTLExtendConfigHolder;

import org.gtlcore.gtlcore.api.data.tag.GTLTagPrefix;
import org.gtlcore.gtlcore.common.data.GTLItems;
import org.gtlcore.gtlcore.common.data.GTLMaterials;
import org.gtlcore.gtlcore.common.data.machines.AdvancedMultiBlockMachine;
import org.gtlcore.gtlcore.common.data.machines.GeneratorMachine;
import org.gtlcore.gtlcore.utils.Registries;

import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.data.recipe.CustomTags;
import com.gregtechceu.gtceu.data.recipe.VanillaRecipeHelper;

import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.function.Consumer;

import static cn.qiuye.gtl_extend.common.data.GTL_Extend_Blocks.DIMENSION_CORE;
import static com.gregtechceu.gtceu.api.GTValues.*;
import static com.gregtechceu.gtceu.common.data.GTMaterials.*;
import static com.gregtechceu.gtceu.common.data.GTRecipeTypes.ASSEMBLER_RECIPES;
import static com.gregtechceu.gtceu.common.data.GTRecipeTypes.ASSEMBLY_LINE_RECIPES;
import static org.gtlcore.gtlcore.common.data.GTLRecipeTypes.SUPRACHRONAL_ASSEMBLY_LINE_RECIPES;

public class GTLEXMachineRecipe {

    public static void init(Consumer<FinishedRecipe> provider) {
        Object[][] magicEnergyConfigs = new Object[][] {
                // 参数格式: [电压等级, 输出物品所在Mod的命名空间, 电缆材料名]
                { "lv", "gtceu", "tin", CustomTags.LV_CIRCUITS },
                { "mv", "gtl_extend", "copper", CustomTags.MV_CIRCUITS },
                { "hv", "gtl_extend", "gold", CustomTags.HV_CIRCUITS },
                { "ev", "gtl_extend", "aluminium", CustomTags.EV_CIRCUITS },
                { "iv", "gtl_extend", "graphene", CustomTags.IV_CIRCUITS },
                { "luv", "gtl_extend", "niobium_nitride", CustomTags.LuV_CIRCUITS },
                { "zpm", "gtl_extend", "naquadah", CustomTags.ZPM_CIRCUITS }
        };

        for (Object[] config : magicEnergyConfigs) {
            String tier = (String) config[0];
            String namespace = (String) config[1];
            String cableMaterial = (String) config[2];
            TagKey<Item> circuitTag = (TagKey<Item>) config[3];

            VanillaRecipeHelper.addShapedRecipe(
                    provider,
                    true,
                    tier + "_primitive_magic_energy",
                    new ItemStack(GetRegistries.getItem(namespace + ":" + tier + "_primitive_magic_energy")),
                    "ADA", "ABA", "CDC",
                    'A', GetRegistries.getItem("gtceu:" + tier + "_machine_casing"),
                    'B', GetRegistries.getItem("minecraft:end_crystal"),
                    'C', GetRegistries.getItem("gtceu:" + cableMaterial + "_single_cable"),
                    'D', circuitTag);
        }
        if (GTLExtendConfigHolder.INSTANCE.enableGeneralPurposeSteamEngine) {
            VanillaRecipeHelper.addShapedRecipe(provider, true, "the_general_steam_engine",
                    MultiBlockMachineA.GENERAL_PURPOSE_STEAM_ENGINE.asStack(),
                    "ABA",
                    "BCB",
                    "ABA",
                    'A', GetRegistries.getItem("gtceu:steam_machine_casing"),
                    'B', GetRegistries.getItem("gtl_extend:void_world_block"),
                    'C', GetRegistries.getItem("kubejs:precision_steam_mechanism"));
        }
        if (GTLExtendConfigHolder.INSTANCE.enableGeneralAEManufacturingMachine) {
            VanillaRecipeHelper.addShapedRecipe(provider, true, "general_ae_production",
                    MultiBlockMachineA.GENERAL_PURPOSE_AE_PRODUCTION.asStack(),
                    "AAA",
                    "ABA",
                    "AAA",
                    'A', GetRegistries.getItem("ae2:sky_stone_block"),
                    'B', CustomTags.EV_CIRCUITS);
        }

        ASSEMBLY_LINE_RECIPES.recipeBuilder("cattle_cattle_machine")
                .inputFluids(GTMaterials.Milk.getFluid(10000000000L))
                .outputItems(MultiBlockMachineA.CATTLE_CATTLE_MACHINE)
                .duration(20000)
                .EUt(V[LuV])
                .stationResearch(b -> b.researchStack(Registries.getItem("gtceu:void_fluid_drilling_rig").getDefaultInstance())
                        .dataStack(GTItems.TOOL_DATA_MODULE.asStack())
                        .EUt(VA[LuV])
                        .CWUt(128))
                .save(provider);

        ASSEMBLER_RECIPES.recipeBuilder("superfluid_general_energy_furnace")
                .inputItems(Registries.getItem("gtceu:electric_blast_furnace"), 8)
                .inputItems(Registries.getItem("gtceu:alloy_blast_smelter"), 8)
                .inputItems(Registries.getItem("gtceu:multi_smelter"), 8)
                .inputItems(Registries.getItem("gtceu:ptfe_pipe_casing"), 64)
                .inputItems(Registries.getItem("gtceu:heat_vent"), 64)
                .inputItems(Registries.getItem("gtceu:high_temperature_smelting_casing"), 64)
                .inputFluids(SolderingAlloy.getFluid(16000))
                .outputItems(MultiBlockMachineA.SUPERFLUID_GENERAL_ENERGY_FURNACE)
                .duration(4096)
                .EUt(V[UHV])
                .save(provider);
        ASSEMBLY_LINE_RECIPES.recipeBuilder("platinum_based_processing_hub")
                .inputItems(Registries.getItem("gtceu:chemical_plant"), 16)
                .inputItems(Registries.getItem("gtceu:large_distillery"), 4)
                .inputItems(Registries.getItem("gtceu:large_sifting_funnel"), 4)
                .inputItems(Registries.getItem("gtceu:large_pyrolyse_oven"), 4)
                .inputItems(CustomTags.UV_CIRCUITS, 32)
                .inputItems(Registries.getItem("gtceu:luv_robot_arm"), 32)
                .inputItems(Registries.getItem("gtceu:ruridit_frame"), 64)
                .inputItems(Registries.getItem("gtceu:double_ruthenium_plate"), 32)
                .inputItems(Registries.getItem("gtceu:double_rhodium_plate"), 32)
                .inputFluids(Ruthenium.getFluid(32000))
                .inputFluids(Rhodium.getFluid(32000))
                .inputFluids(Iridium.getFluid(32000))
                .inputFluids(Osmium.getFluid(32000))
                .outputItems(MultiBlockMachineA.PLATINUM_BASE_DPROCESSING_HUB)
                .duration(6400)
                .EUt(V[LuV])
                .stationResearch(b -> b.researchStack(Registries.getItem("gtceu:isa_mill").getDefaultInstance())
                        .dataStack(GTItems.TOOL_DATA_MODULE.asStack())
                        .EUt(VA[LuV])
                        .CWUt(128))
                .save(provider);

        SUPRACHRONAL_ASSEMBLY_LINE_RECIPES.recipeBuilder("black_hole_matter_decompressor")
                .inputItems(Registries.getItem("gtlcore:dimension_injection_casing"), 64)
                .inputItems(Registries.getItem("kubejs:spacetime_compression_field_generator"), 64)
                .inputItems(Registries.getItem("kubejs:dimensional_stability_casing"), 64)
                .inputItems(Registries.getItem("gtladditions:fuxi_bagua_heaven_forging_furnace"), 4)
                .inputItems(Registries.getItem("gtladditions:arcanic_astrograph"), 8)
                .inputItems(Registries.getItem("gtceu:eye_of_harmony"), 64)
                .inputItems(Registries.getItem("kubejs:ctc_computational_unit"), 64)
                .inputItems(Registries.getItem("kubejs:stabilized_wormhole_generator"), 64)
                .inputItems(CustomTags.MAX_CIRCUITS, 32)
                .inputItems(Registries.getItem("gtceu:eternity_nanoswarm"), 8)
                .inputItems(Registries.getItem("gtlcore:max_emitter"), 64)
                .inputItems(Registries.getItem("gtlcore:max_sensor"), 64)
                .inputItems(Registries.getItem("gtlcore:max_robot_arm"), 64)
                .inputItems(Registries.getItem("kubejs:time_dilation_containment_unit"), 64)
                .inputItems(DIMENSION_CORE, 8)
                .inputItems(Registries.getItem("gtceu:double_chaos_plate"), 64)
                .inputFluids(GTLMaterials.SuperMutatedLivingSolder.getFluid(480000))
                .inputFluids(GTLMaterials.DegenerateRhenium.getFluid(100000))
                .inputFluids(Neutronium.getFluid(100000))
                .inputFluids(GTLMaterials.Infinity.getFluid(16000))
                .outputItems(MultiBlockMachineA.BLACK_HOLE_MATTER_DECOMPRESSOR)
                .duration(4400)
                .EUt(V[MAX] * 16384)
                .stationResearch(b -> b.researchStack(Registries.getItem("gtladditions:arcanic_astrograph").getDefaultInstance())
                        .dataStack(GTL_Extend_Item.ADVANCED_DATA_MODULE.asStack())
                        .EUt(VA[MAX])
                        .CWUt(16384))
                .save(provider);

        SUPRACHRONAL_ASSEMBLY_LINE_RECIPES.recipeBuilder("dimensionalpower")
                .inputItems(GeneratorMachine.ANNIHILATE_GENERATOR, 4)
                .inputItems(GeneratorMachine.ADVANCED_HYPER_REACTOR, 16)
                .inputItems(GeneratorMachine.DYSON_SPHERE, 16)
                .inputItems(GeneratorMachine.HYPER_REACTOR, 64)
                .inputItems(GeneratorMachine.LARGE_NAQUADAH_REACTOR, 64)
                .inputItems(Registries.getItem("kubejs:suprachronal_mainframe_complex"), 64)
                .inputItems(GeneratorMachine.GENERATOR_ARRAY, 64)
                .inputItems(GTLItems.ELECTRIC_MOTOR_MAX, 64)
                .inputItems(GTLItems.ELECTRIC_PUMP_MAX, 64)
                .inputItems(GTLItems.CONVEYOR_MODULE_MAX, 64)
                .inputItems(GTLItems.ROBOT_ARM_MAX, 64)
                .inputItems(GTLItems.ELECTRIC_PISTON_MAX, 64)
                .inputItems(GTLItems.FIELD_GENERATOR_MAX, 64)
                .inputItems(GTLItems.EMITTER_MAX, 64)
                .inputItems(GTLItems.SENSOR_MAX, 64)
                .inputItems(Registries.getItem("avaritia:singularity"), 64)
                .inputFluids(GTLMaterials.DimensionallyTranscendentCrudeCatalyst.getFluid(1000000))
                .inputFluids(GTLMaterials.DimensionallyTranscendentProsaicCatalyst.getFluid(1000000))
                .inputFluids(GTLMaterials.DimensionallyTranscendentResplendentCatalyst.getFluid(1000000))
                .inputFluids(GTLMaterials.DimensionallyTranscendentExoticCatalyst.getFluid(1000000))
                .outputItems(MultiBlockMachineA.DIMENSIONALPOWER)
                .duration(4400)
                .EUt(V[MAX] * 16384)
                .stationResearch(b -> b.researchStack(GeneratorMachine.ANNIHILATE_GENERATOR.asStack())
                        .dataStack(GTL_Extend_Item.ADVANCED_DATA_MODULE.asStack())
                        .EUt(VA[MAX])
                        .CWUt(16384))
                .save(provider);

        SUPRACHRONAL_ASSEMBLY_LINE_RECIPES.recipeBuilder("dimensional_stabilization_core")
                .inputItems(GTL_Extend_Blocks.VOID_WORLD_BLOCK, 64)
                .inputItems(org.gtlcore.gtlcore.common.data.machines.MultiBlockMachineB.DISSOLVING_TANK, 64)
                .inputItems(GTItems.TOOL_DATA_MODULE, 64)
                .inputItems(Registries.getItem("kubejs:uiv_universal_circuit"), 64)
                .inputItems(GTItems.ELECTRIC_PUMP_UEV, 64)
                .inputItems(GTItems.FLUID_REGULATOR_UEV, 64)
                .inputItems(GTItems.ROBOT_ARM_UEV, 64)
                .inputItems(Registries.getItem("kubejs:reinforced_echo_shard"), 64)
                .inputItems(Registries.getItem("kubejs:time_dilation_containment_unit"), 64)
                .inputItems(Registries.getItem("kubejs:aggregatione_core"), 64)
                .inputItems(ChemicalHelper.get(GTLTagPrefix.nanoswarm, Neutronium), 32)
                .inputItems(org.gtlcore.gtlcore.common.data.machines.MultiBlockMachineA.CHEMICAL_DISTORT, 16)
                .inputItems(AdvancedMultiBlockMachine.SPACE_ELEVATOR, 8)
                .inputItems(Registries.getItem("kubejs:stabilizer_core"), 4)
                .inputItems(org.gtlcore.gtlcore.common.data.machines.MultiBlockMachineA.SLAUGHTERHOUSE, 1)
                .inputItems(GTL_Extend_Blocks.DIMENSION_CORE, 1)
                .inputFluids(GTLMaterials.Zylon.getFluid(9216))
                .inputFluids(GTLMaterials.UuAmplifier.getFluid(1000000))
                .inputFluids(GTLMaterials.MutatedLivingSolder.getFluid(1000000))
                .inputFluids(GTLMaterials.SuperMutatedLivingSolder.getFluid(1000000))
                .outputItems(MultiBlockMachineA.DIMENSIONALLY_TRANSCENDENT_DISSOLVING_TANK)
                .duration(4400)
                .EUt(V[MAX] * 16384)
                .stationResearch(b -> b.researchStack(org.gtlcore.gtlcore.common.data.machines.MultiBlockMachineB.DISSOLVING_TANK.asStack())
                        .dataStack(GTL_Extend_Item.ADVANCED_DATA_MODULE.asStack())
                        .EUt(VA[MAX])
                        .CWUt(16384))
                .save(provider);
    }
}
