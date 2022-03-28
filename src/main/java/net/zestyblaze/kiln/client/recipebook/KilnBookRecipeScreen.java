package net.zestyblaze.kiln.client.recipebook;

import net.minecraft.client.gui.screens.recipebook.AbstractFurnaceRecipeBookComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;

import java.util.Set;

public class KilnBookRecipeScreen extends AbstractFurnaceRecipeBookComponent {
    private static final Component LABEL = new TranslatableComponent("gui.recipebook.toggleRecipes.firable");

    @Override
    protected Component getRecipeFilterName() {
        return LABEL;
    }

    @Override
    protected Set<Item> getFuelItems() {
        return AbstractFurnaceBlockEntity.getFuel().keySet();
    }
}
