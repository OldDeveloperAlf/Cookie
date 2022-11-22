package us.rettopvp.cookie.rank;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;

import net.minecraft.util.com.google.gson.JsonElement;
import net.minecraft.util.com.google.gson.JsonParser;
import us.rettopvp.cookie.CookiePlugin;
import us.rettopvp.cookie.network.packet.PacketDeleteRank;
import us.rettopvp.cookie.network.packet.PacketRefreshRank;
import us.rettopvp.cookie.profile.Profile;
import us.rettopvp.cookie.util.message.color.CC;

public class Rank {
	
    private static Map<UUID, Rank> ranks;
    private static MongoCollection<Document> collection;
    private UUID uuid;
    private String displayName;
    private String prefix;
    private ChatColor color;
    private int weight;
    private boolean defaultRank;
    private List<String> permissions;
    private List<Rank> inherited;

    static {
        Rank.ranks = new HashMap<UUID, Rank>();
    }
    
    public Rank(String displayName) {
        this.prefix = "";
        this.color = ChatColor.WHITE;
        this.permissions = new ArrayList<String>();
        this.inherited = new ArrayList<Rank>();
        this.uuid = UUID.randomUUID();
        this.displayName = displayName;
        Rank.ranks.put(this.uuid, this);
    }
    
    public Rank(UUID uuid, String displayName) {
        this.prefix = "";
        this.color = ChatColor.WHITE;
        this.permissions = new ArrayList<String>();
        this.inherited = new ArrayList<Rank>();
        this.uuid = uuid;
        this.displayName = displayName;
    }
    
    public Rank(UUID uuid, String displayName, String prefix, ChatColor color, int weight, boolean defaultRank) {
        this.prefix = "";
        this.color = ChatColor.WHITE;
        this.permissions = new ArrayList<String>();
        this.inherited = new ArrayList<Rank>();
        this.uuid = uuid;
        this.displayName = displayName;
        this.prefix = prefix;
        this.color = color;
        this.weight = weight;
        
        this.defaultRank = defaultRank;
        Rank.ranks.put(uuid, this);
    }
    
    public boolean isDefaultRank() {
        return this.defaultRank;
    }
    
    public boolean addPermission(String permission) {
        if (!this.permissions.contains(permission)) {
            this.permissions.add(permission);
            return true;
        }
        return false;
    }
    
    public boolean deletePermission(String permission) {
        return this.permissions.remove(permission);
    }
    
    public boolean hasPermission(String permission) {
        if (this.permissions.contains(permission)) {
            return true;
        }
        for (Rank rank : this.inherited) {
            if (rank.hasPermission(permission)) {
                return true;
            }
        }
        return false;
    }
    
    public boolean canInherit(Rank rankToCheck) {
        if (this.inherited.contains(rankToCheck) || rankToCheck.inherited.contains(this)) {
            return false;
        }
        for (Rank rank : this.inherited) {
            if (!rank.canInherit(rankToCheck)) {
                return false;
            }
        }
        return true;
    }
    
    public List<String> getAllPermissions() {
        List<String> permissions = new ArrayList<String>();
        permissions.addAll(this.permissions);
        for (Rank rank : this.inherited) {
            permissions.addAll(rank.getAllPermissions());
        }
        return permissions;
    }
    
    public void load() {
        this.load(Rank.collection.find(Filters.eq("uuid", this.uuid.toString())).first());
    }
    
    public void load(Document document) {
        if (document == null) {
            return;
        }
        this.prefix = ChatColor.translateAlternateColorCodes('&', document.getString("prefix"));
        this.color = ChatColor.valueOf(document.getString("color"));
        this.weight = document.getInteger("weight");
        this.defaultRank = document.getBoolean("defaultRank");
    }
    
    public void save() {
        Document document = new Document();
        document.put("uuid", (Object)this.uuid.toString());
        document.put("displayName", (Object)this.displayName);
        document.put("prefix", (Object)this.prefix.replace(String.valueOf('§'), "&"));
        document.put("color", (Object)this.color.name());
        document.put("weight", (Object)this.weight);
        document.put("defaultRank", (Object)this.defaultRank);
        document.put("permissions", (Object)CookiePlugin.GSON.toJson(this.permissions));
        document.put("inherits", (Object)CookiePlugin.GSON.toJson(this.inherited.stream().map(Rank::getUuid).map(UUID::toString).collect(Collectors.toList())));
        Rank.collection.replaceOne(Filters.eq("uuid", this.uuid.toString()), document, new ReplaceOptions().upsert(true));
        CookiePlugin.getPlugin().getHandle().sendPacket(new PacketRefreshRank(this.uuid, this.displayName));
    }
    
    public void delete() {
        Rank.ranks.remove(this.uuid);
        Rank.collection.deleteOne(Filters.eq("uuid", this.uuid.toString()));
        CookiePlugin.getPlugin().getHandle().sendPacket(new PacketDeleteRank(this.uuid));
    }
    
    public static void init() {
        Rank.collection = CookiePlugin.getPlugin().getMongoDatabase().getCollection("ranks");
        Map<Rank, List<UUID>> inheritanceReferences = new HashMap<Rank, List<UUID>>();
        try (MongoCursor<Document> cursor = Rank.collection.find().iterator()) {
            while (cursor.hasNext()) {
                Document document = cursor.next();
                
                Rank rank = new Rank(UUID.fromString(document.getString("uuid")), document.getString("displayName"));
                
                rank.load(document);
                
                ((List)CookiePlugin.GSON.fromJson(document.getString((Object)"permissions"), CookiePlugin.LIST_STRING_TYPE)).forEach(perm -> rank.getPermissions().add((String) perm));
                
                List<UUID> ranksToInherit = new ArrayList<UUID>();
                
                for (JsonElement element : new JsonParser().parse(document.getString("inherits")).getAsJsonArray()) {
                    ranksToInherit.add(UUID.fromString(element.getAsString()));
                }
                
                inheritanceReferences.put(rank, ranksToInherit);
                
                Rank.ranks.put(rank.getUuid(), rank);
            }
        }
        inheritanceReferences.forEach((rank, list) -> list.forEach(uuid -> {
        	Rank inherited = Rank.ranks.get(uuid);
            
        	if (inherited != null) {
                rank.getInherited().add(inherited);
            }
        }));
        getDefaultRank();
    }
    
    public String getColoredName() {
    	return CC.translate(this.color + this.displayName);
    }
    
    public static Rank getRankByUuid(UUID uuid) {
        return Rank.ranks.get(uuid);
    }
    
    public static Rank getRankByDisplayName(String name) {
        for (Rank rank : Rank.ranks.values()) {
            if (rank.getDisplayName().equalsIgnoreCase(name)) {
                return rank;
            }
        }
        return null;
    }
    
    public static Rank getDefaultRank() {
        for (Rank rank : Rank.ranks.values()) {
            if (rank.isDefaultRank()) {
                return rank;
            }
        }
        Rank defaultRank = new Rank("Default");
        defaultRank.setDefaultRank(true);
        defaultRank.save();
        Rank.ranks.put(defaultRank.getUuid(), defaultRank);
        return defaultRank;
    }
    
    public static Map<UUID, Rank> getRanks() {
        return Rank.ranks;
    }
    
    public UUID getUuid() {
        return this.uuid;
    }
    
    public String getDisplayName() {
        return this.displayName;
    }
    
    public String getPrefix() {
        return this.prefix;
    }
    
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
    
    public ChatColor getColor() {
        return this.color;
    }
    
    public void setColor(ChatColor color) {
        this.color = color;
        for(Player player : Bukkit.getOnlinePlayers()) {
        	Profile profile = Profile.getByUuid(player.getUniqueId());
			if (profile.getActiveRank().equals(this)) {
				profile.refreshDisplayName();
			}
        }
    }
    
    public int getWeight() {
        return this.weight;
    }
    
    public void setWeight(int weight) {
        this.weight = weight;
    }
    
    public void setDefaultRank(boolean defaultRank) {
        this.defaultRank = defaultRank;
    }
    
    public List<String> getPermissions() {
        return this.permissions;
    }
    
    public List<Rank> getInherited() {
        return this.inherited;
    }
}
