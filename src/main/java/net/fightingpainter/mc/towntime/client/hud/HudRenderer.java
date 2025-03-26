package net.fightingpainter.mc.towntime.client.hud;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import net.fightingpainter.mc.towntime.TownTime;
import net.fightingpainter.mc.towntime.client.hud.elements.*;


public class HudRenderer {
    // private static final ResourceLocation HudBackground = ResourceLocation.fromNamespaceAndPath(TownTime.MOD_ID, "textures/hud/hud.png");
    // private static final int HudTextureWidth = 182;
    // private static final int HudTextureHeight = 60;
    
    private static final BaseHudElement hotbar = new Hotbar(); //create hotbar element
    private static final BaseHudElement offhand = new Offhand(); //create offhand slot element

    private static final BaseHudElement healthBar = new HealthBar(); //create health bar element
    private static final BaseHudElement hungerBar = new HungerBar(); //create hunger bar element
    private static final BaseHudElement thirstBar = new ThirstBar(); //create thirst bar element

    private static final BaseHudElement temperatureDisplay = new TemperatureDisplay(); //create temperature display element
    private static final BaseHudElement armorDisplay = new ArmorDisplay(); //create armor display element
    private static final BaseHudElement absorbtionDisplay = new AbsorbtionDisplay(); //create absorbtion display element

    private static final BaseHudElement airBar = new AirBar(); //create air bar element
    private static final BaseHudElement mountBar = new MountBar(); //create mount bar element
    private static final BaseHudElement jumpBar = new JumpBar(); //create jump bar element
    
    public static void render(GuiGraphics graphics) {
        Minecraft minecraft = Minecraft.getInstance(); //get instance
        Player player = minecraft.player; //get player
        if (minecraft.options.hideGui) return; //if gui is hidden, return
        if (player == null) return; //if player is null, return

        // int hudX = graphics.guiWidth() - HudTextureWidth;
        // int hudY = graphics.guiHeight() - HudTextureHeight;
        // graphics.blit(HudBackground, hudX, hudY, 0, 0, HudTextureWidth, HudTextureHeight, HudTextureWidth, HudTextureHeight); //render background

        //the hotbar should be bottom center
        int hotbarX = (graphics.guiWidth() / 2) - (hotbar.getWidth()/2); //hotbar x
        int hotbarY = graphics.guiHeight() - hotbar.getHeight(); //hotbar y
        
        //the bars should be on the bottom left
        int barX = 2; //bar x
        int barY = graphics.guiHeight() - 2; //bar y

        //overlay fix
        if (barX+healthBar.getWidth() > hotbarX - offhand.getWidth()) { //if health bar is in the way of the hotbar
            // barX = hotbarX - healthBar.getWidth() - 2; //move bars up
            hotbarX = graphics.guiWidth() - hotbar.getWidth(); //right aligned hotbar
        }

        offhand.renderElement(graphics, player, (hotbarX - offhand.getWidth())+1, hotbarY); //render offhand slot (render before hotbar so the hotbar is above the offhand slot)
        hotbar.renderElement(graphics, player, hotbarX, hotbarY); //render hotbar

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

        //render extra bars
        airBar.renderElement(graphics, player, (graphics.guiWidth()-airBar.getWidth())/2, graphics.guiHeight() - (hotbar.getHeight() + airBar.getHeight() + 20)); //render air bar
        mountBar.renderElement(graphics, player, 2, graphics.guiHeight()-(mountBar.getHeight()+45)); //render mount bar
        jumpBar.renderElement(graphics, player, graphics.guiWidth()-jumpBar.getWidth()-2, (graphics.guiHeight()-jumpBar.getHeight())/2); //render jump bar
    }
    
    public static void xpBarRenderer(Screen screen, GuiGraphics graphics) {
        if (screen instanceof InventoryScreen) {
            // xpBar.getParameters(Minecraft.getInstance().player);
            // xpBar.render(graphics);
        }
    }


    public static void clientTick(Player player) { //get's called each tick
        if (player == null) return; //if player is null, return
        
        if (hotbar.shouldRender(player)) {hotbar.tick();} //tick hotbar
        if (offhand.shouldRender(player)) {offhand.tick();} //tick offhand slot
        
        if (healthBar.shouldRender(player)) {healthBar.tick();} //tick health bar
        if (hungerBar.shouldRender(player)) {hungerBar.tick();} //tick hunger bar
        if (thirstBar.shouldRender(player)) {thirstBar.tick();} //tick thirst bar

        if (temperatureDisplay.shouldRender(player)) {temperatureDisplay.tick();} //tick temperature display
        if (armorDisplay.shouldRender(player)) {armorDisplay.tick();} //tick armor display
        if (absorbtionDisplay.shouldRender(player)) {absorbtionDisplay.tick();} //tick absorbtion display

        if (airBar.shouldRender(player)) {airBar.tick();} //tick air bar
        if (mountBar.shouldRender(player)) {mountBar.tick();} //tick mount bar
        if (jumpBar.shouldRender(player)) {jumpBar.tick();} //tick jump bar
    }
}
