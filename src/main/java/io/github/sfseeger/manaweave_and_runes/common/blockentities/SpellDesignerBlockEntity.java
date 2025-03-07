package io.github.sfseeger.manaweave_and_runes.common.blockentities;

import io.github.sfseeger.lib.common.items.SpellPartHolderItem;
import io.github.sfseeger.lib.common.mana.Mana;
import io.github.sfseeger.lib.common.spells.*;
import io.github.sfseeger.lib.common.spells.data_components.SpellDataComponent;
import io.github.sfseeger.manaweave_and_runes.core.payloads.CraftPayload;
import io.github.sfseeger.manaweave_and_runes.core.payloads.ICraftingPacketHandler;
import io.github.sfseeger.manaweave_and_runes.core.util.IInventoryBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
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
import static io.github.sfseeger.manaweave_and_runes.core.init.MRDataComponentsInit.SPELL_DATA_COMPONENT;
import static io.github.sfseeger.manaweave_and_runes.core.init.MRDataComponentsInit.SPELL_PART_DATA_COMPONENT;
import static io.github.sfseeger.manaweave_and_runes.core.init.MRItemInit.*;

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
                    ItemStack stack1 = new ItemStack(SPELL_HOLDER_ITEM.get(), 1);
                    stack1.set(SPELL_DATA_COMPONENT, new SpellDataComponent(spell));
                    return stack1;
                }
            } else if(!hasEffects){
                List<AbstractSpellModifier> modifiers = new ArrayList<>();
                if (!part.getModifiers().isEmpty()) {
                    modifiers = part.getModifiers();
                    if (modifiers.size() > 16) return ItemStack.EMPTY;
                }
                SpellPart part1 = new SpellPart(part.getCore(), modifiers); //Replace with modifiers from parts?
                for(SpellPart p : parts){
                    if (p != null && p.getCore().value() instanceof AbstractSpellModifier) {
                        part1.getModifiers().add((AbstractSpellModifier) (p.getCore().value()));
                    }
                }
                ItemStack stack1 = new ItemStack(SPELL_PART.get(), 1);
                stack1.set(SPELL_PART_DATA_COMPONENT, part1);
                stack1.set(DataComponents.CUSTOM_NAME, Component.literal(spellName));
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

    @SuppressWarnings("unchecked")
    public ItemStackHandler getItemHandler(@Nullable Direction side) {
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

    public boolean hasChisel() {
        ItemStack s = itemHandler.getStackInSlot(5);
        return !s.isEmpty() && s.getItem() == DIAMOND_CHISEL.get(); //TODO: Change to chisel item
    }

    public Map<Mana, Integer> getManaCost() {
        Map<Mana, Integer> cost = new HashMap<>();
        int count = 0;
        for (int i = 0; i < CHISEL_SLOT_INDEX; i++) {
            ItemStack stack = itemHandler.getStackInSlot(i);
            SpellPart part = stack.get(SPELL_PART_DATA_COMPONENT);
            if (part != null) {
                for (Map.Entry<Mana, Integer> entry : part.getCore().value().getManaCost().entrySet()) {
                    cost.put(entry.getKey(), cost.getOrDefault(entry.getKey(), 0) + entry.getValue());
                }
                for (AbstractSpellModifier modifier : part.getModifiers()) {
                    for (Map.Entry<Mana, Integer> entry : modifier.getManaCost().entrySet()) {
                        cost.put(entry.getKey(), cost.getOrDefault(entry.getKey(), 0) + entry.getValue());
                    }
                }
                count = count + part.getModifiers().size() + 1;
            }
        }
        int scalar = Spell.getModifierCostScalar(count - 1);
        for (Map.Entry<Mana, Integer> entry : cost.entrySet()) {
            entry.setValue(entry.getValue() * scalar);
        }
        return cost;
    }
}
