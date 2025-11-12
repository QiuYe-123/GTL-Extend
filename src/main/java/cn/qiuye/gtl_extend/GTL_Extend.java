package cn.qiuye.gtl_extend;

import cn.qiuye.gtl_extend.client.ClientProxy;
import cn.qiuye.gtl_extend.common.CommonProxy;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLLoader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(GTL_Extend.MODID)
public class GTL_Extend {

    public static final String MODID = "gtl_extend", NAME = "GTL Extend";
    public static final Logger LOGGER = LoggerFactory.getLogger(NAME);

    public static ResourceLocation id(String name) {
        return new ResourceLocation(MODID, name);
    }

    public GTL_Extend() {
        DistExecutor.unsafeRunForDist(() -> ClientProxy::new, () -> CommonProxy::new);
    }

    /**
     * @return if we're running in a production environment
     */
    public static boolean isProd() {
        return FMLLoader.isProduction();
    }

    /**
     * @return if we're not running in a production environment
     */
    public static boolean isDev() {
        return !isProd();
    }

    /**
     * @return if we're running data generation
     */
    public static boolean isDataGen() {
        return FMLLoader.getLaunchHandler().isData();
    }
}
