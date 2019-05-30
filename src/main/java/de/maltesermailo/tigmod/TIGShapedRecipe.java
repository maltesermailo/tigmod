package de.maltesermailo.tigmod;

import de.maltesermailo.tigmod.stones.BasicStone;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.util.NonNullList;

public class TIGShapedRecipe extends ShapedRecipes {

	public TIGShapedRecipe(String group, int width, int height, NonNullList<Ingredient> ingredients, ItemStack result) {
		super(group, width, height, ingredients, result);
	}
	
	@Override
	public ItemStack getRecipeOutput() {
		if(super.getRecipeOutput().getItem() instanceof BasicStone) {
			if(TIGMod.INSTANCE.stonesCrafted.contains(((BasicStone) super.getRecipeOutput().getItem()).getType().name())) {
				return new ItemStack(TIGMod.INSTANCE.dualCompressedStarItem, 8);
			}
		}
		return super.getRecipeOutput();
	}

}
