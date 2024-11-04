package net.secretplaysmc.secrets_magic;

import com.mojang.logging.LogUtils;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.secretplaysmc.secrets_magic.block.ModBlocks;
import net.secretplaysmc.secrets_magic.item.ModCreativeModeTab;
import net.secretplaysmc.secrets_magic.item.ModItems;
import net.secretplaysmc.secrets_magic.loot.ModLootModifiers;
import net.secretplaysmc.secrets_magic.mana.AttachCapabilitiesEventHandler;
import net.secretplaysmc.secrets_magic.mana.ManaHUDOverlay;
import net.secretplaysmc.secrets_magic.mana.PlayerMana;
import net.secretplaysmc.secrets_magic.network.ModNetworking;
import net.secretplaysmc.secrets_magic.potion.ModEffects;
import net.secretplaysmc.secrets_magic.skillTree.PlayerSkills;
import net.secretplaysmc.secrets_magic.spells.ModSpells;
import net.secretplaysmc.secrets_magic.spells.PlayerSpells;
import net.secretplaysmc.secrets_magic.util.ModCommands;
import net.secretplaysmc.secrets_magic.util.ModContainers;
import net.secretplaysmc.secrets_magic.util.ModKeybindings;
import net.secretplaysmc.secrets_magic.util.gui.CustomSpellScreen;
import net.secretplaysmc.secrets_magic.util.worldgen.dimension.ModDimensions;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(SecretsMagic.MOD_ID)
@Mod.EventBusSubscriber(modid = SecretsMagic.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SecretsMagic {
    // Define mod id in a common place for everything to reference
    public static final String MOD_ID = "secrets_magic";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    public SecretsMagic() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModCreativeModeTab.register(modEventBus);
        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModLootModifiers.register(modEventBus);
        ModEffects.register(modEventBus);
        ModContainers.register(modEventBus);

        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::clientSetup);

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        MinecraftForge.EVENT_BUS.register(AttachCapabilitiesEventHandler.class);
        ModNetworking.registerPackets();
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        MinecraftForge.EVENT_BUS.register(ManaHUDOverlay.class);
        MenuScreens.register(ModContainers.CUSTOM_SPELL.get(), CustomSpellScreen::new);
    }


    @SubscribeEvent
    public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
        event.register(PlayerMana.class);
        event.register(PlayerSpells.class);
        event.register(PlayerSkills.class);
    }

    @SubscribeEvent
    public void onRegisterCommands(RegisterCommandsEvent event) {
        ModCommands.register(event.getDispatcher());
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting (ServerStartingEvent event) {
        ServerLevel world = event.getServer().overworld();
        ModSpells.loadCustomSpells(world);
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = SecretsMagic.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            ModKeybindings.register();
        }
    }
}
