package net.fightingpainter.mc.towntime.data;

import java.util.function.UnaryOperator;

import net.fightingpainter.mc.towntime.TownTime;
import net.fightingpainter.mc.towntime.food.SustenanceProperties;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModDataComponentTypes {
    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENT_TYPES_REGISTER = DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, TownTime.MOD_ID);
    
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<SustenanceProperties>> SUSTINENCE = registerDataComponentType("sustinance", //register sustinance data component type
        builder -> builder.persistent(SustenanceProperties.CODEC).networkSynchronized(SustenanceProperties.STREAM_CODEC)
    );
    
    
    private static <T> DeferredHolder<DataComponentType<?>, DataComponentType<T>> registerDataComponentType(String name, UnaryOperator<DataComponentType.Builder<T>> builderOperator) {
        return DATA_COMPONENT_TYPES_REGISTER.register(name, () -> builderOperator.apply(DataComponentType.builder()).build());
    }


    //==================================================================================\\
    /**
     * Register Data Component Types
     * @param modEventBus The mod event bus to register the data component types to
    */
    public static void register(IEventBus modEventBus) {
        TownTime.LOGGER.info("Registering DataComponentTypes...");
        DATA_COMPONENT_TYPES_REGISTER.register(modEventBus); //Register the data component types
    }
}
