package net.fightingpainter.mc.towntime.hud;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public abstract class BaseBarElement extends BaseHudElement {
    protected float maxValue = 0.0F; //max value of the bar
    protected float value = 0.0F; //value of the bar
    
    /**
     * Renders the filled portion of the bar based on the current value.
     * @param texture The texture to render the bar with.
     * @param barLength The length of the bar.
     * @param barHeight The height of the bar.
     * @param x The x position of the bar.
     * @param y The y position of the bar.
    */
    public void renderBarLeft(ResourceLocation texture, int barLength, int barHeight, int x, int y) {
        int fill = (int) (barLength * (value / maxValue)); //fill amount
        graphics.blit(texture, x, y, 0, 0, fill, barHeight, barLength, barHeight); //Render the filled portion of the bar
    }

    /**
     * Renders the filled portion of the bar based on the current value.
     * @param texture The texture to render the bar with.
     * @param barLength The length of the bar.
     * @param barHeight The height of the bar.
     * @param x The x position of the bar.
     * @param y The y position of the bar.
    */
    public void renderBarRight(ResourceLocation texture, int barLength, int barHeight, int x, int y) {
        int fill = (int) (barLength * (value / maxValue)); //fill amount
        graphics.blit(texture, x + barLength - fill, y, barLength - fill, 0, fill, barHeight, barLength, barHeight); //Render the filled portion of the bar
    }

    

}