package cn.qiuye.gtlextend.common.data;

import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.common.data.GTSoundEntries;

import com.lowdragmc.lowdraglib.utils.LocalizationUtils;

import net.minecraft.client.resources.language.I18n;

import static com.gregtechceu.gtceu.common.data.GTRecipeTypes.MULTIBLOCK;
import static com.gregtechceu.gtceu.common.data.GTRecipeTypes.register;
import static com.lowdragmc.lowdraglib.gui.texture.ProgressTexture.FillDirection.LEFT_TO_RIGHT;

public class GTL_Extend_RecipeTypes {

    public final static GTRecipeType DIMENSIONALPOWER_RECIPES = register("dimensional_power", MULTIBLOCK)
            .setMaxIOSize(1, 1, 1, 1)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.SCIENCE);
    public final static GTRecipeType HORIZON_MATTER_DECOMPRESSION_RECIPES = register("horizon_matter_decompression", MULTIBLOCK)
            .setMaxIOSize(1, 0, 1, 36)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.SCIENCE);
    public final static GTRecipeType PLATINUM_BASE_DPROCESSING_HUB_RECIPES = register("one_stop_platinum_treatment", MULTIBLOCK)
            .setMaxIOSize(2, 6, 4, 3)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.FURNACE);
    public final static GTRecipeType GENERAL_PURPOSE_AE_PRODUCTION_RECIPES = register("general_ae_manufacturing_machine", MULTIBLOCK)
            .setMaxIOSize(10, 1, 10, 1)
            .setEUIO(IO.IN)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.MOTOR);
    public final static GTRecipeType VOID_PUMP_RECIPES = register("large_void_pump", MULTIBLOCK)
            .setMaxIOSize(1, 0, 0, 1)
            .setEUIO(IO.IN)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .addDataInfo(data -> LocalizationUtils.format("gtceu.recipe.tier_combination", getCRTier(data.getInt("CRTier"))))
            .setSound(GTSoundEntries.MOTOR);
    public final static GTRecipeType CATTLE_CATTLE_MACHINE_RECIPES = register("cattle_cattle_machine", MULTIBLOCK)
            .setMaxIOSize(1, 0, 1, 1)
            .setEUIO(IO.IN)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .addDataInfo(data -> LocalizationUtils.format("gtceu.recipe.tier_combination", getCRTier(data.getInt("CRTier"))))
            .setSound(GTSoundEntries.MOTOR);

    public static String getCRTier(int tier) {
        if (tier == 2) {
            return I18n.get("gtceu.tier.dimension_core");
        } else {
            return I18n.get("gtceu.tier.void_world_block");
        }
    }

    public static void init() {}
}
