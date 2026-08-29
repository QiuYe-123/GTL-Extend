package cn.qiuye.gtlextend.data.recipe;

import cn.qiuye.gtlextend.GTL_Extend;
import cn.qiuye.gtlextend.common.data.GTL_Extend_Blocks;
import cn.qiuye.gtlextend.common.data.GTL_Extend_Item;
import cn.qiuye.gtlextend.common.data.machines.MultiBlockMachineA;
import cn.qiuye.gtlextend.config.GTLExtendConfigHolder;

import org.gtlcore.gtlcore.api.data.tag.GTLTagPrefix;
import org.gtlcore.gtlcore.common.data.GTLBlocks;
import org.gtlcore.gtlcore.common.data.GTLItems;
import org.gtlcore.gtlcore.common.data.GTLMachines;
import org.gtlcore.gtlcore.common.data.machines.AdvancedMultiBlockMachine;
import org.gtlcore.gtlcore.common.data.machines.GCyMMachines;
import org.gtlcore.gtlcore.common.data.machines.GeneratorMachine;
import org.gtlcore.gtlcore.common.data.machines.MultiBlockMachineB;
import org.gtlcore.gtlcore.utils.Registries;

import com.gtladd.gtladditions.common.items.GTLAddItems;
import com.gtladd.gtladditions.common.machine.multiblock.MultiBlockMachine;
import com.gtladd.gtladditions.common.material.GTLAddMaterial;

import com.gregtechceu.gtceu.api.fluids.store.FluidStorageKeys;
import com.gregtechceu.gtceu.common.data.GCyMBlocks;
import com.gregtechceu.gtceu.common.data.GTBlocks;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.data.machines.GTResearchMachines;
import com.gregtechceu.gtceu.data.recipe.CustomTags;
import com.gregtechceu.gtceu.data.recipe.VanillaRecipeHelper;

import appeng.core.definitions.AEBlocks;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.function.Consumer;

import static cn.qiuye.gtlextend.common.data.GTL_Extend_Blocks.DIMENSION_CORE;
import static com.gregtechceu.gtceu.api.GTValues.*;
import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.*;
import static com.gregtechceu.gtceu.common.data.GTMaterials.*;
import static com.gregtechceu.gtceu.common.data.GTRecipeTypes.*;
import static org.gtlcore.gtlcore.common.data.GTLMaterials.*;
import static org.gtlcore.gtlcore.common.data.GTLRecipeTypes.SUPRACHRONAL_ASSEMBLY_LINE_RECIPES;
import static org.gtlcore.gtlcore.common.data.machines.MultiBlockMachineA.*;

public class GTLEXMachineRecipe {

    public static void init(Consumer<FinishedRecipe> provider) {
        Object[][] magicEnergyConfigs = new Object[][] {
                // 参数格式: [电压等级, 输出物品所在Mod的命名空间, 电缆材料名, 电路板tag]
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
                    GTL_Extend.id(tier + "_primitive_magic_energy"),
                    new ItemStack(Registries.getItem(namespace + ":" + tier + "_primitive_magic_energy")),
                    "ADA", "ABA", "CDC",
                    'A', Registries.getItem("gtceu:" + tier + "_machine_casing"),
                    'B', Registries.getItem("minecraft:end_crystal"),
                    'C', Registries.getItem("gtceu:" + cableMaterial + "_single_cable"),
                    'D', circuitTag);
        }

        Object[][] dragonEnergyConfigs = new Object[][] {
                // 参数格式: [电压等级, 电缆材料名, 电路板tag]
                { "uv", "yttrium_barium_cuprate", CustomTags.UV_CIRCUITS },
                { "uhv", "europium", CustomTags.UHV_CIRCUITS },
                { "uev", "mithril", CustomTags.UEV_CIRCUITS },
                { "uiv", "neutronium", CustomTags.UIV_CIRCUITS }
        };

        for (Object[] config : dragonEnergyConfigs) {
            String tier = (String) config[0];
            String namespace = "gtl_extend";
            String cableMaterial = (String) config[1];
            TagKey<Item> circuitTag = (TagKey<Item>) config[2];

            VanillaRecipeHelper.addShapedRecipe(
                    provider,
                    true,
                    GTL_Extend.id(tier + "_primitive_dragon_egg_energy"),
                    new ItemStack(Registries.getItem(namespace + ":" + tier + "_primitive_dragon_egg_energy")),
                    "ADA", "ABA", "CDC",
                    'A', Registries.getItem("gtceu:" + tier + "_machine_casing"),
                    'B', Registries.getItem("minecraft:dragon_egg"),
                    'C', Registries.getItem("gtceu:" + cableMaterial + "_single_cable"),
                    'D', circuitTag);
        }

        if (GTLExtendConfigHolder.INSTANCE.enableGeneralPurposeSteamEngine) {
            VanillaRecipeHelper.addShapedRecipe(provider, true, GTL_Extend.id("the_general_steam_engine"),
                    MultiBlockMachineA.GENERAL_PURPOSE_STEAM_ENGINE.asStack(),
                    "ABA",
                    "BCB",
                    "ABA",
                    'A', Registries.getItem("gtceu:steam_machine_casing"),
                    'B', Registries.getItem("gtl_extend:void_world_block"),
                    'C', Registries.getItem("kubejs:precision_steam_mechanism"));
        }
        if (GTLExtendConfigHolder.INSTANCE.enableGeneralAEManufacturingMachine) {
            VanillaRecipeHelper.addShapedRecipe(provider, true, GTL_Extend.id("general_ae_production"),
                    MultiBlockMachineA.GENERAL_PURPOSE_AE_PRODUCTION.asStack(),
                    "AAA",
                    "ABA",
                    "AAA",
                    'A', Registries.getItem("ae2:sky_stone_block"),
                    'B', CustomTags.EV_CIRCUITS);
        }

        ASSEMBLER_RECIPES.recipeBuilder(GTL_Extend.id("the_steam_integrated_ore_processing_center"))
                .inputItems(LARGE_STEAM_MACERATOR, 64)
                .inputItems(LARGE_STEAM_BATH, 64)
                .inputItems(LARGE_STEAM_THERMAL_CENTRIFUGE, 64)
                .inputItems(Registries.getItem("kubejs:precision_steam_mechanism"), 64)
                .inputItems(Registries.getItem("kubejs:precision_steam_mechanism"), 64)
                .inputItems(CustomTags.LV_CIRCUITS, 64)
                .inputFluids(SolderingAlloy.getFluid(64000))
                .outputItems(MultiBlockMachineA.STEAM_INTEGRATED_ORE_PROCESSING_CENTER)
                .duration(20000)
                .EUt(VA[LV])
                .save(provider);

        ASSEMBLY_LINE_RECIPES.recipeBuilder(GTL_Extend.id("cattle_cattle_machine"))
                .inputItems(Items.COW_SPAWN_EGG)
                .inputFluids(Milk.getFluid(10000000000L))
                .outputItems(MultiBlockMachineA.CATTLE_CATTLE_MACHINE)
                .duration(20000)
                .EUt(V[LuV])
                .stationResearch(b -> b.researchStack(VOID_FLUID_DRILLING_RIG.asStack())
                        .dataStack(GTItems.TOOL_DATA_MODULE.asStack())
                        .EUt(VA[LuV])
                        .CWUt(128))
                .save(provider);

        ASSEMBLER_RECIPES.recipeBuilder(GTL_Extend.id("superfluid_general_energy_furnace"))
                .inputItems(SUPER_BLAST_SMELTER, 8)
                .inputItems(GCyMMachines.MEGA_BLAST_FURNACE, 8)
                .inputItems(MEGA_ALLOY_BLAST_SMELTER, 8)
                .inputItems(GTBlocks.CASING_POLYTETRAFLUOROETHYLENE_PIPE, 64)
                .inputItems(GCyMBlocks.HEAT_VENT, 64)
                .inputItems(GCyMBlocks.CASING_HIGH_TEMPERATURE_SMELTING, 64)
                .inputFluids(SolderingAlloy.getFluid(16000))
                .outputItems(MultiBlockMachineA.SUPERFLUID_GENERAL_ENERGY_FURNACE)
                .duration(4096)
                .EUt(V[UHV])
                .save(provider);

        ASSEMBLY_LINE_RECIPES.recipeBuilder(GTL_Extend.id("platinum_based_processing_hub"))
                .inputItems(CHEMICAL_PLANT, 16)
                .inputItems(GCyMMachines.LARGE_DISTILLERY, 4)
                .inputItems(GCyMMachines.LARGE_SIFTING_FUNNEL, 4)
                .inputItems(LARGE_PYROLYSE_OVEN, 4)
                .inputItems(CustomTags.UV_CIRCUITS, 32)
                .inputItems(GTItems.ROBOT_ARM_LuV, 32)
                .inputItems(frameGt, Ruridit, 64)
                .inputItems(plateDouble, Ruthenium, 32)
                .inputItems(plateDouble, Rhodium, 32)
                .inputFluids(Ruthenium.getFluid(32000))
                .inputFluids(Rhodium.getFluid(32000))
                .inputFluids(Iridium.getFluid(32000))
                .inputFluids(Osmium.getFluid(32000))
                .outputItems(MultiBlockMachineA.PLATINUM_BASE_DPROCESSING_HUB)
                .duration(6400)
                .EUt(V[LuV])
                .stationResearch(b -> b.researchStack(AdvancedMultiBlockMachine.ISA_MILL.asStack())
                        .dataStack(GTItems.TOOL_DATA_MODULE.asStack())
                        .EUt(VA[LuV])
                        .CWUt(128))
                .save(provider);

        ASSEMBLY_LINE_RECIPES.recipeBuilder(GTL_Extend.id("quantum_computer"))
                .inputItems(AEBlocks.CONDENSER.stack())
                .inputItems(GTLBlocks.CRAFTING_STORAGE_MAX, 10)
                .inputItems(GTResearchMachines.HPCA_BRIDGE_COMPONENT, 10)
                .inputItems(CustomTags.ZPM_CIRCUITS, 64)
                .inputItems(AEBlocks.CREATIVE_ENERGY_CELL.stack(), 10)
                .inputItems(GTLMachines.GTAEMachines.ME_CRAFT_PARALLEL_CORE, 10)
                .inputItems(GTItems.QUBIT_CENTRAL_PROCESSING_UNIT, 64)
                .inputItems(GTItems.QUANTUM_EYE, 64)
                .inputItems(GTItems.QUANTUM_STAR)
                .inputItems(CustomTags.UIV_CIRCUITS, 64)
                .inputItems(GTLTagPrefix.nanoswarm, Neutronium, 10)
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
                .stationResearch(b -> b.researchStack(AdvancedMultiBlockMachine.SUPER_COMPUTATION.asStack())
                        .dataStack(GTItems.TOOL_DATA_MODULE.asStack())
                        .EUt(VA[UHV])
                        .CWUt(256))
                .save(provider);

        SUPRACHRONAL_ASSEMBLY_LINE_RECIPES.recipeBuilder(GTL_Extend.id("black_hole_matter_decompressor"))
                .inputItems(GTLBlocks.DIMENSION_INJECTION_CASING, 64)
                .inputItems(Registries.getItem("kubejs:spacetime_compression_field_generator"), 64)
                .inputItems(Registries.getItem("kubejs:dimensional_stability_casing"), 64)
                .inputItems(MultiBlockMachine.INSTANCE.getFUXI_BAGUA_HEAVEN_FORGING_FURNACE(), 4)
                .inputItems(MultiBlockMachine.INSTANCE.getARCANIC_ASTROGRAPH(), 8)
                .inputItems(AdvancedMultiBlockMachine.EYE_OF_HARMONY, 64)
                .inputItems(Registries.getItem("kubejs:ctc_computational_unit"), 64)
                .inputItems(Registries.getItem("kubejs:stabilized_wormhole_generator"), 64)
                .inputItems(CustomTags.MAX_CIRCUITS, 32)
                .inputItems(GTLTagPrefix.nanoswarm, Eternity, 8)
                .inputItems(GTLItems.EMITTER_MAX, 64)
                .inputItems(GTLItems.SENSOR_MAX, 64)
                .inputItems(GTLItems.ROBOT_ARM_MAX, 64)
                .inputItems(Registries.getItem("kubejs:time_dilation_containment_unit"), 64)
                .inputItems(DIMENSION_CORE, 8)
                .inputItems(plateDouble, Chaos, 64)
                .inputFluids(SuperMutatedLivingSolder.getFluid(480000))
                .inputFluids(DegenerateRhenium.getFluid(100000))
                .inputFluids(Neutronium.getFluid(100000))
                .inputFluids(Infinity.getFluid(16000))
                .outputItems(MultiBlockMachineA.BLACK_HOLE_MATTER_DECOMPRESSOR)
                .duration(4400)
                .EUt(V[MAX] * 16384)
                .stationResearch(b -> b.researchStack(GTLAddItems.INSTANCE.getASTRAL_ARRAY().asStack())
                        .dataStack(GTL_Extend_Item.ADVANCED_DATA_MODULE.asStack())
                        .EUt(VA[MAX])
                        .CWUt(16384))
                .save(provider);

        SUPRACHRONAL_ASSEMBLY_LINE_RECIPES.recipeBuilder(GTL_Extend.id("dimensionalpower"))
                .inputItems(MultiBlockMachine.INSTANCE.getHEART_OF_THE_UNIVERSE(), 4)
                .inputItems(GeneratorMachine.ANNIHILATE_GENERATOR, 16)
                .inputItems(MultiBlockMachine.INSTANCE.getPLANETARY_IONISATION_CONVERGENCE_TOWER(), 16)
                .inputItems(GeneratorMachine.ADVANCED_HYPER_REACTOR, 64)
                .inputItems(GeneratorMachine.DYSON_SPHERE, 64)
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
                .inputFluids(DimensionallyTranscendentProsaicCatalyst.getFluid(1000000))
                .inputFluids(DimensionallyTranscendentResplendentCatalyst.getFluid(1000000))
                .inputFluids(DimensionallyTranscendentExoticCatalyst.getFluid(1000000))
                .inputFluids(DimensionallyTranscendentStellarCatalyst.getFluid(1000000))
                .outputItems(MultiBlockMachineA.DIMENSIONALPOWER)
                .duration(4400)
                .EUt(V[MAX] * 16384)
                .stationResearch(b -> b.researchStack(MultiBlockMachine.INSTANCE.getHEART_OF_THE_UNIVERSE().asStack())
                        .dataStack(GTL_Extend_Item.ADVANCED_DATA_MODULE.asStack())
                        .EUt(VA[MAX])
                        .CWUt(16384))
                .save(provider);

        SUPRACHRONAL_ASSEMBLY_LINE_RECIPES.recipeBuilder(GTL_Extend.id("dimensionally_transcendent_dissolving_tank"))
                .inputItems(GTL_Extend_Blocks.VOID_WORLD_BLOCK, 64)
                .inputItems(MultiBlockMachineB.DISSOLVING_TANK, 64)
                .inputItems(GTItems.TOOL_DATA_MODULE, 64)
                .inputItems(CustomTags.UIV_CIRCUITS, 64)
                .inputItems(GTItems.ELECTRIC_PUMP_UEV, 64)
                .inputItems(GTItems.FLUID_REGULATOR_UEV, 64)
                .inputItems(GTItems.ROBOT_ARM_UEV, 64)
                .inputItems(Registries.getItem("kubejs:reinforced_echo_shard"), 64)
                .inputItems(Registries.getItem("kubejs:time_dilation_containment_unit"), 64)
                .inputItems(Registries.getItem("kubejs:aggregatione_core"), 64)
                .inputItems(GTLTagPrefix.nanoswarm, Neutronium, 32)
                .inputItems(CHEMICAL_DISTORT, 16)
                .inputItems(AdvancedMultiBlockMachine.SPACE_ELEVATOR, 8)
                .inputItems(Registries.getItem("kubejs:stabilizer_core"), 4)
                .inputItems(SLAUGHTERHOUSE, 1)
                .inputItems(GTL_Extend_Blocks.DIMENSION_CORE, 1)
                .inputFluids(Zylon.getFluid(9216))
                .inputFluids(UuAmplifier.getFluid(1000000))
                .inputFluids(MutatedLivingSolder.getFluid(1000000))
                .inputFluids(SuperMutatedLivingSolder.getFluid(1000000))
                .outputItems(MultiBlockMachineA.DIMENSIONALLY_TRANSCENDENT_DISSOLVING_TANK)
                .duration(4400 * 4096)
                .EUt(V[UIV])
                .stationResearch(b -> b.researchStack(MultiBlockMachineB.DISSOLVING_TANK.asStack())
                        .dataStack(GTL_Extend_Item.ADVANCED_DATA_MODULE.asStack())
                        .EUt(VA[UEV])
                        .CWUt(16384))
                .save(provider);

        SUPRACHRONAL_ASSEMBLY_LINE_RECIPES.recipeBuilder(GTL_Extend.id("time_space_breakes"))
                .inputItems(Registries.getItem("kubejs:suprachronal_mainframe_complex"), 128)
                .inputItems(MultiBlockMachine.INSTANCE.getFORGE_OF_THE_ANTICHRIST(), 4)
                .inputItems(MultiBlockMachine.INSTANCE.getTIME_SPACE_DISTORTER(), 16)
                .inputItems(Registries.getItem("kubejs:quantum_anomaly"), 256)
                .inputItems(MultiBlockMachine.INSTANCE.getARCANIC_ASTROGRAPH(), 64)
                .inputItems(Registries.getItem("kubejs:annihilate_core"), 1024)
                .inputItems(MultiBlockMachine.INSTANCE.getAPOCALYPTIC_TORSION_QUANTUM_MATRIX(), 16)
                .inputItems(Registries.getItem("kubejs:chaotic_energy_core"), 64)
                .inputItems(block, MagnetohydrodynamicallyConstrainedStarMatter, 128)
                .inputItems(Registries.getItem("kubejs:create_ultimate_battery"), 32)
                .inputItems(Registries.getItem("kubejs:dimension_creation_casing"), 128)
                .inputItems(MultiBlockMachineA.DIMENSIONALPOWER)
                .inputItems(Registries.getItem("kubejs:create_aggregatione_core"), 32)
                .inputItems(CustomTags.MAX_CIRCUITS, 256)
                .inputItems(Registries.getItem("kubejs:cosmic_singularity"), 32)
                .inputItems(Registries.getItem("avaritia:eternal_singularity"), 256)
                .inputFluids(DimensionallyTranscendentResidue.getFluid(1000000000))
                .inputFluids(Eternity.getFluid(20000000))
                .inputFluids(GTLAddMaterial.INSTANCE.getPHONON_MEDIUM().getFluid(20000000))
                .inputFluids(SpaceTime.getFluid(200000000))
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
