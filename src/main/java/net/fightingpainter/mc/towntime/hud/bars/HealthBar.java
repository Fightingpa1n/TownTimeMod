package net.fightingpainter.mc.towntime.hud.bars;

import net.fightingpainter.mc.towntime.TownTime;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public class HealthBar extends BaseBar{
    private final static ResourceLocation NORMAL = ResourceLocation.fromNamespaceAndPath(TownTime.MOD_ID, "textures/hud/health.png");
    private final static ResourceLocation POISONED = ResourceLocation.fromNamespaceAndPath(TownTime.MOD_ID, "textures/hud/health_poisoned.png");
    private final static ResourceLocation WITHERED = ResourceLocation.fromNamespaceAndPath(TownTime.MOD_ID, "textures/hud/health_withered.png");
    private final static ResourceLocation FREEZING = ResourceLocation.fromNamespaceAndPath(TownTime.MOD_ID, "textures/hud/health_freezing.png");
    private final static ResourceLocation BURNING = ResourceLocation.fromNamespaceAndPath(TownTime.MOD_ID, "textures/hud/health_burning.png");
    private final static ResourceLocation BACKGROUND = ResourceLocation.fromNamespaceAndPath(TownTime.MOD_ID, "textures/hud/health_background.png");

    public HealthBar(int x, int y) {
        this.texture = NORMAL;
        this.backgroundTexture = BACKGROUND;
        this.length = 37;
        this.height = 6;
        this.x = x;
        this.y = y;
    }

    @Override
    public void getParameters(Player player) {
        this.maxValue = player.getMaxHealth();
        this.value = player.getHealth();
    }

    @Override
    public boolean shouldRender(Player player) {return true;} //health should always render
    
    @Override
    public void render(GuiGraphics guiGraphics) {
        super.render(guiGraphics);
    }
}
