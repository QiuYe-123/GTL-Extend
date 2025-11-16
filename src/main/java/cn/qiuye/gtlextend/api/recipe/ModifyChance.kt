package cn.qiuye.gtlextend.api.recipe

import org.gtlcore.gtlcore.api.recipe.IGTRecipe

import com.gregtechceu.gtceu.api.capability.recipe.RecipeCapability
import com.gregtechceu.gtceu.api.recipe.GTRecipe
import com.gregtechceu.gtceu.api.recipe.content.Content

import it.unimi.dsi.fastutil.objects.ObjectArrayList
import it.unimi.dsi.fastutil.objects.Reference2ReferenceOpenHashMap

object ModifyChance {

    fun modifyChance(recipe: GTRecipe, inputChanceRatio: Int): GTRecipe {
        val copy = GTRecipe(
            recipe.recipeType,
            recipe.id,
            modifyContents(recipe.inputs, true, inputChanceRatio),
            modifyContents(recipe.outputs, false, inputChanceRatio),
            recipe.tickInputs,
            recipe.tickOutputs,
            recipe.inputChanceLogics,
            recipe.outputChanceLogics,
            recipe.tickInputChanceLogics,
            recipe.tickOutputChanceLogics,
            recipe.conditions,
            recipe.ingredientActions,
            recipe.data,
            recipe.duration,
            recipe.isFuel,
        )
        IGTRecipe.of(copy).realParallels = IGTRecipe.of(recipe).realParallels
        copy.ocTier = recipe.ocTier
        return copy
    }

    private fun modifyContents(
        before: Map<RecipeCapability<*>, MutableList<Content>>,
        isInput: Boolean,
        inputChanceRatio: Int,
    ): Reference2ReferenceOpenHashMap<RecipeCapability<*>, MutableList<Content>> {
        val after = Reference2ReferenceOpenHashMap<RecipeCapability<*>, MutableList<Content>>()
        for (entry in before) {
            val cap = entry.key
            val contentList = after.computeIfAbsent(cap) { ObjectArrayList() }
            for (cont in entry.value) {
                if (cont.chance >= cont.maxChance) {
                    contentList.add(cont)
                } else if (cont.chance != 0) {
                    val copy = cont.copy(cap, null)
                    if (isInput) {
                        copy.maxChance = cont.maxChance * inputChanceRatio
                    } else {
                        copy.chance = cont.maxChance
                    }
                    contentList.add(copy)
                }
            }
            if (contentList.isEmpty()) after.remove(cap)
        }
        return after
    }
}
