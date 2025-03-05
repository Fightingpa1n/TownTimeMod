package net.fightingpainter.mc.towntime.hud;

import net.fightingpainter.mc.towntime.hud.bars.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;

public class HudRenderer {
    
    private static final HealthBar healthBar = new HealthBar(10, 10);

    private static final AbsorbtionDisplay absorbtionDisplay = new AbsorbtionDisplay(40, 10);
    
    private static final Food foodBar = new Food(10, 30);
    private static final Thirst thirstBar = new Thirst(50, 30);

    private static final TemperatureDisplay temperatureDisplay = new TemperatureDisplay(10, 50);

    private static final Hotbar hotbar = new Hotbar(10, 70);

    private static final XpBar xpBar = new XpBar(10, 10);

    public static void render(GuiGraphics graphics) {
        Minecraft minecraft = Minecraft.getInstance(); //get instance
        Player player = minecraft.player; //get player
        if (player == null) return; //if player is null, return

        if(healthBar.shouldRender(player)) { //if health bar should render
            healthBar.getParameters(player); //get health bar parameters
            healthBar.render(graphics); //render health bar
        }

        if(absorbtionDisplay.shouldRender(player)) { //if absorbtion display should render
            absorbtionDisplay.getParameters(player); //get absorbtion display parameters
            absorbtionDisplay.render(graphics); //render absorbtion display
        }

        if(foodBar.shouldRender(player)) { //if food bar should render
            foodBar.getParameters(player); //get food bar parameters
            foodBar.render(graphics); //render food bar
        }

        if(thirstBar.shouldRender(player)) { //if thirst bar should render
            thirstBar.getParameters(player); //get thirst bar parameters
            thirstBar.render(graphics); //render thirst bar
        }

        if (temperatureDisplay.shouldRender(player)) { //if temperature display should render
            temperatureDisplay.getParameters(player); //get temperature display parameters
            temperatureDisplay.render(graphics); //render temperature display
        }

        if (hotbar.shouldRender(player)) { //if hotbar should render
            hotbar.getParameters(player); //get hotbar parameters
            hotbar.render(graphics); //render hotbar
        }
    }


    public static void xpBarRenderer(Screen screen, GuiGraphics graphics) {
        if (screen instanceof InventoryScreen) {
            xpBar.getParameters(Minecraft.getInstance().player);
            xpBar.render(graphics);
        }
    }
}
