package cn.qiuye.gtlextend.api.registries;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MachineDefinition;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.registry.registrate.MachineBuilder;

import java.util.Locale;
import java.util.function.BiFunction;

import static cn.qiuye.gtlextend.api.registries.GTLEXRegistration.REGISTRATE;

public class MachinesRegister {

    public static MachineDefinition[] registerTieredMachines(String name, BiFunction<IMachineBlockEntity, Integer, MetaMachine> factory, BiFunction<Integer, MachineBuilder<MachineDefinition>, MachineDefinition> builder, int... tiers) {
        MachineDefinition[] definitions = new MachineDefinition[GTValues.TIER_COUNT];

        for (int tier : tiers) {
            MachineBuilder<MachineDefinition> register = REGISTRATE.machine(GTValues.VN[tier].toLowerCase(Locale.ROOT) + "_" + name, (holder) -> factory.apply(holder, tier)).tier(tier);
            definitions[tier] = builder.apply(tier, register);
        }

        return definitions;
    }
}
