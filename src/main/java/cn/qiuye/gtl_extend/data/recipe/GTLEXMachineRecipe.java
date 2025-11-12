package cn.qiuye.gtl_extend.data.recipe;

import cn.qiuye.gtl_extend.api.registries.GetRegistries;
import cn.qiuye.gtl_extend.common.data.GTL_Extend_Blocks;
import cn.qiuye.gtl_extend.common.data.GTL_Extend_Item;
import cn.qiuye.gtl_extend.common.data.machines.MultiBlockMachineA;
import cn.qiuye.gtl_extend.config.GTLExtendConfigHolder;

import org.gtlcore.gtlcore.api.data.tag.GTLTagPrefix;
import org.gtlcore.gtlcore.common.data.GTLBlocks;
import org.gtlcore.gtlcore.common.data.GTLItems;
import org.gtlcore.gtlcore.common.data.GTLMaterials;
import org.gtlcore.gtlcore.common.data.machines.AdvancedMultiBlockMachine;
import org.gtlcore.gtlcore.common.data.machines.GeneratorMachine;
import org.gtlcore.gtlcore.utils.Registries;

import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.fluids.store.FluidStorageKeys;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.common.data.machines.GTResearchMachines;
import com.gregtechceu.gtceu.data.recipe.CustomTags;
import com.gregtechceu.gtceu.data.recipe.VanillaRecipeHelper;

import appeng.core.definitions.AEBlocks;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.function.Consumer;

import static cn.qiuye.gtl_extend.common.data.GTL_Extend_Blocks.DIMENSION_CORE;
import static com.gregtechceu.gtceu.api.GTValues.*;
import static com.gregtechceu.gtceu.common.data.GTMaterials.*;
import static com.gregtechceu.gtceu.common.data.GTRecipeTypes.*;
import static org.gtlcore.gtlcore.common.data.GTLMaterials.*;
import static org.gtlcore.gtlcore.common.data.GTLRecipeTypes.SUPRACHRONAL_ASSEMBLY_LINE_RECIPES;
import static org.gtlcore.gtlcore.common.data.machines.MultiBlockMachineA.*;

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

        ASSEMBLER_RECIPES.recipeBuilder("the_steam_integrated_ore_processing_center")
                .inputItems(LARGE_STEAM_MACERATOR, 64)
                .inputItems(LARGE_STEAM_BATH, 64)
                .inputItems(LARGE_STEAM_THERMAL_CENTRIFUGE, 64)
                .inputItems(GetRegistries.getItem("kubejs:precision_steam_mechanism"), 64)
                .inputItems(GetRegistries.getItem("kubejs:precision_steam_mechanism"), 64)
                .inputItems(GTItems.ELECTRONIC_CIRCUIT_LV, 64)
                .inputFluids(GTMaterials.SolderingAlloy.getFluid(64000))
                .outputItems(MultiBlockMachineA.STEAM_INTEGRATED_ORE_PROCESSING_CENTER)
                .duration(20000)
                .EUt(VA[LV])
                .save(provider);

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

        ASSEMBLY_LINE_RECIPES.recipeBuilder("platinum_based_rocessing_hub")
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

        ASSEMBLY_LINE_RECIPES.recipeBuilder("quantum_computer")
                .inputItems(AEBlocks.CONDENSER.stack())
                .inputItems(GTLBlocks.CRAFTING_STORAGE_MAX, 10)
                .inputItems(GTResearchMachines.HPCA_BRIDGE_COMPONENT, 10)
                .inputItems(GTItems.QUANTUM_MAINFRAME_ZPM, 64)
                .inputItems(AEBlocks.CREATIVE_ENERGY_CELL.stack(), 10)
                .inputItems(Registries.getItem("mae2:256x_crafting_accelerator"), 10)
                .inputItems(GTItems.QUBIT_CENTRAL_PROCESSING_UNIT, 64)
                .inputItems(GTItems.QUANTUM_EYE, 64)
                .inputItems(GTItems.QUANTUM_STAR)
                .inputItems(Registries.getItem("kubejs:optical_mainframe"), 64)
                .inputItems(ChemicalHelper.get(GTLTagPrefix.nanoswarm, Neutronium), 10)
                .inputItems(GTItems.FIELD_GENERATOR_UEV, 64)
                .inputItems(GTResearchMachines.HPCA_ADVANCED_COMPUTATION_COMPONENT, 64)
                .inputItems(GTResearchMachines.HIGH_PERFORMANCE_COMPUTING_ARRAY, 10)
                .inputItems(GTItems.EMITTER_UEV, 64)
                .inputItems(GTResearchMachines.HPCA_ACTIVE_COOLER_COMPONENT, 64)
                .inputFluids(Naquadria.getFluid(10000))
                .inputFluids(Orichalcum.getFluid(FluidStorageKeys.PLASMA, 6000))
                .inputFluids(Lawrencium.getFluid(1000))
                .inputFluids(Nobelium.getFluid(3000))
                .outputItems(MultiBlockMachineA.QUANTUM_COMPUTER)
                .duration(6400)
                .EUt(V[UHV])
                .duration(6400)
                .stationResearch(b -> b.researchStack(GTResearchMachines.HIGH_PERFORMANCE_COMPUTING_ARRAY.asStack())
                        .dataStack(GTItems.TOOL_DATA_MODULE.asStack())
                        .EUt(VA[UHV])
                        .CWUt(256))
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
                .EUt(V[UEV] * 16384)
                .stationResearch(b -> b.researchStack(org.gtlcore.gtlcore.common.data.machines.MultiBlockMachineB.DISSOLVING_TANK.asStack())
                        .dataStack(GTL_Extend_Item.ADVANCED_DATA_MODULE.asStack())
                        .EUt(VA[UEV])
                        .CWUt(16384))
                .save(provider);

        SUPRACHRONAL_ASSEMBLY_LINE_RECIPES.recipeBuilder("time_space_breakes")
                .inputItems(Registries.getItem("kubejs:suprachronal_mainframe_complex"), 128)
                .inputItems(AdvancedMultiBlockMachine.DIMENSIONALLY_TRANSCENDENT_PLASMA_FORGE, 64)
                .inputItems(org.gtlcore.gtlcore.common.data.machines.MultiBlockMachineA.QFT, 64)
                .inputItems(Registries.getItem("kubejs:quantum_anomaly"), 256)
                .inputItems(Registries.getItem("gtladditions:arcanic_astrograph"), 64)
                .inputItems(Registries.getItem("kubejs:annihilate_core"), 1024)
                .inputItems(AdvancedMultiBlockMachine.CREATE_COMPUTATION, 16)
                .inputItems(Registries.getItem("kubejs:chaotic_energy_core"), 64)
                .inputItems(ChemicalHelper.get(TagPrefix.block, TranscendentMetal), 128)
                .inputItems(Registries.getItem("kubejs:create_ultimate_battery"), 32)
                .inputItems(Registries.getItem("kubejs:dimension_creation_casing"), 128)
                .inputItems(MultiBlockMachineA.BLACK_HOLE_MATTER_DECOMPRESSOR, 8)
                .inputItems(Registries.getItem("kubejs:create_aggregatione_core"), 32)
                .inputItems(CustomTags.MAX_CIRCUITS, 256)
                .inputItems(Registries.getItem("kubejs:cosmic_singularity"), 32)
                .inputItems(Registries.getItem("avaritia:eternal_singularity"), 256)
                .inputFluids(GTLMaterials.DimensionallyTranscendentResidue.getFluid(1000000000))
                .inputFluids(GTLMaterials.Eternity.getFluid(20000000))
                .inputFluids(GTLMaterials.MagnetohydrodynamicallyConstrainedStarMatter.getFluid(20000000))
                .inputFluids(GTLMaterials.SpaceTime.getFluid(200000000))
                .outputItems(MultiBlockMachineA.TIME_SPACE_BREAKER)
                .duration(1200)
                .EUt(V[MAX] * 4194304)
                .stationResearch(b -> b.researchStack(AdvancedMultiBlockMachine.DOOR_OF_CREATE.asStack())
                        .dataStack(GTL_Extend_Item.ADVANCED_DATA_MODULE.asStack())
                        .EUt(VA[MAX])
                        .CWUt(114514))
                .save(provider);
    }
}
