package cn.qiuye.gtlextend.mixin.gtm;

import org.gtlcore.gtlcore.api.pattern.util.IMultiblockStateGet;
import org.gtlcore.gtlcore.mixin.gtm.api.machine.IMultiblockStateInvoker;

import com.gregtechceu.gtceu.api.block.ActiveBlock;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiPart;
import com.gregtechceu.gtceu.api.pattern.BlockPattern;
import com.gregtechceu.gtceu.api.pattern.MultiblockState;
import com.gregtechceu.gtceu.api.pattern.TraceabilityPredicate;
import com.gregtechceu.gtceu.api.pattern.error.*;
import com.gregtechceu.gtceu.api.pattern.error.PatternError;
import com.gregtechceu.gtceu.api.pattern.error.SinglePredicateError;
import com.gregtechceu.gtceu.api.pattern.predicates.SimplePredicate;
import com.gregtechceu.gtceu.api.pattern.util.PatternMatchContext;

import it.unimi.dsi.fastutil.longs.*;
import it.unimi.dsi.fastutil.objects.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Map;

@Mixin(value = BlockPattern.class, priority = 114514)
public abstract class BlockPatternMixin {

    @Shadow(remap = false)
    @Final
    protected TraceabilityPredicate[][][] blockMatches;
    @Shadow(remap = false)
    @Final
    protected int fingerLength;
    @Shadow(remap = false)
    @Final
    protected int thumbLength;
    @Shadow(remap = false)
    @Final
    protected int palmLength;
    @Shadow(remap = false)
    @Final
    public int[][] aisleRepetitions;
    @Shadow(remap = false)
    @Final
    protected int[] centerOffset;

    @Shadow(remap = false)
    protected abstract BlockPos setActualRelativeOffset(int x, int y, int z, Direction facing, Direction upwardsFacing, boolean isFlipped);

    /**
     * @author
     * @reason
     */
    @Overwrite(remap = false)
    public boolean checkPatternAt(MultiblockState worldState, BlockPos centerPos, Direction frontFacing, Direction upwardsFacing, boolean isFlipped, boolean savePredicate) {
        boolean findFirstAisle = false;
        int minZ = -centerOffset[4];
        if (worldState instanceof IMultiblockStateInvoker stateGet) stateGet.cleanState();
        PatternMatchContext matchContext = worldState.getMatchContext();
        Map<SimplePredicate, Integer> globalCount = worldState.getGlobalCount();
        Map<SimplePredicate, Integer> layerCount = worldState.getLayerCount();
        // Checking aisles
        for (int c = 0, z = minZ++, r; c < this.fingerLength; c++) {
            // Checking repeatable slices
            loop:
            for (r = 0; (findFirstAisle ? r < aisleRepetitions[c][1] : z <= -centerOffset[3]); r++) {
                // Checking single slice
                layerCount.clear();

                for (int b = 0, y = -centerOffset[1]; b < this.thumbLength; b++, y++) {
                    for (int a = 0, x = -centerOffset[0]; a < this.palmLength; a++, x++) {
                        worldState.setError(null);
                        TraceabilityPredicate predicate = this.blockMatches[c][b][a];
                        if (predicate.isAny()) continue;
                        BlockPos pos = setActualRelativeOffset(x, y, z, frontFacing, upwardsFacing, isFlipped).offset(centerPos.getX(),
                                centerPos.getY(), centerPos.getZ());
                        if (worldState instanceof IMultiblockStateGet stateGet && !stateGet.updateState(pos, predicate)) {
                            return false;
                        }
                        if (predicate.addCache()) {
                            worldState.addPosCache(pos);
                            if (savePredicate) {
                                matchContext.getOrCreate("predicates", Object2ObjectOpenHashMap::new).put(pos, predicate);
                            }
                        }
                        boolean canPartShared = true;
                        if (worldState.getTileEntity() instanceof IMachineBlockEntity machineBlockEntity &&
                                machineBlockEntity.getMetaMachine() instanceof IMultiPart part) { // add detected parts
                            if (!predicate.isAny()) {
                                if (part.isFormed() && !part.canShared() &&
                                        !part.hasController(worldState.controllerPos)) { // check part can be shared
                                    canPartShared = false;
                                    worldState.setError(new PatternStringError("multiblocked.pattern.error.share"));
                                } else {
                                    matchContext.getOrCreate("parts", ObjectOpenHashSet::new).add(part);
                                }
                            }
                        }
                        if (worldState.getBlockState().getBlock() instanceof ActiveBlock) {
                            matchContext.getOrCreate("vaBlocks", LongOpenHashSet::new)
                                    .add(worldState.getPos().asLong());
                        }
                        if (!predicate.test(worldState) || !canPartShared) { // matching failed
                            if (findFirstAisle) {
                                if (r < aisleRepetitions[c][0]) {// retreat to see if the first aisle can start later
                                    r = c = 0;
                                    z = minZ++;
                                    matchContext.reset();
                                    findFirstAisle = false;
                                }
                            } else {
                                z++;// continue searching for the first aisle
                            }
                            continue loop;
                        }
                        matchContext.getOrCreate("ioMap", Long2ObjectOpenHashMap::new).put(worldState.getPos().asLong(),
                                worldState.io);
                    }
                }
                findFirstAisle = true;
                z++;

                // Check layer-local matcher predicate
                for (var entry : layerCount.entrySet()) {
                    if (entry.getValue() < entry.getKey().minLayerCount) {
                        worldState.setError(new SinglePredicateError(entry.getKey(), 3));
                        return false;
                    }
                }
            }
            // Repetitions out of range
            if (r < aisleRepetitions[c][0] || worldState.hasError() || !findFirstAisle) {
                if (!worldState.hasError()) {
                    worldState.setError(new PatternError());
                }
                return false;
            }
        }

        // Check count matches amount
        for (var entry : globalCount.entrySet()) {
            if (entry.getValue() < entry.getKey().minCount) {
                worldState.setError(new SinglePredicateError(entry.getKey(), 1));
                return false;
            }
        }

        worldState.setError(null);
        worldState.setNeededFlip(isFlipped);
        return true;
    }
}
