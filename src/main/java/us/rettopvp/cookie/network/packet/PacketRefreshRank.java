package us.rettopvp.cookie.network.packet;

import java.util.UUID;

import net.minecraft.util.com.google.gson.JsonObject;
import us.rettopvp.cookie.handle.packet.Packet;
import us.rettopvp.cookie.util.json.JsonChain;

public class PacketRefreshRank implements Packet {
	
    private UUID uuid;
    private String name;
    
    public PacketRefreshRank() {
		// TODO Auto-generated constructor stub
	}
    
    @Override
    public int id() {
        return 6;
    }
    
    @Override
    public JsonObject serialize() {
        return new JsonChain().addProperty("uuid", this.uuid.toString()).addProperty("name", this.name).get();
    }
    
    @Override
    public void deserialize(JsonObject jsonObject) {
        this.uuid = UUID.fromString(jsonObject.get("uuid").getAsString());
        this.name = jsonObject.get("name").getAsString();
    }
    
    public PacketRefreshRank(UUID uuid, String name) {
        this.uuid = uuid;
        
        this.name = name;
    }
    
    public UUID getUuid() {
        return this.uuid;
    }
    
    public String getName() {
        return this.name;
    }
}
