package de.maltesermailo.tigmod.stones;

import de.maltesermailo.tigmod.gauntlet.EnumStones;
import de.maltesermailo.tigmod.gauntlet.InfinityGauntlet;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public interface BasicStone {

	public int changeMode(ItemStack item, EntityPlayer player, boolean powerStone, boolean gauntlet);
	public EnumStones getType();
	public void onClickGauntlet(InfinityGauntlet infinityGauntlet, ItemStack stack, EntityPlayer player);
	
}
