package net.zestyblaze.kiln.integration.rei;

import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.util.EntryStacks;
import me.shedaniel.rei.plugin.client.categories.cooking.DefaultCookingCategory;
import net.minecraft.resources.ResourceLocation;
import net.zestyblaze.kiln.Kiln;
import net.zestyblaze.kiln.block.KilnBlock;
import net.zestyblaze.kiln.integration.rei.display.FiringDisplay;
import net.zestyblaze.kiln.recipe.FiringRecipe;
import net.zestyblaze.kiln.registry.KilnBlockInit;

public class REIPlugin implements REIClientPlugin {
    public static final CategoryIdentifier<FiringDisplay> FIRING = CategoryIdentifier.of(Kiln.MODID, "kiln");

    @Override
    public void registerCategories(CategoryRegistry registry) {
        registry.addWorkstations(FIRING, EntryStacks.of(KilnBlockInit.KILN));
        registry.add(new DefaultCookingCategory(FIRING, EntryStacks.of(KilnBlockInit.KILN), "category.kiln.firing"));
    }

    @Override
    public void registerDisplays(DisplayRegistry registry) {
        registry.registerFiller(FiringRecipe.class, FiringDisplay::new);
    }
}
