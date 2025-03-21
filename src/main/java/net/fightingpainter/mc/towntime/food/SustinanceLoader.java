package net.fightingpainter.mc.towntime.food;

import com.google.gson.JsonParser;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;

import net.fightingpainter.mc.towntime.data.ModDataComponentTypes;
import net.fightingpainter.mc.towntime.util.types.Dict;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.core.component.DataComponentMap;
import net.fightingpainter.mc.towntime.mixin.ItemAccessor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.FMLPaths;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.charset.StandardCharsets;


public class SustinanceLoader {


    public static void load() {
        try {
            Path configDir = FMLPaths.CONFIGDIR.get();
            Path jsonFilePath = configDir.resolve("sustenance_data.json");
            String jsonString = Files.readString(jsonFilePath, StandardCharsets.UTF_8);
            
            Dict data = Dict.fromJson(jsonString);
            data.forEach((key, value) -> {
                String itemData = ((Dict)value).toJson(); //convert the data back to a json string
                Item item = BuiltInRegistries.ITEM.get(ResourceLocation.parse(key));
                ItemAccessor itemAccessor = (ItemAccessor) item;
                DataComponentMap originalComponents = itemAccessor.getProperties();
                DataComponentMap.Builder builder = DataComponentMap.builder().addAll(originalComponents);
                builder.set(ModDataComponentTypes.SUSTINENCE.get(), decode(itemData));
                DataComponentMap newComponents = builder.build();
                itemAccessor.setProperties(newComponents);
            });
        }
        catch (Exception e) { //error
            e.printStackTrace();
        }
    }

    /** Encodes a SustenanceProperties object into a JSON string */
    private static String encode(SustenanceProperties properties) {
        String result = SustenanceProperties.CODEC.encodeStart(JsonOps.INSTANCE, properties).result()
            .map(JsonElement::toString).orElse("");
        return result;
    }

    /** Decodes a JSON string into a SustenanceProperties object */
    private static SustenanceProperties decode(String jsonString) {
        JsonElement json = JsonParser.parseString(jsonString);
        return SustenanceProperties.CODEC.parse(JsonOps.INSTANCE, json).result().orElse(null);
    }
}
