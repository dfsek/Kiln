package net.zestyblaze.kiln.registry;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.Stats;
import net.zestyblaze.kiln.Kiln;

public class KilnStatInit {
    public static final ResourceLocation INTERACT_WITH_KILN = new ResourceLocation(Kiln.MODID, "interact_with_kiln");

    public static void registerStats() {
        Registry.register(Registry.CUSTOM_STAT, INTERACT_WITH_KILN, INTERACT_WITH_KILN);
        Stats.CUSTOM.get(INTERACT_WITH_KILN);
    }
}
