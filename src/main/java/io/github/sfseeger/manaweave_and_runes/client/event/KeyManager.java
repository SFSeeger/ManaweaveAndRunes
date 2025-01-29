package io.github.sfseeger.manaweave_and_runes.client.event;

import com.mojang.blaze3d.platform.InputConstants;
import io.github.sfseeger.lib.common.spells.ISpellCaster;
import io.github.sfseeger.lib.common.spells.Spell;
import io.github.sfseeger.manaweave_and_runes.ManaweaveAndRunes;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.settings.KeyConflictContext;
import net.neoforged.neoforge.common.util.Lazy;
import org.lwjgl.glfw.GLFW;

public class KeyManager {
    public static final Lazy<KeyMapping> TOGGLE_SPELL = Lazy.of(() -> new KeyMapping(
            "key." + ManaweaveAndRunes.MODID + ".toggle_spell",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_U,
            "key.categories.misc"
    ));

    public static void registerKeyMapping(RegisterKeyMappingsEvent event){
        event.register(TOGGLE_SPELL.get());
    }

    @SubscribeEvent
    public static void clientTick(ClientTickEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        if (TOGGLE_SPELL.get().consumeClick()) {
            toggleSpell(mc, player);
        }
    }

    private static void toggleSpell(Minecraft mc, Player player) {
        ItemStack stack = player.getMainHandItem();
        if (stack.getItem() instanceof ISpellCaster casterItem){
            casterItem.switchSpell(stack, casterItem.getCurrentSpellIndex(stack) + 1);
            Spell spell = casterItem.getCurrrntSpell(stack);
            String spellName = spell != null ? spell.getName() : "No Spell";
            player.displayClientMessage(Component.literal(
                    "Switched to Spell " + spellName + " at " + casterItem.getCurrentSpellIndex(stack)), true);
        }
    }
}
