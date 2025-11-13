package cn.qiuye.gtlextend.data.recipe;

import cn.qiuye.gtlextend.common.data.GTL_Extend_Item;
import cn.qiuye.gtlextend.common.data.machines.MultiBlockMachineA;

import org.gtlcore.gtlcore.utils.Registries;

import com.gregtechceu.gtceu.common.data.GTItems;

import net.minecraft.data.recipes.FinishedRecipe;

import java.util.function.Consumer;

import static com.gregtechceu.gtceu.api.GTValues.IV;
import static com.gregtechceu.gtceu.api.GTValues.V;
import static org.gtlcore.gtlcore.common.data.GTLRecipeTypes.ELECTRIC_IMPLOSION_COMPRESSOR_RECIPES;

public class ElectricImplosionCompressorRecipe {

    public static void init(Consumer<FinishedRecipe> provider) {
        ELECTRIC_IMPLOSION_COMPRESSOR_RECIPES.recipeBuilder("large_world_void_pump")
                .inputItems(Registries.getItem("gtceu:ev_fluid_drilling_rig"), 64)
                .inputItems(Registries.getItem("gtceu:ev_fluid_drilling_rig"), 64)
                .outputItems(MultiBlockMachineA.LARGE_VOID_PUMP)
                .duration(2048)
                .EUt(V[IV])
                .save(provider);

        ELECTRIC_IMPLOSION_COMPRESSOR_RECIPES.recipeBuilder("advanced_data_module")
                .inputItems(GTItems.TOOL_DATA_MODULE, 64)
                .inputItems(GTItems.TOOL_DATA_MODULE, 64)
                .outputItems(GTL_Extend_Item.ADVANCED_DATA_MODULE, 1)
                .duration(2048)
                .EUt(V[IV])
                .save(provider);
    }
}
