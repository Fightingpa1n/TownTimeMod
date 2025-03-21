package net.fightingpainter.mc.towntime.client.hud.elements;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import net.fightingpainter.mc.towntime.TownTime;

public class AbsorbtionDisplay extends BaseHudElement {
    private final static ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(TownTime.MOD_ID, "textures/hud/absorbtion.png");
    private int value;

    public AbsorbtionDisplay() { //set size
        this.width = 16;
        this.height = 16;
    }

    @Override
    public boolean shouldRender(Player player) {
        return player.getAbsorptionAmount() > 0;
    }

    @Override
    public void getParameters(Player player) {
        this.value = (int) player.getAbsorptionAmount();
    }
    
    @Override
    public void render() {
        renderSimpleTexture(TEXTURE, 16, 16, x, y); //render texture
        
        //render value
        String text = Integer.toString(value);
        Font font = Minecraft.getInstance().font;
        int textWidth = font.width(text);
        int textHeight = font.lineHeight;
        int textX = x + (width - textWidth) / 2;
        int textY = y + (height - textHeight) / 2;
        graphics.drawString(font, text, textX, textY, 0xFFFFFF);
    }
}
