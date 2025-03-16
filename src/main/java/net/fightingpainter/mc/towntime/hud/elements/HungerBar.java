package net.fightingpainter.mc.towntime.hud.elements;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;

import net.fightingpainter.mc.towntime.TownTime;


public class HungerBar extends BaseBarElement{
    private final static ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(TownTime.MOD_ID, "textures/hud/hunger.png");
    private final static ResourceLocation BACKGROUND = ResourceLocation.fromNamespaceAndPath(TownTime.MOD_ID, "textures/hud/hunger_background.png");

    private HungerBarVariant variant = HungerBarVariant.NORMAL;
    
    public HungerBar() { //set size
        this.width = 68;
        this.height = 9;
    }

    @Override
    public boolean shouldRender(Player player) {//check gamemode
        if (player.isCreative() || player.isSpectator()) {return false;}
        else {return true;}
    }

    @Override
    public void getParameters(Player player) {
        this.value = player.getFoodData().getFoodLevel();
        this.maxValue = 20;

        //sorted by priority
        if (player.hasEffect(MobEffects.HUNGER)) {this.variant = HungerBarVariant.HUNGER;} //set variant to hunger
        else if (player.hasEffect(MobEffects.SATURATION)) {this.variant = HungerBarVariant.SATURATION;} //set variant to saturation
        else {this.variant = HungerBarVariant.NORMAL;} //set variant to normal
    }
    
    @Override
    public void render() {
        renderSimpleTexture(BACKGROUND, 68, 9, x, y); //render background

        int textureWidth = 68; //set texture width
        int textureHeight = 23; //set texture height
        renderPartialTexture(TEXTURE, textureWidth, textureHeight, variant.drumstickX, variant.drumstickY, variant.drumstickWidth, variant.drumstickHeight, x+1, y+1); //render drumstick
        renderBarLeft(TEXTURE, textureWidth, textureHeight, variant.barX, variant.barY, variant.barWidth, variant.barHeight, x+7, y+3); //render hunger bar
    }

    private enum HungerBarVariant {
        NORMAL( //normal hunger bar
            0, 0, 7, 7, //drumstick
            8, 0, 60, 3 //bar
        ),
        SATURATION( //saturation
            0, 8, 7, 7, //drumstick
            8, 8, 60, 3 //bar
        ),
        HUNGER( //hunger
            0, 16, 7, 7, //drumstick
            8, 16, 60, 3 //bar
        );

        public final int drumstickX; //drumstick portion u
        public final int drumstickY; //drumstick portion v
        public final int drumstickWidth; //drumstick portion width
        public final int drumstickHeight; //drumstick portion height

        public final int barX; //bar portion u
        public final int barY; //bar portion v
        public final int barWidth; //bar portion width
        public final int barHeight; //bar portion height

        HungerBarVariant(int drumstickX, int drumstickY, int drumstickWidth, int drumstickHeight, int barX, int barY, int barWidth, int barHeight) {
            this.drumstickX = drumstickX;
            this.drumstickY = drumstickY;
            this.drumstickWidth = drumstickWidth;
            this.drumstickHeight = drumstickHeight;
            this.barX = barX;
            this.barY = barY;
            this.barWidth = barWidth;
            this.barHeight = barHeight;
        }
    }
}
