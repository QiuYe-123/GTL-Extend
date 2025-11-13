package cn.qiuye.gtlextend.common.data;

import com.gregtechceu.gtceu.api.data.chemical.Element;
import com.gregtechceu.gtceu.common.data.GTElements;

public class GTL_Extend_Elements {

    public static Element ETERNALBLUEDREAM;

    public static void init() {
        ETERNALBLUEDREAM = GTElements.createAndRegister(150, 110, -1, null, "bluedream", "$&@#*$^%$*", false);
    }
}
