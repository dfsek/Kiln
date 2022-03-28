package net.zestyblaze.kiln.block.screen;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractFurnaceMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.RecipeBookType;
import net.zestyblaze.kiln.registry.KilnRecipeInit;
import net.zestyblaze.kiln.registry.KilnScreenHandlerInit;

public class KilnScreenHandler extends AbstractFurnaceMenu {
    public KilnScreenHandler(int syncId, Inventory playerInventory) {
        super(KilnScreenHandlerInit.KILN_SCREEN_HANDLER, KilnRecipeInit.FIRING, RecipeBookType.FURNACE, syncId, playerInventory);
    }

    public KilnScreenHandler(int syncId, Inventory playerInventory, Container inventory, ContainerData propertyDelegate) {
        super(KilnScreenHandlerInit.KILN_SCREEN_HANDLER, KilnRecipeInit.FIRING, RecipeBookType.FURNACE, syncId, playerInventory, inventory, propertyDelegate);
    }
}
