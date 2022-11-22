package us.rettopvp.cookie.util;

public class CooldownUtil {
	
    private long start;
    private long expire;
    private boolean notified;
    
    public CooldownUtil(long duration) {
        this.start = System.currentTimeMillis();
        this.expire = this.start + duration;
        if (duration == 0L) {
            this.notified = true;
        }
    }
    
    public long getPassed() {
        return System.currentTimeMillis() - this.start;
    }
    
    public long getRemaining() {
        return this.expire - System.currentTimeMillis();
    }
    
    public boolean hasExpired() {
        return System.currentTimeMillis() - this.expire >= 0L;
    }
    
    public String getTimeLeft() {
        if (this.getRemaining() >= 60000L) {
            return TimeUtil.millisToRoundedTime(this.getRemaining());
        }
        return TimeUtil.millisToSeconds(this.getRemaining());
    }
    
    public long getStart() {
        return this.start;
    }
    
    public long getExpire() {
        return this.expire;
    }
    
    public boolean isNotified() {
        return this.notified;
    }
    
    public void setStart(long start) {
        this.start = start;
    }
    
    public void setExpire(long expire) {
        this.expire = expire;
    }
    
    public void setNotified(boolean notified) {
        this.notified = notified;
    }
}
