package net.fightingpainter.mc.towntime.food;

import net.fightingpainter.mc.towntime.mixin.FoodDataAccessor;
import net.minecraft.util.Mth;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.effect.MobEffects;
import toughasnails.api.damagesource.TANDamageTypes;
import toughasnails.api.potion.TANEffects;
import toughasnails.api.temperature.TemperatureHelper;
import toughasnails.api.temperature.TemperatureLevel;
import toughasnails.api.thirst.ThirstHelper;
import toughasnails.core.ToughAsNails;
import toughasnails.init.ModConfig;
import toughasnails.temperature.BuiltInTemperatureModifier;
import toughasnails.temperature.TemperatureData;
import toughasnails.thirst.ThirstData;

import java.util.Iterator;

public class DataHandler {

    public static void playerTick(FoodDataAccessor food, ThirstData thirst, TemperatureData temperature, Player player, Difficulty difficulty) {
        // ------------------- FOOD -------------------
        food.setLastFoodLevel(food.getFoodLevel());

        if (food.getExhaustionLevel() > 4.0F) {
            food.setExhaustionLevel(food.getExhaustionLevel() - 4.0F);

            if (food.getSaturationLevel() > 0.0F) {
                food.setSaturationLevel(Math.max(food.getSaturationLevel() - 1.0F, 0.0F));
            } else if (difficulty != Difficulty.PEACEFUL) {
                food.setFoodLevel(Math.max(food.getFoodLevel() - 1, 0));
            }
        }

        boolean regen = player.level().getGameRules().getBoolean(GameRules.RULE_NATURAL_REGENERATION);

        if (regen && food.getSaturationLevel() > 0.0F && player.isHurt() && food.getFoodLevel() >= 20 && (!ModConfig.thirst.thirstPreventHealthRegen || (thirst.getHydration() > 0.0F && thirst.getThirst() >= 20))) {
            food.setTickTimer(food.getTickTimer() + 1);
            if (food.getTickTimer() >= 10) {
                float amount = Math.min(food.getSaturationLevel(), 6.0F);
                player.heal(amount / 6.0F);
                food.setExhaustionLevel(food.getExhaustionLevel()+amount);
                thirst.addExhaustion(amount);
                food.setTickTimer(0);
            }
        } else if (regen && food.getFoodLevel() >= 18 && (!ModConfig.thirst.thirstPreventHealthRegen || thirst.getThirst() >= 18) && player.isHurt()) {
            food.setTickTimer(food.getTickTimer() + 1);
            if (food.getTickTimer() >= 80) {
                player.heal(1.0F);
                food.setExhaustionLevel(food.getExhaustionLevel()+6.0F);
                thirst.addExhaustion(6.0F);
                food.setTickTimer(0);
            }
        } else if (food.getFoodLevel() <= 0) {
            food.setTickTimer(food.getTickTimer() + 1);
            if (food.getTickTimer() >= 80) {
                if (player.getHealth() > 10.0F || difficulty == Difficulty.HARD || (player.getHealth() > 1.0F && difficulty == Difficulty.NORMAL)) {
                    player.hurt(player.damageSources().starve(), 1.0F);
                }
                food.setTickTimer(0);
            }
        } else {
            food.setTickTimer(0);
        }

        // ------------------- THIRST -------------------
        if (thirst.getExhaustion() > ModConfig.thirst.thirstExhaustionThreshold) {
            thirst.addExhaustion((float) -ModConfig.thirst.thirstExhaustionThreshold);

            if (thirst.getHydration() > 0.0F) {
                thirst.setHydration(Math.max(thirst.getHydration() - 1.0F, 0.0F));
            } else if (difficulty != Difficulty.PEACEFUL) {
                thirst.setThirst(Math.max(thirst.getThirst() - 1, 0));
            }
        }

        if (thirst.getThirst() <= 0) {
            thirst.addTicks(1);
            if (thirst.getTickTimer() >= 80) {
                if (player.getHealth() > 10.0F || difficulty == Difficulty.HARD || (player.getHealth() > 1.0F && difficulty == Difficulty.NORMAL)) {
                    player.hurt(player.damageSources().source(TANDamageTypes.THIRST), 1.0F);
                }
                thirst.setTickTimer(0);
            }
        } else {
            thirst.setTickTimer(0);
        }

        if (difficulty == Difficulty.PEACEFUL && regen) {
            if (thirst.isThirsty() && player.tickCount % 10 == 0) {
                thirst.setThirst(thirst.getThirst() + 1);
            }
        }

        // ------------------- TEMPERATURE -------------------
        if (!ModConfig.temperature.enableTemperature || player.level().isClientSide()) return;

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

        if (player.tickCount % 40 == 0 && TemperatureHelper.isFullyHyperthermic(player)) {
            player.hurt(player.damageSources().source(TANDamageTypes.HYPERTHERMIA), 1);
        }
    }
}
