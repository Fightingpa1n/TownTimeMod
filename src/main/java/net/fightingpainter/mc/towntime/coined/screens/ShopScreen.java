package net.fightingpainter.mc.towntime.coined.screens;

import com.mojang.blaze3d.systems.RenderSystem;

import net.fightingpainter.mc.towntime.TownTime;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
// import net.minecraft.client.gui.screens.inventory.MerchantScreen;

public class ShopScreen extends AbstractContainerScreen<ShopMenu> {

    private static final ResourceLocation BACKGROUND = ResourceLocation.fromNamespaceAndPath(TownTime.MOD_ID, "textures/gui/shop/shop.png");
    private final int textureWidth = 276;
    private final int textureHeight = 166;

    public ShopScreen(ShopMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageWidth = textureWidth;
        this.imageHeight = textureHeight;
        this.inventoryLabelX = 107;
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float delta, int mouseX, int mouseY) {
        RenderSystem.setShaderTexture(0, BACKGROUND);
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;
        
        graphics.blit(BACKGROUND, x, y, 0, 0, this.imageWidth, this.imageHeight, textureWidth, textureHeight);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.renderMenuBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

}
