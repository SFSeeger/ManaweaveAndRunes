package io.github.sfseeger.lib.common.entities;

import io.github.sfseeger.lib.common.entities.projectiles.SpellProjectileEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public class ManaWeaveAndRuneEntityTypes {
    public static final EntityType<SpellProjectileEntity> SPELL_PROJECTILE =
            EntityType.Builder.<SpellProjectileEntity>of(
                            SpellProjectileEntity::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F)
                    .updateInterval(20)
                    .clientTrackingRange(4)
                    .build("spell_projectile");

}
