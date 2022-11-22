package us.rettopvp.cookie.util.json;

import net.minecraft.util.com.google.gson.JsonObject;

public interface JsonDeserializer<T> {
	
    T deserialize(final JsonObject object);
}
