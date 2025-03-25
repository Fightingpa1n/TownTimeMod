package net.fightingpainter.mc.towntime.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import toughasnails.thirst.ThirstHooks;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;

@Mixin(ThirstHooks.class)
public class ThirstHooksMixin {
    @Overwrite
    public static void onCauseFoodExhaustion(Player player, float exhaustion) {} //Do nothing

    @Overwrite
    public static void doFoodDataTick(FoodData data, Player player) {} //Do nothing
}