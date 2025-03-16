package net.fightingpainter.mc.towntime.hud.elements;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import toughasnails.api.temperature.ITemperature;
import toughasnails.api.temperature.TemperatureHelper;
import toughasnails.api.temperature.TemperatureLevel;

import net.fightingpainter.mc.towntime.TownTime;


public class HealthBar extends BaseBarElement {
    private final static ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(TownTime.MOD_ID, "textures/hud/health.png");
    private final static ResourceLocation BACKGROUND = ResourceLocation.fromNamespaceAndPath(TownTime.MOD_ID, "textures/hud/health_background.png");

    private HealthBarVariant variant = HealthBarVariant.NORMAL;

    public HealthBar() { //set size
        this.width = 130;
        this.height = 11;
    }
    
    @Override
    public boolean shouldRender(Player player) { //check gamemode
        if (player.isCreative() || player.isSpectator()) {return false;}
        else {return true;}
    }
    
    @Override
    public void getParameters(Player player) { //get health values
        this.maxValue = player.getMaxHealth();
        this.value = player.getHealth();

        //sorted by priority
        if (player.isOnFire() && !player.hasEffect(MobEffects.FIRE_RESISTANCE)) {this.variant = HealthBarVariant.BURNING;} //set variant to burning
        else if (player.hasEffect(MobEffects.WITHER)) {this.variant = HealthBarVariant.WITHER;} //set variant to wither
        else if (player.getAirSupply() <= 0) {this.variant = HealthBarVariant.DROWNING;} //set variant to drowning
        else if (player.isFullyFrozen()) {this.variant = HealthBarVariant.FREEZING;} //set variant to freezing
        else if (TemperatureHelper.isFullyHyperthermic(player)) {this.variant = HealthBarVariant.OVERHEATING;} //set variant to overheating
        else if (player.hasEffect(MobEffects.POISON)) {this.variant = HealthBarVariant.POISON;} //set variant to poison
        else if (player.hasEffect(MobEffects.REGENERATION)) {this.variant = HealthBarVariant.REGENERATION;} //set variant to regeneration
        else {this.variant = HealthBarVariant.NORMAL;} //set variant to normal
    }
    
    @Override
    public void render() { //render health bar
        renderSimpleTexture(BACKGROUND, 130, 11, x, y); //render background
        
        int textureWidth = 130; //set texture width
        int textureHeight = 79; //set texture height
        renderPartialTexture(TEXTURE, textureWidth, textureHeight, variant.heartX, variant.heartY, variant.heartWidth, variant.heartHeight, x+1, y+1); //render heart
        renderBarLeft(TEXTURE, textureWidth, textureHeight, variant.barX, variant.barY, variant.barWidth, variant.barHeight, x+9, y+3); //render health bar

        TextElement valueText = new TextElement(Math.round(this.value) + "/" + Math.round(this.maxValue));
        valueText.alignCenter(); //center align
        valueText.setScale(0.6f); //scale down
        valueText.setYShift(1); //shift down
        renderText(valueText, (x+9)+(variant.barWidth/2), (y+3)+(variant.barHeight/2)); //render text
    }
    
    private enum HealthBarVariant {
        NORMAL( //normal health bar
            0, 0, 9, 9, //heart 
            10, 0, 120, 5 //bar    
        ),
        REGENERATION( //regeneration
            0, 10, 9, 9, //heart
            10, 10, 120, 5 //bar
        ),
        POISON( //poison
            0, 20, 9, 9, //heart
            10, 20, 120, 5 //bar
        ),
        WITHER( //wither
            0, 30, 9, 9, //heart
            10, 30, 120, 5 //bar
        ),
        DROWNING( //drowning
            0, 40, 9, 9, //heart
            10, 40, 120, 5 //bar
        ),
        FREEZING( //freezing (from powdered snow)
            0, 50, 9, 9, //heart
            10, 50, 120, 5 //bar
        ),
        OVERHEATING( //overheating/hypertermia (from Tough As Nails)
            0, 60, 9, 9, //heart
            10, 70, 120, 5 //bar
        ),
        BURNING( //burning
            0, 70, 9, 9, //heart
            10, 80, 120, 5 //bar
        );

        public final int heartX; //heart portion u
        public final int heartY; //heart portion v
        public final int heartWidth; //heart portion width
        public final int heartHeight; //heart portion height

        public final int barX; //bar portion u
        public final int barY; //bar portion v
        public final int barWidth; //bar portion width
        public final int barHeight; //bar portion height

        HealthBarVariant(int heartX, int heartY, int heartWidth, int heartHeight, int barX, int barY, int barWidth, int barHeight) {
            this.heartX = heartX;
            this.heartY = heartY;
            this.heartWidth = heartWidth;
            this.heartHeight = heartHeight;
            this.barX = barX;
            this.barY = barY;
            this.barWidth = barWidth;
            this.barHeight = barHeight;
        }
    }
}
