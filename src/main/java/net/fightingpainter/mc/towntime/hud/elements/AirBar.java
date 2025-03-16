package net.fightingpainter.mc.towntime.hud.elements;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import net.fightingpainter.mc.towntime.TownTime;


public class AirBar extends BaseBarElement {
    private final static ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(TownTime.MOD_ID, "textures/hud/air.png");
    private final static ResourceLocation BACKGROUND_TEXTURE = ResourceLocation.fromNamespaceAndPath(TownTime.MOD_ID, "textures/hud/air_background.png");

    public AirBar() { //set size
        this.width = 68;
        this.height = 9;
    }

    @Override
    public boolean shouldRender(Player player) {
        return player.isUnderWater();
    }

    @Override
    public void getParameters(Player player) {
        this.value = player.getAirSupply();
        this.maxValue = player.getMaxAirSupply();
    }

    @Override
    public void render() {
        renderSimpleTexture(BACKGROUND_TEXTURE, 68, 9, x, y);
        renderBarRight(TEXTURE, 60, 3, x+7, y+3);
    }
    
}
