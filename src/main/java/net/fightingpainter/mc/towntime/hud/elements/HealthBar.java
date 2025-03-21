package net.fightingpainter.mc.towntime.hud.elements;

import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import toughasnails.api.temperature.TemperatureHelper;

import net.fightingpainter.mc.towntime.ClientConfig;
import net.fightingpainter.mc.towntime.TownTime;
import net.fightingpainter.mc.towntime.client.ModSounds;
import net.fightingpainter.mc.towntime.util.Txt;

public class HealthBar extends BaseBarElement { //health bar element

    //============================== Settings ==============================\\ //TODO: I need to move some of these to the config (also maybe I should sort it a bit better)
    //=========== Texture ===========\\
    private static final ResourceLocation BACKGROUND = ResourceLocation.fromNamespaceAndPath(TownTime.MOD_ID, "textures/hud/health/background.png");
    private static final int BACKGROUND_WIDTH = 130; //width of the background texture
    private static final int BACKGROUND_HEIGHT = 11; //height of the background texture
    
    private static final int TEXTURE_WIDTH = 144; //width of the healthbar variant texture
    private static final int TEXTURE_HEIGHT = 59; //height of the healthbar variant texture
    
    private static final int ICON_WIDTH = 11; //width of a heart portion of the texture
    private static final int ICON_HEIGHT = 11; //height of a heart portion of the texture
    private static final int ICON_OFFSET_X = 0; //x offset of a heart portion of the texture
    private static final int ICON_OFFSET_Y = 0; //y offset of a heart portion of the texture

    private static final int BAR_WIDTH = 120; //width of the bar portion of the texture
    private static final int BAR_HEIGHT = 5; //height of the bar portion of the texture
    private static final int BAR_OFFSET_X = 9; //x offset of the bar portion of the texture
    private static final int BAR_OFFSET_Y = 3; //y offset of the bar portion of the texture

    //=========== Texture Offsets ===========\\
    private static final int NORMAL_U = 0; //normal heart texture variant u coordinate
    private static final int BEAT_U = 12; //heartBeat heart texture variant u coordinate
    private static final int BAR_U = 24; //bar texture variant u coordinate

    private static final int NORMAL_V = 0; //normal texture variant v coordinate
    private static final int DAMAGE_V = 12; //damage texture variant v coordinate
    private static final int HEAL_V = 24; //heal texture variant v coordinate
    private static final int DAMAGE_SUB_V = 36; //damage subbar texture variant v coordinate
    private static final int HEAL_SUB_V = 48; //heal subbar texture variant v coordinate

    //=========== Sounds ===========\\
    private static final SoundEvent WARNING_SOUND = ModSounds.HUD_HEARTBEAT.get(); //the sound that will be played when the warning starts 
    private static final float WARNING_SOUND_VOLUME = 1.0f; //the volume of the warning sound

    //=========== Ticks ===========\\
    private static final int DAMAGE_ALT_TICKS = 3; //the amount of ticks the damage texture will be shown when hurt
    private static final int HEAL_ALT_TICKS = 3; //the amount of ticks the heal texture will be shown when healing
    
    private static final int DAMAGE_SUB_DELAY = 10; //the amount of ticks the damage subbar will wait before starting to decrease
    private static final int HEAL_SUB_DELAY = 10; //the amount of ticks the heal subbar will wait before starting to decrease
    private static final int WARNING_BEAT_TICKS = 3; //the amount of ticks the heart will use the heartBeat texture
    
    //=========== Other ===========\\
    private static final float DAMAGE_SUB_DECREASE = 0.1f; //the amount the damage subbar will decrease each tick
    private static final float HEAL_SUB_INCREASE = 0.1f; //the amount the heal subbar will decrease each tick
    private static final int WARNING_THRESHOLD = 4; //the health threshold for the first warning speed
    private static final int WARNING_INTERVAL_MULTIPLIER = 10; //the multiplier used to calculate the interval for each warning speed (intervalTicks = health * multiplier)
    

    //============================== Variables ==============================\\
    private BarVariant variant = BarVariant.NORMAL; //health bar variant (different textures for different status effects and such)

    private float previousValue; //previous health value
    
    private int damageAltCounter = 0; //tick counter for alternate texture
    private boolean damageAlt = false; //whether the damage texture is currently shown
    
    private int healAltCounter = 0; //tick counter for alternate texture
    private boolean healAlt = false; //whether the heal texture is currently shown
    
    private float damageSubValue; //the value of the damage subbar
    private int damageSubCounter = 0; //tick counter for damage subbar
    private boolean damageSub = false; //whether the damage subbar is currently shown

    private float healSubValue; //the value of the heal subbar
    private int healSubCounter = 0; //tick counter for heal subbar
    private boolean healSub = false; //whether the heal subbar is currently shown
    
    private boolean warning = false; //if the warning should be shown
    private int warningCounter = 0; //tick counter for warning
    private SoundInstance warningSound; //the sound instance for the heartBeat sound
    private boolean heartBeat = false; //if it should use the heartBeat texture instead

    
    //============================== Overrides ==============================\\
    public HealthBar() { //set size
        this.width = 130;
        this.height = 11;
    }
    
    @Override
    public boolean shouldRender(Player player) { //check if health bar should render
        if (!getGameMode().canHurtPlayer()) {return false;} //if player can't be hurt return false
        return true; //otherwise return true
    }

    @Override
    public void getParameters(Player player) { //get health values
        previousValue = value; //set previous value
        maxValue = player.getMaxHealth();
        value = player.getHealth();
        
        if (value < previousValue) { //if current value is less than previous value (took damage)
            damageAlt = true; damageAltCounter = 0; //activate damage texture
            damageSub = true; damageSubCounter = 0; //activate damage subbar
            healAlt = false; //cancel heal texture (if active)
            healSub = false; //cancel healing subbar (if active)
            if (value <= WARNING_THRESHOLD) { //if health is below threshold
                warning = true; //make sure warning is active
            } else { //deactivate warning if health is above threshold
                warning = false;
                heartBeat = false;
                stopWarningSound();
            }
        }
        
        else if (value > previousValue) { //if current value is greater than previous value (healed)
            healAlt = true; healAltCounter = 0; //set heal texture to true
            healSub = true; healSubCounter = 0; //activate healing subbar
            damageAlt = false; //cancel damage texture (if active)
            damageSub = false;//cancel damage subbar
            if (value <= WARNING_THRESHOLD) { //if health is below threshold
                warning = true; //activate warning
                heartBeat = false; //make sure heartBeat is false
                warningCounter = 0; //reset warning counter
            } else { //deactivate warning if health is above threshold
                warning=false;
                heartBeat=false;
                stopWarningSound();
            }
        }
        
        //sorted by priority
        if (player.isOnFire() && !player.hasEffect(MobEffects.FIRE_RESISTANCE)) {variant = BarVariant.BURNING;} //set variant to burning
        else if (player.hasEffect(MobEffects.WITHER)) {variant = BarVariant.WITHER;} //set variant to wither
        else if (player.getAirSupply() <= 0) {variant = BarVariant.DROWNING;} //set variant to drowning
        else if (player.isFullyFrozen()) {variant = BarVariant.FREEZING;} //set variant to freezing
        else if (TemperatureHelper.isFullyHyperthermic(player)) {variant = BarVariant.OVERHEATING;} //set variant to overheating
        else if (player.hasEffect(MobEffects.POISON)) {variant = BarVariant.POISON;} //set variant to poison
        else if (player.hasEffect(MobEffects.REGENERATION)) {variant = BarVariant.REGENERATION;} //set variant to regeneration
        else {variant = BarVariant.NORMAL;} //set variant to normal
    }
    
    @Override
    public void tick() { //each client tick
        float barFillValue = (ClientConfig.ROUND_HEALTH_TEXTURE) ? valueRound(value) : value; //get value for bar rendering

        if (damageAlt) { //if damage texture is active
            if (damageAltCounter >= DAMAGE_ALT_TICKS) { //if enough time has passed
                damageAlt = false; //set damage texture to false
                damageAltCounter = 0; //reset tick counter
            } else {damageAltCounter++;} //increase tick counter
        }

        if (healAlt) { //if heal texture is active
            if (healAltCounter >= HEAL_ALT_TICKS) { //if enough time has passed
                healAlt = false; //set heal texture to false
                healAltCounter = 0; //reset tick counter
            } else {healAltCounter++;} //increase tick counter
        }

        if (damageSub) { //if damage subbar is active
            if (damageSubCounter < DAMAGE_SUB_DELAY) {damageSubCounter++;} //wait for delay
            else { //after delay
                if (damageSubValue > barFillValue) { //if subbar value is greater than current value
                    damageSubValue = Math.max(barFillValue, damageSubValue - DAMAGE_SUB_DECREASE); //decrease subbar value
                    if (damageSubValue <= barFillValue) { //if subbar value reached current value
                        damageSub = false; //hide subbar
                        damageSubCounter = 0; //reset tick counter
                    }
                }
            }
        } else {damageSubValue = barFillValue;} //set subbar value to current value if not displaying subbar (so once we should display it, it will have the correct value)

        if (healSub) { //if heal subbar is active
            if (healSubCounter < HEAL_SUB_DELAY) {healSubCounter++;} //wait for delay
            else { //after delay
                if (healSubValue < barFillValue) { //if subbar value is less than current value
                    healSubValue = Math.min(barFillValue, healSubValue + HEAL_SUB_INCREASE); //increase subbar value
                    if (healSubValue >= barFillValue) { //if subbar value reached current value
                        healSub = false; //hide subbar
                        healSubCounter = 0; //reset tick counter
                    }
                }
            }
        } else {healSubValue = barFillValue;} //set subbar value to current value if not displaying subbar (so once we should display it, it will have the correct value)
    
        if (warning) { //if warning is active
            int interval = valueRound(value) * WARNING_INTERVAL_MULTIPLIER; //calculate interval
            if (warningCounter >= interval+WARNING_BEAT_TICKS) { //if beat ticks are over (start anew)
                warningCounter = 0; //reset counter
                heartBeat = false; //set heartBeat to false
            } else if (warningCounter == interval) { //if interval is over (start beat ticks)
                heartBeat = true; //set heartBeat to true
                playWarningSound(); //play warning sound
                warningCounter++; //increase counter (to prevent it from playing the sound again)      
            } else {warningCounter++;} //increase counter
        }
    }

    @Override
    public void render() { //render health bar
        renderSimpleTexture(BACKGROUND, BACKGROUND_WIDTH, BACKGROUND_HEIGHT, x, y); //render background

        float fillValue = (ClientConfig.ROUND_HEALTH_TEXTURE) ? valueRound(value) : value; //get value for bar rendering
        
        if (damageSub) { //if damage subbar is active
            renderDamageSubBar(damageSubValue, maxValue, x, y); //render damage subbar
            if (damageAlt) {renderDamageBar(fillValue, maxValue, x, y);} //render damage bar
            else if (healAlt) {renderHealingBar(fillValue, maxValue, x, y);} //render healing bar
            else {renderHealthBar(fillValue, maxValue, x, y);} //render normal bar
        } else if (healSub) { //if heal subbar is active
            renderHealSubBar(fillValue, maxValue, x, y); //render heal subbar
            if (damageAlt) {renderDamageBar(healSubValue, maxValue, x, y);} //render damage bar
            else if (healAlt) {renderHealingBar(healSubValue, maxValue, x, y);} //render healing bar
            else {renderHealthBar(healSubValue, maxValue, x, y);} //render normal bar
        } else { //if no subbar is active render everything as normal
            if (damageAlt) {renderDamageBar(fillValue, maxValue, x, y);} //render damage bar
            else if (healAlt) {renderHealingBar(fillValue, maxValue, x, y);} //render healing bar
            else {renderHealthBar(fillValue, maxValue, x, y);} //render normal bar
        }

        if (ClientConfig.ROUND_HEALTH_VALUE) {renderIntValueText(valueRound(value), Math.round(maxValue), Txt.DEFAULT, 0.6f, x+9, y+3, BAR_WIDTH, BAR_HEIGHT);} //render health value
        else {renderFloatValueText(value, maxValue, Txt.DEFAULT, 0.6f, x+9, y+3, BAR_WIDTH, BAR_HEIGHT);} //render health value
    }


    //============================== Helpers ==============================\\
    private void renderHeart(int v, int x, int y) { //render heart icon
        if (!heartBeat) {renderPartialTexture(variant.texture, TEXTURE_WIDTH, TEXTURE_HEIGHT, NORMAL_U, v, ICON_WIDTH, ICON_HEIGHT, x+ICON_OFFSET_X, y+ICON_OFFSET_Y);} //render the normal heart texture
        else {renderPartialTexture(variant.texture, TEXTURE_WIDTH, TEXTURE_HEIGHT, BEAT_U, v, ICON_WIDTH, ICON_HEIGHT, x+ICON_OFFSET_X, y+ICON_OFFSET_Y);} //render the heartBeat heart texture

    }

    //=========== Alt ===========\\
    private void renderHealthBar(float value, float maxValue, int x, int y) { //render the normal bar texture
        renderHeart(NORMAL_V, x, y); //render the heart icon
        renderBarLeft(variant.texture, TEXTURE_WIDTH, TEXTURE_HEIGHT, BAR_U, NORMAL_V, BAR_WIDTH, BAR_HEIGHT, value, maxValue, x+BAR_OFFSET_X, y+BAR_OFFSET_Y);
    }

    private void renderDamageBar(float value, float maxValue, int x, int y) { //render the damage bar texture (basically just a different color that it uses instead of the normal one when taking damage)
        renderHeart(DAMAGE_V, x, y); //render the heart icon
        renderBarLeft(variant.texture, TEXTURE_WIDTH, TEXTURE_HEIGHT, BAR_U, DAMAGE_V, BAR_WIDTH, BAR_HEIGHT, value, maxValue, x+BAR_OFFSET_X, y+BAR_OFFSET_Y);
    }

    private void renderHealingBar(float value, float maxValue, int x, int y) { //render the regenerating bar texture (basically just a different color that it uses instead of the normal one when regenerating)
        renderHeart(HEAL_V, x, y); //render the heart icon
        renderBarLeft(variant.texture, TEXTURE_WIDTH, TEXTURE_HEIGHT, BAR_U, HEAL_V, BAR_WIDTH, BAR_HEIGHT, value, maxValue, x+BAR_OFFSET_X, y+BAR_OFFSET_Y);
    }

    //=========== SubBar ===========\\
    private void renderDamageSubBar(float value, float maxValue, int x, int y) { //render the damage sub bar texture (the subbar that shows the delayed health value when taking damage to make it look smoother)
        renderBarLeft(variant.texture, TEXTURE_WIDTH, TEXTURE_HEIGHT, BAR_U, DAMAGE_SUB_V, BAR_WIDTH, BAR_HEIGHT, value, maxValue, x+BAR_OFFSET_X, y+BAR_OFFSET_Y);
    }

    private void renderHealSubBar(float value, float maxValue, int x, int y) { //render the healing sub bar texture (the subbar that shows the future health value when healing to make it look smoother)
        renderBarLeft(variant.texture, TEXTURE_WIDTH, TEXTURE_HEIGHT, BAR_U, HEAL_SUB_V, BAR_WIDTH, BAR_HEIGHT, value, maxValue, x+BAR_OFFSET_X, y+BAR_OFFSET_Y);
    }

    //=========== Sound ===========\\
    private void playWarningSound() { //play sound
        stopWarningSound(); //stop sound if it's playing
        warningSound = playSound(WARNING_SOUND, WARNING_SOUND_VOLUME); //play sound
    }

    private void stopWarningSound() { //stop sound
        if (warningSound != null) { //if sound is playing
            stopSound(warningSound); //stop sound
            warningSound = null; //set sound to null
        }
    }

    //============================== Enum ==============================\\
    private enum BarVariant {
        NORMAL(ResourceLocation.fromNamespaceAndPath(TownTime.MOD_ID, "textures/hud/health/normal.png")),
        POISON(ResourceLocation.fromNamespaceAndPath(TownTime.MOD_ID, "textures/hud/health/poison.png")),
        REGENERATION(ResourceLocation.fromNamespaceAndPath(TownTime.MOD_ID, "textures/hud/health/regeneration.png")),
        WITHER(ResourceLocation.fromNamespaceAndPath(TownTime.MOD_ID, "textures/hud/health/wither.png")),
        DROWNING(ResourceLocation.fromNamespaceAndPath(TownTime.MOD_ID, "textures/hud/health/drowning.png")),
        FREEZING(ResourceLocation.fromNamespaceAndPath(TownTime.MOD_ID, "textures/hud/health/freezing.png")),
        OVERHEATING(ResourceLocation.fromNamespaceAndPath(TownTime.MOD_ID, "textures/hud/health/overheating.png")),
        BURNING(ResourceLocation.fromNamespaceAndPath(TownTime.MOD_ID, "textures/hud/health/burning.png"));
        
        public final ResourceLocation texture;
        BarVariant(ResourceLocation texture) {this.texture = texture;}
    }
}
