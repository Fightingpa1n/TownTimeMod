package net.fightingpainter.mc.towntime.hud.elements;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;

import net.fightingpainter.mc.towntime.util.Txt;


/**
 * A Helper Class for Text Parts of Elements.
 * (it's a helper class instead of helper methods in the base element, as that would have been way to many methods) 
*/
public class TextElement {
    private String text; //the text to render
    private Font font; //the font to render the text with
    private int color; //the color of the text
    private AlignH horizontalAlignment; //the horizontal alignment of the text
    private AlignV verticalAlignment; //the vertical alignment of the text


    //============================== Constructors ==============================\\
    /**
     * creates a new TextElement
     * @param text the text to render
     * @param font the font to render the text with
     * @param color the color of the text
     * @param horizontalAlignment the horizontal alignment of the text
     * @param verticalAlignment the vertical alignment of the text
    */
    public TextElement(String text, Font font, int color, AlignH horizontalAlignment, AlignV verticalAlignment) {
        this.text = text;
        this.font = font;
        this.color = color;
        this.horizontalAlignment = horizontalAlignment;
        this.verticalAlignment = verticalAlignment;
    }

    /**
     * creates a new TextElement With default vertical alignment (Centered)
     * @param text the text to render
     * @param font the font to render the text with
     * @param color the color of the text
     * @param horizontalAlignment the horizontal alignment of the text
    */
    public TextElement(String text, Font font, int color, AlignH horizontalAlignment) {
        this(text, font, color, horizontalAlignment, AlignV.CENTER);
    }

    /**
     * creates a new TextElement with default alignment (Left-Centered)
     * @param text the text to render
     * @param font the font to render the text with
     * @param color the color of the text
    */
    public TextElement(String text, Font font, int color) {
        this(text, font, color, AlignH.LEFT, AlignV.CENTER);
    }

    /**
     * creates a new TextElement with default color (White) and alignment (Left-Centered)
     * @param text the text to render
     * @param font the font to render the text with
    */
    public TextElement(String text, Font font) {
        this(text, font, Txt.DEFAULT, AlignH.LEFT, AlignV.CENTER);
    }

    /**
     * creates a new TextElement with default font (Minecraft's default font) and alignment (Left-Centered)
     * @param text the text to render
     * @param color the color of the text
    */
    public TextElement(String text, int color) {
        this(text, Minecraft.getInstance().font, color, AlignH.LEFT, AlignV.CENTER);
    }

    /**
     * creates a new TextElement with default settings (default font, white, left-centered)
     * @param text the text to render
    */
    public TextElement(String text) {
        this(text, Minecraft.getInstance().font, Txt.DEFAULT, AlignH.LEFT, AlignV.CENTER);
    }


    //============================== Setters ==============================\\
    /**
     * sets the text of the TextElement
     * @param text The text it should use
     * @return itself (for chaining)
    */
    public TextElement setText(String text) {
        this.text = text;
        return this;
    }

    /**
     * sets the font of the TextElement
     * @param font The font it should use
     * @return itself (for chaining)
    */
    public TextElement setFont(Font font) {
        this.font = font;
        return this;
    }

    /**
     * sets the color of the TextElement
     * @param color The color it should use
     * @return itself (for chaining)
    */
    public TextElement setColor(int color) {
        this.color = color;
        return this;
    }

    
    //============================== Alignment ==============================\\
    //=========== Get ===========\\
    /**
     * Gets the Horizontal Alignment of the TextElement 
     * @return The Horizontal Alignment it currently uses
    */
    public AlignH getAlignHorizontal() {return horizontalAlignment;}

    /**
     * Gets the Vertical Alignment of the TextElement
     * @return The Vertical Alignment it currently uses
    */
    public AlignV getAlignVertical() {return verticalAlignment;}

    //=========== Horizontal ===========\\
    /**
     * sets the Horizontal Alignment of the TextElement
     * @param horizontalAlignment The Horizontal Alignment it should use
     * @return itself (for chaining)
    */
    public TextElement setAlignHorizontal(AlignH horizontalAlignment) {
        this.horizontalAlignment = horizontalAlignment;
        return this;
    }

    /**
     * sets the Horizontal Alignment of the TextElement to Left
     * @return itself (for chaining)
    */
    public TextElement alignHorizontalLeft() {return setAlignHorizontal(AlignH.LEFT);}

    /**
     * sets the Horizontal Alignment of the TextElement to Center
     * @return itself (for chaining)
    */
    public TextElement alignHorizontalCenter() {return setAlignHorizontal(AlignH.CENTER);}

    /**
     * sets the Horizontal Alignment of the TextElement to Right
     * @return itself (for chaining)
    */
    public TextElement alignHorizontalRight() {return setAlignHorizontal(AlignH.RIGHT);}

    //=========== Vertical ===========\\
    /**
     * sets the Vertical Alignment of the TextElement
     * @param verticalAlignment The Vertical Alignment it should use
     * @return itself (for chaining)
    */
    public TextElement setAlignVertical(AlignV verticalAlignment) {
        this.verticalAlignment = verticalAlignment;
        return this;
    }

    /**
     * sets the Vertical Alignment of the TextElement to Bottom
     * @return itself (for chaining)
    */
    public TextElement alignVerticalTop() {return setAlignVertical(AlignV.TOP);}

    /**
     * sets the Vertical Alignment of the TextElement to Center
     * @return itself (for chaining)
    */
    public TextElement alignVerticalCenter() {return setAlignVertical(AlignV.CENTER);}

    /**
     * sets the Vertical Alignment of the TextElement to Bottom
     * @return itself (for chaining)
    */
    public TextElement alignVerticalBottom() {return setAlignVertical(AlignV.BOTTOM);}

    //=========== Both ===========\\
    /**
     * sets the Alignment of the TextElement
     * @param horizontalAlignment The Horizontal Alignment it should use
     * @param verticalAlignment The Vertical Alignment it should use
     * @return itself (for chaining)
    */
    public TextElement setAlign(AlignH horizontalAlignment, AlignV verticalAlignment) {
        setAlignHorizontal(horizontalAlignment);
        setAlignVertical(verticalAlignment);
        return this;
    }

    /**
     * sets the Alignment of the TextElement to Left (left+centered)
     * @return itself (for chaining)
    */
    public TextElement alignLeft() {return setAlign(AlignH.LEFT, AlignV.CENTER);}

    /**
     * sets the Alignment of the TextElement to Center
     * @return itself (for chaining)
    */
    public TextElement alignCenter() {return setAlign(AlignH.CENTER, AlignV.CENTER);}
    
    /**
     * sets the Alignment of the TextElement to Right (right+centered)
     * @return itself (for chaining)
    */
    public TextElement alignRight() {return setAlign(AlignH.RIGHT, AlignV.CENTER);}
    

    //============================== Rendering ==============================\\
    public void render (GuiGraphics graphics, int x, int y) {
        int textWidth = font.width(text);
        int textHeight = font.lineHeight;

        int textX = 0; //init textX
        if (horizontalAlignment == AlignH.LEFT) {textX = x;}
        else if (horizontalAlignment == AlignH.CENTER) {textX = x - textWidth / 2;}
        else if (horizontalAlignment == AlignH.RIGHT) {textX = x - textWidth;}

        int textY = 0; //init textY
        if (verticalAlignment == AlignV.TOP) {textY = y;}
        else if (verticalAlignment == AlignV.CENTER) {textY = y - textHeight / 2;}
        else if (verticalAlignment == AlignV.BOTTOM) {textY = y - textHeight;}

        graphics.drawString(font, text, textX, textY, color);
    }

    //============================== Alignments ==============================\\
    public static enum AlignH {LEFT, CENTER, RIGHT}
    public static enum AlignV {TOP, CENTER, BOTTOM} 
}
