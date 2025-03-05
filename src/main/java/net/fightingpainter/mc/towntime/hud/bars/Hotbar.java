package net.fightingpainter.mc.towntime.hud.bars;


import net.fightingpainter.mc.towntime.TownTime;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class Hotbar {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(TownTime.MOD_ID, "textures/hud/hotbar.png"); 
    private static final ResourceLocation SELECTED = ResourceLocation.fromNamespaceAndPath(TownTime.MOD_ID, "textures/hud/hotbar_selected.png");
    private final int width = 154;
    private final int height = 18;
    private int x;
    private int y;
    private ItemStack[] items = new ItemStack[9];
    private int active = 0;

    public Hotbar(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void getParameters(Player player) { //get items
        for (int i = 0; i < 9; i++) {
            items[i] = player.getInventory().getItem(i);
        }
        active = player.getInventory().selected;
    }


    public boolean shouldRender(Player player) {
        return true;
    }


    public void render(GuiGraphics guiGraphics) {
        guiGraphics.blit(TEXTURE, x, y, 0, 0, width, height, width, height);
        Font font = Minecraft.getInstance().font;
        
        for (int i = 0; i < 9; i++) {
            if (i == active) {guiGraphics.blit(SELECTED, x + i * 17, y, 0, 0, 16, 16, 16, 16);}
            if (items[i] != null) {
                guiGraphics.renderItem(items[i], x + 1 + i * 17, y + 1);

                if (items[i].getCount() > 1) {
                    String text = Integer.toString(items[i].getCount());
                    int textWidth = font.width(text);
                    int textX = x + 17 + i * 17 - textWidth;
                    int textY = y + 17;
                    guiGraphics.drawString(font, text, textX, textY, 0xFFFFFF);
                }
            }
        }

        if (items[active] != null) { //if active item exists render name above hotbar
            Component name = items[active].getHoverName();
            int textWidth = font.width(name);
            int textX = x + (width - textWidth) / 2;
            int textY = y - 10;
            guiGraphics.drawString(font, name, textX, textY, 0xFFFFFF);
        }
    }
}
