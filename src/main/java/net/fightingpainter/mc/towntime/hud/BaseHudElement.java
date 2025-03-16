package net.fightingpainter.mc.towntime.hud;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

/**
 * Base Hud Element
 * Used to create custom hud elements
 * Contains a bunch of render Helper methods
 * @see #shouldRender(Player player)
 * @see #getParameters(Player player)
 * @see #render()
*/
public abstract class BaseHudElement {
    protected GuiGraphics graphics; //graphics instance
    protected int width, height; //width and height
    protected int x, y; //x and y position

    //============================== Subclass Methods ==============================\\
    //=========== Abstract Methods ===========\\
    /**
     * Check if the component should render
     * @param player the player to get the parameters and stuff from
     * @return if the component should render
    */
    public abstract boolean shouldRender(Player player); //should render method
    
    /**
     * Get the parameters of the component
     * @param player the player to get the parameters and stuff from
     */
    public abstract void getParameters(Player player); //get parameters method
    
    /**
     * Render the component
     * Rendering should use the graphics instance to render stuff
     * the element should be rendered at the x and y position
     * the parametrs should be private variables in the subclass and updated in the getParameters method
     * @see #getParameters(Player player)
    */
    public abstract void render(); //render method
    
    //=========== Rendering ===========\\
    /**
     * Render Logic for the element (get's parameters and renders only if shouldRender is true)
     * Positioning should be done in the hud renderer using the setPos method or using the alternate renderElement method
     * @param graphics the GuiGraphics instance to render stuff
     * @param player the player to get the parameters and stuff from
     * @see #setPos(int x, int y)
     * @see #renderElement(GuiGraphics graphics, Player player, int x, int y)
    */
    public void renderElement(GuiGraphics graphics, Player player) {
        if (shouldRender(player)) { //if should render is true
            this.graphics = graphics; //update graphics instance
            getParameters(player); //get parameters from player
            render(); //render the element
        }
    }

    /**
     * Render Logic for the element (get's parameters and renders only if shouldRender is true)
     * @param graphics the GuiGraphics instance to render stuff
     * @param player the player to get the parameters and stuff from
     * @param x the x position of the element
     * @param y the y position of the element
    */
    public void renderElement(GuiGraphics graphics, Player player, int x, int y) {
        setPos(x, y); //set the position
        renderElement(graphics, player); //render element logic
    }
    

    //============================== Positioning / Sizing ==============================\\
    //=========== Positioning ===========\\
    /**
     * Set the position of the Element (used in HudRenderer)
     * @param x the x position
     * @param y the y position
    */
    public void setPos(int x, int y) {
        this.x = x;
        this.y = y;
    }

    //=========== Sizing ===========\\
    /**
     * Get's the Elements Width (width needs to be set by subclass)
     * @return the Width
    */
    public int getWidth() {return width;}

    /**
     * Get's the Elements Height (height needs to be set by subclass)
     * @return the Height
    */
    public int getHeight() {return height;}
    

    //============================== Renderer Helpers ==============================\\
    //=========== General Rendering ===========\\
    /**
     * render something on a different zIndex
     * @param zIndex the zIndex to render on
     * @param render the render logic
    */
    public void zIndex(int zIndex, Runnable render) {
        graphics.pose().pushPose(); //push pose
        graphics.pose().translate(0, 0, zIndex); //translate
        render.run(); //run the render logic
        graphics.pose().popPose(); //pop pose
    }

    //=========== Texture Rendering ===========\\
    /**
     * Render a simple texture with the given width and height
     * @param texture the texture to render
     * @param width the width of the texture
     * @param height the height of the texture
     * @param x the x position
     * @param y the y position
     * @see renderSimpleTexture(ResourceLocation texture, int width, int height, int x, int y, int zIndex) with zIndex
     */
    public void renderSimpleTexture(ResourceLocation texture, int width, int height, int x, int y) {
        graphics.blit(texture, x, y, 0, 0, width, height, width, height);
    }
    
    /**
     * Render a simple texture with the given width and height and zIndex
     * @param texture the texture to render
     * @param width the width of the texture
     * @param height the height of the texture
     * @param x the x position
     * @param y the y position
     * @param zIndex the zIndex of the texture
     * @see renderSimpleTexture(ResourceLocation texture, int width, int height, int x, int y) without zIndex
    */
    public void renderSimpleTexture(ResourceLocation texture, int width, int height, int x, int y, int zIndex) {
        zIndex(zIndex, () -> renderSimpleTexture(texture, width, height, x, y)); //render the texture with zIndex
    }

    //=========== Text ===========\\
    /**
     * get's the font instance for rendering text
     * @return the font instance
    */
    public Font getFont() {
        return Minecraft.getInstance().font;
    }
}
