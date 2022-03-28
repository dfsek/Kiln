package net.zestyblaze.kiln.registry;

import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.MenuType;
import net.zestyblaze.kiln.Kiln;
import net.zestyblaze.kiln.block.screen.KilnScreenHandler;

public class KilnScreenHandlerInit {
    public static final MenuType<KilnScreenHandler> KILN_SCREEN_HANDLER = ScreenHandlerRegistry.registerSimple(new ResourceLocation(Kiln.MODID, "kiln"), KilnScreenHandler::new);

    public static void loadScreens() {}
}
