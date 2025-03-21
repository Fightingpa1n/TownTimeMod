package net.fightingpainter.mc.towntime.client.hud.elements;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import net.fightingpainter.mc.towntime.TownTime;


public class MountBar extends BaseBarElement {
    private final static ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(TownTime.MOD_ID, "textures/hud/mount.png");

    public MountBar() { //set size
        this.width = 130;
        this.height = 11;
    }

    @Override
    public boolean shouldRender(Player player) {
        if (player.isCreative() || player.isSpectator()) {return false;} //check gamemode

        if (player.getVehicle() instanceof LivingEntity) {return true;} //check for mount
        else {return false;} //no mount (or not living entity)
    }

    @Override
    public void getParameters(Player player) {
        if (player.getVehicle() instanceof LivingEntity mount) {
            this.value = mount.getHealth();
            this.maxValue = mount.getMaxHealth();
        }
    }

    @Override
    public void render() {
        int textureWidth = 130; //set texture width
        int textureHeight = 22; //set texture height
        renderPartialTexture(TEXTURE, textureWidth, textureHeight, 0, 0, 130, 11, x, y); //render mount bar background
        renderBarLeft(TEXTURE, textureWidth, textureHeight, 9, 14, 120, 5, x+9, y+3); //render mount bar

        TextElement valueText = new TextElement(Math.round(this.value) + "/" + Math.round(this.maxValue));
        valueText.alignCenter(); //center align
        valueText.setScale(0.6f); //scale down
        valueText.setYShift(1); //shift down
        renderText(valueText, (x+7)+(120/2), (y+3)+(5/2)); //render text
    }
}
