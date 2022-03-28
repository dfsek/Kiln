package net.zestyblaze.kiln.registry;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.zestyblaze.kiln.Kiln;

public class KilnItemInit {
    public static void registerItems() {
        Registry.register(Registry.ITEM, new ResourceLocation(Kiln.MODID, "kiln"), new BlockItem(KilnBlockInit.KILN, new FabricItemSettings().group(CreativeModeTab.TAB_DECORATIONS)));
    }
}
