package net.fightingpainter.mc.towntime.hud;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import net.fightingpainter.mc.towntime.TownTime;
import net.fightingpainter.mc.towntime.hud.elements.*;


public class HudRenderer {
    private static final ResourceLocation HudBackground = ResourceLocation.fromNamespaceAndPath(TownTime.MOD_ID, "textures/hud/hud.png");
    private static final int HudTextureWidth = 182;
    private static final int HudTextureHeight = 60;
    
    private static final BaseHudElement hotbar = new Hotbar(); //create hotbar element

    private static final BaseHudElement healthBar = new HealthBar(); //create health bar element
    private static final BaseHudElement hungerBar = new HungerBar(); //create hunger bar element
    private static final BaseHudElement thirstBar = new ThirstBar(); //create thirst bar element

    private static final BaseHudElement temperatureDisplay = new TemperatureDisplay(); //create temperature display element
    private static final BaseHudElement armorDisplay = new ArmorDisplay(); //create armor display element
    private static final BaseHudElement absorbtionDisplay = new AbsorbtionDisplay(); //create absorbtion display element

    public static void render(GuiGraphics graphics) {
        Minecraft minecraft = Minecraft.getInstance(); //get instance
        Player player = minecraft.player; //get player
        if (player == null) return; //if player is null, return

        graphics.pose().pushPose(); //push pose
        graphics.pose().translate(0, 0, 50); //translate

        int hudX = graphics.guiWidth() - HudTextureWidth;
        int hudY = graphics.guiHeight() - HudTextureHeight;
        graphics.blit(HudBackground, hudX, hudY, 0, 0, HudTextureWidth, HudTextureHeight, HudTextureWidth, HudTextureHeight); //render background

        int centerX = graphics.guiWidth() / 2; //center x

        //the hotbar should be bottom center
        hotbar.renderElement(graphics, player, centerX - (hotbar.getWidth()/2), graphics.guiHeight() - hotbar.getHeight()); //render hotbar

        //the bars should be on the bottom left
        int barX = 2; //bar x
        int barY = graphics.guiHeight() - 2; //bar y
        
        //overlay fix
        if (barX + healthBar.getWidth() >= centerX-(hotbar.getWidth()/2)) {
            barY -= hotbar.getHeight(); //decrease y by hotbar height so instead of ovelapping it will be above the hotbar
        }

        barY -= healthBar.getHeight(); //decrease y by health bar height
        healthBar.renderElement(graphics, player, barX, barY); //render health bar

        barY -= hungerBar.getHeight() + 2; //decrease y by hunger bar height and 1 (for spacing)
        hungerBar.renderElement(graphics, player, barX, barY); //render hunger bar
        
        barY -= thirstBar.getHeight() + 2; //decrease y by thirst bar height and 1 (for spacing)
        thirstBar.renderElement(graphics, player, barX, barY); //render thirst bar

        //render displays
        int displayX = barX + Math.max(thirstBar.getWidth(), hungerBar.getWidth()) + 2; //increase x by the max of health bar width and hunger bar width and 1 (for spacing)
        int displayY = barY + 2;
        temperatureDisplay.renderElement(graphics, player, displayX, displayY); //render temperature display

        displayX += temperatureDisplay.getWidth() + 2; //increase x by temperature display width and 2 (for spacing)
        armorDisplay.renderElement(graphics, player, displayX, displayY); //render armor display

        if (armorDisplay.shouldRender(player)) { //if armor display should render
            displayX += armorDisplay.getWidth() + 2; //increase x by armor display width and 2 (for spacing)
        }
        absorbtionDisplay.renderElement(graphics, player, displayX, displayY); //render absorbtion display
        
        graphics.pose().popPose(); //pop pose
    }

    public static void xpBarRenderer(Screen screen, GuiGraphics graphics) {
        if (screen instanceof InventoryScreen) {
            // xpBar.getParameters(Minecraft.getInstance().player);
            // xpBar.render(graphics);
        }
    }
}
