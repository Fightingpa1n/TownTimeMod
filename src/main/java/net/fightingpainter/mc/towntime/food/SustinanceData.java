package net.fightingpainter.mc.towntime.food;

import net.fightingpainter.mc.towntime.mixin.FoodDataAccessor;
import net.fightingpainter.mc.towntime.mixin.ThirstDataAccessor;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import toughasnails.api.thirst.ThirstHelper;
import toughasnails.thirst.ThirstData;

public class SustinanceData {

    private FoodDataAccessor foodData;
    private ThirstDataAccessor thirstData;

    private SustinanceData(FoodDataAccessor foodData, ThirstDataAccessor thirstData) {
        this.foodData = foodData;
        this.thirstData = thirstData;
    }

    /**
     * Get's the SUstinanceData of a player 
     * @param player the player to get the sustinance data of
     * @return the sustinance data of the player
    */
    public static SustinanceData of(Player player) {
        FoodData foodData = player.getFoodData();
        ThirstData thirstData = (ThirstData)ThirstHelper.getThirst(player);
        return new SustinanceData((FoodDataAccessor)foodData, (ThirstDataAccessor)thirstData);    
    }

    //============================== Methods ==============================\\
    //=========== Hunger ===========\\
    /** get the Hunger Value of the player */
    public int getHunger() {
        return foodData.getFoodLevel();
    }
    
    /**
     * set the Hunger Value of the player
     * @param hunger the new hunger value
    */
    public void setHunger(int hunger) {
        foodData.setFoodLevel(Math.clamp(hunger, 0, 20));
    }
    
    /**
     * add to the hunger value of the player
     * @param hunger the amount to add to the hunger value
    */
    public void addHunger(int hunger) {
        setHunger(getHunger() + hunger);
    }
    
    //=========== Saturation ===========\\
    /** get the Saturation Value of the player */
    public float getSaturation() {
        return foodData.getSaturationLevel();
    }
    
    /**
     * set the Saturation Value of the player
     * @param saturation the new saturation value
    */
    public void setSaturation(float saturation) {
        foodData.setSaturationLevel(Math.clamp(saturation, 0, 20));
    }
    
    /**
     * add to the saturation value of the player
     * @param saturation the amount to add to the saturation value
    */
    public void addSaturation(float saturation) {
        setSaturation(getSaturation() + saturation);
    }
    
    //=========== Exhaustion ===========\\
    /** get the Exhaustion Value of the player */
    public float getExhaustion() {
        return foodData.getExhaustionLevel();
    }

    /**
     * set the Exhaustion Value of the player
     * @param exhaustion the new exhaustion value
    */
    public void setExhaustion(float exhaustion) {
        foodData.setExhaustionLevel(exhaustion);
    }
    
    //=========== Thirst ===========\\
    /** get the Thirst Value of the player */
    public int getThirst() {
        return thirstData.getThirstLevel();
    }
    
    /**
     * set the Thirst Value of the player
     * @param thirst the new thirst value
    */
    public void setThirst(int thirst) {
        thirstData.setThirstLevel(Math.clamp(thirst, 0, 20));
    }
    
    /**
     * add to the thirst value of the player
     * @param thirst the amount to add to the thirst value
    */
    public void addThirst(int thirst) {
        setThirst(getThirst() + thirst);
    }
    
    //=========== Hydration ===========\\
    /** get the Hydration Value of the player */
    public float getHydration() {
        return thirstData.getHydrationLevel();
    }

    /**
     * set the Hydration Value of the player
     * @param hydration the new hydration value
    */
    public void setHydration(float hydration) {
        thirstData.setHydrationLevel(Math.clamp(hydration, 0, 20));
    }

    /**
     * add to the hydration value of the player
     * @param hydration the amount to add to the hydration value
    */
    public void addHydration(float hydration) {
        setHydration(getHydration() + hydration);
    }

    //=========== Thirst Exhaustion ===========\\
    /** get the Thirst Exhaustion Value of the player */
    public float getThirstExhaustion() {
        return thirstData.getExhaustionLevel();
    }

    /**
     * set the Thirst Exhaustion Value of the player
     * @param exhaustion the new exhaustion value
    */
    public void setThirstExhaustion(float exhaustion) {
        thirstData.setExhaustionLevel(exhaustion);
    }

    //=========== Tick Timer ===========\\
    /** get the Tick Timer Value of the player */
    public int getTickTimer() {
        return foodData.getTickTimer();
    }

    /**
     * set the Tick Timer Value of the player
     * @param tickTimer the new tick timer value
    */
    public void setTickTimer(int tickTimer) {
        foodData.setTickTimer(tickTimer);
    }

    /** increment the tick timer value of the player */
    public void incrementTickTimer() {
        setTickTimer(getTickTimer() + 1);
    }
}
