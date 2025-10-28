package cn.qiuye.gtl_extend.mixin.gtl;

import org.gtlcore.gtlcore.api.machine.multiblock.IModularMachineModule;
import org.gtlcore.gtlcore.common.data.GTLBlocks;
import org.gtlcore.gtlcore.common.data.GTLRecipeModifiers;
import org.gtlcore.gtlcore.common.data.machines.AdvancedMultiBlockMachine;
import org.gtlcore.gtlcore.common.machine.multiblock.electric.SpaceElevatorMachine;
import org.gtlcore.gtlcore.common.machine.multiblock.electric.SpaceElevatorModuleMachine;

import com.gregtechceu.gtceu.api.capability.GTCapabilityHelper;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.feature.IMachineLife;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableElectricMultiblockMachine;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.OverclockingLogic;
import com.gregtechceu.gtceu.api.recipe.RecipeHelper;
import com.gregtechceu.gtceu.api.recipe.logic.OCParams;
import com.gregtechceu.gtceu.api.recipe.logic.OCResult;
import com.gregtechceu.gtceu.common.data.GTRecipeModifiers;
import com.gregtechceu.gtceu.utils.FormattingUtil;

import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;

import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.*;

@Mixin(SpaceElevatorModuleMachine.class)
public abstract class SpaceElevatorModuleMachineMixin extends WorkableElectricMultiblockMachine implements IModularMachineModule<SpaceElevatorMachine, SpaceElevatorModuleMachine>, IMachineLife {

    @Shadow(remap = false)
    protected static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(SpaceElevatorModuleMachineMixin.class, WorkableElectricMultiblockMachine.MANAGED_FIELD_HOLDER);
    @DescSynced
    @Unique
    private int gtl_extend$SpaceElevatorTier = 0;
    @Unique
    private int gtl_extend$ModuleTier = 0;
    @Unique
    private final boolean gtl_extend$SEPMTier;
    @Unique
    private BlockPos gtl_extend$controller;
    @Unique
    private int gtl_extend$tier = 0;

    public SpaceElevatorModuleMachineMixin(IMachineBlockEntity holder, boolean SEPMTier, Object... args) {
        super(holder, args);
        this.gtl_extend$SEPMTier = SEPMTier;
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    private void getSpaceElevatorTier() {
        if (this.gtl_extend$controller != null && this.getLevel() != null) {
            RecipeLogic logic = GTCapabilityHelper.getRecipeLogic(this.getLevel(), this.gtl_extend$controller, (Direction) null);
            if (logic != null && logic.getMachine().getDefinition() == AdvancedMultiBlockMachine.SPACE_ELEVATOR) {
                if (logic.isWorking() && logic.getProgress() > 80) {
                    this.gtl_extend$SpaceElevatorTier = ((SpaceElevatorMachine) logic.machine).getTier() - 7;
                    this.gtl_extend$ModuleTier = ((SpaceElevatorMachine) logic.machine).getCasingTier();
                } else if (!logic.isWorking()) {
                    this.gtl_extend$SpaceElevatorTier = 0;
                    this.gtl_extend$ModuleTier = 0;
                }
            } else if (logic == null) {
                this.gtl_extend$SpaceElevatorTier = 0;
                this.gtl_extend$ModuleTier = 0;
            }
        } else {
            Level level = this.getLevel();
            BlockPos pos = this.getPos();
            BlockPos[] coordinates = new BlockPos[] { pos.offset(8, -2, 3), pos.offset(8, -2, -3), pos.offset(-8, -2, 3), pos.offset(-8, -2, -3), pos.offset(3, -2, 8), pos.offset(-3, -2, 8), pos.offset(3, -2, -8), pos.offset(-3, -2, -8) };

            for (BlockPos i : coordinates) {
                if (level != null && level.getBlockState(i).getBlock() == GTLBlocks.POWER_CORE.get()) {
                    BlockPos[] coordinatess = new BlockPos[] { i.offset(3, 2, 0), i.offset(-3, 2, 0), i.offset(0, 2, 3), i.offset(0, 2, -3) };

                    for (BlockPos j : coordinatess) {
                        RecipeLogic logic = GTCapabilityHelper.getRecipeLogic(level, j, (Direction) null);
                        if (logic != null && logic.getMachine().getDefinition() == AdvancedMultiBlockMachine.SPACE_ELEVATOR) {
                            this.gtl_extend$controller = j;
                            if (logic.isWorking() && logic.getProgress() > 80) {
                                this.gtl_extend$SpaceElevatorTier = ((SpaceElevatorMachine) logic.machine).getTier() - 7;
                                this.gtl_extend$ModuleTier = ((SpaceElevatorMachine) logic.machine).getCasingTier();
                            } else if (!logic.isWorking()) {
                                this.gtl_extend$SpaceElevatorTier = 0;
                                this.gtl_extend$ModuleTier = 0;
                            }
                        }
                    }
                }
            }
        }
    }

    @Shadow(remap = false)
    public @NotNull ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    @Nullable
    public static GTRecipe recipeModifier(MetaMachine machine, @NotNull GTRecipe recipe, @NotNull OCParams params,
                                          @NotNull OCResult result) {
        if (machine instanceof SpaceElevatorModuleMachineMixin moduleMachine) {
            moduleMachine.getSpaceElevatorTier();
            if (moduleMachine.gtl_extend$SpaceElevatorTier < 1) {
                return null;
            }
            if (moduleMachine.gtl_extend$SEPMTier && recipe.data.getInt("SEPMTier") > moduleMachine.gtl_extend$ModuleTier) {
                return null;
            }
            moduleMachine.gtl_extend$tier = moduleMachine.getTier();
            GTRecipe recipe1 = GTLRecipeModifiers.reduction(machine, recipe, 1, Math.pow(0.8, moduleMachine.gtl_extend$ModuleTier - 1));
            if (recipe1 != null) {
                recipe1 = GTRecipeModifiers.accurateParallel(machine, recipe1, moduleMachine.gtl_extend$Parallel(), false).getFirst();
                if (recipe1 != null) {
                    return RecipeHelper.applyOverclock(OverclockingLogic.NON_PERFECT_OVERCLOCK_SUBTICK, recipe1, moduleMachine.getOverclockVoltage(), params, result);
                }
            }
        }

        return null;
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public boolean onWorking() {
        boolean value = super.onWorking();
        if (this.getOffsetTimer() % 20L == 0L) {
            this.getSpaceElevatorTier();
            if (this.gtl_extend$SpaceElevatorTier < 1) {
                this.getRecipeLogic().setProgress(0);
            }
        }

        return value;
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public void addDisplayText(@NotNull List<Component> textList) {
        super.addDisplayText(textList);
        if (!this.isFormed) return;
        if (getOffsetTimer() % 10 == 0) {
            getSpaceElevatorTier();
        }
        textList.add(Component.translatable("gtceu.multiblock.parallel", Component.literal(FormattingUtil.formatNumbers(gtl_extend$Parallel())).withStyle(ChatFormatting.DARK_PURPLE)).withStyle(ChatFormatting.GRAY));
        textList.add(Component.literal((gtl_extend$SpaceElevatorTier < 1 ? "未" : "已") + "连接正在运行的太空电梯"));
        textList.add(Component.translatable("gtceu.machine.duration_multiplier.tooltip", FormattingUtil.formatPercent(Math.pow(0.8, gtl_extend$SpaceElevatorTier - 1))));
    }

    @Unique
    private int gtl_extend$Parallel() {
        double var = Math.pow(4, gtl_extend$ModuleTier - 1);
        double var1 = var * gtl_extend$tier;
        int parallel = (int) var1;
        return parallel;
    }
}
