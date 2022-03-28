package net.zestyblaze.kiln.integration.rei.display;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.plugin.common.displays.cooking.DefaultCookingDisplay;
import net.zestyblaze.kiln.integration.rei.REIPlugin;
import net.zestyblaze.kiln.recipe.FiringRecipe;

public class FiringDisplay extends DefaultCookingDisplay {
    public FiringDisplay(FiringRecipe recipe) {
        super(recipe);
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return REIPlugin.FIRING;
    }
}
