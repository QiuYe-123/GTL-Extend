package cn.qiuye.gtlextend.common.data.machines.MultiBlock.Cattle_cattle_machine;

import com.gregtechceu.gtceu.api.pattern.FactoryBlockPattern;

public class Cattle_cattle_machine_MultiBlockStructure {

    public static final FactoryBlockPattern CATTLE_CATTLE_MACHINE;

    static {
        CATTLE_CATTLE_MACHINE = FactoryBlockPattern.start()
                .aisle(Cattle_cattle_machine_Part1.LAYER_001)
                .aisle(Cattle_cattle_machine_Part1.LAYER_002)
                .aisle(Cattle_cattle_machine_Part1.LAYER_003)
                .aisle(Cattle_cattle_machine_Part1.LAYER_004)
                .aisle(Cattle_cattle_machine_Part1.LAYER_005)
                .aisle(Cattle_cattle_machine_Part1.LAYER_006)
                .aisle(Cattle_cattle_machine_Part1.LAYER_007);
    }

    public Cattle_cattle_machine_MultiBlockStructure() {}
}
