package cn.qiuye.gtl_extend.common.machine.multiblock.electric;

import cn.qiuye.gtl_extend.api.machine.IThreadModifierParallelMachine;
import cn.qiuye.gtl_extend.common.machine.trait.MultipleRecipesLogic;
import cn.qiuye.gtl_extend.config.GTLExtendConfigHolder;
import cn.qiuye.gtl_extend.utils.NumberUtils;

import org.gtlcore.gtlcore.api.machine.multiblock.NoEnergyMultiblockMachine;
import org.gtlcore.gtlcore.utils.MachineIO;

import com.gregtechceu.gtceu.api.machine.ConditionalSubscriptionHandler;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.utils.FormattingUtil;

import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.BlockHitResult;

import java.math.BigInteger;
import java.util.List;
import java.util.UUID;

import javax.annotation.ParametersAreNonnullByDefault;

import static cn.qiuye.gtl_extend.common.data.GTL_Extend_Materials.ETERNALBLUEDREAM;

import com.hepdd.gtmthings.api.misc.WirelessEnergyManager;
import com.hepdd.gtmthings.utils.TeamUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class BlackHoleMatterDecompressor extends NoEnergyMultiblockMachine implements IThreadModifierParallelMachine {

    public static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            BlackHoleMatterDecompressor.class, NoEnergyMultiblockMachine.MANAGED_FIELD_HOLDER);
    // 常量定义
    private static final int BASE_PARALLEL = 64;
    private static final long BASE_EU_COST = 5277655810867200L;
    @Persisted
    private long eternalbluedream; // 永恒蓝梦流体存储量
    @Persisted
    private int oc = 0;     // 当前电路配置编号
    @Persisted
    private UUID userId;// 绑定用户ID
    private boolean eut = false;

    protected ConditionalSubscriptionHandler StartupSubs;

    public BlackHoleMatterDecompressor(IMachineBlockEntity holder, Object... args) {
        super(holder, args);
        this.StartupSubs = new ConditionalSubscriptionHandler(this, this::StartupUpdate, this::isFormed);
    }

    @Override
    protected RecipeLogic createRecipeLogic(Object @NotNull... args) {
        return new MultipleRecipesLogic(this);
    }

    // 判断是否启用无限蓝梦模式
    private static boolean isInfinityDreamEnabled() {
        return GTLExtendConfigHolder.INSTANCE != null && GTLExtendConfigHolder.INSTANCE.enableInfinityDreamAndDreamHostCrafting;
    }

    @Override
    public boolean beforeWorking(@Nullable GTRecipe recipe) {
		if (this.userId != null) {
			if (WirelessEnergyManager.getUserEU(userId).compareTo(BigInteger.valueOf(getRecipeEUt())) > 0) {
				this.eut = true;
				return true;
			} else {
				this.eut = false;
				return false;
			}
		}
		return false;
    }

    @Override
    public boolean onWorking() {
        boolean value = super.onWorking();
        if (this.eut && this.userId != null) {
            WirelessEnergyManager.addEUToGlobalEnergyMap(userId, -getRecipeEUt(), this);
        }
        return value;
    }

    @Override
    public void afterWorking() {
        super.afterWorking();
    }

    @Override
    public void onStructureFormed() {
        super.onStructureFormed();
        StartupSubs.initialize(getLevel());
    }

    // 获取超频次数（电路配置映射）
    private int calculateOverclockTimes() {
        return switch (Math.min(oc, 4)) {
            case 2 -> 4;
            case 3 -> 16;
            case 4 -> 64;
            default -> 1;
        };
    }

    // 计算启动能耗
    private long getRecipeEUt() {
        int ocTimes = calculateOverclockTimes();
        return (long) (BASE_EU_COST * Math.pow(32, ocTimes));
    }

    // 计算实际并行（考虑蓝梦流体加成）
    private int calculateParallel() {
        int base = getBaseParallel();
        if (!isInfinityDreamEnabled()) return base;

        // 每1000B流体翻倍一次，但不超过int最大值
        long multiplier = eternalbluedream / 1_000_000L;
        return (int) Math.min(base * (1L << multiplier), Integer.MAX_VALUE);
    }

    // 计算基础并行（电路编号的8次方，1号特殊处理）
    private int getBaseParallel() {
        return (oc == 1) ? BASE_PARALLEL : (int) Math.pow(oc, 8);
    }

    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    // 电路配置更新逻辑
    protected void StartupUpdate() {
        if (getOffsetTimer() % 20 == 0) {
            oc = 0;
            // 处理额外流体输入（永恒蓝梦）
            if (isInfinityDreamEnabled()) {
                if (MachineIO.inputFluid(this, ETERNALBLUEDREAM.getFluid(100000000))) {
                    eternalbluedream += 100000000;
                }
            }
            int[] priorityOrder = { 8, 7, 6, 5, 4, 3, 2, 1 };
            for (int config : priorityOrder) {
                if (MachineIO.notConsumableCircuit(this, config)) {
                    this.oc = config;
                    return;
                }
            }
        }
    }

    // 玩家交互绑定
    @Override
    public boolean shouldOpenUI(Player player, InteractionHand hand, BlockHitResult hit) {
        if (this.userId == null || !this.userId.equals(player.getUUID())) {
            this.userId = player.getUUID();
        }
        return true;
    }

    @Override
    public void addDisplayText(List<Component> textList) {
        super.addDisplayText(textList);
        if (isFormed()) {
            // 模式状态显示
            textList.add(Component.translatable("gtl_extend_machine_mode",
                    isInfinityDreamEnabled() ? "蓝梦模式" : "功率模式"));

            if (isInfinityDreamEnabled()) {
                textList.add(Component.literal("永恒蓝梦: " +
                        FormattingUtil.formatNumbers(eternalbluedream) + " mB"));
                textList.add(Component.literal("基础并行: " + getBaseParallel()));
            } else {
                double actualMultiplier = getPowerMultiplier() * Math.pow(2, calculateOverclockTimes());
                String powerMultiplierDisplay = (actualMultiplier >= Double.MAX_VALUE / 1e3) ? "∞" : FormattingUtil.formatNumbers(actualMultiplier);
                textList.add(Component.literal("当前功率倍率: " + powerMultiplierDisplay));
                // 显示无线电网允许的最大功率（仅在非蓝梦模式下显示）
                if (userId != null) {
                    BigInteger totalEU = WirelessEnergyManager.getUserEU(userId);
                    if (totalEU.compareTo(BigInteger.ZERO) > 0) {
                        BigInteger maxAllowedEU = totalEU.divide(BigInteger.valueOf(100));
                        long maxAllowedEULong;
                        try {
                            maxAllowedEULong = maxAllowedEU.longValueExact();
                        } catch (ArithmeticException e) {
                            maxAllowedEULong = Long.MAX_VALUE;
                        }
                        textList.add(Component.literal("最大允许功率: " + FormattingUtil.formatNumbers(maxAllowedEULong) + " EU/t"));
                    }
                }
            }
            // 用户无线电网信息（公共显示部分）
            if (userId != null) {
                textList.add(Component.translatable("gtmthings.machine.wireless_energy_monitor.tooltip.0",
                        TeamUtil.GetName(getLevel(), userId)));
                textList.add(Component.translatable("gtmthings.machine.wireless_energy_monitor.tooltip.1",
                        NumberUtils.formatBigIntegerNumberOrSic(WirelessEnergyManager.getUserEU(userId))));
            }
            // 公共信息
            textList.add(Component.literal("耗能：" + NumberUtils.formatBigIntegerNumberOrSic(BigInteger.valueOf(getRecipeEUt())) + " EU/t"));
            textList.add(Component.literal("最终并行: " + calculateParallel()));
            textList.add(Component.translatable("gtl_extend.machine.circuit",
                    oc,  // 直接显示原始电路编号
                    getPowerMultiplier() // 仅计算倍率
            ));
        }
    }

    private double getPowerMultiplier() {
        // 明确倍率规则：电路编号限制到4，但显示仍用原始值
        int effectiveConfig = Math.min(oc, 4);
        return switch (effectiveConfig) {
            case 2 -> 32.0;
            case 3 -> 1024.0;
            case 4 -> 32768.0;
            default -> 1.0;
        };
    }

    /**
     * @return .
     */
    @Override
    public int getMaxParallel() {
        return calculateParallel();
    }

    /**
     * @return .
     */
    @Override
    public int getExtendlThread() {
        return Integer.MAX_VALUE;
    }

    /**
     * @return .
     */
    @Override
    public int getExtendlDuration() {
        return (int) (4800 / Math.pow(2, this.oc));
    }
}
