package cn.qiuye.gtl_extend;

import cn.qiuye.gtl_extend.api.registries.GTLEXRegistration;
import cn.qiuye.gtl_extend.common.data.GTL_Extend_Blocks;
import cn.qiuye.gtl_extend.common.data.GTL_Extend_Elements;
import cn.qiuye.gtl_extend.common.data.GTL_Extend_Item;
import cn.qiuye.gtl_extend.common.data.GTL_Extend_Ores;
import cn.qiuye.gtl_extend.data.recipe.*;

import com.gregtechceu.gtceu.api.addon.GTAddon;
import com.gregtechceu.gtceu.api.addon.IGTAddon;
import com.gregtechceu.gtceu.api.addon.events.MaterialCasingCollectionEvent;
import com.gregtechceu.gtceu.api.registry.registrate.GTRegistrate;

import net.minecraft.data.recipes.FinishedRecipe;

import java.util.function.Consumer;

@GTAddon
public class GTL_Extend_Addon implements IGTAddon {

    @Override
    public String addonModId() {
        return GTL_Extend.MODID;
    }

    @Override
    public GTRegistrate getRegistrate() {
        return GTLEXRegistration.REGISTRATE;
    }

    @Override
    public boolean requiresHighTier() {
        return true;
    }

    @Override
    public void initializeAddon() {
        GTL_Extend_Item.init();
        GTL_Extend_Blocks.init();
    }

    @Override
    public void registerElements() {
        GTL_Extend_Elements.init();
    }

    @Override
    public void collectMaterialCasings(MaterialCasingCollectionEvent event) {
        IGTAddon.super.collectMaterialCasings(event);
    }

    @Override
    public void registerOreVeins() {
        GTL_Extend_Ores.init();
    }

    @Override
    public void addRecipes(Consumer<FinishedRecipe> provider) {
        GTLEXMachineRecipe.init(provider);
        CircuitsRecipe.init(provider);
        GeneralAE_Recipe.init(provider);
        VoidPumpRecipe.init(provider);
        CattleCattleRecipe.init(provider);
        ElectricImplosionCompressorRecipe.init(provider);
        OneStopPlatinumTreatmentRecipe.init(provider);
        AdvFormulationLimitsRecipe.init(provider);
        HorizonMatterDecompression.init(provider);
        DimensionalPowerRecipe.init(provider);
    }
}
