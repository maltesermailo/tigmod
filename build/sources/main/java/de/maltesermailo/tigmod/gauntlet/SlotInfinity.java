package de.maltesermailo.tigmod.gauntlet;

import de.maltesermailo.tigmod.stones.BasicStone;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotInfinity extends Slot {
	
	private EnumStones stoneType;

	public SlotInfinity(IInventory inventoryIn, int index, int xPosition, int yPosition, EnumStones stone) {
		super(inventoryIn, index, xPosition, yPosition);
		this.stoneType = stone;
	}
	
	@Override
	public boolean isItemValid(ItemStack stack) {
		if(stack.getItem() instanceof BasicStone) {
			return ((BasicStone)stack.getItem()).getType().equals(this.stoneType);
		}
		return false;
	}
	
	@Override
	public ItemStack getStack() {
		return this.inventory.getStackInSlot(this.slotNumber);
	}

}
