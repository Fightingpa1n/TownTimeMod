package net.fightingpainter.mc.towntime.client;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import net.fightingpainter.mc.towntime.TownTime;


public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(Registries.SOUND_EVENT, TownTime.MOD_ID);

    public static final DeferredHolder<SoundEvent, SoundEvent> HUD_HEARTBEAT = registerSound("hud.heartbeat");


    private static DeferredHolder<SoundEvent, SoundEvent> registerSound(String name) {
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(TownTime.MOD_ID, name)));
    }

    public static void register(IEventBus modEventBus) {
        TownTime.LOGGER.info("Registering sounds");
        SOUND_EVENTS.register(modEventBus);
    }
}
