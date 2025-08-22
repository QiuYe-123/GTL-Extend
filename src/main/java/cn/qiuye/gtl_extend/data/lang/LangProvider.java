package cn.qiuye.gtl_extend.data.lang;

import com.gregtechceu.gtceu.data.lang.LangHandler;

import com.tterrag.registrate.providers.RegistrateLangProvider;

public class LangProvider extends LangHandler {

    public static void init(RegistrateLangProvider provider) {
        provider.add("config.screen.gtl_extend", "GTL EX config");
        provider.add("config.gtl_extend.option.enableInfinityDreamAndDreamHostCrafting", "Enable the Eternal Blue Dream and recipes registration of Eternal Blue Dream mainframe.Warning,It may destroy the balance of the game.(need to restart)");
        provider.add("config.gtl_extend.option.enableBlackHoleMatterDecompressor", "Enable Black Hole Matter Decompressor entity rendering.Disable it when taking excessive lag");
        provider.add("config.gtl_extend.option.enableHyperDimensionalPower", "Enable Dimensionally Transcendent Generator entity rendering.Disable it when taking excessive lag");
        provider.add("config.gtl_extend.option.enableGeneralPurposeSteamEngine", "Enable General Purpose Steam Engine(need to restart)");
        provider.add("config.gtl_extend.option.enableGeneralAEManufacturingMachine", "Enable General AE Manufacturing Machine(need to restart)");
        provider.add("gtl_extend.registry.add", "Added by GregTech Leisure-Extend");
        provider.add("gtl_extend_machine_mode", "Current mode, %s");
        provider.add("gtl_extend_machine_circuit", "Circuit configuration, %d (x%s power)");
        provider.add("material.gtceu.eternal_blue_dream_vein", "Eternal Blue Dream");
        provider.add("material.gtceu.fluix_crystal", "Fluix Crystal");
        replace(provider, "block.gtl_extend.dimensionalpower", "Dimensionally Transcendent Generator");
        replace(provider, "block.gtl_extend.platinum_based_processing_hub", "Platinum Group Processing Hub");
        replace(provider, "block.gtl_extend.void_world_block", "World Core");
        provider.add("block.gtl_extend.general_ae_manufacturing_machine", "General AE Manufacturing Machine");
        provider.add("block.gtl_extend.general_purpose_steam_engine", "General Purpose Steam Engine");
        replace(provider, "itemGroup.gtl_extend.blocks_item", "GTL_EX blocks");
        replace(provider, "itemGroup.gtl_extend.gtl_ex_gt_item", "GTL_EX items");
        replace(provider, "itemGroup.gtl_extend.machines_item", "GTL_EX machines");
        System.out.println("Running");
    }
}
