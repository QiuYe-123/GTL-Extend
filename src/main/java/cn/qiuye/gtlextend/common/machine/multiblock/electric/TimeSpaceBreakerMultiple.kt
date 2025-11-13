package cn.qiuye.gtlextend.common.machine.multiblock.electric

import cn.qiuye.gtlextend.api.machine.IThreadModifierParallelMachine
import cn.qiuye.gtlextend.api.recipe.ModifyContents.Companion.copyAndModifyRecipe
import cn.qiuye.gtlextend.common.machine.trait.TimeSpaceBreakerMultipleRecipesLogic
import cn.qiuye.gtlextend.common.record.ParallelData

import org.gtlcore.gtlcore.api.machine.multiblock.NoEnergyMultiblockMachine

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic
import com.gregtechceu.gtceu.api.recipe.GTRecipe
import com.gregtechceu.gtceu.api.recipe.content.ContentModifier

import it.unimi.dsi.fastutil.longs.LongArrayList
import it.unimi.dsi.fastutil.objects.ObjectArrayList

class TimeSpaceBreakerMultiple(holder: IMachineBlockEntity, vararg args: Any?) :
    NoEnergyMultiblockMachine(holder, *args),
    IThreadModifierParallelMachine {

    override fun getExtendlThread(): Int = Int.MAX_VALUE
    override fun getMaxParallel(): Int = Int.MAX_VALUE
    override fun getExtendlDuration(): Int = 20

    override fun createRecipeLogic(vararg args: Any): RecipeLogic = TimeSpaceBreakerLogic(this)

    override fun getRecipeLogic(): TimeSpaceBreakerLogic = super.getRecipeLogic() as TimeSpaceBreakerLogic

    companion object {
        class TimeSpaceBreakerLogic(machine: TimeSpaceBreakerMultiple) :
            TimeSpaceBreakerMultipleRecipesLogic(machine) {

            override fun getMachine(): TimeSpaceBreakerMultiple = super.getMachine() as TimeSpaceBreakerMultiple

            override fun calculateParallels(): ParallelData? {
                val recipes = lookupRecipeIterator()
                if (recipes.isEmpty()) return null

                val recipeList = ObjectArrayList<GTRecipe>(recipes.size)
                val parallelsList = LongArrayList(recipes.size)
                val modifier = ContentModifier.multiplier(100.0)

                for (recipe in recipes) {
                    recipe ?: continue
                    val modified = copyAndModifyRecipe(recipe, modifier)
                    val parallel = getMaxParallel(modified, Long.MAX_VALUE)
                    if (parallel > 0) {
                        recipeList.add(modified)
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
