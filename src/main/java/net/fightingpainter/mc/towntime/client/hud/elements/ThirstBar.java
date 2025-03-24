package net.fightingpainter.mc.towntime.client.hud.elements;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import toughasnails.api.thirst.ThirstHelper;
import toughasnails.api.potion.TANEffects;
import net.fightingpainter.mc.towntime.ClientConfig;
import net.fightingpainter.mc.towntime.TownTime;
import net.fightingpainter.mc.towntime.food.ConsumableHelper;
import net.fightingpainter.mc.towntime.food.SustenanceProperties;
import net.fightingpainter.mc.towntime.food.SustinanceData;
import net.fightingpainter.mc.towntime.util.Txt;


public class ThirstBar extends BaseBarElement{
    //============================== Settings ==============================\\ //TODO: I need to move some of these to the config (also maybe I should sort it a bit better)
    //=========== Texture ===========\\
    private static final ResourceLocation BACKGROUND = ResourceLocation.fromNamespaceAndPath(TownTime.MOD_ID, "textures/hud/thirst/background.png");
    private static final int BACKGROUND_WIDTH = 87; //width of the background texture
    private static final int BACKGROUND_HEIGHT = 9; //height of the background texture

    private static final int TEXTURE_WIDTH = 185; //width of the thirst variant texture
    private static final int TEXTURE_HEIGHT = 39; //height of the thirst variant texture

    private static final int ICON_WIDTH = 7; //width of a droplet portion of the texture
    private static final int ICON_HEIGHT = 7; //height of a droplet portion of the texture
    private static final int ICON_OFFSET_X = 1; //x offset of a droplet portion of the texture
    private static final int ICON_OFFSET_Y = 1; //y offset of a droplet portion of the texture

    private static final int BAR_WIDTH = 80; //width of the bar portion of the texture
    private static final int BAR_HEIGHT = 5; //height of the bar portion of the texture
    private static final int BAR_OFFSET_X = 6; //x offset of the bar portion of the texture
    private static final int BAR_OFFSET_Y = 2; //y offset of the bar portion of the texture

    //=========== Texture Offsets ===========\\
    private static final int NORMAL_U = 0; //normal droplet u coordinate
    private static final int THIRSTY_U = 8; //thirsty droplet u coordinate
    private static final int DEHYDRATED_U = 16; //dehydrated droplet u coordinate
    private static final int BAR_U = 24; //bar u coordinate
    private static final int HYDRATION_U = 105; //hydration bar u coordinate

    private static final int NORMAL_V = 0; //normal thirst bar v coordinate
    private static final int DECREASE_V = 8; //v coordinate for alt texture when thirst goes down
    private static final int INCREASE_V = 16; //v coordinate for alt texture when thirst goes up
    private static final int INCREASE_SUB_V = 24; //v coordinate for thirst increase subbar texture
    private static final int PREVIEW_SUB_V = 32; //v coordinate for thirst preview subbar texture

    //=========== Ticks ===========\\
    private static final int DECREASE_ALT_TICKS = 3; //how many ticks to show the decrease texture
    private static final int INCREASE_ALT_TICKS = 3; //how many ticks to show the increase texture
    private static final int INCREASE_SUB_DELAY = 10; //how many ticks to wait before increasing the increase subbar

    //=========== Other ===========\\
    private static final int THIRSTY_THRESHOLD = 6; //thirst level threshold for thirsty texture
    private static final int DEHYDRATED_THRESHOLD = 0; //thirst level threshold for dehydrated texture
    private static final float INCREASE_SUB_INCREASE = 0.3f; //how much to increase the thirst increase subbar each tick

    //=========== Preview ===========\\
    private static final float PREVIEW_MAX_ALPHA = 0.85f; //max alpha value for preview
    private static final float PREVIEW_MIN_ALPHA = 0.1f; //min alpha value for preview
    private static final float PREVIEW_ALPHA_CHANGE = 0.025f; //how much to change the alpha value each tick

    //============================== Variables ==============================\\
    private BarVariant variant = BarVariant.NORMAL; //thirst bar variant (different textures for different status effects and such)
    private DropletVariation droplet = DropletVariation.NORMAL; //droplet variant (looks different depending on thirst level, normal, thirsty, dehydrated)

    private boolean doPreview = false; //previews are possible
    private boolean previewAlphaIncrease = true; //if preview alpha is increasing or decreasing
    private float previewAlpha; //alpha value for preview

    //=========== Thirst ===========\\
    private float previousValue; //previous thirst value
    
    private int decreaseAltCounter = 0; //tick counter for alternate texture
    private boolean decreaseAlt = false; //if alt texture is currently shown
    
    private int increaseAltCounter = 0; //tick counter for alternate texture
    private boolean increaseAlt = false; //if alt texture is currently shown

    private float increaseSubValue; //value of the thirst increase subbar
    private int increaseSubCounter = 0; //tick counter for the thirst increase subbar
    private boolean increaseSub = false; //if the thirst increase subbar is currently shown

    private float previewValue; //value of the thirst preview subbar
    private boolean previewSub = false; //if the thirst preview subbar is currently shown
    
    //=========== Hydration ===========\\
    private float hydrationValue; //hydration level (this is maybe a bit confusing due to the same name but the float hydration is the hydration level of the player, and the HydrationVariant is for the hydration effect)
    private float previousHydrationValue; //previous hydration value
    
    private int hydrationDecreaseCounter = 0; //tick counter for alternate texture
    private boolean hydrationDecrease = false; //if alt texture is currently shown

    private int hydrationIncreaseCounter = 0; //tick counter for alternate texture
    private boolean hydrationIncrease = false; //if alt texture is currently shown

    private float hydrationIncreaseSubValue; //value of the hydration increase subbar
    private int hydrationIncreaseSubCounter = 0; //tick counter for the hydration increase subbar
    private boolean hydrationIncreaseSub = false; //if the hydration increase subbar is currently shown

    private float hydrationPreviewValue; //value of the hydration preview subbar
    private boolean hydrationPreviewSub = false; //if the hydration preview subbar is currently shown


    //============================== Overrides ==============================\\
    public ThirstBar() { //set size
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
        SustinanceData data = SustinanceData.of(player); //get sustinance data

        previousValue = value; //set previous value
        value = data.getThirst(); //get thirst value
        maxValue = 20; //this is hardcoded because the max thirst level never changes
        
        if (value < previousValue) { //if current value is less than previous value (thirst decreased)
            decreaseAlt = true; decreaseAltCounter = 0; //set decrease alt texture
            increaseAlt = false; //unset increase alt texture
            increaseSub = false; //deactivate increase subbar
        }
        
        else if (value > previousValue) { //if current value is greater than previous value (thirst increase)
            increaseAlt = true; increaseAltCounter = 0; //set increase alt texture
            increaseSub = true; increaseSubCounter = 0; //activate increase subbar
            decreaseAlt = false; //unset decrease alt texture
        }
        
        previousHydrationValue = hydrationValue; //set previous hydration value
        hydrationValue = data.getHydration(); //get hydration value
        
        if (hydrationValue < previousHydrationValue) { //if current value is less than previous value (hydration decreased)
            hydrationDecrease = true; hydrationDecreaseCounter = 0; //set decrease alt texture
            hydrationIncrease = false; //unset increase alt texture
            hydrationIncreaseSub = false; //deactivate increase subbar
        }
        
        else if (hydrationValue > previousHydrationValue) { //if current value is greater than previous value (hydration increase)
            hydrationIncrease = true; hydrationIncreaseCounter = 0; //set increase alt texture
            hydrationIncreaseSub = true; hydrationIncreaseSubCounter = 0; //activate increase subbar
            hydrationDecrease = false; //unset decrease alt texture
        }

        //preview
        ItemStack stack = player.getMainHandItem(); //get the itemstack in the player's main hand
        if (ConsumableHelper.canConsume(player, stack)) { //if the item is consumable
            doPreview = true; //previews are possible
            SustenanceProperties props = ConsumableHelper.getSustenanceProperties(stack); //get the sustenance properties of the item

            if (props.getWater() > 0) { //if the item has water
                previewValue = Math.min(value + props.getWater(), maxValue); //set preview value to the current value plus the nutrition value of the item
                previewSub = true;
            } else {previewSub = false;} //previews are not possible
            
            if (props.getHydration() > 0) { //if the item has hydration
                hydrationPreviewValue = Math.min(hydrationValue + props.getHydration(), maxValue); //set preview value to the current value plus the hydration value of the item
                hydrationPreviewSub = true;
            } else {hydrationPreviewSub = false;} //previews are not possible

        } else {
            doPreview = false; //previews are not possible
            previewSub = false; //deactivate preview subbar
            hydrationPreviewSub = false; //deactivate hydration preview subbar
        }

        //droplet
        if (value <= DEHYDRATED_THRESHOLD) {droplet = DropletVariation.DEHYDRATED;} //if below dehydrated threshold set droplet to dehydrated
        else if (value <= THIRSTY_THRESHOLD) {droplet = DropletVariation.THIRSTY;} //if below thirsty threshold set droplet to thirsty
        else {droplet = DropletVariation.NORMAL;} //else set droplet to normal

        //sorted by priority
        if (player.hasEffect(TANEffects.THIRST)) {variant = BarVariant.THIRST;} //set variant to thirst
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
        
        float hydrationFillValue = (ClientConfig.ROUND_SATURATION_TEXTURE) ? Math.round(hydrationValue) : hydrationValue; //round hydration value if config is set to round hydration texture
        
        if (hydrationDecrease) { //if decrease alt texture is shown
            if (hydrationDecreaseCounter >= DECREASE_ALT_TICKS) { //if enough time has passed
                hydrationDecrease = false; //set texture back to normal
                hydrationDecreaseCounter = 0; //reset tick counter
            } else {hydrationDecreaseCounter++;} //increase tick counter
        }

        if (hydrationIncrease) { //if increase alt texture is shown
            if (hydrationIncreaseCounter >= INCREASE_ALT_TICKS) { //if enough time has passed
                hydrationIncrease = false; //set texture back to normal
                hydrationIncreaseCounter = 0; //reset tick counter
            } else {hydrationIncreaseCounter++;} //increase tick counter
        }

        if (hydrationIncreaseSub) { //if increase subbar is active
            if (hydrationIncreaseSubCounter < INCREASE_SUB_DELAY) {hydrationIncreaseSubCounter++;} //wait for delay
            else { //after delay
                if (hydrationIncreaseSubValue < hydrationFillValue) { //if subbar value is less than current value
                    hydrationIncreaseSubValue = Math.min(hydrationFillValue, hydrationIncreaseSubValue + INCREASE_SUB_INCREASE); //increase subbar value
                    if (hydrationIncreaseSubValue >= hydrationFillValue) { //if subbar value reached current value
                        hydrationIncreaseSub = false; //hide subbar
                        hydrationIncreaseSubCounter = 0; //reset tick counter
                    }
                }
            }
        } else {hydrationIncreaseSubValue = hydrationFillValue;} //set subbar value to current value if not displaying subbar (so once we should display it, it will have the correct value)

        if (doPreview) { //if previews are possible
            if (previewAlphaIncrease) { //if preview alpha is increasing
                if (previewAlpha < PREVIEW_MAX_ALPHA) {previewAlpha += PREVIEW_ALPHA_CHANGE;} //increase alpha
                else {previewAlphaIncrease = false;} //if max alpha is reached, start decreasing
            } else { //if preview alpha is decreasing
                if (previewAlpha > PREVIEW_MIN_ALPHA) {previewAlpha -= PREVIEW_ALPHA_CHANGE;} //decrease alpha
                else {previewAlphaIncrease = true;} //if min alpha is reached, start increasing
            }
        }
    }

    @Override
    public void render() {
        renderSimpleTexture(BACKGROUND, BACKGROUND_WIDTH, BACKGROUND_HEIGHT, x, y); //render background
        
        if (previewSub) {renderThirstPreviewSub(previewValue, maxValue, x, y);} //render preview subbar
        if (increaseSub) { //if increase subbar is active
            renderThirstIncreaseSub(value, maxValue, x, y); //render increase subbar (we actually render the normal bar as the subbar)
            if (decreaseAlt) {renderThirstDecrease(increaseSubValue, maxValue, x, y);} //render decrease texture (we render the subbar as a fake main bar making it seem like it slowly is increasing)
            else if (increaseAlt) {renderThirstIncrease(increaseSubValue, maxValue, x, y);} //render increase texture
            else {renderThirst(increaseSubValue, maxValue, x, y);} //render normal texture
        } else {
            if (decreaseAlt) {renderThirstDecrease(value, maxValue, x, y);} //render decrease texture
            else if (increaseAlt) {renderThirstIncrease(value, maxValue, x, y);} //render increase texture
            else {renderThirst(value, maxValue, x, y);} //render normal texture
        }
        
        float hydrationFillValue = (ClientConfig.ROUND_SATURATION_TEXTURE) ? Math.round(hydrationValue) : hydrationValue; //round hydration value if config is set to round hydration texture
        
        if (hydrationPreviewSub) {renderHydrationPreviewSub(hydrationPreviewValue, maxValue, x, y);} //render hydration preview subbar
        if (hydrationIncreaseSub) { //if increase subbar is active
            renderHydrationIncreaseSub(hydrationFillValue, maxValue, x, y); //render increase subbar (we actually render the normal bar as the subbar)
            if (hydrationDecrease) {renderHydrationDecrease(hydrationIncreaseSubValue, maxValue, x, y);} //render decrease texture (we render the subbar as a fake main bar making it seem like it slowly is increasing)
            else if (hydrationIncrease) {renderHydrationIncrease(hydrationIncreaseSubValue, maxValue, x, y);} //render increase texture
            else {renderHydration(hydrationIncreaseSubValue, maxValue, x, y);} //render normal texture
        } else {
            if (hydrationDecrease) {renderHydrationDecrease(hydrationFillValue, maxValue, x, y);} //render decrease texture
            else if (hydrationIncrease) {renderHydrationIncrease(hydrationFillValue, maxValue, x, y);} //render increase texture
            else {renderHydration(hydrationFillValue, maxValue, x, y);} //render normal texture
        }
        
        //render thirst bar value as text
        if (ClientConfig.HUNGER_VALUE) {renderIntValueText((int)value, (int)maxValue, Txt.DEFAULT, ClientConfig.HUNGER_VALUE_SIZE, x+BAR_OFFSET_X, y+BAR_OFFSET_Y, BAR_WIDTH, BAR_HEIGHT);}
    }

    //============================== Helpers ==============================\\
    private void renderDroplet(int v, int x, int y) { //render droplet icon
        switch (droplet) {
            case NORMAL: renderPartialTexture(variant.texture, TEXTURE_WIDTH, TEXTURE_HEIGHT, NORMAL_U, v, ICON_WIDTH, ICON_HEIGHT, x+ICON_OFFSET_X, y+ICON_OFFSET_Y); break;
            case THIRSTY: renderPartialTexture(variant.texture, TEXTURE_WIDTH, TEXTURE_HEIGHT, THIRSTY_U, v, ICON_WIDTH, ICON_HEIGHT, x+ICON_OFFSET_X, y+ICON_OFFSET_Y); break;
            case DEHYDRATED: renderPartialTexture(variant.texture, TEXTURE_WIDTH, TEXTURE_HEIGHT, DEHYDRATED_U, v, ICON_WIDTH, ICON_HEIGHT, x+ICON_OFFSET_X, y+ICON_OFFSET_Y); break;
        }
    }

    //=========== Thirst Bar ===========\\
    private void renderThirst(float value, float maxValue, int x, int y) { //render thirst bar
        renderDroplet(NORMAL_V, x, y); //render droplet
        renderBarLeft(variant.texture, TEXTURE_WIDTH, TEXTURE_HEIGHT, BAR_U, NORMAL_V, BAR_WIDTH, BAR_HEIGHT, value, maxValue, x+BAR_OFFSET_X, y+BAR_OFFSET_Y);
    }

    private void renderThirstDecrease(float value, float maxValue, int x, int y) { //render thirst decrease bar
        renderDroplet(DECREASE_V, x, y); //render droplet
        renderBarLeft(variant.texture, TEXTURE_WIDTH, TEXTURE_HEIGHT, BAR_U, DECREASE_V, BAR_WIDTH, BAR_HEIGHT, value, maxValue, x+BAR_OFFSET_X, y+BAR_OFFSET_Y);
    }

    private void renderThirstIncrease(float value, float maxValue, int x, int y) { //render thirst increase bar
        renderDroplet(INCREASE_V, x, y); //render droplet
        renderBarLeft(variant.texture, TEXTURE_WIDTH, TEXTURE_HEIGHT, BAR_U, INCREASE_V, BAR_WIDTH, BAR_HEIGHT, value, maxValue, x+BAR_OFFSET_X, y+BAR_OFFSET_Y);
    }

    private void renderThirstIncreaseSub(float value, float maxValue, int x, int y) { //render thirst increase sub bar
        renderBarLeft(variant.texture, TEXTURE_WIDTH, TEXTURE_HEIGHT, BAR_U, INCREASE_SUB_V, BAR_WIDTH, BAR_HEIGHT, value, maxValue, x+BAR_OFFSET_X, y+BAR_OFFSET_Y);
    }
    
    //=========== Hydration Bar ===========\\
    private void renderHydration(float value, float maxValue, int x, int y) { //render hydration bar
        renderBarLeft(variant.texture, TEXTURE_WIDTH, TEXTURE_HEIGHT, HYDRATION_U, NORMAL_V, BAR_WIDTH, BAR_HEIGHT, value, maxValue, x+BAR_OFFSET_X, y+BAR_OFFSET_Y);
    }
    
    private void renderHydrationDecrease(float value, float maxValue, int x, int y) { //render hydration decrease bar
        renderBarLeft(variant.texture, TEXTURE_WIDTH, TEXTURE_HEIGHT, HYDRATION_U, DECREASE_V, BAR_WIDTH, BAR_HEIGHT, value, maxValue, x+BAR_OFFSET_X, y+BAR_OFFSET_Y);
    }
    
    private void renderHydrationIncrease(float value, float maxValue, int x, int y) { //render hydration increase bar
        renderBarLeft(variant.texture, TEXTURE_WIDTH, TEXTURE_HEIGHT, HYDRATION_U, INCREASE_V, BAR_WIDTH, BAR_HEIGHT, value, maxValue, x+BAR_OFFSET_X, y+BAR_OFFSET_Y);
    }
    
    private void renderHydrationIncreaseSub(float value, float maxValue, int x, int y) { //render hydration increase sub bar
        renderBarLeft(variant.texture, TEXTURE_WIDTH, TEXTURE_HEIGHT, HYDRATION_U, INCREASE_SUB_V, BAR_WIDTH, BAR_HEIGHT, value, maxValue, x+BAR_OFFSET_X, y+BAR_OFFSET_Y);
    }
    
    //=========== Preview Bars ===========\\
    private void renderThirstPreviewSub(float value, float maxValue, int x, int y) { //render thirst preview sub bar
        enableAlpha(previewAlpha); //enable alpha
        renderBarLeft(variant.texture, TEXTURE_WIDTH, TEXTURE_HEIGHT, BAR_U, PREVIEW_SUB_V, BAR_WIDTH, BAR_HEIGHT, value, maxValue, x+BAR_OFFSET_X, y+BAR_OFFSET_Y);
        disableAlpha(); //disable alpha
    }

    private void renderHydrationPreviewSub(float value, float maxValue, int x, int y) { //render hydration preview sub bar
        enableAlpha(previewAlpha); //enable alpha
        renderBarLeft(variant.texture, TEXTURE_WIDTH, TEXTURE_HEIGHT, HYDRATION_U, PREVIEW_SUB_V, BAR_WIDTH, BAR_HEIGHT, value, maxValue, x+BAR_OFFSET_X, y+BAR_OFFSET_Y);
        disableAlpha(); //disable alpha
    }


    //============================== Enum ==============================\\
    private enum BarVariant {
        NORMAL(ResourceLocation.fromNamespaceAndPath(TownTime.MOD_ID, "textures/hud/thirst/normal.png")),
        THIRST(ResourceLocation.fromNamespaceAndPath(TownTime.MOD_ID, "textures/hud/thirst/thirst.png"));

        public final ResourceLocation texture;
        BarVariant(ResourceLocation texture) {this.texture = texture;}
    }

    private enum DropletVariation {NORMAL, THIRSTY, DEHYDRATED}
}
