package net.fightingpainter.mc.towntime.coined.screens;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class ShopMenu extends AbstractContainerMenu {
    private final Container container;
    private final ContainerData data;

    public ShopMenu(int id, Inventory playerInventory) {
        this(id, playerInventory, new SimpleContainer(9), new SimpleContainerData(1));
    }

    public ShopMenu(int id, Inventory playerInventory, FriendlyByteBuf buf) {
        this(id, playerInventory, new SimpleContainer(9), new SimpleContainerData(1));
    }

    public ShopMenu(int id, Inventory playerInventory, Container container, ContainerData data) {
        super(ModScreens.SHOP_MENU.get(), id);
        this.container = container;
        this.data = data;

        //Player Inventory
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                this.addSlot(new Slot(playerInventory, col + row * 9 + 9, 108 + col * 18, 84 + row * 18));
            }
        }

        //Player Hotbar
        for (int col = 0; col < 9; ++col) {
            this.addSlot(new Slot(playerInventory, col, 108 + col * 18, 142));
        }
    }

    @Override
    public boolean stillValid(Player player) {
        return this.container.stillValid(player);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int slotIndex) {
        Slot slot = this.slots.get(slotIndex);
        if (slot == null || !slot.hasItem()) {
            return ItemStack.EMPTY;
        }

        ItemStack stack = slot.getItem();
        ItemStack result = stack.copy();
        
        if (slotIndex < 27) { //if slot is in Inventory
            //move to first slot in Hotbar
            if (!this.moveItemStackTo(stack, 27, 36, false)) {
                return ItemStack.EMPTY;
            }
        } else if (slotIndex < 36) { //if slot is in Hotbar
            //move to first slot in Inventory
            if (!this.moveItemStackTo(stack, 0, 27, false)) {
                return ItemStack.EMPTY;
            }
        }


        if (stack.isEmpty()) {
            slot.set(ItemStack.EMPTY);
        } else {
            slot.setChanged();
        }

        return result;
    }
}
