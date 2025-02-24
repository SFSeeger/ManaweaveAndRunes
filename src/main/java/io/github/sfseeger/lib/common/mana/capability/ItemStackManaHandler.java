package io.github.sfseeger.lib.common.mana.capability;

import io.github.sfseeger.lib.common.mana.Mana;
import io.github.sfseeger.lib.common.mana.ManaDataComponent;
import io.github.sfseeger.manaweave_and_runes.core.init.MRDataComponentsInit;
import net.minecraft.core.Holder;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Supplier;

public class ItemStackManaHandler extends ManaHandler {
    protected ItemStack stack;

    public ItemStackManaHandler(ItemStack stack, int capacity, int maxManaReceive, int maxManaExtract,
            @Nullable List<Supplier<Mana>> allowedMana) {
        super(capacity, maxManaReceive, maxManaExtract, allowedMana);
        this.stack = stack;
        ManaDataComponent manaComponent = getManaDataComponent();

        for (Mana mana : manaComponent.getManaTypes().stream().map(Holder::value).toList()) {
            this.setMana(mana, manaComponent.getManaAmount(mana));
        }
    }

    @Override
    public int receiveMana(int amount, Mana manaType, boolean simulate) {
        int received = super.receiveMana(amount, manaType, simulate);
        if (!simulate && received > 0) {
            updateManaDataComponent(manaType);
        }
        return received;
    }

    @Override
    public int extractMana(int amount, Mana manaType, boolean simulate) {
        int extracted = super.extractMana(amount, manaType, simulate);
        if (!simulate && extracted > 0) {
            updateManaDataComponent(manaType);
        }

        return extracted;
    }

    @Override
    public int getManaStored(Mana manaType) {
        return getManaDataComponent().getManaAmount(manaType);
    }

    private void updateManaDataComponent(Mana manaType) {
        ManaDataComponent manaDataComponent = getManaDataComponent();
        manaDataComponent.setManaAmount(manaType,
                                        super.getManaStored(manaType));
        this.stack.set(MRDataComponentsInit.MANA_DATA_COMPONENT, manaDataComponent);
    }

    public ManaDataComponent getManaDataComponent() {
        return this.stack.getOrDefault(MRDataComponentsInit.MANA_DATA_COMPONENT,
                                       new ManaDataComponent());
    }
}
