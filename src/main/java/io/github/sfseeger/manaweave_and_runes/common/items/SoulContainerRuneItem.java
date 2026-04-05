package io.github.sfseeger.manaweave_and_runes.common.items;

import io.github.sfseeger.lib.common.rituals.ritual_data.IRitualDataCapable;
import io.github.sfseeger.lib.common.rituals.ritual_data.builtin.PlayerRitualData;
import io.github.sfseeger.manaweave_and_runes.common.blocks.ScryingPool;
import io.github.sfseeger.manaweave_and_runes.common.data_components.PlayerDataComponent;
import io.github.sfseeger.manaweave_and_runes.common.entity.scrying.CameraProxyEntity;
import io.github.sfseeger.manaweave_and_runes.core.payloads.CameraSetPayload;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetCameraPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

import static io.github.sfseeger.manaweave_and_runes.core.init.MRDataComponentsInit.PLAYER_DATA_COMPONENT;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class SoulContainerRuneItem extends Item implements IRitualDataCapable {
    public SoulContainerRuneItem() {
        super(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON));
    }

    public static void setPlayerComponent(ItemStack stack, Player player) {
        stack.set(PLAYER_DATA_COMPONENT, new PlayerDataComponent(player));
    }

    public static boolean isPlayerLookingAtPlayer(Player observer, Player target, double maxDistance) {
        // Get observer's position and viewing direction
        Vec3 eyePosition = observer.getEyePosition(1.0F);
        Vec3 lookVector = observer.getViewVector(1.0F);
        Vec3 rayEnd = eyePosition.add(lookVector.scale(maxDistance)); // End of the ray

        // Get entities in the ray's path
        Level level = observer.level();
        AABB searchBox = new AABB(eyePosition, rayEnd).inflate(1.0D); // Inflate for safety
        for (Entity entity : level.getEntities(observer, searchBox)) {
            if (entity instanceof Player potentialTarget && potentialTarget.equals(target)) {
                // Check if the target is within the line of sight
                AABB targetBox = potentialTarget.getBoundingBox().inflate(0.1D); // Slightly enlarge target's hitbox
                if (targetBox.clip(eyePosition, rayEnd).isPresent()) {
                    return true;
                }
            }
        }
        return false; // No playerUUIDs in the line of sight
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 1200;
    }

    @Override
    public void onUseTick(Level level, LivingEntity livingEntity, ItemStack stack, int remainingUseDuration) {
        int elapsedTicks = this.getUseDuration(stack, livingEntity) - remainingUseDuration;
        if (!level.isClientSide()) {
            if (elapsedTicks % 20 == 0) {
                PlayerDataComponent playerDataComponent = stack.get(PLAYER_DATA_COMPONENT);
                if (livingEntity instanceof Player player && playerDataComponent != null) {
                    Player playerToView = level.getPlayerByUUID(playerDataComponent.playerUUID());
                    ServerPlayer serverPlayer = (ServerPlayer) player ;
                    ServerLevel serverLevel = (ServerLevel) level;
                    if (playerToView != null && !playerToView.is(player)) {
                        if (elapsedTicks == 0) {
                            int viewDistance = Mth.clamp(serverPlayer.requestedViewDistance(), 2, serverPlayer.server.getPlayerList().getViewDistance());
                            CameraProxyEntity proxyEntity = new CameraProxyEntity(level, playerDataComponent.playerUUID());
                            proxyEntity.setChunkLoadingDistance(viewDistance);
                            serverLevel.addFreshEntity(proxyEntity);
                            PacketDistributor.sendToPlayer(serverPlayer, new CameraSetPayload(playerToView.getId()));
                        }
                    }
                }
            }
        }
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity entity, int timeCharged) {
        if (entity instanceof Player player && !level.isClientSide() && ((ServerPlayer) player).getCamera() instanceof CameraProxyEntity cameraProxyEntity) {
            cameraProxyEntity.stopViewing((ServerPlayer) player);
        }
        super.releaseUsing(stack, level, entity, timeCharged);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        if (player.isCrouching()) {
            player.getItemInHand(usedHand).remove(PLAYER_DATA_COMPONENT);
            return new InteractionResultHolder<>(InteractionResult.SUCCESS, player.getItemInHand(usedHand));
        }
        setPlayerComponent(player.getItemInHand(usedHand), player);
        return new InteractionResultHolder<>(InteractionResult.SUCCESS, player.getItemInHand(usedHand));
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity
            interactionTarget, InteractionHand usedHand
    ) {
        if (player.getMainHandItem().is(stack.getItem()) && interactionTarget instanceof Player p) {
            if (player.isCrouching() && !isPlayerLookingAtPlayer(p, player, 5.0D)) {
                ItemStack s = player.getMainHandItem();
                setPlayerComponent(s, p);
                player.displayClientMessage(
                        Component.translatable("item.manaweave_and_runes.soul_container_rune.stole_soul",
                                               p.getDisplayName()), true
                );
                return InteractionResult.SUCCESS;
            }
            p.displayClientMessage(
                    Component.translatable("item.manaweave_and_runes.soul_container_rune.stole_soul_fragment"),
                    true);
            player.displayClientMessage(
                    Component.translatable("item.manaweave_and_runes.soul_container_rune.detected"),
                    true);
            return InteractionResult.FAIL;
        }
        return InteractionResult.PASS;
    }

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        if (context.getLevel().getBlockState(context.getClickedPos()).getBlock() instanceof ScryingPool) {
            context.getPlayer().startUsingItem(context.getHand());
            return InteractionResult.SUCCESS;
        }
        return super.onItemUseFirst(stack, context);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext
            context, List<Component> tooltipComponents, TooltipFlag tooltipFlag
    ) {
        PlayerDataComponent component = stack.get(PLAYER_DATA_COMPONENT);
        if (component != null) {
            try (Level level = context.level()) {
                Player player = level.getPlayerByUUID(component.playerUUID());

                Component name =
                        Component.translatable("item.manaweave_and_runes.soul_container_rune.unknown_player");

                if (player != null) {
                    name = player.getDisplayName();
                }
                if ((player == null || name == null) && !component.lastPlayerName().isEmpty()) {
                    name = Component.literal(component.lastPlayerName());
                }

                tooltipComponents.add(Component.literal("Player: ").append(name));
            } catch (Exception e) {
                tooltipComponents.add(Component.literal("Player: Unknown"));
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public PlayerRitualData getData(ItemStack stack) {
        PlayerDataComponent component = stack.get(PLAYER_DATA_COMPONENT);
        if (component != null) {
            return new PlayerRitualData(component.playerUUID());
        }
        return null;
    }
}
