package de.maltesermailo.tigmod.gauntlet;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class InfinityGauntletContainer extends Container {

	private InfinityGauntletInventory inventory;
	
	public InfinityGauntletContainer(InfinityGauntletInventory inv, InventoryPlayer invPlayer) {
		this.inventory = inv;
		
		this.addSlotToContainer(new SlotInfinity(inv, 0, 8, 18, EnumStones.SPACE_STONE));
		this.addSlotToContainer(new SlotInfinity(inv, 1, 26, 18, EnumStones.POWER_STONE));
		this.addSlotToContainer(new SlotInfinity(inv, 2, 44, 18, EnumStones.REALITY_STONE));
		this.addSlotToContainer(new SlotInfinity(inv, 3, 62, 18, EnumStones.TIME_STONE));
		this.addSlotToContainer(new SlotInfinity(inv, 4, 80, 18, EnumStones.SOUL_STONE));
		this.addSlotToContainer(new SlotInfinity(inv, 5, 98, 18, EnumStones.MIND_STONE));
		
		for (int i = 0; i < 3; ++i)
		{
			for (int j = 0; j < 9; ++j)
			{
				this.addSlotToContainer(new Slot(invPlayer, j + i * 9 + 9, 8 + j * 18, 140 + i * 18));
			}
		}
		
		for (int i = 0; i < 9; ++i)
		{
			this.addSlotToContainer(new Slot(invPlayer, i, 8 + i * 18, 198));
		}
	}
	
	public InfinityGauntletContainer() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
		return ItemStack.EMPTY;
	}
	
	@Override
	public void onContainerClosed(EntityPlayer playerIn) {
		
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return true;
	}

}
