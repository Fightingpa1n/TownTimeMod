package net.fightingpainter.mc.towntime.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.fightingpainter.mc.towntime.food.DataHandler;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import toughasnails.api.temperature.TemperatureHelper;
import toughasnails.api.thirst.ThirstHelper;
import toughasnails.temperature.TemperatureData;
import toughasnails.thirst.ThirstData;

@Mixin(value=Player.class, priority=1100)
public abstract class PlayerMixin {

    // @Inject(method="causeFoodExhaustion", at=@At("HEAD"), cancellable=true)
    // public void onCauseFoodExhaustion(float exhaustion, CallbackInfo ci) { //cancel exhaustion
    //     ci.cancel(); //cancel the original method
    // }

    @Inject(method="tick", at=@At("TAIL"))
    public void onTick(CallbackInfo ci) {
        Player player = (Player)(Object)this; //get player
        if (!player.level().isClientSide()){ //if not on client side
            Difficulty difficulty = player.level().getDifficulty(); //get difficulty
            FoodDataAccessor food = (FoodDataAccessor)player.getFoodData(); //get food data
            ThirstData thirst = (ThirstData)ThirstHelper.getThirst(player); //get thirst data
            TemperatureData temperature = (TemperatureData)TemperatureHelper.getTemperatureData(player); //get temperature data
            
            DataHandler.playerTick(food, thirst, temperature, player, difficulty); //do player tick
        }
    }

    //TODO: change methods to change exhasution stuff like inject into methods that call causeFoodExhaustion.
}
