package de.maltesermailo.tigmod;

import de.maltesermailo.tigmod.gauntlet.InfinityGauntlet;
import de.maltesermailo.tigmod.stones.BasicStone;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class ChangeModeMessageHandler implements IMessageHandler<ChangeModeMessage, IMessage> {

	@Override
	public IMessage onMessage(ChangeModeMessage message, MessageContext ctx) {
		if(ctx.side == Side.SERVER) {
			EntityPlayerMP serverPlayer = ctx.getServerHandler().player;
			
			serverPlayer.getServerWorld().addScheduledTask(() -> {
				if(message.modeId == 0) {
					ItemStack item = serverPlayer.getHeldItemMainhand();
					
					if(item.getItem() instanceof BasicStone) {
						((BasicStone) item.getItem()).changeMode(item, serverPlayer, false, false);
					}
					if(item.getItem() instanceof InfinityGauntlet) {
						((InfinityGauntlet) item.getItem()).changeMode(item, serverPlayer);
					}
				} else {
					ItemStack item = serverPlayer.getHeldItemMainhand();
					
					if(item.getItem() instanceof InfinityGauntlet) {
						((InfinityGauntlet) item.getItem()).changeStone(item, serverPlayer);
					}
				}
			});
		}
		
		return null;
	}

}
