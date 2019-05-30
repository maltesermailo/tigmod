package de.maltesermailo.tigmod.proxy;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ProxyClient extends Proxy {
	
	public KeyBinding keyBindingChangeMode = new KeyBinding("Change Mode for Infinity Stone", Keyboard.KEY_V, "TIG Mod");
	public KeyBinding keyBindingChangeStone = new KeyBinding("Change Stone for Infinity Gauntlet", Keyboard.KEY_X, "TIG Mod");
	
	@Override
	public void preInit(FMLPreInitializationEvent e) {
		super.preInit(e);
		
		ClientRegistry.registerKeyBinding(keyBindingChangeMode);
		ClientRegistry.registerKeyBinding(keyBindingChangeStone);
	}

}
