package us.rettopvp.cookie.network.packet;

import net.minecraft.util.com.google.gson.JsonObject;
import us.rettopvp.cookie.handle.packet.Packet;
import us.rettopvp.cookie.util.json.JsonChain;

public class PacketReportPlayer implements Packet {
	
    private String senderName;
    private String targetName;
    private String serverName;
    private String reason;
    
    public PacketReportPlayer() {
		// TODO Auto-generated constructor stub
	}
    
    public PacketReportPlayer(String senderName, String targetName, String serverName, String reason) {
        this.senderName = senderName;
        this.targetName = targetName;
        this.serverName = serverName;
        this.reason = reason;
    }
    
    @Override
    public int id() {
        return 9;
    }
    
    @Override
    public JsonObject serialize() {
        return new JsonChain()
        		.addProperty("senderName", this.senderName)
        		.addProperty("targetName", this.targetName)
        		.addProperty("serverName", this.serverName)
        		.addProperty("reason", this.reason)
        		.get();
    }
    
    @Override
    public void deserialize(JsonObject jsonObject) {
        this.senderName = jsonObject.get("playerName").getAsString();
        this.targetName = jsonObject.get("targetName").getAsString();
        this.serverName = jsonObject.get("serverName").getAsString();
        this.reason = jsonObject.get("reason").getAsString();
    }
    
    public String getSenderName() {
		return senderName;
	}
    
    public String getTargetName() {
		return targetName;
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
    
    public void setTargetName(String targetName) {
		this.targetName = targetName;
	}
    
    public void setServerName(String serverName) {
        this.serverName = serverName;
    }
    
    public void setReason(String reason) {
		this.reason = reason;
	}
}
