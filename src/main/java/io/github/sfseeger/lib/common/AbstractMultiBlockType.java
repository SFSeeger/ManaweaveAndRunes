package io.github.sfseeger.lib.common;

import io.github.sfseeger.lib.core.ManaweaveAndRunesRegistries;
import io.github.sfseeger.manaweave_and_runes.core.util.MultiblockValidator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.core.BlockPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import java.util.List;
import java.util.Optional;

public abstract class AbstractMultiBlockType {
    public final ResourceLocation structureLocation;
    private final Tier tier;

    public AbstractMultiBlockType(Tier tier, ResourceLocation location) {
        this.tier = tier;
        this.structureLocation = location;
    }

    public Tier getTier() {
        return tier;
    }

    public MultiblockValidator getShapeValidator(Level level) {
        RegistryAccess registryAccess;
        if (level.isClientSide) {
            ClientPacketListener listener = Minecraft.getInstance().getConnection();
            if (listener == null) {
                return null;
            }
            registryAccess = listener.registryAccess();
        } else {
            registryAccess = level.registryAccess();
        }
        return registryAccess
                .registry(ManaweaveAndRunesRegistries.MULTIBLOCK_VALIDATOR_REGISTRY_KEY)
                .flatMap(reg -> Optional.ofNullable(reg.get(structureLocation)))
                .orElseThrow(() -> new IllegalStateException("Validator not found for key: " + structureLocation));
    }

    public boolean isValidShape(Level level, BlockPos pos) {
        return validate(level, pos).isValid();
    }

    public MultiblockValidator.MultiBlockValidationData validate(Level level, BlockPos pos) {
        return getShapeValidator(level).isValid(level, pos);
    }

    public List<BlockPos> findBlocks(Level level, Block block) {
        try {
            return getShapeValidator(level).findBlocks(block);
        } catch (IllegalArgumentException e) {
            return List.of();
        }
    }
}
