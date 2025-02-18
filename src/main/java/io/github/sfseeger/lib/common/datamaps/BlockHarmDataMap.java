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

public record BlockHarmDataMap(Block block, float strength, float chance) {
    public static final Codec<BlockHarmDataMap> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BuiltInRegistries.BLOCK.byNameCodec().fieldOf("block").forGetter(BlockHarmDataMap::block),
            Codec.FLOAT.optionalFieldOf("strength", 1f).forGetter(BlockHarmDataMap::strength),
            Codec.floatRange(0, 1).optionalFieldOf("chance", 1f).forGetter(BlockHarmDataMap::chance)
    ).apply(instance, BlockHarmDataMap::new));

    public static final DataMapType<Block, BlockHarmDataMap> BLOCK_BLOCK_HARM_DATA = DataMapType.builder(
            ResourceLocation.fromNamespaceAndPath("manaweave_and_runes", "block_harm_data"),
            Registries.BLOCK,
            BlockHarmDataMap.CODEC
    ).build();

    public static Optional<Block> getConvertedBlock(Block target, RandomSource random, @Nullable Float strength) {
        Holder<Block> blockHolder = target.builtInRegistryHolder();
        BlockHarmDataMap data = blockHolder.getData(BLOCK_BLOCK_HARM_DATA);

        strength = strength == null ? 1 : strength;
        if (data == null || (random.nextInt(0,
                                            100) > 100 * data.chance() && !(strength == 2 * data.strength())) || strength < data.strength()) {
            return Optional.empty();
        }
        return Optional.of(data.block());
    }
}
