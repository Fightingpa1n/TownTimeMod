package net.fightingpainter.mc.towntime.hud.bars;

import net.fightingpainter.mc.towntime.TownTime;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public class AbsorbtionDisplay {
    private final static ResourceLocation NORMAL = ResourceLocation.fromNamespaceAndPath(TownTime.MOD_ID, "textures/hud/absorbtion.png");
    private final int width = 16;
    private final int height = 16;
    private int x;
    private int y;
    private int value;

    public AbsorbtionDisplay(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void getParameters(Player player) {
        this.value = (int) player.getAbsorptionAmount();
    }

    public boolean shouldRender(Player player) {
        return player.getAbsorptionAmount() > 0;
    }
    
    public void render(GuiGraphics guiGraphics) {
        if (value == 0) {return;}
        guiGraphics.blit(NORMAL, x, y, 0, 0, width, height, width, height);
        
        String text = Integer.toString(value);
        Font font = Minecraft.getInstance().font;
        int textWidth = font.width(text);
        int textHeight = font.lineHeight;
        int textX = x + (width - textWidth) / 2;
        int textY = y + (height - textHeight) / 2;
        guiGraphics.drawString(font, text, textX, textY, 0xFFFFFF);
    }
}
