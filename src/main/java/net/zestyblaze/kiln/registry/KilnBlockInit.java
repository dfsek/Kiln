package net.zestyblaze.kiln.registry;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.zestyblaze.kiln.Kiln;
import net.zestyblaze.kiln.block.KilnBlock;

public class KilnBlockInit {
    public static final Block KILN = Registry.register(Registry.BLOCK, new ResourceLocation(Kiln.MODID, "kiln"), new KilnBlock(FabricBlockSettings.copy(Blocks.BRICKS)));

    public static void loadBlocks() {}
}
