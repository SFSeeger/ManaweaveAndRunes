package io.github.sfseeger.manaweave_and_runes.core.util;

import com.google.gson.JsonParser;
import com.mojang.datafixers.util.Either;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.slf4j.Logger;

import java.util.*;
import java.util.stream.Collectors;

/**
 * A class that represents a multiblock structure and can validate if a structure in the world.
 * A structure is defined by a 3D array of characters, where each character represents a block in the structure. The mapping goes from top to bottom, west to east, and north to south.
 * The symmetrical tag is not required, but will increase performance if the structure is symmetrical.
 * The following characters are reserved:
 * - ' ': Represents air blocks
 * - '0': Represents the origin block of the structure
 * - '_': Represents any Block or air
 * Example:
 * <pre>
 * {
 *   "blockMapping": {
 *     "A": "minecraft:amethyst_block",
 *   },
 *   "structure": [
 *     [" A ","A0A"," A ",],
 *     ["_A_","AAA","_A_",],
 *   ],
 *   "symmetric": true
 * }
 * </pre>
 */
public class MultiblockValidator {
    public static final Codec<MultiblockValidator> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.unboundedMap(
                    Codec.sizeLimitedString(1),
                    Codec.either(BuiltInRegistries.BLOCK.byNameCodec(), TagKey.hashedCodec(Registries.BLOCK))
            ).fieldOf("blockMapping").forGetter(MultiblockValidator::getBlockMapping),
            Codec.list(
                    Codec.list(
                            Codec.STRING
                    )
            ).fieldOf("structure").forGetter(MultiblockValidator::getMultiblockStructureList),
            Codec.BOOL.optionalFieldOf("symmetric", false).forGetter(MultiblockValidator::isSymmetric)
    ).apply(instance, MultiblockValidator::fromCodec));
    private static final Logger LOGGER = LogUtils.getLogger();


    public final Map<Character, Either<Block, TagKey<Block>>> blockMapping = new HashMap<>();
    private final char[][][] multiblockStructure;
    private final Vec3i structureOrigin;
    private final boolean symmetric;

    public MultiblockValidator(char[][][] structure, Vec3i origin, boolean symmetric) {
        this.blockMapping.put(' ', Either.left(Blocks.AIR));
        this.multiblockStructure = structure;
        this.structureOrigin = origin;
        this.symmetric = symmetric;
    }


    public MultiblockValidator(char[][][] structure) {
        this(structure, new Vec3i(0, 0, 0), false);
    }

    private static MultiblockValidator fromCodec(Map<String, Either<Block, TagKey<Block>>> blockMap,
            List<List<String>> structure,
            boolean symmetric) {
        Map<Character, Either<Block, TagKey<Block>>> charBlockMap = blockMap.entrySet().stream()
                .collect(Collectors.toMap(entry -> entry.getKey().charAt(0), Map.Entry::getValue));
        int height = structure.size();
        int depth = structure.getFirst().size();
        int width = structure.getFirst().getFirst().length();

        char[][][] structureArray = new char[height][depth][width];
        Vec3i origin = null;

        for (int y = 0; y < height; y++) {
            List<String> layer = structure.get(y);
            for (int z = 0; z < depth; z++) {
                char[] row = layer.get(z).toCharArray();
                for (int x = 0; x < width; x++) {
                    char symbol = row[x];
                    structureArray[y][z][x] = symbol;
                    if (symbol == '0') {
                        if (origin != null) {
                            throw new IllegalArgumentException("Multiple origin blocks found in multiblock structure");
                        }
                        origin = new Vec3i(x, y, z);
                    }
                }
            }
        }
        origin = origin == null ? new Vec3i(0, 0, 0) : origin;
        MultiblockValidator validator =
                new MultiblockValidator(structureArray, origin, symmetric);
        validator.blockMapping.putAll(charBlockMap);
        return validator;
    }

    public static MultiblockValidator fromString(String jsonString) {
        return CODEC.parse(JsonOps.INSTANCE, JsonParser.parseString(jsonString))
                .result()
                .orElseThrow(() -> new IllegalArgumentException("Failed to parse multiblock validator from json"));
    }

    public Map<String, Either<Block, TagKey<Block>>> getBlockMapping() {
        return blockMapping.entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> entry.getKey().toString(),
                        Map.Entry::getValue
                ));
    }

    public List<List<String>> getMultiblockStructureList() {
        return Arrays.stream(multiblockStructure)
                .map(layer -> Arrays.stream(layer)
                        .map(String::new)
                        .collect(Collectors.toList()))
                .collect(Collectors.toList());
    }

    private Boolean isSymmetric() {
        return symmetric;
    }

    public MultiBlockValidationData isValid(Level level, BlockPos origin) {
        for (int y = 0; y < multiblockStructure.length; y++) {
            int yOffset = structureOrigin.getY() - y;
            for (int z = 0; z < multiblockStructure[y].length; z++) {
                int zOffset = z - structureOrigin.getZ();
                for (int x = 0; x < multiblockStructure[y][z].length; x++) {
                    int xOffset = x - structureOrigin.getX();

                    char symbol = multiblockStructure[y][z][x];

                    if (symbol == '_' || symbol == '0') continue; // Ignore not required blocks and origin block

                    BlockPos pos = origin.offset(xOffset, yOffset, zOffset);
                    BlockState state = level.getBlockState(pos);

                    Either<Block, TagKey<Block>> expected = blockMapping.get(symbol);
                    if (expected == null) continue;

                    if (!matches(state, expected)) {
                        return new MultiBlockValidationData(false, pos, state.getBlock(), expected);
                    }
                }
            }
        }
        return new MultiBlockValidationData(true, null, null, null);
    }

    private boolean matches(BlockState state, Either<Block, TagKey<Block>> expected) {
        return expected.map(state::is, state::is);
    }

    public List<BlockPos> findBlocks(Block block) throws IllegalArgumentException {
        return findBlocks(Either.left(block));
    }

    public List<BlockPos> findBlocks(TagKey<Block> block) throws IllegalArgumentException {
        return findBlocks(Either.right(block));
    }

    public List<BlockPos> findBlocks(Either<Block, TagKey<Block>> target) throws IllegalArgumentException {
        // TODO: implement not symmetrical multiblocks
        char symbol = blockMapping.entrySet().stream()
                .filter(entry -> entry.getValue().equals(target))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Block not found in block mapping"));
        List<BlockPos> positions = new ArrayList<>();
        for (int y = 0; y < multiblockStructure.length; y++) {
            int yOffset = structureOrigin.getY() - y;
            for (int z = 0; z < multiblockStructure[y].length; z++) {
                int zOffset = z - structureOrigin.getZ();
                for (int x = 0; x < multiblockStructure[y][z].length; x++) {
                    int xOffset = x - structureOrigin.getX();
                    if (multiblockStructure[y][z][x] == symbol) {
                        positions.add(new BlockPos(xOffset, yOffset, zOffset));
                    }
                }
            }
        }
        return positions;
    }

    public record MultiBlockValidationData(boolean isValid, BlockPos errorLocation, Block currentBlock,
                                           Either<Block, TagKey<Block>> expected) {
    }
}