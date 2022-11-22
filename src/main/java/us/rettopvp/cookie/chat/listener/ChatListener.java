package us.rettopvp.cookie.chat.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import us.rettopvp.cookie.CookiePlugin;
import us.rettopvp.cookie.chat.ChatAttempt;
import us.rettopvp.cookie.chat.event.ChatAttemptEvent;
import us.rettopvp.cookie.misc.MiscListener;
import us.rettopvp.cookie.profile.Profile;
import us.rettopvp.cookie.util.message.Messager;

public class ChatListener extends MiscListener {
	
    public ChatListener(CookiePlugin cookiePlugin) {
        super(cookiePlugin);
    }
    
    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent event) {
    	Player player = event.getPlayer();
    	
        ChatAttempt chatAttempt = this.plugin.getChat().attemptChatMessage(player, event.getMessage());
        
        ChatAttemptEvent chatAttemptEvent = new ChatAttemptEvent(player, chatAttempt, event.getMessage());
        
        this.plugin.getServer().getPluginManager().callEvent((Event)chatAttemptEvent);
        
        Profile profile = Profile.getProfiles().get(player.getUniqueId());
        
        if (!chatAttemptEvent.isCancelled()) {
            switch (chatAttempt.getResponse()) {
                case ALLOWED: {
//                    event.setFormat(CookieAPI.getPrefixdPlayer(player) + CC.RESET + ": %2$s");
                    break;
                }
                case MESSAGE_FILTERED: {
                    event.setCancelled(true);
                    Messager.sendMessage(player, "&cYou message was filtered.");
                    Messager.sendStaffMessage("&c&l[Filtered] &r" + profile.getColoredUsername() + "&7: &f" + event.getMessage());
                    break;
                }
                case PLAYER_MUTED: {
                    event.setCancelled(true);
                    Messager.sendMessage(player, "&cYou are currently muted.");
                    Messager.sendMessage(player, "&cReason: &f" + chatAttempt.getPunishment().getAddedReason());
                    Messager.sendMessage(player, "&cExpires: &f" + chatAttempt.getPunishment().getTimeRemaining());
                    break;
                }
                default:
                	break;
            }
        }
    }
}
