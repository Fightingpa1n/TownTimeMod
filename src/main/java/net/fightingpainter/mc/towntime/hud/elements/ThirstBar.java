package net.fightingpainter.mc.towntime.hud.elements;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import toughasnails.api.thirst.ThirstHelper;
import toughasnails.api.potion.TANEffects;

import net.fightingpainter.mc.towntime.TownTime;


public class ThirstBar extends BaseBarElement{
    private final static ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(TownTime.MOD_ID, "textures/hud/thirst.png");
    private final static ResourceLocation BACKGROUND = ResourceLocation.fromNamespaceAndPath(TownTime.MOD_ID, "textures/hud/thirst_background.png");
    
    private ThirstBarVariant variant = ThirstBarVariant.NORMAL;

    public ThirstBar() { //set size
        this.width = 87;
        this.height = 9;
    }
    
    @Override
    public boolean shouldRender(Player player) {//check gamemode
        if (player.isCreative() || player.isSpectator()) {return false;}
        else {return true;}
    }

    @Override
    public void getParameters(Player player) {
        this.value = ThirstHelper.getThirst(player).getThirst();
        this.maxValue = 20;

        //sorted by priority
        if (player.hasEffect(TANEffects.THIRST)) {this.variant = ThirstBarVariant.THIRST;} //set variant to thirst
        else {this.variant = ThirstBarVariant.NORMAL;} //set variant to normal
    }

    @Override
    public void render() {
        renderSimpleTexture(BACKGROUND, 87, 9, x, y); //render background

        int textureWidth = 88; //set texture width
        int textureHeight = 15; //set texture height
        renderPartialTexture(TEXTURE, textureWidth, textureHeight, variant.dropletX, variant.dropletY, variant.dropletWidth, variant.dropletHeight, x+1, y+1); //render droplet
        renderBarLeft(TEXTURE, textureWidth, textureHeight, variant.barX, variant.barY, variant.barWidth, variant.barHeight, x+6, y+2); //render thirst bar

        TextElement valueText = new TextElement(Math.round(this.value) + "/" + Math.round(this.maxValue));
        valueText.alignCenter(); //center align
        valueText.setScale(0.5f); //scale down
        valueText.setYShift(1); //shift down
        renderText(valueText, (x+6)+(variant.barWidth/2), (y+2)+(variant.barHeight/2)); //render text
    }

    private enum ThirstBarVariant {
        NORMAL( //normal thirst bar
            0, 0, 7, 7, //droplet
            8, 0, 80, 5 //bar
        ),
        THIRST(
            0, 8, 7, 7, //droplet
            8, 8, 80, 5 //bar
        );

        public final int dropletX;
        public final int dropletY;
        public final int dropletWidth;
        public final int dropletHeight;

        public final int barX;
        public final int barY;
        public final int barWidth;
        public final int barHeight;

        ThirstBarVariant(int dropletX, int dropletY, int dropletWidth, int dropletHeight, int barX, int barY, int barWidth, int barHeight) {
            this.dropletX = dropletX;
            this.dropletY = dropletY;
            this.dropletWidth = dropletWidth;
            this.dropletHeight = dropletHeight;
            this.barX = barX;
            this.barY = barY;
            this.barWidth = barWidth;
            this.barHeight = barHeight;
        }
    }
}
