package cn.qiuye.gtlextend.data.recipe;

import org.gtlcore.gtlcore.common.data.GTLMaterials;
import org.gtlcore.gtlcore.utils.Registries;

import net.minecraft.data.recipes.FinishedRecipe;

import java.util.function.Consumer;

import static cn.qiuye.gtlextend.common.data.GTL_Extend_RecipeTypes.CATTLE_CATTLE_MACHINE_RECIPES;
import static com.gregtechceu.gtceu.api.GTValues.*;
import static com.gregtechceu.gtceu.common.data.GTMaterials.Milk;

public class CattleCattleRecipe {

    public static void init(Consumer<FinishedRecipe> provider) {
        CATTLE_CATTLE_MACHINE_RECIPES.recipeBuilder("gtl_extend_milk")
                .notConsumable(Registries.getItem("minecraft:cow_spawn_egg"))
                .outputFluids(Milk.getFluid(10000))
                .duration(20)
                .EUt(V[HV])
                .addData("CRTier", 1)
                .save(provider);

        CATTLE_CATTLE_MACHINE_RECIPES.recipeBuilder("gtl_extend_liquidstarlight")
                .inputFluids(GTLMaterials.Mana.getFluid(10000))
                .outputFluids(GTLMaterials.LiquidStarlight.getFluid(10000))
                .duration(20)
                .EUt(V[UXV])
                .addData("CRTier", 2)
                .save(provider);
    }
}
