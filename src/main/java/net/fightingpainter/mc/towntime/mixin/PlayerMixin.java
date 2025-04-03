package net.fightingpainter.mc.towntime.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.fightingpainter.mc.towntime.food.DataHandler;
import net.fightingpainter.mc.towntime.food.PlayerWrapper;
import net.fightingpainter.mc.towntime.food.CustomTemperatureData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import toughasnails.api.temperature.TemperatureHelper;
import toughasnails.api.thirst.ThirstHelper;
import toughasnails.temperature.TemperatureData;
import toughasnails.thirst.ThirstData;

@Mixin(value=Player.class, priority=1100)
public abstract class PlayerMixin extends LivingEntity implements PlayerWrapper {
    private PlayerMixin(EntityType<? extends LivingEntity> type, Level level){super(type, level);} //ignore this. this is required for mixin to work. it is a constructor that is not used.

    // @Inject(method="causeFoodExhaustion", at=@At("HEAD"), cancellable=true)
    // public void onCauseFoodExhaustion(float exhaustion, CallbackInfo ci) { //cancel exhaustion
    //     ci.cancel(); //cancel the original method
    // }

    @Unique
    private CustomTemperatureData temperatureData = new CustomTemperatureData();

    @Inject(method="readAdditionalSaveData", at=@At("TAIL"))
    public void onReadAdditionalSaveData(CompoundTag nbt, CallbackInfo ci) {
        this.temperatureData.readFromTag(nbt); //read temperature data from tag
    }

    @Inject(method="addAdditionalSaveData", at=@At("TAIL"))
    public void onAddAdditionalSaveData(CompoundTag nbt, CallbackInfo ci) {
        this.temperatureData.writeToTag(nbt); //write temperature data to tag
    }


    @Inject(method="tick", at=@At("TAIL"))
    public void onTick(CallbackInfo ci) {
        Player player = (Player)(Object)this; //get player
        if (!player.level().isClientSide()){ //if not on client side
            Difficulty difficulty = player.level().getDifficulty(); //get difficulty
            FoodDataAccessor food = (FoodDataAccessor)player.getFoodData(); //get food data
            ThirstData thirst = (ThirstData)ThirstHelper.getThirst(player); //get thirst data
            // TemperatureData temperature = (TemperatureData)TemperatureHelper.getTemperatureData(player); //get temperature data
            GameRules rules = player.level().getGameRules();
            
            DataHandler.playerTick(food, thirst, (toughasnails.temperature.TemperatureData)TemperatureHelper.getTemperatureData(player), player, difficulty, rules); //do player tick //TODO: change to use my new temperature data class
        }
    }

    //TODO: change methods to change exhasution stuff like inject into methods that call causeFoodExhaustion.

    @Override
    public CustomTemperatureData getTemperatureData() {return this.temperatureData;} //get temperature data
}
