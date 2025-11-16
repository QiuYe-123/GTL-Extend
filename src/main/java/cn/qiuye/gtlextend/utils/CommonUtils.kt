package cn.qiuye.gtlextend.utils

import cn.qiuye.gtlextend.common.record.ParallelData

import com.gregtechceu.gtceu.api.recipe.GTRecipe

import it.unimi.dsi.fastutil.ints.IntList
import it.unimi.dsi.fastutil.longs.LongList
import it.unimi.dsi.fastutil.objects.ObjectList

import kotlin.math.min

object CommonUtils {

    // ===================================================
    // Recipe Calculation
    // ===================================================

    @JvmStatic
    fun getParallelData(
        remaining: Long,
        parallels: LongArray,
        remainingWants: LongList,
        remainingIndices: IntList,
        recipeList: ObjectList<GTRecipe>,
    ): ParallelData? {
        if (recipeList.isEmpty()) return null
        if (remaining <= 0 || remainingWants.isEmpty()) return ParallelData(recipeList, parallels)

        return if (remainingWants.size <= 64) {
            getParallelDataBitmap(remaining, parallels, remainingWants, remainingIndices, recipeList)
        } else {
            getParallelDataIndexArray(remaining, parallels, remainingWants, remainingIndices, recipeList)
        }
    }

    private fun getParallelDataBitmap(
        remaining: Long,
        parallels: LongArray,
        remainingWants: LongList,
        remainingIndices: IntList,
        recipeList: ObjectList<GTRecipe>,
    ): ParallelData {
        val count = remainingWants.size
        var activeBits = (1L shl count) - 1
        var activeCount = count

        var remaining = remaining
        while (remaining > 0 && activeCount > 0) {
            val perRecipe = remaining / activeCount
            if (perRecipe <= 0L) break

            var distributed = 0L
            var newActiveBits = 0L
            var newActiveCount = 0

            var bits = activeBits
            while (bits != 0L) {
                val i = bits.countTrailingZeroBits()
                bits = bits and (bits - 1)

                val idx = remainingIndices.getInt(i)
                val want = remainingWants.getLong(i)
                val give = min(want, perRecipe)
                parallels[idx] += give
                distributed += give
                remainingWants.set(i, want - give)

                if (want - give > 0) {
                    newActiveBits = newActiveBits or (1L shl i)
                    newActiveCount++
                }
            }

            activeBits = newActiveBits
            activeCount = newActiveCount
            remaining -= distributed
        }

        return ParallelData(recipeList, parallels)
    }

    private fun getParallelDataIndexArray(
        remaining: Long,
        parallels: LongArray,
        remainingWants: LongList,
        remainingIndices: IntList,
        recipeList: ObjectList<GTRecipe>,
    ): ParallelData {
        var activeCount = remainingWants.size
        var remaining = remaining

        while (remaining > 0 && activeCount > 0) {
            val perRecipe = remaining / activeCount
            if (perRecipe <= 0L) break

            var distributed = 0L
            var writePos = 0

            for (readPos in 0 until activeCount) {
                val idx = remainingIndices.getInt(readPos)
                val want = remainingWants.getLong(readPos)
                val give = min(want, perRecipe)
                parallels[idx] += give
                distributed += give

                val newWant = want - give
                if (newWant > 0) {
                    remainingWants.set(writePos, newWant)
                    remainingIndices.set(writePos, idx)
                    writePos++
                }
            }

            activeCount = writePos
            remaining -= distributed
        }

        return ParallelData(recipeList, parallels)
    }
}
