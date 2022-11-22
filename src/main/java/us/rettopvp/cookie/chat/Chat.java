package us.rettopvp.cookie.chat;

import org.bukkit.entity.Player;

import us.rettopvp.cookie.CookiePlugin;
import us.rettopvp.cookie.chat.filter.ChatFilter;
import us.rettopvp.cookie.chat.filter.impl.ContainsFilter;
import us.rettopvp.cookie.misc.Misc;
import us.rettopvp.cookie.profile.Profile;

public class Chat extends Misc {
	
    private boolean publicChatMuted;
    
    public Chat(CookiePlugin cookiePlugin) {
        super(cookiePlugin);
        this.publicChatMuted = false;
    }
    
    public ChatAttempt attemptChatMessage(Player player, String message) {
        Profile profile = Profile.getProfiles().get(player.getUniqueId());
        
        if (profile.getActiveMute() != null) {
            return new ChatAttempt(ChatAttempt.Response.PLAYER_MUTED, profile.getActiveMute());
        }
        
        String msg = message.toLowerCase().replace("3", "e").replace("1", "i").replace("!", "i").replace("@", "a").replace("7", "t").replace("0", "o").replace("5", "s").replace("8", "b").replace("4", "a").replace("v", "u").replaceAll("\\p{Punct}|\\d", "").trim();
        
        String[] words = msg.trim().split(" ");

        ChatFilter containsFilter = new ContainsFilter(this.plugin);
        if(containsFilter.isFiltered(msg, words) && !player.hasPermission("cookie.staff.bypass")) {
        	return new ChatAttempt(ChatAttempt.Response.MESSAGE_FILTERED);
        }
        
        return new ChatAttempt(ChatAttempt.Response.ALLOWED);
    }
    
    public boolean isPublicChatMuted() {
        return this.publicChatMuted;
    }
    
    public void setPublicChatMuted(boolean muted) {
        if (this.publicChatMuted == muted) {
            return;
        }
        this.publicChatMuted = !this.publicChatMuted;
    }
}
