package cn.qiuye.gtl_extend.common.data;

import com.gregtechceu.gtceu.api.item.ComponentItem;
import com.gregtechceu.gtceu.common.data.GTCompassSections;
import com.gregtechceu.gtceu.common.item.DataItemBehavior;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import static cn.qiuye.gtl_extend.api.registries.GTLEXRegistration.REGISTRATE;
import static com.gregtechceu.gtceu.common.data.GTItems.attach;
import static com.gregtechceu.gtceu.common.data.GTItems.compassNode;

import com.tterrag.registrate.util.entry.ItemEntry;

public class GTL_Extend_Item {

    public static final ItemEntry<Item> FOREVER;
    public static final ItemEntry<ComponentItem> ADVANCED_DATA_MODULE;
    public static final ItemEntry<Item> ETERNALBLUE_DREAM_ULV_PROCESSOR_MAINFRAME;
    public static final ItemEntry<Item> ETERNALBLUE_DREAM_LV_PROCESSOR_MAINFRAME;
    public static final ItemEntry<Item> ETERNALBLUE_DREAM_MV_PROCESSOR_MAINFRAME;
    public static final ItemEntry<Item> ETERNALBLUE_DREAM_HV_PROCESSOR_MAINFRAME;
    public static final ItemEntry<Item> ETERNALBLUE_DREAM_EV_PROCESSOR_MAINFRAME;
    public static final ItemEntry<Item> ETERNALBLUE_DREAM_IV_PROCESSOR_MAINFRAME;
    public static final ItemEntry<Item> ETERNALBLUE_DREAM_LUV_PROCESSOR_MAINFRAME;
    public static final ItemEntry<Item> ETERNALBLUE_DREAM_ZPM_PROCESSOR_MAINFRAME;
    public static final ItemEntry<Item> ETERNALBLUE_DREAM_UV_PROCESSOR_MAINFRAME;
    public static final ItemEntry<Item> ETERNALBLUE_DREAM_UHV_PROCESSOR_MAINFRAME;
    public static final ItemEntry<Item> ETERNALBLUE_DREAM_UEV_PROCESSOR_MAINFRAME;
    public static final ItemEntry<Item> ETERNALBLUE_DREAM_UIV_PROCESSOR_MAINFRAME;
    public static final ItemEntry<Item> ETERNALBLUE_DREAM_UXV_PROCESSOR_MAINFRAME;
    public static final ItemEntry<Item> ETERNALBLUE_DREAM_OPV_PROCESSOR_MAINFRAME;
    public static final ItemEntry<Item> ETERNALBLUE_DREAM_MAX_PROCESSOR_MAINFRAME;

    static {
        // 设置创造模式标签页
        REGISTRATE.creativeModeTab(() -> GTL_Extend_CreativeModeTabs.GTL_EX_GT_ITEM);
    }

    static {
        FOREVER = REGISTRATE.item("forever", Item::new)
                .lang("Forever")
                .register();
        ADVANCED_DATA_MODULE = REGISTRATE.item("advanced_data_module", ComponentItem::create)
                .onRegister(attach(new DataItemBehavior(true)))
                .onRegister(compassNode(GTCompassSections.COMPONENTS))
                .register();

        ETERNALBLUE_DREAM_ULV_PROCESSOR_MAINFRAME = registerMainframe("ulv");
        ETERNALBLUE_DREAM_LV_PROCESSOR_MAINFRAME = registerMainframe("lv");
        ETERNALBLUE_DREAM_MV_PROCESSOR_MAINFRAME = registerMainframe("mv");
        ETERNALBLUE_DREAM_HV_PROCESSOR_MAINFRAME = registerMainframe("hv");
        ETERNALBLUE_DREAM_EV_PROCESSOR_MAINFRAME = registerMainframe("ev");
        ETERNALBLUE_DREAM_IV_PROCESSOR_MAINFRAME = registerMainframe("iv");
        ETERNALBLUE_DREAM_LUV_PROCESSOR_MAINFRAME = registerMainframe("luv");
        ETERNALBLUE_DREAM_ZPM_PROCESSOR_MAINFRAME = registerMainframe("zpm");
        ETERNALBLUE_DREAM_UV_PROCESSOR_MAINFRAME = registerMainframe("uv");
        ETERNALBLUE_DREAM_UHV_PROCESSOR_MAINFRAME = registerMainframe("uhv");
        ETERNALBLUE_DREAM_UEV_PROCESSOR_MAINFRAME = registerMainframe("uev");
        ETERNALBLUE_DREAM_UIV_PROCESSOR_MAINFRAME = registerMainframe("uiv");
        ETERNALBLUE_DREAM_UXV_PROCESSOR_MAINFRAME = registerMainframe("uxv");
        ETERNALBLUE_DREAM_OPV_PROCESSOR_MAINFRAME = registerMainframe("opv");
        ETERNALBLUE_DREAM_MAX_PROCESSOR_MAINFRAME = registerMainframe("max");
    }

    private static ItemEntry<Item> registerMainframe(String tier) {
        return REGISTRATE.item("eternalbluedream_" + tier + "_processor_mainframe", Item::new)
                .lang("Eternal Blue Dream " + tier.toUpperCase() + " Processor mainframe")
                .model((ctx, prov) -> prov.withExistingParent(ctx.getName(), new ResourceLocation("gtl_extend", "item/eternalbluedream_processor_mainframe")))
                .register();
    }

    public static void init() {}
}
