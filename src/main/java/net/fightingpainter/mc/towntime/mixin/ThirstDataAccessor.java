package net.fightingpainter.mc.towntime.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import toughasnails.thirst.ThirstData;

@Mixin(ThirstData.class)
public interface ThirstDataAccessor {
    @Accessor("thirstLevel") public abstract int getThirstLevel();
    @Accessor("thirstLevel") public abstract void setThirstLevel(int thirstLevel);

    @Accessor("hydrationLevel") public abstract float getHydrationLevel();
    @Accessor("hydrationLevel") public abstract void setHydrationLevel(float hydrationLevel);
}