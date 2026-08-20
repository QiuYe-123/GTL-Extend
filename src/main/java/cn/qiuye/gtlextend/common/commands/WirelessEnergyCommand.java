package cn.qiuye.gtlextend.common.commands;

import com.hepdd.gtmthings.api.misc.WirelessEnergyManager;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;
import java.util.UUID;

public class WirelessEnergyCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("gtlex")
                .requires(commandSourceStack -> commandSourceStack.hasPermission(4))
                .then(Commands.literal("wireless_energy")
                        .then(Commands.literal("add")
                                .then(Commands.argument("EU", StringArgumentType.string()).executes(WirelessEnergyCommand::addEnergy)))
                        .then(Commands.literal("set")
                                .then(Commands.argument("EU", StringArgumentType.string()).executes(WirelessEnergyCommand::setEnergy)))));
    }

    private static int setEnergy(CommandContext<CommandSourceStack> context) {
        String energyString = StringArgumentType.getString(context, "EU");
        UUID player = Objects.requireNonNull(context.getSource().getPlayer(), "Cannot execute the command.").getUUID();
        try {
            BigInteger energy = new BigDecimal(energyString).toBigInteger();
            WirelessEnergyManager.setUserEU(player, energy);
        } catch (Exception e) {
            throw new RuntimeException("\"" + energyString + "\" is not a valid number.");
        }
        return 0;
    }

    private static int addEnergy(CommandContext<CommandSourceStack> context) {
        String energyString = StringArgumentType.getString(context, "EU");
        UUID player = Objects.requireNonNull(context.getSource().getPlayer(), "Cannot execute the command.").getUUID();
        try {
            BigInteger energy = new BigDecimal(energyString).toBigInteger();
            BigInteger EU = WirelessEnergyManager.getUserEU(player);
            WirelessEnergyManager.setUserEU(player, EU.add(energy));
        } catch (Exception e) {
            throw new RuntimeException("\"" + energyString + "\" is not a valid number.");
        }
        return 1;
    }
}
