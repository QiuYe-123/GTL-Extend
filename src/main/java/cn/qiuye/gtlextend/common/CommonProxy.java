package cn.qiuye.gtlextend.common;

import cn.qiuye.gtlextend.GTL_Extend;
import cn.qiuye.gtlextend.common.data.GTL_Extend_CreativeModeTabs;
import cn.qiuye.gtlextend.common.data.GTL_Extend_Machines;
import cn.qiuye.gtlextend.common.data.GTL_Extend_Materials;
import cn.qiuye.gtlextend.common.data.GTL_Extend_RecipeTypes;
import cn.qiuye.gtlextend.config.GTLExtendConfigHolder;

import com.gregtechceu.gtceu.api.GTCEuAPI;
import com.gregtechceu.gtceu.api.data.chemical.material.event.MaterialEvent;
import com.gregtechceu.gtceu.api.data.chemical.material.event.MaterialRegistryEvent;
import com.gregtechceu.gtceu.api.data.chemical.material.event.PostMaterialEvent;
import com.gregtechceu.gtceu.api.machine.MachineDefinition;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.api.recipe.condition.RecipeConditionType;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import static cn.qiuye.gtlextend.api.registries.GTLEXRegistration.REGISTRATE;

public class CommonProxy {

    public CommonProxy() {
        CommonProxy.init();
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        REGISTRATE.registerEventListeners(eventBus);
        eventBus.addListener(this::commonSetup);
        eventBus.addListener(this::clientSetup);
        eventBus.addListener(this::addMaterialRegistries);
        eventBus.addListener(this::addMaterials);
        eventBus.addListener(this::modifyMaterials);
        eventBus.addGenericListener(RecipeConditionType.class, this::registerRecipeConditions);
        eventBus.addGenericListener(GTRecipeType.class, this::registerRecipeTypes);
        eventBus.addGenericListener(MachineDefinition.class, this::registerMachines);
    }

    public static void init() {
        GTL_Extend_CreativeModeTabs.init();
        GTLExtendConfigHolder.init();
        if (GTL_Extend.isDataGen() || GTL_Extend.isDev()) {
            GTLExtendConfigHolder.INSTANCE.enableInfinityDreamAndDreamHostCrafting = true;
            GTLExtendConfigHolder.INSTANCE.enableGeneralPurposeSteamEngine = true;
            GTLExtendConfigHolder.INSTANCE.enableGeneralAEManufacturingMachine = true;
        }
    }

    private void commonSetup(final FMLCommonSetupEvent event) {}

    protected void clientSetup(final FMLClientSetupEvent event) {}

    private void addMaterialRegistries(MaterialRegistryEvent event) {
        GTCEuAPI.materialManager.createRegistry(GTL_Extend.MODID);
    }

    private void addMaterials(MaterialEvent event) {
        GTL_Extend_Materials.init();
    }

    private void modifyMaterials(PostMaterialEvent event) {}

    private void registerRecipeTypes(GTCEuAPI.RegisterEvent<ResourceLocation, GTRecipeType> event) {
        GTL_Extend_RecipeTypes.init();
    }

    private void registerRecipeConditions(GTCEuAPI.RegisterEvent<ResourceLocation, RecipeConditionType<?>> event) {}

    private void registerMachines(GTCEuAPI.RegisterEvent<ResourceLocation, MachineDefinition> event) {
        GTL_Extend_Machines.init();
    }
}
