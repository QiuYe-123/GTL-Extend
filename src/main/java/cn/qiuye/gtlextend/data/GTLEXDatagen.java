package cn.qiuye.gtlextend.data;

import cn.qiuye.gtlextend.api.registries.GTLEXRegistration;
import cn.qiuye.gtlextend.data.lang.LangProvider;

import com.tterrag.registrate.providers.ProviderType;

public class GTLEXDatagen {

    public static void initPost() {
        GTLEXRegistration.REGISTRATE.addDataGenerator(ProviderType.LANG, LangProvider::init);
    }
}
