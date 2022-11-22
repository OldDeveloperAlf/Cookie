package us.rettopvp.cookie.profile;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;

import com.mongodb.Block;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;

import net.minecraft.util.com.google.gson.JsonArray;
import net.minecraft.util.com.google.gson.JsonElement;
import net.minecraft.util.com.google.gson.JsonObject;
import net.minecraft.util.com.google.gson.JsonParser;
import us.rettopvp.cookie.CookiePlugin;
import us.rettopvp.cookie.cosmetic.CosmeticColor;
import us.rettopvp.cookie.cosmetic.CosmeticTag;
import us.rettopvp.cookie.profile.grant.Grant;
import us.rettopvp.cookie.profile.grant.event.GrantAppliedEvent;
import us.rettopvp.cookie.profile.grant.event.GrantExpireEvent;
import us.rettopvp.cookie.profile.punishment.Punishment;
import us.rettopvp.cookie.profile.punishment.PunishmentType;
import us.rettopvp.cookie.rank.Rank;
import us.rettopvp.cookie.util.message.color.CC;

public class Profile {
	
	/* Cached */
	private static Map<UUID, Profile> profiles;
    
	/* Document */
	private static MongoCollection<Document> collection;
    
	/* General */
	private String username;
    private UUID uuid;
    
    private String currentAddress;
    private List<String> ipAddresses;
    
    private String nickName;
    
    /* Grants */
    private Grant activeGrant;
    private List<Grant> grants;
    
    /* Punishments*/
    private List<Punishment> punishments;
    private List<UUID> knownAlts;
    
    /* Session */
    private Long firstSeen;
    private Long lastSeen;
    private boolean loaded;
    
    /* Cosmetic */
    private CosmeticColor color;
    private CosmeticTag tag;
    private CosmeticColor tagColor;
        
    static {
        Profile.profiles = new HashMap<UUID, Profile>();
    }
    
    public Profile(String username, UUID uuid) {
    	this.username = username;
        this.uuid = uuid;
        
        this.ipAddresses = new ArrayList<String>();
        this.knownAlts = new ArrayList<UUID>(); 
        this.grants = new ArrayList<Grant>();
        this.punishments = new ArrayList<Punishment>();
        this.load();
    }
    
    public Player getPlayer() {
        return Bukkit.getPlayer(this.uuid);
    }
    
    public String getColoredUsername() {
        return this.activeGrant.getRank().getColor() + (this.nickName == null ? this.username : this.getNickName());
    }
    
    public void refreshDisplayName() {
        Player player = this.getPlayer();
        if (player != null) {
            if (player.hasPermission("cookie.cosmetic.color") && this.color != null) {
                player.setDisplayName(CC.translate(this.color.getDisplay() + (this.nickName == null ? player.getName() : this.getNickName())));
            }
            else if (this.getActiveRank() == null) {
                player.setDisplayName(CC.translate((this.nickName == null ? player.getName() : this.getNickName())));
            } else {
                player.setDisplayName(CC.translate(this.getActiveGrant().getRank().getPrefix() + (this.nickName == null ? player.getName() : this.getNickName())));
            }
        }
    }
    
    public Punishment getActiveMute() {
        for (Punishment punishment : this.punishments) {
            if (punishment.getType() == PunishmentType.MUTE && punishment.isActive()) {
                return punishment;
            }
        }
        return null;
    }
    
    public Punishment getActiveBan() {
        for (Punishment punishment : this.punishments) {
            if (punishment.getType().isBan() && punishment.isActive()) {
                return punishment;
            }
        }
        return null;
    }
    
    public int getPunishmentCountByType(PunishmentType type) {
        int i = 0;
        for (Punishment punishment : this.punishments) {
            if (punishment.getType() == type) {
                ++i;
            }
        }
        return i;
    }
    
    public Rank getActiveRank() {
        return this.activeGrant.getRank();
    }
    
    public void setActiveGrant(Grant grant) {
        this.activeGrant = grant;
        Player player = this.getPlayer();
        if (player != null) {
            player.setDisplayName(grant.getRank().getPrefix() + (this.nickName == null ? player.getName() : this.getNickName()));
        }
    }
    
    public void activateNextGrant() {
        List<Grant> grants = new ArrayList<Grant>(this.grants);
        grants.sort(Comparator.comparingInt(grant -> grant.getRank().getWeight()));
        for (Grant grant : grants) {
            if (!grant.isRemoved() && !grant.hasExpired()) {
                this.setActiveGrant(grant);
            }
        }
    }
    
    public void checkGrants() {
        for (Grant grant : this.grants) {
            if (!grant.isRemoved() && grant.hasExpired()) {
                grant.setRemovedAt(System.currentTimeMillis());
                grant.setRemovedReason("Grant expired");
                grant.setRemoved(true);
                if (this.activeGrant != null && this.activeGrant.equals(grant)) {
                    this.activeGrant = null;
                }
                Player player = this.getPlayer();
                if (player == null) {
                    continue;
                }
                new GrantExpireEvent(player, grant).call();
            }
        }
        if (this.activeGrant == null) {
            this.activateNextGrant();
            if (this.activeGrant != null) {
                return;
            }
            Grant grant = new Grant(UUID.randomUUID(), Rank.getDefaultRank(), null, System.currentTimeMillis(), "Default", 2147483647L);
            this.grants.add(grant);
            this.setActiveGrant(grant);
            Player player = this.getPlayer();
            if (player != null) {
                new GrantAppliedEvent(player, grant).call();
            }
        }
    }
    
    public void setupPermissionsAttachment(CookiePlugin cookiePlugin, Player player) {
    	for (PermissionAttachmentInfo attachmentInfo : player.getEffectivePermissions()) {
            if (attachmentInfo.getAttachment() == null) {
                continue;
            }
            attachmentInfo.getAttachment().getPermissions().forEach((permission, value) -> attachmentInfo.getAttachment().unsetPermission(permission));
        }
        PermissionAttachment attachment = player.addAttachment(cookiePlugin);
        for (String perm : this.activeGrant.getRank().getAllPermissions()) {
            attachment.setPermission(perm, true);
        }
        player.recalculatePermissions();
    }
    
    /**
	 * Load the player's profile.
	 */
    public void load() {
        Document document = Profile.collection.find(Filters.eq("uuid", this.uuid.toString())).first();
        if (document != null) {
            if (this.username == null) {
                this.username = document.getString("username");
            }
            this.firstSeen = document.getLong("firstSeen");
            this.lastSeen = document.getLong("lastSeen");
            this.currentAddress = document.getString("currentAddress");
            this.ipAddresses = CookiePlugin.GSON.fromJson(document.getString("ipAddresses"), CookiePlugin.LIST_STRING_TYPE);
            this.nickName = document.getString("nickName");
            if(document.containsKey("cosmetic")) {
            	Document cosmetic = (Document) document.get("cosmetic");
            	if(cosmetic.containsKey("color")) {
            		this.color = CosmeticColor.valueOf(cosmetic.getString("color"));
            	}
            	if(cosmetic.containsKey("tag")) {
            		this.tag = CosmeticTag.valueOf(cosmetic.getString("tag"));
            	}
            	if(cosmetic.containsKey("tag_color")) {
            		this.tagColor = CosmeticColor.valueOf(cosmetic.getString("tag_color"));
            	}
            }
            JsonArray grants = new JsonParser().parse(document.getString("grants")).getAsJsonArray();
            for (JsonElement jsonElement : grants) {
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                Rank rank = Rank.getRankByUuid(UUID.fromString(jsonObject.get("rank").getAsString()));
                if (rank != null) {
                    this.grants.add(Grant.DESERIALIZER.deserialize(jsonObject));
                }
            }
            JsonArray punishments = new JsonParser().parse(document.getString("punishments")).getAsJsonArray();
            for (JsonElement json : punishments) {
                JsonObject object = json.getAsJsonObject();
                this.punishments.add(Punishment.DESERIALIZER.deserialize(object));
            }
        }
        this.checkGrants();
        this.loaded = true;
    }
    
    /**
	 * Saves the player's profile.
	 */
    public void save() {
        Document document = new Document();
        document.put("username", (Object)this.username);
        document.put("uuid", (Object)this.uuid.toString());
        document.put("firstSeen", (Object)this.firstSeen);
        document.put("lastSeen", (Object)this.lastSeen);
        document.put("currentAddress", (Object)this.currentAddress);
        
        document.put("ipAddresses", (Object)CookiePlugin.GSON.toJson(this.ipAddresses, CookiePlugin.LIST_STRING_TYPE));
        List<String> addressesList = new ArrayList<String>();
        for(String string : document.get("ipAddresses").toString().replace("[", "").replace("]", "").replace(" ", "").split(",")) {
        	if(!string.isEmpty()) {
        		addressesList.add(string);
        	}
        }
        this.ipAddresses.addAll(addressesList);
        
        if(!this.ipAddresses.isEmpty()) {
        	for(String ipaddress : this.ipAddresses) {
        		Profile.collection.find(Filters.eq("currentAddress", ipaddress)).forEach(new Block() {
        			
        			@Override
        			public void apply(Object object) {
        				Document doc = (Document)object;
        				Profile.this.knownAlts.add(UUID.fromString(doc.getString("uuid")));
        			}
				});
        	}
        }
        
        document.put("nickName", this.nickName);
        JsonArray grants = new JsonArray();
        for (Grant grant : this.grants) {
            grants.add(Grant.SERIALIZER.serialize(grant));
        }
        Document cosmetic = new Document();
        if(this.color != null) {
        	cosmetic.put("color", this.color.name());
        }
        if(this.tag != null) {
        	cosmetic.put("tag", this.tag.name());
        }
        if(this.tagColor != null) {
        	cosmetic.put("tag_color", this.tagColor.name());
        }
        document.put("grants", (Object)grants.toString());
        JsonArray punishments = new JsonArray();
        for (Punishment punishment : this.punishments) {
            punishments.add(Punishment.SERIALIZER.serialize(punishment));
        }
        document.put("punishments", punishments.toString());
        document.put("cosmetic", cosmetic);
        Profile.collection.replaceOne(Filters.eq("uuid", this.uuid.toString()), document, new ReplaceOptions().upsert(true));
    }
    
    public static void init() {
        Profile.collection = CookiePlugin.getPlugin().getMongoDatabase().getCollection("profiles");
    }
    
    /**
	 * Retrieves a cached instance of Profile or creates and returns a new instance.
	 *
	 * @param uuid the player identifier
	 *
	 * @return the player's Profile instance
	 * */
    public static Profile getByUuid(UUID uuid) {
        if (Profile.profiles.containsKey(uuid)) {
            return Profile.profiles.get(uuid);
        }
        return new Profile(null, uuid);
    }
    
    /**
	 * This method should only be called asynchronously as it could fetch results from Redis.
	 *
	 * @param name the name
	 *
	 * @return A Profile instance if results were fetched
	 * */
    public static Profile getByUsername(String username) {
        Player player = Bukkit.getPlayer(username);
        if (player != null) {
            return Profile.profiles.get(player.getUniqueId());
        }
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(username);
        if (offlinePlayer.hasPlayedBefore()) {
            if (Profile.profiles.containsKey(offlinePlayer.getUniqueId())) {
                return Profile.profiles.get(offlinePlayer.getUniqueId());
            }
            return new Profile(offlinePlayer.getName(), offlinePlayer.getUniqueId());
        } else {
            UUID uuid = CookiePlugin.getPlugin().getUuidCache().getUuid(username);
            if (uuid == null) {
                return null;
            }
            if (Profile.profiles.containsKey(uuid)) {
                return Profile.profiles.get(uuid);
            }
            return new Profile(username, uuid);
        }
    }
    
    /**
     * Get the IP address of the players.
     * 
     * @param address the address
     * 
     * @return A Address IP, if results were fetched
     * */
    public static List<Profile> getByIpAddress(String ipAddress) {
        List<Profile> profiles = new ArrayList<Profile>();
        try (MongoCursor<Document> cursor = Profile.collection.find(Filters.eq("currentAddress", ipAddress)).iterator()) {
            while (cursor.hasNext()) {
                Document document = cursor.next();
                profiles.add(new Profile(document.getString("username"), UUID.fromString(document.getString("uuid"))));
            }
        }
        return profiles;
    }
    
    public static Map<UUID, Profile> getProfiles() {
        return Profile.profiles;
    }
    
    public String getUsername() {
        return this.username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public UUID getUuid() {
        return this.uuid;
    }
    
    public Long getFirstSeen() {
        return this.firstSeen;
    }
    
    public void setFirstSeen(Long firstSeen) {
        this.firstSeen = firstSeen;
    }
    
    public Long getLastSeen() {
        return this.lastSeen;
    }
    
    public void setLastSeen(Long lastSeen) {
        this.lastSeen = lastSeen;
    }
    
    public String getCurrentAddress() {
        return this.currentAddress;
    }
    
    public void setCurrentAddress(String currentAddress) {
        this.currentAddress = currentAddress;
    }
    
    public List<String> getIpAddresses() {
        return this.ipAddresses;
    }
    
    public List<UUID> getKnownAlts() {
        return this.knownAlts;
    }
    
    public Grant getActiveGrant() {
        return this.activeGrant;
    }
    
    public List<Grant> getGrants() {
        return this.grants;
    }
    
    public List<Punishment> getPunishments() {
        return this.punishments;
    }
    
    public boolean isLoaded() {
        return this.loaded;
    }
    
    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }
    
    public CosmeticColor getColor() {
		return color;
	}
    
    public void setColor(CosmeticColor color) {
        this.color = color;
        if (this.color != null) {
            this.refreshDisplayName();
        }
    }
    
    public CosmeticTag getTag() {
		return tag;
	}
    
    public void setTag(CosmeticTag tag) {
		this.tag = tag;
	}
    
    public CosmeticColor getTagColor() {
		return tagColor;
	}
    
    public void setTagColor(CosmeticColor tagColor) {
		this.tagColor = tagColor;
	}
    
    public String getNickName() {
		return nickName;
	}
    
    public void setNickName(String nickName) {
    	this.nickName = nickName;
	}
}
