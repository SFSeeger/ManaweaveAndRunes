package io.github.sfseeger.manaweave_and_runes.common.blockentities;

import io.github.sfseeger.lib.common.items.SpellPartHolderItem;
import io.github.sfseeger.lib.common.spells.AbstractSpellType;
import io.github.sfseeger.lib.common.spells.SpellPart;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;

import static io.github.sfseeger.manaweave_and_runes.core.init.ManaweaveAndRunesBlockEntityInit.SPELL_DESIGNER_BLOCK_ENTITY;
import static io.github.sfseeger.manaweave_and_runes.core.init.ManaweaveAndRunesDataComponentsInit.SPELL_PART_DATA_COMPONENT;

public class SpellDesignerBlockEntity extends BlockEntity {
    public static final int MAIN_SLOT_INDEX = 0;

    private ItemStackHandler itemHandler = new ItemStackHandler(5);
    private String spellName = "";

    public SpellDesignerBlockEntity(BlockPos pos, BlockState blockState) {
        super(SPELL_DESIGNER_BLOCK_ENTITY.get(), pos, blockState);
    }

    public ItemStack assembleSpell() {
        ItemStack stack = itemHandler.getStackInSlot(MAIN_SLOT_INDEX);

        if (stack.isEmpty()) {
            return ItemStack.EMPTY;
        }
        if (stack.getItem() instanceof SpellPartHolderItem partHolder) {
            if (!stack.has(SPELL_PART_DATA_COMPONENT)) {
                return ItemStack.EMPTY;
            }
            SpellPart part = stack.get(SPELL_PART_DATA_COMPONENT);
            if (part.getCore() instanceof AbstractSpellType) {
                makeSpell();
            } else {
                makePart();
            }
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
}
