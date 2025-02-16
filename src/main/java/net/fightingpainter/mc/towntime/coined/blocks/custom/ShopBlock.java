package net.fightingpainter.mc.towntime.coined.blocks.custom;

import org.jetbrains.annotations.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ShopBlock extends Block implements EntityBlock {
    public ShopBlock() {
        super(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).strength(2.0f).noOcclusion());
    }


    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ShopBlockEntity(pos, state);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (level.isClientSide) {return ItemInteractionResult.CONSUME;}

        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (!(blockEntity instanceof ShopBlockEntity)) {return ItemInteractionResult.FAIL;}
        ShopBlockEntity shopBlockEntity = (ShopBlockEntity) blockEntity;
        
        MenuProvider menuProvider = this.getMenuProvider(state, level, pos);
        if (menuProvider != null) {
            ServerPlayer serverPlayer = (ServerPlayer) player;
            serverPlayer.openMenu(menuProvider);
            return ItemInteractionResult.SUCCESS;
        }
        else {
            return ItemInteractionResult.FAIL;
        }

        // if (player.isCrouching()) { //if player is sneaking 
        //     String itemName = shopBlockEntity.getStoredItem().getHoverName().getString();
        //     player.sendSystemMessage(Component.literal("Stored: "+itemName));
        //     return ItemInteractionResult.SUCCESS;
        // } else {
        //     shopBlockEntity.setStoredItem(stack.copy());
        //     return ItemInteractionResult.SUCCESS;
        // }
    }



    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        VoxelShape shape = Shapes.empty();

        //simple voxel shape
        shape = Shapes.join(shape, Shapes.box(0.0625, 0, 0.0625, 0.9375, 0.1875, 0.9375), BooleanOp.OR);

        //fancy voxel shape
        // shape = Shapes.join(shape, Shapes.box(0.6875, 0, 0.0625, 0.9375, 0.125, 0.3125), BooleanOp.OR);
        // shape = Shapes.join(shape, Shapes.box(0.0625, 0, 0.0625, 0.3125, 0.125, 0.3125), BooleanOp.OR);
        // shape = Shapes.join(shape, Shapes.box(0.6875, 0, 0.6875, 0.9375, 0.125, 0.9375), BooleanOp.OR);
        // shape = Shapes.join(shape, Shapes.box(0.0625, 0, 0.6875, 0.3125, 0.125, 0.9375), BooleanOp.OR);
        // shape = Shapes.join(shape, Shapes.box(0.125, 0, 0.125, 0.875, 0.1875, 0.875), BooleanOp.OR);

        return shape;
    }

    @Override
    public MenuProvider getMenuProvider(BlockState state, Level level, BlockPos pos) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        return blockEntity instanceof MenuProvider ? (MenuProvider) blockEntity : null;
    }




}
