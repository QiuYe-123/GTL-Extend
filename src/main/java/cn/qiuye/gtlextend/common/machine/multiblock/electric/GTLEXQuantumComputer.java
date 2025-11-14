package cn.qiuye.gtlextend.common.machine.multiblock.electric;

import cn.qiuye.gtlextend.utils.NumberUtils;

import org.gtlcore.gtlcore.api.machine.multiblock.NoEnergyMultiblockMachine;
import org.gtlcore.gtlcore.utils.MachineIO;
import org.gtlcore.gtlcore.utils.Registries;
import org.gtlcore.gtlcore.utils.TextUtil;

import com.gregtechceu.gtceu.api.block.ICoilType;
import com.gregtechceu.gtceu.api.capability.IControllable;
import com.gregtechceu.gtceu.api.capability.IOpticalComputationProvider;
import com.gregtechceu.gtceu.api.capability.recipe.FluidRecipeCapability;
import com.gregtechceu.gtceu.api.machine.ConditionalSubscriptionHandler;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.TickableSubscription;
import com.gregtechceu.gtceu.api.machine.multiblock.MultiblockDisplayText;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.common.block.CoilBlock;
import com.gregtechceu.gtceu.utils.FormattingUtil;
import com.gregtechceu.gtceu.utils.GTTransferUtils;

import com.lowdragmc.lowdraglib.misc.FluidTransferList;
import com.lowdragmc.lowdraglib.side.fluid.FluidStack;
import com.lowdragmc.lowdraglib.side.fluid.IFluidTransfer;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;

import com.hepdd.gtmthings.api.misc.WirelessEnergyManager;
import com.hepdd.gtmthings.utils.TeamUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class GTLEXQuantumComputer extends NoEnergyMultiblockMachine
                                  implements IOpticalComputationProvider, IControllable {

    public static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            GTLEXQuantumComputer.class, NoEnergyMultiblockMachine.MANAGED_FIELD_HOLDER);

    @Persisted
    private int oc = 0;     // 当前电路配置编号
    public int allocatedCWUt = 0;
    @Persisted
    public long totalCWU = 0;
    @Persisted
    public int maxCWUt = 0;
    private ICoilType coilType = CoilBlock.CoilType.CUPRONICKEL;
    @Persisted
    private UUID userId;// 绑定用户ID
    String lastAllocatedCWUt = "0";
    boolean canProvideCWUt = true;

    private boolean hasNotEnoughEnergy;
    private IFluidTransfer coolantHandler; // 添加冷却液处理器

    @Nullable
    protected TickableSubscription tickSubs;

    protected ConditionalSubscriptionHandler StartupSubs;

    public GTLEXQuantumComputer(IMachineBlockEntity holder, Object... args) {
        super(holder, args);
        this.StartupSubs = new ConditionalSubscriptionHandler(this, this::StartupUpdate, this::isFormed);
        this.coolantHandler = new FluidTransferList(new ArrayList<>()); // 初始化冷却液处理器
    }

    // 电路配置更新逻辑
    protected void StartupUpdate() {
        if (getOffsetTimer() % 20 == 0) {
            oc = 0;
            int[] priorityOrder = { 8, 7, 6, 5, 4, 3, 2, 1 };
            for (int config : priorityOrder) {
                if (MachineIO.notConsumableCircuit(this, config)) {
                    this.oc = config;
                    return;
                }
            }
        }
    }

    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    /**
     * @param cwut     Maximum amount of CWU/t requested.
     * @param simulate .
     * @param seen     The Optical Computation Providers already checked
     * @return .
     */
    @Override
    public int requestCWUt(int cwut, boolean simulate, Collection<IOpticalComputationProvider> seen) {
        seen.add(this);
        if (!canProvideCWUt) return 0;
        return !hasNotEnoughEnergy ? allocatedCWUt(cwut, simulate) : 0;
    }

    private int allocatedCWUt(int cwut, boolean simulate) {
        if (totalCWU < getMaxCWUt()) {
            if (this.userId != null && this.oc >= 1)
                totalCWU += getMaxCWUt();
            maxCWUt = 0;
        }
        int maxCWUt = getMaxCWUt();
        int availableCWUt = maxCWUt - this.allocatedCWUt;
        int toAllocate = Math.min(cwut, (int) Math.min(availableCWUt, totalCWU));
        if (!simulate) {
            this.allocatedCWUt += toAllocate;
        }
        return toAllocate;
    }

    /**
     * @param seen The Optical Computation Providers already checked
     * @return .
     */
    @Override
    public int getMaxCWUt(Collection<IOpticalComputationProvider> seen) {
        seen.add(this);
        return calculate(coilType.getCoilTemperature(), oc);
    }

    /**
     * @param seen The Optical Computation Providers already checked
     * @return .
     */
    @Override
    public boolean canBridge(Collection<IOpticalComputationProvider> seen) {
        seen.add(this);
        return true;
    }

    public void tick() {
        if (isWorkingEnabled()) {
            consumeEnergy();
            consumeCoolant(); // 添加冷却液消耗
        }
        totalCWU -= allocatedCWUt;
        lastAllocatedCWUt = String.valueOf(allocatedCWUt);
        if (getRecipeLogic().isSuspend()) {
            allocatedCWUt = 0;
            canProvideCWUt = false;
            return;
        } else {
            canProvideCWUt = true;
        }
        if (allocatedCWUt != 0) {
            getRecipeLogic().setStatus(RecipeLogic.Status.WORKING);
            allocatedCWUt = 0;
        } else {
            getRecipeLogic().setStatus(RecipeLogic.Status.IDLE);
        }
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (getLevel() instanceof ServerLevel serverLevel) {
            serverLevel.getServer().tell(new TickTask(0, this::updateTickSubscription));
        }
    }

    @Override
    public void onUnload() {
        super.onUnload();
        if (tickSubs != null) {
            tickSubs.unsubscribe();
            tickSubs = null;
        }
    }

    protected void updateTickSubscription() {
        if (isFormed && oc >= 1) {
            tickSubs = subscribeServerTick(tickSubs, this::tick);
        } else if (tickSubs != null) {
            tickSubs.unsubscribe();
            tickSubs = null;
        }
    }

    @Override
    public void onStructureFormed() {
        super.onStructureFormed();
        StartupSubs.initialize(getLevel());
        var type = getMultiblockState().getMatchContext().get("CoilType");
        if (type instanceof ICoilType coil) {
            this.coilType = coil;
        }
        // 获取冷却液容器
        List<IFluidTransfer> coolantContainers = new ArrayList<>();
        for (var part : getParts()) {
            for (var handler : part.getRecipeHandlers()) {
                if (handler.getCapability() == FluidRecipeCapability.CAP &&
                        handler instanceof IFluidTransfer fluidHandler) {
                    coolantContainers.add(fluidHandler);
                }
            }
        }
        this.coolantHandler = new FluidTransferList(coolantContainers);
    }

    @Override
    public void onChanged() {
        maxCWUt = 0;
    }

    private void consumeEnergy() {
        if (this.userId != null && WirelessEnergyManager.getUserEU(userId).compareTo(energy()) > 0) {
            WirelessEnergyManager.addEUToGlobalEnergyMap(
                    this.userId,
                    energy().negate(),
                    this);
            this.hasNotEnoughEnergy = false;
        } else {
            this.hasNotEnoughEnergy = true;
            getRecipeLogic().setStatus(RecipeLogic.Status.WAITING);
        }
    }

    /**
     * 根据最大算力消耗冷却液
     */
    private void consumeCoolant() {
        int maxCWUt = getMaxCWUt();
        if (maxCWUt <= 0) return;

        // 计算冷却液需求 - 每1M CWU/t消耗1L/t冷却液
        long coolantToDrain = (long) Math.ceil(maxCWUt / 1_000.0);
        if (coolantToDrain <= 0) return;

        // 尝试抽取冷却液
        FluidStack coolantStack = getCoolantStack(coolantToDrain);
        FluidStack drained = GTTransferUtils.drainFluidAccountNotifiableList(coolantHandler, coolantStack, false);

        // 如果冷却液不足，停止工作
        if (drained.getAmount() < coolantToDrain) {
            this.hasNotEnoughEnergy = true;
            getRecipeLogic().setStatus(RecipeLogic.Status.WAITING);
        } else {
            // 实际抽取冷却液
            GTTransferUtils.drainFluidAccountNotifiableList(coolantHandler, coolantStack, true);
        }
    }

    /**
     * 获取冷却液堆栈
     */
    private FluidStack getCoolantStack(long amount) {
        return FluidStack.create(Registries.getFluid("kubejs:gelid_cryotheum"), amount);
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
        MultiblockDisplayText.builder(textList, isFormed());
        if (isFormed()) {
            // 用户无线电网信息（公共显示部分）
            if (userId != null) {
                textList.add(Component.translatable("gtmthings.machine.wireless_energy_monitor.tooltip.0",
                        TeamUtil.GetName(getLevel(), userId)));
                textList.add(Component.translatable("gtmthings.machine.wireless_energy_monitor.tooltip.1",
                        NumberUtils.formatBigIntegerNumberOrSic(WirelessEnergyManager.getUserEU(userId))));
            }
            // 公共信息
            textList.add(Component.literal("启动耗能：" + NumberUtils.formatBigIntegerNumberOrSic(energy()) + " EU/t"));
            textList.add(Component.translatable("gtceu.multiblock.blast_furnace.max_temperature",
                    Component.translatable(FormattingUtil.formatNumbers(coilType.getCoilTemperature()) + "K")
                            .setStyle(Style.EMPTY.withColor(ChatFormatting.RED))));
            textList.add(Component.translatable(
                    "gtceu.multiblock.hpca.computation", Component.literal(
                            lastAllocatedCWUt + " / " +
                                    (calculate(coilType.getCoilTemperature(), oc) == Integer.MAX_VALUE ? TextUtil.full_color("无尽") : getMaxCWUt()) +
                                    " CWU/t")
                            .withStyle(ChatFormatting.AQUA))
                    .withStyle(ChatFormatting.GRAY));
            textList.add(Component.translatable("gtl_extend.machine.circuit",
                    oc  // 直接显示原始电路编号
            ));

            // 添加冷却液消耗显示
            long coolantToDrain = (long) Math.ceil(getMaxCWUt() / 1_000_000.0);
            textList.add(Component.translatable("gtl_extend.machine.coolant_usage",
                    coolantToDrain + " L/t")
                    .withStyle(ChatFormatting.BLUE));
        }
    }

    private static int calculate(int k, int oc) {
        // 确保输入在有效范围内
        k = Math.max(1, Math.min(k, 96000));
        oc = Math.max(1, Math.min(oc, 8));

        // 计算温度(k)的影响部分 (占50%)
        double kNormalized = (double) (k - 1) / (96000 - 1); // 归一化到0-1范围
        double kContribution = 0.5 * kNormalized; // 温度影响占50%

        // 计算电路编号(oc)的影响部分 (占50%)
        double ocNormalized = (double) (oc - 1) / (8 - 1); // 归一化到0-1范围
        double ocContribution = 0.5 * ocNormalized; // 电路编号影响占50%

        // 合并两部分影响
        double totalContribution = kContribution + ocContribution;

        // 计算输出值，确保在1024到Integer.MAX_VALUE之间
        double minValue = 1024;
        double maxValue = Integer.MAX_VALUE;
        double value = minValue + (maxValue - minValue) * totalContribution;

        // 四舍五入到最接近的整数
        long rounded = Math.round(value);

        // 确保结果在有效范围内
        if (rounded < minValue) {
            return (int) minValue;
        } else if (rounded > maxValue) {
            return (int) maxValue;
        }

        return (int) rounded;
    }

    public BigInteger energy() {
        return calculateEnergyConsumption(calculate(coilType.getCoilTemperature(), oc));
    }

    // 电量消耗函数
    public static BigInteger calculateEnergyConsumption(int p) {
        // 确保p在范围内
        p = Math.max(1024, p);

        // 计算常数A和B
        double log10Emin = Math.log10(2147483647.0);
        double log10Emax = 20.0;
        double A = (log10Emax - log10Emin) / (Integer.MAX_VALUE - 1024);
        double B = log10Emin - A * 1024;

        // 计算log10(e)
        double log10E = A * p + B;

        // 计算e = 10^log10E
        double eDouble = Math.pow(10, log10E);

        // 将double转换为BigInteger，四舍五入
        BigDecimal eBigDecimal = new BigDecimal(eDouble);
        BigInteger eBigInteger = eBigDecimal.setScale(0, RoundingMode.HALF_UP).toBigInteger();

        // 确保e在范围内
        BigInteger minE = BigInteger.valueOf(2147483647L);
        BigInteger maxE = new BigInteger("100000000000000000000"); // 10^20

        if (eBigInteger.compareTo(minE) < 0) {
            return minE;
        } else if (eBigInteger.compareTo(maxE) > 0) {
            return maxE;
        }

        return eBigInteger;
    }
}
