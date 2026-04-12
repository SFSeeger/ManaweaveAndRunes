package io.github.sfseeger.manaweave_and_runes.common.blockentities;

import com.mojang.datafixers.util.Either;
import io.github.sfseeger.lib.common.items.SpellPartHolderItem;
import io.github.sfseeger.lib.common.mana.Mana;
import io.github.sfseeger.lib.common.spells.Spell;
import io.github.sfseeger.lib.common.spells.SpellNodeType;
import io.github.sfseeger.lib.common.spells.SpellPart;
import io.github.sfseeger.manaweave_and_runes.common.spells.SpellAssembler;
import io.github.sfseeger.manaweave_and_runes.core.init.MRTagInit;
import io.github.sfseeger.manaweave_and_runes.core.payloads.CraftPayload;
import io.github.sfseeger.manaweave_and_runes.core.payloads.ICraftingPacketHandler;
import io.github.sfseeger.manaweave_and_runes.core.util.IInventoryBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.github.sfseeger.manaweave_and_runes.core.init.MRBlockEntityInit.SPELL_DESIGNER_BLOCK_ENTITY;
import static io.github.sfseeger.manaweave_and_runes.core.init.MRDataComponentsInit.SPELL_PART_DATA_COMPONENT;
import static io.github.sfseeger.manaweave_and_runes.core.init.MRItemInit.DIAMOND_CHISEL;


public class SpellDesignerBlockEntity extends BlockEntity implements ICraftingPacketHandler, IInventoryBlockEntity {
    public static final int MAIN_SLOT_INDEX = 0;
    public static final int CHISEL_SLOT_INDEX = 5;
    public static final int OUTPUT_SLOT_INDEX = 6;
    private static final String DEFAULT_SPELL_NAME = "Spell";
    private final ItemStackHandler itemHandler = new ItemStackHandler(OUTPUT_SLOT_INDEX + 1);
    private String spellName = DEFAULT_SPELL_NAME;

    public SpellDesignerBlockEntity(BlockPos pos, BlockState blockState) {
        super(SPELL_DESIGNER_BLOCK_ENTITY.get(), pos, blockState);
    }

    public @Nullable Either<Spell, SpellPart> getCurrentResult() {
        ItemStack stack = itemHandler.getStackInSlot(MAIN_SLOT_INDEX);

        if (stack.isEmpty()) {
            return null;
        }
        if (!(stack.getItem() instanceof SpellPartHolderItem) || !stack.has(SPELL_PART_DATA_COMPONENT))
            return null;

        SpellPart coreSpellPart = stack.get(SPELL_PART_DATA_COMPONENT);

        boolean hasEffects = false;
        List<SpellPart> parts = new ArrayList<>(4);
        for (int i = 0; i < 4; i++) {
            SpellPart p = itemHandler.getStackInSlot(i + 1).get(SPELL_PART_DATA_COMPONENT);
            if (p == null) {
                continue;
            }
            if (p.getSpellNodeType() == SpellNodeType.EFFECT) {
                hasEffects = true;
            }
            parts.add(p);
        }
        if (coreSpellPart.getSpellNodeType() == SpellNodeType.TYPE && hasEffects) {
            Spell spell = SpellAssembler.assambleSpell(coreSpellPart, parts, spellName);
            return spell != null ? Either.left(spell) : null;
        } else {
            SpellPart spellPart = SpellAssembler.assembleSpellPart(coreSpellPart, parts);
            return spellPart != null ? Either.right(spellPart) : null;
        }
    }

    public ItemStack assembleSpell() {
        Either<Spell, SpellPart> result = getCurrentResult();
        if (result == null) return ItemStack.EMPTY;

        if (result.left().isPresent()) {
            return SpellAssembler.createSpellItemStack(result.left().get());
        } else if (result.right().isPresent()) {
            return SpellAssembler.createSpellPartItemStack(result.right().get(), spellName);
        }
        return ItemStack.EMPTY;
    }

    public void markChanged() {
        setChanged();
        if (level != null) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        itemHandler.deserializeNBT(registries, tag.getCompound("Inventory"));
        spellName = tag.getString("SpellName");
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("Inventory", itemHandler.serializeNBT(registries));
        tag.putString("SpellName", spellName);
    }

    @SuppressWarnings("unchecked")
    public ItemStackHandler getItemHandler(@Nullable Direction side) {
        return itemHandler;
    }

    public String getSpellName() {
        return spellName;
    }

    public void setSpellName(String name) {
        spellName = name;
        markChanged();
    }

    public void onCraft(Player player) {
        if (itemHandler.getStackInSlot(6).isEmpty() && (player.isCreative() || hasChisel())) {
            ItemStack stack = assembleSpell();
            if (!stack.isEmpty() && !player.level().isClientSide) {
                itemHandler.setStackInSlot(OUTPUT_SLOT_INDEX, stack);
                for (int i = 0; i < 5; i++) {
                    if (!itemHandler.getStackInSlot(i).isEmpty()) itemHandler.extractItem(i, 1, false);
                }
                itemHandler.getStackInSlot(CHISEL_SLOT_INDEX)
                        .hurtAndBreak(4, (ServerLevel) player.level(), (ServerPlayer) player, e -> {
                        });
                setSpellName(DEFAULT_SPELL_NAME);
                markChanged();
            }
        }
    }

    @Override
    public void onPacketReceive(CraftPayload payload, Player player) {
        switch (payload.actionId()) {
            default -> {
                if (payload.customName() != null && !payload.customName().isEmpty()) {
                    setSpellName(payload.customName());
                }
                onCraft(player);
            }
        }
    }

    public boolean hasChisel() {
        ItemStack s = itemHandler.getStackInSlot(5);
        return !s.isEmpty() && s.is(MRTagInit.CHISEL_ITEM);
    }

    public Map<Mana, Integer> getManaCost() {
        Either<Spell, SpellPart> result = getCurrentResult();

        if (result != null) {
            if (result.left().isPresent()) {
                return result.left().get().getManaCost();
            } else if (result.right().isPresent()) {
                return result.right().get().getManaCost();
            }
        }
        return new HashMap<>();
    }
}
