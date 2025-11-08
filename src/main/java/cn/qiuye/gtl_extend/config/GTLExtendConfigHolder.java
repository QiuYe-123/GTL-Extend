package cn.qiuye.gtl_extend.config;

import cn.qiuye.gtl_extend.GTL_Extend;

import dev.toma.configuration.Configuration;
import dev.toma.configuration.config.Config;
import dev.toma.configuration.config.Configurable;
import dev.toma.configuration.config.format.ConfigFormats;

@Config(id = GTL_Extend.MODID)
public class GTLExtendConfigHolder {

    public static GTLExtendConfigHolder INSTANCE;

    public static void init() {
        if (INSTANCE == null) {
            INSTANCE = Configuration.registerConfig(GTLExtendConfigHolder.class,
                    ConfigFormats.yaml()).getConfigInstance();
        }
    }

    @Configurable
    @Configurable.Comment("开启永恒蓝梦和蓝梦主机合成表的注册，注意这可能会影响游戏平衡（修改后请退出重进）")
    public boolean enableInfinityDreamAndDreamHostCrafting = false;
    @Configurable
    @Configurable.Comment("开启黑洞物质解压器实体渲染,当延迟过大时关闭此项(未实装)")
    public boolean enableBlackHoleMatterDecompressor = true;
    @Configurable
    @Configurable.Comment("开启超维度发电机实体渲染,当延迟过大时关闭此项(未实装)")
    public boolean enableHyperDimensionalPower = true;
    @Configurable
    @Configurable.Comment("开启通用蒸汽机（修改后请退出重进）")
    public boolean enableGeneralPurposeSteamEngine = false;
    @Configurable
    @Configurable.Comment("开启通用AE制造机（修改后请退出重进）")
    public boolean enableGeneralAEManufacturingMachine = false;
    @Configurable
    @Configurable.Comment("最大配方线程数逻辑使用更改(修改后请退出重进) AUTO为Integer.MAX，ADD为gtladd的线程计算逻辑，SET为设置线程数")
    public ThreadsType max_threads_type = ThreadsType.AUTO;
    @Configurable
    @Configurable.Comment("配方线程数(修改后请退出重进)")
    @Configurable.Range(min = 1, max = Integer.MAX_VALUE)
    public int max_threads = 2048;
    @Configurable
    @Configurable.Comment("实体加速")
    public boolean ticktime = true;

    public enum ThreadsType {
        AUTO,
        ADD,
        SET
    }
	
	public boolean ThreadsSET() {
		return max_threads_type == ThreadsType.SET;
	}
	
	public boolean ThreadsADD() {
		return max_threads_type == ThreadsType.ADD;
	}
	
	public boolean ThreadsSetAUTO() {
		return max_threads_type == ThreadsType.AUTO;
	}
}
