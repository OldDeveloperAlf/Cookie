package us.rettopvp.cookie.network.packet;

import java.util.UUID;

import net.minecraft.util.com.google.gson.JsonObject;
import us.rettopvp.cookie.handle.packet.Packet;
import us.rettopvp.cookie.util.json.JsonChain;

public class PacketDeleteRank implements Packet {
	
    private UUID uuid;
    
    public PacketDeleteRank() {
		// TODO Auto-generated constructor stub
	}
    
    @Override
    public int id() {
        return 5;
    }
    
    @Override
    public JsonObject serialize() {
        return new JsonChain().addProperty("uuid", this.uuid.toString()).get();
    }
    
    @Override
    public void deserialize(JsonObject jsonObject) {
        this.uuid = UUID.fromString(jsonObject.get("uuid").getAsString());
    }
    
    public PacketDeleteRank(UUID uuid) {
        this.uuid = uuid;
    }
    
    public UUID getUuid() {
        return this.uuid;
    }
}
