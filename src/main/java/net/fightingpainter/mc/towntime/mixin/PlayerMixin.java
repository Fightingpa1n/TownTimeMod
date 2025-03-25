package net.fightingpainter.mc.towntime.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.fightingpainter.mc.towntime.food.DataHandler;
import net.minecraft.world.entity.player.Player;

@Mixin(Player.class)
public abstract class PlayerMixin {

    @Inject(method="tick", at=@At(value="TAIL"))
    public void onTick(CallbackInfo ci) {
        Player player = (Player)(Object)this;
        DataHandler.playerTick(player);
    }

    @Inject(method="causeFoodExhaustion", at=@At(value="HEAD"))
    public void onCauseFoodExhaustion(float exhaustion, CallbackInfo ci) {
        Player player = (Player)(Object)this;
        DataHandler.causeFoodExhaustion(player, exhaustion);
    }
    
}
