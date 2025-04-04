package net.fightingpainter.mc.towntime.food;

import java.util.List;

import com.mojang.datafixers.util.Either;

import net.fightingpainter.mc.towntime.ClientConfig;
import net.fightingpainter.mc.towntime.data.ModDataComponentTypes;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;

public class ConsumableHelper {

    /**
     * Get the SustenanceProperties of an ItemStack if it has one
     * @param stack the ItemStack to get the SustenanceProperties of
     * @return the SustenanceProperties of the ItemStack, or null if it doesn't have one
    */
    public static SustenanceProperties getSustenanceProperties(ItemStack stack) {
        if (!stack.has(ModDataComponentTypes.SUSTINENCE.get())) {return null;}
        try {
            SustenanceProperties props = stack.get(ModDataComponentTypes.SUSTINENCE.get());
            if (props == null) {return null;}
            return props;
        } catch (Exception e) {
            return null;
        }
    }
    
    /**
     * Check if an item is consumable
     * @param stack the itemstack to check
     * @return true if the item is consumable (has SustenanceProperties)
    */
    public static boolean isConsumable(ItemStack stack) {
        SustenanceProperties props = getSustenanceProperties(stack);
        return props != null;
    }
    
    /**
     * Check if a player can consume SustenanceProperties
     * @param player the player to check
     * @param props the SustenanceProperties to check
     * @return true if the player can consume the item
    */
    public static boolean canConsume(Player player, SustenanceProperties props) {
        SustinanceData data = SustinanceData.of(player); //get the player's sustinance data
        boolean couldEat = (data.getHunger() <= 20 && props.getNutrition() > 0); //if the player is not full and the food has nutrition
        boolean couldDrink = (data.getThirst() <= 20 && props.getWater() > 0); //if the player is not full and the food has water
        return (couldEat || couldDrink) || props.canAlwaysConsume();
    }

    /**
     * Check if a player can consume an item
     * @param player the player to check
     * @param stack the itemstack to check
     * @return true if the player can consume the item
    */
    public static boolean canConsume(Player player, ItemStack stack) {
        SustenanceProperties props = getSustenanceProperties(stack);
        if (props == null) {return false;}
        return canConsume(player, props);
    }

    /**
     * Consume a consumable item
     * @param player the player to consume the item
     * @param props the SustenanceProperties of the item
    */
    public static void consume(Player player, SustenanceProperties props) {
        SustinanceData data = SustinanceData.of(player);
        data.addHunger(props.getNutrition()); data.addSaturation(props.getSaturation());
        data.addThirst(props.getWater()); data.addHydration(props.getHydration());
    }

    /**
     * Apply the effects of a consumable item to a player
     * Should be server-side only
     * @param player the player to apply the effects to
     * @param props the SustenanceProperties of the item
    */
    public static void applyEffects(Player player, SustenanceProperties props) { //Apply effects
        for (SustenanceProperties.PossibleEffect effectData : props.getEffects()) {
            if (player.getRandom().nextFloat() < effectData.getProbability()) {
                player.addEffect(effectData.getEffect());
            }
        }
    }


    /**
     * Modify the tooltip of a consumable item
     * @param tooltipElements the tooltip to modify
     * @param stack the itemstack to modify
    */
    public static void modifyTooltip(List<Either<FormattedText, TooltipComponent>> tooltipElements, ItemStack stack) {
        SustenanceProperties props = getSustenanceProperties(stack);
        if (props == null) {return;}

        int nutrition = props.getNutrition();
        float saturation = props.getSaturation();
        int water = props.getWater();
        float hydration = props.getHydration();
        
        tooltipElements.add(Either.left(FormattedText.of("Nutrition: "+nutrition + " | Saturation: "+saturation)));
        tooltipElements.add(Either.left(FormattedText.of("Water: "+water + " | Hydration: "+hydration)));
    }    
}
