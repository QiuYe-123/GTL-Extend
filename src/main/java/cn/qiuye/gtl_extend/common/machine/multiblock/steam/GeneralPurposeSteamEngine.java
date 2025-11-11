package cn.qiuye.gtl_extend.common.machine.multiblock.steam;

import cn.qiuye.gtl_extend.api.machine.IThreadModifierParallelMachine;
import cn.qiuye.gtl_extend.common.machine.trait.SteamMultipleRecipesLogic;

import com.gregtechceu.gtceu.api.capability.recipe.EURecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.FluidRecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.gui.fancy.FancyMachineUIWidget;
import com.gregtechceu.gtceu.api.gui.fancy.IFancyUIProvider;
import com.gregtechceu.gtceu.api.gui.fancy.TooltipsPanel;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.feature.IFancyUIMachine;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IDisplayUIMachine;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiPart;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableMultiblockMachine;
import com.gregtechceu.gtceu.api.machine.steam.SteamEnergyRecipeHandler;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableFluidTank;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import com.lowdragmc.lowdraglib.gui.modular.ModularUI;
import com.lowdragmc.lowdraglib.gui.widget.*;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;

import net.minecraft.ChatFormatting;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.annotation.ParametersAreNonnullByDefault;

import static org.gtlcore.gtlcore.common.data.GTLMachines.LARGE_STEAM_HATCH;

import org.jetbrains.annotations.NotNull;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class GeneralPurposeSteamEngine extends WorkableMultiblockMachine implements IFancyUIMachine, IDisplayUIMachine, IThreadModifierParallelMachine {

    protected static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            GeneralPurposeSteamEngine.class, WorkableMultiblockMachine.MANAGED_FIELD_HOLDER);

    private static final double CONVERSION_RATE = 100.0D;
    @Persisted
    private int amountOC;
    private int MaxParallel;
    private int ExtendlThread;

    public GeneralPurposeSteamEngine(IMachineBlockEntity holder, int MaxParallel, int ExtendlThread, Object... args) {
        super(holder, args);
        this.MaxParallel = MaxParallel;
        this.ExtendlThread = ExtendlThread;
    }

    @Override
    protected RecipeLogic createRecipeLogic(Object @NotNull... args) {
        return new SteamMultipleRecipesLogic(this);
    }

    @Override
    public void onStructureFormed() {
        super.onStructureFormed();
        var handlers = capabilitiesProxy.get(IO.IN, FluidRecipeCapability.CAP);
        if (handlers == null) return;
        var itr = handlers.iterator();
        while (itr.hasNext()) {
            var handler = itr.next();
            if (handler instanceof NotifiableFluidTank tank) {
                if (tank.getFluidInTank(0).isFluidEqual(GTMaterials.Steam.getFluid(1))) {
                    boolean isOC = tank.getMachine().getDefinition() == LARGE_STEAM_HATCH;
                    itr.remove();
                    if (!capabilitiesProxy.contains(IO.IN, EURecipeCapability.CAP)) {
                        capabilitiesProxy.put(IO.IN, EURecipeCapability.CAP, new ArrayList<>());
                    }
                    Objects.requireNonNull(capabilitiesProxy.get(IO.IN, EURecipeCapability.CAP))
                            .add(new SteamEnergyRecipeHandler(tank, CONVERSION_RATE * (isOC ? Math.pow(3, this.amountOC) : 1)));
                    return;
                }
            }
        }
    }

    @Override
    public void addDisplayText(List<Component> textList) {
        IDisplayUIMachine.super.addDisplayText(textList);
        if (isFormed()) {
            textList.add(Component.translatable(getRecipeType().registryName.toLanguageKey())
                    .setStyle(Style.EMPTY.withColor(ChatFormatting.AQUA)
                            .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                                    Component.translatable("gtceu.gui.machinemode.title")))));
            if (!isWorkingEnabled()) {
                textList.add(Component.translatable("gtceu.multiblock.work_paused"));
            } else if (isActive()) {
                textList.add(Component.translatable("gtceu.multiblock.running"));
                int currentProgress = (int) (recipeLogic.getProgressPercent() * 100);
                textList.add(Component.translatable("gtceu.multiblock.progress", currentProgress));
            } else {
                textList.add(Component.translatable("gtceu.multiblock.idling"));
            }
            if (recipeLogic.isWaiting()) {
                textList.add(Component.translatable("gtceu.multiblock.waiting")
                        .setStyle(Style.EMPTY.withColor(ChatFormatting.RED)));
            }
        } else {
            Component tooltip = Component.translatable("gtceu.multiblock.invalid_structure.tooltip")
                    .withStyle(ChatFormatting.GRAY);
            textList.add(Component.translatable("gtceu.multiblock.invalid_structure")
                    .withStyle(Style.EMPTY.withColor(ChatFormatting.RED)
                            .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, tooltip))));
        }
        getDefinition().getAdditionalDisplay().accept(this, textList);
    }

    @Override
    public Widget createUIWidget() {
        var group = new WidgetGroup(0, 0, 182 + 8, 117 + 8);
        group.addWidget(new DraggableScrollableWidgetGroup(4, 4, 182, 117)
                .setBackground(getScreenTexture())
                .addWidget(new LabelWidget(4, 5, self().getBlockState().getBlock().getDescriptionId()))
                .addWidget(new ComponentPanelWidget(4, 17, this::addDisplayText)
                        .setMaxWidthLimit(150)
                        .clickHandler(this::handleDisplayClick)));
        group.setBackground(GuiTextures.BACKGROUND_INVERSE);
        return group;
    }

    @Override
    public ModularUI createUI(Player entityPlayer) {
        return new ModularUI(198, 208, this, entityPlayer)
                .widget(new FancyMachineUIWidget(this, 198, 208));
    }

    @Override
    public List<IFancyUIProvider> getSubTabs() {
        return getParts().stream()
                .filter(Objects::nonNull)
                .map(IFancyUIProvider.class::cast)
                .toList();
    }

    @Override
    public void attachTooltips(TooltipsPanel tooltipsPanel) {
        for (IMultiPart part : getParts()) {
            part.attachFancyTooltipsToController(this, tooltipsPanel);
        }
    }

    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    /**
     * @return .
     */
    @Override
    public int getExtendlDuration() {
        return 1;
    }

    /**
     * @return .
     */
    @Override
    public int getMaxParallel() {
        return MaxParallel;
    }

    /**
     * @return .
     */
    @Override
    public int getExtendlThread() {
        return ExtendlThread;
    }
}
