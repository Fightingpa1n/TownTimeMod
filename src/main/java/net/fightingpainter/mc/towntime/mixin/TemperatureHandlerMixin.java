package net.fightingpainter.mc.towntime.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import toughasnails.temperature.TemperatureHandler;
import net.minecraft.world.entity.player.Player;

@Mixin(TemperatureHandler.class)
public class TemperatureHandlerMixin {
    @Overwrite
    public static void onPlayerTick(Player player) {} //Do nothing
}