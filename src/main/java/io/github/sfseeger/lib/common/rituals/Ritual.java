package io.github.sfseeger.lib.common.rituals;


import com.mojang.serialization.Codec;
import io.github.sfseeger.lib.common.Tier;
import io.github.sfseeger.lib.common.mana.Mana;
import io.github.sfseeger.lib.core.ManaweaveAndRunesRegistries;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public abstract class Ritual {
    public static final Codec<Ritual> CODEC =
            Codec.lazyInitialized(() -> ManaweaveAndRunesRegistries.RITUAL_REGISTRY.byNameCodec());

    final Tier tier;
    String descriptionId;
    RitualInput input;
    final int duration;

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
    public Map<Mana, Integer> getManaCost() {
        return input.getManaCost();
    }

    public List<Ingredient> getInitialItemCost() {
        return input.getInitialItemCost();
    }

    public List<Ingredient> getTickItemCost() {
        return input.getTickItemCost();
    }

    public RitualStepResult onRitualServerTick(ServerLevel level, BlockPos pos, BlockState state, int ticksPassed,
            RitualOriginType originType) {
        return RitualStepResult.SUCCESS;
    }

    public void onRitualClientTick(Level level, BlockPos pos, BlockState state, int ticksPassed,
            RitualOriginType originType) {
    }

    public RitualStepResult onRitualStart(Level level, BlockPos pos, BlockState state, RitualOriginType originType) {
        return RitualStepResult.SUCCESS;
    }

    public void onRitualEnd(Level level, BlockPos pos, BlockState state, RitualOriginType originType) {
    }

    public void onRitualInterrupt(Level level, BlockPos pos, BlockState state, RitualOriginType originType) {
    }

    public int getDuration() {
        return duration;
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

    public boolean matches(List<Ingredient> items, Tier tier, RitualOriginType originType, Level level) {
        RegistryAccess registryAccess;
        if (level.isClientSide) {
            ClientPacketListener listener = Minecraft.getInstance().getConnection();
            if (listener == null) {
                return false;
            }
            registryAccess = listener.registryAccess();
        } else {
            registryAccess = level.registryAccess();
        }
        return registryAccess.registry(ManaweaveAndRunesRegistries.RITUAL_INPUT_REGISTRY_KEY)
                .flatMap(reg -> Optional.ofNullable(reg.get(getRegistryName())))
                .map(input -> input.matches(items)
                        && tier.greaterThanEqual(this.tier)
                        && (originType == RitualOriginType.CIRCLE ? usableInSpellcastingCircle() : usableInRitualAnchor()))
                .orElse(false);
    }

    public Holder<Ritual> registryHolder() {
        return ManaweaveAndRunesRegistries.RITUAL_REGISTRY.wrapAsHolder(this);
    }

    protected ResourceLocation getRegistryName() {
        return ManaweaveAndRunesRegistries.RITUAL_REGISTRY.getKey(this);
    }

    public String toString() {
        return "Ritual{" + getRegistryName() + "}";
    }

    public enum RitualOriginType {
        CIRCLE,
        ANCHOR
    }
}
