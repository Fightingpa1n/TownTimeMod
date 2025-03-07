package net.fightingpainter.mc.towntime.hud.bars;

import net.fightingpainter.mc.towntime.TownTime;
import net.fightingpainter.mc.towntime.hud.BaseBarElement;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public class HungerBar extends BaseBarElement{
    private final static ResourceLocation NORMAL = ResourceLocation.fromNamespaceAndPath(TownTime.MOD_ID, "textures/hud/hunger.png");
    private final static ResourceLocation BACKGROUND = ResourceLocation.fromNamespaceAndPath(TownTime.MOD_ID, "textures/hud/hunger_background.png");
    
    public HungerBar(int x, int y) {super(x, y);} //constructor

    @Override
    public boolean shouldRender(Player player) {//check gamemode
        if (player.isCreative() || player.isSpectator()) {return false;}
        else {return true;}
    }

    @Override
    public void getParameters(Player player) {
        this.value = player.getFoodData().getFoodLevel();
        this.maxValue = 20;
    }
    
    @Override
    public void render(GuiGraphics graphics) {
        renderSimpleTexture(graphics, BACKGROUND, 68, 9, getX(), getY()); //render background
        renderBarRight(graphics, NORMAL, 60, 3, getX()+1, getY()+3); //render hunger bar
    }
}
