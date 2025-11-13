package cn.qiuye.gtlextend.api.recipe;

import com.gtladd.gtladditions.api.recipe.WirelessGTRecipe;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.capability.recipe.RecipeCapability;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.api.recipe.content.Content;
import com.gregtechceu.gtceu.common.data.GTRecipeTypes;
import com.gregtechceu.gtceu.data.recipe.builder.GTRecipeBuilder;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigInteger;
import java.util.List;

public class NoEnergyGTRecipeBuilder extends GTRecipeBuilder {

    public @Nullable BigInteger wirelessEut;

    public NoEnergyGTRecipeBuilder(ResourceLocation id, GTRecipeType recipeType) {
        super(id, recipeType);
    }

    public static NoEnergyGTRecipeBuilder ofRaw() {
        return ofRaw(GTRecipeTypes.DUMMY_RECIPES);
    }

    public static NoEnergyGTRecipeBuilder ofRaw(GTRecipeType recipeType) {
        return new NoEnergyGTRecipeBuilder(GTCEu.id("raw"), recipeType);
    }

    public @NotNull NoEnergyGTRecipeBuilder input(RecipeCapability<?> cap, List<Content> contents) {
        this.input.put(cap, contents);
        return this;
    }

    public @NotNull NoEnergyGTRecipeBuilder output(RecipeCapability<?> cap, List<Content> contents) {
        this.output.put(cap, contents);
        return this;
    }

    public @NotNull NoEnergyGTRecipeBuilder duration(int duration) {
        this.duration = duration;
        return this;
    }

    public @NotNull WirelessGTRecipe buildRawRecipe() {
        return new WirelessGTRecipe(this.recipeType, this.id.withPrefix(this.recipeType.registryName.getPath() + "/"), this.input, this.output, this.tickInput, this.tickOutput, this.inputChanceLogic, this.outputChanceLogic, this.tickInputChanceLogic, this.tickOutputChanceLogic, this.conditions, List.of(), this.data, this.duration, this.isFuel, this.wirelessEut);
    }
}
