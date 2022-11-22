package us.rettopvp.cookie.network.packet;

import net.minecraft.util.com.google.gson.JsonObject;
import us.rettopvp.cookie.handle.packet.Packet;
import us.rettopvp.cookie.util.json.JsonChain;

public class PacketStaffJoinNetwork implements Packet {
	
    private String playerName;
    
    private String serverName;
    
    public PacketStaffJoinNetwork() {
		// TODO Auto-generated constructor stub
	}
    
    public PacketStaffJoinNetwork(String playerName, String serverName) {
        this.playerName = playerName;
        
        this.serverName = serverName;
    }
    
    @Override
    public int id() {
        return 8;
    }
    
    @Override
    public JsonObject serialize() {
        return new JsonChain().addProperty("playerName", this.playerName).addProperty("serverName", this.serverName).get();
    }
    
    @Override
    public void deserialize(JsonObject jsonObject) {
        this.playerName = jsonObject.get("playerName").getAsString();
        this.serverName = jsonObject.get("serverName").getAsString();
    }
    
    public String getPlayerName() {
        return this.playerName;
    }
    
    public String getServerName() {
        return this.serverName;
    }
}
