package cn.qiuye.gtlextend.api.recipe

import org.gtlcore.gtlcore.api.recipe.IGTRecipe

import com.gtladd.gtladditions.common.machine.muiltblock.controller.ForgeOfTheAntichrist.Companion.ForgeOfTheAntichristLogic.Companion.cycleItems

import com.gregtechceu.gtceu.api.capability.recipe.ItemRecipeCapability
import com.gregtechceu.gtceu.api.capability.recipe.RecipeCapability
import com.gregtechceu.gtceu.api.recipe.GTRecipe
import com.gregtechceu.gtceu.api.recipe.content.Content
import com.gregtechceu.gtceu.api.recipe.content.ContentModifier
import com.gregtechceu.gtceu.api.recipe.ingredient.SizedIngredient

import it.unimi.dsi.fastutil.objects.ObjectArrayList
import it.unimi.dsi.fastutil.objects.Reference2ReferenceOpenHashMap

import kotlin.collections.set

class ModifyContents {

    companion object {
        fun copyAndModifyRecipe(recipe: GTRecipe, modifier: ContentModifier): GTRecipe {
            val copy = GTRecipe(
                recipe.recipeType,
                recipe.id,
                modifyInputContents(recipe.inputs, modifier),
                modifyOutputContents(recipe.outputs, modifier),
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

        fun modifyOutputContents(
            before: Map<RecipeCapability<*>, List<Content>>,
            modifier: ContentModifier,
        ): Map<RecipeCapability<*>, List<Content>> {
            val after = Reference2ReferenceOpenHashMap<RecipeCapability<*>, List<Content>>()
            for (entry in before) {
                val cap = entry.key
                val contentList = entry.value
                val copyList = ObjectArrayList<Content>(contentList.size)

                if (cap == ItemRecipeCapability.CAP) {
                    for (content in contentList) {
                        if (content.content is SizedIngredient &&
                            (content.content as SizedIngredient).items[0].item in cycleItems
                        ) {
                            copyList.add(content)
                        } else {
                            copyList.add(content.copy(ItemRecipeCapability.CAP, modifier))
                        }
                    }
                } else {
                    for (content in contentList) {
                        copyList.add(content.copy(cap, modifier))
                    }
                }
                after[cap] = copyList
            }
            return after
        }

        fun modifyInputContents(
            before: Map<RecipeCapability<*>, List<Content>>,
            modifier: ContentModifier,
        ): Map<RecipeCapability<*>, List<Content>> {
            if (!before.containsKey(ItemRecipeCapability.CAP)) return before

            val after = Reference2ReferenceOpenHashMap<RecipeCapability<*>, List<Content>>()
            for (entry in before) {
                val cap = entry.key
                val contentList = entry.value

                if (cap == ItemRecipeCapability.CAP) {
                    val copyList = ObjectArrayList<Content>(contentList.size)
                    for (content in contentList) {
                        if (content.content is SizedIngredient &&
                            (content.content as SizedIngredient).items[0].item in cycleItems
                        ) {
                            copyList.add(content.copy(ItemRecipeCapability.CAP, modifier))
                        } else {
                            copyList.add(content)
                        }
                    }
                    after[cap] = copyList
                } else {
                    after[cap] = contentList
                }
            }
            return after
        }
    }
}
