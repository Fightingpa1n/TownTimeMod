package net.fightingpainter.mc.towntime.food;

import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameRules;

public class DataHandler {

    public static void playerTick(Player player) {
        if (player.level().isClientSide()) return;
        SustinanceData data = SustinanceData.of(player);
        Difficulty difficulty = player.level().getDifficulty();

    }

    public static void causeFoodExhaustion(Player player, float exhaustion) {


    }


    public static void foodTick(SustinanceData data, Player player) {
        Difficulty difficulty = player.level().getDifficulty();

        // // Exhaustion processing stays the same (still affects hunger)
        // if (data.getExhaustion() > 4.0F) {
        //     data.setExhaustion(data.getExhaustion() - 4.0F);
        //     if (data.getSaturation() > 0.0F) {
        //         data.addSaturation(-1.0F);
        //     } else if (difficulty != Difficulty.PEACEFUL) {
        //         data.addHunger(-1);
        //     }
        // }

        // // Health regen ONLY based on thirst now
        // boolean naturalRegen = player.level().getGameRules().getBoolean(GameRules.RULE_NATURAL_REGENERATION);
        // if (naturalRegen && player.isHurt()) {
        //     if (data.getThirst() >= 20) {
        //         // Fast regen when hydrated
        //         if (data.getHydration() > 0.0F) {
        //             data.incrementTickTimer();
        //             if (data.getTickTimer() >= 10) {
        //                 float replenish = Math.min(data.getHydration(), 6.0F);
        //                 player.heal(replenish / 6.0F);
        //                 data.setThirstExhaustion(data.getThirstExhaustion() + replenish);
        //                 data.setTickTimer(0);
        //             }
        //         }
        //         // Slow regen when no hydration but thirst full
        //         else {
        //             data.incrementTickTimer();
        //             if (data.getTickTimer() >= 80) {
        //                 player.heal(1.0F);
        //                 data.setThirstExhaustion(data.getThirstExhaustion() + 6.0F);
        //                 data.setTickTimer(0);
        //             }
        //         }
        //     } else {
        //         data.setTickTimer(0);  // No regen if thirst < 20
        //     }
        // } else {
        //     data.setTickTimer(0);
        // }

        // // Starvation from hunger still applies as usual
        // if (data.getHunger() <= 0) {
        //     data.incrementTickTimer();
        //     if (data.getTickTimer() >= 80) {
        //         float health = player.getHealth();
        //         if (health > 10.0F || difficulty == Difficulty.HARD || (health > 1.0F && difficulty == Difficulty.NORMAL)) {
        //             player.hurt(player.damageSources().source(TANDamageTypes.THIRST), 1.0F);
        //         }
        //         data.setTickTimer(0);
        //     }
        // }
    }
}
