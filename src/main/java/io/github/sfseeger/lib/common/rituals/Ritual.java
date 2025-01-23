package io.github.sfseeger.lib.common.rituals;


import com.mojang.serialization.Codec;
import io.github.sfseeger.lib.common.Tier;
import io.github.sfseeger.lib.common.mana.Mana;
import io.github.sfseeger.lib.common.rituals.ritual_data.RitualContext;
import io.github.sfseeger.lib.core.ManaweaveAndRunesRegistries;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.core.BlockPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public abstract class Ritual {
    public static final Codec<Ritual> CODEC =
            Codec.lazyInitialized(ManaweaveAndRunesRegistries.RITUAL_REGISTRY::byNameCodec);

    final Tier tier;
    final int duration;
    String descriptionId;
    RitualInput input;

    public Ritual(Tier tier, int duration) {
        this.tier = tier;
        this.duration = duration;
    }

    public String getDescriptionId() {
        if (this.descriptionId == null) {
            this.descriptionId =
                    Util.makeDescriptionId("ritual", getRegistryName());
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

    /**
     * Returns the mana cost of the ritual
     * If the ritual is not instant, the mana cost will be the cost per tick
     */
    public Map<Mana, Integer> getManaCost(Level level) {
        RitualInput input = getInput(level).orElse(null);
        return input != null ? input.getManaCost() : Map.of();
    }

    public List<Ingredient> getInitialItemCost(Level level) {
        RitualInput input = getInput(level).orElse(null);
        return input != null ? input.getInitialItemCost() : List.of();
    }

    public List<Ingredient> getTickItemCost(Level level) {
        RitualInput input = getInput(level).orElse(null);
        return input != null ? input.getTickItemCost() : List.of();
    }

    public abstract RitualStepResult onRitualServerTick(ServerLevel level, BlockPos pos, BlockState state,
            int ticksPassed, RitualContext context,
            RitualOriginType originType);

    public void onRitualClientTick(Level level, BlockPos pos, BlockState state, int ticksPassed, RitualContext context,
            RitualOriginType originType) {
    }

    public RitualStepResult onRitualStart(Level level, BlockPos pos, BlockState state, RitualContext context,
            RitualOriginType originType) {
        return RitualStepResult.SUCCESS;
    }

    public abstract void onRitualEnd(Level level, BlockPos pos, BlockState state, RitualContext context,
            RitualOriginType originType);

    public abstract void onRitualInterrupt(Level level, BlockPos pos, BlockState state, RitualContext context,
            RitualOriginType originType);

    public int getDuration() {
        return duration;
    }

    /**
     * Returns the rate in ticks at which items are consumed
     */
    public int getItemRate(Level level) {
        RitualInput input = getInput(level).orElse(null);
        return input != null ? input.getItemRate() : 1;
    }

    /**
     * Returns the rate in ticks at which mana is consumed
     */
    public int getManaRate(Level level) {
        RitualInput input = getInput(level).orElse(null);
        return input != null ? input.getManaRate() : 1;
    }


    public boolean usableInSpellcastingCircle() {
        return false;
    }

    public boolean usableInRitualAnchor() {
        return true;
    }

    public Optional<RitualInput> getInput(Level level) {
        RegistryAccess registryAccess = getRegistryAccess(level);
        if (registryAccess == null) {
            return Optional.empty();
        }
        return registryAccess.registry(ManaweaveAndRunesRegistries.RITUAL_INPUT_REGISTRY_KEY)
                .flatMap(reg -> Optional.ofNullable(reg.get(getRegistryName())));
    }

    private RegistryAccess getRegistryAccess(Level level) {
        if (level.isClientSide) {
            ClientPacketListener listener = Minecraft.getInstance().getConnection();
            if (listener == null) {
                return null;
            }
            return listener.registryAccess();
        } else {
            return level.registryAccess();
        }
    }

    public boolean matches(List<ItemStack> items, Tier tier, RitualOriginType originType, Level level) {
        RegistryAccess registryAccess = getRegistryAccess(level);
        if (registryAccess == null) {
            return false;
        }
        boolean res = registryAccess.registry(ManaweaveAndRunesRegistries.RITUAL_INPUT_REGISTRY_KEY)
                .flatMap(reg -> Optional.ofNullable(reg.get(getRegistryName())))
                .map(input -> input.matches(items)
                        && tier.greaterThanEqual(this.tier)
                        && (originType == RitualOriginType.CIRCLE ? usableInSpellcastingCircle() : usableInRitualAnchor()))
                .orElse(false);
        return res;
    }

    public ResourceLocation getRegistryName() {
        return ManaweaveAndRunesRegistries.RITUAL_REGISTRY.getKey(this);
    }

    public String toString() {
        return "Ritual{" + getRegistryName() + "}";
    }

    public enum RitualOriginType {
        CIRCLE,
        ANCHOR
    }

    public <T> Codec<T> getExtraDataCodec() {
        return null;
    }

    ;
}
