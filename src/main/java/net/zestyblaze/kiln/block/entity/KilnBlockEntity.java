package net.zestyblaze.kiln.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.zestyblaze.kiln.block.screen.KilnScreenHandler;
import net.zestyblaze.kiln.config.KilnModConfig;
import net.zestyblaze.kiln.mixin.AbstractFurnaceBlockEntityAccessor;
import net.zestyblaze.kiln.registry.KilnBlockEntityInit;
import net.zestyblaze.kiln.registry.KilnRecipeInit;

public class KilnBlockEntity extends AbstractFurnaceBlockEntity {
    public KilnBlockEntity(BlockPos pos, BlockState state) {
        super(KilnBlockEntityInit.KILN_BLOCK_ENTITY, pos, state, KilnRecipeInit.FIRING);
    }

    @Override
    protected Component getDefaultName() {
        return new TranslatableComponent("container.kiln.kiln");
    }

    @Override
    protected AbstractContainerMenu createMenu(int containerId, Inventory inventory) {
        return new KilnScreenHandler(containerId, inventory, this, this.dataAccess);
    }

    @Override
    protected int getBurnDuration(ItemStack fuel) {
        return super.getBurnDuration(fuel) / 2;
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, KilnBlockEntity blockEntity) {
        if(level != null && level.dimensionType().ultraWarm() && KilnModConfig.get().autoFireUp) {
            ((AbstractFurnaceBlockEntityAccessor)blockEntity).setLitTime(4);
            ((AbstractFurnaceBlockEntityAccessor)blockEntity).setLitDuration(4);
            if(!state.getValue(AbstractFurnaceBlock.LIT)) {
                level.setBlock(pos, level.getBlockState(pos).setValue(AbstractFurnaceBlock.LIT, true), 3);
            }
        }
    }
}
