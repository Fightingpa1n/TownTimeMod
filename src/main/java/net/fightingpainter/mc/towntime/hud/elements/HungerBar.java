package net.fightingpainter.mc.towntime.hud.elements;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import net.fightingpainter.mc.towntime.TownTime;


public class HungerBar extends BaseBarElement{
    private final static ResourceLocation NORMAL_TEXTURE = ResourceLocation.fromNamespaceAndPath(TownTime.MOD_ID, "textures/hud/hunger.png");
    private final static ResourceLocation BACKGROUND_TEXTURE = ResourceLocation.fromNamespaceAndPath(TownTime.MOD_ID, "textures/hud/hunger_background.png");
    
    public HungerBar() { //set size
        this.width = 68;
        this.height = 9;
    }

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
    public void render() {
        renderSimpleTexture(BACKGROUND_TEXTURE, 68, 9, x, y); //render background
        renderBarLeft(NORMAL_TEXTURE, 60, 3, x+7, y+3); //render hunger bar
    }
}
