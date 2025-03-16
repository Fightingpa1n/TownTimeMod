package net.fightingpainter.mc.towntime.hud;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import net.fightingpainter.mc.towntime.TownTime;
import net.fightingpainter.mc.towntime.hud.bars.*;


public class HudRenderer {
    private static final ResourceLocation HudBackground = ResourceLocation.fromNamespaceAndPath(TownTime.MOD_ID, "textures/hud/hud.png");
    private static final int HudTextureWidth = 182;
    private static final int HudTextureHeight = 60;
    
    private static final BaseHudElement hotbar = new Hotbar(); //create hotbar element

    private static final BaseHudElement healthBar = new HealthBar(); //create health bar element
    private static final BaseHudElement hungerBar = new HungerBar(); //create hunger bar element
    private static final BaseHudElement thirstBar = new ThirstBar(); //create thirst bar element

    private static final BaseHudElement temperatureDisplay = new TemperatureDisplay(); //create temperature display element

    public static void render(GuiGraphics graphics) {
        Minecraft minecraft = Minecraft.getInstance(); //get instance
        Player player = minecraft.player; //get player
        if (player == null) return; //if player is null, return

        graphics.pose().pushPose(); //push pose
        graphics.pose().translate(0, 0, 50); //translate

        int hudX = graphics.guiWidth() - HudTextureWidth;
        int hudY = graphics.guiHeight() - HudTextureHeight;
        graphics.blit(HudBackground, hudX, hudY, 0, 0, HudTextureWidth, HudTextureHeight, HudTextureWidth, HudTextureHeight); //render background
        
        int left = 0; //left x
        int right = graphics.guiWidth(); //right xs

        int top = 0; //top y
        int bottom = graphics.guiHeight(); //bottom y
        
        int centerX = graphics.guiWidth() / 2; //center x
        int centerY = graphics.guiHeight() / 2; //center y

        //the hotbar should be bottom center
        hotbar.renderElement(graphics, player, centerX - (hotbar.getWidth()/2), bottom - hotbar.getHeight()); //render hotbar

        //the bars should be on the bottom left
        int barX = left + 1; //bar x
        int barY = bottom - 1; //bar y

        barY -= healthBar.getHeight(); //decrease y by health bar height
        healthBar.renderElement(graphics, player, barX, barY); //render health bar

        barY -= hungerBar.getHeight() + 1; //decrease y by hunger bar height and 1 (for spacing)
        hungerBar.renderElement(graphics, player, barX, barY); //render hunger bar
        
        barY -= thirstBar.getHeight() + 1; //decrease y by thirst bar height and 1 (for spacing)
        thirstBar.renderElement(graphics, player, barX, barY); //render thirst bar

        barX += Math.max(thirstBar.getWidth(), hungerBar.getWidth()) + 1; //increase x by the max of health bar width and hunger bar width and 1 (for spacing)
        
        int thirstAndHungerBarHeight = thirstBar.getHeight() + hungerBar.getHeight() + 1; //thirst bar height + hunger bar height + 1 (for spacing)
        int thirstAndHungerBarCenter = barY + (thirstAndHungerBarHeight / 2); //thirst and hunger bar center
        // int temperatureDisplay = 


        temperatureDisplay.renderElement(graphics, player, barX, barY); //render temperature display

        graphics.pose().popPose(); //pop pose
    }

    public static void xpBarRenderer(Screen screen, GuiGraphics graphics) {
        if (screen instanceof InventoryScreen) {
            // xpBar.getParameters(Minecraft.getInstance().player);
            // xpBar.render(graphics);
        }
    }
}
