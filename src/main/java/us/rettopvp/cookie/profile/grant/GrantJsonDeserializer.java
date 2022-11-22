package us.rettopvp.cookie.profile.grant;

import java.util.UUID;

import net.minecraft.util.com.google.gson.JsonObject;
import us.rettopvp.cookie.rank.Rank;
import us.rettopvp.cookie.util.json.JsonDeserializer;

public class GrantJsonDeserializer implements JsonDeserializer<Grant> {
	
    @Override
    public Grant deserialize(JsonObject object) {
        Rank rank = Rank.getRankByUuid(UUID.fromString(object.get("rank").getAsString()));
        
        Grant grant = new Grant(UUID.fromString(object.get("uuid").getAsString()), rank, null, object.get("addedAt").getAsLong(), object.get("addedReason").getAsString(), object.get("duration").getAsLong());
        
        if (!object.get("addedBy").isJsonNull()) {
            grant.setAddedBy(UUID.fromString(object.get("addedBy").getAsString()));
        }
        
        if (!object.get("removedBy").isJsonNull()) {
            grant.setRemovedBy(UUID.fromString(object.get("removedBy").getAsString()));
        }
        
        if (!object.get("removedAt").isJsonNull()) {
            grant.setRemovedAt(object.get("removedAt").getAsLong());
        }
        
        if (!object.get("removedReason").isJsonNull()) {
            grant.setRemovedReason(object.get("removedReason").getAsString());
        }
        
        if (!object.get("removed").isJsonNull()) {
            grant.setRemoved(object.get("removed").getAsBoolean());
        }
        return grant;
    }
}
