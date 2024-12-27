package io.github.sfseeger.manaweave_and_runes.common.blockentities;

import io.github.sfseeger.lib.common.mana.IManaItem;
import io.github.sfseeger.lib.common.mana.capability.IManaHandler;
import io.github.sfseeger.lib.common.mana.capability.ManaweaveAndRunesCapabilities;
import io.github.sfseeger.lib.common.recipes.mana_concentrator.ManaConcentratorInput;
import io.github.sfseeger.lib.common.recipes.mana_concentrator.ManaConcentratorRecipe;
import io.github.sfseeger.manaweave_and_runes.common.blocks.mana_concentrator.ManaConcentratorBlock;
import io.github.sfseeger.manaweave_and_runes.common.blocks.mana_concentrator.ManaConcentratorType;
import io.github.sfseeger.manaweave_and_runes.core.init.ManaweaveAndRunesBlockEntityInit;
import io.github.sfseeger.manaweave_and_runes.core.init.ManaweaveAndRunesBlockInit;
import io.github.sfseeger.manaweave_and_runes.core.init.ManaweaveAndRunesRecipeInit;
import io.github.sfseeger.manaweave_and_runes.core.util.MultiblockValidator;
import io.github.sfseeger.manaweave_and_runes.core.util.ParticleUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class ManaConcentratorBlockEntity extends BlockEntity {
    private boolean isActive;
    public ItemStackHandler inventory = new ItemStackHandler(10) { // TODO: Replace with config value
        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            return stack.getItem() instanceof IManaItem;
        }

        @Override
        protected void onContentsChanged(int slot) {
            super.onContentsChanged(slot);
            markUpdated();
        }
    };
    private int craftTime;
    private int craftTimeRemaining;
    private Stack<Integer> slotStack = new Stack<>();

    public IItemHandler getItemHandler(@Nullable Direction side) {
        return inventory;
    }

    public ManaConcentratorBlockEntity(BlockPos pos, BlockState state) {
        super(ManaweaveAndRunesBlockEntityInit.MANA_CONCENTRATOR_BLOCK_ENTITY.get(), pos, state);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state,
            ManaConcentratorBlockEntity blockEntity) {
        if (level.getGameTime() % 20 == 0) {
            blockEntity.setActive(blockEntity.validateMultiblock().isValid());
            blockEntity.markUpdated();
        }
    }

    public static void clientTick(Level level, BlockPos pos, BlockState state,
            ManaConcentratorBlockEntity blockEntity) {
        RandomSource randomsource = level.getRandom();
        if (randomsource.nextFloat() <= 0.5F && blockEntity.isActive()) {
            Vec3 vec3 = ParticleUtils.randomPosInsideBox(pos, randomsource, -0.25, 0.25, -0.25, 1.25, 0.75, 1.25);
            level.addParticle(ParticleTypes.GLOW, vec3.x(), vec3.y(), vec3.z(), 0.0, 0.0, 0.0);
        }
    }

    public MultiblockValidator.MultiBlockValidationData validateMultiblock() {
        if (level == null) {
            return new MultiblockValidator.MultiBlockValidationData(false, null, null, null);
        }
        ManaConcentratorType type = level.getBlockState(getBlockPos()).getBlock() instanceof ManaConcentratorBlock block
                ? block.getType()
                : null;
        if (type == null || type.getShapeValidator((ServerLevel) level) == null) {
            return new MultiblockValidator.MultiBlockValidationData(false, null, null, null);
        }
        return type.validate(level, getBlockPos());
    }

    private void markUpdated() {
        setChanged();
        if (level != null) {
            level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), ManaConcentratorBlock.UPDATE_ALL);
        }
    }

    public ItemStack craft() {
        if (!isActive || level == null) {
            return ItemStack.EMPTY;
        }
        RecipeManager recipes = ((ServerLevel) level).getRecipeManager();
        ManaConcentratorType type = getManaConcentratorType();
        List<ItemStack> inputItems = new ArrayList<>();
        List<BlockPos> relativePedestalPositions =
                type.findBlocks(level, ManaweaveAndRunesBlockInit.RUNE_PEDESTAL_BLOCK.get());
        for (BlockPos pos : relativePedestalPositions) {
            BlockEntity blockEntity = level.getBlockEntity(getBlockPos().offset(pos));
            ItemStack item;
            if (blockEntity instanceof RunePedestalBlockEntity runePedestalBlockEntity && !(item =
                    runePedestalBlockEntity.getItem()).isEmpty()) {
                inputItems.add(item);
            }
        }
        ManaConcentratorInput input = new ManaConcentratorInput(inputItems, type.getTier());
        Optional<RecipeHolder<ManaConcentratorRecipe>> optional = recipes.getRecipeFor(
                ManaweaveAndRunesRecipeInit.MANA_CONCENTRATOR_RECIPE_TYPE.get(), input, level);
        ManaConcentratorRecipe recipe = optional.map(RecipeHolder::value).orElse(null);

        if (recipe != null) {
            for (int i = 0; i < inventory.getSlots(); i++) {
                ItemStack stack = inventory.getStackInSlot(i);
                if (!stack.isEmpty() && stack.getItem() instanceof IManaItem manaItem) {
                    int manaAmount = recipe.manaMap().getOrDefault(manaItem.getManaType(), 0);
                    if (manaAmount != 0) {
                        IManaHandler handler = stack.getCapability(ManaweaveAndRunesCapabilities.MANA_HANDLER_ITEM);
                        if (handler != null) {
                            int extracted = handler.extractMana(manaAmount, manaItem.getManaType(), false);
                            if (extracted != manaAmount) {
                                return ItemStack.EMPTY;
                            }
                        }
                    }
                }
            }

            relativePedestalPositions.stream()
                    .map(pos -> level.getBlockEntity(getBlockPos().offset(pos)))
                    .filter(blockEntity -> blockEntity instanceof RunePedestalBlockEntity)
                    .map(blockEntity -> (RunePedestalBlockEntity) blockEntity)
                    .forEach(runePedestalBlockEntity -> runePedestalBlockEntity.getItemHandler(null)
                            .extractItem(0, 1, false));
            return recipe.assemble(input, level.registryAccess());
        }

        return ItemStack.EMPTY;
    }

    public ManaConcentratorType getManaConcentratorType() {
        return level.getBlockState(getBlockPos()).getBlock() instanceof ManaConcentratorBlock block
                ? block.getType()
                : null;
    }

    public boolean placeItem(ItemStack stack) {
        for (int i = 0; i < inventory.getSlots(); i++) {
            if (inventory.getStackInSlot(i).isEmpty() && inventory.insertItem(i, stack.copy(), false).isEmpty()) {
                slotStack.push(i);
                markUpdated();
                return true;
            }
        }
        return false;
    }

    public ItemStack removeItem() {
        // Remove the last item placed or the first existing item in the inventory
        while (!slotStack.isEmpty()) {
            int slot = slotStack.pop();
            ItemStack stack = inventory.extractItem(slot, inventory.getStackInSlot(slot).getCount(), false);
            if (!stack.isEmpty()) {
                markUpdated();
                return stack;
            }
        }
        for (int i = 0; i < inventory.getSlots(); i++) {
            ItemStack stack = inventory.extractItem(i, inventory.getStackInSlot(i).getCount(), false);
            markUpdated();
            if (!stack.isEmpty()) {
                markUpdated();
                return stack;
            }
        }
        return ItemStack.EMPTY;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        isActive = tag.contains("isActive") && tag.getBoolean("isActive");
        inventory.deserializeNBT(registries, tag.getCompound("inventory"));
        if (tag.contains("craftTime")) {
            craftTime = tag.getInt("craftTime");
        }
        if (tag.contains("craftTimeRemaining")) {
            craftTimeRemaining = tag.getInt("craftTimeRemaining");
        }
        if (tag.contains("slotStack")) {
            this.slotStack = new Stack<>();
            Arrays.stream(tag.getIntArray("slotStack")).forEach(slotStack::push);
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putBoolean("isActive", isActive);
        tag.put("inventory", inventory.serializeNBT(registries));
        tag.putInt("craftTime", craftTime);
        tag.putInt("craftTimeRemaining", craftTimeRemaining);
        tag.putIntArray("slotStack", slotStack.stream().mapToInt(i -> i).toArray());
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag tag = super.getUpdateTag(registries);
        saveAdditional(tag, registries);
        return tag;
    }
}
