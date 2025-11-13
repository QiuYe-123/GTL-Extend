package cn.qiuye.gtlextend.common.data.machines;

import cn.qiuye.gtlextend.common.data.GTL_Extend_Elements;
import cn.qiuye.gtlextend.config.GTLExtendConfigHolder;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialIconSet;
import com.gregtechceu.gtceu.api.fluids.FluidBuilder;

import static cn.qiuye.gtlextend.common.data.GTL_Extend_Materials.ETERNALBLUEDREAM;
import static cn.qiuye.gtlextend.common.data.GTL_Extend_Materials.FLUIXCRYSTAL;
import static com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialFlags.GENERATE_FRAME;
import static com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialIconSet.BRIGHT;

public class GTL_Extend_MaterialsBuilder {

    public static void init() {
        if (GTLExtendConfigHolder.INSTANCE.enableGeneralAEManufacturingMachine) {
            FLUIXCRYSTAL = new Material.Builder(GTCEu.id("fluix_crystal"))
                    .liquid(new FluidBuilder().customStill())
                    .iconSet(new MaterialIconSet("fluix_crystal"))
                    .color(0x835abc)
                    .secondaryColor(0x56479d)
                    .buildAndRegister();
        }
        ETERNALBLUEDREAM = new Material.Builder(GTCEu.id("eternal_blue_dream_vein"))
                .gem()
                .ore()
                .fluid()
                .element(GTL_Extend_Elements.ETERNALBLUEDREAM)
                .color(0x3F8EFF)
                .secondaryColor(0x297CFF)
                .iconSet(BRIGHT)
                .flags(GENERATE_FRAME)
                .buildAndRegister();
    }
}
