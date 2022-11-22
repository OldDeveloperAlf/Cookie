package us.rettopvp.cookie.chat.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

import us.rettopvp.cookie.chat.ChatAttempt;
import us.rettopvp.cookie.util.event.BaseEvent;

public class ChatAttemptEvent extends BaseEvent implements Cancellable {
	
    private Player player;
    private ChatAttempt chatAttempt;
    private String chatMessage;
    private boolean cancelled;
    private String cancelReason;
    
    public ChatAttemptEvent(Player player, ChatAttempt chatAttempt, String chatMessage) {
        this.cancelReason = "";
        this.player = player;
        this.chatAttempt = chatAttempt;
        this.chatMessage = chatMessage;
    }
    
    public Player getPlayer() {
        return this.player;
    }
    
    public ChatAttempt getChatAttempt() {
        return this.chatAttempt;
    }
    
    public String getChatMessage() {
        return this.chatMessage;
    }
    
    public boolean isCancelled() {
        return this.cancelled;
    }
    
    public String getCancelReason() {
        return this.cancelReason;
    }
    
    public void setChatMessage(String chatMessage) {
        this.chatMessage = chatMessage;
    }
    
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
    
    public void setCancelReason(String cancelReason) {
        this.cancelReason = cancelReason;
    }
}
