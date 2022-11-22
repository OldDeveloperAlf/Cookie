package us.rettopvp.cookie.profile.grant;

import java.util.Date;
import java.util.UUID;

import us.rettopvp.cookie.rank.Rank;
import us.rettopvp.cookie.util.TimeUtil;

public class Grant {
	
    public static GrantJsonSerializer SERIALIZER;
    public static GrantJsonDeserializer DESERIALIZER;
    private UUID uuid;
    private Rank rank;
    private UUID addedBy;
    private long addedAt;
    private String addedReason;
    private long duration;
    private UUID removedBy;
    private long removedAt;
    private String removedReason;
    private boolean removed;
    
    static {
        Grant.SERIALIZER = new GrantJsonSerializer();
        Grant.DESERIALIZER = new GrantJsonDeserializer();
    }
    
    public Grant(UUID uuid, Rank rank, UUID addedBy, long addedAt, String addedReason, long duration) {
        this.uuid = uuid;
        this.rank = rank;
        this.addedBy = addedBy;
        this.addedAt = addedAt;
        this.addedReason = addedReason;
        this.duration = duration;
    }
    
    public boolean isPermanent() {
        return this.duration == 2147483647L;
    }
    
    public boolean hasExpired() {
        return !this.isPermanent() && System.currentTimeMillis() >= this.addedAt + this.duration;
    }
    
    public String getAddedAtDate() {
        return TimeUtil.dateToString(new Date(this.addedAt));
    }
    
    public String getExpiresAtDate() {
        return (this.duration == 2147483647L) ? "Never" : TimeUtil.dateToString(new Date(this.addedAt + this.duration));
    }
    
    public String getRemovedAtDate() {
        return TimeUtil.dateToString(new Date(this.addedAt));
    }
    
    public UUID getUuid() {
        return this.uuid;
    }
    
    public Rank getRank() {
        return this.rank;
    }
    
    public UUID getAddedBy() {
        return this.addedBy;
    }
    
    public void setAddedBy(UUID addedBy) {
        this.addedBy = addedBy;
    }
    
    public long getAddedAt() {
        return this.addedAt;
    }
    
    public String getAddedReason() {
        return this.addedReason;
    }
    
    public long getDuration() {
        return this.duration;
    }
    
    public UUID getRemovedBy() {
        return this.removedBy;
    }
    
    public void setRemovedBy(UUID removedBy) {
        this.removedBy = removedBy;
    }
    
    public long getRemovedAt() {
        return this.removedAt;
    }
    
    public void setRemovedAt(long removedAt) {
        this.removedAt = removedAt;
    }
    
    public String getRemovedReason() {
        return this.removedReason;
    }
    
    public void setRemovedReason(String removedReason) {
        this.removedReason = removedReason;
    }
    
    public boolean isRemoved() {
        return this.removed;
    }
    
    public void setRemoved(boolean removed) {
        this.removed = removed;
    }
}
