package net.fightingpainter.mc.towntime.hud.elements;

import net.minecraft.resources.ResourceLocation;

public abstract class BaseBarElement extends BaseHudElement {
    protected float maxValue = 0.0F; //maximum value of the bar
    protected float value = 0.0F; //value of the bar

    //============================== Bar Helpers ==============================\\
    /**
     * Rounds a float value to the nearest int while only returning 0 if the unrounded value is 0 or less
     * so 0 can only be returned if the value truly is 0
     * @param value the unrounded value
     * @return the rounded value
    */
    protected int valueRound(float value) { //round value to the "nearest" int
        if (value <= 0.0f) {return 0;} //only return 0 if unrounded value is 0 (or less)
        return Math.max(1, Math.round(value)); //round value (minimum 1, so 0.1 rounds to 1)
    }

    /**
     * Calculates the fill amount of a bar based given params
     * @param value the current value of the bar
     * @param maxValue the maximum the value could be (if the bar would be full)
     * @param barLength the length of the bar (the maximum fill amount in pixels)
     * @return the number of pixels that should be filled
    */
    protected int calculateFill(float value, float maxValue, int barLength) {
        value = Math.clamp(value, 0.0f, maxValue); //clamp value
        int result = Math.round(barLength * (value / maxValue)); //calculate fill amount
        return Math.clamp(result, 0, barLength); //return fill amount (clamped just to be sure)
    }
    
    //============================== Partial Texture Bar Render Helpers ==============================\\
    //=========== Left Aligned ===========\\
    /**
     * Renders a left side aligned bar from a Partial Texture
     * @param texture The texture containing the bar
     * @param textureWidth The total width of the texture
     * @param textureHeight The total height of the texture
     * @param u The x-coordinate of the top-left corner of the bar within the texture
     * @param v The y-coordinate of the top-left corner of the bar within the texture
     * @param barLength The length of the bar
     * @param barHeight The height of the bar
     * @param fill The number of pixels the bar is filled from left to right (maximum is barLength)
     * @param x The x-coordinate where the bar should be rendered (top-left corner)
     * @param y The y-coordinate where the bar should be rendered (top-left corner)
    */
    protected void renderBarLeft(ResourceLocation texture, int textureWidth, int textureHeight, int u, int v, int barLength, int barHeight, int fill, int x, int y) {
        fill = Math.clamp(fill, 0, barLength); //clamp fill value
        renderPartialTexture(texture, textureWidth, textureHeight, u, v, fill, barHeight, x, y); //render the filled portion of the bar
    }

    /**
     * Renders a left side aligned bar from a Partial Texture
     * @param texture The texture containing the bar
     * @param textureWidth The total width of the texture
     * @param textureHeight The total height of the texture
     * @param u The x-coordinate of the top-left corner of the bar within the texture
     * @param v The y-coordinate of the top-left corner of the bar within the texture
     * @param barLength The length of the bar
     * @param barHeight The height of the bar
     * @param value The current value of the bar
     * @param maxValue The maximum value of the bar
     * @param x The x-coordinate where the bar should be rendered (top-left corner)
     * @param y The y-coordinate where the bar should be rendered (top-left corner)
    */
    protected void renderBarLeft(ResourceLocation texture, int textureWidth, int textureHeight, int u, int v, int barLength, int barHeight, float value, float maxValue, int x, int y) {
        int fill = calculateFill(value, maxValue, barLength); //calculate fill amount
        renderBarLeft(texture, textureWidth, textureHeight, u, v, barLength, barHeight, fill, x, y); //render the filled portion of the bar
    }

    /**
     * Renders the bar as left aligned from a partial texture (uses the main value and maxValue variables)
     * @param texture The texture containing the bar
     * @param textureWidth The total width of the texture
     * @param textureHeight The total height of the texture
     * @param u The x-coordinate of the top-left corner of the bar within the texture
     * @param v The y-coordinate of the top-left corner of the bar within the texture
     * @param barLength The length of the bar
     * @param barHeight The height of the bar
     * @param x The x-coordinate where the bar should be rendered (top-left corner)
     * @param y The y-coordinate where the bar should be rendered (top-left corner)
    */
    protected void renderBarLeft(ResourceLocation texture, int textureWidth, int textureHeight, int u, int v, int barLength, int barHeight, int x, int y) {
        int fill = calculateFill(this.value, this.maxValue, barLength); //calculate fill amount
        renderBarLeft(texture, textureWidth, textureHeight, u, v, barLength, barHeight, fill, x, y); //render the filled portion of the bar
    }

    //=========== Right Aligned ===========\\
    /**
     * Renders a right side aligned bar from a Partial Texture
     * @param texture The texture containing the bar
     * @param textureWidth The total width of the texture
     * @param textureHeight The total height of the texture
     * @param u The x-coordinate of the top-left corner of the bar within the texture
     * @param v The y-coordinate of the top-left corner of the bar within the texture
     * @param barLength The total length of the bar
     * @param barHeight The height of the bar
     * @param fill The number of pixels the bar is filled from right to left (maximum is barLength)
     * @param x The x-coordinate where the bar should be rendered (top-left corner)
     * @param y The y-coordinate where the bar should be rendered (top-left corner)
    */
    protected void renderBarRight(ResourceLocation texture, int textureWidth, int textureHeight, int u, int v, int barLength, int barHeight, int fill, int x, int y) {
        fill = Math.clamp(fill, 0, barLength); //clamp fill value
        u += barLength - fill; //shift u right by empty bar space (so to start from left side of the filled portion)
        x += barLength - fill; //shift x right by empty bar space (so to start from left side of the filled portion)
        renderPartialTexture(texture, textureWidth, textureHeight, u, v, fill, barHeight, x, y); //render the filled portion of the bar
    }

    /**
     * Renders a right side aligned bar from a Partial Texture
     * @param texture The texture containing the bar
     * @param textureWidth The total width of the texture
     * @param textureHeight The total height of the texture
     * @param u The x-coordinate of the top-left corner of the bar within the texture
     * @param v The y-coordinate of the top-left corner of the bar within the texture
     * @param barLength The total length of the bar
     * @param barHeight The height of the bar
     * @param value The current value of the bar
     * @param maxValue The maximum value of the bar
     * @param x The x-coordinate where the bar should be rendered (top-left corner)
     * @param y The y-coordinate where the bar should be rendered (top-left corner)
    */
    protected void renderBarRight(ResourceLocation texture, int textureWidth, int textureHeight, int u, int v, int barLength, int barHeight, float value, float maxValue, int x, int y) {
        int fill = calculateFill(value, maxValue, barLength); //calculate fill amount
        renderBarRight(texture, textureWidth, textureHeight, u, v, barLength, barHeight, fill, x, y); //render the filled portion of the bar
    }

    /**
     * Renders the bar as right aligned from a partial texture (uses the main value and maxValue variables)
     * @param texture The texture containing the bar
     * @param textureWidth The total width of the texture
     * @param textureHeight The total height of the texture
     * @param u The x-coordinate of the top-left corner of the bar within the texture
     * @param v The y-coordinate of the top-left corner of the bar within the texture
     * @param barLength The total length of the bar
     * @param barHeight The height of the bar
     * @param x The x-coordinate where the bar should be rendered (top-left corner)
     * @param y The y-coordinate where the bar should be rendered (top-left corner)
    */
    protected void renderBarRight(ResourceLocation texture, int textureWidth, int textureHeight, int u, int v, int barLength, int barHeight, int x, int y) {
        int fill = calculateFill(this.value, this.maxValue, barLength); //calculate fill amount
        renderBarRight(texture, textureWidth, textureHeight, u, v, barLength, barHeight, fill, x, y); //render the filled portion of the bar
    }

    //=========== Top Aligned ===========\\
    /**
     * Renders a top side aligned bar from a Partial Texture
     * @param texture The texture containing the bar
     * @param textureWidth The total width of the texture
     * @param textureHeight The total height of the texture
     * @param u The x-coordinate of the top-left corner of the bar within the texture
     * @param v The y-coordinate of the top-left corner of the bar within the texture
     * @param barWidth The length of the bar
     * @param barLength The height/length of the bar (as this is a vertical bar the height is the bar length)
     * @param fill The number of pixels the bar is filled from top to bottom (maximum is barLength)
     * @param x The x-coordinate where the bar should be rendered (top-left corner)
     * @param y The y-coordinate where the bar should be rendered (top-left corner)
     */
    protected void renderBarTop(ResourceLocation texture, int textureWidth, int textureHeight, int u, int v, int barWidth, int barLength, int fill, int x, int y) {
        fill = Math.clamp(fill, 0, barLength); //clamp fill value
        renderPartialTexture(texture, textureWidth, textureHeight, u, v, barWidth, fill, x, y); //render the filled portion of the bar
    }

    /**
     * Renders a top side aligned bar from a Partial Texture
     * @param texture The texture containing the bar
     * @param textureWidth The total width of the texture
     * @param textureHeight The total height of the texture
     * @param u The x-coordinate of the top-left corner of the bar within the texture
     * @param v The y-coordinate of the top-left corner of the bar within the texture
     * @param barWidth The length of the bar
     * @param barLength The height/length of the bar (as this is a vertical bar the height is the bar length)
     * @param value The current value of the bar
     * @param maxValue The maximum value of the bar
     * @param x The x-coordinate where the bar should be rendered (top-left corner)
     * @param y The y-coordinate where the bar should be rendered (top-left corner)
    */
    protected void renderBarTop(ResourceLocation texture, int textureWidth, int textureHeight, int u, int v, int barWidth, int barLength, float value, float maxValue, int x, int y) {
        int fill = calculateFill(value, maxValue, barLength); //calculate fill amount
        renderBarTop(texture, textureWidth, textureHeight, u, v, barWidth, barLength, fill, x, y); //render the filled portion of the bar
    }

    /**
     * Renders the bar as top aligned from a partial texture (uses the main value and maxValue variables)
     * @param texture The texture containing the bar
     * @param textureWidth The total width of the texture
     * @param textureHeight The total height of the texture
     * @param u The x-coordinate of the top-left corner of the bar within the texture
     * @param v The y-coordinate of the top-left corner of the bar within the texture
     * @param barWidth The length of the bar
     * @param barLength The height/length of the bar (as this is a vertical bar the height is the bar length)
     * @param x The x-coordinate where the bar should be rendered (top-left corner)
     * @param y The y-coordinate where the bar should be rendered (top-left corner)
    */
    protected void renderBarTop(ResourceLocation texture, int textureWidth, int textureHeight, int u, int v, int barWidth, int barLength, int x, int y) {
        int fill = calculateFill(this.value, this.maxValue, barLength); //calculate fill amount
        renderBarTop(texture, textureWidth, textureHeight, u, v, barWidth, barLength, fill, x, y); //render the filled portion of the bar
    }
    
    //=========== Bottom Aligned ===========\\
    /**
     * Renders a bottom side aligned bar from a Partial Texture
     * @param texture The texture containing the bar
     * @param textureWidth The total width of the texture
     * @param textureHeight The total height of the texture
     * @param u The x-coordinate of the top-left corner of the bar within the texture
     * @param v The y-coordinate of the top-left corner of the bar within the texture
     * @param barWidth The length of the bar
     * @param barLength The height/length of the bar (as this is a vertical bar the height is the bar length)
     * @param fill The number of pixels the bar is filled from bottom to top (maximum is barLength)
     * @param x The x-coordinate where the bar should be rendered (top-left corner)
     * @param y The y-coordinate where the bar should be rendered (top-left corner)
     */
    protected void renderBarBottom(ResourceLocation texture, int textureWidth, int textureHeight, int u, int v, int barWidth, int barLength, int fill, int x, int y) {
        fill = Math.clamp(fill, 0, barLength); //clamp fill value
        v += barLength - fill; //shift v down by empty bar space (so to start from top side of the filled portion)
        y += barLength - fill; //shift y down by empty bar space (so to start from top side of the filled portion)
        renderPartialTexture(texture, textureWidth, textureHeight, u, v, barWidth, fill, x, y); //render the filled portion of the bar
    }

    /**
     * Renders a bottom side aligned bar from a Partial Texture
     * @param texture The texture containing the bar
     * @param textureWidth The total width of the texture
     * @param textureHeight The total height of the texture
     * @param u The x-coordinate of the top-left corner of the bar within the texture
     * @param v The y-coordinate of the top-left corner of the bar within the texture
     * @param barWidth The length of the bar
     * @param barLength The height/length of the bar (as this is a vertical bar the height is the bar length)
     * @param value The current value of the bar
     * @param maxValue The maximum value of the bar
     * @param x The x-coordinate where the bar should be rendered (top-left corner)
     * @param y The y-coordinate where the bar should be rendered (top-left corner)
    */
    protected void renderBarBottom(ResourceLocation texture, int textureWidth, int textureHeight, int u, int v, int barWidth, int barLength, float value, float maxValue, int x, int y) {
        int fill = calculateFill(value, maxValue, barLength); //calculate fill amount
        renderBarBottom(texture, textureWidth, textureHeight, u, v, barWidth, barLength, fill, x, y); //render the filled portion of the bar
    }

    /**
     * Renders the bar as bottom aligned from a partial texture (uses the main value and maxValue variables)
     * @param texture The texture containing the bar
     * @param textureWidth The total width of the texture
     * @param textureHeight The total height of the texture
     * @param u The x-coordinate of the top-left corner of the bar within the texture
     * @param v The y-coordinate of the top-left corner of the bar within the texture
     * @param barWidth The length of the bar
     * @param barLength The height/length of the bar (as this is a vertical bar the height is the bar length)
     * @param x The x-coordinate where the bar should be rendered (top-left corner)
     * @param y The y-coordinate where the bar should be rendered (top-left corner)
    */
    protected void renderBarBottom(ResourceLocation texture, int textureWidth, int textureHeight, int u, int v, int barWidth, int barLength, int x, int y) {
        int fill = calculateFill(this.value, this.maxValue, barLength); //calculate fill amount
        renderBarBottom(texture, textureWidth, textureHeight, u, v, barWidth, barLength, fill, x, y); //render the filled portion of the bar
    }
    

    //============================== Full Texture Bar Render Helpers ==============================\\
    //=========== Left Aligned ===========\\
    /**
     * Renders a left side aligned bar from a Full Texture
     * @param texture The texture of the bar
     * @param barLength The total length of the bar
     * @param barHeight The height of the bar
     * @param fill The number of pixels the bar is filled from left to right (maximum is barLength)
     * @param x The x-coordinate where the bar should be rendered (top-left corner)
     * @param y The y-coordinate where the bar should be rendered (top-left corner)
    */
    protected void renderBarLeft(ResourceLocation texture, int barLength, int barHeight, int fill, int x, int y) {
        renderBarLeft(texture, barLength, barHeight, 0, 0, barLength, barHeight, fill, x, y); //render bar from full texture
    }

    /**
     * Renders a left side aligned bar from a Full Texture
     * @param texture The texture of the bar
     * @param barLength The total length of the bar
     * @param barHeight The height of the bar
     * @param value The current value of the bar
     * @param maxValue The maximum value of the bar
     * @param x The x-coordinate where the bar should be rendered (top-left corner)
     * @param y The y-coordinate where the bar should be rendered (top-left corner)
    */
    protected void renderBarLeft(ResourceLocation texture, int barLength, int barHeight, float value, float maxValue, int x, int y) {
        renderBarLeft(texture, barLength, barHeight, 0, 0, barLength, barHeight, value, maxValue, x, y); //render bar from full texture
    }

    /**
     * Renders the bar as left aligned from a full texture (uses the main value and maxValue variables)
     * @param texture The texture of the bar
     * @param barLength The total length of the bar
     * @param barHeight The height of the bar
     * @param x The x-coordinate where the bar should be rendered (top-left corner)
     * @param y The y-coordinate where the bar should be rendered (top-left corner)
    */
    protected void renderBarLeft(ResourceLocation texture, int barLength, int barHeight, int x, int y) {
        renderBarLeft(texture, barLength, barHeight, 0, 0, barLength, barHeight, x, y); //render bar from full texture
    }

    //=========== Right Aligned ===========\\
    /**
     * Renders a right side aligned bar from a Full Texture
     * @param texture The texture of the bar
     * @param barLength The total length of the bar
     * @param barHeight The height of the bar
     * @param fill The number of pixels the bar is filled from right to left (maximum is barLength)
     * @param x The x-coordinate where the bar should be rendered (top-left corner)
     * @param y The y-coordinate where the bar should be rendered (top-left corner)
    */
    protected void renderBarRight(ResourceLocation texture, int barLength, int barHeight, int fill, int x, int y) {
        renderBarRight(texture, barLength, barHeight, 0, 0, barLength, barHeight, fill, x, y); //render bar from full texture
    }
    
    /**
     * Renders a right side aligned bar from a Full Texture
     * @param texture The texture of the bar
     * @param barLength The total length of the bar
     * @param barHeight The height of the bar
     * @param value The current value of the bar
     * @param maxValue The maximum value of the bar
     * @param x The x-coordinate where the bar should be rendered (top-left corner)
     * @param y The y-coordinate where the bar should be rendered (top-left corner)
    */
    protected void renderBarRight(ResourceLocation texture, int barLength, int barHeight, float value, float maxValue, int x, int y) {
        renderBarRight(texture, barLength, barHeight, 0, 0, barLength, barHeight, value, maxValue, x, y); //render bar from full texture
    }

    /**
     * Renders the bar as right aligned from a full texture (uses the main value and maxValue variables)
     * @param texture The texture of the bar
     * @param barLength The total length of the bar
     * @param barHeight The height of the bar
     * @param x The x-coordinate where the bar should be rendered (top-left corner)
     * @param y The y-coordinate where the bar should be rendered (top-left corner)
    */
    protected void renderBarRight(ResourceLocation texture, int barLength, int barHeight, int x, int y) {
        renderBarRight(texture, barLength, barHeight, 0, 0, barLength, barHeight, x, y); //render bar from full texture
    }

    //=========== Top Aligned ===========\\
    /**
     * Renders a top side aligned bar from a Full Texture
     * @param texture The texture of the bar
     * @param barWidth The length of the bar
     * @param barLength The height/length of the bar (as this is a vertical bar the height is the bar length)
     * @param fill The number of pixels the bar is filled from top to bottom (maximum is barLength)
     * @param x The x-coordinate where the bar should be rendered (top-left corner)
     * @param y The y-coordinate where the bar should be rendered (top-left corner)
    */
    protected void renderBarTop(ResourceLocation texture, int barWidth, int barLength, int fill, int x, int y) {
        renderBarTop(texture, barWidth, barLength, 0, 0, barWidth, barLength, fill, x, y); //render bar from full texture
    }

    /**
     * Renders a top side aligned bar from a Full Texture
     * @param texture The texture of the bar
     * @param barWidth The length of the bar
     * @param barLength The height/length of the bar (as this is a vertical bar the height is the bar length)
     * @param value The current value of the bar
     * @param maxValue The maximum value of the bar
     * @param x The x-coordinate where the bar should be rendered (top-left corner)
     * @param y The y-coordinate where the bar should be rendered (top-left corner)
    */
    protected void renderBarTop(ResourceLocation texture, int barWidth, int barLength, float value, float maxValue, int x, int y) {
        renderBarTop(texture, barWidth, barLength, 0, 0, barWidth, barLength, value, maxValue, x, y); //render bar from full texture
    }

    /**
     * Renders the bar as top aligned from a full texture (uses the main value and maxValue variables)
     * @param texture The texture of the bar
     * @param barWidth The length of the bar
     * @param barLength The height/length of the bar (as this is a vertical bar the height is the bar length)
     * @param x The x-coordinate where the bar should be rendered (top-left corner)
     * @param y The y-coordinate where the bar should be rendered (top-left corner)
    */
    protected void renderBarTop(ResourceLocation texture, int barWidth, int barLength, int x, int y) {
        renderBarTop(texture, barWidth, barLength, 0, 0, barWidth, barLength, x, y); //render bar from full texture
    }
    
    //=========== Bottom Aligned ===========\\
    /**
     * Renders a bottom side aligned bar from a Full Texture
     * @param texture The texture of the bar
     * @param barWidth The length of the bar
     * @param barLength The height/length of the bar (as this is a vertical bar the height is the bar length)
     * @param fill The number of pixels the bar is filled from bottom to top (maximum is barLength)
     * @param x The x-coordinate where the bar should be rendered (top-left corner)
     * @param y The y-coordinate where the bar should be rendered (top-left corner)
    */
    protected void renderBarBottom(ResourceLocation texture, int barWidth, int barLength, int fill, int x, int y) {
        renderBarBottom(texture, barWidth, barLength, 0, 0, barWidth, barLength, fill, x, y); //render bar from full texture
    }

    /**
     * Renders a bottom side aligned bar from a Full Texture
     * @param texture The texture of the bar
     * @param barWidth The length of the bar
     * @param barLength The height/length of the bar (as this is a vertical bar the height is the bar length)
     * @param value The current value of the bar
     * @param maxValue The maximum value of the bar
     * @param x The x-coordinate where the bar should be rendered (top-left corner)
     * @param y The y-coordinate where the bar should be rendered (top-left corner)
    */
    protected void renderBarBottom(ResourceLocation texture, int barWidth, int barLength, float value, float maxValue, int x, int y) {
        renderBarBottom(texture, barWidth, barLength, 0, 0, barWidth, barLength, value, maxValue, x, y); //render bar from full texture
    }

    /**
     * Renders the bar as bottom aligned from a full texture (uses the main value and maxValue variables)
     * @param texture The texture of the bar
     * @param barWidth The length of the bar
     * @param barLength The height/length of the bar (as this is a vertical bar the height is the bar length)
     * @param x The x-coordinate where the bar should be rendered (top-left corner)
     * @param y The y-coordinate where the bar should be rendered (top-left corner)
    */
    protected void renderBarBottom(ResourceLocation texture, int barWidth, int barLength, int x, int y) {
        renderBarBottom(texture, barWidth, barLength, 0, 0, barWidth, barLength, x, y); //render bar from full texture
    }


    //============================== Value Text Renderer ==============================\\
    /**
     * Renders the value of the bar as text
     * @param value The current value of the bar
     * @param maxValue The maximum value of the bar
     * @param color The color of the text
     * @param scale The scale of the text
     * @param x The x-coordinate where the text should be rendered (top-left corner)
     * @param y The y-coordinate where the text should be rendered (top-left corner)
     * @param barWidth The width of the bar
     * @param barHeight The height of the bar
    */
    protected void renderIntValueText(int value, int maxValue, int color, float scale, int x, int y, int barWidth, int barHeight) {
        TextElement valueText = new TextElement(value+"/"+maxValue);
        valueText.alignCenter(); //center align
        valueText.setScale(scale); //scale down
        valueText.setYShift(1); //shift down
        renderText(valueText, x+(barWidth/2), y+(barHeight/2)); //render text
    }

    /**
     * Renders the value of the bar as text
     * @param value The current value of the bar (has to be rounded beforehand)
     * @param maxValue The maximum value of the bar (has to be rounded beforehand)
     * @param color The color of the text
     * @param scale The scale of the text
     * @param x The x-coordinate where the text should be rendered (top-left corner)
     * @param y The y-coordinate where the text should be rendered (top-left corner)
     * @param barWidth The width of the bar
     * @param barHeight The height of the bar
    */
    protected void renderFloatValueText(float value, float maxValue, int color, float scale, int x, int y, int barWidth, int barHeight) {
        TextElement valueText = new TextElement(value+"/"+maxValue);
        valueText.alignCenter(); //center align
        valueText.setScale(scale); //scale down
        valueText.setYShift(1); //shift down
        renderText(valueText, x+(barWidth/2), y+(barHeight/2)); //render text
    }
}