package cn.qiuye.gtlextend.data.recipe;

import cn.qiuye.gtlextend.common.data.GTL_Extend_Materials;
import cn.qiuye.gtlextend.config.GTLExtendConfigHolder;

import org.gtlcore.gtlcore.utils.Registries;

import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.item.ItemStack;

import java.util.function.Consumer;

import static cn.qiuye.gtlextend.common.data.GTL_Extend_RecipeTypes.GENERAL_PURPOSE_AE_PRODUCTION_RECIPES;
import static com.gregtechceu.gtceu.api.GTValues.IV;
import static com.gregtechceu.gtceu.api.GTValues.V;

public class GeneralAE_Recipe {

    public static void init(Consumer<FinishedRecipe> provider) {
        if (GTLExtendConfigHolder.INSTANCE.enableGeneralAEManufacturingMachine) {
            Object[][] aeCableConfigs = new Object[][] {
                    // 格式: [circuitMeta, 输入物品, 输出物品, 输出数量]
                    { 1, "ae2:quartz_fiber", "ae2:fluix_glass_cable", 16 },
                    { 2, "ae2:quartz_fiber", "ae2:fluix_covered_cable", 16 },
                    { 3, "ae2:quartz_fiber", "ae2:fluix_smart_cable", 16 },
                    { 4, "ae2:quartz_fiber", "ae2:fluix_covered_dense_cable", 16 },
                    { 5, "ae2:quartz_fiber", "ae2:fluix_smart_dense_cable", 16 },
                    { 1, "ae2:blank_pattern", "ae2:blank_pattern", 8 } // 特殊处理
            };

            for (Object[] cableConfig : aeCableConfigs) {
                int circuitMeta = (int) cableConfig[0];
                String inputItem = (String) cableConfig[1];
                String outputItem = (String) cableConfig[2];
                int outputCount = (int) cableConfig[3];

                // 生成配方名称后缀（移除命名空间前缀）
                String suffix = outputItem.replace("ae2:", "");
                if (outputItem.equals("ae2:blank_pattern")) {
                    suffix = "blank_pattern"; // 保持与原名称一致
                }

                GENERAL_PURPOSE_AE_PRODUCTION_RECIPES.recipeBuilder("gtl_ex_ae2_fluix_cable_" + suffix)
                        .circuitMeta(circuitMeta)
                        .chancedInput(new ItemStack(Registries.getItem(inputItem)), 9000, 0) // 动态指定输入
                        .inputFluids(GTL_Extend_Materials.FLUIXCRYSTAL.getFluid(144))
                        .outputItems(Registries.getItem(outputItem), outputCount)
                        .duration(512)
                        .EUt(V[IV])
                        .save(provider);
            }
        }
    }
}
