package de.maltesermailo.tigmod.commands;

import de.maltesermailo.tigmod.ConfigTIGMod;
import de.maltesermailo.tigmod.TIGMod;
import de.maltesermailo.tigmod.gauntlet.EnumStones;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

public class TIGModCommands extends CommandBase {

	@Override
	public String getName() {
		return "tigsetstone";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return null;
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if(args.length > 1) {
			if(args[0].equalsIgnoreCase("enable")) {
				String stone = args[1];
				
				if(EnumStones.valueOf(stone) != null) {
					TIGMod.INSTANCE.stonesCrafted.add(EnumStones.valueOf(stone).name());
					TIGMod.INSTANCE.sync();
					sender.sendMessage(new TextComponentString("Stone is not craftable anymore!"));
				} else {
					sender.sendMessage(new TextComponentString("Stone does not exist!"));
				}
			} else if(args[0].equalsIgnoreCase("disable")) {
				String stone = args[1];
				
				if(EnumStones.valueOf(stone) != null) {
					TIGMod.INSTANCE.stonesCrafted.remove(EnumStones.valueOf(stone).name());
					TIGMod.INSTANCE.sync();
					sender.sendMessage(new TextComponentString("Stone is craftable again!"));
				} else {
					sender.sendMessage(new TextComponentString("Stone does not exist!"));
				}
			}
		} else {
			sender.sendMessage(new TextComponentString("/tigsetstone <enable|disable> <stone>"));
		}
	}
	
	@Override
	public int getRequiredPermissionLevel() {
		return 4;
	}

}
