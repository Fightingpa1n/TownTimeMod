package net.fightingpainter.mc.towntime.hud.elements;

// import net.fightingpainter.mc.towntime.TownTime;
// import net.minecraft.client.gui.GuiGraphics;
// import net.minecraft.resources.ResourceLocation;
// import net.minecraft.world.entity.player.Player;

// public class XpBar extends BaseBar{
//     private final static ResourceLocation NORMAL = ResourceLocation.fromNamespaceAndPath(TownTime.MOD_ID, "textures/hud/health.png");
//     private final static ResourceLocation BACKGROUND = ResourceLocation.fromNamespaceAndPath(TownTime.MOD_ID, "textures/hud/health_background.png");

//     public XpBar(int x, int y) {
//         this.texture = NORMAL;
//         this.backgroundTexture = BACKGROUND;
//         this.length = 37;
//         this.height = 6;
//         this.x = x;
//         this.y = y;
//     }

//     @Override
//     public void getParameters(Player player) {
//         this.maxValue = player.getXpNeededForNextLevel();
//         this.value = player.totalExperience;
//         //this.level = player.experienceLevel;
//     }

//     @Override
//     public void render(GuiGraphics guiGraphics) {
//         super.render(guiGraphics);
//     }
// }
