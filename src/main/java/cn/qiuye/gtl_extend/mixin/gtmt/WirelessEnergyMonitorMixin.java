package cn.qiuye.gtl_extend.mixin.gtmt;

import cn.qiuye.gtl_extend.utils.NumberUtils;

import org.gtlcore.gtlcore.integration.gtmt.NewGTValues;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.feature.IFancyUIMachine;
import com.gregtechceu.gtceu.utils.FormattingUtil;
import com.gregtechceu.gtceu.utils.GTUtil;

import com.lowdragmc.lowdraglib.gui.widget.ComponentPanelWidget;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.Style;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.hepdd.gtmthings.api.misc.WirelessEnergyManager;
import com.hepdd.gtmthings.common.block.machine.electric.WirelessEnergyMonitor;
import com.hepdd.gtmthings.utils.TeamUtil;
import com.mojang.datafixers.util.Pair;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = WirelessEnergyMonitor.class, priority = 9999)
public class WirelessEnergyMonitorMixin extends MetaMachine implements IFancyUIMachine {

    @Shadow(remap = false)
    protected static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(WirelessEnergyMonitor.class, MetaMachine.MANAGED_FIELD_HOLDER);
    @Shadow(remap = false)
    private static final BigInteger BIG_INTEGER_MAX_LONG = BigInteger.valueOf(Long.MAX_VALUE);
    @Shadow(remap = false)
    public static int p;
    @Shadow(remap = false)
    public static BlockPos pPos;
    @Shadow(remap = false)
    private UUID userid;
    @Shadow(remap = false)
    private BigInteger beforeEnergy;
    @Shadow(remap = false)
    private ArrayList<BigInteger> longArrayList;
    @Shadow(remap = false)
    private List<Map.Entry<Pair<UUID, MetaMachine>, Long>> sortedEntries = null;
    @Shadow(remap = false)
    private boolean all = false;

    public WirelessEnergyMonitorMixin(IMachineBlockEntity holder) {
        super(holder);
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    private void addDisplayText(@NotNull List<Component> textList) {
        BigInteger energyTotal = WirelessEnergyManager.getUserEU(this.userid);
        textList.add(Component.translatable("gtmthings.machine.wireless_energy_monitor.tooltip.0", TeamUtil.GetName(this.holder.level(), this.userid)).withStyle(ChatFormatting.AQUA));
        textList.add(Component.translatable("gtmthings.machine.wireless_energy_monitor.tooltip.1", NumberUtils.formatBigIntegerNumberOrSic(energyTotal)).withStyle(ChatFormatting.GRAY));
        BigDecimal avgEnergy = this.getAvgUsage(energyTotal);
        BigDecimal absAvgEnergy = avgEnergy.abs();

        long absAvgLongEut = org.gtlcore.gtlcore.utils.NumberUtils.getLongValue(absAvgEnergy.toBigInteger());
        int avgEnergyTier = absAvgLongEut == Long.MAX_VALUE ? GTValues.MAX_TRUE : org.gtlcore.gtlcore.utils.NumberUtils.getFakeVoltageTier(absAvgLongEut);
        Component voltageName = Component.literal(NewGTValues.VNF[avgEnergyTier]);
        BigDecimal voltageAmperage = absAvgEnergy.divide(BigDecimal.valueOf(GTValues.VEX[avgEnergyTier]), 2, RoundingMode.FLOOR);

        if (avgEnergy.compareTo(BigDecimal.valueOf(0L)) >= 0) {
            textList.add(Component.translatable("gtl_extend.machine.wireless_energy_monitor.tooltip.input",
                    Component.literal(NumberUtils.formatBigDecimalNumberOrSic(absAvgEnergy)).withStyle(ChatFormatting.BLUE), NumberUtils.formatBigDecimalNumberOrSic(voltageAmperage), voltageName).withStyle(ChatFormatting.GRAY));
            textList.add(Component.translatable("gtceu.multiblock.power_substation.time_to_fill", Component.translatable("gtmthings.machine.wireless_energy_monitor.tooltip.time_to_fill")).withStyle(ChatFormatting.GRAY));
        } else {
            textList.add(Component.translatable("gtl_extend.machine.wireless_energy_monitor.tooltip.output",
                    Component.literal(NumberUtils.formatBigDecimalNumberOrSic(absAvgEnergy)).withStyle(ChatFormatting.BLUE), NumberUtils.formatBigDecimalNumberOrSic(voltageAmperage), voltageName).withStyle(ChatFormatting.GRAY));
            textList.add(Component.translatable("gtceu.multiblock.power_substation.time_to_drain", getTimeToFillDrainText(energyTotal.divide(avgEnergy.abs().toBigInteger().multiply(BigInteger.valueOf(20L))))).withStyle(ChatFormatting.GRAY));
        }

        textList.add(Component.translatable("gtmthings.machine.wireless_energy_monitor.tooltip.statistics").append(ComponentPanelWidget.withButton(this.all ? Component.translatable("gtmthings.machine.wireless_energy_monitor.tooltip.all") : Component.translatable("gtmthings.machine.wireless_energy_monitor.tooltip.team"), "all")));

        for (Map.Entry<Pair<UUID, MetaMachine>, Long> m : this.getSortedEntries()) {
            UUID uuid = (UUID) ((Pair<?, ?>) m.getKey()).getFirst();
            if (this.all || TeamUtil.getTeamUUID(uuid) == TeamUtil.getTeamUUID(this.userid)) {
                MetaMachine machine = (MetaMachine) ((Pair<?, ?>) m.getKey()).getSecond();
                long eut = m.getValue();
                String pos = machine.getPos().toShortString();
                if (eut > 0L) {
                    textList.add(Component.translatable(machine.getBlockState().getBlock().getDescriptionId()).withStyle(Style.EMPTY.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.translatable("recipe.condition.dimension.tooltip", new Object[] { machine.getLevel().dimension().location() }).append(" [").append(pos).append("] ").append(Component.translatable("gtmthings.machine.wireless_energy_monitor.tooltip.0", TeamUtil.GetName(this.holder.level(), uuid)))))).append(" +").append(FormattingUtil.formatNumbers(eut)).append(" EU/t (").append(GTValues.VNF[GTUtil.getFloorTierByVoltage(eut)]).append(")").append(ComponentPanelWidget.withButton(Component.literal(" [ ] "), pos)));
                } else {
                    textList.add(Component.translatable(machine.getBlockState().getBlock().getDescriptionId()).withStyle(Style.EMPTY.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.translatable("recipe.condition.dimension.tooltip", new Object[] { machine.getLevel().dimension().location() }).append(" [").append(pos).append("] ").append(Component.translatable("gtmthings.machine.wireless_energy_monitor.tooltip.0", TeamUtil.GetName(this.holder.level(), uuid)))))).append(" -").append(FormattingUtil.formatNumbers(-eut)).append(" EU/t (").append(GTValues.VNF[GTUtil.getFloorTierByVoltage(-eut)]).append(")").append(ComponentPanelWidget.withButton(Component.literal(" [ ] "), pos)));
                }
            }
        }
    }

    @Shadow(remap = false)
    private List<Map.Entry<Pair<UUID, MetaMachine>, Long>> getSortedEntries() {
        if (this.sortedEntries == null || this.getOffsetTimer() % 20L == 0L) {
            this.sortedEntries = WirelessEnergyManager.MachineData.entrySet().stream().sorted(Map.Entry.comparingByValue()).toList();
            WirelessEnergyManager.MachineData.clear();
        }

        return this.sortedEntries;
    }

    @Shadow(remap = false)
    private static Component getTimeToFillDrainText(BigInteger timeToFillSeconds) {
        if (timeToFillSeconds.compareTo(BIG_INTEGER_MAX_LONG) > 0) {
            timeToFillSeconds = BIG_INTEGER_MAX_LONG;
        }

        Duration duration = Duration.ofSeconds(timeToFillSeconds.longValue());
        String key;
        long fillTime;
        if (duration.getSeconds() <= 180L) {
            fillTime = duration.getSeconds();
            key = "gtceu.multiblock.power_substation.time_seconds";
        } else if (duration.toMinutes() <= 180L) {
            fillTime = duration.toMinutes();
            key = "gtceu.multiblock.power_substation.time_minutes";
        } else if (duration.toHours() <= 72L) {
            fillTime = duration.toHours();
            key = "gtceu.multiblock.power_substation.time_hours";
        } else if (duration.toDays() <= 730L) {
            fillTime = duration.toDays();
            key = "gtceu.multiblock.power_substation.time_days";
        } else {
            if (duration.toDays() / 365L >= 1000000L) {
                return Component.translatable("gtceu.multiblock.power_substation.time_forever");
            }

            fillTime = duration.toDays() / 365L;
            key = "gtceu.multiblock.power_substation.time_years";
        }

        return Component.translatable(key, FormattingUtil.formatNumbers(fillTime));
    }

    @Shadow(remap = false)
    private BigDecimal getAvgUsage(BigInteger now) {
        BigInteger changed = now.subtract(this.beforeEnergy);
        this.beforeEnergy = now;
        if (this.longArrayList.size() >= 20) {
            this.longArrayList.remove(0);
        }

        this.longArrayList.add(changed);
        return calculateAverage(this.longArrayList);
    }

    @Shadow(remap = false)
    private static BigDecimal calculateAverage(ArrayList<BigInteger> bigIntegers) {
        BigInteger sum = BigInteger.ZERO;

        for (BigInteger bi : bigIntegers) {
            sum = sum.add(bi);
        }

        return (new BigDecimal(sum)).divide(new BigDecimal(bigIntegers.size()), RoundingMode.HALF_UP);
    }
}
