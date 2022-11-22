package us.rettopvp.cookie.network.packet;

import net.minecraft.util.com.google.gson.JsonObject;
import us.rettopvp.cookie.handle.packet.Packet;
import us.rettopvp.cookie.util.json.JsonChain;

public class PacketBroadcastRaw implements Packet {

	private String serverName;
	private String chatMessage;
	
	public PacketBroadcastRaw() {
		// TODO Auto-generated constructor stub
	}
	
	public PacketBroadcastRaw(String serverName, String chatMessage) {
		this.serverName = serverName;
		this.chatMessage = chatMessage;
	}
	
	@Override
	public int id() {
		return 9;
	}

	@Override
	public JsonObject serialize() {
		return new JsonChain()
				.addProperty("serverName", this.serverName)
				.addProperty("chatMessage", this.chatMessage)
				.get();
	}

	@Override
	public void deserialize(JsonObject object) {
		this.serverName = object.get("serverName").getAsString();
		this.chatMessage = object.get("chatMessage").getAsString();
	}
	
	public String getServerName() {
		return serverName;
	}
	
	public String getChatMessage() {
		return chatMessage;
	}
}
