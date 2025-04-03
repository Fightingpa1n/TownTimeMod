package net.fightingpainter.mc.towntime.food;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.fightingpainter.mc.towntime.mixin.PlayerMixin;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import toughasnails.api.player.ITANPlayer;

public class CustomTemperatureData {

    private static final float DEFAULT_TEMPERATURE = 0.0f; //default temperature value



    private float temperatureValue; //the temperature value, (-100 to 100 where 0 is the default temperature)

    public CustomTemperatureData(float temperatureValue) {
        this.temperatureValue = Math.clamp(temperatureValue, -100.0f, 100.0f); //clamp value
    }

    public CustomTemperatureData() {
        this.temperatureValue = DEFAULT_TEMPERATURE; //default value
    }


    /**
     * Set the temperature value, (-100 to 100).
     * @param temperatureValue the temperature value to set
    */
    public void setTemperatureValue(float temperatureValue) {
        this.temperatureValue = Math.clamp(temperatureValue, -100.0f, 100.0f);
    }

    /** Get the temperature value. */
    public float getTemperatureValue() {return temperatureValue;}

    /**
     * Get the temperature level based on the temperature value.
     * @see TemperatureLevel#getTemperatureLevel(float)
    */
    public TemperatureLevel getTemperatureLevel() {
        return TemperatureLevel.getTemperatureLevel(temperatureValue);
    }


    //============================== Data Saving ==============================\\
    /**
     * Write the temperature data to players CompoundTag
     * @param nbt the CompoundTag to write to
    */
    public void writeToTag(CompoundTag nbt) {
        nbt.putFloat("temperature_value", temperatureValue); //write the temperature value to the tag
    }

    /**
     * Read the temperature data from players CompoundTag
     * @param nbt the CompoundTag to read from
    */
    public void readFromTag(CompoundTag nbt) {
        if (nbt.contains("temperature_value")) {temperatureValue = nbt.getFloat("temperature_value");} //read the temperature value from the tag
        else {temperatureValue = DEFAULT_TEMPERATURE;} //if not found, set to default value
    }

    /**
     * Get the temperature data from the player.
     * @param player the player to get the temperature data from
     * @return the temperature data of the player
    */
    public static CustomTemperatureData of(Player player) {
        return ((PlayerWrapper)player).getTemperatureData();
    }

    public enum TemperatureLevel {
        NEUTRAL,
        COLD, ICY, FREEZING,
        WARM, HOT, SCORCHING;

        /**
         * Get the temperature level based on the temperature value.
         * temperatureValue is between -100 and 100, where 0 is the default temperature.
         * The temperature levels are as follows:
         *  -100 = FREEZING
         *  -100 to -75 = ICY
         *  -75 to -25 = COLD
         *  -25 to 25 = NEUTRAL 
         *  25 to 75 = WARM
         *  75 to 100 = HOT
         *  100 = SCORCHING
         * @param temperatureValue the temperature value to check
         * @return the temperature level the value would result in
        */
        public static TemperatureLevel getTemperatureLevel(float temperatureValue) {
            temperatureValue = Math.clamp(temperatureValue, -100.0f, 100.0f); //clamp value
            if (temperatureValue == -100.0f) {return FREEZING;}
            else if (temperatureValue >= -100.0f && temperatureValue < -75.0f) {return ICY;}
            else if (temperatureValue >= -75.0f && temperatureValue < -25.0f) {return COLD;}
            else if (temperatureValue >= -25.0f && temperatureValue < 25.0f) {return NEUTRAL;}
            else if (temperatureValue >= 25.0f && temperatureValue < 75.0f) {return WARM;}
            else if (temperatureValue >= 75.0f && temperatureValue < 100.0f) {return HOT;}
            else if (temperatureValue == 100.0f) {return SCORCHING;}
            else {return NEUTRAL;} //default case. should never happen, but just in case
        }
    }
}