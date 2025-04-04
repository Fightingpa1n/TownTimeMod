package net.fightingpainter.mc.towntime.food;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;
import javax.annotation.Nonnull;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;

/** the properties of a food item that can be consumed*/
public class SustenanceProperties {
    public static final int DEFAULT_NUTRITION = 0;
    public static final float DEFAULT_SATURATION = 0.0F;
    public static final int DEFAULT_WATER = 0;
    public static final float DEFAULT_HYDRATION = 0.0F;
    public static final boolean DEFAULT_CAN_ALWAYS_CONSUME = false;
    public final static float DEFAULT_CONSUME_TIME = 1.6F;
    public static final Optional<ItemStack> DEFAULT_USING_CONVERTS_TO = Optional.empty();
    public static final List<SustenanceProperties.PossibleEffect> DEFAULT_EFFECTS = List.of();

    private final int nutrition; //how much hunger is restored upon consumtion
    private final float saturation; //how much saturation is "restored" upon consumtion
    private final int water; //how much water is restored upon consumtion
    private final float hydration; //how much hydration is "restored" upon consumtion
    private final boolean canAlwaysConsume; //if the food can always be consumed
    private final float consumeTime; //how long it takes to consume the food
    private final Optional<ItemStack> usingConvertsTo; //what the food converts to when used
    private final List<SustenanceProperties.PossibleEffect> effects; //possible effects that the food can give

    public SustenanceProperties(int nutrition, float saturation, int water, float hydration, boolean canAlwaysConsume, float consumeTime, Optional<ItemStack> usingConvertsTo, List<SustenanceProperties.PossibleEffect> effects) {
        this.nutrition = nutrition;
        this.saturation = saturation;
        this.water = water;
        this.hydration = hydration;
        this.canAlwaysConsume = canAlwaysConsume;
        this.consumeTime = consumeTime;
        this.usingConvertsTo = usingConvertsTo;
        this.effects = effects;
    }
    

    //============================== Data ==============================\\
    /** Get the nutrition level of the food (how much hunger is restored upon consuming)*/
    public int getNutrition() {return this.nutrition;}
    /** Get the saturation level of the food (how much saturation is restored upon consuming)*/
    public float getSaturation() {return this.saturation;}
    /** Get the water level of the food (how much water is restored upon consuming)*/
    public int getWater() {return this.water;}
    /** Get the hydration level of the food (how much hydration is restored upon consuming)*/
    public float getHydration() {return this.hydration;}
    /** Get if the food can always be consumed*/
    public boolean canAlwaysConsume() {return this.canAlwaysConsume;}
    /** Get the time it takes to consume the food*/
    public float getConsumeTime() {return this.consumeTime;}
    /** Get the item that the food converts to when used*/
    public Optional<ItemStack> getUsingConvertsTo() {return this.usingConvertsTo;}
    /** Get the list of possible effects that the food can give*/
    public List<SustenanceProperties.PossibleEffect> getEffects() {return this.effects;}

    //=========== Methotds ===========\\
    /**
     * Check if the food has a conversion item it will turn into after being used
     * @return true if the food has a conversion item
    */
    public boolean hasConversionItem() {
        return usingConvertsTo.isPresent();
    }

    /**
     * Get the item that the food converts to when used
     * @return the item that the food converts to when used
    */
    public ItemStack getConversionItem() {
        return usingConvertsTo.orElse(ItemStack.EMPTY);
    }


    //============================== Codec ==============================\\
    /** Codec for SustenanceProperties*/
    public static final Codec<SustenanceProperties> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        ExtraCodecs.NON_NEGATIVE_INT.fieldOf("nutrition").forGetter(SustenanceProperties::getNutrition),
        Codec.FLOAT.fieldOf("saturation").forGetter(SustenanceProperties::getSaturation),
        ExtraCodecs.NON_NEGATIVE_INT.fieldOf("water").forGetter(SustenanceProperties::getWater),
        Codec.FLOAT.fieldOf("hydration").forGetter(SustenanceProperties::getHydration),
        Codec.BOOL.optionalFieldOf("can_always_consume", Boolean.valueOf(false)).forGetter(SustenanceProperties::canAlwaysConsume),
        Codec.FLOAT.optionalFieldOf("consume_time", DEFAULT_CONSUME_TIME).forGetter(SustenanceProperties::getConsumeTime),
        ItemStack.SINGLE_ITEM_CODEC.optionalFieldOf("using_converts_to").forGetter(SustenanceProperties::getUsingConvertsTo),
        SustenanceProperties.PossibleEffect.CODEC.listOf().optionalFieldOf("effects", List.of()).forGetter(SustenanceProperties::getEffects)
    ).apply(instance, SustenanceProperties::new));

    /** StreamCodec for SustenanceProperties*/
    public static final StreamCodec<RegistryFriendlyByteBuf, SustenanceProperties> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public SustenanceProperties decode(@Nonnull RegistryFriendlyByteBuf buf) {
            int nutrition = ByteBufCodecs.VAR_INT.decode(buf);
            float saturation = ByteBufCodecs.FLOAT.decode(buf);
            int water = ByteBufCodecs.VAR_INT.decode(buf);
            float hydration = ByteBufCodecs.FLOAT.decode(buf);
            boolean canAlwaysConsume = ByteBufCodecs.BOOL.decode(buf);
            float consumeTime = ByteBufCodecs.FLOAT.decode(buf);
            Optional<ItemStack> usingConvertsTo = ByteBufCodecs.optional(ItemStack.STREAM_CODEC).decode(buf);
            List<SustenanceProperties.PossibleEffect> effects = SustenanceProperties.PossibleEffect.STREAM_CODEC.apply(ByteBufCodecs.list()).decode(buf);
    
            return new SustenanceProperties(nutrition, saturation, water, hydration, canAlwaysConsume, consumeTime, usingConvertsTo, effects);
        }
    
        @Override
        public void encode(@Nonnull RegistryFriendlyByteBuf buf, @Nonnull SustenanceProperties s) {
            ByteBufCodecs.VAR_INT.encode(buf, s.getNutrition());
            ByteBufCodecs.FLOAT.encode(buf, s.getSaturation());
            ByteBufCodecs.VAR_INT.encode(buf, s.getWater());
            ByteBufCodecs.FLOAT.encode(buf, s.getHydration());
            ByteBufCodecs.BOOL.encode(buf, s.canAlwaysConsume());
            ByteBufCodecs.FLOAT.encode(buf, s.getConsumeTime());
            ByteBufCodecs.optional(ItemStack.STREAM_CODEC).encode(buf, s.getUsingConvertsTo());
            SustenanceProperties.PossibleEffect.STREAM_CODEC.apply(ByteBufCodecs.list()).encode(buf, s.getEffects());
        }
    };

    @Override
    public boolean equals(Object o) {
        if (this == o) return true; // Compare memory references
        if (o == null || getClass() != o.getClass()) return false; // Check for null or different class
        SustenanceProperties that = (SustenanceProperties) o; // Cast to this class
        boolean result;
        result = this.nutrition == that.nutrition;
        result = result && Float.compare(that.saturation, this.saturation) == 0;
        result = result && this.water == that.water;
        result = result && Float.compare(that.hydration, this.hydration) == 0;
        result = result && this.canAlwaysConsume == that.canAlwaysConsume;
        result = result && Float.compare(that.consumeTime, this.consumeTime) == 0;
        result = result && this.usingConvertsTo.equals(that.usingConvertsTo);
        result = result && this.effects.equals(that.effects);
        return result;
    }

    @Override
    public int hashCode() {return Objects.hash(nutrition, saturation, water, hydration, canAlwaysConsume, consumeTime, usingConvertsTo, effects);}


    //============================== Debug ==============================\\
    @Override
    public String toString() {
        return "SustenanceProperties{"
            + "nutrition=" + nutrition
            + ", saturation=" + saturation
            + ", water=" + water
            + ", hydration=" + hydration
            + ", canAlwaysConsume=" + canAlwaysConsume
            + ", consumeTime=" + consumeTime
            + ", usingConvertsTo=" + usingConvertsTo
            + ", effects=" + effects
            + '}';
    }

    
    //============================== PossibleEffect ==============================\\
    public static class PossibleEffect {
        private final Supplier<MobEffectInstance> effectSupplier;
        private float probability;

        public PossibleEffect(Supplier<MobEffectInstance> effectSupplier, float probability) {
            this.effectSupplier = effectSupplier;
            this.probability = probability;
        }
        
        public PossibleEffect(MobEffectInstance effect, float probability) {this(() -> effect, probability);}
        
        //============================== Data ==============================\\
        /** Get the effect supplier*/
        public Supplier<MobEffectInstance> getEffectSupplier() {return this.effectSupplier;}
        /** Get the effect*/
        public MobEffectInstance getEffect() {return new MobEffectInstance(this.effectSupplier.get());}
        /** Get the probability of the effect*/
        public float getProbability() {return this.probability;}

        //============================== Codec ==============================\\
        /** Codec for PossibleEffect*/
        public static final Codec<PossibleEffect> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            MobEffectInstance.CODEC.fieldOf("effect").forGetter(PossibleEffect::getEffect),
            Codec.FLOAT.optionalFieldOf("probability", 1.0F).forGetter(PossibleEffect::getProbability)
        ).apply(instance, PossibleEffect::new));

        /** StreamCodec for PossibleEffect*/
        public static final StreamCodec<RegistryFriendlyByteBuf, PossibleEffect> STREAM_CODEC = StreamCodec.composite(
            MobEffectInstance.STREAM_CODEC,
            effect -> effect.getEffect(),
            ByteBufCodecs.FLOAT,
            PossibleEffect::getProbability,
            (effectInstance, probability) -> new PossibleEffect(effectInstance, probability)
        );
        
        @Override
        public boolean equals(Object o) {
            if (this == o) return true; //Compare memory references
            if (o == null || getClass() != o.getClass()) return false; //Check for null or different class
            PossibleEffect that = (PossibleEffect) o; //Cast to this class
            MobEffectInstance thisEffect = this.effectSupplier.get();
            MobEffectInstance thatEffect = that.effectSupplier.get();
            return thisEffect.equals(thatEffect) && Float.compare(this.probability, that.probability) == 0;
        }

        @Override
        public int hashCode() {return Objects.hash(effectSupplier, probability);}

        //============================== Debug ==============================\\
        @Override
        public String toString() {return "PossibleEffect{" + "effect=" + effectSupplier.get().toString() + ", probability=" + probability + '}';}
    }
}
