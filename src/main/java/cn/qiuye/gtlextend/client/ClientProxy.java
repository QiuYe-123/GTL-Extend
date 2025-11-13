package cn.qiuye.gtlextend.client;

import cn.qiuye.gtlextend.common.CommonProxy;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ClientProxy extends CommonProxy {

    public ClientProxy() {
        super();
        init();
    }

    public static void init() {}
}
