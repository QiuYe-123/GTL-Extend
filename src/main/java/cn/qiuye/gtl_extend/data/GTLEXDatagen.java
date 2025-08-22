package cn.qiuye.gtl_extend.data;

import cn.qiuye.gtl_extend.api.registries.GTLEXRegistration;
import cn.qiuye.gtl_extend.data.lang.LangProvider;

import com.tterrag.registrate.providers.ProviderType;

public class GTLEXDatagen {

    public static void initPost() {
        GTLEXRegistration.REGISTRATE.addDataGenerator(ProviderType.LANG, LangProvider::init);
    }
}
