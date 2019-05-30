package de.maltesermailo.tigmod.proxy;

import de.maltesermailo.tigmod.ChangeModeMessage;
import de.maltesermailo.tigmod.ChangeModeMessageHandler;
import de.maltesermailo.tigmod.TIGMod;
import de.maltesermailo.tigmod.TeleportMessage;
import de.maltesermailo.tigmod.TeleportMessageHandler;
import de.maltesermailo.tigmod.gauntlet.GuiInfinityGauntletInventory;
import de.maltesermailo.tigmod.gauntlet.InfinityGauntletContainer;
import de.maltesermailo.tigmod.gauntlet.InfinityGauntletInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class Proxy implements IGuiHandler {
	
	public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel("tigmod");
	
	public void preInit(FMLPreInitializationEvent e) {
		
	}
	
	public void init(FMLInitializationEvent e) {
		INSTANCE.registerMessage(new ChangeModeMessageHandler(), ChangeModeMessage.class, 1, Side.SERVER);
		INSTANCE.registerMessage(new TeleportMessageHandler(), TeleportMessage.class, 2, Side.SERVER);
		
		NetworkRegistry.INSTANCE.registerGuiHandler(TIGMod.INSTANCE, this);
	}
	
	public void postInit(FMLPostInitializationEvent e) {
		
	}

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		return new InfinityGauntletContainer(new InfinityGauntletInventory(player.getHeldItemMainhand()), player.inventory);
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		return new GuiInfinityGauntletInventory(new InfinityGauntletContainer(new InfinityGauntletInventory(player.getHeldItemMainhand()), player.inventory));
	}

}
