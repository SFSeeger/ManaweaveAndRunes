package io.github.sfseeger.lib.common.datamaps;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.datamaps.DataMapType;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public record BlockHealDataMap(Block block, float strength, float chance) {
    public static final Codec<BlockHealDataMap> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BuiltInRegistries.BLOCK.byNameCodec().fieldOf("block").forGetter(BlockHealDataMap::block),
            Codec.FLOAT.optionalFieldOf("strength", 1f).forGetter(BlockHealDataMap::strength),
            Codec.floatRange(0, 1).optionalFieldOf("chance", 1f).forGetter(BlockHealDataMap::chance)
    ).apply(instance, BlockHealDataMap::new));

    public static final DataMapType<Block, BlockHealDataMap> BLOCK_BLOCK_HEAL_DATA = DataMapType.builder(
            ResourceLocation.fromNamespaceAndPath("manaweave_and_runes", "block_heal_data"),
            Registries.BLOCK,
            BlockHealDataMap.CODEC
    ).build();

    public static Optional<Block> getConvertedBlock(Block target, RandomSource random, @Nullable Float strength) {
        Holder<Block> blockHolder = target.builtInRegistryHolder();
        BlockHealDataMap data = blockHolder.getData(BLOCK_BLOCK_HEAL_DATA);

        strength = strength == null ? 1 : strength;
        if (data == null || (random.nextInt(0,
                                            100) > 100 * data.chance() && !(strength == 2 * data.strength())) || strength < data.strength()) {
            return Optional.empty();
        }
        return Optional.of(data.block());
    }
}
