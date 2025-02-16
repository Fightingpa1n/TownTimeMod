package net.fightingpainter.mc.towntime.coined.blocks.custom;

import javax.annotation.Nullable;

import net.fightingpainter.mc.towntime.coined.blocks.ModBlocks;
import net.fightingpainter.mc.towntime.coined.screens.ShopMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class ShopBlockEntity extends BlockEntity implements MenuProvider {
    private ItemStack storedItem = ItemStack.EMPTY; // Default to no item
    private int value = 0;

    public ShopBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlocks.SHOP_BLOCK_ENTITY.get(), pos, state);
    }

    // Load values from NBT
    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        // Check if "StoredItem" exists before loading to avoid errors
        if (tag.contains("StoredItem")) {
            this.storedItem = ItemStack.CODEC.parse(NbtOps.INSTANCE, tag.get("StoredItem")).result().orElse(ItemStack.EMPTY);
        }
    }

    // Save values into NBT
    @Override
    public void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        
        // Save the item stack if it's not empty
        if (!this.storedItem.isEmpty()) {
            Tag itemTag = this.storedItem.save(registries, new CompoundTag());
            tag.put("StoredItem", itemTag);
        }
    }


    @Override
    public CompoundTag getUpdateTag(Provider registries) {
        CompoundTag tag = new CompoundTag();
        saveAdditional(tag, this.level.registryAccess()); //Include NBT data in updates
        return tag;
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        CompoundTag tag = new CompoundTag();
        this.saveAdditional(tag, this.level.registryAccess());
        return ClientboundBlockEntityDataPacket.create(this);
    }

    
    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt, Provider lookupProvider) {
        CompoundTag tag = pkt.getTag();
        loadAdditional(tag, this.level.registryAccess()); //Load NBT data from updates
    }

    @Override
    public void handleUpdateTag(CompoundTag tag, Provider lookupProvider) {
        loadAdditional(tag, this.level.registryAccess()); //Load NBT data from updates
    }

    public void setStoredItem(ItemStack item) {
        this.storedItem = item.isEmpty() ? ItemStack.EMPTY : item.copy(); // Copy to prevent modifying original stack
        setChanged(); // Mark for saving

        if (level != null && !level.isClientSide) { // Only update if on the server
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }

    public ItemStack getStoredItem() {
        return storedItem;
    }

    @Override
    @Nullable
    public AbstractContainerMenu createMenu(int id, Inventory playerInventory, Player player) {
        return new ShopMenu(id, playerInventory);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("container.towntime.coined.shop");
    }
}
