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

@Mixin(value=FoodData.class, priority=1100)
public abstract class FoodDataMixin {
    @Shadow private int foodLevel = 20; //overwrite default value
    @Shadow private float saturationLevel = 20.0f; //overwrite default value

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true) //cancel tick method
    public void onTick(Player player, CallbackInfo ci) {
        ci.cancel(); //cancel the original method
    }
}
