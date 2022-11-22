package us.rettopvp.cookie.profile.punishment;

import net.minecraft.util.com.google.gson.JsonObject;
import us.rettopvp.cookie.util.json.JsonSerializer;

public class PunishmentJsonSerializer implements JsonSerializer<Punishment> {
	
    @Override
    public JsonObject serialize(Punishment punishment) {
        JsonObject object = new JsonObject();
        
        object.addProperty("uuid", punishment.getUuid().toString());
        
        object.addProperty("type", punishment.getType().name());
        
        object.addProperty("addedBy", (punishment.getAddedBy() == null) ? null : punishment.getAddedBy().toString());
        
        object.addProperty("addedAt", punishment.getAddedAt());
        
        object.addProperty("addedReason", punishment.getAddedReason());
        
        object.addProperty("duration", punishment.getDuration());
        
        object.addProperty("pardonedBy", (punishment.getPardonedBy() == null) ? null : punishment.getPardonedBy().toString());
        
        object.addProperty("pardonedAt", punishment.getPardonedAt());
        
        object.addProperty("pardonedReason", punishment.getPardonedReason());
        
        object.addProperty("pardoned", Boolean.valueOf(punishment.isPardoned()));
        return object;
    }
}
