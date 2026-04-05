package io.github.sfseeger.manaweave_and_runes.core.mixin;


import net.minecraft.server.level.ChunkMap;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = ChunkMap.class, priority = 1100)
public abstract class ChunkMapMixin {

}
