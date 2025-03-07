package net.fightingpainter.mc.towntime.hud;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public abstract class BaseHudElement {
    private final int xOffset, yOffset; //x and y offset
    private int x, y; //x and y position

    public BaseHudElement(int xOffset, int yOffset) {
        this.xOffset = xOffset;
        this.yOffset = yOffset;
    }

    //=========== Abstract Methods ===========\\
    /**
     * Render the component 
     * @param graphics the GuiGraphics instance to render stuff
    */
    public abstract void render(GuiGraphics graphics); //render method

    /**
     * Get the parameters of the component
     * @param player the player to get the parameters and stuff from
    */
    public abstract void getParameters(Player player); //get parameters method

    /**
     * Check if the component should render
     * @param player the player to get the parameters and stuff from
     * @return if the component should render
    */
    public abstract boolean shouldRender(Player player); //should render method
    
    //=========== Rendering ===========\\
    /**
     * Render Logic for the element (render if shouldRender is true)
     * @param graphics the GuiGraphics instance to render stuff
     * @param player the player to get the parameters and stuff from
     * @param x the x position
     * @param y the y position
    */
    public void renderElement(GuiGraphics graphics, Player player, int x, int y) {
        if (shouldRender(player)) {
            setPos(x, y);
            getParameters(player);
            render(graphics);
        }
    }

    //=========== Positioning ===========\\
    /**
     * Set the position of the component
     * @param x the x position
     * @param y the y position
    */
    public void setPos(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Get's the Base X position which is the X position with the offset 
     * @return the X Pos (x + xOffset)
    */
    public int getX() {return x + xOffset;} //get X Base position

    /**
     * Get's the Base Y position which is the Y position with the offset 
     * @return the Y Pos (y + yOffset)
    */
    public int getY() {return y + yOffset;} //get Y Base position


    //=========== Renderer Helpers ===========\\
    /**
     * Render a simple texture with the given width and height
     * @param graphics the GuiGraphics instance to render stuff
     * @param texture the texture to render
     * @param width the width of the texture
     * @param height the height of the texture
     * @param x the x position
     * @param y the y position
     */
    public void renderSimpleTexture(GuiGraphics graphics, ResourceLocation texture, int width, int height, int x, int y) {
        graphics.blit(texture, x, y, 0, 0, width, height, width, height);
    }
    
    /**
     * Render a simple texture with the given width and height and zIndex
     * @param graphics the GuiGraphics instance to render stuff
     * @param texture the texture to render
     * @param width the width of the texture
     * @param height the height of the texture
     * @param x the x position
     * @param y the y position
     * @param zIndex the zIndex of the texture
    */
    public void renderSimpleTexture(GuiGraphics graphics, ResourceLocation texture, int width, int height, int x, int y, int zIndex) {
        graphics.pose().pushPose();
        graphics.pose().translate(0, 0, zIndex);
        renderSimpleTexture(graphics, texture, x, y, width, height);
        graphics.pose().popPose();
    }

    /**
     * Render a simple texture with the given width and height and zIndex
     * @param graphics the GuiGraphics instance to render stuff
     * @param texture the texture to render
     * @param textureStartX the x position on the texture we want to start rendering from
     * @param textureStartY the y position on the texture we want to start rendering from
     * @param textureWidth the width of the texture we want to render
     * @param textureHeight the height of the texture we want to render
     * @param width the width of the element
     * @param height the height of the element
     * @param x the x position
     * @param y the y position
    */
    public void renderSimpleTexture(GuiGraphics graphics, ResourceLocation texture, int textureStartX, int textureStartY, int textureWidth, int textureHeight, int width, int height, int x, int y) {
        graphics.blit(texture, x, y, textureStartX, textureStartY, textureWidth, textureHeight, width, height);
    }

    /**
     * Render a simple texture with the given width and height and zIndex
     * @param graphics the GuiGraphics instance to render stuff
     * @param texture the texture to render
     * @param textureStartX the x position on the texture we want to start rendering from
     * @param textureStartY the y position on the texture we want to start rendering from
     * @param textureWidth the width of the texture we want to render
     * @param textureHeight the height of the texture we want to render
     * @param width the width of the element
     * @param height the height of the element
     * @param x the x position
     * @param y the y position
     * @param zIndex the zIndex of the texture
    */
    public void renderSimpleTexture(GuiGraphics graphics, ResourceLocation texture, int textureStartX, int textureStartY, int textureWidth, int textureHeight, int width, int height, int x, int y, int zIndex) {
        graphics.pose().pushPose();
        graphics.pose().translate(0, 0, zIndex);
        renderSimpleTexture(graphics, texture, textureStartX, textureStartY, textureWidth, textureHeight, width, height, x, y);
        graphics.pose().popPose();
    }

    /**
     * get's the font instance for rendering text
     * @return the font instance
    */
    public Font getFont() {
        return Minecraft.getInstance().font;
    }
}
