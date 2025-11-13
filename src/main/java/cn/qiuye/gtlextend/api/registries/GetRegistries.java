package cn.qiuye.gtlextend.api.registries;

import cn.qiuye.gtlextend.GTL_Extend;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.registries.ForgeRegistries;

public class GetRegistries {

    public static Item getItem(String s) {
        Item i = ForgeRegistries.ITEMS.getValue(new ResourceLocation(s));
        if (i == Items.AIR) {
            GTL_Extend.LOGGER.error("未找到ID为{}的物品", s);
            return Items.BARRIER;
        }
        return i;
    }

    public static Block getBlock(String s) {
        Block b = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(s));
        if (b == Blocks.AIR) {
            GTL_Extend.LOGGER.error("未找到ID为{}的方块", s);
            return Blocks.BARRIER;
        }
        return b;
    }
}
