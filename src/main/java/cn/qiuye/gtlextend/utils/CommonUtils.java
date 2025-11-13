package cn.qiuye.gtlextend.utils;

import cn.qiuye.gtlextend.common.record.ParallelData;

import com.gtladd.gtladditions.common.record.RecipeData;

import com.gregtechceu.gtceu.api.recipe.GTRecipe;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.objects.ObjectArrayFIFOQueue;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.jetbrains.annotations.Nullable;

public class CommonUtils {

    // ===================================================
    // Recipe Calculation
    // ===================================================

    public static @Nullable ParallelData getParallelData(int length, long remaining, long[] parallels, ObjectArrayFIFOQueue<RecipeData> queue, ObjectArrayList<GTRecipe> recipeList) {
        if (recipeList.isEmpty()) return null;

        var remainingWants = new long[length];
        var activeIndices = new IntArrayList(queue.size());
        while (!queue.isEmpty()) {
            var data = queue.dequeue();
            remainingWants[data.index] = data.remainingWant;
            activeIndices.add(data.index);
        }

        while (remaining > 0 && !activeIndices.isEmpty()) {
            long perRecipe = remaining / activeIndices.size();
            if (perRecipe == 0) break;

            long distributed = 0;
            for (var it = activeIndices.iterator(); it.hasNext();) {
                int idx = it.nextInt();
                long give = Math.min(remainingWants[idx], perRecipe);
                parallels[idx] += give;
                distributed += give;
                remainingWants[idx] -= give;
                if (remainingWants[idx] == 0) {
                    it.remove();
                }
            }
            remaining -= distributed;
        }

        return new ParallelData(recipeList, parallels);
    }
}
