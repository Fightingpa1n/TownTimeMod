package net.fightingpainter.mc.towntime.hud.bars;

import net.fightingpainter.mc.towntime.TownTime;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import net.fightingpainter.mc.towntime.hud.BaseHudElement;


public class Hotbar extends BaseHudElement {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(TownTime.MOD_ID, "textures/hud/hotbar.png");
    private static final ResourceLocation SELECTED_TEXTURE = ResourceLocation.fromNamespaceAndPath(TownTime.MOD_ID, "textures/hud/hotbar_selected.png");
    
    private ItemStack[] items = new ItemStack[9];
    private int selected = 0;
    
    public Hotbar(int xOffset, int yOffset) {super(xOffset, yOffset);} //constructor

    @Override
    public boolean shouldRender(Player player) { //should render
        return true; //the hotbar should always render (I think)
    }

    @Override
    public void getParameters(Player player) { //get items
        for (int i = 0; i < 9; i++) { //get items from player's hotbar
            items[i] = player.getInventory().getItem(i);
        }
        selected = player.getInventory().selected; //get selected item/slot
    }
    
    @Override
    public void render(GuiGraphics graphics) {
        renderSimpleTexture(graphics, TEXTURE, 172, 20, getX(), getY()); //render hotbar texture

        for (int i = 0; i < 9; i++) {
            int slotX = (getX() + i * 19); //slot x
            if (i == selected) { //if slot is selected render selected texture
                renderSimpleTexture(graphics, SELECTED_TEXTURE, 20, 20, slotX, getY());
            }
            renderSlot(graphics, slotX+2, getY()+2, items[i]); //render slot (+2 to center item in slot)
        }

        // if (!items[active].isEmpty()) { //if active item exists render name above hotbar
        //     Component name = items[active].getHoverName();
        //     int textWidth = font.width(name);
        //     int textX = baseX + (width - textWidth) / 2;
        //     int textY = baseY - 10;
        //     graphics.drawString(font, name, textX, textY, 0xFFFFFF);
        // }
    }

    private void renderSlot(GuiGraphics graphics, int x, int y, ItemStack item) {
        if (!item.isEmpty()) { //if item...
            graphics.renderItem(item, x, y); //render item
            graphics.renderItemDecorations(getFont(), item, x, y); //render item decorations (enchantments, etc)
        }
    }
}
