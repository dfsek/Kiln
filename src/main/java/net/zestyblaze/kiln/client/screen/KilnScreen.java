package net.zestyblaze.kiln.client.screen;

import net.minecraft.client.gui.screens.inventory.AbstractFurnaceScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.zestyblaze.kiln.Kiln;
import net.zestyblaze.kiln.block.screen.KilnScreenHandler;
import net.zestyblaze.kiln.client.recipebook.KilnBookRecipeScreen;

public class KilnScreen extends AbstractFurnaceScreen<KilnScreenHandler> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(Kiln.MODID, "textures/gui/container/kiln.png");

    public KilnScreen(KilnScreenHandler abstractFurnaceMenu, Inventory inventory, Component component) {
        super(abstractFurnaceMenu, new KilnBookRecipeScreen(), inventory, component, TEXTURE);
    }
}
