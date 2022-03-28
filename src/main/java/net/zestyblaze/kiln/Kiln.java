package net.zestyblaze.kiln;

import net.fabricmc.api.ModInitializer;
import net.zestyblaze.kiln.config.KilnModConfig;
import net.zestyblaze.kiln.registry.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Kiln implements ModInitializer {
	public static final String MODID = "kiln";
	public static final String MODNAME = "Kiln";
	public static final Logger LOGGER = LogManager.getLogger(MODNAME);

	@Override
	public void onInitialize() {
		LOGGER.info("Kiln is installed correctly, loading now! Thanks for installing! <3");
		KilnConfigInit.registerConfig();
		KilnBlockEntityInit.loadBlockEntities();
		KilnBlockInit.loadBlocks();
		KilnItemInit.registerItems();
		KilnRecipeInit.loadRecipes();
		KilnScreenHandlerInit.loadScreens();
		KilnStatInit.registerStats();

		if(KilnModConfig.get().debugMode) {
			LOGGER.info("Kiln Common: Registry - Mod Fully Loaded!");
		}
	}
}
