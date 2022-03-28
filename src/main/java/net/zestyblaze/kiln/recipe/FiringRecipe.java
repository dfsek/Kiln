package net.zestyblaze.kiln.recipe;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.zestyblaze.kiln.registry.KilnRecipeInit;

public class FiringRecipe extends AbstractCookingRecipe {
    public FiringRecipe(ResourceLocation resourceLocation, String string, Ingredient ingredient, ItemStack itemStack, float f, int i) {
        super(KilnRecipeInit.FIRING, resourceLocation, string, ingredient, itemStack, f, i);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return KilnRecipeInit.FIRING_SERIALIZER;
    }

    @Override
    public boolean isSpecial() {
        return true;
    }
}
