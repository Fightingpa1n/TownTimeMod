package net.fightingpainter.mc.towntime.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.fightingpainter.mc.towntime.food.ConsumableHelper;
import net.fightingpainter.mc.towntime.food.SustenanceProperties;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

@Mixin(Item.class)
public class ItemMixin {

    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    public void useSustenance(Level level, Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResultHolder<ItemStack>> cir) {
        ItemStack stack = player.getItemInHand(hand);

        if (ConsumableHelper.isConsumable(stack)) { //if consumable
            if (ConsumableHelper.canConsume(player, stack)) { //if the player can consume the item
                player.startUsingItem(hand);
                cir.setReturnValue(InteractionResultHolder.consume(stack)); // Override vanilla
            } else {
                cir.setReturnValue(InteractionResultHolder.fail(stack));
            }
        }
    }


    @Inject(method = "finishUsingItem", at = @At("HEAD"), cancellable = true)
    public void finishSustenance(ItemStack stack, Level level, LivingEntity entity, CallbackInfoReturnable<ItemStack> cir) {
        if (ConsumableHelper.isConsumable(stack)) {
            SustenanceProperties props = ConsumableHelper.getSustenanceProperties(stack);

            //Apply hunger and thirst
            if (entity instanceof Player player) {
                ConsumableHelper.consume(player, props);
                
                //Apply effects
                if (!level.isClientSide) {
                    ConsumableHelper.applyEffects(player, props);
                }
            }

            //Handle item consumption and conversion
            if (!props.hasConversionItem()) {
                stack.shrink(1); //Decrease the stack size by 1
                cir.setReturnValue(stack.isEmpty() ? ItemStack.EMPTY : stack); //return stack
            } else { //if conversion item
                ItemStack conversionStack = props.getUsingConvertsTo().orElse(ItemStack.EMPTY);
                stack.shrink(1); //Decrease the original stack
                if (stack.isEmpty()) { //if stack is empty
                    cir.setReturnValue(conversionStack); //return conversion stack
                } else { //if stack is not empty
                    if (entity instanceof Player player && !player.getInventory().add(conversionStack)) { //try to add the conversion item to the player's inventory
                        player.drop(conversionStack, false); // Drop the item if inventory is full
                    }
                    cir.setReturnValue(stack);
                }
            }
        }
    }

    @Inject(method = "getUseAnimation", at = @At("HEAD"), cancellable = true)
    private void modifyUseAnimation(ItemStack stack, CallbackInfoReturnable<UseAnim> cir) {
        if (ConsumableHelper.isConsumable(stack)) {
            cir.setReturnValue(UseAnim.EAT); // Example: setting to EAT animation
        }
    }
    
    @Inject(method = "getUseDuration", at = @At("HEAD"), cancellable = true)
    private void modifyUseDuration(ItemStack stack, LivingEntity entity, CallbackInfoReturnable<Integer> cir) {
        if (ConsumableHelper.isConsumable(stack)) {

            SustenanceProperties props = ConsumableHelper.getSustenanceProperties(stack);
            int duration = (int) (props.getConsumeTime() * 20); // Convert seconds to ticks
            cir.setReturnValue(duration);
        }
    }
}
