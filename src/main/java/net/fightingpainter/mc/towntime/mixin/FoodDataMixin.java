package net.fightingpainter.mc.towntime.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.fightingpainter.mc.towntime.food.DataHandler;
import net.fightingpainter.mc.towntime.food.SustinanceData;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;

@Mixin(value=FoodData.class)
public abstract class FoodDataMixin {
    @Shadow private int foodLevel = 20; //overwrite default value
    @Shadow private float saturationLevel = 20.0f; //overwrite default value

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true) //inject into the tick method
    public void onTick(Player player, CallbackInfo ci) {
        DataHandler.foodTick(SustinanceData.of(player), player);
        ci.cancel(); //cancel the original method
    }
}
