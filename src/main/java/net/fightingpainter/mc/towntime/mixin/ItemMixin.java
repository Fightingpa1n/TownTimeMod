package net.fightingpainter.mc.towntime.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.fightingpainter.mc.towntime.data.ModDataComponentTypes;
import net.fightingpainter.mc.towntime.food.SustenanceProperties;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import toughasnails.api.thirst.ThirstHelper;

@Mixin(Item.class)
public class ItemMixin {

    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    public void useSustenance(Level level, Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResultHolder<ItemStack>> cir) {
        ItemStack stack = player.getItemInHand(hand);

        if (stack.has(ModDataComponentTypes.SUSTINENCE.get())) {
            SustenanceProperties props = stack.get(ModDataComponentTypes.SUSTINENCE.get());

            if (player.canEat(props.canAlwaysConsume())) {
                player.startUsingItem(hand);
                cir.setReturnValue(InteractionResultHolder.consume(stack)); // Override vanilla
            } else {
                cir.setReturnValue(InteractionResultHolder.fail(stack));
            }
        }
    }


    @Inject(method = "finishUsingItem", at = @At("HEAD"), cancellable = true)
    public void finishSustenance(ItemStack stack, Level level, LivingEntity entity, CallbackInfoReturnable<ItemStack> cir) {
        if (stack.has(ModDataComponentTypes.SUSTINENCE.get())) {
            SustenanceProperties props = stack.get(ModDataComponentTypes.SUSTINENCE.get());

            // Apply hunger and thirst
            if (entity instanceof Player player) {
                player.getFoodData().eat(props.getNutrition(), props.getSaturationModifier());
                ThirstHelper.getThirst(player).drink(props.getWater(), props.getHydrationModifier());

                // Apply effects
                if (!level.isClientSide) {
                    for (SustenanceProperties.PossibleEffect effectData : props.getEffects()) {
                        if (entity.getRandom().nextFloat() < effectData.getProbability()) {
                            entity.addEffect(effectData.getEffect());
                        }
                    }
                }
            }

            // Handle item consumption and conversion
            ItemStack resultStack = props.getUsingConvertsTo().orElse(ItemStack.EMPTY);
            if (resultStack.isEmpty()) {
                stack.shrink(1); // Decrease the stack size by 1
                cir.setReturnValue(stack.isEmpty() ? ItemStack.EMPTY : stack);
            } else {
                stack.shrink(1); // Decrease the original stack
                if (stack.isEmpty()) {
                    cir.setReturnValue(resultStack);
                } else {
                    if (entity instanceof Player player && !player.getInventory().add(resultStack)) {
                        player.drop(resultStack, false); // Drop the item if inventory is full
                    }
                    cir.setReturnValue(stack);
                }
            }
        }
    }

    @Inject(method = "getUseAnimation", at = @At("HEAD"), cancellable = true)
    private void modifyUseAnimation(ItemStack stack, CallbackInfoReturnable<UseAnim> cir) {
        if (stack.has(ModDataComponentTypes.SUSTINENCE.get())) {
            // Determine the appropriate animation based on your properties
            cir.setReturnValue(UseAnim.EAT); // Example: setting to EAT animation
        }
    }
    
    @Inject(method = "getUseDuration", at = @At("HEAD"), cancellable = true)
    private void modifyUseDuration(ItemStack stack, LivingEntity entity, CallbackInfoReturnable<Integer> cir) {
        if (stack.has(ModDataComponentTypes.SUSTINENCE.get())) {
            SustenanceProperties props = stack.get(ModDataComponentTypes.SUSTINENCE.get());
            int duration = (int) (props.getConsumeTime() * 20); // Convert seconds to ticks
            cir.setReturnValue(duration);
        }
    }
    
}
