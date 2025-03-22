package net.fightingpainter.mc.towntime.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import net.minecraft.world.food.FoodData;

@Mixin(FoodData.class)
public interface FoodDataAccessor {
    @Accessor("foodLevel") public abstract int getFoodLevel();
    @Accessor("foodLevel") public abstract void setFoodLevel(int foodLevel);

    @Accessor("saturationLevel") public abstract float getSaturationLevel();
    @Accessor("saturationLevel") public abstract void setSaturationLevel(float saturationLevel);
}