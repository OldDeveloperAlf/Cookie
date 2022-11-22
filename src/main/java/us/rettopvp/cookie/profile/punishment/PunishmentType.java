package us.rettopvp.cookie.profile.punishment;

import org.bukkit.ChatColor;

public enum PunishmentType {
	
    BLACKLIST("blacklisted", "unblacklisted", true, true, new PunishmentTypeData("Blacklists", ChatColor.DARK_RED, 14)), 
    BAN("banned", "unbanned", true, true, new PunishmentTypeData("Bans", ChatColor.GOLD, 1)), 
    MUTE("muted", "unmuted", false, true, new PunishmentTypeData("Mutes", ChatColor.YELLOW, 4)), 
    WARN("warned", (String) null, false, false, new PunishmentTypeData("Warnings", ChatColor.GREEN, 13)), 
    KICK("kicked", (String) null, false, false, new PunishmentTypeData("Kicks", ChatColor.GRAY, 7));
    
    private String context;
    private String undoContext;
    private boolean ban;
    private boolean canBePardoned;
    private PunishmentTypeData typeData;
    
    public String getContext() {
        return this.context;
    }
    
    public String getUndoContext() {
        return this.undoContext;
    }
    
    public boolean isBan() {
        return this.ban;
    }
    
    public boolean isCanBePardoned() {
        return this.canBePardoned;
    }
    
    public PunishmentTypeData getTypeData() {
        return this.typeData;
    }
    
    private PunishmentType(String context, String undoContext, boolean ban, boolean canBePardoned, PunishmentTypeData typeData) {
        this.context = context;
        this.undoContext = undoContext;
        this.ban = ban;
        this.canBePardoned = canBePardoned;
        this.typeData = typeData;
    }
    
    public static class PunishmentTypeData {
    	
        private String readable;
        private ChatColor color;
        private int durability;
        
        public PunishmentTypeData(String readable, ChatColor color, int durability) {
            this.readable = readable;
            this.color = color;
            this.durability = durability;
        }
        
        public String getReadable() {
            return this.readable;
        }
        
        public ChatColor getColor() {
            return this.color;
        }
        
        public int getDurability() {
            return this.durability;
        }
    }
}
