package net.fightingpainter.mc.towntime.network.packets;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.codec.ByteBufCodecs;

import net.fightingpainter.mc.towntime.TownTime;
import net.fightingpainter.mc.towntime.food.CustomTemperatureData;

public record PlayerTemperatureDataSync(float temperatureValue) implements CustomPacketPayload {
    public static final Type<PlayerTemperatureDataSync> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(TownTime.MOD_ID, "player_temperature_data_sync"));

    @Override
    public Type<PlayerTemperatureDataSync> type() {
        return TYPE;
    }
    
    /** Get the temperature value from the packet. */
    public CustomTemperatureData getTemperatureData() {
        return new CustomTemperatureData(temperatureValue);
    }

    //============================== Codec ==============================\\
    public static final StreamCodec<FriendlyByteBuf, PlayerTemperatureDataSync> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.FLOAT, PlayerTemperatureDataSync::temperatureValue,
        PlayerTemperatureDataSync::new
    );

    //============================== Packet Creation ==============================\\
    /**
     * Send the packet to the player.
     * @param player the player to send the packet to
     * @param temperatureData the temperature data to send
    */
    public static void send(ServerPlayer player, CustomTemperatureData temperatureData) {
        PlayerTemperatureDataSync packet = new PlayerTemperatureDataSync(temperatureData.getTemperatureValue()); //create the packet
        player.connection.send(packet); //send the packet to the player
    }
}
