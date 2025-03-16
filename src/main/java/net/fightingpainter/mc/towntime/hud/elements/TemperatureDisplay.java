package net.fightingpainter.mc.towntime.hud.elements;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import toughasnails.api.temperature.ITemperature;
import toughasnails.api.temperature.TemperatureHelper;
import toughasnails.api.temperature.TemperatureLevel;

import net.fightingpainter.mc.towntime.TownTime;


public class TemperatureDisplay extends BaseHudElement {
    private final static ResourceLocation TEMPERATURE_TEXTURE = ResourceLocation.fromNamespaceAndPath(TownTime.MOD_ID, "textures/hud/temperature.png");
    private final static ResourceLocation BACKGROUND_TEXTURE = ResourceLocation.fromNamespaceAndPath(TownTime.MOD_ID, "textures/hud/temperature_background.png");
    private int textureOffset = 0;

    public TemperatureDisplay() {
        this.width = 16;
        this.height = 16;
    }
    
    @Override
    public boolean shouldRender(Player player) {//check gamemode
        if (player.isCreative() || player.isSpectator()) {return false;}
        else {return true;}
    }

    @Override
    public void getParameters(Player player) {
        ITemperature temperatureStats = TemperatureHelper.getTemperatureData(player);
        switch (temperatureStats.getLevel()) {
            case TemperatureLevel.ICY:
                textureOffset = 0;
                break;
            case TemperatureLevel.COLD:
                textureOffset = 1;
                break;
            case TemperatureLevel.NEUTRAL:
                textureOffset = 2;
                break;
            case TemperatureLevel.WARM:
                textureOffset = 3;
                break;
            case TemperatureLevel.HOT:
                textureOffset = 4;
                break;
        }
    }

    @Override
    public void render() {
        renderSimpleTexture(BACKGROUND_TEXTURE, 16, 16, x, y); //render background
        renderPartialTexture(TEMPERATURE_TEXTURE, 80, 16, 16*textureOffset, 0, 16, 16, x, y);
    }
}
