package cn.qiuye.gtlextend.common.record

import com.gregtechceu.gtceu.api.recipe.GTRecipe

@Suppress("ArrayInDataClass")
@JvmRecord
data class ParallelData(@JvmField val recipeList: List<GTRecipe>, @JvmField val parallels: LongArray)
