package team.chisel.common.inventory;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import team.chisel.api.IChiselItem;
import team.chisel.common.item.ItemChisel;

public class InventoryChiselSelection implements IInventory {

    ItemStack chisel = null;
    public final int size;
    public int activeVariations = 0;
    ContainerChisel container;
    ItemStack[] inventory;

    public InventoryChiselSelection(ItemStack c, int size) {
        super();
        this.size = size;
        inventory = new ItemStack[size + 1];
        chisel = c;
    }

    public void onInventoryUpdate(int slot) {
    }

    @Override
    public int getSizeInventory() {
        return size + 1;
    }

    @Override
    public ItemStack getStackInSlot(int var1) {
        return inventory[var1];
    }

    public void updateInventoryState(int slot) {
        onInventoryUpdate(slot);
    }

    @Override
    public ItemStack decrStackSize(int slot, int amount) {
        if (this.inventory[slot] != null) {
            ItemStack stack;
            if (this.inventory[slot].stackSize <= amount) {
                stack = this.inventory[slot];
                this.inventory[slot] = null;
                updateInventoryState(slot);
                return stack;
            } else {
                stack = this.inventory[slot].splitStack(amount);

                if (this.inventory[slot].stackSize == 0)
                    this.inventory[slot] = null;

                updateInventoryState(slot);

                return stack;
            }
        } else
            return null;
    }

    @Override
    public ItemStack removeStackFromSlot(int slot){
        ItemStack stack = getStackInSlot(slot);

        if (stack == null)
            return null;
        inventory[slot] = null;

        updateInventoryState(slot);
        return stack;
    }

    @Override
    public String getName() {
        return "container.chisel";
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public void markDirty() {

    }

    @Override
    public ITextComponent getDisplayName() {
        return new TextComponentTranslation(getName());
    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer entityplayer) {
        ItemStack held = entityplayer.inventory.getStackInSlot(container.getChiselSlot());
        return held != null && held.getItem() instanceof IChiselItem;
    }

    public void clearItems() {
        activeVariations = 0;
        for (int i = 0; i < size; i++) {
            inventory[i] = null;
        }
    }

    public ItemStack getStackInSpecialSlot() {
        return inventory[size];
    }
    
    public void setStackInSpecialSlot(ItemStack stack) {
        inventory[size] = stack;
    }

    public void updateItems() {
        ItemStack chiseledItem = inventory[size];
        clearItems();

        if (chiseledItem == null) {
            return;
        }

        Item item = chiseledItem.getItem();
        if (item == null) {
            return;
        }

        List<ItemStack> list = container.getCarving().getItemsForChiseling(chiseledItem);

        activeVariations = 0;
        while (activeVariations < size && activeVariations < list.size()) {
            inventory[activeVariations] = list.get(activeVariations);
            activeVariations++;
        }
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack stack) {
        inventory[slot] = stack;
        updateInventoryState(slot);
    }


    @Override
    public boolean isItemValidForSlot(int i, ItemStack stack) {
        //Really didn't think people would chisel a shovel
        if (stack.getItem() instanceof ItemTool) {
            return false;
        }

        return !(stack != null && (stack.getItem() instanceof ItemChisel)) && i == size;
    }

    @Override
    public void clear() {
        inventory = new ItemStack[inventory.length];
    }

    @Override
    public int getField(int var1) {
        return var1;
    }

    @Override
    public void setField(int var1, int var2) {
    }

    @Override
    public int getFieldCount() {
        return 0;
    }

    @Override
    public void openInventory(EntityPlayer var1) {

    }

    @Override
    public void closeInventory(EntityPlayer var1) {

    }

}