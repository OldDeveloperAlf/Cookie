package us.rettopvp.cookie.network.packet;

import net.minecraft.util.com.google.gson.JsonObject;
import us.rettopvp.cookie.handle.packet.Packet;
import us.rettopvp.cookie.util.json.JsonChain;

public class PacketRequestPlayer implements Packet {
	
    private String senderName;
    private String serverName;
    private String reason;
    
    public PacketRequestPlayer() {
		// TODO Auto-generated constructor stub
	}
    
    public PacketRequestPlayer(String senderName, String targetName, String serverName, String reason) {
        this.senderName = senderName;
        this.serverName = serverName;
        this.reason = reason;
    }
    
    @Override
    public int id() {
        return 10;
    }
    
    @Override
    public JsonObject serialize() {
        return new JsonChain()
        		.addProperty("senderName", this.senderName)
        		.addProperty("serverName", this.serverName)
        		.addProperty("reason", this.reason)
        		.get();
    }
    
    @Override
    public void deserialize(JsonObject jsonObject) {
        this.senderName = jsonObject.get("playerName").getAsString();
        this.serverName = jsonObject.get("serverName").getAsString();
        this.reason = jsonObject.get("reason").getAsString();
    }
    
    public String getSenderName() {
		return senderName;
	}
    
    public String getServerName() {
        return this.serverName;
    }
    
    public String getReason() {
		return reason;
	}
    
    public void setSenderName(String senderName) {
		this.senderName = senderName;
	}
    
    public void setServerName(String serverName) {
        this.serverName = serverName;
    }
    
    public void setReason(String reason) {
		this.reason = reason;
	}
}
