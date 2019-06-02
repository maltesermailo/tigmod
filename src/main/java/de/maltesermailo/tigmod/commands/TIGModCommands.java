package de.maltesermailo.tigmod.commands;

import java.util.Arrays;

import de.maltesermailo.tigmod.ConfigTIGMod;
import de.maltesermailo.tigmod.TIGMod;
import de.maltesermailo.tigmod.gauntlet.EnumStones;
import de.maltesermailo.tigmod.stones.BasicStone;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class TIGModCommands extends CommandBase {
	
	private void sendInvaild(ICommandSender sender) {
		String msg = "§c---§lTIG§r§c Mod---\n";
		msg += "§6Help:\n";
		msg += "§e/tig\n";
		msg += "§e    info\n";
		msg += "§e    setstone <enable|disable> <stone>\n";
		msg += "§c-------------";
		sender.sendMessage(new TextComponentString(msg));
	}

	@Override
	public String getName() {
		return "tig";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return null;
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if(args.length >= 1) {
			switch (args[0]) {
			case "info":
				String[] hiddenStones = {"NONE", "SNAP"};
				String msg = "§c---§lTIG§r§c Mod---\n§6Stone Info:\n";
				for (EnumStones stone : EnumStones.values()) {
					if (Arrays.asList(hiddenStones).contains(stone.name())) {
						continue;
					}
					  if (TIGMod.INSTANCE.stonesCrafted.contains(stone.name())) {
						  msg += "§e" + stone.getCaption() + ": §4Not Craftable\n";
					  }else {
						  msg += "§e" + stone.getCaption() + ": §2Craftable\n";
					  }
					}
				msg += "§c-------------";
				sender.sendMessage(new TextComponentString(msg));
				break;
			case "setstone":
				EntityPlayer player = (EntityPlayer)sender;
				if (player.canUseCommandBlock()) { //Not sure if .canUseCommandBlock() is the right way to do it, but its the best way I found.
					if(args[1].equalsIgnoreCase("enable")) {
						String stone = args[2];
						
						if(EnumStones.valueOf(stone) != null) {
							TIGMod.INSTANCE.stonesCrafted.add(EnumStones.valueOf(stone).name());
							TIGMod.INSTANCE.sync();
							sender.sendMessage(new TextComponentString("Stone is not craftable anymore!"));
						} else {
							sender.sendMessage(new TextComponentString("Stone does not exist!"));
						}
					} else if(args[1].equalsIgnoreCase("disable")) {
						String stone = args[2];
						
						if(EnumStones.valueOf(stone) != null) {
							TIGMod.INSTANCE.stonesCrafted.remove(EnumStones.valueOf(stone).name());
							TIGMod.INSTANCE.sync();
							sender.sendMessage(new TextComponentString("Stone is craftable again!"));
						} else {
							sender.sendMessage(new TextComponentString("Stone does not exist!"));
						}
					}
				}else {
					sender.sendMessage(new TextComponentString("You have not the Persmisson the enable/disable the Stones"));
				}
				break;
			default:
				sendInvaild(sender);
			}
		} else {
			sendInvaild(sender);
		}
	}
	
	@Override
	public boolean checkPermission(MinecraftServer server, ICommandSender sender)
    {
        return true;
    }
	
	@Override
	public int getRequiredPermissionLevel() {
		return 0;
	}

}
