package net.zestyblaze.kiln.block.entity;

import com.google.common.collect.Maps;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.RecipeHolder;
import net.minecraft.world.inventory.StackedContentsCompatible;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.zestyblaze.kiln.block.KilnBlock;
import net.zestyblaze.kiln.block.screen.KilnScreenHandler;
import net.zestyblaze.kiln.config.KilnModConfig;
import net.zestyblaze.kiln.recipe.FiringRecipe;
import net.zestyblaze.kiln.registry.KilnBlockEntityInit;
import net.zestyblaze.kiln.registry.KilnRecipeInit;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.Map;

public class KilnBlockEntity extends BaseContainerBlockEntity implements WorldlyContainer, RecipeHolder, StackedContentsCompatible {
    private static final int[] SLOTS_FOR_UP = new int[]{0};
    private static final int[] SLOTS_FOR_DOWN = new int[]{2, 1};
    private static final int[] SLOTS_FOR_SIDES = new int[]{1};
    private static final Map<Item, Integer> AVAILABLE_FUELS = Maps.newHashMap();

    private final Object2IntOpenHashMap<ResourceLocation> recipesUsed;
    protected NonNullList<ItemStack> inventory;
    protected final ContainerData propertyDelegate;
    private Recipe<?> lastRecipe;
    private int smeltTimeTotal;
    private int smeltTime;
    private int burnTime;
    private int fuelTime;

    public KilnBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(KilnBlockEntityInit.KILN_BLOCK_ENTITY, blockPos, blockState);
        this.inventory = NonNullList.withSize(3, ItemStack.EMPTY);

        this.recipesUsed = new Object2IntOpenHashMap<>();
        this.propertyDelegate = new ContainerData() {
            public int get(int index) {
                return switch (index) {
                    case 0 -> KilnBlockEntity.this.burnTime;
                    case 1 -> KilnBlockEntity.this.fuelTime;
                    case 2 -> KilnBlockEntity.this.smeltTime;
                    case 3 -> KilnBlockEntity.this.smeltTimeTotal;
                    default -> 0;
                };
            }

            public void set(int index, int value) {
                switch (index) {
                    case 0 -> KilnBlockEntity.this.burnTime = value;
                    case 1 -> KilnBlockEntity.this.fuelTime = value;
                    case 2 -> KilnBlockEntity.this.smeltTime = value;
                    case 3 -> KilnBlockEntity.this.smeltTimeTotal = value;
                }
            }

            public int getCount() {
                return 4;
            }
        };
    }

    private boolean isBurning() {
        return burnTime > 0;
    }

    @Override
    public int getContainerSize() {
        return inventory.size();
    }

    @Override
    public boolean isEmpty() {
        Iterator<ItemStack> iterator = inventory.iterator();
        ItemStack itemStack;
        do {
            if (!iterator.hasNext()) {
                return true;
            }
            itemStack = iterator.next();
        } while (itemStack.isEmpty());

        return false;
    }

    @Override
    public ItemStack getItem(int slot) {
        return inventory.get(slot);
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        return ContainerHelper.removeItem(inventory, slot, amount);
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        return ContainerHelper.takeItem(inventory, slot);
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        ItemStack itemStack = inventory.get(slot);
        boolean stackValid = !stack.isEmpty() && stack.sameItem(itemStack) && ItemStack.tagMatches(stack, itemStack);
        inventory.set(slot, stack);
        if (stack.getCount() > getMaxStackSize()) {
            stack.setCount(getMaxStackSize());
        }
        if ((slot == 0 || slot == 1) && !stackValid) {
            smeltTimeTotal = getSmeltTime();
            smeltTime = 0;
            setChanged();
        }
    }

    protected int getSmeltTime() {
        if (level == null) return 200;
        return level.getRecipeManager()
                .getRecipeFor(KilnRecipeInit.FIRING, this, level)
                .map(FiringRecipe::getCookingTime)
                .orElse(0);
    }

    @Override
    public boolean stillValid(Player player) {
        if (level != null && level.getBlockEntity(worldPosition) != this) {
            return false;
        }
        return player.distanceToSqr(
                worldPosition.getX() + 0.5D,
                worldPosition.getY() + 0.5D,
                worldPosition.getZ() + 0.5D
        ) <= 64.0D;
    }

    @Override
    public void clearContent() {
        inventory.clear();
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("block.kiln.kiln");
    }

    @Override
    protected AbstractContainerMenu createMenu(int syncId, Inventory playerInventory) {
        return new KilnScreenHandler(syncId, playerInventory, this, propertyDelegate);
    }

    private static boolean canBurn(@Nullable Recipe<?> recipe, NonNullList<ItemStack> nonNullList, int i) {
        if (!nonNullList.get(0).isEmpty() && recipe != null) {
            ItemStack itemStack = recipe.getResultItem();
            if (itemStack.isEmpty()) {
                return false;
            } else {
                ItemStack itemStack2 = nonNullList.get(2);
                if (itemStack2.isEmpty()) {
                    return true;
                } else if (!itemStack2.sameItem(itemStack)) {
                    return false;
                } else if (itemStack2.getCount() < i && itemStack2.getCount() < itemStack2.getMaxStackSize()) {
                    return true;
                } else {
                    return itemStack2.getCount() < itemStack.getMaxStackSize();
                }
            }
        } else {
            return false;
        }
    }

    @Override
    public void fillStackedContents(StackedContents finder) {
        for (ItemStack itemStack : this.inventory) {
            finder.accountStack(itemStack);
        }
    }

    @Override
    public void setRecipeUsed(Recipe<?> recipe) {
        if (recipe != null) {
            ResourceLocation recipeId = recipe.getId();
            recipesUsed.addTo(recipeId, 1);
            lastRecipe = recipe;
        }
    }

    @Override
    public Recipe<?> getRecipeUsed() {
        return this.lastRecipe;
    }

    @Override
    public int[] getSlotsForFace(Direction side) {
        if (side == Direction.DOWN) {
            return SLOTS_FOR_DOWN;
        }
        return side == Direction.UP ? SLOTS_FOR_UP : SLOTS_FOR_SIDES;
    }

    @Override
    public boolean canPlaceItemThroughFace(int slot, ItemStack stack, Direction dir) {
        return this.canPlaceItem(slot, stack);
    }

    @Override
    public boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction dir) {
        if (dir == Direction.DOWN && slot == 2) {
            return stack.getItem() == Items.BUCKET;
        }
        return true;
    }

    protected int getFuelTime(ItemStack fuel) {
        if (fuel.isEmpty()) {
            return 0;
        }
        Item item = fuel.getItem();
        return AVAILABLE_FUELS.getOrDefault(item, getFabricFuel(fuel));
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        inventory = NonNullList.withSize(getContainerSize(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(tag, inventory);
        burnTime = tag.getShort("BurnTime");
        fuelTime = tag.getShort("FuelTime");
        smeltTime = tag.getShort("SmeltTime");
        smeltTimeTotal = tag.getShort("SmeltTimeTotal");
        CompoundTag compoundTag = tag.getCompound("RecipesUsed");
        for (String id : compoundTag.getAllKeys()) {
            recipesUsed.put(new ResourceLocation(id), compoundTag.getInt(id));
        }
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);

        tag.putShort("BurnTime", (short) burnTime);
        tag.putShort("FuelTime", (short) fuelTime);
        tag.putShort("SmeltTime", (short) smeltTime);
        tag.putShort("SmeltTimeTotal", (short) smeltTimeTotal);
        ContainerHelper.saveAllItems(tag, inventory);
        CompoundTag usedRecipes = new CompoundTag();
        recipesUsed.forEach((identifier, integer) -> usedRecipes.putInt(identifier.toString(), integer));
        tag.put("RecipesUsed", usedRecipes);
    }

    public boolean canPlaceItem(int slot, ItemStack stack) {
        if (slot == 3) {
            return false;
        }
        else if (slot != 2) {
            return true;
        }
        ItemStack itemStack = this.inventory.get(2);
        return canUseAsFuel(stack) || stack.getItem() == Items.BUCKET && itemStack.getItem() != Items.BUCKET;
    }

    public static boolean canUseAsFuel(ItemStack stack) {
        return AVAILABLE_FUELS.containsKey(stack.getItem());
    }

    public static void registerFuel(ItemLike fuel, int time) {
        AVAILABLE_FUELS.put(fuel.asItem(), time);
    }

    private static int getFabricFuel(ItemStack stack) {
        Integer ticks = FuelRegistry.INSTANCE.get(stack.getItem());
        return ticks == null ? 0 : ticks;
    }

    private static boolean burn(@Nullable Recipe<?> recipe, NonNullList<ItemStack> nonNullList, int i) {
        if (recipe != null && canBurn(recipe, nonNullList, i)) {
            ItemStack itemStack = nonNullList.get(0);
            ItemStack itemStack2 = recipe.getResultItem();
            ItemStack itemStack3 = nonNullList.get(2);
            if (itemStack3.isEmpty()) {
                nonNullList.set(2, itemStack2.copy());
            } else if (itemStack3.is(itemStack2.getItem())) {
                itemStack3.grow(1);
            }

            itemStack.shrink(1);
            return true;
        } else {
            return false;
        }
    }

    public void getRecipesToAwardAndPopExperience(ServerLevel level, Vec3 vec3) {
        for (Object2IntMap.Entry<ResourceLocation> resourceLocationEntry : this.recipesUsed.object2IntEntrySet()) {
            level.getRecipeManager().byKey(resourceLocationEntry.getKey()).ifPresent((recipe) -> createExperience(level, vec3, resourceLocationEntry.getIntValue(), ((AbstractCookingRecipe) recipe).getExperience()));
        }
    }

    private static void createExperience(ServerLevel level, Vec3 vec3, int i, float f) {
        int j = Mth.floor((float)i * f);
        float g = Mth.frac((float)i * f);
        if (g != 0.0F && Math.random() < (double)g) {
            ++j;
        }

        ExperienceOrb.award(level, vec3, j);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, KilnBlockEntity blockEntity) {
        if (level == null) return;

        if(level.dimensionType().ultraWarm() && KilnModConfig.get().autoFireUp) {
            blockEntity.burnTime = 4;
            blockEntity.fuelTime = 4;
            if(!blockEntity.getBlockState().getValue(KilnBlock.LIT)) {
                level.setBlock(pos, level.getBlockState(pos).setValue(KilnBlock.LIT, true), 3);
            }
        }

        boolean bl = blockEntity.isBurning();
        boolean bl2 = false;
        if (blockEntity.isBurning()) {
            --blockEntity.burnTime;
        }
        ItemStack itemStack = blockEntity.inventory.get(1);
        if (blockEntity.isBurning() || !itemStack.isEmpty() && !blockEntity.inventory.get(0).isEmpty()) {
            Recipe<?> recipe = level.getRecipeManager().getRecipeFor(KilnRecipeInit.FIRING, blockEntity, level).orElse(null);
            int i = blockEntity.getMaxStackSize();
            if (!blockEntity.isBurning() && canBurn(recipe, blockEntity.inventory, i)) {
                blockEntity.burnTime = blockEntity.getFuelTime(itemStack);
                blockEntity.fuelTime = blockEntity.burnTime;
                if (blockEntity.isBurning()) {
                    bl2 = true;
                    if (!itemStack.isEmpty()) {
                        Item item = itemStack.getItem();
                        itemStack.shrink(1);
                        if (itemStack.isEmpty()) {
                            Item item2 = item.getCraftingRemainingItem();
                            blockEntity.inventory.set(1, item2 == null ? ItemStack.EMPTY : new ItemStack(item2));
                        }
                    }
                }
            }
            if (blockEntity.isBurning() && canBurn(recipe, blockEntity.inventory, i)) {
                ++blockEntity.smeltTime;
                if (blockEntity.smeltTime == blockEntity.smeltTimeTotal) {
                    blockEntity.smeltTime = 0;
                    blockEntity.smeltTimeTotal = blockEntity.getSmeltTime();
                    if (burn(recipe, blockEntity.inventory, i)) {
                        blockEntity.setRecipeUsed(recipe);
                    }

                    bl2 = true;
                }
            } else {
                blockEntity.smeltTime = 0;
            }
        } else if (blockEntity.smeltTime > 0) {
            blockEntity.smeltTime = Mth.clamp(blockEntity.smeltTime - 2, 0, blockEntity.smeltTimeTotal);
        }

        if (bl != blockEntity.isBurning()) {
            bl2 = true;
            state = state.setValue(KilnBlock.LIT, blockEntity.isBurning());
            level.setBlock(pos, state, 3);
        }

        if (bl2) {
            setChanged(level, pos, state);
        }
    }

    static {
        AbstractFurnaceBlockEntity.getFuel().forEach(KilnBlockEntity::registerFuel);
    }

}
