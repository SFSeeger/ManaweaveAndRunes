package io.github.sfseeger.manaweave_and_runes.core.init;

import io.github.sfseeger.lib.common.entities.ManaWeaveAndRuneEntityTypes;
import io.github.sfseeger.lib.common.entities.projectiles.SpellProjectileEntity;
import io.github.sfseeger.manaweave_and_runes.ManaweaveAndRunes;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class EntityTypeInit {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(Registries.ENTITY_TYPE, ManaweaveAndRunes.MODID);

    public static final Supplier<EntityType<SpellProjectileEntity>> SPELL_PROJECTILE =
            ENTITY_TYPES.register("spell_projectile", () -> ManaWeaveAndRuneEntityTypes.SPELL_PROJECTILE);
}
