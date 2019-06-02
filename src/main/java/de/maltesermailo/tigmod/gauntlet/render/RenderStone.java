package de.maltesermailo.tigmod.gauntlet.render;

import java.util.Arrays;

import de.maltesermailo.tigmod.gauntlet.EnumStones;
import de.maltesermailo.tigmod.gauntlet.InfinityGauntlet;
import net.minecraft.item.ItemStack;

public class RenderStone {

	public static int getModel(ItemStack stack) {
		String bin = "";
		String[] hiddenStones = {"NONE", "SNAP"};
		for (EnumStones stone : EnumStones.values()) {
			if (Arrays.asList(hiddenStones).contains(stone.name())) {
				continue;
			}
			bin += InfinityGauntlet.checkStone(stack, stone)? 1 : 0;
		}
		try {
			//Space: 32, Power: 16, Reality: 8, Time: 4, Mind: 2, Soul: 1
			return Integer.parseInt(bin, 2);
		}catch(java.lang.NumberFormatException e) {
			System.err.println(e);
			return 0;
		}
	}
}
