package net.fightingpainter.mc.towntime.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import toughasnails.thirst.ThirstData;

@Mixin(ThirstData.class)
public abstract class ThirstDataMixin {
    @Shadow private int thirstLevel = 20; //overwrite default value
    @Shadow private float hydrationLevel = 20.0f; //overwrite default value
}