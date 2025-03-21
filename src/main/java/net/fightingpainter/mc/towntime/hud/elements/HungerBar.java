package net.fightingpainter.mc.towntime.hud.elements;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.fightingpainter.mc.towntime.ClientConfig;
import net.fightingpainter.mc.towntime.TownTime;
import net.fightingpainter.mc.towntime.util.Txt;


public class HungerBar extends BaseBarElement{
    //============================== Settings ==============================\\ //TODO: I need to move some of these to the config (also maybe I should sort it a bit better)
    //=========== Texture ===========\\
    private static final ResourceLocation BACKGROUND = ResourceLocation.fromNamespaceAndPath(TownTime.MOD_ID, "textures/hud/hunger/background.png");
    private static final int BACKGROUND_WIDTH = 87; //width of the background texture
    private static final int BACKGROUND_HEIGHT = 9; //height of the background texture

    private static final int TEXTURE_WIDTH = 185; //width of the hunger variant texture
    private static final int TEXTURE_HEIGHT = 39; //height of the hunger variant texture

    private static final int ICON_WIDTH = 7; //width of a drumstick portion of the texture
    private static final int ICON_HEIGHT = 7; //height of a drumstick portion of the texture
    private static final int ICON_OFFSET_X = 1; //x offset of a drumstick portion of the texture
    private static final int ICON_OFFSET_Y = 1; //y offset of a drumstick portion of the texture

    private static final int BAR_WIDTH = 80; //width of the bar portion of the texture
    private static final int BAR_HEIGHT = 5; //height of the bar portion of the texture
    private static final int BAR_OFFSET_X = 6; //x offset of the bar portion of the texture
    private static final int BAR_OFFSET_Y = 2; //y offset of the bar portion of the texture

    //=========== Texture Offsets ===========\\
    private static final int NORMAL_U = 0; //normal drumstick u coordinate
    private static final int HUNGRY_U = 8; //hungry drumstick u coordinate
    private static final int STARVING_U = 16; //starving drumstick u coordinate
    private static final int BAR_U = 24; //bar u coordinate
    private static final int SATURATION_U = 105; //saturation bar u coordinate

    private static final int NORMAL_V = 0; //normal hunger bar v coordinate
    private static final int DECREASE_V = 8; //v coordinate for alt texture when hunger goes down
    private static final int INCREASE_V = 16; //v coordinate for alt texture when hunger goes up
    private static final int INCREASE_SUB_V = 24; //v coordinate for hunger increase subbar texture
    private static final int PREVIEW_SUB_V = 32; //v coordinate for hunger preview subbar texture

    //=========== Ticks ===========\\
    private static final int DECREASE_ALT_TICKS = 3; //how many ticks to show the decrease texture
    private static final int INCREASE_ALT_TICKS = 3; //how many ticks to show the increase texture
    private static final int INCREASE_SUB_DELAY = 10; //how many ticks to wait before increasing the increase subbar

    //=========== Other ===========\\
    private static final int HUNGRY_THRESHOLD = 6; //hunger level threshold for hungry texture
    private static final int STARVING_THRESHOLD = 0; //hunger level threshold for starving texture
    private static final float INCREASE_SUB_INCREASE = 0.1f; //how much to increase the hunger increase subbar each tick


    //============================== Variables ==============================\\
    private BarVariant variant = BarVariant.NORMAL; //hunger bar variant (different textures for different status effects and such)
    private DrumstickVariation drumstick = DrumstickVariation.NORMAL; //drumstick variant (looks different depending on hunger level, normal, hungry if can't do stuff like sprint, and starving for once you take damage)

    //=========== Hunger ===========\\
    private float previousValue; //previous hunger value
    
    private int decreaseAltCounter = 0; //tick counter for alternate texture
    private boolean decreaseAlt = false; //if alt texture is currently shown
    
    private int increaseAltCounter = 0; //tick counter for alternate texture
    private boolean increaseAlt = false; //if alt texture is currently shown

    private float increaseSubValue; //value of the hunger increase subbar
    private int increaseSubCounter = 0; //tick counter for the hunger increase subbar
    private boolean increaseSub = false; //if the hunger increase subbar is currently shown
    
    //=========== Saturation ===========\\
    private float saturationValue; //saturation level (this is maybe a bit confusing due to the same name but the float saturation is the saturation level of the player, and the SaturationVariant is for the saturation effect)
    private float previousSaturationValue; //previous saturation value
    
    private int saturationDecreaseCounter = 0; //tick counter for alternate texture
    private boolean saturationDecrease = false; //if alt texture is currently shown

    private int saturationIncreaseCounter = 0; //tick counter for alternate texture
    private boolean saturationIncrease = false; //if alt texture is currently shown

    private float saturationIncreaseSubValue; //value of the saturation increase subbar
    private int saturationIncreaseSubCounter = 0; //tick counter for the saturation increase subbar
    private boolean saturationIncreaseSub = false; //if the saturation increase subbar is currently shown

    
    //============================== Overrides ==============================\\
    public HungerBar() { //set size
        this.width = 87;
        this.height = 9;
    }

    @Override
    public boolean shouldRender(Player player) {//check gamemode
        if (!getGameMode().canHurtPlayer()) {return false;} //if player can't be hurt return false
        return true; //otherwise return true
    }

    @Override
    public void getParameters(Player player) {
        previousValue = value; //set previous value
        value = (int)player.getFoodData().getFoodLevel();
        maxValue = 20; //this is hardcoded because the max hunger level never changes
        
        if (value < previousValue) { //if current value is less than previous value (hunger decreased)
            decreaseAlt = true; decreaseAltCounter = 0; //set decrease alt texture
            increaseAlt = false; //unset increase alt texture
            increaseSub = false; //deactivate increase subbar
        }
        
        else if (value > previousValue) { //if current value is greater than previous value (hunger increase)
            increaseAlt = true; increaseAltCounter = 0; //set increase alt texture
            increaseSub = true; increaseSubCounter = 0; //activate increase subbar
            decreaseAlt = false; //unset decrease alt texture
        }
        
        previousSaturationValue = saturationValue; //set previous saturation value
        saturationValue = player.getFoodData().getSaturationLevel(); //get saturation level
        
        if (saturationValue < previousSaturationValue) { //if current value is less than previous value (saturation decreased)
            saturationDecrease = true; saturationDecreaseCounter = 0; //set decrease alt texture
            saturationIncrease = false; //unset increase alt texture
            saturationIncreaseSub = false; //deactivate increase subbar
        }
        
        else if (saturationValue > previousSaturationValue) { //if current value is greater than previous value (saturation increase)
            saturationIncrease = true; saturationIncreaseCounter = 0; //set increase alt texture
            saturationIncreaseSub = true; saturationIncreaseSubCounter = 0; //activate increase subbar
            saturationDecrease = false; //unset decrease alt texture
        }

        //drumstick
        if (value <= STARVING_THRESHOLD) {drumstick = DrumstickVariation.STARVING;} //if below starving threshold set drumstick to starving
        else if (value <= HUNGRY_THRESHOLD) {drumstick = DrumstickVariation.HUNGRY;} //if below hungry threshold set drumstick to hungry
        else {drumstick = DrumstickVariation.NORMAL;} //else set drumstick to normal

        //sorted by priority
        if (player.hasEffect(MobEffects.HUNGER)) {variant = BarVariant.HUNGER;} //set variant to hunger
        else if (player.hasEffect(MobEffects.SATURATION)) {variant = BarVariant.SATURATION;} //set variant to saturation
        else {variant = BarVariant.NORMAL;} //set variant to normal
    }
    
    @Override
    public void tick() { //each client tick

        if (decreaseAlt) { //if decrease alt texture is shown
            if (decreaseAltCounter >= DECREASE_ALT_TICKS) { //if enough time has passed
                decreaseAlt = false; //set texture back to normal
                decreaseAltCounter = 0; //reset tick counter
            } else {decreaseAltCounter++;} //increase tick counter
        }

        if (increaseAlt) { //if increase alt texture is shown
            if (increaseAltCounter >= INCREASE_ALT_TICKS) { //if enough time has passed
                increaseAlt = false; //set texture back to normal
                increaseAltCounter = 0; //reset tick counter
            } else {increaseAltCounter++;} //increase tick counter
        }
        
        if (increaseSub) { //if increase subbar is active
            if (increaseSubCounter < INCREASE_SUB_DELAY) {increaseSubCounter++;} //wait for delay
            else { //after delay
                if (increaseSubValue < value) { //if subbar value is less than current value
                    increaseSubValue = Math.min(value, increaseSubValue + INCREASE_SUB_INCREASE); //increase subbar value
                    if (increaseSubValue >= value) { //if subbar value reached current value
                        increaseSub = false; //hide subbar
                        increaseSubCounter = 0; //reset tick counter
                    }
                }
            }
        } else {increaseSubValue = value;} //set subbar value to current value if not displaying subbar (so once we should display it, it will have the correct value)

        float saturationFillValue = (ClientConfig.ROUND_SATURATION_TEXTURE) ? Math.round(saturationValue) : saturationValue; //round saturation value if config is set to round saturation texture
        
        if (saturationDecrease) { //if decrease alt texture is shown
            if (saturationDecreaseCounter >= DECREASE_ALT_TICKS) { //if enough time has passed
                saturationDecrease = false; //set texture back to normal
                saturationDecreaseCounter = 0; //reset tick counter
            } else {saturationDecreaseCounter++;} //increase tick counter
        }

        if (saturationIncrease) { //if increase alt texture is shown
            if (saturationIncreaseCounter >= INCREASE_ALT_TICKS) { //if enough time has passed
                saturationIncrease = false; //set texture back to normal
                saturationIncreaseCounter = 0; //reset tick counter
            } else {saturationIncreaseCounter++;} //increase tick counter
        }

        if (saturationIncreaseSub) { //if increase subbar is active
            if (saturationIncreaseSubCounter < INCREASE_SUB_DELAY) {saturationIncreaseSubCounter++;} //wait for delay
            else { //after delay
                if (saturationIncreaseSubValue < saturationFillValue) { //if subbar value is less than current value
                    saturationIncreaseSubValue = Math.min(saturationFillValue, saturationIncreaseSubValue + INCREASE_SUB_INCREASE); //increase subbar value
                    if (saturationIncreaseSubValue >= saturationFillValue) { //if subbar value reached current value
                        saturationIncreaseSub = false; //hide subbar
                        saturationIncreaseSubCounter = 0; //reset tick counter
                    }
                }
            }
        } else {saturationIncreaseSubValue = saturationFillValue;} //set subbar value to current value if not displaying subbar (so once we should display it, it will have the correct value)
        
    }

    @Override
    public void render() {
        renderSimpleTexture(BACKGROUND, BACKGROUND_WIDTH, BACKGROUND_HEIGHT, x, y); //render background

        if (increaseSub) { //if increase subbar is active
            renderHungerIncreaseSub(value, maxValue, x, y); //render increase subbar (we actually render the normal bar as the subbar)
            if (decreaseAlt) {renderHungerDecrease(increaseSubValue, maxValue, x, y);} //render decrease texture (we render the subbar as a fake main bar making it seem like it slowly is increasing)
            else if (increaseAlt) {renderHungerIncrease(increaseSubValue, maxValue, x, y);} //render increase texture
            else {renderHunger(increaseSubValue, maxValue, x, y);} //render normal texture
        } else {
            if (decreaseAlt) {renderHungerDecrease(value, maxValue, x, y);} //render decrease texture
            else if (increaseAlt) {renderHungerIncrease(value, maxValue, x, y);} //render increase texture
            else {renderHunger(value, maxValue, x, y);} //render normal texture
        }


        float saturationFillValue = (ClientConfig.ROUND_SATURATION_TEXTURE) ? Math.round(saturationValue) : saturationValue; //round saturation value if config is set to round saturation texture

        if (saturationIncreaseSub) { //if increase subbar is active
            renderSaturationIncreaseSub(saturationFillValue, maxValue, x, y); //render increase subbar (we actually render the normal bar as the subbar)
            if (saturationDecrease) {renderSaturationDecrease(saturationIncreaseSubValue, maxValue, x, y);} //render decrease texture (we render the subbar as a fake main bar making it seem like it slowly is increasing)
            else if (saturationIncrease) {renderSaturationIncrease(saturationIncreaseSubValue, maxValue, x, y);} //render increase texture
            else {renderSaturation(saturationIncreaseSubValue, maxValue, x, y);} //render normal texture
        } else {
            if (saturationDecrease) {renderSaturationDecrease(saturationFillValue, maxValue, x, y);} //render decrease texture
            else if (saturationIncrease) {renderSaturationIncrease(saturationFillValue, maxValue, x, y);} //render increase texture
            else {renderSaturation(saturationFillValue, maxValue, x, y);} //render normal texture
        }

        //render hunger bar value as text
        if (ClientConfig.HUNGER_VALUE) {renderIntValueText((int)value, (int)maxValue, Txt.DEFAULT, ClientConfig.HUNGER_VALUE_SIZE, x+BAR_OFFSET_X, y+BAR_OFFSET_Y, BAR_WIDTH, BAR_HEIGHT);}
    }

    //============================== Helpers ==============================\\
    private void renderDrumstick(int v, int x, int y) { //render drumstick
        switch (drumstick) {
            case NORMAL: renderPartialTexture(variant.texture, TEXTURE_WIDTH, TEXTURE_HEIGHT, NORMAL_U, v, ICON_WIDTH, ICON_HEIGHT, x+ICON_OFFSET_X, y+ICON_OFFSET_Y); break;
            case HUNGRY: renderPartialTexture(variant.texture, TEXTURE_WIDTH, TEXTURE_HEIGHT, HUNGRY_U, v, ICON_WIDTH, ICON_HEIGHT, x+ICON_OFFSET_X, y+ICON_OFFSET_Y); break;
            case STARVING: renderPartialTexture(variant.texture, TEXTURE_WIDTH, TEXTURE_HEIGHT, STARVING_U, v, ICON_WIDTH, ICON_HEIGHT, x+ICON_OFFSET_X, y+ICON_OFFSET_Y); break;
        }
    }

    //=========== Hunger Bar ===========\\
    private void renderHunger(float value, float maxValue, int x, int y) { //render hunger bar
        renderDrumstick(NORMAL_V, x, y); //render drumstick
        renderBarLeft(variant.texture, TEXTURE_WIDTH, TEXTURE_HEIGHT, BAR_U, NORMAL_V, BAR_WIDTH, BAR_HEIGHT, value, maxValue, x+BAR_OFFSET_X, y+BAR_OFFSET_Y);
    }

    private void renderHungerDecrease(float value, float maxValue, int x, int y) { //render hunger decrease bar
        renderDrumstick(DECREASE_V, x, y); //render drumstick
        renderBarLeft(variant.texture, TEXTURE_WIDTH, TEXTURE_HEIGHT, BAR_U, DECREASE_V, BAR_WIDTH, BAR_HEIGHT, value, maxValue, x+BAR_OFFSET_X, y+BAR_OFFSET_Y);
    }

    private void renderHungerIncrease(float value, float maxValue, int x, int y) { //render hunger increase bar
        renderDrumstick(INCREASE_V, x, y); //render drumstick
        renderBarLeft(variant.texture, TEXTURE_WIDTH, TEXTURE_HEIGHT, BAR_U, INCREASE_V, BAR_WIDTH, BAR_HEIGHT, value, maxValue, x+BAR_OFFSET_X, y+BAR_OFFSET_Y);
    }

    private void renderHungerIncreaseSub(float value, float maxValue, int x, int y) { //render hunger increase sub bar
        renderBarLeft(variant.texture, TEXTURE_WIDTH, TEXTURE_HEIGHT, BAR_U, INCREASE_SUB_V, BAR_WIDTH, BAR_HEIGHT, value, maxValue, x+BAR_OFFSET_X, y+BAR_OFFSET_Y);
    }

    private void renderHungerPreviewSub(float value, float maxValue, int x, int y) { //render hunger preview sub bar
        renderBarLeft(variant.texture, TEXTURE_WIDTH, TEXTURE_HEIGHT, BAR_U, PREVIEW_SUB_V, BAR_WIDTH, BAR_HEIGHT, value, maxValue, x+BAR_OFFSET_X, y+BAR_OFFSET_Y);
    }

    //=========== Saturation Bar ===========\\
    private void renderSaturation(float value, float maxValue, int x, int y) { //render saturation bar
        renderBarLeft(variant.texture, TEXTURE_WIDTH, TEXTURE_HEIGHT, SATURATION_U, NORMAL_V, BAR_WIDTH, BAR_HEIGHT, value, maxValue, x+BAR_OFFSET_X, y+BAR_OFFSET_Y);
    }

    private void renderSaturationDecrease(float value, float maxValue, int x, int y) { //render saturation decrease bar
        renderBarLeft(variant.texture, TEXTURE_WIDTH, TEXTURE_HEIGHT, SATURATION_U, DECREASE_V, BAR_WIDTH, BAR_HEIGHT, value, maxValue, x+BAR_OFFSET_X, y+BAR_OFFSET_Y);
    }

    private void renderSaturationIncrease(float value, float maxValue, int x, int y) { //render saturation increase bar
        renderBarLeft(variant.texture, TEXTURE_WIDTH, TEXTURE_HEIGHT, SATURATION_U, INCREASE_V, BAR_WIDTH, BAR_HEIGHT, value, maxValue, x+BAR_OFFSET_X, y+BAR_OFFSET_Y);
    }

    private void renderSaturationIncreaseSub(float value, float maxValue, int x, int y) { //render saturation increase sub bar
        renderBarLeft(variant.texture, TEXTURE_WIDTH, TEXTURE_HEIGHT, SATURATION_U, INCREASE_SUB_V, BAR_WIDTH, BAR_HEIGHT, value, maxValue, x+BAR_OFFSET_X, y+BAR_OFFSET_Y);
    }

    private void renderSaturationPreviewSub(float value, float maxValue, int x, int y) { //render saturation preview sub bar
        renderBarLeft(variant.texture, TEXTURE_WIDTH, TEXTURE_HEIGHT, SATURATION_U, PREVIEW_SUB_V, BAR_WIDTH, BAR_HEIGHT, value, maxValue, x+BAR_OFFSET_X, y+BAR_OFFSET_Y);
    }


    //============================== Enum ==============================\\
    private enum BarVariant {
        NORMAL(ResourceLocation.fromNamespaceAndPath(TownTime.MOD_ID, "textures/hud/hunger/normal.png")),
        HUNGER(ResourceLocation.fromNamespaceAndPath(TownTime.MOD_ID, "textures/hud/hunger/hunger.png")),
        SATURATION(ResourceLocation.fromNamespaceAndPath(TownTime.MOD_ID, "textures/hud/hunger/saturation.png"));

        public final ResourceLocation texture;
        BarVariant(ResourceLocation texture) {this.texture = texture;}
    }

    private enum DrumstickVariation {NORMAL, HUNGRY, STARVING}
}
