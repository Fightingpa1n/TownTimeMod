package net.fightingpainter.mc.towntime.network;

import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;

import net.fightingpainter.mc.towntime.food.CustomTemperatureData;
import net.fightingpainter.mc.towntime.network.packets.PlayerTemperatureDataSync;


public class ModNetworking {
    public static void registerPayloads(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1.0").optional();

        registrar.playToClient(
            PlayerTemperatureDataSync.TYPE,
            PlayerTemperatureDataSync.STREAM_CODEC,
            ModNetworking::handlePlayerTemperatureDataSync
        );
    }

    private static void handlePlayerTemperatureDataSync(PlayerTemperatureDataSync payload, IPayloadContext context) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            CustomTemperatureData incomingData = payload.getTemperatureData(); //get the temperature data from the packet
            CustomTemperatureData playerData = CustomTemperatureData.of(player); //get the players temperature data

            playerData.setTemperatureValue(incomingData.getTemperatureValue()); //set the players temperature data to the incoming data
        }
    }
}

