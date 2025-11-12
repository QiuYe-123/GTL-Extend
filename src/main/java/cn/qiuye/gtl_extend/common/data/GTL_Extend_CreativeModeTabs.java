package cn.qiuye.gtl_extend.common.data;

import cn.qiuye.gtl_extend.GTL_Extend;
import cn.qiuye.gtl_extend.api.registries.GTLEXRegistration;
import cn.qiuye.gtl_extend.common.data.machines.MultiBlockMachineA;

import com.gregtechceu.gtceu.common.data.GTCreativeModeTabs;

import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.world.item.CreativeModeTab;

public class GTL_Extend_CreativeModeTabs {

    public static RegistryEntry<CreativeModeTab> MACHINES_ITEM;
    public static RegistryEntry<CreativeModeTab> BLOCKS_ITEM;
    public static RegistryEntry<CreativeModeTab> GTL_EX_GT_ITEM;

    static {
        MACHINES_ITEM = GTLEXRegistration.REGISTRATE.defaultCreativeTab("machines_item", (builder) -> builder.displayItems(new GTCreativeModeTabs.RegistrateDisplayItemsGenerator("machines_item", GTLEXRegistration.REGISTRATE))
                .icon(MultiBlockMachineA.SUPERFLUID_GENERAL_ENERGY_FURNACE::asStack)
                .title(GTLEXRegistration.REGISTRATE.addLang("itemGroup", GTL_Extend.id("machines_item"), "GTL Extend Machines Items"))
                .build()).register();

        BLOCKS_ITEM = GTLEXRegistration.REGISTRATE.defaultCreativeTab("blocks_item", (builder) -> builder.displayItems(new GTCreativeModeTabs.RegistrateDisplayItemsGenerator("blocks_item", GTLEXRegistration.REGISTRATE))
                .icon(GTL_Extend_Blocks.DIMENSION_CORE::asStack)
                .title(GTLEXRegistration.REGISTRATE.addLang("itemGroup", GTL_Extend.id("blocks_item"), "GTL Extend Blocks Items"))
                .build()).register();

        GTL_EX_GT_ITEM = GTLEXRegistration.REGISTRATE.defaultCreativeTab("gtl_ex_gt_item", (builder) -> builder.displayItems(new GTCreativeModeTabs.RegistrateDisplayItemsGenerator("gtl_ex_gt_item", GTLEXRegistration.REGISTRATE))
                .icon(GTL_Extend_Item.FOREVER::asStack)
                .title(GTLEXRegistration.REGISTRATE.addLang("itemGroup", GTL_Extend.id("gtl_ex_gt_item"), "GTL Extend GT Items"))
                .build()).register();
    }

    public static void init() {}
}
