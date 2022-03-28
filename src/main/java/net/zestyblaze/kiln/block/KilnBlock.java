package net.zestyblaze.kiln.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.zestyblaze.kiln.block.entity.KilnBlockEntity;
import net.zestyblaze.kiln.registry.KilnBlockEntityInit;
import net.zestyblaze.kiln.registry.KilnStatInit;
import org.jetbrains.annotations.Nullable;

public class KilnBlock extends AbstractFurnaceBlock {
    public KilnBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected void openContainer(Level level, BlockPos pos, Player player) {
        BlockEntity block = level.getBlockEntity(pos);
        if(block instanceof KilnBlockEntity) {
            player.openMenu((MenuProvider)block);
            player.awardStat(KilnStatInit.INTERACT_WITH_KILN);
        }
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new KilnBlockEntity(pos, state);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState state = super.getStateForPlacement(context);
        if(context.getLevel().dimensionType().ultraWarm()) {
            assert state != null;
            return state.setValue(AbstractFurnaceBlock.LIT, true);
        }
        return state;
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, KilnBlockEntityInit.KILN_BLOCK_ENTITY, KilnBlockEntity::serverTick);
    }
}
