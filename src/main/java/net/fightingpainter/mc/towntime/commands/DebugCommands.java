package net.fightingpainter.mc.towntime.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.fightingpainter.mc.towntime.util.Txt;


public class DebugCommands {

    //============================== Register ==============================\\
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("dget")
            .then(Commands.literal("health").executes(context -> getHealth(context)))
            .then(Commands.literal("hunger").executes(context -> getHunger(context)))
            .then(Commands.literal("saturation").executes(context -> getSaturation(context)))
        );

        dispatcher.register(Commands.literal("dset")
            .then(Commands.literal("health")
                .then(Commands.argument("newHealth", FloatArgumentType.floatArg(0))
                    .executes(context -> setHealth(context, FloatArgumentType.getFloat(context, "newHealth")))
                )
            )
            .then(Commands.literal("hunger")
                .then(Commands.argument("newHunger", IntegerArgumentType.integer(0, 20))
                    .executes(context -> setHunger(context, IntegerArgumentType.getInteger(context, "newHunger")))
                )
            )
        );
    }


    //============================== Methods ==============================\\
    //=========== Get ===========\\
    private static int getHealth(CommandContext<CommandSourceStack> context) {
        try {
            Player player = context.getSource().getPlayerOrException(); //get player
            float health = player.getHealth(); //get health
            float maxHealth = player.getMaxHealth(); //get max health
            message(context, "Health: " + health+"/"+maxHealth); //send health
            return 1; //return success
        } catch (Exception e) {
            e.printStackTrace();
            return 0; //return failure
        }
    }

    private static int getHunger(CommandContext<CommandSourceStack> context) {
        try {
            Player player = context.getSource().getPlayerOrException(); //get player
            int hunger = player.getFoodData().getFoodLevel(); //get hunger
            int maxHunger = 20; //get max hunger
            message(context, "Hunger: " + hunger+"/"+maxHunger); //send hunger
            return 1; //return success
        } catch (Exception e) {
            e.printStackTrace();
            return 0; //return failure
        }
    }

    private static int getSaturation(CommandContext<CommandSourceStack> context) {
        try {
            Player player = context.getSource().getPlayerOrException(); //get player
            float saturation = player.getFoodData().getSaturationLevel(); //get saturation
            float maxSaturation = 20; //get max saturation
            message(context, "Saturation: " + saturation+"/"+maxSaturation); //send saturation
            return 1; //return success
        } catch (Exception e) {
            e.printStackTrace();
            return 0; //return failure
        }
    }
    
    //=========== Set ===========\\
    private static int setHealth(CommandContext<CommandSourceStack> context, float health) {
        try {
            Player player = context.getSource().getPlayerOrException(); //get player
            health = Math.clamp(health, 0, player.getMaxHealth()); //clamp health
            player.setHealth(health); //set health
            message(context, "Health set to " + health); //send message
            return 1; //return success
        } catch (Exception e) {
            e.printStackTrace();
            return 0; //return failure
        }
    }

    private static int setHunger(CommandContext<CommandSourceStack> context, int hunger) {
        try {
            Player player = context.getSource().getPlayerOrException(); //get player
            hunger = Math.clamp(hunger, 0, 20); //clamp hunger
            player.getFoodData().setFoodLevel(hunger); //set hunger
            message(context, "Hunger set to " + hunger); //send message
            return 1; //return success
        } catch (Exception e) {
            e.printStackTrace();
            return 0; //return failure
        }
    }

    // private static int setSaturation(CommandContext<CommandSourceStack> context, float saturation) { //TODO
    //     try {
    //         Player player = context.getSource().getPlayerOrException(); //get player
    //         FoodData foodData = player.getFoodData(); //get food data
    //         //we can't directly add or set the saturation level, so...
    //         //I just took a look at the adding of Saturation and there are modifiers and such,
    //         //so I realised I don't have the slightest clue how saturation works. like I know the game mechanic, but not how the code begind it works.
    //         //and it's hard to find anything online as the first results are always about the game mechanic... a

    //         message(context, "Saturation set to " + saturation); //send message
    //         return 1; //return success
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //         return 0; //return failure
    //     }
    // }


    //============================== Helper ==============================\\
    private static void message(CommandContext<CommandSourceStack> context, String message) { //send message more cleanly
        context.getSource().sendSuccess(() -> Txt.text(message), false);
    }

}
