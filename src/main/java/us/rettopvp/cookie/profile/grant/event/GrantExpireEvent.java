package us.rettopvp.cookie.profile.grant.event;

import org.bukkit.entity.Player;

import us.rettopvp.cookie.profile.grant.Grant;
import us.rettopvp.cookie.util.event.BaseEvent;

public class GrantExpireEvent extends BaseEvent {
	
    private Player player;
    private Grant grant;
    
    public GrantExpireEvent(Player player, Grant grant) {
        this.player = player;
        this.grant = grant;
    }
    
    public Player getPlayer() {
        return this.player;
    }
    
    public Grant getGrant() {
        return this.grant;
    }
}
