package net.fightingpainter.mc.towntime.food;

import net.fightingpainter.mc.towntime.mixin.FoodDataAccessor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.effect.MobEffects;
import toughasnails.api.damagesource.TANDamageTypes;
import toughasnails.api.potion.TANEffects;
import toughasnails.api.temperature.TemperatureHelper;
import toughasnails.api.temperature.TemperatureLevel;
import toughasnails.init.ModConfig;
import toughasnails.init.ModPackets;
import toughasnails.network.UpdateTemperaturePacket;
import toughasnails.network.UpdateThirstPacket;
import toughasnails.temperature.BuiltInTemperatureModifier;
import toughasnails.temperature.TemperatureData;
import toughasnails.thirst.ThirstData;

public class DataHandler {

    //TODO: maybe move to config also sort stuff
    //============================== Settings ==============================\\
    private static final float FOOD_EXHAUSTION_THRESHOLD = 4.0F; //the threshold for food exhaustion
    private static final float THIRST_EXHAUSTION_THRESHOLD = 4.0F; //the threshold for thirst exhaustion

    private static final int FAST_HEAL_TICKS = 10; //the ticks for fast heal (default is 10, or 0.5 seconds)
    private static final int SLOW_HEAL_TICKS = 80; //the ticks for slow heal (default is 80, or 4 seconds)
    private static final int DAMAGE_TICKS = 80; //the ticks for damage (default is 80, or 4 seconds)

    private static final float SATURATION_PER_HEAL = 6.0F; //the saturation required to heal 1 health
    private static final float SLOW_HEAL_AMOUNT = 1.0F; //the amount of health to heal for slow heal
    private static final float SLOW_HEAL_EXHAUSTION = 6.0F; //the amount of exhaustion to add for slow heal


    public static void playerTick(FoodDataAccessor food, ThirstData thirst, TemperatureData temperature, Player player, Difficulty difficulty, GameRules rules) {
        food.setLastFoodLevel(food.getFoodLevel()); //set last food level
        thirst.setLastThirst(thirst.getThirst()); //set last thirst level

        //rules and settings
        boolean isPeaceful = (difficulty == Difficulty.PEACEFUL); //if difficulty is peaceful
        boolean doRegen = rules.getBoolean(GameRules.RULE_NATURAL_REGENERATION); //get natural regeneration rule
        boolean thirstPreventHealing = ModConfig.thirst.thirstPreventHealthRegen; //get thirst healing setting (if thirst should prevent health regen)
        
        boolean resetFoodTimer = true; //if food timer should be reset
        boolean resetThirstTimer = true; //if thirst timer should be reset

        //=========== Exhaustion ===========\\
        if (food.getExhaustionLevel() > FOOD_EXHAUSTION_THRESHOLD) { //if food exhaustion level is greater than threshold
            food.setExhaustionLevel(Math.max(food.getExhaustionLevel() - FOOD_EXHAUSTION_THRESHOLD, 0.0F)); //decrement food exhaustion level by threshold
            if (food.getSaturationLevel() > 0.0F) { //if player has saturation
                food.setSaturationLevel(Math.max(food.getSaturationLevel() - 1.0F, 0.0F)); //decrement saturation by 1
            } else if (!isPeaceful) {food.setFoodLevel(Math.max(food.getFoodLevel() - 1, 0));} //if not pecaceful, decrement food level by 1 if no saturation
        }
        
        if (thirst.getExhaustion() > THIRST_EXHAUSTION_THRESHOLD) { //if thirst exhaustion level is greater than threshold
            thirst.setExhaustion(Math.max(thirst.getExhaustion() - THIRST_EXHAUSTION_THRESHOLD, 0.0F)); //decrement thirst exhaustion level by threshold
            if (thirst.getHydration() > 0.0F) { //if player has hydration
                thirst.setHydration(Math.max(thirst.getHydration() - 1.0F, 0.0F)); //decrement hydration by 1
            } else if (!isPeaceful) {thirst.setThirst(Math.max(thirst.getThirst() - 1, 0));} //if not peaceful, decrement thirst by 1 if no hydration
        }
        
        //=========== Regeneration ===========\\
        if (player.isHurt() && doRegen) { //if player is hurt and natural regeneration is enabled
            if (food.getSaturationLevel() > 0.0F && food.getFoodLevel() >= 20 && (!thirstPreventHealing || (thirst.getHydration() > 0.0F && thirst.getThirst() >= 20))) { //if player has saturation and food level is full (same goes for thirst if enabled)
                resetFoodTimer = false; //don't reset food timer
                food.setTickTimer(food.getTickTimer() + 1); //increment food tick timer
                if (food.getTickTimer() >= FAST_HEAL_TICKS) { //if food tick timer reached fast heal ticks
                    float amount = Math.min(food.getSaturationLevel(), SATURATION_PER_HEAL); //get saturation amount available for this heal tick (max is SATURATION_PER_HEAL)
                    player.heal(amount / SATURATION_PER_HEAL); //heal player by amount / SATURATION_PER_HEAL (1 health if amount is equal to SATURATION_PER_HEAL)
                    food.setExhaustionLevel(food.getExhaustionLevel()+amount); //add amount to exhaustion level
                    thirst.setExhaustion(thirst.getExhaustion()+amount); //add amount to thirst exhaustion level
                    food.setTickTimer(0); //reset food tick timer
                }
            } else if (food.getFoodLevel() >= 18 && (!thirstPreventHealing || thirst.getThirst() >= 18)) { //if food level is greater than or equal to 18 (same goes for thirst if enabled)
                resetFoodTimer = false; //don't reset food timer
                food.setTickTimer(food.getTickTimer() + 1); //increment tick timer
                if (food.getTickTimer() >= SLOW_HEAL_TICKS) { //if food tick timer reached slow heal ticks
                    player.heal(SLOW_HEAL_AMOUNT); //heal player by SLOW_HEAL_AMOUNT
                    food.setExhaustionLevel(food.getExhaustionLevel()+SLOW_HEAL_EXHAUSTION); //add SLOW_HEAL_EXHAUSTION to exhaustion level
                    thirst.setExhaustion(thirst.getExhaustion()+SLOW_HEAL_EXHAUSTION); //add SLOW_HEAL_EXHAUSTION to thirst exhaustion level
                    food.setTickTimer(0);
                }
            }
        }

        //=========== Damage ===========\\
        if (food.getFoodLevel() <= 0) { //if food level is empty
            resetFoodTimer = false; //don't reset food timer
            food.setTickTimer(food.getTickTimer() + 1); //increment tick timer
            if (food.getTickTimer() >= DAMAGE_TICKS) { //if food tick timer reached damage ticks
                if (player.getHealth() > 10.0F || difficulty == Difficulty.HARD || (player.getHealth() > 1.0F && difficulty == Difficulty.NORMAL)) { //if player health is greater than 10, difficulty is hard, or player health is greater than 1 and difficulty is normal
                    player.hurt(player.damageSources().starve(), 1.0F); //hurt player by 1 health
                }
                food.setTickTimer(0); //reset food tick timer
            }
        }

        if (thirst.getThirst() <= 0) { //if thirst level is empty
            resetThirstTimer = false; //don't reset thirst timer
            thirst.setTickTimer(thirst.getTickTimer() + 1); //increment tick timer
            if (thirst.getTickTimer() >= DAMAGE_TICKS) { //if thirst tick timer reached damage ticks
                if (player.getHealth() > 10.0F || difficulty == Difficulty.HARD || (player.getHealth() > 1.0F && difficulty == Difficulty.NORMAL)) { //if player health is greater than 10, difficulty is hard, or player health is greater than 1 and difficulty is normal
                    player.hurt(player.damageSources().source(TANDamageTypes.THIRST), 1.0F); //hurt player by 1 health
                }
                thirst.setTickTimer(0); //reset thirst tick timer
            }
        }
        
        //=========== Reset ===========\\
        if (resetFoodTimer) {food.setTickTimer(0);} //reset food tick timer
        if (resetThirstTimer) {thirst.setTickTimer(0);} //reset thirst tick timer
        

        // ------------------- TEMPERATURE -------------------
        if (!ModConfig.temperature.enableTemperature) return;

        // Remove invalid thermoregulators
        temperature.getNearbyThermoregulators().removeIf(pos -> player.level().getBlockEntity(pos) == null || player.distanceToSqr(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5) > 1024);

        temperature.setChangeDelayTicks(Math.max(0, temperature.getChangeDelayTicks() - 1));
        temperature.setDryTicks(temperature.getDryTicks() + 1);

        if (!player.hasEffect(TANEffects.CLIMATE_CLEMENCY)) {
            int changeDelay = ModConfig.temperature.temperatureChangeDelay;
            TemperatureLevel current = temperature.getTargetLevel();
            TemperatureLevel newTarget = TemperatureHelper.getTemperatureAtPos(player.level(), player.blockPosition());

            for (var modifier : BuiltInTemperatureModifier.getTemperatureModifierOrder()) {
                var out = modifier.apply(player, newTarget, changeDelay);
                newTarget = out.getA();
                changeDelay = out.getB();
            }

            if (newTarget != current) {
                temperature.setTargetLevel(newTarget);
                if ((temperature.getLevel() == TemperatureLevel.ICY || temperature.getLevel() == TemperatureLevel.HOT)
                        && newTarget != TemperatureLevel.ICY && newTarget != TemperatureLevel.HOT) {
                    changeDelay = Math.min(changeDelay, ModConfig.temperature.extremityReboundTemperatureChangeDelay);
                }
                temperature.setChangeDelayTicks(changeDelay);
            }

            if (temperature.getChangeDelayTicks() == 0 && current != temperature.getLevel()) {
                int delta = Integer.compare(temperature.getTargetLevel().ordinal(), temperature.getLevel().ordinal());
                temperature.setLevel(temperature.getLevel().increment(delta));
                temperature.setChangeDelayTicks(changeDelay);
            }
        } else {
            temperature.setLevel(TemperatureLevel.NEUTRAL);
        }

        temperature.setExtremityDelayTicks(Math.max(0, temperature.getExtremityDelayTicks() - 1));

        if (temperature.getLastLevel() != temperature.getLevel() && (temperature.getLevel() == TemperatureLevel.ICY || temperature.getLevel() == TemperatureLevel.HOT)) {
            temperature.setExtremityDelayTicks(ModConfig.temperature.extremityDamageDelay);
        }

        int hyperthermic = temperature.getHyperthermiaTicks();
        int maxHyper = TemperatureHelper.getTicksRequiredForHyperthermia();

        if (!player.isCreative() && !player.isSpectator()) {
            if (!player.hasEffect(MobEffects.FIRE_RESISTANCE) && temperature.getLevel() == TemperatureLevel.HOT && temperature.getExtremityDelayTicks() == 0) {
                temperature.setHyperthermiaTicks(Math.min(maxHyper, hyperthermic + 1));
            } else {
                temperature.setHyperthermiaTicks(Math.max(0, hyperthermic - 2));
            }
        } else {
            if (temperature.getHyperthermiaTicks() > 0) {
                temperature.setHyperthermiaTicks(Math.max(0, hyperthermic - 2));
            }
        }
        
        ResourceLocation SPEED_MODIFIER_ID = ResourceLocation.fromNamespaceAndPath("toughasnails", "speed_modifier");
        AttributeInstance attribute = player.getAttribute(Attributes.MOVEMENT_SPEED);
        if (attribute != null && attribute.getModifier(SPEED_MODIFIER_ID) != null) {
            attribute.removeModifier(SPEED_MODIFIER_ID);
        }

        if (!player.level().getBlockState(player.getOnPos()).isAir()) {
            int ticks = TemperatureHelper.getTicksHyperthermic(player);
            if (ticks > 0) {
                AttributeInstance attributeinstance = player.getAttribute(Attributes.MOVEMENT_SPEED);
                if (attributeinstance == null) {return;}
                float f = 0.015F * TemperatureHelper.getPercentHyperthermic(player);
                attributeinstance.addTransientModifier(new AttributeModifier(SPEED_MODIFIER_ID, (double)f, Operation.ADD_VALUE));
            }
        }

        if (player.tickCount % 40 == 0 && TemperatureHelper.isFullyHyperthermic(player)) {
            player.hurt(player.damageSources().source(TANDamageTypes.HYPERTHERMIA), 1);
        }

        //=========== Sync ===========\\
        ModPackets.HANDLER.sendToPlayer(new UpdateThirstPacket(thirst.getThirst(), thirst.getHydration()), (ServerPlayer)player);
        ModPackets.HANDLER.sendToPlayer(new UpdateTemperaturePacket(temperature.getLevel(), temperature.getHyperthermiaTicks(), temperature.getNearbyThermoregulators()), (ServerPlayer)player);
    }
}
