package cn.qiuye.gtl_extend.data.recipe;

import cn.qiuye.gtl_extend.common.data.GTL_Extend_Item;
import cn.qiuye.gtl_extend.config.GTLExtendConfigHolder;

import org.gtlcore.gtlcore.utils.Registries;

import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.item.Item;

import java.util.function.Consumer;

import static cn.qiuye.gtl_extend.common.data.GTL_Extend_Materials.ETERNALBLUEDREAM;
import static com.gregtechceu.gtceu.api.GTValues.*;
import static org.gtlcore.gtlcore.common.data.GTLRecipeTypes.SUPRACHRONAL_ASSEMBLY_LINE_RECIPES;

import com.tterrag.registrate.util.entry.ItemEntry;

public class CircuitsRecipe {

    public static void init(Consumer<FinishedRecipe> provider) {
        if (GTLExtendConfigHolder.INSTANCE.enableInfinityDreamAndDreamHostCrafting) {

            // 生成 ULV 配方（基础）
            SUPRACHRONAL_ASSEMBLY_LINE_RECIPES.recipeBuilder("eternalbluedream_ulv_processor_mainframe")
                    .inputItems(Registries.getItem("minecraft:sand"), 64)
                    .inputFluids(ETERNALBLUEDREAM.getFluid(9216))
                    .outputItems(GTL_Extend_Item.ETERNALBLUE_DREAM_ULV_PROCESSOR_MAINFRAME.get())
                    .EUt(VA[UEV] * 10L)
                    .duration(1)
                    .save(provider);

            // 从 LV 开始，逐级生成高阶配方
            String[] tiers = { "ulv", "lv", "mv", "hv", "ev", "iv", "luv", "zpm", "uv", "uhv", "uev", "uiv", "uxv", "opv", "max" };
            for (int i = 1; i < tiers.length; i++) {
                String currentTier = tiers[i];
                String prevTier = tiers[i - 1];

                SUPRACHRONAL_ASSEMBLY_LINE_RECIPES.recipeBuilder("eternalbluedream_" + currentTier + "_processor_mainframe")
                        .inputItems(getItemEntry(prevTier).get()) // 输入前一级物品
                        .inputFluids(ETERNALBLUEDREAM.getFluid(9216)) // 固定流体输入
                        .outputItems(getItemEntry(currentTier).get()) // 输出当前级物品
                        .EUt(V[UEV] * 10L)
                        .duration(1)
                        .save(provider);
            }
        }
    }

    // 辅助方法：根据层级名称获取对应的 ItemEntry
    private static ItemEntry<Item> getItemEntry(String tier) {
        return switch (tier) {
            case "ulv" -> GTL_Extend_Item.ETERNALBLUE_DREAM_ULV_PROCESSOR_MAINFRAME;
            case "lv" -> GTL_Extend_Item.ETERNALBLUE_DREAM_LV_PROCESSOR_MAINFRAME;
            case "mv" -> GTL_Extend_Item.ETERNALBLUE_DREAM_MV_PROCESSOR_MAINFRAME;
            case "hv" -> GTL_Extend_Item.ETERNALBLUE_DREAM_HV_PROCESSOR_MAINFRAME;
            case "ev" -> GTL_Extend_Item.ETERNALBLUE_DREAM_EV_PROCESSOR_MAINFRAME;
            case "iv" -> GTL_Extend_Item.ETERNALBLUE_DREAM_IV_PROCESSOR_MAINFRAME;
            case "luv" -> GTL_Extend_Item.ETERNALBLUE_DREAM_LUV_PROCESSOR_MAINFRAME;
            case "zpm" -> GTL_Extend_Item.ETERNALBLUE_DREAM_ZPM_PROCESSOR_MAINFRAME;
            case "uv" -> GTL_Extend_Item.ETERNALBLUE_DREAM_UV_PROCESSOR_MAINFRAME;
            case "uhv" -> GTL_Extend_Item.ETERNALBLUE_DREAM_UHV_PROCESSOR_MAINFRAME;
            case "uev" -> GTL_Extend_Item.ETERNALBLUE_DREAM_UEV_PROCESSOR_MAINFRAME;
            case "uiv" -> GTL_Extend_Item.ETERNALBLUE_DREAM_UIV_PROCESSOR_MAINFRAME;
            case "uxv" -> GTL_Extend_Item.ETERNALBLUE_DREAM_UXV_PROCESSOR_MAINFRAME;
            case "opv" -> GTL_Extend_Item.ETERNALBLUE_DREAM_OPV_PROCESSOR_MAINFRAME;
            case "max" -> GTL_Extend_Item.ETERNALBLUE_DREAM_MAX_PROCESSOR_MAINFRAME;
            default -> throw new IllegalArgumentException("Unknown tier: " + tier);
        };
    }
}
