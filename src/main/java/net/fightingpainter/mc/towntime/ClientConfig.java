package net.fightingpainter.mc.towntime;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Neo's config APIs
@EventBusSubscriber(modid = TownTime.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class ClientConfig {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    
    public static boolean ROUND_HEALTH_BAR_VALUE;
    private static final ModConfigSpec.BooleanValue roundHealthBarValue = BUILDER
        .comment("Whether to round the Healthbar to use whole numbers instead of decimals")
        .define("roundHealthBar", true);

    public static boolean ROUND_HEALTH_BAR_TEXTURE;
    private static final ModConfigSpec.BooleanValue roundHealthBarTexture = BUILDER
        .comment("Round the health bar Texture") //TODO: I need to better explain what this does
        .define("roundHealthBarTexture", false);

    public static final ModConfigSpec SPEC = BUILDER.build();
    
    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) { //load config
        ROUND_HEALTH_BAR_TEXTURE = roundHealthBarTexture.get();
        ROUND_HEALTH_BAR_VALUE = roundHealthBarValue.get();
    }
}
