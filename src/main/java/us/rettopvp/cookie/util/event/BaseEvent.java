package us.rettopvp.cookie.util.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import us.rettopvp.cookie.CookiePlugin;

public class BaseEvent extends Event {
	
    private static final HandlerList handlers;
    
    public HandlerList getHandlers() {
        return BaseEvent.handlers;
    }
    
    public static HandlerList getHandlerList() {
        return BaseEvent.handlers;
    }
    
    public void call() {
        CookiePlugin.getPlugin().getServer().getPluginManager().callEvent((Event)this);
    }
    
    static {
        handlers = new HandlerList();
    }
}
