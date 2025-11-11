package cn.qiuye.gtl_extend.common.data.machines.MultiBlock;

import com.gregtechceu.gtceu.api.pattern.FactoryBlockPattern;

public class MultiBlockStructure {

    public static final FactoryBlockPattern GENERAL_PURPOSE_STEAM_ENGINE = FactoryBlockPattern.start()
            .aisle("AAAAA", "A A A", "AA AA", "A A A", "AAAAA")
            .aisle("AAAAA", "     ", "A   A", "     ", "AAAAA")
            .aisle("AAAAA", "A A A", "  B  ", "A A A", "AAAAA")
            .aisle("AAAAA", "     ", "A   A", "     ", "AAAAA")
            .aisle("AA~AA", "A A A", "AA AA", "A A A", "AAAAA");

    public static final FactoryBlockPattern GENERAL_PURPOSE_AE_PRODUCTION = FactoryBlockPattern.start()
            .aisle("AAAAAAAAA", "CCCCCCCCC", "         ", "         ", "         ", "         ", "         ", "         ")
            .aisle("AAAAAAAAA", "C       C", "         ", "         ", "         ", "         ", "         ", "         ")
            .aisle("AAAAAAAAA", "C D C D C", "  D C D  ", "  D D D  ", "  E E E  ", "    C    ", "         ", "         ")
            .aisle("AAAAAAAAA", "C       C", "         ", "    C    ", "         ", "    C    ", "         ", "         ")
            .aisle("AAAAAAAAA", "C C C C C", "  C F C  ", "  DCDCD  ", "  E E E  ", "  CCECC  ", "    E    ", "    C    ")
            .aisle("AAAAAAAAA", "C       C", "         ", "    C    ", "         ", "    C    ", "         ", "         ")
            .aisle("AAAAAAAAA", "C D C D C", "  D C D  ", "  D D D  ", "  E E E  ", "    C    ", "         ", "         ")
            .aisle("AAAAAAAAA", "C       C", "         ", "         ", "         ", "         ", "         ", "         ")
            .aisle("AAAA~AAAA", "CCCCCCCCC", "         ", "         ", "         ", "         ", "         ", "         ");
}
