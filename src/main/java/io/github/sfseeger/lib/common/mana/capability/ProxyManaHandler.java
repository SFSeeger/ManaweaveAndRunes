package io.github.sfseeger.lib.common.mana.capability;

import io.github.sfseeger.lib.common.mana.Mana;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ProxyManaHandler implements IManaHandler {
    private final IItemHandler runeInventory;

    public ProxyManaHandler(IItemHandler runeInventory) {
        this.runeInventory = runeInventory;
    }

    @Override
    public int receiveMana(int amount, Mana manatype, boolean simulate) {
        int remaining = amount;
        for (int i = 0; i < runeInventory.getSlots(); i++) {
            IManaHandler handler = getManaHandler(i);
            if (handler != null) {
                if (handler.canReceive(manatype)) {
                    int received = handler.receiveMana(remaining, manatype, simulate);
                    remaining -= received;
                    if (remaining <= 0) return amount;
                }
            }
        }
        return amount - remaining;
    }

    @Override
    public int extractMana(int amount, Mana manatype, boolean simulate) {
        int extracted = 0;
        for (int i = 0; i < runeInventory.getSlots(); i++) {
            IManaHandler handler = getManaHandler(i);
            if (handler != null) {
                if (handler.canExtract(manatype)) {
                    int pulled = handler.extractMana(amount - extracted, manatype, simulate);
                    extracted += pulled;
                    if (extracted >= amount) return amount;
                }
            }
        }
        return extracted;
    }

    @Override
    public int getManaStored(Mana manatype) {
        int total = 0;
        for (int i = 0; i < runeInventory.getSlots(); i++) {
            IManaHandler handler = getManaHandler(i);
            if (handler != null) {
                total += handler.getManaStored(manatype);
            }
        }
        return total;
    }

    @Override
    public int getManaCapacity() {
        int total = 0;
        for (int i = 0; i < runeInventory.getSlots(); i++) {
            IManaHandler handler = getManaHandler(i);
            if (handler != null) {
                total += handler.getManaCapacity();
            }
        }
        return total;
    }

    @Override
    public boolean canExtract(Mana manatype) {
        for (int i = 0; i < runeInventory.getSlots(); i++) {
            IManaHandler handler = getManaHandler(i);
            if (handler != null) {
                if (handler.canExtract(manatype)) return true;
            }
        }
        return false;
    }

    @Override
    public boolean canReceive(Mana manatype) {
        for (int i = 0; i < runeInventory.getSlots(); i++) {
            IManaHandler handler = getManaHandler(i);
            if (handler != null) {
                if (handler.canReceive(manatype)) return true;
            }
        }
        return false;
    }

    @Override
    public List<Mana> getManaTypesStored() {
        List<Mana> types = new ArrayList<>();
        for (int i = 0; i < runeInventory.getSlots(); i++) {
            IManaHandler handler = getManaHandler(i);
            if (handler != null) {
                for (Mana type : handler.getManaTypesStored()) {
                    if (!types.contains(type)) {
                        types.add(type);
                    }
                }
            }
        }
        return types;
    }

    @Override
    public boolean hasMana(Mana manatype) {
        for (int i = 0; i < runeInventory.getSlots(); i++) {
            IManaHandler handler = getManaHandler(i);
            if (handler != null) {
                if (handler.hasMana(manatype)) return true;
            }
        }
        return false;
    }

    public int getManaCapacity(Mana manatype) {
        int total = 0;
        for (int i = 0; i < runeInventory.getSlots(); i++) {
            IManaHandler handler = getManaHandler(i);
            if (handler != null && handler.hasMana(manatype)) {
                total += handler.getManaCapacity();
            }
        }
        return total;
    }

    private @Nullable IManaHandler getManaHandler(int slot) {
        ItemStack stack = runeInventory.getStackInSlot(slot);
        if (stack.getItem() instanceof IManaItem rune) {
            return rune.getManaHandler(stack);
        }
        return null;
    }
}
