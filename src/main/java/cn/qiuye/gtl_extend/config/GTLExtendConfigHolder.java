package cn.qiuye.gtl_extend.config;

import cn.qiuye.gtl_extend.GTL_Extend;

import dev.toma.configuration.Configuration;
import dev.toma.configuration.config.Config;
import dev.toma.configuration.config.Configurable;
import dev.toma.configuration.config.format.ConfigFormats;

@Config(id = GTL_Extend.MODID)
public class GTLExtendConfigHolder {

    public static GTLExtendConfigHolder INSTANCE;
    @Configurable
    @Configurable.Comment("开启永恒蓝梦和蓝梦主机合成表的注册，注意这可能会影响游戏平衡（修改后请退出重进）")
    public boolean enableInfinityDreamAndDreamHostCrafting = false;
    @Configurable
    @Configurable.Comment("开启黑洞物质解压器实体渲染,当延迟过大时关闭此项")
    public boolean enableBlackHoleMatterDecompressor = true;
    @Configurable
    @Configurable.Comment("开启超维度发电机实体渲染,当延迟过大时关闭此项")
    public boolean enableHyperDimensionalPower = true;
    @Configurable
    @Configurable.Comment("开启通用蒸汽机（修改后请退出重进）")
    public boolean enableGeneralPurposeSteamEngine = false;
    @Configurable
    @Configurable.Comment("开启通用AE制造机（修改后请退出重进）")
    public boolean enableGeneralAEManufacturingMachine = false;

    public static void init() {
        if (INSTANCE == null) {
            INSTANCE = Configuration.registerConfig(GTLExtendConfigHolder.class,
                    ConfigFormats.yaml()).getConfigInstance();
        }
    }
}
