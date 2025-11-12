package cn.qiuye.gtl_extend.mixin.gtladd;

import org.gtlcore.gtlcore.integration.gtmt.NewGTValues;
import org.gtlcore.gtlcore.utils.NumberUtils;

import com.gtladd.gtladditions.common.machine.muiltblock.part.WirelessEnergyNetworkTerminalPartMachineBase;
import com.gtladd.gtladditions.integration.jade.provider.WirelessEnergyNetworkTerminalProvider;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;

import com.hepdd.gtmthings.api.misc.WirelessEnergyManager;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

import static com.hepdd.gtmthings.utils.TeamUtil.*;
import static net.minecraft.ChatFormatting.*;
import static org.gtlcore.gtlcore.utils.TextUtil.GTL_CORE$VC;

@Mixin(WirelessEnergyNetworkTerminalProvider.class)
public class WirelessEnergyNetworkTerminalProviderMixin {

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public void appendTooltip(ITooltip tooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        if (blockAccessor.getBlockEntity() instanceof IMachineBlockEntity blockEntity) {
            if (blockEntity.getMetaMachine() instanceof WirelessEnergyNetworkTerminalPartMachineBase) {
                CompoundTag serverData = blockAccessor.getServerData();
                if (!serverData.hasUUID("uuid")) {
                    tooltip.add(Component.translatable("gtmthings.machine.wireless_energy_hatch.tooltip.1"));
                } else {
                    UUID uuid = serverData.getUUID("uuid");
                    if (hasOwner(blockAccessor.getLevel(), uuid)) {
                        tooltip.add(Component.translatable("gtmthings.machine.wireless_energy_hatch.tooltip.2", GetName(blockAccessor.getLevel(), uuid)));
                    } else {
                        tooltip.add(Component.translatable("gtmthings.machine.wireless_energy_hatch.tooltip.3", uuid));
                    }

                    var totalEu = WirelessEnergyManager.getUserEU(uuid);
                    var abs = totalEu.abs();
                    var longEu = org.gtlcore.gtlcore.utils.NumberUtils.getLongValue(totalEu);
                    var energyTier = longEu == Long.MAX_VALUE ? GTValues.MAX_TRUE : NumberUtils.getFakeVoltageTier(longEu);
                    Component text = Component.literal(cn.qiuye.gtl_extend.utils.NumberUtils.formatBigIntegerNumberOrSic(abs)).withStyle(RED)
                            .append(Component.literal(" EU").withStyle(RESET)
                                    .append(Component.literal(" (").withStyle(GREEN)
                                            .append(Component
                                                    .translatable("gtceu.top.electricity",
                                                            String.format("%.2e", new BigDecimal(abs).divide(BigDecimal.valueOf(GTValues.VEX[energyTier]), 3, RoundingMode.DOWN).doubleValue()),
                                                            NewGTValues.VNF[energyTier])
                                                    .withStyle(style -> style.withColor(GTL_CORE$VC[Math.min(energyTier, 14)])))
                                            .append(Component.literal(")").withStyle(GREEN))));

                    tooltip.add(Component.translatable("gtladditions.machine.wireless_energy_network_terminal.tooltips.1", text));
                }
            }
        }
    }
}
