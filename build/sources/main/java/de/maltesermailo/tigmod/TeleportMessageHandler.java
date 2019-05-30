package de.maltesermailo.tigmod;

import de.maltesermailo.tigmod.gauntlet.InfinityGauntlet;
import de.maltesermailo.tigmod.stones.SpaceStone;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class TeleportMessageHandler implements IMessageHandler<TeleportMessage, IMessage> {

	@Override
	public IMessage onMessage(TeleportMessage message, MessageContext ctx) {
		if(ctx.side == Side.SERVER) {
			EntityPlayerMP serverPlayer = ctx.getServerHandler().player;
			
			serverPlayer.getServerWorld().addScheduledTask(() -> {
				ItemStack item = serverPlayer.getHeldItemMainhand();
				
				if(item.getItem() instanceof SpaceStone || item.getItem() instanceof InfinityGauntlet) {
					TIGMod.INSTANCE.spaceStoneItem.powerTeleportServer(ctx.getServerHandler().player.world, ctx.getServerHandler().player, new BlockPos(message.x, message.y, message.z));
				}
			});
		}
		
		return null;
	}

}
