package de.maltesermailo.tigmod.stones;

import de.maltesermailo.tigmod.gauntlet.EnumStones;
import de.maltesermailo.tigmod.gauntlet.InfinityGauntlet;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;

public class Snap implements BasicStone {

	@Override
	public int changeMode(ItemStack item, EntityPlayer player, boolean powerStone, boolean gauntlet) {
		return 0;
	}

	@Override
	public EnumStones getType() {
		return EnumStones.SNAP;
	}

	@Override
	public void onClickGauntlet(InfinityGauntlet infinityGauntlet, ItemStack stack, EntityPlayer player) {
		for(EntityLivingBase entity : player.world.getEntitiesWithinAABB(EntityLivingBase.class, player.getEntityBoundingBox().grow(50, 50, 50))) {
			if(!entity.isEntityEqual(player)) {
				entity.attackEntityFrom(DamageSource.MAGIC, Float.MAX_VALUE);
			}
		}
	}

}
