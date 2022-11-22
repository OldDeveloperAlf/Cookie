package us.rettopvp.cookie.util.json;

import net.minecraft.util.com.google.gson.JsonElement;
import net.minecraft.util.com.google.gson.JsonObject;

public class JsonChain {
	
    private JsonObject json;
    
    public JsonChain() {
        this.json = new JsonObject();
    }
    
    public JsonChain addProperty(String property, String value) {
        this.json.addProperty(property, value);
        return this;
    }
    
    public JsonChain addProperty(String property, Number value) {
        this.json.addProperty(property, value);
        return this;
    }
    
    public JsonChain addProperty(String property, Boolean value) {
        this.json.addProperty(property, value);
        return this;
    }
    
    public JsonChain addProperty(String property, Character value) {
        this.json.addProperty(property, value);
        return this;
    }
    
    public JsonChain add(String property, JsonElement element) {
        this.json.add(property, element);
        return this;
    }
    
    public JsonObject get() {
        return this.json;
    }
}
