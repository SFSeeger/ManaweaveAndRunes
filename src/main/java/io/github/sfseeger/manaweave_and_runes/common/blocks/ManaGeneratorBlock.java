package io.github.sfseeger.manaweave_and_runes.common.blocks;

import io.github.sfseeger.lib.common.mana.Mana;
import io.github.sfseeger.lib.common.mana.ManaDataComponent;
import io.github.sfseeger.lib.common.mana.capability.IManaHandler;
import io.github.sfseeger.lib.common.mana.capability.ManaweaveAndRunesCapabilities;
import io.github.sfseeger.manaweave_and_runes.common.blockentities.ManaGeneratorBlockEntity;
import io.github.sfseeger.manaweave_and_runes.core.init.ManaweaveAndRunesDataComponentsInit;
import io.github.sfseeger.manaweave_and_runes.core.util.InventoryUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class ManaGeneratorBlock extends Block implements EntityBlock {
    public ManaGeneratorBlock() {
        super(BlockBehaviour.Properties.of());
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new ManaGeneratorBlockEntity(blockPos, blockState);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos,
            Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (!level.isClientSide) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            ManaDataComponent manaDataComponent =
                    stack.get(ManaweaveAndRunesDataComponentsInit.MANA_DATA_COMPONENT.value());
            if (blockEntity instanceof ManaGeneratorBlockEntity manaGeneratorBlockEntity && manaDataComponent != null) {
                IManaHandler manaHandler = manaGeneratorBlockEntity.getManaHandler(null);
                int totalReceived = 0;

                for (Holder<Mana> mana : manaDataComponent.getManaTypes()) {
                    boolean wasReceived = false;

                    int manaAmount = manaDataComponent.getManaAmount(mana.value());

                    ReceiverBlockResult result = getClosestReceiver(level, pos, mana.value(), manaAmount);
                    if (result != null) {
                        int received = result.handler().receiveMana(manaAmount, mana.value(), false);
                        if (received > 0) {
                            wasReceived = true;
                            level.sendBlockUpdated(pos.relative(result.direction()), state, state, Block.UPDATE_ALL);
                        } else {
                            received = manaHandler.receiveMana(manaDataComponent.getManaAmount(mana.value()),
                                                               mana.value(),
                                                               false);
                            if (received > 0) {
                                wasReceived = true;
                            }
                        }
                    } else {
                        int received = manaHandler.receiveMana(manaDataComponent.getManaAmount(mana.value()),
                                                               mana.value(),
                                                               false);
                        if (received > 0) {
                            wasReceived = true;
                        }
                    }
                    if (wasReceived) {
                        totalReceived += manaAmount;
                        InventoryUtil.shrinkStackIfSurvival(player, stack, 1);
                    }
                }
                return totalReceived > 0 ? ItemInteractionResult.SUCCESS : ItemInteractionResult.FAIL;
            }
        }
        return ItemInteractionResult.FAIL;
    }

    private ReceiverBlockResult getClosestReceiver(Level level, BlockPos pos, Mana mana, int amount) {
        for (Direction direction : Direction.values()) {
            IManaHandler handler =
                    level.getCapability(ManaweaveAndRunesCapabilities.MANA_HANDLER_BLOCK, pos.relative(direction),
                                        direction.getOpposite());
            if (handler != null && handler.receiveMana(amount, mana, true) > 0) {
                return new ReceiverBlockResult(handler, direction);
            }
        }
        return null;
    }

    public record ReceiverBlockResult(IManaHandler handler, Direction direction) {
    }
}
