package us.rettopvp.cookie.util.json;

import net.minecraft.util.com.google.gson.JsonObject;

public interface JsonSerializer<T> {
	
    JsonObject serialize(final T t);
}
