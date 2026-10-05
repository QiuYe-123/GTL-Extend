package cn.qiuye.gtlextend.common.machine.multiblock.noenergy

import cn.qiuye.gtlextend.common.data.GTL_Extend_RecipeTypes

import org.gtlcore.gtlcore.api.recipe.IGTRecipe
import org.gtlcore.gtlcore.api.recipe.IParallelLogic
import org.gtlcore.gtlcore.api.recipe.RecipeExtensionCopier
import org.gtlcore.gtlcore.api.recipe.RecipeRunnerHelper

import com.gtladd.gtladditions.api.machine.logic.GTLAddMultipleTypeWirelessRecipesLogic
import com.gtladd.gtladditions.api.machine.trait.IWirelessNetworkEnergyHandler
import com.gtladd.gtladditions.api.machine.wireless.GTLAddWirelessWorkableElectricMultipleTypeRecipesMachine
import com.gtladd.gtladditions.api.recipe.ChanceParallelLogic
import com.gtladd.gtladditions.common.data.ParallelData
import com.gtladd.gtladditions.utils.RecipeCalculationHelper

import com.gregtechceu.gtceu.api.GTValues
import com.gregtechceu.gtceu.api.capability.recipe.RecipeCapability
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity
import com.gregtechceu.gtceu.api.machine.trait.MachineTrait
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic
import com.gregtechceu.gtceu.api.recipe.GTRecipe
import com.gregtechceu.gtceu.api.recipe.content.Content
import com.gregtechceu.gtceu.api.recipe.content.ContentModifier

import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder

import com.hepdd.gtmthings.api.misc.WirelessEnergyManager
import it.unimi.dsi.fastutil.longs.LongArrayList
import it.unimi.dsi.fastutil.objects.ObjectArrayList
import it.unimi.dsi.fastutil.objects.Reference2ReferenceOpenHashMap

import java.math.BigInteger

open class TimeSpaceBreakerMultipleType(holder: IMachineBlockEntity, vararg args: Any?) :
    GTLAddWirelessWorkableElectricMultipleTypeRecipesMachine(
        holder,
        GTL_Extend_RecipeTypes.SPACETIME_BREAK,
        *args,
    ) {
    val wirelessNetworkTrait = NoEnergyWirelessNetworkHandler()

    // ========================================
    // Core overrides
    // ========================================
    override fun getWirelessNetworkEnergyHandler(): IWirelessNetworkEnergyHandler = wirelessNetworkTrait

    override fun getMaxParallel(): Int = Int.MAX_VALUE

    override fun createRecipeLogic(vararg args: Any): RecipeLogic = TimeSpaceBreakerLogic(this)

    override fun getRecipeLogic(): TimeSpaceBreakerLogic = super.getRecipeLogic() as TimeSpaceBreakerLogic

    override fun needConfirmMEStock(): Boolean = true

    override fun getTier(): Int = GTValues.MAX_TRUE

    @Suppress("unused")
    fun supportsBatchProcessing(): Boolean = false

    @Suppress("unused")
    fun canConfigureBatchProcessing(): Boolean = false

    // ========================================
    // Logic
    // ========================================

    companion object {
        class TimeSpaceBreakerLogic(parallel: TimeSpaceBreakerMultipleType) :
            GTLAddMultipleTypeWirelessRecipesLogic(parallel) {
            init {
                this.setReduction(0.0, 1.0)
            }

            override fun getMachine(): TimeSpaceBreakerMultipleType = super.getMachine() as TimeSpaceBreakerMultipleType

            override fun getMultipleThreads(): Int = Int.MAX_VALUE

            override fun getMaxParallel(recipe: GTRecipe, limit: Long): Long = ChanceParallelLogic.getMaxParallel(
                getMachine(),
                recipe,
                limit,
                chanceCaches,
                recipe.type.chanceFunction,
                IGTRecipe.of(recipe).euTier,
                getMachine().tier,
            )

            override fun calculateParallels(): ParallelData? {
                val recipes = lookupRecipeIterator()
                val totalParallel = getMachine().maxParallel.toLong() * getMultipleThreads()

                var remain = totalParallel
                var consumedParallels = 0L
                val recipeList = ObjectArrayList<GTRecipe>()
                val processedRecipeList = ObjectArrayList<GTRecipe>()
                val parallelsList = LongArrayList()

                for (match in recipes) {
                    if (remain <= 0L) break
                    val modified = modifyChance(match)
                    val p = getMaxParallel(modified, remain)
                    if (p <= 0L) continue

                    val paralleledRecipe = IParallelLogic.getRecipeOutputChance(
                        machine,
                        RecipeCalculationHelper.multipleRecipe(modified, p) { recipe ->
                            RecipeCalculationHelper.copyFixRecipe(
                                recipe,
                                ContentModifier.multiplier(p.toDouble()),
                                INPUT_CHANCE_RATIO,
                            )
                        },
                    )

                    if (RecipeRunnerHelper.handleRecipeInput(machine, paralleledRecipe)) {
                        remain -= p
                        consumedParallels += p
                        recipeList.add(match)
                        processedRecipeList.add(paralleledRecipe)
                        parallelsList.add(p)
                    }
                }

                if (recipeList.isEmpty) return null

                applyOutputMultiplier(processedRecipeList)

                return ParallelData(recipeList, parallelsList.toLongArray(), false, processedRecipeList)
            }

            companion object {
                private const val INPUT_CHANCE_RATIO = 10
                private const val OUTPUT_MULTIPLIER = 100.0

                private fun applyOutputMultiplier(recipes: Iterable<GTRecipe>) {
                    val modifier = ContentModifier.multiplier(OUTPUT_MULTIPLIER)
                    for (recipe in recipes) {
                        for ((capability, contents) in recipe.outputs) {
                            for (i in contents.indices) {
                                contents[i] = contents[i].copy(capability, modifier)
                            }
                        }
                    }
                }

                private fun modifyContents(
                    before: Map<RecipeCapability<*>, MutableList<Content>>,
                    isInput: Boolean,
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
                                    copy.maxChance = cont.maxChance * INPUT_CHANCE_RATIO
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

                private fun modifyChance(recipe: GTRecipe): GTRecipe {
                    val copy = GTRecipe(
                        recipe.recipeType,
                        recipe.id,
                        modifyContents(recipe.inputs, true),
                        modifyContents(recipe.outputs, false),
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
                    RecipeExtensionCopier.copy(recipe, copy)
                    return copy
                }
            }
        }
    }

    inner class NoEnergyWirelessNetworkHandler :
        MachineTrait(this),
        IWirelessNetworkEnergyHandler {
        override fun consumeEnergy(energy: Int): Boolean = uuid != null
        override fun consumeEnergy(energy: Long): Boolean = uuid != null
        override fun consumeEnergy(energy: BigInteger): Boolean = uuid != null
        override val maxAvailableEnergy: BigInteger
            get() = if (uuid != null) WirelessEnergyManager.getUserEU(uuid) else BigInteger.ZERO
        override val isOnline: Boolean
            get() = uuid != null
        override fun getFieldHolder(): ManagedFieldHolder = SELF_WIRELESS_NETWORK_PROXY_FIELD_HOLDER
    }
}
