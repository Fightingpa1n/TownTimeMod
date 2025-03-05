package net.fightingpainter.mc.towntime.hud.bars;

import net.fightingpainter.mc.towntime.TownTime;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import toughasnails.api.thirst.IThirst;
import toughasnails.api.thirst.ThirstHelper;

public class Thirst extends BaseBar{
    private final static ResourceLocation NORMAL = ResourceLocation.fromNamespaceAndPath(TownTime.MOD_ID, "textures/hud/thirst.png");
    private final static ResourceLocation BACKGROUND = ResourceLocation.fromNamespaceAndPath(TownTime.MOD_ID, "textures/hud/health_background.png");
    
    public Thirst(int x, int y) {
        this.texture = NORMAL;
        this.backgroundTexture = BACKGROUND;
        this.length = 37;
        this.height = 6;
        this.x = x;
        this.y = y;
    }

    @Override
    public void getParameters(Player player) {
        this.value = ThirstHelper.getThirst(player).getThirst();
        this.maxValue = 20;
    }   

    @Override
    public boolean shouldRender(Player player) {return true;} //health should always render
    
    @Override
    public void render(GuiGraphics guiGraphics) {
        super.render(guiGraphics);
    }
}
