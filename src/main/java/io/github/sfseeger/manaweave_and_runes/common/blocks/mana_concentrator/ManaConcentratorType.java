package io.github.sfseeger.manaweave_and_runes.common.blocks.mana_concentrator;

import io.github.sfseeger.lib.common.Tier;
import io.github.sfseeger.lib.core.ManaweaveAndRunesRegistries;
import io.github.sfseeger.manaweave_and_runes.core.util.MultiblockValidator;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import java.util.List;
import java.util.Optional;

public class ManaConcentratorType {
    public final ResourceLocation structureLocation;
    private final Tier tier;

    public ManaConcentratorType(ResourceLocation location, Tier tier) {
        this.structureLocation = location;
        this.tier = tier;
    }

    public MultiblockValidator getShapeValidator(ServerLevel level) {
        return level.registryAccess()
                .registry(ManaweaveAndRunesRegistries.MULTIBLOCK_VALIDATOR_REGISTRY_KEY)
                .flatMap(reg -> Optional.ofNullable(reg.get(structureLocation)))
                .orElseThrow(() -> new IllegalStateException("Validator not found for key: " + structureLocation));
    }

    public boolean isValidShape(Level level, BlockPos pos) {
        return validate(level, pos).isValid();
    }

    public MultiblockValidator.MultiBlockValidationData validate(Level level, BlockPos pos) {
        return getShapeValidator((ServerLevel) level).isValid(level, pos);
    }

    public List<BlockPos> findBlocks(Level level, Block block) {
        try {
            return getShapeValidator((ServerLevel) level).findBlocks(block);
        } catch (IllegalArgumentException e) {
            return List.of();
        }
    }

    public Tier getTier() {
        return tier;
    }

}
