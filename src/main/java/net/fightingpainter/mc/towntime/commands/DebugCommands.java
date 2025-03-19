package net.fightingpainter.mc.towntime.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.world.entity.player.Player;

import net.fightingpainter.mc.towntime.util.Txt;


public class DebugCommands {

    //============================== Register ==============================\\
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("dget")
            .then(Commands.literal("health")
                .executes(context -> getHealth(context, false))
                .then(Commands.literal("round")
                    .executes(context -> getHealth(context, true))
                )
            )
        );

        dispatcher.register(Commands.literal("dset")
            .then(Commands.literal("health")
                .then(Commands.argument("newHealth", FloatArgumentType.floatArg(0))
                    .executes(context -> setHealth(context, FloatArgumentType.getFloat(context, "newHealth")))
                )
            )
        );
    }


    //============================== Methods ==============================\\
    //=========== Get ===========\\
    private static int getHealth(CommandContext<CommandSourceStack> context, boolean round) {
        try {
            Player player = context.getSource().getPlayerOrException(); //get player
            float health = player.getHealth(); //get health
            float maxHealth = player.getMaxHealth(); //get max health
            
            if (round) { //if rounding
                message(context, "Health: " + Math.round(health) + "/" + Math.round(maxHealth)); //send rounded health
            } else { //if not rounding
                message(context, "Health: " + health + "/" + maxHealth); //send health
            }

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


    //============================== Helper ==============================\\
    private static void message(CommandContext<CommandSourceStack> context, String message) { //send message more cleanly
        context.getSource().sendSuccess(() -> Txt.text(message), false);
    }

}
