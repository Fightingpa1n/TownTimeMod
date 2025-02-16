package net.fightingpainter.mc.towntime.coined.blocks;

import java.util.function.Supplier;

import net.fightingpainter.mc.towntime.TownTime;
import net.fightingpainter.mc.towntime.coined.blocks.custom.ShopBlock;
import net.fightingpainter.mc.towntime.coined.blocks.custom.ShopBlockEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCK_REGISTER = DeferredRegister.createBlocks(TownTime.MOD_ID);
    public static final DeferredRegister.Items ITEM_REGISTER = DeferredRegister.createItems(TownTime.MOD_ID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, TownTime.MOD_ID);
    
    
    //Shop Blocks
    public static final DeferredBlock<Block> SHOP_BLOCK_WHITE = registerShopBlock("shop_block_white"); //white / default
    public static final DeferredBlock<Block> SHOP_BLOCK_LIGHT_GRAY = registerShopBlock("shop_block_light_gray"); //light gray
    public static final DeferredBlock<Block> SHOP_BLOCK_GRAY = registerShopBlock("shop_block_gray"); //gray
    public static final DeferredBlock<Block> SHOP_BLOCK_BLACK = registerShopBlock("shop_block_black"); //black
    public static final DeferredBlock<Block> SHOP_BLOCK_BROWN = registerShopBlock("shop_block_brown"); //brown
    public static final DeferredBlock<Block> SHOP_BLOCK_RED = registerShopBlock("shop_block_red"); //red
    public static final DeferredBlock<Block> SHOP_BLOCK_ORANGE = registerShopBlock("shop_block_orange"); //orange
    public static final DeferredBlock<Block> SHOP_BLOCK_YELLOW = registerShopBlock("shop_block_yellow"); //yellow
    public static final DeferredBlock<Block> SHOP_BLOCK_LIME = registerShopBlock("shop_block_lime"); //lime
    public static final DeferredBlock<Block> SHOP_BLOCK_GREEN = registerShopBlock("shop_block_green"); //green
    public static final DeferredBlock<Block> SHOP_BLOCK_CYAN = registerShopBlock("shop_block_cyan"); //cyan
    public static final DeferredBlock<Block> SHOP_BLOCK_LIGHT_BLUE = registerShopBlock("shop_block_light_blue"); //light blue
    public static final DeferredBlock<Block> SHOP_BLOCK_BLUE = registerShopBlock("shop_block_blue"); //blue
    public static final DeferredBlock<Block> SHOP_BLOCK_PURPLE = registerShopBlock("shop_block_purple"); //purple
    public static final DeferredBlock<Block> SHOP_BLOCK_MAGENTA = registerShopBlock("shop_block_magenta"); //magenta
    public static final DeferredBlock<Block> SHOP_BLOCK_PINK = registerShopBlock("shop_block_pink"); //pink
    
    //Shop Block Items
    public static final DeferredItem<BlockItem> SHOP_BLOCK_WHITE_ITEM = registerShopBlockItem(SHOP_BLOCK_WHITE);
    public static final DeferredItem<BlockItem> SHOP_BLOCK_LIGHT_GRAY_ITEM = registerShopBlockItem(SHOP_BLOCK_LIGHT_GRAY);
    public static final DeferredItem<BlockItem> SHOP_BLOCK_GRAY_ITEM = registerShopBlockItem(SHOP_BLOCK_GRAY);
    public static final DeferredItem<BlockItem> SHOP_BLOCK_BLACK_ITEM = registerShopBlockItem(SHOP_BLOCK_BLACK);
    public static final DeferredItem<BlockItem> SHOP_BLOCK_BROWN_ITEM = registerShopBlockItem(SHOP_BLOCK_BROWN);
    public static final DeferredItem<BlockItem> SHOP_BLOCK_RED_ITEM = registerShopBlockItem(SHOP_BLOCK_RED);
    public static final DeferredItem<BlockItem> SHOP_BLOCK_ORANGE_ITEM = registerShopBlockItem(SHOP_BLOCK_ORANGE);
    public static final DeferredItem<BlockItem> SHOP_BLOCK_YELLOW_ITEM = registerShopBlockItem(SHOP_BLOCK_YELLOW);
    public static final DeferredItem<BlockItem> SHOP_BLOCK_LIME_ITEM = registerShopBlockItem(SHOP_BLOCK_LIME);
    public static final DeferredItem<BlockItem> SHOP_BLOCK_GREEN_ITEM = registerShopBlockItem(SHOP_BLOCK_GREEN);
    public static final DeferredItem<BlockItem> SHOP_BLOCK_CYAN_ITEM = registerShopBlockItem(SHOP_BLOCK_CYAN);
    public static final DeferredItem<BlockItem> SHOP_BLOCK_LIGHT_BLUE_ITEM = registerShopBlockItem(SHOP_BLOCK_LIGHT_BLUE);
    public static final DeferredItem<BlockItem> SHOP_BLOCK_BLUE_ITEM = registerShopBlockItem(SHOP_BLOCK_BLUE);
    public static final DeferredItem<BlockItem> SHOP_BLOCK_PURPLE_ITEM = registerShopBlockItem(SHOP_BLOCK_PURPLE);
    public static final DeferredItem<BlockItem> SHOP_BLOCK_MAGENTA_ITEM = registerShopBlockItem(SHOP_BLOCK_MAGENTA);
    public static final DeferredItem<BlockItem> SHOP_BLOCK_PINK_ITEM = registerShopBlockItem(SHOP_BLOCK_PINK);
    
    private static DeferredBlock<Block> registerShopBlock(String name) {
        DeferredBlock<Block> block = BLOCK_REGISTER.register(name, () -> new ShopBlock());
        return block;
    }
    
    private static DeferredItem<BlockItem> registerShopBlockItem(DeferredBlock<Block> block) {
        return ITEM_REGISTER.registerSimpleBlockItem(block.getId().getPath(), block);
    }
    
    
    public static final Supplier<BlockEntityType<ShopBlockEntity>> SHOP_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register("my_block_entity",
        () -> BlockEntityType.Builder.of(ShopBlockEntity::new,

            //Blocks that use this block entity
            SHOP_BLOCK_WHITE.get(),
            SHOP_BLOCK_LIGHT_GRAY.get(),
            SHOP_BLOCK_GRAY.get(),
            SHOP_BLOCK_BLACK.get(),
            SHOP_BLOCK_BROWN.get(),
            SHOP_BLOCK_RED.get(),
            SHOP_BLOCK_ORANGE.get(),
            SHOP_BLOCK_YELLOW.get(),
            SHOP_BLOCK_LIME.get(),
            SHOP_BLOCK_GREEN.get(),
            SHOP_BLOCK_CYAN.get(),
            SHOP_BLOCK_LIGHT_BLUE.get(),
            SHOP_BLOCK_BLUE.get(),
            SHOP_BLOCK_PURPLE.get(),
            SHOP_BLOCK_MAGENTA.get(),
            SHOP_BLOCK_PINK.get()
        
        ).build(null)
    );
    
    /**
     * Register blocks with the mod event bus
     * (should be called in mod constructor)
     * @param modEventBus
     */
    public static void register(IEventBus modEventBus) {
        TownTime.LOGGER.info("Registering blocks...");
        BLOCK_ENTITY_TYPES.register(modEventBus);
        BLOCK_REGISTER.register(modEventBus);
        ITEM_REGISTER.register(modEventBus);
    }
}
