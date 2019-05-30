package de.maltesermailo.tigmod.gauntlet;

import de.maltesermailo.tigmod.stones.BasicStone;
import de.maltesermailo.tigmod.stones.SpaceStone;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

public class InfinityGauntletInventory extends InventoryBasic {

	private final ItemStack stack;
	private ItemStack[] items = new ItemStack[this.getSizeInventory()];
	private boolean dirty;
	private boolean reading;
	
	public InfinityGauntletInventory(ItemStack infinityGauntlet) {
		super("Infinity Gauntlet", false, 6);
		this.stack = infinityGauntlet;
		this.openInventory(null);
	}

	@Override
	public void openInventory(EntityPlayer player) {
		if(reading) {
			return;
		}
		
		reading = true;
		NBTTagCompound tag = this.stack.getTagCompound();
		
		if(!tag.hasKey("stones")) {
			tag.setTag("stones", new NBTTagList());
		}
		
		NBTTagList tagList = tag.getTagList("stones", 10); //Type NBTTagCompound
		
		for (int tagCount = 0; tagCount < tagList.tagCount(); tagCount++) {
            NBTTagCompound tagCompound = tagList.getCompoundTagAt(tagCount);
            byte slotID = tagCompound.getByte("Slot");
            if (slotID >= 0 && slotID < getSizeInventory()) {
                setInventorySlotContents(slotID, new ItemStack(tagCompound));
            }
		}
		
		reading = false;
	}
	
	@Override
	public void markDirty() {
		super.markDirty();
		if(!reading) {
			this.closeInventory(null);
		}
	}

	@Override
	public void closeInventory(EntityPlayer player) {
		NBTTagList tagList = new NBTTagList();
        for (int slotCount = 0; slotCount < getSizeInventory(); slotCount++) {
            if (!getStackInSlot(slotCount).isEmpty()) {
                NBTTagCompound tagCompound = new NBTTagCompound();
                tagCompound.setByte("Slot", (byte) slotCount);
                getStackInSlot(slotCount).writeToNBT(tagCompound);
                tagList.appendTag(tagCompound);
            }
        }
        this.stack.getTagCompound().setTag("stones", tagList);
        
        ((InfinityGauntlet) stack.getItem()).updateModel(stack);
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		return stack.getItem() instanceof BasicStone;
	}
	
}
