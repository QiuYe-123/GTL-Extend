package cn.qiuye.gtlextend.common.data.machines.MultiBlock.Void_Pump;

import com.gregtechceu.gtceu.api.pattern.FactoryBlockPattern;

public class Void_Pump_MultiBlockStructure {

    public static final FactoryBlockPattern VOID_PUMP;

    static {
        VOID_PUMP = FactoryBlockPattern.start()
                .aisle(Void_Pump_Part1.LAYER_001)
                .aisle(Void_Pump_Part1.LAYER_002)
                .aisle(Void_Pump_Part1.LAYER_003)
                .aisle(Void_Pump_Part1.LAYER_004)
                .aisle(Void_Pump_Part1.LAYER_005)
                .aisle(Void_Pump_Part1.LAYER_006)
                .aisle(Void_Pump_Part1.LAYER_007)
                .aisle(Void_Pump_Part1.LAYER_008)
                .aisle(Void_Pump_Part1.LAYER_009)
                .aisle(Void_Pump_Part1.LAYER_010)
                .aisle(Void_Pump_Part1.LAYER_011)
                .aisle(Void_Pump_Part1.LAYER_012)
                .aisle(Void_Pump_Part1.LAYER_013)
                .aisle(Void_Pump_Part1.LAYER_014)
                .aisle(Void_Pump_Part1.LAYER_015);
    }
}
