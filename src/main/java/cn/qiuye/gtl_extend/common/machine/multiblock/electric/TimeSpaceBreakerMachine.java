package cn.qiuye.gtl_extend.common.machine.multiblock.electric;

import cn.qiuye.gtl_extend.api.machine.IThreadModifierParallelMachine;
import cn.qiuye.gtl_extend.common.machine.trait.MultipleRecipesLogic;

import org.gtlcore.gtlcore.api.machine.multiblock.NoEnergyMultiblockMachine;

import com.gregtechceu.gtceu.api.capability.recipe.ItemRecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.RecipeCapability;
import com.gregtechceu.gtceu.api.machine.ConditionalSubscriptionHandler;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.content.Content;
import com.gregtechceu.gtceu.api.recipe.content.ContentModifier;
import com.gregtechceu.gtceu.api.recipe.ingredient.SizedIngredient;
import com.gregtechceu.gtceu.common.data.GTRecipeModifiers;

import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;

import net.minecraft.MethodsReturnNonnullByDefault;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.ParametersAreNonnullByDefault;

import it.unimi.dsi.fastutil.objects.Reference2ReferenceOpenHashMap;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class TimeSpaceBreakerMachine extends NoEnergyMultiblockMachine implements IThreadModifierParallelMachine {

    public static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            TimeSpaceBreakerMachine.class, NoEnergyMultiblockMachine.MANAGED_FIELD_HOLDER);

    protected ConditionalSubscriptionHandler StartupSubs;

    public TimeSpaceBreakerMachine(IMachineBlockEntity holder, Object... args) {
        super(holder, args);
        this.StartupSubs = new ConditionalSubscriptionHandler(this, this::StartupUpdate, this::isFormed);
    }

    private void StartupUpdate() {}

    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    @Override
    public RecipeLogic createRecipeLogic(Object @NotNull... args) {
        return new MultipleRecipesLogic(this);
    }

    @Nullable
    public GTRecipe recipeModifier(MetaMachine machine, GTRecipe recipe) {
        if (machine instanceof TimeSpaceBreakerMachine) {
            {
                GTRecipe modifiedRecipe = recipe.copy();

                modifiedRecipe.duration = 1;
                modifiedRecipe.inputs.putAll(modifyInputContents(recipe.inputs));
                modifiedRecipe.outputs.putAll(modifyOutputContents(recipe.outputs));

                // 应用精确并行处理并返回结果
                return GTRecipeModifiers.accurateParallel(
                        this, // 传入当前实例
                        modifiedRecipe,
                        Integer.MAX_VALUE,
                        false).getFirst();
            }
        }
        return null;
    }

    private static Reference2ReferenceOpenHashMap<RecipeCapability<?>, List<Content>> modifyInputContents(Map<RecipeCapability<?>, List<Content>> before) {
        val after = new Reference2ReferenceOpenHashMap<RecipeCapability<?>, List<Content>>();
        for (Map.Entry<RecipeCapability<?>, List<Content>> entry : before.entrySet()) {
            RecipeCapability<?> cap = entry.getKey();
            List<Content> contentList = entry.getValue();

            if (cap == ItemRecipeCapability.CAP) {
                List<Content> copyList = new ArrayList<>(contentList.size());
                for (Content content : contentList) {
                    // 假设 SizedIngredient 有 getItems() 方法，返回数组或列表
                    if (content.getContent() instanceof SizedIngredient sizedIngredient) {
                        Object firstItem = sizedIngredient.getItems()[0]; // 或者 getItems().get(0)

                        if (contentList.contains(firstItem)) {
                            copyList.add(content.copy(ItemRecipeCapability.CAP, ContentModifier.multiplier(1)));
                        } else {
                            copyList.add(content);
                        }
                    } else {
                        copyList.add(content);
                    }
                }
                after.put(cap, copyList);
            } else {
                after.put(cap, contentList);
            }
        }
        return after;
    }

    private static Reference2ReferenceOpenHashMap<RecipeCapability<?>, List<Content>> modifyOutputContents(Map<RecipeCapability<?>, List<Content>> before) {
        val after = new Reference2ReferenceOpenHashMap<RecipeCapability<?>, List<Content>>();
        for (Map.Entry<RecipeCapability<?>, List<Content>> entry : before.entrySet()) {
            RecipeCapability<?> cap = entry.getKey();
            List<Content> contentList = entry.getValue();
            List<Content> copyList = new ArrayList<>(contentList.size());

            if (cap == ItemRecipeCapability.CAP) {
                for (Content content : contentList) {
                    if (content.getContent() instanceof SizedIngredient sizedIngredient) {
                        Object firstItem = sizedIngredient.getItems()[0];

                        if (contentList.contains(firstItem)) {
                            copyList.add(content);
                        } else {
                            copyList.add(content.copy(ItemRecipeCapability.CAP, ContentModifier.multiplier(100)));
                        }
                    } else {
                        copyList.add(content.copy(ItemRecipeCapability.CAP, ContentModifier.multiplier(100)));
                    }
                }
            } else {
                for (Content content : contentList) {
                    copyList.add(content.copy(cap, ContentModifier.multiplier(100)));
                }
            }
            after.put(cap, copyList);
        }
        return after;
    }

    @Override
    public int getMaxParallel() {
        return Integer.MAX_VALUE;
    }

    @Override
    public void onStructureInvalid() {
        super.onStructureInvalid();
    }

    @Override
    public void onPartUnload() {
        super.onPartUnload();
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
    public int getExtendlThread() {
        return Integer.MAX_VALUE;
    }
}
