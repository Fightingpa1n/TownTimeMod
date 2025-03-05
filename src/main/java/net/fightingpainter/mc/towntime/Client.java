package net.fightingpainter.mc.towntime;

import net.fightingpainter.mc.towntime.hud.HudRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;

@EventBusSubscriber(modid=TownTime.MOD_ID, bus = EventBusSubscriber.Bus.GAME, value=Dist.CLIENT)
public class Client {
    public static long tickCount = 0L;

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) { //client tick handler so to keep the tick count accurate
        if (!Minecraft.getInstance().isPaused()) {tickCount++;}
    }

    @SubscribeEvent
    public static void disableVanillaOverlays(RenderGuiLayerEvent.Pre event) {
        ResourceLocation overlay = event.getName();
        if ( //if any of the following overlays are present, cancel the event
            overlay == VanillaGuiLayers.PLAYER_HEALTH
            || overlay == VanillaGuiLayers.FOOD_LEVEL
            || overlay == VanillaGuiLayers.AIR_LEVEL
            || overlay == VanillaGuiLayers.EXPERIENCE_BAR
            || overlay == VanillaGuiLayers.EXPERIENCE_LEVEL
            || overlay == VanillaGuiLayers.VEHICLE_HEALTH
            || overlay == VanillaGuiLayers.HOTBAR
            || overlay == VanillaGuiLayers.ARMOR_LEVEL
        ) {event.setCanceled(true);}



        // if (!AsteorBar.config.enableOverlay()) return;
        // ResourceLocation overlay = event.getName();
        // if (overlay == VanillaGuiLayers.PLAYER_HEALTH) {
        //     Overlays.reset();
        // }
        // if (overlay == VanillaGuiLayers.PLAYER_HEALTH
        //         || overlay == VanillaGuiLayers.FOOD_LEVEL
        //         || overlay == VanillaGuiLayers.AIR_LEVEL
        //         || (AsteorBar.config.overwriteVanillaExperienceBar() && (overlay == VanillaGuiLayers.EXPERIENCE_BAR || overlay == VanillaGuiLayers.EXPERIENCE_LEVEL))
        //         || overlay == VanillaGuiLayers.VEHICLE_HEALTH
        //         || (AsteorBar.config.overwriteVanillaArmorBar() && overlay == VanillaGuiLayers.ARMOR_LEVEL)
        //         || AsteorBar.compatibility.appleskin && overlay.getNamespace().equals("appleskin")
        // ) {
        //     event.setCanceled(true);
        // }
    }

    @SubscribeEvent
    public static void registerOverlay(RenderGuiEvent.Pre event) { //register Hud Overlays
        // TownTime.LOGGER.info("Rendering Hud Overlays");
        HudRenderer.render(event.getGuiGraphics());
    }

    @SubscribeEvent
    public static void renderGui(ScreenEvent.Render.Post event) {
        HudRenderer.xpBarRenderer(event.getScreen(), event.getGuiGraphics());
    }
}
