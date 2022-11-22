package us.rettopvp.cookie.network.packet;

import java.util.UUID;

import net.minecraft.util.com.google.gson.JsonObject;
import us.rettopvp.cookie.handle.packet.Packet;
import us.rettopvp.cookie.profile.punishment.Punishment;
import us.rettopvp.cookie.util.json.JsonChain;

public class PacketBroadcastPunishment implements Packet {
	
    private Punishment punishment;
    private String staff;
    private String target;
    private UUID targetUuid;
    private boolean silent;
    
    public PacketBroadcastPunishment() {
		// TODO Auto-generated constructor stub
	}
    
    public PacketBroadcastPunishment(Punishment punishment, String staff, String target, UUID targetUuid, boolean silent) {
        this.punishment = punishment;
        this.staff = staff;
        this.target = target;
        this.targetUuid = targetUuid;
        this.silent = silent;
    }
    
    @Override
    public int id() {
        return 3;
    }
    
    @Override
    public JsonObject serialize() {
        return new JsonChain().add("punishment", Punishment.SERIALIZER.serialize(this.punishment)).addProperty("staff", this.staff).addProperty("target", this.target).addProperty("targetUuid", this.targetUuid.toString()).addProperty("silent", Boolean.valueOf(this.silent)).get();
    }
    
    @Override
    public void deserialize(JsonObject object) {
        this.punishment = Punishment.DESERIALIZER.deserialize(object.get("punishment").getAsJsonObject());
        this.staff = object.get("staff").getAsString();
        this.target = object.get("target").getAsString();
        this.targetUuid = UUID.fromString(object.get("targetUuid").getAsString());
        this.silent = object.get("silent").getAsBoolean();
    }
    
    public Punishment getPunishment() {
        return this.punishment;
    }
    
    public String getStaff() {
        return this.staff;
    }
    
    public String getTarget() {
        return this.target;
    }
    
    public UUID getTargetUuid() {
        return this.targetUuid;
    }
    
    public boolean isSilent() {
        return this.silent;
    }
}
