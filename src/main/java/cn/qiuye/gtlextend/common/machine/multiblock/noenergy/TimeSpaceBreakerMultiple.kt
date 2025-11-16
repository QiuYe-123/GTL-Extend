package cn.qiuye.gtlextend.common.machine.multiblock.noenergy

import cn.qiuye.gtlextend.api.machine.IThreadModifierParallelMachine
import cn.qiuye.gtlextend.api.machine.logic.TimeSpaceBreakerMultipleRecipesLogic
import cn.qiuye.gtlextend.api.recipe.ModifyChance.modifyChance
import cn.qiuye.gtlextend.api.recipe.ModifyContents.copyAndModifyRecipe
import cn.qiuye.gtlextend.common.record.ParallelData

import org.gtlcore.gtlcore.api.machine.trait.ILockRecipe
import org.gtlcore.gtlcore.api.machine.trait.IRecipeStatus
import org.gtlcore.gtlcore.api.recipe.RecipeText

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableElectricMultiblockMachine
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic
import com.gregtechceu.gtceu.api.recipe.GTRecipe
import com.gregtechceu.gtceu.api.recipe.content.ContentModifier

import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.HoverEvent
import net.minecraft.network.chat.Style

import it.unimi.dsi.fastutil.longs.LongArrayList
import it.unimi.dsi.fastutil.objects.ObjectArrayList

open class TimeSpaceBreakerMultiple(holder: IMachineBlockEntity, vararg args: Any?) :
    WorkableElectricMultiblockMachine(holder, *args),
    IThreadModifierParallelMachine {

    override fun getExtendlThread(): Long = Int.MAX_VALUE * 2L
    override fun getMaxParallel(): Int = Int.MAX_VALUE
    override fun getExtendlDuration(): Int = 20

    override fun addDisplayText(textList: MutableList<Component?>) {
        if (isFormed()) {
            // Machine mode
            addMachineModeDisplay(textList)

            // Working status
            addWorkingStatus(textList)

            // Recipe/Working status errors
            (recipeLogic as IRecipeStatus).let { status ->
                status.recipeStatus?.reason?.copy()?.withStyle(ChatFormatting.RED)?.let(textList::add)
                status.workingStatus?.reason?.copy()?.withStyle(ChatFormatting.RED)?.let(textList::add)
            }
        } else {
            textList.add(
                Component.translatable("gtceu.multiblock.invalid_structure")
                    .withStyle(
                        Style.EMPTY.withColor(ChatFormatting.RED)
                            .withHoverEvent(
                                HoverEvent(
                                    HoverEvent.Action.SHOW_TEXT,
                                    Component.translatable("gtceu.multiblock.invalid_structure.tooltip")
                                        .withStyle(ChatFormatting.GRAY),
                                ),
                            ),
                    ),
            )
        }

        // Recipe lock display
        (recipeLogic as? ILockRecipe)?.let { addRecipeLockDisplay(textList, it) }

        definition.additionalDisplay.accept(this, textList)
        parts.forEach { it.addMultiText(textList) }
    }

    protected open fun addMachineModeDisplay(textList: MutableList<Component?>) {
        textList.add(
            Component.translatable(
                "gtceu.gui.machinemode",
                Component.translatable(recipeType.registryName.toLanguageKey()),
            )
                .withStyle(ChatFormatting.AQUA),
        )
    }

    protected open fun addWorkingStatus(textList: MutableList<Component?>) {
        when {
            !isWorkingEnabled -> textList.add(
                Component.translatable("gtceu.multiblock.work_paused").withStyle(ChatFormatting.GOLD),
            )

            isActive -> {
                textList.add(Component.translatable("gtceu.multiblock.running").withStyle(ChatFormatting.GREEN))
                textList.add(
                    Component.translatable("gtceu.multiblock.progress", (recipeLogic.progressPercent * 100).toInt()),
                )
            }

            else -> textList.add(Component.translatable("gtceu.multiblock.idling"))
        }
    }

    protected open fun addRecipeLockDisplay(textList: MutableList<Component?>, iLockRecipe: ILockRecipe) {
        val text = if (iLockRecipe.isLock && iLockRecipe.lockRecipe != null) {
            Component.translatable("gui.gtlcore.recipe_lock.recipe").withStyle {
                it.withHoverEvent(
                    HoverEvent(
                        HoverEvent.Action.SHOW_TEXT,
                        RecipeText.getRecipeInputText(iLockRecipe.lockRecipe)
                            .append(RecipeText.getRecipeOutputText(iLockRecipe.lockRecipe)),
                    ),
                )
            }
        } else {
            Component.translatable("gui.gtlcore.recipe_lock.no_recipe")
        }
        textList.add(text)
    }

    override fun createRecipeLogic(vararg args: Any): RecipeLogic = TimeSpaceBreakerLogic(this)

    override fun getRecipeLogic(): TimeSpaceBreakerLogic = super.getRecipeLogic() as TimeSpaceBreakerLogic

    companion object {
        private const val INPUT_CHANCE_RATIO = 10
        class TimeSpaceBreakerLogic(machine: TimeSpaceBreakerMultiple) :
            TimeSpaceBreakerMultipleRecipesLogic(machine) {

            override fun calculateParallels(): ParallelData? {
                val recipes = lookupRecipeIterator()
                if (recipes.isEmpty()) return null

                val recipeList = ObjectArrayList<GTRecipe>(recipes.size)
                val parallelsList = LongArrayList(recipes.size)
                val modifier = ContentModifier.multiplier(100.0)

                for (recipe in recipes) {
                    recipe ?: continue
                    val modified = modifyChance(recipe, INPUT_CHANCE_RATIO)
                    val finalmodified = copyAndModifyRecipe(modified, modifier)
                    val parallel = getMaxParallel(finalmodified, Long.MAX_VALUE)
                    if (parallel > 0) {
                        recipeList.add(finalmodified)
                        parallelsList.add(parallel)
                    }
                }

                return if (recipeList.isEmpty) {
                    null
                } else {
                    ParallelData(recipeList, parallelsList.toLongArray())
                }
            }
        }
    }
}
