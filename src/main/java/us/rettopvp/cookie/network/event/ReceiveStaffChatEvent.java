package us.rettopvp.cookie.network.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

import us.rettopvp.cookie.util.event.BaseEvent;

public class ReceiveStaffChatEvent extends BaseEvent implements Cancellable {
	
    private Player player;
    private boolean cancelled;
    
    public ReceiveStaffChatEvent(Player player) {
        this.player = player;
    }
    
    public Player getPlayer() {
        return this.player;
    }
    
    public boolean isCancelled() {
        return this.cancelled;
    }
    
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}
