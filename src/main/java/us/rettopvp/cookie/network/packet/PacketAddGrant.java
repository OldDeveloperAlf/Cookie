package us.rettopvp.cookie.network.packet;

import java.util.UUID;

import net.minecraft.util.com.google.gson.JsonElement;
import net.minecraft.util.com.google.gson.JsonObject;
import us.rettopvp.cookie.handle.packet.Packet;
import us.rettopvp.cookie.profile.grant.Grant;
import us.rettopvp.cookie.util.json.JsonChain;

public class PacketAddGrant implements Packet {
	
    private UUID playerUuid;
    private Grant grant;
    
    public PacketAddGrant() {
		// TODO Auto-generated constructor stub
	}
    
    public PacketAddGrant(UUID playerUuid, Grant grant) {
        this.playerUuid = playerUuid;
        this.grant = grant;
    }
    
    @Override
    public int id() {
        return 1;
    }
    
    @Override
    public JsonObject serialize() {
        return new JsonChain().addProperty("playerUuid", this.playerUuid.toString()).add("grant", (JsonElement)Grant.SERIALIZER.serialize(this.grant)).get();
    }
    
    @Override
    public void deserialize(JsonObject jsonObject) {
        this.playerUuid = UUID.fromString(jsonObject.get("playerUuid").getAsString());
        
        this.grant = Grant.DESERIALIZER.deserialize(jsonObject.get("grant").getAsJsonObject());
    }
    
    public UUID getPlayerUuid() {
        return this.playerUuid;
    }
    
    public Grant getGrant() {
        return this.grant;
    }
}
