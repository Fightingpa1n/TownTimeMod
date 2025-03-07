package net.fightingpainter.mc.towntime.hud.bars;

import net.fightingpainter.mc.towntime.TownTime;
import net.fightingpainter.mc.towntime.hud.BaseHudElement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import toughasnails.api.temperature.ITemperature;
import toughasnails.api.temperature.TemperatureHelper;
import toughasnails.api.temperature.TemperatureLevel;

public class TemperatureDisplay extends BaseHudElement {
    private final static ResourceLocation TEMPERATURE = ResourceLocation.fromNamespaceAndPath(TownTime.MOD_ID, "textures/hud/temperature.png");
    private final static ResourceLocation BACKGROUND = ResourceLocation.fromNamespaceAndPath(TownTime.MOD_ID, "textures/hud/temperature_background.png");
    private int textureOffset = 0;

    public TemperatureDisplay(int x, int y) {super(x, y);} //constructor
    
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
    public void render(GuiGraphics graphics) {
        renderSimpleTexture(graphics, BACKGROUND, 16, 16, getX(), getY()); //render background
        graphics.blit(TEMPERATURE, getX(), getY(), textureOffset*16, 0, 16, 16); //render temperature icon
    }
}
