package net.fightingpainter.mc.towntime.hud.bars;

import net.fightingpainter.mc.towntime.TownTime;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import toughasnails.api.temperature.ITemperature;
import toughasnails.api.temperature.TemperatureHelper;
import toughasnails.api.temperature.TemperatureLevel;

public class TemperatureDisplay {
    // private final static ResourceLocation TEMPERATURE = ResourceLocation.fromNamespaceAndPath(TownTime.MOD_ID, "textures/hud/temperature.png");
    private int x;
    private int y;
    private String temperature;

    public TemperatureDisplay(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void getParameters(Player player) {
        ITemperature temperatureStats = TemperatureHelper.getTemperatureData(player);
        switch (temperatureStats.getLevel()) {
            case TemperatureLevel.ICY:
                temperature = "ICY";
                break;
            case TemperatureLevel.COLD:
                temperature = "COLD";
                break;
            case TemperatureLevel.NEUTRAL:
                temperature = "NEUTRAL";
                break;
            case TemperatureLevel.WARM:
                temperature = "WARM";
                break;
            case TemperatureLevel.HOT:
                temperature = "HOT";
                break;
        }
    }

    public boolean shouldRender(Player player) {
        return true;
    }
    
    public void render(GuiGraphics guiGraphics) {
        if (temperature == null) {return;}
        Font font = Minecraft.getInstance().font;
        guiGraphics.drawString(font, temperature, x, y, 0xFFFFFF);
    }
}
