package de.maltesermailo.tigmod;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class ChangeModeMessage implements IMessage {
	
	public int modeId = 0;
	
	public ChangeModeMessage(int modeId) {
		this.modeId = modeId;
	}
	
	public ChangeModeMessage() {
		
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.modeId = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(this.modeId);
	}

}
