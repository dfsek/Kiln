package net.zestyblaze.kiln.registry;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.zestyblaze.kiln.Kiln;
import net.zestyblaze.kiln.block.entity.KilnBlockEntity;
import net.zestyblaze.kiln.config.KilnModConfig;

public class KilnBlockEntityInit {
    public static final BlockEntityType<KilnBlockEntity> KILN_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, new ResourceLocation(Kiln.MODID, "kiln"), FabricBlockEntityTypeBuilder.create(KilnBlockEntity::new, KilnBlockInit.KILN).build(null));

    public static void loadBlockEntities() {
        if(KilnModConfig.get().debugMode) {
            Kiln.LOGGER.info("Kiln Common: Registry - Block Entities Registered");
        }
    }
}
