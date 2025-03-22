package net.fightingpainter.mc.towntime.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.world.food.FoodData;

@Mixin(FoodData.class)
public abstract class FoodDataMixin {
    @Shadow private int foodLevel = 20; //overwrite default value
    @Shadow private float saturationLevel = 20.0f; //overwrite default value
}
