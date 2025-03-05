package net.fightingpainter.mc.towntime.hud;

import net.fightingpainter.mc.towntime.hud.bars.HealthBar;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.player.Player;

public class HudRenderer {
    
    private static final HealthBar healthBar = new HealthBar(10, 10);

    public static void render(GuiGraphics graphics) {
        Minecraft minecraft = Minecraft.getInstance(); //get instance
        Player player = minecraft.player; //get player
        if (player == null) return; //if player is null, return

        if(healthBar.shouldRender(player)) { //if health bar should render
            healthBar.getParameters(player); //get health bar parameters
            healthBar.render(graphics); //render health bar
        }
    }
}
