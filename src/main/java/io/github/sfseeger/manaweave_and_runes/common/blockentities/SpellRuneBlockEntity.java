package io.github.sfseeger.manaweave_and_runes.common.blockentities;

import io.github.sfseeger.lib.common.spells.AbstractSpellCastingContext;
import io.github.sfseeger.lib.common.spells.SpellCastingResult;
import io.github.sfseeger.lib.common.spells.SpellResolver;
import io.github.sfseeger.lib.common.spells.casting_context.BlockCasterCastingContext;
import io.github.sfseeger.manaweave_and_runes.core.init.MRBlockEntityInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Objects;

public class SpellRuneBlockEntity extends BlockEntity {
    SpellResolver resolver;
    AbstractSpellCastingContext context =
            new BlockCasterCastingContext(worldPosition, new Vec3(0, 1, 0), Direction.UP, null);


    public SpellRuneBlockEntity(BlockPos pos, BlockState blockState) {
        super(MRBlockEntityInit.SPELL_RUNE_BLOCK_ENTITY.get(), pos, blockState);
    }


    public void onCast(LivingEntity entity) {
        if (this.resolver == null || this.context == null) return;

        SpellCastingResult result = resolver.resolve(new EntityHitResult(entity), context);
        if (result == SpellCastingResult.SUCCESS) {
            entity.level().destroyBlock(this.worldPosition, false);
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        if (this.resolver != null) tag.put("resolver", resolver.serializeNBT(registries));
        if (this.context != null) tag.put("context", context.serializeNBT(registries));
        super.saveAdditional(tag, registries);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        if (tag.contains("resolver")) {
            this.resolver = new SpellResolver(null);
            this.resolver.deserializeNBT(registries, Objects.requireNonNull(tag.get("resolver")));
        }
        if (tag.contains("context")) {
            this.context.deserializeNBT(registries, tag.getCompound("context"));
        }
        super.loadAdditional(tag, registries);
    }

    @Override
    public void onLoad() {
        if (this.level != null) {
            this.context.loadLevelDependentData(level);
        }
    }

    public void setResolver(SpellResolver resolver) {
        this.resolver = resolver;
    }

    public void setContext(AbstractSpellCastingContext context) {
        this.context = context;
    }

    public static void tick(Level level, BlockPos pos, BlockState state, SpellRuneBlockEntity spellRuneBlockEntity) {
        if (level.isClientSide()) return;

        AABB area = new AABB(pos).inflate(spellRuneBlockEntity.context.getFloatContextData("width", 0));

        List<Entity> entityList = level.getEntities(null, area);
        entityList.stream()
                .filter(entity -> entity instanceof LivingEntity)
                .map(entity -> (LivingEntity) entity)
                .filter(entity -> spellRuneBlockEntity.context.getCaster() == null || !entity.equals(spellRuneBlockEntity.context.getCaster()))
                .min((e, e2) -> (int) e2.position().distanceTo(e.position()))
                .ifPresent(spellRuneBlockEntity::onCast);
    }
}
