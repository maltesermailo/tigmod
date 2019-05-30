package de.maltesermailo.tigmod.ingredients;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class ItemCompressedStar extends Item {
	
	public ItemCompressedStar() {
		setUnlocalizedName("compressed_star");
		setRegistryName("compressed_star");
		setCreativeTab(CreativeTabs.MATERIALS);
	}
	
	public static class ItemDualCompressedStar extends Item {
		
		public ItemDualCompressedStar() {
			setUnlocalizedName("dual_compressed_star");
			setRegistryName("dual_compressed_star");
			setCreativeTab(CreativeTabs.MATERIALS);
		}
		
	}

}
