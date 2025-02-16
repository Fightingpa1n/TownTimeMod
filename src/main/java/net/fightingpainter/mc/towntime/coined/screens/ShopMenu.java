package net.fightingpainter.mc.towntime.coined.screens;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
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

        // Add slots and data slots here
    }

    @Override
    public boolean stillValid(Player player) {
        return this.container.stillValid(player);
    }

    @Override
    public ItemStack quickMoveStack(Player arg0, int arg1) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'quickMoveStack'");
    }
}
