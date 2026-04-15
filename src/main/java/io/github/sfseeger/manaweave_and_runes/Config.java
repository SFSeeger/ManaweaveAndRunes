package io.github.sfseeger.manaweave_and_runes;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Neo's config APIs
@EventBusSubscriber(modid = ManaweaveAndRunes.MODID, bus = EventBusSubscriber.Bus.MOD)
public class Config
{
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    private static final ModConfigSpec.BooleanValue ALLOW_HARM_SPELL_GRIEFING = BUILDER
            .comment("Whether to allow harm spells to damage blocks. This can be set to false to prevent griefing on servers.")
            .define("allowHarmSpellGriefing", true);

    private static final ModConfigSpec.BooleanValue ALLOW_HEAL_SPELL_GRIEFING = BUILDER
            .comment("Whether to allow heal spells to heal blocks. This can be set to false to prevent griefing on servers.")
            .define("allowHealSpellGriefing", true);

    private static final ModConfigSpec.IntValue MINIMUM_SPELL_COOLDOWN = BUILDER
            .comment("Minimum cooldown in ticks between spell casts.")
            .defineInRange("minimumSpellCooldown", 2, 0, Integer.MAX_VALUE);


    static final ModConfigSpec SPEC = BUILDER.build();

    public static boolean allowHarmSpellGriefing;
    public static boolean allowHealSpellGriefing;
    public static int minimumSpellCooldown;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event)
    {
        allowHarmSpellGriefing = ALLOW_HARM_SPELL_GRIEFING.get();
        allowHealSpellGriefing = ALLOW_HEAL_SPELL_GRIEFING.get();
        minimumSpellCooldown = MINIMUM_SPELL_COOLDOWN.get();
    }
}
