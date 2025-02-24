package io.github.sfseeger.manaweave_and_runes.common.items;

import io.github.sfseeger.lib.common.rituals.ritual_data.IRitualDataCapable;
import io.github.sfseeger.lib.common.rituals.ritual_data.builtin.PlayerRitualData;
import io.github.sfseeger.manaweave_and_runes.common.data_components.PlayerDataComponent;
import net.minecraft.network.chat.Component;
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
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static io.github.sfseeger.manaweave_and_runes.core.init.MRDataComponentsInit.PLAYER_DATA_COMPONENT;

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
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        if (player.isCrouching()) {
            player.getItemInHand(usedHand).remove(PLAYER_DATA_COMPONENT);
            return new InteractionResultHolder<>(InteractionResult.SUCCESS, player.getItemInHand(usedHand));
        }
        setPlayerComponent(player.getItemInHand(usedHand), player);
        return new InteractionResultHolder<>(InteractionResult.SUCCESS, player.getItemInHand(usedHand));
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity interactionTarget,
            InteractionHand usedHand) {
        if (player.getMainHandItem() == stack && interactionTarget instanceof Player p) {
            if (player.isCrouching() && !isPlayerLookingAtPlayer(p, player, 5.0D)) {
                ItemStack s = player.getMainHandItem();
                setPlayerComponent(s, p);
                return InteractionResult.SUCCESS;
            }
            p.displayClientMessage(
                    Component.translatable("item.manaweave_and_runes.soul_container_rune.stole_soul_fragment"), true);
            player.displayClientMessage(Component.translatable("item.manaweave_and_runes.soul_container_rune.detected"),
                                        true);
            return InteractionResult.FAIL;
        }
        return InteractionResult.PASS;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents,
            TooltipFlag tooltipFlag) {
        PlayerDataComponent component = stack.get(PLAYER_DATA_COMPONENT);
        if (component != null) {
            // TODO: Save name in component so the name stays, even if people log off
            try (Level level = context.level()) {
                Player player = level.getPlayerByUUID(UUID.fromString(component.playerUUID()));
                tooltipComponents.add(Component.literal("Player: ")
                                              .append(player != null ? Objects.requireNonNull(
                                                      player.getDisplayName()) : Component.literal(
                                                      "Unknown"))); // TODO: Add translation
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
