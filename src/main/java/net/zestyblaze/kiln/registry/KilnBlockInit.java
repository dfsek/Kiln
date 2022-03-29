package net.zestyblaze.kiln.registry;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.zestyblaze.kiln.Kiln;
import net.zestyblaze.kiln.block.KilnBlock;
import net.zestyblaze.kiln.config.KilnModConfig;

public class KilnBlockInit {
    public static final Block KILN = Registry.register(Registry.BLOCK, new ResourceLocation(Kiln.MODID, "kiln"), new KilnBlock(Blocks.BRICKS));

    public static void loadBlocks() {
        if(KilnModConfig.get().debugMode) {
            Kiln.LOGGER.info("Kiln Common: Registry - Blocks Registered");
        }
    }
}
