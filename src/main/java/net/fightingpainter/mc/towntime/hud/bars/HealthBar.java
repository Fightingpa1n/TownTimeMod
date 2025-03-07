package net.fightingpainter.mc.towntime.hud.bars;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import net.fightingpainter.mc.towntime.hud.BaseBarElement;
import net.fightingpainter.mc.towntime.TownTime;

public class HealthBar extends BaseBarElement {
    private final static ResourceLocation NORMAL = ResourceLocation.fromNamespaceAndPath(TownTime.MOD_ID, "textures/hud/health.png");
    private final static ResourceLocation POISONED = ResourceLocation.fromNamespaceAndPath(TownTime.MOD_ID, "textures/hud/health_poisoned.png");
    private final static ResourceLocation WITHERED = ResourceLocation.fromNamespaceAndPath(TownTime.MOD_ID, "textures/hud/health_withered.png");
    private final static ResourceLocation FREEZING = ResourceLocation.fromNamespaceAndPath(TownTime.MOD_ID, "textures/hud/health_freezing.png");
    private final static ResourceLocation BURNING = ResourceLocation.fromNamespaceAndPath(TownTime.MOD_ID, "textures/hud/health_burning.png");
    private final static ResourceLocation BACKGROUND = ResourceLocation.fromNamespaceAndPath(TownTime.MOD_ID, "textures/hud/health_background.png");

    public HealthBar(int x, int y) {super(x, y);} //constructor
    
    @Override
    public boolean shouldRender(Player player) {//check gamemode
        if (player.isCreative() || player.isSpectator()) {return false;}
        else {return true;}
    }
    
    @Override
    public void getParameters(Player player) {
        this.maxValue = player.getMaxHealth();
        this.value = player.getHealth();
    }

    @Override
    public void render(GuiGraphics graphics) {
        renderSimpleTexture(graphics, BACKGROUND, 130, 11, getX(), getY()); //render background
        renderBarRight(graphics, NORMAL, 120, 5, getX()+1, getY()+3); //render health bar
    }
}
