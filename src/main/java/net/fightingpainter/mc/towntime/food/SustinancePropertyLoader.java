package net.fightingpainter.mc.towntime.food;

import com.google.gson.JsonParser;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;

import net.fightingpainter.mc.towntime.TownTime;
import net.fightingpainter.mc.towntime.data.ModDataComponentTypes;
import net.fightingpainter.mc.towntime.util.types.Dict;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.fightingpainter.mc.towntime.mixin.ItemAccessor;
import net.neoforged.fml.loading.FMLPaths;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.nio.charset.StandardCharsets;

public class SustinancePropertyLoader {

    public static void load() {
        if (!Files.exists(getFilePath())) {generateFile();} //generate the file if it doesn't exist
        loadSustinanceProperties(); //load sustenance properties from the file
    }

    /** tries to load the sustenance properties from the sustenance data file and applies them to the specified items */
    private static void loadSustinanceProperties() {
        try {
            Dict data = Dict.fromJson(Files.readString(getFilePath(), StandardCharsets.UTF_8));
            data.forEach((key, value) -> { //loop through all items in the data
                Item item = BuiltInRegistries.ITEM.get(ResourceLocation.parse(key)); //get the item from the key
                if (item != null) { //if the item exists
                    ItemAccessor itemAccessor = (ItemAccessor) item; //get item accessor
                    DataComponentMap components = itemAccessor.getProperties(); //get components
                    DataComponentMap.Builder builder = DataComponentMap.builder().addAll(components); //create component builder
                    builder.set(ModDataComponentTypes.SUSTINENCE.get(), decode(((Dict)value).toJson())); //set sustenance properties
                    itemAccessor.setProperties(builder.build()); //set the new components
                }
            });
        } catch (Exception e) {
            TownTime.LOGGER.error("Failed to load sustenance data file...", e);
        }
    }


    /** Removes the FoodProperties from Items */ //TODO: either remove it or fix it, can't use it in a maybe works state
    private static void removeFoodProperties() {
        for (Item item : BuiltInRegistries.ITEM) {
            ItemAccessor itemAccessor = (ItemAccessor) item;
            DataComponentMap components = itemAccessor.getProperties();
            if (components.has(DataComponents.FOOD)) {
                DataComponentMap.Builder builder = DataComponentMap.builder();

                components.forEach(component -> {
                    DataComponentType<?> type = component.type();

                    // Exclude the FoodProperties component
                    if (!type.equals(DataComponents.FOOD)) {
                        builder.set(() -> (DataComponentType<Object>) type, component.value());
                    }
                });
            }
        }
    }


    /** Generates a sustenance data file from all items with food properties */
    private static void generateFile() {
        try { //try to generate sustenance data file
            Dict genData = new Dict(); //dict to store generated data

            for (Item item : BuiltInRegistries.ITEM) { //loop through all items
                ItemAccessor itemAccessor = (ItemAccessor) item; //get the item accessor
                DataComponentMap components = itemAccessor.getProperties(); //get the original components of the item
                
                if (components.has(DataComponents.FOOD)) { //if the item has food properties
                    FoodProperties foodProperties = components.get(DataComponents.FOOD); //get the food properties
                    SustenanceProperties sustenanceProperties = convertFoodProperties(foodProperties); //convert the food properties to sustenance properties
                    String key = BuiltInRegistries.ITEM.getKey(item).toString(); //get the item key
                    genData.add(key, Dict.fromJson(encode(sustenanceProperties))); //add the data to the generated data
                }
            }
            
            Files.writeString(getFilePath(), genData.toJson(4), StandardCharsets.UTF_8); //write the generated data to the file

        } catch (Exception e) {
            TownTime.LOGGER.error("Failed to generate sustenance data file, manual creation may be required at: "+getFilePath().toString(), e);
        }
    }

    /**
     * Generates a SustenanceProperties object from a FoodProperties object
     * @param foodProperties the FoodProperties object to convert
     * @return the Generated SustenanceProperties object
    */
    private static SustenanceProperties convertFoodProperties(FoodProperties prop) {
        return new SustenanceProperties(
            prop.nutrition(), //get nutrition value
            prop.saturation(), //get saturation value
            0, //0 as food properties don't have a water value
            0.0f, //0 as food properties don't have a hydration value
            prop.canAlwaysEat(), //get can always consume value
            prop.eatSeconds(), //get eat time value
            prop.usingConvertsTo(), //get conversion item
            convertEffects(prop.effects()) //convert effects
        );
    }

    /**
     * Converts a list of FoodProperties.PossibleEffect objects into a list of SustenanceProperties.PossibleEffect objects
     * @param effects the list of FoodProperties.PossibleEffect objects to convert
     * @return the converted list of SustenanceProperties.PossibleEffect objects
    */
    private static List<SustenanceProperties.PossibleEffect> convertEffects(List<FoodProperties.PossibleEffect> effects) {
        return effects.stream()
            .map(effect -> new SustenanceProperties.PossibleEffect(
                () -> new MobEffectInstance(effect.effect()), // Supplier for MobEffectInstance
                effect.probability() // Probability of the effect
            ))
            .collect(Collectors.toList());
    }

    /** get the file path of the config file */
    private static Path getFilePath() {return FMLPaths.CONFIGDIR.get().resolve("sustenance_data.json");}

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
