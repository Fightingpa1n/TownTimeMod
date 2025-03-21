package net.fightingpainter.mc.towntime.client.hud.elements;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import net.fightingpainter.mc.towntime.TownTime;


public class Offhand extends BaseHudElement {
    private final static ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(TownTime.MOD_ID, "textures/hud/offhand.png");

    private ItemStack item;

    public Offhand() { //set size
        this.width = 26;
        this.height = 20;
    }

    @Override
    public boolean shouldRender(Player player) {
        if (player.getOffhandItem().isEmpty()) {return false;}
        else {return true;}
    }

    @Override
    public void getParameters(Player player) {
        item = player.getOffhandItem();
    }
    
    @Override
    public void render() {
        renderSimpleTexture(TEXTURE, 26, 20, x, y); //render background

        if (!item.isEmpty()) { //if item
            graphics.renderItem(item, x+2, y+2); //render item
            graphics.renderItemDecorations(getFont(), item, x+2, y+2);
        }
    }
}
