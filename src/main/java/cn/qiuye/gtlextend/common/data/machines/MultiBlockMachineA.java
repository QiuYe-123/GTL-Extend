package cn.qiuye.gtlextend.common.data.machines;

import cn.qiuye.gtlextend.api.registries.GTLEXRegistration;
import cn.qiuye.gtlextend.api.registries.GetRegistries;
import cn.qiuye.gtlextend.client.renderer.machine.BlackHoleMatterDecompressorRender;
import cn.qiuye.gtlextend.client.renderer.machine.DimensionalPowerRender;
import cn.qiuye.gtlextend.common.data.GTL_Extend_Blocks;
import cn.qiuye.gtlextend.common.data.GTL_Extend_CreativeModeTabs;
import cn.qiuye.gtlextend.common.data.GTL_Extend_RecipeTypes;
import cn.qiuye.gtlextend.common.data.machines.MultiBlock.BlackHoleMatterDecompressor.BlackHoleMatterDecompressorMultiBlockStructure;
import cn.qiuye.gtlextend.common.data.machines.MultiBlock.Cattle_cattle_machine.Cattle_cattle_machine_MultiBlockStructure;
import cn.qiuye.gtlextend.common.data.machines.MultiBlock.MultiBlockStructure;
import cn.qiuye.gtlextend.common.data.machines.MultiBlock.Platinum_basedProcessingHub.Platinum_basedProcessingHub_MultiBlockStructure;
import cn.qiuye.gtlextend.common.data.machines.MultiBlock.QuantumComputer.QuantumComputer;
import cn.qiuye.gtlextend.common.data.machines.MultiBlock.SteamOP.SteamOP;
import cn.qiuye.gtlextend.common.data.machines.MultiBlock.SuperfluidGeneralEnergyFurnace.SuperfluidGeneralEnergyFurnace;
import cn.qiuye.gtlextend.common.data.machines.MultiBlock.TimeSpaceBreaker.TimeSpaceBreaker;
import cn.qiuye.gtlextend.common.data.machines.MultiBlock.Void_Pump.Void_Pump_MultiBlockStructure;
import cn.qiuye.gtlextend.common.machine.multiblock.electric.*;
import cn.qiuye.gtlextend.common.machine.multiblock.noenergy.BlackHoleMatterDecompressor;
import cn.qiuye.gtlextend.common.machine.multiblock.noenergy.DimensionalPower;
import cn.qiuye.gtlextend.common.machine.multiblock.noenergy.TimeSpaceBreakerMultiple;
import cn.qiuye.gtlextend.common.machine.multiblock.steam.GeneralPurposeSteamEngine;
import cn.qiuye.gtlextend.config.GTLExtendConfigHolder;

import org.gtlcore.gtlcore.api.pattern.GTLPredicates;
import org.gtlcore.gtlcore.common.data.GTLBlocks;
import org.gtlcore.gtlcore.common.data.GTLMachines;
import org.gtlcore.gtlcore.common.data.GTLRecipeTypes;
import org.gtlcore.gtlcore.utils.TextUtil;

import com.gtladd.gtladditions.api.machine.GTLAddPartAbility;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.data.RotationState;
import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.machine.MultiblockMachineDefinition;
import com.gregtechceu.gtceu.api.machine.multiblock.PartAbility;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableElectricMultiblockMachine;
import com.gregtechceu.gtceu.api.pattern.Predicates;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.OverclockingLogic;
import com.gregtechceu.gtceu.common.block.BoilerFireboxType;
import com.gregtechceu.gtceu.common.data.*;
import com.gregtechceu.gtceu.common.data.machines.GTResearchMachines;

import appeng.core.AppEng;
import appeng.core.definitions.AEBlocks;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluids;

import java.util.List;
import java.util.function.BiConsumer;

import static com.gregtechceu.gtceu.api.pattern.Predicates.blocks;
import static com.gregtechceu.gtceu.common.data.GTBlocks.*;

public class MultiBlockMachineA {

    public static final BiConsumer<ItemStack, List<Component>> GTL_EX_ADD = (stack, components) -> components
            .add(Component.literal(TextUtil.full_color(
                    Component.translatable("gtl_extend.registry.add").getString())));

    static {
        GTLEXRegistration.REGISTRATE.creativeModeTab(() -> GTL_Extend_CreativeModeTabs.MACHINES_ITEM);
    }

    public static final MultiblockMachineDefinition GENERAL_PURPOSE_AE_PRODUCTION = GTLExtendConfigHolder.INSTANCE.enableGeneralAEManufacturingMachine ? GTLEXRegistration.REGISTRATE.multiblock("general_ae_manufacturing_machine", WorkableElectricMultiblockMachine::new)
            .rotationState(RotationState.NON_Y_AXIS)
            .appearanceBlock(() -> GetRegistries.getBlock("ae2:sky_stone_block"))
            .recipeType(GTL_Extend_RecipeTypes.GENERAL_PURPOSE_AE_PRODUCTION_RECIPES)
            .tooltips(Component.literal(TextUtil.full_color("方便的生产一些AE的物品")))
            .tooltips(Component.translatable("gtceu.machine.available_recipe_map_1.tooltip",
                    Component.translatable("gtceu.general_ae_manufacturing_machine")))
            .tooltipBuilder(GTL_EX_ADD)
            .recipeModifiers(GTRecipeModifiers.PARALLEL_HATCH, GTRecipeModifiers.ELECTRIC_OVERCLOCK.apply(OverclockingLogic.PERFECT_OVERCLOCK_SUBTICK))
            .pattern(definition -> MultiBlockStructure.GENERAL_PURPOSE_AE_PRODUCTION
                    .where("~", Predicates.controller(Predicates.blocks(definition.get())))
                    .where(" ", Predicates.any())
                    .where("C", Predicates.blocks(GetRegistries.getBlock("ae2:quartz_wall")))
                    .where("D", Predicates.blocks(GetRegistries.getBlock("ae2:sky_stone_block")))
                    .where("F", Predicates.blocks(GetRegistries.getBlock("minecraft:anvil")))
                    .where("E", Predicates.blocks(GetRegistries.getBlock("ae2:fluix_block")))
                    .where("A", Predicates.blocks(GetRegistries.getBlock("ae2:sky_stone_block")).setMinGlobalLimited(10)
                            .or(Predicates.abilities(PartAbility.EXPORT_ITEMS).setPreviewCount(1))
                            .or(Predicates.abilities(PartAbility.IMPORT_ITEMS).setPreviewCount(1))
                            .or(Predicates.abilities(PartAbility.EXPORT_FLUIDS).setPreviewCount(1))
                            .or(Predicates.abilities(PartAbility.IMPORT_FLUIDS).setPreviewCount(1))
                            .or(Predicates.abilities(PartAbility.INPUT_ENERGY).setMaxGlobalLimited(2)))
                    .build())
            .workableCasingRenderer(AppEng.makeId("block/sky_stone_block"),
                    GTCEu.id("block/multiblock/fusion_reactor"))
            .register() : null;

    public static final MultiblockMachineDefinition GENERAL_PURPOSE_STEAM_ENGINE = GTLExtendConfigHolder.INSTANCE.enableGeneralPurposeSteamEngine ? GTLEXRegistration.REGISTRATE.multiblock("general_purpose_steam_engine", (holder) -> new GeneralPurposeSteamEngine(holder, 1000000))
            .rotationState(RotationState.NON_Y_AXIS)
            .recipeTypes(GTLRecipeTypes.LAVA_FURNACE_RECIPES,
                    GTRecipeTypes.FORGE_HAMMER_RECIPES,
                    GTRecipeTypes.COMPRESSOR_RECIPES,
                    GTRecipeTypes.ALLOY_SMELTER_RECIPES,
                    GTRecipeTypes.MACERATOR_RECIPES,
                    GTRecipeTypes.CIRCUIT_ASSEMBLER_RECIPES,
                    GTRecipeTypes.MIXER_RECIPES,
                    GTRecipeTypes.CENTRIFUGE_RECIPES,
                    GTRecipeTypes.THERMAL_CENTRIFUGE_RECIPES,
                    GTRecipeTypes.CHEMICAL_BATH_RECIPES,
                    GTRecipeTypes.ORE_WASHER_RECIPES,
                    GTRecipeTypes.FURNACE_RECIPES,
                    GTRecipeTypes.EXTRACTOR_RECIPES)
            .appearanceBlock(GTBlocks.CASING_BRONZE_BRICKS)
            .recipeModifiers(GTRecipeModifiers.PARALLEL_HATCH,
                    GTRecipeModifiers.ELECTRIC_OVERCLOCK.apply(OverclockingLogic.PERFECT_OVERCLOCK_SUBTICK))
            .recipeModifier((machine, recipe, params, result) -> {
                GTRecipe recipe1 = recipe.copy();
                recipe1.duration = 1;
                recipe1 = GTRecipeModifiers.fastParallel(machine, recipe1, 16384, false).getFirst();
                return recipe1;
            })
            .tooltips(Component.literal(TextUtil.full_color("暴力.....")))
            .tooltips(Component.literal(TextUtil.full_color("设置所有配方时间为1t,自带16384并行")))
            .tooltips(Component.translatable("gtceu.machine.available_recipe_map_13.tooltip",
                    Component.translatable("gtceu.lava_furnace"),
                    Component.translatable("gtceu.forge_hammer"),
                    Component.translatable("gtceu.compressor"),
                    Component.translatable("gtceu.alloy_smelter"),
                    Component.translatable("gtceu.macerator"),
                    Component.translatable("gtceu.circuit_assembler"),
                    Component.translatable("gtceu.mixer"),
                    Component.translatable("gtceu.centrifuge"),
                    Component.translatable("gtceu.thermal_centrifuge"),
                    Component.translatable("gtceu.chemical_bath"),
                    Component.translatable("gtceu.ore_washer"),
                    Component.translatable("gtceu.electric_furnace"),
                    Component.translatable("gtceu.extractor")))
            .tooltipBuilder(GTL_EX_ADD)
            .pattern(definition -> MultiBlockStructure.GENERAL_PURPOSE_STEAM_ENGINE
                    .where("~", Predicates.controller(blocks(definition.get())))
                    .where(" ", Predicates.any())
                    .where("A", Predicates.blocks(GTBlocks.CASING_BRONZE_BRICKS.get())
                            .or(Predicates.abilities(PartAbility.IMPORT_ITEMS).setMaxGlobalLimited(1))
                            .or(Predicates.abilities(PartAbility.EXPORT_ITEMS).setMaxGlobalLimited(1))
                            .or(Predicates.abilities(PartAbility.IMPORT_FLUIDS).setMaxGlobalLimited(1))
                            .or(Predicates.abilities(PartAbility.EXPORT_FLUIDS).setMaxGlobalLimited(1))
                            .or(Predicates.abilities(PartAbility.MAINTENANCE).setMaxGlobalLimited(1))
                            .or(Predicates.abilities(PartAbility.STEAM).setExactLimit(1)))
                    .where("B", Predicates.blocks(GTL_Extend_Blocks.VOID_WORLD_BLOCK.get()))
                    .build())
            .workableCasingRenderer(GTCEu.id("block/casings/solid/machine_casing_bronze_plated_bricks"),
                    GTCEu.id("block/multiblock/steam_oven"))
            .register() : null;

    public static final MultiblockMachineDefinition STEAM_INTEGRATED_ORE_PROCESSING_CENTER = GTLEXRegistration.REGISTRATE.multiblock("steam_integrated_ore_processing_center", (holder) -> new GeneralPurposeSteamEngine(holder, 100000000))
            .rotationState(RotationState.NON_Y_AXIS)
            .recipeType(GTLRecipeTypes.INTEGRATED_ORE_PROCESSOR)
            .appearanceBlock(GTBlocks.CASING_BRONZE_BRICKS)
            .recipeModifiers(GTRecipeModifiers.PARALLEL_HATCH,
                    GTRecipeModifiers.ELECTRIC_OVERCLOCK.apply(OverclockingLogic.PERFECT_OVERCLOCK_SUBTICK))
            .recipeModifier((machine, recipe, params, result) -> {
                GTRecipe recipe1 = recipe.copy();
                recipe1.duration = 1;
                recipe1 = GTRecipeModifiers.fastParallel(machine, recipe1, 100000, false).getFirst();
                return recipe1;
            })
            .tooltips(Component.literal(TextUtil.full_color("暴力.....")))
            .tooltips(Component.literal(TextUtil.full_color("设置所有配方时间为1t,自带100000并行")))
            .tooltips(Component.translatable("gtceu.machine.available_recipe_map_1.tooltip",
                    Component.translatable("gtceu.integrated_ore_processor")))
            .tooltipBuilder(GTL_EX_ADD)
            .pattern(definition -> SteamOP.PATTERN
                    .where("~", Predicates.controller(blocks(definition.getBlock())))
                    .where(" ", Predicates.any())
                    .where("A", Predicates.blocks(GTBlocks.CASING_BRONZE_BRICKS.get())
                            .or(Predicates.abilities(PartAbility.IMPORT_ITEMS))
                            .or(Predicates.abilities(PartAbility.IMPORT_FLUIDS))
                            .or(Predicates.abilities(PartAbility.STEAM_IMPORT_ITEMS)))
                    .where("B", Predicates.blocks(ChemicalHelper.getBlock(TagPrefix.frameGt, GTMaterials.Invar)))
                    .where("C", Predicates.blocks(GTBlocks.CASING_STEEL_SOLID.get()))
                    .where("D", Predicates.blocks(ChemicalHelper.getBlock(TagPrefix.frameGt, GTMaterials.Bronze)))
                    .where("E", Predicates.blocks(ChemicalHelper.getBlock(TagPrefix.frameGt, GTMaterials.Steel)))
                    .where("F", Predicates.blocks(GTBlocks.BRONZE_HULL.get()))
                    .where("G", Predicates.blocks(GTBlocks.CASING_STEEL_GEARBOX.get()))
                    .where("H", Predicates.blocks(GTBlocks.CASING_BRONZE_GEARBOX.get()))
                    .where("I", Predicates.blocks(GTBlocks.MACHINE_CASING_LV.get()))
                    .where("J", Predicates.blocks(ALL_FIREBOXES.get(BoilerFireboxType.BRONZE_FIREBOX).get()))
                    .where("K", Predicates.blocks(GTBlocks.CASING_BRONZE_BRICKS.get()))
                    .where("L", Predicates.fluids(Fluids.WATER))
                    .where("M", Predicates.blocks(GTBlocks.CASING_BRONZE_BRICKS.get()))
                    .where("N", Predicates.blocks(GTBlocks.CASING_STEEL_PIPE.get()))
                    .where("O", Predicates.blocks(GTBlocks.CASING_BRONZE_BRICKS.get())
                            .or(Predicates.abilities(PartAbility.STEAM).setMinGlobalLimited(1).setMaxGlobalLimited(9)))
                    .where("P", Predicates.blocks(GTBlocks.CASING_BRONZE_BRICKS.get())
                            .or(Predicates.abilities(PartAbility.EXPORT_ITEMS).setMaxGlobalLimited(20))
                            .or(Predicates.abilities(PartAbility.STEAM_EXPORT_ITEMS).setMaxGlobalLimited(20)))
                    .where("Q", Predicates.blocks(ChemicalHelper.getBlock(TagPrefix.block, GTMaterials.WroughtIron)))
                    .where("R", Predicates.abilities(PartAbility.MUFFLER))
                    .build())
            .workableCasingRenderer(GTCEu.id("block/casings/solid/machine_casing_bronze_plated_bricks"),
                    GTCEu.id("block/multiblock/steam_oven"))
            .register();

    public static final MultiblockMachineDefinition LARGE_VOID_PUMP = GTLEXRegistration.REGISTRATE.multiblock("large_void_pump", (holder) -> new CRTierCasingMachine(holder, "CRTier"))
            .rotationState(RotationState.NON_Y_AXIS)
            .appearanceBlock(HIGH_POWER_CASING)
            .recipeType(GTL_Extend_RecipeTypes.VOID_PUMP_RECIPES)
            .recipeModifiers(GTRecipeModifiers.PARALLEL_HATCH, GTRecipeModifiers.ELECTRIC_OVERCLOCK.apply(OverclockingLogic.PERFECT_OVERCLOCK_SUBTICK))
            .tooltips(Component.literal(TextUtil.full_color("从虚拟的宇宙中抽取无尽的流体")))
            .tooltips(Component.translatable("gtceu.machine.available_recipe_map_1.tooltip",
                    Component.translatable("gtceu.large_void_pump")))
            .tooltipBuilder(GTL_EX_ADD)
            .pattern(definition -> Void_Pump_MultiBlockStructure.VOID_PUMP
                    .where("~", Predicates.controller(blocks(definition.getBlock())))
                    .where(" ", Predicates.any())
                    .where("A", Predicates.blocks(HIGH_POWER_CASING.get())
                            .or(Predicates.abilities(PartAbility.IMPORT_ITEMS).setPreviewCount(1))
                            .or(Predicates.abilities(PartAbility.EXPORT_FLUIDS).setPreviewCount(1))
                            .or(Predicates.abilities(PartAbility.PARALLEL_HATCH).setMaxGlobalLimited(1))
                            .or(Predicates.abilities(PartAbility.INPUT_LASER).setMaxGlobalLimited(1))
                            .or(Predicates.abilities(PartAbility.INPUT_ENERGY).setMaxGlobalLimited(2))
                            .or(Predicates.abilities(GTLAddPartAbility.THREAD_MODIFIER).setMaxGlobalLimited(1)))
                    .where("B", Predicates.blocks(GetRegistries.getBlock("gtceu:stainless_steel_frame")))
                    .where("F", Predicates.blocks(GetRegistries.getBlock("gtceu:ev_machine_casing")))
                    .where("D", Predicates.blocks(GetRegistries.getBlock("gtceu:stable_machine_casing")))
                    .where("E", GTLPredicates.tierCasings(GTL_Extend_Blocks.crmap, "CRTier"))
                    .build())
            .workableCasingRenderer(GTCEu.id("block/casings/hpca/high_power_casing"),
                    GTCEu.id("block/multiblock/cleanroom"), false)
            .register();

    public static final MultiblockMachineDefinition CATTLE_CATTLE_MACHINE = GTLEXRegistration.REGISTRATE.multiblock("cattle_cattle_machine", (holder) -> new CRTierCasingMachine(holder, "CRTier"))
            .rotationState(RotationState.NON_Y_AXIS)
            .appearanceBlock(HIGH_POWER_CASING)
            .recipeType(GTL_Extend_RecipeTypes.CATTLE_CATTLE_MACHINE_RECIPES)
            .recipeModifiers(GTRecipeModifiers.PARALLEL_HATCH,
                    GTRecipeModifiers.ELECTRIC_OVERCLOCK.apply(OverclockingLogic.PERFECT_OVERCLOCK_SUBTICK))
            .tooltips(Component.literal(TextUtil.full_color("撸管太多导致出现了奇怪的现象")))
            .tooltips(Component.translatable("gtceu.machine.available_recipe_map_1.tooltip",
                    Component.translatable("gtceu.cattle_cattle_machine")))
            .tooltipBuilder(GTL_EX_ADD)
            .pattern(definition -> Cattle_cattle_machine_MultiBlockStructure.CATTLE_CATTLE_MACHINE
                    .where("~", Predicates.controller(blocks(definition.getBlock())))
                    .where(" ", Predicates.any())
                    .where("C", blocks(GetRegistries.getBlock("minecraft:pink_concrete"))
                            .or(Predicates.abilities(PartAbility.IMPORT_ITEMS).setMaxGlobalLimited(2))
                            .or(Predicates.abilities(PartAbility.IMPORT_FLUIDS).setMaxGlobalLimited(2)))
                    .where("D", Predicates.blocks(GetRegistries.getBlock("minecraft:pink_concrete"))
                            .or(Predicates.abilities(PartAbility.EXPORT_ITEMS).setMaxGlobalLimited(1))
                            .or(Predicates.abilities(PartAbility.EXPORT_FLUIDS).setMaxGlobalLimited(1)))
                    .where("E", Predicates.blocks(HIGH_POWER_CASING.get())
                            .or(Predicates.abilities(PartAbility.PARALLEL_HATCH).setMaxGlobalLimited(1))
                            .or(Predicates.abilities(PartAbility.INPUT_ENERGY).setMaxGlobalLimited(2))
                            .or(Predicates.abilities(PartAbility.INPUT_LASER).setMaxGlobalLimited(1))
                            .or(Predicates.abilities(GTLAddPartAbility.THREAD_MODIFIER).setMaxGlobalLimited(1)))
                    .where("A", GTLPredicates.tierCasings(GTL_Extend_Blocks.crmap, "CRTier"))
                    .where("F", Predicates.blocks(GetRegistries.getBlock("minecraft:white_concrete")))
                    .where("B", Predicates.blocks(GetRegistries.getBlock("minecraft:pink_concrete")))
                    .build())
            .workableCasingRenderer(GTCEu.id("block/casings/hpca/high_power_casing"),
                    GTCEu.id("block/multiblock/fusion_reactor"), false)
            .register();

    public static final MultiblockMachineDefinition SUPERFLUID_GENERAL_ENERGY_FURNACE = GTLEXRegistration.REGISTRATE.multiblock("superfluid_general_energy_furnace", GTLEXSuperfluidGeneralEnergyFurnaceMachine::new)
            .rotationState(RotationState.NON_Y_AXIS)
            .appearanceBlock(GCyMBlocks.CASING_HIGH_TEMPERATURE_SMELTING)
            .recipeType(GTRecipeTypes.BLAST_RECIPES)
            .recipeType(GTRecipeTypes.ALLOY_SMELTER_RECIPES)
            .recipeType(GCyMRecipeTypes.ALLOY_BLAST_RECIPES)
            .recipeModifiers(GTRecipeModifiers.PARALLEL_HATCH,
                    GTRecipeModifiers.ELECTRIC_OVERCLOCK.apply(OverclockingLogic.PERFECT_OVERCLOCK_SUBTICK))
            .recipeModifier((machine, recipe, params, result) -> {
                GTRecipe recipe_s = recipe.copy();
                recipe_s.duration = 1;
                recipe_s = GTRecipeModifiers.fastParallel(machine, recipe_s, Integer.MAX_VALUE, false).getFirst();
                return recipe_s;
            })
            .tooltips(Component.literal(TextUtil.full_color("最大并行数：int")))
            .tooltips(Component.literal(TextUtil.full_color("所有配方都为1s")))
            .tooltips(Component.literal(TextUtil.full_color("物质在我们手中不过是玩具")))
            .tooltips(Component.literal(TextUtil.full_color("可以随意捏造")))
            .tooltips(Component.translatable("gtceu.multiblock.laser.tooltip"))
            .tooltips(Component.translatable("gtceu.machine.multiple_recipes.tooltip"))
            .tooltips(Component.translatable("gtceu.machine.perfect_oc"))
            .tooltips(Component.translatable("gtceu.machine.available_recipe_map_3.tooltip",
                    Component.translatable("gtceu.electric_blast_furnace"),
                    Component.translatable("gtceu.alloy_blast_smelter"),
                    Component.translatable("gtceu.alloy_smelter")))
            .tooltipBuilder(GTL_EX_ADD)
            .pattern(definition -> SuperfluidGeneralEnergyFurnace.PATTERN
                    .where("~", Predicates.controller(blocks(definition.getBlock())))
                    .where(" ", Predicates.any())
                    .where("!", Predicates.blocks(GetRegistries.getBlock("kubejs:containment_field_generator")))
                    .where("#", Predicates.blocks(GetRegistries.getBlock("kubejs:molecular_coil")))
                    .where("$", Predicates.blocks(GCyMBlocks.CASING_HIGH_TEMPERATURE_SMELTING.get()))
                    .where("%", Predicates.blocks(ChemicalHelper.getBlock(TagPrefix.frameGt, GTMaterials.Neutronium)))
                    .where("&", Predicates.blocks(ChemicalHelper.getBlock(TagPrefix.frameGt, GTMaterials.NaquadahAlloy)))
                    .where("*", Predicates.blocks(GTLBlocks.STELLAR_CONTAINMENT_CASING.get()))
                    .where("-", Predicates.blocks(GCyMBlocks.HEAT_VENT.get()))
                    .where("@", Predicates.blocks(GTLBlocks.MOLECULAR_CASING.get()))
                    .where("A", Predicates.blocks(GCyMBlocks.CASING_HIGH_TEMPERATURE_SMELTING.get())
                            .or(Predicates.abilities(PartAbility.EXPORT_ITEMS).setPreviewCount(1))
                            .or(Predicates.abilities(PartAbility.IMPORT_ITEMS).setPreviewCount(1))
                            .or(Predicates.abilities(PartAbility.EXPORT_FLUIDS).setPreviewCount(1))
                            .or(Predicates.abilities(PartAbility.IMPORT_FLUIDS).setPreviewCount(1))
                            .or(Predicates.abilities(PartAbility.MAINTENANCE).setExactLimit(1))
                            .or(Predicates.abilities(PartAbility.INPUT_LASER).setMaxGlobalLimited(2))
                            .or(Predicates.abilities(PartAbility.INPUT_ENERGY).setMaxGlobalLimited(2)))
                    .where("^", Predicates.blocks(GTLBlocks.ADVANCED_COMPRESSED_FUSION_COIL.get()))
                    .build())
            .workableCasingRenderer(GTCEu.id("block/casings/gcym/high_temperature_smelting_casing"),
                    GTCEu.id("block/multiblock/fusion_reactor"), false)
            .register();

    public static final MultiblockMachineDefinition BLACK_HOLE_MATTER_DECOMPRESSOR = GTLEXRegistration.REGISTRATE.multiblock("black_hole_matter_decompressor", BlackHoleMatterDecompressor::new)
            .rotationState(RotationState.NON_Y_AXIS)
            .appearanceBlock(GTLBlocks.DIMENSION_CONNECTION_CASING)
            .recipeType(GTLRecipeTypes.COSMOS_SIMULATION_RECIPES)
            .recipeType(GTL_Extend_RecipeTypes.HORIZON_MATTER_DECOMPRESSION_RECIPES)
            .tooltips(Component.literal(TextUtil.full_color("创造一个黑洞，并从里面获取无限的资源")))
            .tooltips(Component.literal(TextUtil.purplish_red("宇宙中的万千黑洞不断地蒸发质量，真是浪费。我们从中拿取一颗，使之完全变为我们需要的材料。")))
            .tooltips(Component.literal("工作模式:"))
            .tooltips(Component.literal("  - 功率模式: 基础并行，能耗较低"))
            .tooltips(Component.literal("  - 蓝梦模式: 消耗永恒蓝梦流体获得指数级并行"))
            .tooltips(Component.literal("能耗公式:"))
            .tooltips(Component.literal("  - 基础能耗: 5.28P EU/启动"))
            .tooltips(Component.literal("  - 超频能耗 = 基础能耗 × 32^(超频次数)"))
            .tooltips(Component.literal("  - 电路1: 无超频 (1倍)"))
            .tooltips(Component.literal("  - 电路2: 4倍超频"))
            .tooltips(Component.literal("  - 电路3: 16倍超频"))
            .tooltips(Component.literal("  - 电路4: 64倍超频"))
            .tooltips(Component.literal("并行计算:"))
            .tooltips(Component.literal("  - 基础并行 = 电路编号^8 (电路1固定为64)"))
            .tooltips(Component.literal("  - 蓝梦模式: 每1000B永恒蓝梦翻倍并行"))
            .tooltips(Component.literal("  - 最大并行: Integer.MAX_VALUE"))
            .tooltips(Component.literal("配方时间:"))
            .tooltips(Component.literal("  - 基础时间: 4800 ticks"))
            .tooltips(Component.literal("  - 实际时间 = 4800 / 2^(电路编号)"))
            .tooltips(Component.literal("功率倍率:"))
            .tooltips(Component.literal("  - 电路1: 1倍"))
            .tooltips(Component.literal("  - 电路2: 32倍"))
            .tooltips(Component.literal("  - 电路3: 1024倍"))
            .tooltips(Component.literal("  - 电路4: 32768倍"))
            .tooltips(Component.literal("特殊功能:"))
            .tooltips(Component.literal("  - 需要绑定玩家无线电网供电"))
            .tooltips(Component.literal("  - 蓝梦模式下自动消耗永恒蓝梦流体"))
            .tooltips(Component.literal("  - 功率模式下限制最大能耗为电网1%"))
            .tooltips(Component.translatable("gtceu.machine.available_recipe_map_2.tooltip",
                    Component.translatable("gtceu.cosmos_simulation"),
                    Component.translatable("gtceu.horizon_matter_decompression")))
            .tooltipBuilder(GTL_EX_ADD)
            .pattern(definition -> BlackHoleMatterDecompressorMultiBlockStructure.PATTERN
                    .where("~", Predicates.controller(blocks(definition.getBlock())))
                    .where(" ", Predicates.any())
                    .where("!", Predicates.blocks(GetRegistries.getBlock("kubejs:dimension_creation_casing")))
                    .where("#", Predicates.blocks(GetRegistries.getBlock("gtlcore:dimensionally_transcendent_casing")))
                    .where("$", Predicates.blocks(GetRegistries.getBlock("gtlcore:power_module_5")))
                    .where("%", Predicates.blocks(GetRegistries.getBlock("kubejs:spacetime_compression_field_generator")))
                    .where("&", Predicates.blocks(GetRegistries.getBlock("kubejs:create_hpca_component")))
                    .where("*", Predicates.blocks(GetRegistries.getBlock("gtceu:white_borderless_lamp")))
                    .where("+", Predicates.blocks(GetRegistries.getBlock("gtceu:naquadria_frame")))
                    .where("-", Predicates.blocks(GetRegistries.getBlock("kubejs:annihilate_core")))
                    .where("=", Predicates.blocks(GetRegistries.getBlock("gtceu:high_power_casing")))
                    .where("@", Predicates.blocks(GetRegistries.getBlock("gtlcore:dimension_connection_casing")))
                    .where("A", Predicates.blocks(GetRegistries.getBlock("gtlcore:dimension_connection_casing"))
                            .or(Predicates.abilities(PartAbility.EXPORT_ITEMS).setPreviewCount(1))
                            .or(Predicates.abilities(PartAbility.IMPORT_ITEMS).setPreviewCount(1))
                            .or(Predicates.abilities(PartAbility.EXPORT_FLUIDS).setPreviewCount(1))
                            .or(Predicates.abilities(PartAbility.IMPORT_FLUIDS).setPreviewCount(1)))
                    .where("B", Predicates.blocks(GetRegistries.getBlock("gtlcore:enhance_hyper_mechanical_casing")))
                    .where("C", Predicates.blocks(GetRegistries.getBlock("gtlcore:graviton_field_constraint_casing")))
                    .where("D", Predicates.blocks(GetRegistries.getBlock("kubejs:create_aggregatione_core")))
                    .where("^", Predicates.blocks(GetRegistries.getBlock("gtlcore:hyper_mechanical_casing")))
                    .build())
            .renderer(BlackHoleMatterDecompressorRender::new)
            .hasTESR(true)
            .register();

    public static final MultiblockMachineDefinition DIMENSIONALPOWER = GTLEXRegistration.REGISTRATE.multiblock("dimensionalpower", DimensionalPower::new)
            .rotationState(RotationState.NON_Y_AXIS)
            .appearanceBlock(GTLBlocks.DIMENSION_CONNECTION_CASING)
            .recipeType(GTL_Extend_RecipeTypes.DIMENSIONALPOWER_RECIPES)
            .recipeModifier((machine, recipe, params, result) -> ((DimensionalPower) machine).recipeModifier(recipe))
            .tooltips(Component.literal(TextUtil.full_color("创造一个高维粒子，并从里面获取无限的电量")))
            .tooltips(Component.literal("发电量公式:"))
            .tooltips(Component.literal("  - 基础发电: " + Long.MAX_VALUE + " EU/t"))
            .tooltips(Component.literal("  - 最大发电: 2^" + Integer.MAX_VALUE + " EU/t"))
            .tooltips(Component.literal("  - 最终发电 = 基础发电 × 并行^(超频倍数)"))
            .tooltips(Component.literal("电路配置影响:"))
            .tooltips(Component.literal("  - 并行 = 电路编号^8 (电路1固定为64)"))
            .tooltips(Component.literal("  - 超频倍数: 线性增长，电路1为1000倍，每级增加20000倍"))
            .tooltips(Component.literal("  - 电路12可达约220000倍超频"))
            .tooltips(Component.literal("特殊功能:"))
            .tooltips(Component.literal("  - 直接向绑定玩家的无线电网供电"))
            .tooltips(Component.literal("  - 无法使用常规能量舱提取电量"))
            .tooltips(Component.literal("  - 发电量受电路配置精确控制"))
            .tooltips(Component.literal("具体电路效果请在GUI中查看"))
            .tooltips(Component.translatable("gtceu.machine.available_recipe_map_1.tooltip",
                    Component.translatable("gtceu.dimensional_power")))
            .tooltipBuilder(GTL_EX_ADD)
            .pattern(definition -> BlackHoleMatterDecompressorMultiBlockStructure.PATTERN
                    .where("~", Predicates.controller(blocks(definition.getBlock())))
                    .where(" ", Predicates.any())
                    .where("!", Predicates.blocks(GetRegistries.getBlock("kubejs:dimension_creation_casing")))
                    .where("#", Predicates.blocks(GetRegistries.getBlock("gtlcore:dimensionally_transcendent_casing")))
                    .where("$", Predicates.blocks(GetRegistries.getBlock("gtlcore:power_module_5")))
                    .where("%", Predicates.blocks(GetRegistries.getBlock("kubejs:spacetime_compression_field_generator")))
                    .where("&", Predicates.blocks(GetRegistries.getBlock("kubejs:create_hpca_component")))
                    .where("*", Predicates.blocks(GetRegistries.getBlock("gtceu:white_borderless_lamp")))
                    .where("+", Predicates.blocks(GetRegistries.getBlock("gtceu:naquadria_frame")))
                    .where("-", Predicates.blocks(GetRegistries.getBlock("kubejs:annihilate_core")))
                    .where("=", Predicates.blocks(GetRegistries.getBlock("gtceu:high_power_casing")))
                    .where("@", Predicates.blocks(GetRegistries.getBlock("gtlcore:dimension_connection_casing")))
                    .where("A", Predicates.blocks(GetRegistries.getBlock("gtlcore:dimension_connection_casing"))
                            .or(Predicates.abilities(PartAbility.EXPORT_ITEMS).setPreviewCount(1))
                            .or(Predicates.abilities(PartAbility.IMPORT_ITEMS).setPreviewCount(1))
                            .or(Predicates.abilities(PartAbility.EXPORT_FLUIDS).setPreviewCount(1))
                            .or(Predicates.abilities(PartAbility.IMPORT_FLUIDS).setPreviewCount(1)))
                    .where("B", Predicates.blocks(GetRegistries.getBlock("gtlcore:enhance_hyper_mechanical_casing")))
                    .where("C", Predicates.blocks(GetRegistries.getBlock("gtlcore:graviton_field_constraint_casing")))
                    .where("D", Predicates.blocks(GetRegistries.getBlock("kubejs:create_aggregatione_core")))
                    .where("^", Predicates.blocks(GetRegistries.getBlock("gtlcore:hyper_mechanical_casing")))
                    .build())
            .renderer(DimensionalPowerRender::new)
            .hasTESR(true)
            .register();

    public static final MultiblockMachineDefinition PLATINUM_BASE_DPROCESSING_HUB = GTLEXRegistration.REGISTRATE.multiblock("platinum_based_rocessing_hub", PlatinumBasedRocessingHub::new)
            .rotationState(RotationState.NON_Y_AXIS)
            .recipeType(GTL_Extend_RecipeTypes.PLATINUM_BASE_DPROCESSING_HUB_RECIPES)
            .appearanceBlock(ADVANCED_COMPUTER_CASING)
            .tooltips(Component.literal(TextUtil.full_color("一步完成铂系金属处理")))
            .tooltips(Component.literal("可使用等离子增产"))
            .tooltips(Component.literal("可使用等离子：氩等离子，铁等离子，镍等离子，简并态等离子"))
            .recipeModifiers(GTRecipeModifiers.PARALLEL_HATCH,
                    GTRecipeModifiers.ELECTRIC_OVERCLOCK.apply(OverclockingLogic.PERFECT_OVERCLOCK_SUBTICK))
            .tooltipBuilder(GTL_EX_ADD)
            .pattern(definition -> Platinum_basedProcessingHub_MultiBlockStructure.PLATINUM_BASE_DPROCESSING_HUB
                    .where("~", Predicates.controller(blocks(definition.getBlock())))
                    .where(" ", Predicates.any())
                    .where("A", Predicates.blocks(ADVANCED_COMPUTER_CASING.get())
                            .or(Predicates.abilities(PartAbility.EXPORT_ITEMS))
                            .or(Predicates.abilities(PartAbility.IMPORT_ITEMS))
                            .or(Predicates.abilities(PartAbility.EXPORT_FLUIDS))
                            .or(Predicates.abilities(PartAbility.IMPORT_FLUIDS))
                            .or(Predicates.abilities(GTLAddPartAbility.THREAD_MODIFIER).setMaxGlobalLimited(1))
                            .or(Predicates.abilities(PartAbility.INPUT_LASER).setMaxGlobalLimited(1))
                            .or(Predicates.abilities(PartAbility.INPUT_ENERGY).setMaxGlobalLimited(2)))
                    .where("B", Predicates.blocks(GetRegistries.getBlock("gtceu:tritanium_frame")))
                    .where("C", Predicates.blocks(GetRegistries.getBlock("gtceu:tritanium_coil_block")))
                    .where("D", Predicates.blocks(GetRegistries.getBlock("gtlcore:extreme_strength_tritanium_casing")))
                    .where("E", Predicates.blocks(GetRegistries.getBlock("gtceu:plascrete")))
                    .where("F", Predicates.blocks(GetRegistries.getBlock("gtl_extend:dimension_core")))
                    .where("G", Predicates.blocks(GetRegistries.getBlock("gtceu:high_power_casing")))
                    .where("H", Predicates.blocks(GetRegistries.getBlock("gtlcore:iridium_casing")))
                    .build())
            .workableCasingRenderer(GTCEu.id("block/casings/hpca/advanced_computer_casing/back"),
                    GTCEu.id("block/multiblock/cosmos_simulation"), false)
            .register();

    public static final MultiblockMachineDefinition DIMENSIONALLY_TRANSCENDENT_DISSOLVING_TANK = GTLEXRegistration.REGISTRATE.multiblock("dimensionally_transcendent_dissolving_tank", GTLEXDimensionallyTranscendentDissolvingTank::new)
            .rotationState(RotationState.NON_Y_AXIS)
            .recipeTypes(GTLRecipeTypes.DISSOLUTION_TREATMENT)
            .tooltips(Component.translatable("gtceu.machine.available_recipe_map_1.tooltip",
                    Component.translatable("gtceu.dissolution_treatment")))
            .tooltips(Component.translatable("gtceu.multiblock.laser.tooltip"))
            .tooltips(Component.translatable("gtceu.machine.multiple_recipes.tooltip"))
            .recipeModifiers(GTRecipeModifiers.PARALLEL_HATCH,
                    GTRecipeModifiers.ELECTRIC_OVERCLOCK.apply(OverclockingLogic.PERFECT_OVERCLOCK_SUBTICK))
            .appearanceBlock(GTBlocks.CASING_STAINLESS_CLEAN)
            .tooltipBuilder(GTL_EX_ADD)
            .pattern(definition -> GTLMachines.DTPF
                    .where("a", Predicates.controller(Predicates.blocks(definition.get())))
                    .where("e", Predicates.blocks(GTBlocks.CASING_STAINLESS_CLEAN.get())
                            .or(Predicates.abilities(PartAbility.EXPORT_ITEMS).setPreviewCount(1))
                            .or(Predicates.abilities(PartAbility.IMPORT_ITEMS).setPreviewCount(1))
                            .or(Predicates.abilities(PartAbility.EXPORT_FLUIDS).setPreviewCount(1))
                            .or(Predicates.abilities(PartAbility.IMPORT_FLUIDS).setPreviewCount(1))
                            .or(Predicates.abilities(PartAbility.INPUT_ENERGY).setMaxGlobalLimited(2))
                            .or(Predicates.abilities(PartAbility.INPUT_LASER).setMaxGlobalLimited(2))
                            .or(Predicates.abilities(GTLAddPartAbility.THREAD_MODIFIER).setMaxGlobalLimited(1))
                            .or(Predicates.abilities(PartAbility.MAINTENANCE).setExactLimit(1)))
                    .where("b", Predicates.blocks(GTBlocks.CASING_INVAR_HEATPROOF.get()))
                    .where("C", Predicates.blocks(GetRegistries.getBlock("kubejs:abyssalalloy_coil_block")))
                    .where("d", Predicates.blocks(GTBlocks.CASING_STAINLESS_CLEAN.get()))
                    .where("s", Predicates.blocks(GTBlocks.CASING_STAINLESS_TURBINE.get()))
                    .where(" ", Predicates.any())
                    .build())
            .workableCasingRenderer(GTCEu.id("block/casings/solid/machine_casing_clean_stainless_steel"),
                    GTCEu.id("block/multiblock/generator/large_gas_turbine"))
            .register();

    public static final MultiblockMachineDefinition QUANTUM_COMPUTER = GTLEXRegistration.REGISTRATE.multiblock("quantum_computer", GTLEXQuantumComputer::new)
            .rotationState(RotationState.NON_Y_AXIS)
            .recipeType(GTRecipeTypes.DUMMY_RECIPES)
            .tooltips(Component.literal(TextUtil.full_color("量子计算机 - 提供强大的计算能力")))
            .tooltips(Component.literal("算力产出公式:"))
            .tooltips(Component.literal("  - 基础算力: 1024 ~ " + Integer.MAX_VALUE + " CWU/t"))
            .tooltips(Component.literal("  - 线圈温度影响: 50% (1K-96000K)"))
            .tooltips(Component.literal("  - 电路配置影响: 50% (1-8)"))
            .tooltips(Component.literal("  - 最终算力 = 1024 + (MAX_INT-1024) × (0.5×(温度/96000) + 0.5×(电路/8))"))
            .tooltips(Component.literal("能耗公式:"))
            .tooltips(Component.literal("  - 基础能耗: 2,147,483,647 EU/t ~ 10^20 EU/t"))
            .tooltips(Component.literal("  - 能耗随算力指数增长: log10(E) = A×算力 + B"))
            .tooltips(Component.literal("  - 其中A和B为常数，确保算力从1024到MAX_INT时"))
            .tooltips(Component.literal("    能耗从2,147,483,647 EU/t增长到10^20 EU/t"))
            .tooltips(Component.literal("冷却液消耗:"))
            .tooltips(Component.literal("  - 每1M CWU/t消耗1L/t 极寒之凛冰"))
            .tooltips(Component.literal("  - 冷却液不足会导致机器停止工作"))
            .tooltips(Component.literal("特殊功能:"))
            .tooltips(Component.literal("  - 需要绑定玩家无线电网供电"))
            .tooltips(Component.literal("  - 自动从多方块结构中的流体容器消耗冷却液"))
            .recipeModifiers(GTRecipeModifiers.PARALLEL_HATCH,
                    GTRecipeModifiers.ELECTRIC_OVERCLOCK.apply(OverclockingLogic.PERFECT_OVERCLOCK_SUBTICK))
            .appearanceBlock(HIGH_POWER_CASING)
            .tooltipBuilder(GTL_EX_ADD)
            .pattern(definition -> QuantumComputer.PATTERN
                    .where("H", Predicates.controller(Predicates.blocks(definition.get())))
                    .where(" ", Predicates.any())
                    .where("A", Predicates.blocks(HIGH_POWER_CASING.get())
                            .or(Predicates.abilities(PartAbility.IMPORT_FLUIDS).setMaxGlobalLimited(1))
                            .or(Predicates.abilities(PartAbility.MAINTENANCE).setExactLimit(1))
                            .or(Predicates.abilities(PartAbility.COMPUTATION_DATA_TRANSMISSION).setExactLimit(1)))
                    .where("B", Predicates.blocks(GTLBlocks.HSSS_REINFORCED_BOROSILICATE_GLASS.get()))
                    .where("C", Predicates.blocks(GTBlocks.COMPUTER_HEAT_VENT.get()))
                    .where("D", Predicates.blocks(Blocks.REINFORCED_DEEPSLATE))
                    .where("E", Predicates.blocks(GTLBlocks.IRIDIUM_CASING.get()))
                    .where("F", Predicates.blocks(GCyMBlocks.CASING_LASER_SAFE_ENGRAVING.get()))
                    .where("G", Predicates.blocks(Blocks.NETHERITE_BLOCK))
                    .where("I", Predicates.blocks(GetRegistries.getBlock("kubejs:containment_field_generator")))
                    .where("J", Predicates.blocks(Blocks.BEACON))
                    .where("K", Predicates.blocks(AEBlocks.QUARTZ_VIBRANT_GLASS.block()))
                    .where("L", Predicates.blocks(Blocks.GLOWSTONE))
                    .where("M", Predicates.heatingCoils())
                    .where("N", Predicates.blocks(GTBlocks.MACHINE_CASING_EV.get()))
                    .where("O", Predicates.blocks(GTLBlocks.SUPER_COOLER_COMPONENT.get()))
                    .where("P", Predicates.blocks(FUSION_GLASS.get()))
                    .where("Q", Predicates.blocks(Blocks.CHAIN))
                    .where("R", Predicates.blocks(ChemicalHelper.getBlock(TagPrefix.frameGt, GTMaterials.Tritanium)))
                    .where("S", Predicates.blocks(ChemicalHelper.getBlock(TagPrefix.block, GTMaterials.NetherStar)))
                    .where("T", Predicates.blocks(GTBlocks.CASING_STAINLESS_CLEAN.get()))
                    .where("U", Predicates.blocks(GTResearchMachines.HPCA_HEAT_SINK_COMPONENT.get()))
                    .where("V", Predicates.blocks(GTL_Extend_Blocks.DIMENSION_CORE.get()))
                    .where("W", Predicates.blocks(GTBlocks.ADVANCED_COMPUTER_CASING.get()))
                    .build())
            .workableCasingRenderer(GTCEu.id("block/casings/hpca/high_power_casing"),
                    GTCEu.id("block/multiblock/hpca"))
            .register();

    public static final MultiblockMachineDefinition TIME_SPACE_BREAKER = GTLEXRegistration.REGISTRATE.multiblock("time_space_breaker", TimeSpaceBreakerMultiple::new)
            .rotationState(RotationState.NON_Y_AXIS)
            .recipeTypes(GTLRecipeTypes.QFT_RECIPES,
                    GTLRecipeTypes.DIMENSIONALLY_TRANSCENDENT_PLASMA_FORGE_RECIPES)
            .appearanceBlock(HIGH_POWER_CASING)
            .recipeModifiers(GTRecipeModifiers.PARALLEL_HATCH,
                    GTRecipeModifiers.ELECTRIC_OVERCLOCK.apply(OverclockingLogic.PERFECT_OVERCLOCK_SUBTICK))
            .tooltips(Component.literal(TextUtil.full_color("把不可能变为可能，我们无所不能")))
            .tooltips(Component.literal("此机器无需耗电，只需放置MAX激光靶仓就可以跑满并行"))
            .tooltips(Component.literal("灯建议使用创造搭，因自动搭建逻辑问题，无法生存自动搭建，需要扔掉对应的灯，使用创造搭建"))
            .tooltips(Component.literal(TextUtil.purplish_red("结构来源：Mailgusang ---已获取授权")))
            .tooltips(Component.translatable("gtceu.machine.available_recipe_map_2.tooltip",
                    Component.translatable("gtceu.dimensionally_transcendent_plasma_forge"),
                    Component.translatable("gtceu.qft")))
            .tooltipBuilder(GTL_EX_ADD)
            .pattern(definition -> TimeSpaceBreaker.PATTERN
                    .where("~", Predicates.controller(blocks(definition.getBlock())))
                    .where(" ", Predicates.any())
                    .where("!", Predicates.blocks(GTLBlocks.DIMENSIONALLY_TRANSCENDENT_CASING.get()))
                    .where("#", Predicates.blocks(GTLBlocks.COMPRESSED_FUSION_COIL_MK2_PROTOTYPE.get()))
                    .where("$", Predicates.blocks(GTBlocks.FUSION_GLASS.get()))
                    .where("%", Predicates.blocks(GTBlocks.SUPERCONDUCTING_COIL.get()))
                    .where("&", Predicates.blocks(GetRegistries.getBlock("kubejs:annihilate_core")))
                    .where("*", Predicates.blocks(GTBlocks.MACHINE_CASING_IV.get()))
                    .where("+", Predicates.blocks(GTLBlocks.LAFIUM_MECHANICAL_CASING.get()))
                    .where("-", Predicates.blocks(Blocks.REDSTONE_BLOCK))
                    .where("=", Predicates.blocks(GTLBlocks.DIMENSION_CONNECTION_CASING.get()))
                    .where("@", Predicates.blocks(Blocks.SEA_LANTERN))
                    .where("A", Predicates.blocks(GetRegistries.getBlock("kubejs:dimension_creation_casing"))
                            .or(Predicates.abilities(PartAbility.EXPORT_ITEMS))
                            .or(Predicates.abilities(PartAbility.IMPORT_ITEMS))
                            .or(Predicates.abilities(PartAbility.EXPORT_FLUIDS))
                            .or(Predicates.abilities(PartAbility.IMPORT_FLUIDS)))
                    .where("B", Predicates.blocks(GTLBlocks.FUSION_CASING_MK5.get()))
                    .where("C", Predicates.blocks(GTL_Extend_Blocks.DIMENSION_CORE.get()))
                    .where("D", Predicates.blocks(Blocks.CHAIN))
                    .where("E", Predicates.blocks(GTBlocks.LAMPS.get(DyeColor.byId(11)).get().defaultBlockState().getBlock()))
                    .where("F", Predicates.blocks(AEBlocks.QUARTZ_SLAB.block()))
                    .where("G", Predicates.blocks(AEBlocks.SMOOTH_QUARTZ_WALL.block()))
                    .where("H", Predicates.blocks(AEBlocks.SMOOTH_QUARTZ_SLAB.block()))
                    .where("I", Predicates.blocks(AEBlocks.QUARTZ_PILLAR_WALL.block()))
                    .where("J", Predicates.blocks(GetRegistries.getBlock("kubejs:magic_core")))
                    .where("K", Predicates.blocks(GTBlocks.LAMPS.get(DyeColor.byId(0)).get().defaultBlockState().getBlock()))
                    .where("L", Predicates.blocks(GTBlocks.COIL_TRITANIUM.get()))
                    .where("M", Predicates.blocks(GTLBlocks.ENHANCE_HYPER_MECHANICAL_CASING.get()))
                    .where("N", Predicates.blocks(Blocks.SHROOMLIGHT))
                    .where("O", Predicates.blocks(Blocks.GOLD_BLOCK))
                    .where("^", Predicates.blocks(Blocks.GLOWSTONE))
                    .build())
            .workableCasingRenderer(GTCEu.id("block/casings/hpca/high_power_casing"),
                    GTCEu.id("block/multiblock/hpca"))
            .register();

    public static void init() {}
}
