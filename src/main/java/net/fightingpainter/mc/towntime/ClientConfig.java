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

    //============================== Hud ==============================\\
    //=========== Health ===========\\
    public static boolean ROUND_HEALTH_VALUE;
    private static final ModConfigSpec.BooleanValue roundHealthValue = BUILDER
        .comment("Whether to round the Healthbar to use whole numbers instead of decimals")
        .define("roundHealthValue", true);

    public static boolean ROUND_HEALTH_TEXTURE;
    private static final ModConfigSpec.BooleanValue roundHealthTexture = BUILDER
        .comment("Round the health bar Texture") //TODO: I need to better explain what this does
        .define("roundHealthTexture", false);

    //=========== Hunger ===========\\
    public static boolean HUNGER_VALUE;
    private static final ModConfigSpec.BooleanValue hungerValue = BUILDER
        .comment("whether to show the hunger bar value as text on the hunger bar")
        .define("hungerValue", true);

    public static float HUNGER_VALUE_SIZE;
    private static final ModConfigSpec.DoubleValue hungerValueSize = BUILDER
        .comment("The size of the hunger value text")
        .defineInRange("hungerValueSize", 0.5, 0.3, 1.0);
    

    public static boolean ROUND_SATURATION_TEXTURE;
    private static final ModConfigSpec.BooleanValue roundSaturationTexture = BUILDER
        .comment("Round the saturation bar Texture") //TODO: I need to better explain what this does
        .define("roundSaturationTexture", false);


    public static final ModConfigSpec SPEC = BUILDER.build();
    
    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) { //load config
        ROUND_HEALTH_VALUE = roundHealthValue.get();
        ROUND_HEALTH_TEXTURE = roundHealthTexture.get();

        HUNGER_VALUE = hungerValue.get();
        HUNGER_VALUE_SIZE = hungerValueSize.get().floatValue();
        ROUND_SATURATION_TEXTURE = roundSaturationTexture.get();
    }
}
