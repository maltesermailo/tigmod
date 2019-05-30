package de.maltesermailo.tigmod;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class TeleportMessage implements IMessage {

	public int x;
	public int y;
	public int z;
	
	public TeleportMessage(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public TeleportMessage() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		this.x = buf.readInt();
		this.y = buf.readInt();
		this.z = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(this.x);
		buf.writeInt(this.y);
		buf.writeInt(this.z);
	}
	
}
