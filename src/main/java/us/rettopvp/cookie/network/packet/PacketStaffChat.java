package us.rettopvp.cookie.network.packet;

import net.minecraft.util.com.google.gson.JsonObject;
import us.rettopvp.cookie.handle.packet.Packet;
import us.rettopvp.cookie.util.json.JsonChain;

public class PacketStaffChat implements Packet {
	
    private String playerName;
    private String serverName;
    private String chatMessage;
    
    public PacketStaffChat() {
		// TODO Auto-generated constructor stub
	}
    
    public PacketStaffChat(String playerName, String serverName, String chatMessage) {
        this.playerName = playerName;
        this.serverName = serverName;
        this.chatMessage = chatMessage;
    }
    
    @Override
    public int id() {
        return 7;
    }
    
    @Override
    public JsonObject serialize() {
        return new JsonChain()
        		.addProperty("playerName", this.playerName)
        		.addProperty("serverName", this.serverName)
        		.addProperty("chatMessage", this.chatMessage)
        		.get();
    }
    
    @Override
    public void deserialize(JsonObject jsonObject) {
        this.playerName = jsonObject.get("playerName").getAsString();
        this.serverName = jsonObject.get("serverName").getAsString();
        this.chatMessage = jsonObject.get("chatMessage").getAsString();
    }
    
    public String getPlayerName() {
        return this.playerName;
    }
    
    public String getServerName() {
        return this.serverName;
    }
    
    public String getChatMessage() {
        return this.chatMessage;
    }
    
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }
    
    public void setServerName(String serverName) {
        this.serverName = serverName;
    }
    
    public void setChatMessage(String chatMessage) {
        this.chatMessage = chatMessage;
    }
}
