package net.fightingpainter.mc.towntime.hud.bars;

import cpw.mods.modlauncher.api.ITransformationService.Resource;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

/**
 * the base class for all bars 
*/
public class BaseBar {
    public int x = 0;
    public int y = 0;
    public int length = 100;
    public int height = 6;
    public ResourceLocation texture;
    public ResourceLocation backgroundTexture;
    public float maxValue = 100;
    public float value = 0;

    public void getParameters(Player player) {
    }

    public boolean shouldRender(Player player) {
        return false;
    }

    /**
     * Renders the bar on the screen.
     * @param guiGraphics The graphics object to render with.
    */
    public void render(GuiGraphics guiGraphics) {
        if (texture == null) {
            return; // No texture to render
        }

        renderBackground(guiGraphics);
        renderBar(guiGraphics);
        renderValue(guiGraphics);
    }

    /**
     * Renders the filled portion of the bar based on the current value.
     * @param guiGraphics The graphics object to render with.
    */
    private void renderBar(GuiGraphics guiGraphics) {
        int fill = (int) ((value / maxValue) * length); // Calculate the length of the filled portion of the bar
        guiGraphics.blit(texture, x, y, 0, 0, fill, height, length, height); // Render the filled portion of the bar
    }

    /**
     * Renders the background of the bar.
     * @param guiGraphics The graphics object to render with.
    */
    private void renderBackground(GuiGraphics guiGraphics) {
        guiGraphics.blit(backgroundTexture, x, y, 0, 0, length, height, length, height); // Render the background of the bar
    }

    /**
     * Renders the text (<value>/<maxValue>) on top of the bar.
     * @param guiGraphics The graphics object to render with.
    */
    private void renderValue(GuiGraphics guiGraphics) {//the value/maxValue text ontop of the bar
        String text = (int) value + "/" + (int) maxValue; // Create the text to render
        int textWidth = guiGraphics.getStringWidth(text); // Calculate the width of the text
        int textX = x + (length / 2) - (textWidth / 2); // Calculate the x position of the text
        int textY = y + (height / 2) - 4; // Calculate the y position of the text
        guiGraphics.drawString(text, textX, textY, 0xFFFFFF); // Render the text
    }
}
