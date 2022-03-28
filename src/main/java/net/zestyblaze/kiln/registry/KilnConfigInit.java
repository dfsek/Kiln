package net.zestyblaze.kiln.registry;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.zestyblaze.kiln.Kiln;
import net.zestyblaze.kiln.config.KilnModConfig;

public class KilnConfigInit {
    public static void registerConfig() {
        AutoConfig.register(KilnModConfig.class, GsonConfigSerializer::new);

        if(KilnModConfig.get().debugMode) {
            Kiln.LOGGER.info("Kiln Client/Common: Registry - Config Registered");
        }
    }
}
