package us.rettopvp.cookie.handle.packet;

import net.minecraft.util.com.google.gson.JsonObject;

public interface Packet {
	
    int id();
    
    JsonObject serialize();
    
    void deserialize(JsonObject object);
}
