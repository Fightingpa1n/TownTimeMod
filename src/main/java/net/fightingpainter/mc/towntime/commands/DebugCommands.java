package net.fightingpainter.mc.towntime.commands;

import java.util.stream.Collectors;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.item.ItemStack;
import net.fightingpainter.mc.towntime.food.SustinanceData;
import net.fightingpainter.mc.towntime.mixin.FoodDataMixin;
import net.fightingpainter.mc.towntime.util.Txt;


public class DebugCommands {

    //============================== Register ==============================\\
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("debugValues")

            .then(Commands.literal("health")
                .executes(context -> getHealth(context))
                .then(Commands.literal("set")
                    .then(Commands.argument("newHealth", FloatArgumentType.floatArg(0))
                        .executes(context -> setHealth(context, FloatArgumentType.getFloat(context, "newHealth")))
                    )
                )
                .then(Commands.literal("add")
                    .then(Commands.argument("health", FloatArgumentType.floatArg(0))
                        .executes(context -> addHealth(context, FloatArgumentType.getFloat(context, "health")))
                    )
                )
                .then(Commands.literal("remove")
                    .then(Commands.argument("health", FloatArgumentType.floatArg(0))
                        .executes(context -> addHealth(context, -FloatArgumentType.getFloat(context, "health")))
                    )
                )
            )

            .then(Commands.literal("hunger")
                .executes(context -> getHunger(context))
                .then(Commands.literal("set")
                    .then(Commands.argument("newHunger", IntegerArgumentType.integer(0, 20))
                        .executes(context -> setHunger(context, IntegerArgumentType.getInteger(context, "newHunger")))
                    )
                )
                .then(Commands.literal("add")
                    .then(Commands.argument("hunger", IntegerArgumentType.integer(0))
                        .executes(context -> addHunger(context, IntegerArgumentType.getInteger(context, "hunger")))
                    )
                )
                .then(Commands.literal("remove")
                    .then(Commands.argument("hunger", IntegerArgumentType.integer(0))
                        .executes(context -> addHunger(context, -IntegerArgumentType.getInteger(context, "hunger")))
                    )
                )
            )

            .then(Commands.literal("saturation")
                .executes(context -> getSaturation(context))
                .then(Commands.literal("set")
                    .then(Commands.argument("newSaturation", FloatArgumentType.floatArg(0))
                        .executes(context -> setSaturation(context, FloatArgumentType.getFloat(context, "newSaturation")))
                    )
                )
                .then(Commands.literal("add")
                    .then(Commands.argument("saturation", FloatArgumentType.floatArg(0))
                        .executes(context -> addSaturation(context, FloatArgumentType.getFloat(context, "saturation")))
                    )
                )
                .then(Commands.literal("remove")
                    .then(Commands.argument("saturation", FloatArgumentType.floatArg(0))
                        .executes(context -> addSaturation(context, -FloatArgumentType.getFloat(context, "saturation")))
                    )
                )
            )

            .then(Commands.literal("thirst")
                .executes(context -> getThirst(context))
                .then(Commands.literal("set")
                    .then(Commands.argument("newThirst", IntegerArgumentType.integer(0, 20))
                        .executes(context -> setThirst(context, IntegerArgumentType.getInteger(context, "newThirst")))
                    )
                )
                .then(Commands.literal("add")
                    .then(Commands.argument("thirst", IntegerArgumentType.integer(0))
                        .executes(context -> addThirst(context, IntegerArgumentType.getInteger(context, "thirst")))
                    )
                )
                .then(Commands.literal("remove")
                    .then(Commands.argument("thirst", IntegerArgumentType.integer(0))
                        .executes(context -> addThirst(context, -IntegerArgumentType.getInteger(context, "thirst")))
                    )
                )
            )

            .then(Commands.literal("hydration")
                .executes(context -> getHydration(context))
                .then(Commands.literal("set")
                    .then(Commands.argument("newHydration", FloatArgumentType.floatArg(0))
                        .executes(context -> setHydration(context, FloatArgumentType.getFloat(context, "newHydration")))
                    )
                )
                .then(Commands.literal("add")
                    .then(Commands.argument("hydration", FloatArgumentType.floatArg(0))
                        .executes(context -> addHydration(context, FloatArgumentType.getFloat(context, "hydration")))
                    )
                )
                .then(Commands.literal("remove")
                    .then(Commands.argument("hydration", FloatArgumentType.floatArg(0))
                        .executes(context -> addHydration(context, -FloatArgumentType.getFloat(context, "hydration")))
                    )
                )
            )
        );

        dispatcher.register(Commands.literal("debugItem").executes(context -> getItem(context)));
    }
    
    
    //============================== Methods ==============================\\
    //=========== Health ===========\\
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
    
    private static int addHealth(CommandContext<CommandSourceStack> context, float health) {
        try {
            Player player = context.getSource().getPlayerOrException(); //get player
            health = Math.clamp(player.getHealth()+health, 0, player.getMaxHealth()); //clamp health
            player.setHealth(health); //set health
            message(context, "Health set to " + health); //send message
            return 1; //return success
        } catch (Exception e) {
            e.printStackTrace();
            return 0; //return failure
        }
    }

    //=========== Hunger ===========\\
    private static int getHunger(CommandContext<CommandSourceStack> context) {
        try {
            Player player = context.getSource().getPlayerOrException(); //get player
            SustinanceData sustinanceData = SustinanceData.of(player); //get sustinance data
            int hunger = sustinanceData.getHunger(); //get hunger
            int maxHunger = 20; //get max hunger
            message(context, "Hunger: " + hunger+"/"+maxHunger); //send hunger
            return 1; //return success
        } catch (Exception e) {
            e.printStackTrace();
            return 0; //return failure
        }
    }
    
    private static int setHunger(CommandContext<CommandSourceStack> context, int hunger) {
        try {
            Player player = context.getSource().getPlayerOrException(); //get player
            SustinanceData sustinanceData = SustinanceData.of(player); //get sustinance data
            sustinanceData.setHunger(hunger); //set hunger
            message(context, "Hunger set to " + sustinanceData.getHunger()); //send message
            return 1; //return success
        } catch (Exception e) {
            e.printStackTrace();
            return 0; //return failure
        }
    }
    
    private static int addHunger(CommandContext<CommandSourceStack> context, int hunger) {
        try {
            Player player = context.getSource().getPlayerOrException(); //get player
            SustinanceData sustinanceData = SustinanceData.of(player); //get sustinance data
            sustinanceData.addHunger(hunger); //add hunger
            message(context, "Hunger set to " + sustinanceData.getHunger()); //send message
            return 1; //return success
        } catch (Exception e) {
            e.printStackTrace();
            return 0; //return failure
        }
    }

    //=========== Saturation ===========\\
    private static int getSaturation(CommandContext<CommandSourceStack> context) {
        try {
            Player player = context.getSource().getPlayerOrException(); //get player
            SustinanceData sustinanceData = SustinanceData.of(player); //get sustinance data
            float saturation = sustinanceData.getSaturation(); //get saturation
            float maxSaturation = 20.0f; //get max saturation
            message(context, "Saturation: " + saturation+"/"+maxSaturation); //send saturation
            return 1; //return success
        } catch (Exception e) {
            e.printStackTrace();
            return 0; //return failure
        }
    }

    private static int setSaturation(CommandContext<CommandSourceStack> context, float saturation) {
        try {
            Player player = context.getSource().getPlayerOrException(); //get player
            SustinanceData sustinanceData = SustinanceData.of(player); //get sustinance data
            sustinanceData.setSaturation(saturation); //set saturation
            message(context, "Saturation set to " + sustinanceData.getSaturation()); //send message
            return 1; //return success
        } catch (Exception e) {
            e.printStackTrace();
            return 0; //return failure
        }
    }

    private static int addSaturation(CommandContext<CommandSourceStack> context, float saturation) {
        try {
            Player player = context.getSource().getPlayerOrException(); //get player
            SustinanceData sustinanceData = SustinanceData.of(player); //get sustinance data
            sustinanceData.addSaturation(saturation); //add saturation
            message(context, "Saturation set to " + sustinanceData.getSaturation()); //send message
            return 1; //return success
        } catch (Exception e) {
            e.printStackTrace();
            return 0; //return failure
        }
    }

    //=========== Thirst ===========\\
    private static int getThirst(CommandContext<CommandSourceStack> context) {
        try {
            Player player = context.getSource().getPlayerOrException(); //get player
            SustinanceData sustinanceData = SustinanceData.of(player); //get sustinance data
            int thirst = sustinanceData.getThirst(); //get thirst
            int maxThirst = 20; //get max thirst
            message(context, "Thirst: " + thirst+"/"+maxThirst); //send thirst
            return 1; //return success
        } catch (Exception e) {
            e.printStackTrace();
            return 0; //return failure
        }
    }
    
    private static int setThirst(CommandContext<CommandSourceStack> context, int thirst) {
        try {
            Player player = context.getSource().getPlayerOrException(); //get player
            SustinanceData sustinanceData = SustinanceData.of(player); //get sustinance data
            sustinanceData.setThirst(thirst); //set thirst
            message(context, "Thirst set to " + sustinanceData.getThirst()); //send message
            return 1; //return success
        } catch (Exception e) {
            e.printStackTrace();
            return 0; //return failure
        }
    }
    
    private static int addThirst(CommandContext<CommandSourceStack> context, int thirst) {
        try {
            Player player = context.getSource().getPlayerOrException(); //get player
            SustinanceData sustinanceData = SustinanceData.of(player); //get sustinance data
            sustinanceData.addThirst(thirst); //add thirst
            message(context, "Thirst set to " + sustinanceData.getThirst()); //send message
            return 1; //return success
        } catch (Exception e) {
            e.printStackTrace();
            return 0; //return failure
        }
    }

    //=========== Hydration ===========\\
    private static int getHydration(CommandContext<CommandSourceStack> context) {
        try {
            Player player = context.getSource().getPlayerOrException(); //get player
            SustinanceData sustinanceData = SustinanceData.of(player); //get sustinance data
            float hydration = sustinanceData.getHydration(); //get hydration
            float maxHydration = 20.0f; //get max hydration
            message(context, "Hydration: " + hydration+"/"+maxHydration); //send hydration
            return 1; //return success
        } catch (Exception e) {
            e.printStackTrace();
            return 0; //return failure
        }
    }
    
    private static int setHydration(CommandContext<CommandSourceStack> context, float hydration) {
        try {
            Player player = context.getSource().getPlayerOrException(); //get player
            SustinanceData sustinanceData = SustinanceData.of(player); //get sustinance data
            sustinanceData.setHydration(hydration); //set hydration
            message(context, "Hydration set to " + sustinanceData.getHydration()); //send message
            return 1; //return success
        } catch (Exception e) {
            e.printStackTrace();
            return 0; //return failure
        }
    }
    
    private static int addHydration(CommandContext<CommandSourceStack> context, float hydration) {
        try {
            Player player = context.getSource().getPlayerOrException(); //get player
            SustinanceData sustinanceData = SustinanceData.of(player); //get sustinance data
            sustinanceData.addHydration(hydration); //add hydration
            message(context, "Hydration set to " + sustinanceData.getHydration()); //send message
            return 1; //return success
        } catch (Exception e) {
            e.printStackTrace();
            return 0; //return failure
        }
    }

    //=========== Else ===========\\
    private static int getItem(CommandContext<CommandSourceStack> context) {
        try {
            Player player = context.getSource().getPlayerOrException(); //get player
            ItemStack item = player.getMainHandItem(); //get item

            //get ItemStack components
            String name = item.getHoverName().getString(); //get name
            DataComponentMap components = item.getComponents();
            String tags = item.getTags()
                .map(TagKey::location)
                .map(Object::toString)
                .collect(Collectors.joining(", "));

            message(context, name+" :\n components: "+components.toString()+"\n\n tags: "+tags); //send item
            
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
