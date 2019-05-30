package de.maltesermailo.tigmod.gauntlet;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;

public class GuiInfinityGauntletInventory extends GuiContainer {

	public ResourceLocation iconLocation = new ResourceLocation("thanosinfinitygauntlet","textures/gui/inventory.png");
	
	public GuiInfinityGauntletInventory(Container inventorySlotsIn) {
		super(inventorySlotsIn);
		
		this.ySize = 114 + 6 * 18;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(iconLocation);
		
		int k = (this.width - this.xSize) / 2;
		int l = (this.height - this.ySize) / 2;
		
		this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
	}
	
	protected void drawGuiContainerForegroundLayer(int par1, int par2)
	{
		String s = "Infinity Gauntlet";
		this.fontRenderer.drawString(s, this.xSize / 2 - this.fontRenderer.getStringWidth(s) / 2, 4, 4210752);
	}
}
