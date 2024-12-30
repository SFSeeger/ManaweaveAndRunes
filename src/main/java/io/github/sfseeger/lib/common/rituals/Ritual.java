package io.github.sfseeger.lib.common.rituals;


import io.github.sfseeger.lib.common.Tier;
import io.github.sfseeger.lib.common.mana.Mana;
import io.github.sfseeger.lib.core.ManaweaveAndRunesRegistries;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.core.BlockPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public abstract class Ritual {
    final boolean instant;
    final boolean doesTick;
    final Tier tier;
    String descriptionId;
    ResourceLocation ritualInputLocation;
    RitualInput input;

    public Ritual(boolean instant, boolean doesTick, Tier tier, ResourceLocation ritualInputLocation) {
        this.instant = instant;
        this.doesTick = doesTick;
        this.tier = tier;
        this.ritualInputLocation = ritualInputLocation;
    }

    public String getDescriptionId() {
        if (this.descriptionId == null) {
            this.descriptionId =
                    Util.makeDescriptionId("ritual", ManaweaveAndRunesRegistries.RITUAL_REGISTRY.getKey(this));
        }
        return this.descriptionId;
    }

    public MutableComponent getName() {
        return Component.translatable(getDescriptionId());
    }

    public Tier getTier() {
        return tier;
    }

    public abstract Vec3 getDimension();

    public void loadRitualInput(Level level) {
        RegistryAccess registryAccess;

        if (level == null) {
            return;
        }

        if (level.isClientSide) {
            ClientPacketListener connection = Minecraft.getInstance().getConnection();
            if (connection == null) {
                return;
            }
            registryAccess = connection.registryAccess();
        } else {
            registryAccess = level.registryAccess();
        }
        registryAccess.registry(ManaweaveAndRunesRegistries.RITUAL_INPUT_REGISTRY_KEY)
                .flatMap(reg -> Optional.ofNullable(reg.get(ritualInputLocation)))
                .ifPresentOrElse(ritualInput -> this.input = ritualInput,
                                 () -> {
                                     throw new IllegalStateException(
                                             "Ritual input not found for key: " + ritualInputLocation);
                                 });
    }

    /**
     * Returns the mana cost of the ritual
     * If the ritual is not instant, the mana cost will be the cost per tick
     */
    public Map<Mana, Integer> getManaCost() {
        return input.getManaCost();
    }

    public List<Ingredient> getInitialItemCost() {
        return input.getInitialItemCost();
    }

    public List<Ingredient> getTickItemCost() {
        return input.getTickItemCost();
    }

    ;

    public void onRitualTick(Level level, BlockPos pos, BlockState state) {
        if (!doesTick) {
            return;
        }
    }

    public void onRitualStart(Level level, BlockPos pos, BlockState state, RitualOriginType originType) {
    }

    public void onRitualEnd(Level level, BlockPos pos, BlockState state, RitualOriginType originType) {
    }

    public void onRitualInterrupt(Level level, BlockPos pos, BlockState state, RitualOriginType originType) {
    }

    public int getDuration() {
        return 0;
    }

    /**
     * Returns the rate in ticks at which items are consumed
     */
    public int getItemRate() {
        return 1;
    }

    /**
     * Returns the rate in ticks at which mana is consumed
     */
    public int getManaRate() {
        return 1;
    }


    public boolean isInstant() {
        return instant;
    }

    public boolean doesTick() {
        return doesTick;
    }

    public boolean usableInSpellcastingCircle() {
        return false;
    }

    public boolean usableInRitualAnchor() {
        return true;
    }

    public RitualInput getInput() {
        return input;
    }

    public boolean matches(List<Ingredient> items, Tier tier, RitualOriginType originType) {
        return input.matches(items)
                && tier.greaterThanEqual(this.tier)
                && originType == RitualOriginType.CIRCLE ? usableInSpellcastingCircle() : usableInRitualAnchor(); //TODO: Make this data driven
    }

    public String toString() {
        return "Ritual{" + ManaweaveAndRunesRegistries.RITUAL_REGISTRY.getKey(this) + "}";
    }

    public static enum RitualOriginType {
        CIRCLE,
        ANCHOR
    }
}
