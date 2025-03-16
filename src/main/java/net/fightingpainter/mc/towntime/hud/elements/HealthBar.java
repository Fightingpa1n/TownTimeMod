package net.fightingpainter.mc.towntime.hud.elements;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;

import org.checkerframework.checker.units.qual.t;

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

        boolean regenerating = player.hasEffect(MobEffects.REGENERATION); //check if player is regenerating
        boolean poisoned = player.hasEffect(MobEffects.POISON); //check if player is poisoned
        boolean withered = player.hasEffect(MobEffects.WITHER); //check if player is withered
        boolean drowning = player.getAirSupply() <= 0; //check if player is drowning
        boolean freezing = player.isFullyFrozen(); //check if player is freezing


        this.variant = HealthBarVariant.NORMAL; //set variant to normal
    }

    @Override
    public void render() { //render health bar
        renderSimpleTexture(BACKGROUND, 130, 11, x, y); //render background
        int textureWidth = 130; //set texture width
        int textureHeight = 11; //set texture height
        renderPartialTexture(TEXTURE, textureWidth, textureHeight, variant.heartX, variant.heartY, variant.heartWidth, variant.heartHeight, x, y); //render heart
        renderBarLeft(TEXTURE, textureWidth, textureHeight, variant.barX, variant.barY, variant.barWidth, variant.barHeight, x, y); //render health bar
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
        HYPOTHERMIA( //hypothermia (from Tough As Nails)
            0, 60, 9, 9, //heart
            10, 60, 120, 5 //bar
        ),
        OVERHEATING( //overheating/hypertermia (from Tough As Nails)
            0, 70, 9, 9, //heart
            10, 70, 120, 5 //bar
        ),
        BURNING( //burning
            0, 80, 9, 9, //heart
            10, 80, 120, 5 //bar
        ),
        SOUL_BURNING( //burning from soul fire.
            0, 90, 9, 9, //heart
            10, 90, 120, 5 //bar
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
