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
    
    private static final BaseHudElement hotbar = new Hotbar(9, 39); //create hotbar element

    private static final BaseBarElement healthBar = new HealthBar(51, 26); //create health bar element
    private static final BaseBarElement hungerBar = new HungerBar(112, 15); //create hunger bar element
    private static final BaseBarElement thirstBar = new ThirstBar(112, 4); //create thirst bar element

    private static final BaseHudElement temperatureDisplay = new TemperatureDisplay(92, 5); //create temperature display element
    /* 
    private static final HealthBar healthBar = new HealthBar(10, 10);
    private static final AbsorbtionDisplay absorbtionDisplay = new AbsorbtionDisplay(40, 10);
    private static final Food foodBar = new Food(10, 30);
    private static final Thirst thirstBar = new Thirst(50, 30);
    private static final TemperatureDisplay temperatureDisplay = new TemperatureDisplay(10, 50);
     */
    // private static final XpBar xpBar = new XpBar(10, 10);




    public static void render(GuiGraphics graphics) {
        Minecraft minecraft = Minecraft.getInstance(); //get instance
        Player player = minecraft.player; //get player
        if (player == null) return; //if player is null, return

        graphics.pose().pushPose(); //push pose
        graphics.pose().translate(0, 0, 50); //translate

        int hudX = graphics.guiWidth() - HudTextureWidth;
        int hudY = graphics.guiHeight() - HudTextureHeight;
        graphics.blit(HudBackground, hudX, hudY, 0, 0, HudTextureWidth, HudTextureHeight, HudTextureWidth, HudTextureHeight); //render background

        hotbar.renderElement(graphics, player, hudX, hudY); //render hotbar

        healthBar.renderElement(graphics, player, hudX, hudY); //render health bar
        hungerBar.renderElement(graphics, player, hudX, hudY); //render hunger bar
        thirstBar.renderElement(graphics, player, hudX, hudY); //render thirst bar

        temperatureDisplay.renderElement(graphics, player, hudX, hudY); //render temperature display

        
        // if(healthBar.shouldRender(player)) { //if health bar should render
        //     healthBar.getParameters(player); //get health bar parameters
        //     healthBar.render(graphics); //render health bar
        // }
        
        // if(absorbtionDisplay.shouldRender(player)) { //if absorbtion display should render
        //     absorbtionDisplay.getParameters(player); //get absorbtion display parameters
        //     absorbtionDisplay.render(graphics); //render absorbtion display
        // }
        
        // if(foodBar.shouldRender(player)) { //if food bar should render
        //     foodBar.getParameters(player); //get food bar parameters
        //     foodBar.render(graphics); //render food bar
        // }
        
        // if(thirstBar.shouldRender(player)) { //if thirst bar should render
        //     thirstBar.getParameters(player); //get thirst bar parameters
        //     thirstBar.render(graphics); //render thirst bar
        // }
        
        // if (temperatureDisplay.shouldRender(player)) { //if temperature display should render
        //     temperatureDisplay.getParameters(player); //get temperature display parameters
        //     temperatureDisplay.render(graphics); //render temperature display
        // }
        
        // if (hotbar.shouldRender(player)) { //if hotbar should render
        //     hotbar.getParameters(player); //get hotbar parameters
        //     hotbar.render(graphics); //render hotbar
        // }

        graphics.pose().popPose(); //pop pose
    }
    
    
    public static void xpBarRenderer(Screen screen, GuiGraphics graphics) {
        if (screen instanceof InventoryScreen) {
            // xpBar.getParameters(Minecraft.getInstance().player);
            // xpBar.render(graphics);
        }
    }
}
