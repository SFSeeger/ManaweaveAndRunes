package io.github.sfseeger.manaweave_and_runes.common.blockentities;

import io.github.sfseeger.lib.common.items.SpellPartHolderItem;
import io.github.sfseeger.lib.common.spells.*;
import io.github.sfseeger.lib.common.spells.data_components.SpellDataComponent;
import io.github.sfseeger.manaweave_and_runes.core.payloads.CraftPayload;
import io.github.sfseeger.manaweave_and_runes.core.payloads.ICraftingPacketHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;

import java.util.ArrayList;

import static io.github.sfseeger.manaweave_and_runes.core.init.ManaweaveAndRunesBlockEntityInit.SPELL_DESIGNER_BLOCK_ENTITY;
import static io.github.sfseeger.manaweave_and_runes.core.init.ManaweaveAndRunesDataComponentsInit.SPELL_DATA_COMPONENT;
import static io.github.sfseeger.manaweave_and_runes.core.init.ManaweaveAndRunesDataComponentsInit.SPELL_PART_DATA_COMPONENT;
import static io.github.sfseeger.manaweave_and_runes.core.init.ManaweaveAndRunesItemInit.AMETHYST_SPELL_HOLDER_ITEM;
import static io.github.sfseeger.manaweave_and_runes.core.init.ManaweaveAndRunesItemInit.AMETHYST_SPELL_PART_ITEM;

public class SpellDesignerBlockEntity extends BlockEntity implements ICraftingPacketHandler {
    public static final int MAIN_SLOT_INDEX = 0;

    private ItemStackHandler itemHandler = new ItemStackHandler(7);
    private static final String DEFAULT_SPELL_NAME = "Unnamed Spell";
    private String spellName = DEFAULT_SPELL_NAME;

    public SpellDesignerBlockEntity(BlockPos pos, BlockState blockState) {
        super(SPELL_DESIGNER_BLOCK_ENTITY.get(), pos, blockState);
    }

    public ItemStack assembleSpell() {
        ItemStack stack = itemHandler.getStackInSlot(MAIN_SLOT_INDEX);

        if (stack.isEmpty()) {
            return ItemStack.EMPTY;
        }
        if (stack.getItem() instanceof SpellPartHolderItem) {
            if (!stack.has(SPELL_PART_DATA_COMPONENT)) {
                return ItemStack.EMPTY;
            }

            boolean hasEffects = false;
            SpellPart[] parts = new SpellPart[4];
            for (int i = 0; i < 4; i++) {
                SpellPart p = itemHandler.getStackInSlot(i + 1).get(SPELL_PART_DATA_COMPONENT);
                if(p == null){
                    continue;
                }
                if(p.getCore().value() instanceof AbstractSpellEffect){
                    hasEffects = true;
                }
                parts[i] = p;
            }

            SpellPart part = stack.get(SPELL_PART_DATA_COMPONENT);
            Spell spell = new Spell();
            spell.setName(spellName);
            if(hasEffects && part.getCore().value() instanceof AbstractSpellType spellType) {
                //Spell creation logic
                spell.setSpellType(spellType);
                if (!part.getModifiers().isEmpty())
                    spell.getModifiers().computeIfAbsent(spellType, k -> new ArrayList<>()).addAll(part.getModifiers());
                for(SpellPart p : parts){
                    if (p != null && p.getCore().value() instanceof AbstractSpellEffect effect) {
                        spell.getEffects().add(effect);
                        if (!p.getModifiers().isEmpty()) spell.getModifiers()
                                .computeIfAbsent(effect, k -> new ArrayList<>())
                                .addAll(p.getModifiers());
                    }
                }
                if(spell.isValid()){
                    ItemStack stack1 = new ItemStack(AMETHYST_SPELL_HOLDER_ITEM.get(), 1);
                    stack1.set(SPELL_DATA_COMPONENT, new SpellDataComponent(spell));
                    return stack1;
                }
            } else if(!hasEffects){
                SpellPart part1 = new SpellPart(part.getCore(), new ArrayList<>());
                for(SpellPart p : parts){
                    if (p != null && p.getCore().value() instanceof AbstractSpellModifier) {
                        part1.getModifiers().add((AbstractSpellModifier) (p.getCore().value()));
                    }
                }
                ItemStack stack1 = new ItemStack(AMETHYST_SPELL_PART_ITEM.get(), 1);
                stack1.set(SPELL_PART_DATA_COMPONENT, part1);
                return stack1;
            }
        }
        return ItemStack.EMPTY;
    }

    public void markChanged(){
        setChanged();
        if(level != null){
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

    public ItemStackHandler getItemHandler() {
        return itemHandler;
    }

    public void setSpellName(String name) {
        spellName = name;
        markChanged();
    }

    public String getSpellName() {
        return spellName;
    }

    public void onCraft(Player player) {
        if (itemHandler.getStackInSlot(6).isEmpty()) {
            ItemStack stack = assembleSpell();
            if (!stack.isEmpty() && !player.level().isClientSide) {
                itemHandler.setStackInSlot(6, stack);
                for (int i = 0; i < 5; i++) {
                    if (!itemHandler.getStackInSlot(i).isEmpty()) itemHandler.extractItem(i, 1, false);
                }
                itemHandler.getStackInSlot(5)
                        .hurtAndBreak(200, (ServerLevel) player.level(), (ServerPlayer) player, e -> {
                        });
                setSpellName("");
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
}
