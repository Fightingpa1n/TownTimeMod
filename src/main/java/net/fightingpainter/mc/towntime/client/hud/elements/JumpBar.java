package net.fightingpainter.mc.towntime.client.hud.elements;

import net.fightingpainter.mc.towntime.TownTime;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PlayerRideableJumping;
import net.minecraft.world.entity.player.Player;

public class JumpBar extends BaseBarElement {
    private final static ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(TownTime.MOD_ID, "textures/hud/jump.png");

    public JumpBar() { //set size
        this.width = 16;
        this.height = 56;
    }

    @Override
    public boolean shouldRender(Player player) {
        if (player.isSpectator()) {return false;} //check gamemode

        LocalPlayer localPlayer = Minecraft.getInstance().player;
        if (localPlayer == null) {return false;} //check for player

        PlayerRideableJumping playerrideablejumping = localPlayer.jumpableVehicle();
        if (playerrideablejumping != null) {return true;} //check for jumpable vehicle

        return false; //no jumpable vehicle
    }

    @Override
    public void getParameters(Player player) {
        LocalPlayer localPlayer = Minecraft.getInstance().player;
        if (localPlayer == null) {return;} //check for player

        this.value = localPlayer.getJumpRidingScale();
        this.maxValue = 1.0F;
    }

    @Override
    public void render() {
        int textureWidth = 32; //set texture width
        int textureHeight = 56; //set texture height
        renderPartialTexture(TEXTURE, textureWidth, textureHeight, 0, 0, 16, 56, x, y); //render jump bar background
        renderBarBottom(TEXTURE, textureWidth, textureHeight, 17, 1, 14, 54, x+1, y+1); //render jump bar
    }
}