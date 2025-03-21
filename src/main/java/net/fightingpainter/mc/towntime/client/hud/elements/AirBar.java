package net.fightingpainter.mc.towntime.client.hud.elements;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import net.fightingpainter.mc.towntime.TownTime;


public class AirBar extends BaseBarElement {
    private final static ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(TownTime.MOD_ID, "textures/hud/air.png");

    public AirBar() { //set size
        this.width = 130;
        this.height = 11;
    }

    @Override
    public boolean shouldRender(Player player) {
        if (player.isCreative() || player.isSpectator()) {return false;} //check gamemode
        if (player.getAirSupply() >= player.getMaxAirSupply()) {return false;} //check air supply
        return true;
    }

    @Override
    public void getParameters(Player player) {
        this.value = player.getAirSupply();
        this.maxValue = player.getMaxAirSupply();
    }

    @Override
    public void render() {
        int textureWidth = 130; //set texture width
        int textureHeight = 22; //set texture height
        renderPartialTexture(TEXTURE, textureWidth, textureHeight, 0, 0, 130, 11, x, y); //render air bar background
        renderBarLeft(TEXTURE, textureWidth, textureHeight, 9, 14, 120, 5, x+9, y+3); //render air bar
    }
}
