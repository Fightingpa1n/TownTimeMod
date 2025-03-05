package net.fightingpainter.mc.towntime.hud.bars;

import net.fightingpainter.mc.towntime.TownTime;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public class Food extends BaseBar{
    private final static ResourceLocation NORMAL = ResourceLocation.fromNamespaceAndPath(TownTime.MOD_ID, "textures/hud/food.png");
    private final static ResourceLocation BACKGROUND = ResourceLocation.fromNamespaceAndPath(TownTime.MOD_ID, "textures/hud/health_background.png");
    
    public Food(int x, int y) {
        this.texture = NORMAL;
        this.backgroundTexture = BACKGROUND;
        this.length = 37;
        this.height = 6;
        this.x = x;
        this.y = y;
    }

    @Override
    public void getParameters(Player player) {
        this.value = player.getFoodData().getFoodLevel();
        this.maxValue = 20;
    }

    @Override
    public boolean shouldRender(Player player) {return true;} //health should always render
    
    @Override
    public void render(GuiGraphics guiGraphics) {
        super.render(guiGraphics);
    }
}
