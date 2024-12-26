package io.github.sfseeger.manaweave_and_runes.common.blocks.mana_concentrator;

import io.github.sfseeger.manaweave_and_runes.core.init.DatapackRegistries;
import io.github.sfseeger.manaweave_and_runes.core.util.MultiblockValidator;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;

import java.util.Optional;

public class ManaConcentratorType {
    public final ResourceLocation structureLocation;

    public ManaConcentratorType(ResourceLocation location) {
        this.structureLocation = location;
    }

    public MultiblockValidator getShapeValidator(ServerLevel level) {
        return level.registryAccess()
                .registry(DatapackRegistries.MULTIBLOCK_VALIDATOR_REGISTRY_KEY)
                .flatMap(reg -> Optional.ofNullable(reg.get(structureLocation)))
                .orElseThrow(() -> new IllegalStateException("Validator not found for key: " + structureLocation));
    }

    public boolean isValidShape(Level level, BlockPos pos) {
        return validate(level, pos).isValid();
    }

    public MultiblockValidator.MultiBlockValidationData validate(Level level, BlockPos pos) {
        return getShapeValidator((ServerLevel) level).isValid(level, pos);
    }
}
