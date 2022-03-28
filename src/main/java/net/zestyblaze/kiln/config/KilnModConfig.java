package net.zestyblaze.kiln.config;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import net.zestyblaze.kiln.Kiln;

@Config(name = Kiln.MODID)
public class KilnModConfig implements ConfigData {
    public boolean debugMode = false;

    public boolean autoFireUp = true;

    public static KilnModConfig get() {
        return AutoConfig.getConfigHolder(KilnModConfig.class).getConfig();
    }
}
