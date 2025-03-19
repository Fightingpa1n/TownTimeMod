package net.fightingpainter.mc.towntime;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

import net.fightingpainter.mc.towntime.commands.DebugCommands;


@EventBusSubscriber(modid = TownTime.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class Events {

    @SubscribeEvent
    public static void onServerStarting(ServerStartingEvent event) { //server starting
        // Do something when the server starts
        TownTime.LOGGER.info("Heey! that's cool! I'm inside onServerStarting! it's more spacious here so you can come and join me! :D");
    }

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) { //register commands
        DebugCommands.register(event.getDispatcher());
    }
    
}
