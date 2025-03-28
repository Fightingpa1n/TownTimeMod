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

    @Accessor("exhaustionLevel") public abstract float getExhaustionLevel();
    @Accessor("exhaustionLevel") public abstract void setExhaustionLevel(float exhaustionLevel);

    @Accessor("tickTimer") public abstract int getTickTimer();
    @Accessor("tickTimer") public abstract void setTickTimer(int tickTimer);

    @Accessor("lastFoodLevel") public abstract int getLastFoodLevel();
    @Accessor("lastFoodLevel") public abstract void setLastFoodLevel(int lastFoodLevel);
}