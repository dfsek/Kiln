package net.zestyblaze.kiln.registry;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SimpleCookingSerializer;
import net.zestyblaze.kiln.Kiln;
import net.zestyblaze.kiln.config.KilnModConfig;
import net.zestyblaze.kiln.recipe.FiringRecipe;

public class KilnRecipeInit {
    public static final RecipeType<FiringRecipe> FIRING = RecipeType.register("kiln:firing");
    public static final SimpleCookingSerializer<FiringRecipe> FIRING_SERIALIZER = Registry.register(Registry.RECIPE_SERIALIZER, new ResourceLocation(Kiln.MODID, "firing"), new SimpleCookingSerializer<>(FiringRecipe::new, 100));

    public static void loadRecipes() {
        if(KilnModConfig.get().debugMode) {
            Kiln.LOGGER.info("Kiln Common: Registry - Recipes Registered");
        }
    }
}
