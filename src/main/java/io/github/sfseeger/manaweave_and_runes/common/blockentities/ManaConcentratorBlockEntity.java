package io.github.sfseeger.manaweave_and_runes.common.blockentities;

import io.github.sfseeger.lib.common.Tier;
import io.github.sfseeger.lib.common.mana.Mana;
import io.github.sfseeger.lib.common.mana.capability.IManaHandler;
import io.github.sfseeger.lib.common.mana.capability.IManaItem;
import io.github.sfseeger.lib.common.mana.capability.ManaweaveAndRunesCapabilities;
import io.github.sfseeger.lib.common.recipes.mana_concentrator.ManaConcentratorInput;
import io.github.sfseeger.lib.common.recipes.mana_concentrator.ManaConcentratorRecipe;
import io.github.sfseeger.manaweave_and_runes.common.blocks.RuneBlock;
import io.github.sfseeger.manaweave_and_runes.common.blocks.mana_concentrator.ManaConcentratorBlock;
import io.github.sfseeger.manaweave_and_runes.common.blocks.mana_concentrator.ManaConcentratorType;
import io.github.sfseeger.manaweave_and_runes.core.init.MRBlockEntityInit;
import io.github.sfseeger.manaweave_and_runes.core.init.MRBlockInit;
import io.github.sfseeger.manaweave_and_runes.core.init.MRParticleTypeInit;
import io.github.sfseeger.manaweave_and_runes.core.init.MRRecipeInit;
import io.github.sfseeger.manaweave_and_runes.core.util.IInventoryBlockEntity;
import io.github.sfseeger.manaweave_and_runes.core.util.MultiblockValidator;
import io.github.sfseeger.manaweave_and_runes.core.util.ParticleUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.BlockItem;
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
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.*;

import static io.github.sfseeger.lib.common.LibUtils.encode;
import static io.github.sfseeger.lib.common.ManaweaveAndRunesCodecs.BLOCK_POS_LIST_CODEC;

public class ManaConcentratorBlockEntity extends BlockEntity implements IInventoryBlockEntity, GeoBlockEntity {
    protected static final RawAnimation DEPLOY_ANIMATION = RawAnimation.begin().thenLoop("idle");
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
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
    private int craftTimePassed;
    private boolean isCrafting;
    private Stack<Integer> slotStack = new Stack<>();
    private ManaConcentratorRecipe currentRecipe;

    public IItemHandler getItemHandler(@Nullable Direction side) {
        return inventory;
    }

    public ManaConcentratorBlockEntity(BlockPos pos, BlockState state) {
        super(MRBlockEntityInit.MANA_CONCENTRATOR_BLOCK_ENTITY.get(), pos, state);
    }
    private List<BlockPos> pedestalPositions;

    public static void serverTick(Level level, BlockPos pos, BlockState state,
            ManaConcentratorBlockEntity blockEntity) {
        if (level.getGameTime() % 20 == 0) {
            boolean isActive = blockEntity.validateMultiblock().isValid();
            if (!isActive && blockEntity.isCrafting) blockEntity.abortCrafting();
            blockEntity.setActive(isActive);
            blockEntity.markUpdated();
        }

        if (blockEntity.isCrafting) {
            blockEntity.craftTimePassed++;
            if (blockEntity.craftTimePassed >= blockEntity.craftTime) {
                ItemStack result = blockEntity.craft();
                if (!result.isEmpty()) {
                    if (result.getItem() instanceof BlockItem blockItem && blockItem.getBlock() instanceof ManaConcentratorBlock block) {
                        level.setBlockAndUpdate(pos, block.defaultBlockState());
                    } else {
                        level.addFreshEntity(
                                new ItemEntity(level, pos.getX() + 0.5, pos.getY() + 1.5, pos.getZ() + 0.5, result));
                    }

                    level.playSound(null, pos, SoundEvents.AMETHYST_BLOCK_RESONATE, SoundSource.BLOCKS, 1.0F, 1.0F);

                    float yOffset = blockEntity.getEffectYOffset();
                    ((ServerLevel) level).sendParticles(MRParticleTypeInit.MANA_CONCENTRATED.get(), pos.getX() + .5,
                                                        pos.getY() + yOffset, pos.getZ() + 0.5,
                                                        1, 0, 0, 0, yOffset < 1.5 ? 1 : 0);
                    blockEntity.stopCrafting();
                }
                blockEntity.stopCrafting();
            } else if (level.getGameTime() % 15 == 0) {
                level.playSound(null, pos, SoundEvents.AMETHYST_BLOCK_PLACE, SoundSource.BLOCKS, 1.0F,
                                1.0F + level.random.nextFloat() * 0.1F);
            }

        } else if (blockEntity.craftTime > 0 && blockEntity.isActive && blockEntity.craftTimePassed <= blockEntity.craftTime) {
            ManaConcentratorRecipe recipe = blockEntity.getAvailableRecipe();
            if (recipe == null) {
                blockEntity.startCrafting();
            } else blockEntity.currentRecipe = recipe;
        }
    }

    private void stopCrafting() {
        isCrafting = false;
        craftTime = 0;
        craftTimePassed = 0;
        markUpdated();
        getManaConcentratorType().findBlocks(level, MRBlockInit.RUNE_BLOCK.get()).forEach(pos -> {
            BlockPos blockPos = getBlockPos().offset(pos);
            BlockState state = level.getBlockState(blockPos);
            try {
                level.setBlockAndUpdate(blockPos, state.setValue(RuneBlock.ACTIVE, false));
            } catch (IllegalArgumentException e) {
                // Ignore
            }
        });
    }

    private void abortCrafting() {
        stopCrafting();
        if (level != null) {
            level.playSound(null, getBlockPos(), SoundEvents.AMETHYST_BLOCK_BREAK, SoundSource.BLOCKS, 1.0F, 1.0F);
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

    public boolean startCrafting() {
        if (!isActive || level == null || isCrafting) {
            return false;
        }
        currentRecipe = getAvailableRecipe();
        if (currentRecipe != null) {
            craftTime = currentRecipe.craftTime();
            craftTimePassed = 0;
            isCrafting = true;
            level.playSound(null, getBlockPos(), SoundEvents.BEACON_ACTIVATE, SoundSource.BLOCKS, 1.0F, 1.0F);
            getManaConcentratorType().findBlocks(level, MRBlockInit.RUNE_BLOCK.get()).forEach(pos -> {
                BlockPos blockPos = getBlockPos().offset(pos);
                BlockState state = level.getBlockState(blockPos);
                level.setBlockAndUpdate(blockPos, state.setValue(RuneBlock.ACTIVE, true));
            });
            markUpdated();
            return true;
        }
        return false;
    }

    public ManaConcentratorRecipe getAvailableRecipe() {
        if (!isActive || level == null) {
            return null;
        }
        RecipeManager recipes = level.getRecipeManager();
        ManaConcentratorType type = getManaConcentratorType();
        ManaConcentratorInput input = getInput(type);
        Optional<RecipeHolder<ManaConcentratorRecipe>> optional = recipes.getRecipeFor(
                MRRecipeInit.MANA_CONCENTRATOR_RECIPE_TYPE.get(), input, level);
        return optional.map(RecipeHolder::value).orElse(null);
    }

    public static void clientTick(Level level, BlockPos pos, BlockState state,
            ManaConcentratorBlockEntity blockEntity) {
        RandomSource randomsource = level.getRandom();


        if (randomsource.nextFloat() <= 0.5F && blockEntity.isActive()) {
            Vec3 vec3 = ParticleUtils.randomPosInsideBox(pos, randomsource, -0.25, 0.25, -0.25, 1.25, 0.75, 1.25);
            level.addParticle(ParticleTypes.GLOW, vec3.x(), vec3.y(), vec3.z(), 0.0, 0.0, 0.0);
        }

        if (blockEntity.isCrafting) {
            Vec3 vec3 = ParticleUtils.randomPosInsideBox(pos, randomsource, -0.25, 0.25, -0.25, 1.25, 0.75, 1.25);
            level.addParticle(ParticleTypes.END_ROD, vec3.x(), vec3.y(), vec3.z(), 0.0, 0.0, 0.0);

            List<BlockPos> filteredPedestalPositions = blockEntity.pedestalPositions.stream()
                    .filter(pedestalPos -> level.getBlockEntity(
                            pos.offset(pedestalPos)) instanceof RunePedestalBlockEntity re && !re.getItem().isEmpty())
                    .toList();

            for (BlockPos pedestalPos : filteredPedestalPositions.stream().map(pos::offset).toList()) {
                Vec3 pedestalVec =
                        new Vec3(pedestalPos.getX(), pedestalPos.getY(), pedestalPos.getZ());
                Vec3 vecToConcentrator = new Vec3(pos.getX(), pos.getY(), pos.getZ()).vectorTo(pedestalVec);
                for (int i = 0; i < 4; i++) {
                    Vec3 randomPedestalVec = vecToConcentrator.offsetRandom(randomsource, .5f);
                    level.addParticle(MRParticleTypeInit.MANA_TRAVEL_PARTICLE.get(),
                                      pos.getX() + 0.5f, pos.getY() + 1.5f, pos.getZ() + 0.5f,
                                      randomPedestalVec.x(), randomPedestalVec.y(), randomPedestalVec.z());
                }
            }
        }
    }

    public ItemStack craft() {
        if (!isActive || level == null) {
            return ItemStack.EMPTY;
        }
        ManaConcentratorType type = getManaConcentratorType();
        List<BlockPos> relativePedestalPositions =
                type.findBlocks(level, MRBlockInit.RUNE_PEDESTAL_BLOCK.get());

        if (this.currentRecipe != null) {
            Map<Mana, Integer> extractedMana = new HashMap<>();
            for (int i = 0; i < inventory.getSlots(); i++) {
                ItemStack stack = inventory.getStackInSlot(i);
                if (stack.isEmpty() || !(stack.getItem() instanceof IManaItem manaItem)) {
                    continue;
                }

                // TODO: Allow for mana from storage blocks
                // after that, abort with side effect
                Mana manaType = manaItem.getManaType();
                int manaAmount = this.currentRecipe.manaMap().getOrDefault(manaType, 0);
                if (manaAmount != 0) {
                    IManaHandler handler = stack.getCapability(ManaweaveAndRunesCapabilities.MANA_HANDLER_ITEM);
                    if (handler != null) {
                        int alreadyExtracted = extractedMana.getOrDefault(manaType, 0);
                        int extracted = handler.extractMana(manaAmount - alreadyExtracted, manaType, false);
                        if (extracted > 0) {
                            extractedMana.put(manaType, alreadyExtracted + extracted);
                        }
                    }
                }
            }
            if (extractedMana.size() != this.currentRecipe.manaMap().size()) {
                abortCrafting();
                return ItemStack.EMPTY;
            }
            if (extractedMana.entrySet()
                    .stream()
                    .anyMatch(entry -> entry.getValue() < this.currentRecipe.manaMap().get(entry.getKey()))) {
                abortCrafting();
                return ItemStack.EMPTY;
            }

            relativePedestalPositions.stream()
                    .map(pos -> level.getBlockEntity(getBlockPos().offset(pos)))
                    .filter(blockEntity -> blockEntity instanceof RunePedestalBlockEntity)
                    .map(blockEntity -> (RunePedestalBlockEntity) blockEntity)
                    .forEach(runePedestalBlockEntity -> runePedestalBlockEntity.getItemHandler(null)
                            .extractItem(0, 1, false));
            return this.currentRecipe.assemble(getInput(type), level.registryAccess());
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

    public ManaConcentratorInput getInput(ManaConcentratorType type) {
        List<BlockPos> relativePedestalPositions =
                type.findBlocks(level, MRBlockInit.RUNE_PEDESTAL_BLOCK.get());
        // We can set this here to save on updates
        pedestalPositions = relativePedestalPositions;
        List<ItemStack> inputItems = new ArrayList<>();
        for (BlockPos pos : relativePedestalPositions) {
            BlockEntity blockEntity = level.getBlockEntity(getBlockPos().offset(pos));
            ItemStack item;
            if (blockEntity instanceof RunePedestalBlockEntity runePedestalBlockEntity && !(item =
                    runePedestalBlockEntity.getItem()).isEmpty()) {
                inputItems.add(item);
            }
        }
        return new ManaConcentratorInput(inputItems, type.getTier());
    }

    public float getEffectYOffset() {
        ManaConcentratorType type = getManaConcentratorType();
        return type != null && type.getTier() == Tier.ASCENDED ? 0.5f : 1.5f; // TODO: maybe move this to the type
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
            craftTimePassed = tag.getInt("craftTimeRemaining");
        }
        if (tag.contains("slotStack")) {
            this.slotStack = new Stack<>();
            Arrays.stream(tag.getIntArray("slotStack")).forEach(slotStack::push);
        }
        if (tag.contains("isCrafting")) {
            isCrafting = tag.getBoolean("isCrafting");
        }
        if (tag.contains("pedestalPositions")) {
            pedestalPositions = BLOCK_POS_LIST_CODEC.parse(NbtOps.INSTANCE, tag.get("pedestalPositions"))
                    .result()
                    .orElseThrow(() -> new IllegalArgumentException("Failed to parse pedestal positions"));
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putBoolean("isActive", isActive);
        tag.put("inventory", inventory.serializeNBT(registries));
        tag.putInt("craftTime", craftTime);
        tag.putInt("craftTimeRemaining", craftTimePassed);
        tag.putIntArray("slotStack", slotStack.stream().mapToInt(i -> i).toArray());
        tag.putBoolean("isCrafting", isCrafting);
        tag.put("pedestalPositions", encode(BLOCK_POS_LIST_CODEC,
                                            pedestalPositions == null || pedestalPositions.isEmpty() ? List.of() : pedestalPositions,
                                            registries));
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

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 0, this::deployAnimController));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    private PlayState deployAnimController(AnimationState<ManaConcentratorBlockEntity> state) {
        return state.setAndContinue(DEPLOY_ANIMATION);
    }
}
