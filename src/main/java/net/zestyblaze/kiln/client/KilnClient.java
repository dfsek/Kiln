package net.zestyblaze.kiln.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.zestyblaze.kiln.Kiln;
import net.zestyblaze.kiln.client.screen.KilnScreen;
import net.zestyblaze.kiln.config.KilnModConfig;
import net.zestyblaze.kiln.registry.KilnScreenHandlerInit;

@Environment(EnvType.CLIENT)
public class KilnClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ScreenRegistry.register(KilnScreenHandlerInit.KILN_SCREEN_HANDLER, KilnScreen::new);

        if(KilnModConfig.get().debugMode) {
            Kiln.LOGGER.info("Kiln Client: Registry - Screens Registered");
        }
    }
}
