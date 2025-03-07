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
    private static final ResourceLocation SELECTED_TEXTURE = ResourceLocation.fromNamespaceAndPath(TownTime.MOD_ID, "textures/hud/hotbar_selected.png");
    private static final int width = 178;
    private static final int height = 20;
    
    
    private final int xOffset;
    private final int yOffset;
    private final int zIndex;
    private int x = 0;
    private int y = 0;
    private ItemStack[] items = new ItemStack[9];
    private int selected = 0;

    public Hotbar(int xOffset, int yOffset, int zIndex) {
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        this.zIndex = zIndex;
    }

    public void getParameters(Player player) { //get items
        for (int i = 0; i < 9; i++) {
            items[i] = player.getInventory().getItem(i);
        }
        selected = player.getInventory().selected;
    }

    public void render(GuiGraphics graphics) {
        Font font = Minecraft.getInstance().font;
        int baseX = x + xOffset;
        int baseY = y + yOffset;

        graphics.pose().pushPose();
        graphics.pose().translate(0, 0, zIndex); //set z index
        graphics.blit(TEXTURE, baseX, baseY, 0, 0, 178, 20, 178, 20); //render hotbar
        graphics.pose().popPose();

        for (int i = 0; i < 9; i++) {
            int slotX = (baseX + i * 19); //slot x
        
            if (i == selected) { //if slot is selected render selected texture
                graphics.pose().pushPose();
                graphics.pose().translate(0, 0, zIndex+1); //set z index +1
                graphics.blit(SELECTED_TEXTURE, slotX, baseY, 0, 0, 20, 20, 20, 20);
                graphics.pose().popPose();
            }

            if (!items[i].isEmpty()) { //render items
                graphics.pose().pushPose();
                graphics.pose().translate(0, 0, zIndex+2); //set z index +2 (renderItem adds 150 to the z index)
                graphics.renderItem(items[i], slotX+2, baseY+2);
                graphics.pose().popPose();

                if (items[i].getCount() > 1) {
                    String text = Integer.toString(items[i].getCount());
                    int textWidth = font.width(text);
                    int textHeight = font.lineHeight;

                    int textX = (slotX + 19)-textWidth;
                    int textY = (baseY + 20)-textHeight;
                    graphics.pose().pushPose();
                    graphics.pose().translate(0, 0, (zIndex+3)+200); //set z index +3 (+150 for above item)
                    graphics.drawString(font, text, textX, textY, 0xFFFFFF);
                    graphics.pose().popPose();
                }
            }
        }

        // if (!items[active].isEmpty()) { //if active item exists render name above hotbar
        //     Component name = items[active].getHoverName();
        //     int textWidth = font.width(name);
        //     int textX = baseX + (width - textWidth) / 2;
        //     int textY = baseY - 10;
        //     graphics.drawString(font, name, textX, textY, 0xFFFFFF);
        // }
    }

    public void setPos(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
