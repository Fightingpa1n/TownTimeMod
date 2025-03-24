package net.fightingpainter.mc.towntime.client.hud.elements;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.player.Player;

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.systems.RenderSystem;

import net.fightingpainter.mc.towntime.client.hud.elements.TextElement.*;
import net.fightingpainter.mc.towntime.util.Txt;


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

    /**
     * get's called every tick (can be used to update stuff) (optional)
    */
    public void tick() {}

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
    protected void zIndex(int zIndex, Runnable render) {
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
     * @param x The x-coordinate where the texture should be rendered
     * @param y The y-coordinate where the texture should be rendered
     * @see renderSimpleTexture(ResourceLocation texture, int width, int height, int x, int y, int zIndex) with zIndex
     */
    protected void renderSimpleTexture(ResourceLocation texture, int width, int height, int x, int y) {
        graphics.blit(texture, x, y, 0, 0, width, height, width, height);
    }

    /**
     * Render a partial texture with the given width and height
     * @param texture the texture to render
     * @param textureWidth the width of the texture
     * @param textureHeight the height of the texture
     * @param u The x-coordinate of the top-left corner of the specific part of the texture to render
     * @param v The y-coordinate of the top-left corner of the specific part of the texture to render
     * @param width the width of the part of the texture to render
     * @param height the height of the part of the texture to render
     * @param x The x-coordinate where the texture should be rendered
     * @param y The y-coordinate where the texture should be rendered
     * @see renderPartialTexture(ResourceLocation texture, int textureWidth, int textureHeight, int u, int v, int width, int height, int x, int y, int zIndex) with zIndex
    */
    protected void renderPartialTexture(ResourceLocation texture, int textureWidth, int textureHeight, int u, int v, int width, int height, int x, int y) {
        graphics.blit(texture, x, y, (float)u, (float)v, width, height, textureWidth, textureHeight);
    }

    //=========== Text ===========\\ (text helpers don't have zIndex variants as that would be way to many helper methods)
    /**
     * Render a TextElement at the given position
     * @param textElement the text element to render
     * @see TextElement
    */
    protected void renderText(TextElement textElement, int x, int y) {
        textElement.render(graphics, x, y);
    }

    /**
     * Renders a new TextElement at the given position
     * @param text the text to render
     * @param font the font to render the text with
     * @param color the color of the text
     * @param horizontalAlignment the horizontal alignment of the text
     * @param verticalAlignment the vertical alignment of the text
     * @param x the x position of the text
     * @param y the y position of the text
    */
    protected void renderText(String text, Font font, int color, AlignH horizontalAlignment, AlignV verticalAlignment, int x, int y) {
        renderText(new TextElement(text, font, color, horizontalAlignment, verticalAlignment), x, y);
    }

    //=========== Default Aligned Text ===========\\
    /**
     * Renders a new TextElement at the given position with default alignment (Left-Centered)
     * @param text the text to render
     * @param font the font to render the text with
     * @param color the color of the text
     * @param x the x position of the text
     * @param y the y position of the text
    */
    protected void renderText(String text, Font font, int color, int x, int y) {
        renderText(new TextElement(text, font, color), x, y);
    }

    /**
     * Renders a new TextElement at the given position with default settings (default font, white, left-centered)
     * @param text the text to render
     * @param x the x position of the text
     * @param y the y position of the text
    */
    protected void renderText(String text, int x, int y) {
        renderText(new TextElement(text, Minecraft.getInstance().font), x, y);
    }

    //=========== Center Aligned Text ===========\\
    /**
     * Renders a new TextElement at the given position with center alignment
     * @param text the text to render
     * @param font the font to render the text with
     * @param color the color of the text
     * @param x the x position of the text
     * @param y the y position of the text
    */
    protected void renderCenteredText(String text, Font font, int color, int x, int y) {
        renderText(new TextElement(text, font, color, AlignH.CENTER, AlignV.CENTER), x, y);
    }

    /**
     * Renders a new TextElement at the given position with center alignment, default font and color
     * @param text the text to render
     * @param x the x position of the text
     * @param y the y position of the text
    */
    protected void renderCenteredText(String text, int x, int y) {
        renderText(new TextElement(text, Minecraft.getInstance().font, Txt.DEFAULT, AlignH.CENTER, AlignV.CENTER), x, y);
    }

    //=========== Alpha ===========\\
    /**
     * Enable Alpha Blending with the given alpha value
     * @param alpha the alpha value to enable
    */
    protected void enableAlpha(float alpha) {
        RenderSystem.enableBlend();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, alpha);
        RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
    }

    /** Disable Alpha Blending */
    protected void disableAlpha() {
        RenderSystem.disableBlend();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    }
    

    //============================== Sounds ==============================\\
    //=========== Start ===========\\
    /**
     * Play a sound with the given volume and pitch
     * @param sound the sound to play
     * @param volume the volume to play the sound with
     * @param pitch  the pitch to play the sound with
     * @return the sound instance
    */
    protected SoundInstance playSound(SoundEvent sound, float volume, float pitch) {
        SoundInstance instance = SimpleSoundInstance.forUI(sound, volume, pitch); //create the sound instance
        getSoundManager().play(instance); //play the sound
        return instance; //return the sound instance
    }

    /**
     * Play a sound with the given volume and default pitch (1.0f)
     * @param sound the sound to play
     * @param volume the volume to play the sound with
     * @return the sound instance
    */
    protected SoundInstance playSound(SoundEvent sound, float volume) {
        return playSound(sound, volume, 1.0f);
    }

    /**
     * Play a sound with the default volume and pitch (both 1.0f)
     * @param sound the sound to play
     * @return the sound instance
    */
    protected SoundInstance playSound(SoundEvent sound) {return playSound(sound, 1.0f, 1.0f);}

    //=========== Stop ===========\\
    /**
     * Stop a sound instance
     * @param soundInstance the sound instance to stop
    */
    protected void stopSound(SoundInstance soundInstance) {getSoundManager().stop(soundInstance);}




    //============================== Other ==============================\\
    /**
     * Get the current Minecraft Instance (I would say the client instance but I'm not sure so minecraft instance it is)
     * @return the Minecraft Instance
    */
    protected Minecraft getMinecraft() {return Minecraft.getInstance();}

    /**
     * Get the SoundManager (assuming client side only)
     * @return the SoundManager
    */
    protected SoundManager getSoundManager() {return getMinecraft().getSoundManager();}
    
    /**
     * Get the current World (assuming client side only)
     * @return the World
    */
    protected ClientLevel getWorld() {return getMinecraft().level;}
    
    /**
     * Get the current Player (assuming client side only)
     * @return the Player
    */
    protected Player getPlayer() {return getMinecraft().player;}
    
    /**
     * Get the current GameMode (assuming client side only)
     * @return the GameMode
    */
    protected MultiPlayerGameMode getGameMode() {return getMinecraft().gameMode;}
        
    /**
     * get the Default Font
     * @return default font
    */
    protected Font getFont() {return getMinecraft().font;}
}
