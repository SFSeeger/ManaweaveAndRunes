package io.github.sfseeger.manaweave_and_runes;

import com.mojang.logging.LogUtils;
import io.github.sfseeger.lib.common.mana.network.ManaNetworkHandler;
import io.github.sfseeger.manaweave_and_runes.client.event.KeyManager;
import io.github.sfseeger.manaweave_and_runes.core.init.*;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import org.slf4j.Logger;

@Mod(ManaweaveAndRunes.MODID)
public class ManaweaveAndRunes {
    public static final String MODID = "manaweave_and_runes";
    public static final Logger LOGGER = LogUtils.getLogger();

    public ManaweaveAndRunes(IEventBus modEventBus, ModContainer modContainer) {
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        ManaInit.MANA_TYPES.register(modEventBus);
        MRDataComponentsInit.DATA_COMPONENTS.register(modEventBus);

        MRBlockInit.BLOCKS.register(modEventBus);
        MRBlockEntityInit.BLOCK_ENTITY_TYPES.register(modEventBus);
        MRItemInit.ITEMS.register(modEventBus);

        ManaweaveAndRunesItemGroupInit.CREATIVE_MODE_TABS.register(modEventBus);
        MRParticleTypeInit.PARTICLE_TYPES.register(modEventBus);

        EntityTypeInit.ENTITY_TYPES.register(modEventBus);

        MRRecipeInit.RECIPE_TYPES.register(modEventBus);
        MRRecipeInit.RECIPE_SERIALIZERS.register(modEventBus);
        MRIngredientInit.INGREDIENT_TYPES.register(modEventBus);

        MRMenuInit.MENUS.register(modEventBus);

        RitualDataTypesInit.register(modEventBus);
        RitualInit.RITUALS.register(modEventBus);

        SpellNodeInit.SPELL_NODES.register(modEventBus);

        MRLootModifierInit.GLOBAL_LOOT_MODIFIER_SERIALIZERS.register(modEventBus);
        MRStructureTypes.STRUCTURES.register(modEventBus);

        NeoForge.EVENT_BUS.register(ManaNetworkHandler.class);

        if (FMLEnvironment.dist == Dist.CLIENT) {
            NeoForge.EVENT_BUS.register(KeyManager.class);
            modEventBus.addListener(KeyManager::registerKeyMapping);
        }


        NeoForge.EVENT_BUS.register(this);


        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        // Some common setup code
        LOGGER.info("HELLO FROM COMMON SETUP");

        if (Config.logDirtBlock)
            LOGGER.info("DIRT BLOCK >> {}", BuiltInRegistries.BLOCK.getKey(Blocks.DIRT));
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("HELLO from server starting");
    }

    @EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            // Some client setup code
            LOGGER.info("HELLO FROM CLIENT SETUP");
            LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
        }
    }
}
