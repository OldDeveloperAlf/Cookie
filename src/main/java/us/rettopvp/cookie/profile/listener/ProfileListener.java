package us.rettopvp.cookie.profile.listener;

import java.util.List;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import us.rettopvp.cookie.CookiePlugin;
import us.rettopvp.cookie.misc.MiscListener;
import us.rettopvp.cookie.network.packet.PacketStaffJoinNetwork;
import us.rettopvp.cookie.profile.Profile;
import us.rettopvp.cookie.profile.punishment.Punishment;
import us.rettopvp.cookie.util.file.cursor.ConfigCursor;
import us.rettopvp.cookie.util.message.color.CC;

public class ProfileListener extends MiscListener {
	
//	private static List<String> BLOCKED_COMMANDS;
//    
//    static {
//    	ProfileListener.BLOCKED_COMMANDS = Arrays.asList("/bukkit:help", "/help", "/about", "bukkit:?", "/?", "/pl", "/plugins", "/version","/ver","//calc", "//eval", "//solve", "/bukkit:", "/me", "/bukkit:me", "/minecraft:", "/minecraft:me");
//    }
	
    public ProfileListener(CookiePlugin cookiePlugin) {
        super(cookiePlugin);
    }
 
    @EventHandler
    public void onAsyncPlayerPreLogin(AsyncPlayerPreLoginEvent event) {
        Player player = this.plugin.getServer().getPlayer(event.getUniqueId());
        
        if (player != null && player.isOnline()) {
            event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
            event.setKickMessage(CC.translate("&cYou tried to login too quickly after disconnecting.\n&cTry again in a few seconds."));
            
            this.plugin.getServer().getScheduler().runTask((Plugin)this.plugin, () -> player.kickPlayer(CC.translate("&cDuplicate login kick")));
            return;
        }
        
        Profile profile = new Profile(event.getName(), event.getUniqueId());
        
        try {
            if (!profile.isLoaded()) {
                event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
                event.setKickMessage(CC.translate("&cAn error has occurred, when loading your profile"));
                return;
            }
            
            if (profile.getActiveBan() != null) {
                this.handleBan(event, profile.getActiveBan());
                return;
            }
            
            profile.setUsername(event.getName());
            
            if (profile.getFirstSeen() == null) {
                profile.setFirstSeen(System.currentTimeMillis());
            }
            
            profile.setLastSeen(System.currentTimeMillis());
            
            if (profile.getCurrentAddress() == null) {
                profile.setCurrentAddress(event.getAddress().getHostAddress());
            }
            
            if (!profile.getIpAddresses().contains(event.getAddress().getHostAddress())) {
                profile.getIpAddresses().add(event.getAddress().getHostAddress());
            }
            
            if (!profile.getCurrentAddress().equals(event.getAddress().getHostAddress())) {
                List<Profile> alts = Profile.getByIpAddress(event.getAddress().getHostAddress());
                for (Profile alt : alts) {
                    if (alt.getActiveBan() != null) {
                        this.handleBan(event, alt.getActiveBan());
                        return;
                    }
                }
            }
            profile.save();
        } catch (Exception e) {
            this.plugin.getLogger().log(Level.SEVERE, "Failed to load profile for " + event.getName());
        }
        
        if (profile == null || !profile.isLoaded()) {
        	event.setKickMessage(CC.translate("&cAn error has occurred, when loading your profile"));
            event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
            return;
        }
        
        Profile.getProfiles().put(profile.getUuid(), profile);
        this.plugin.getUuidCache().update(event.getName(), event.getUniqueId());
    }
    
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        
        event.setJoinMessage(null);
        
        Profile profile = Profile.getProfiles().get(player.getUniqueId());
        profile.setupPermissionsAttachment(this.plugin, event.getPlayer());
 
        profile.refreshDisplayName();

        if(player.hasPermission("cookie.staff.bypass")) {
        	ConfigCursor cursor = new ConfigCursor(CookiePlugin.getPlugin().getRootConfig(), "settings.server");
        	CookiePlugin.getPlugin().getHandle().sendPacket(new PacketStaffJoinNetwork(profile.getColoredUsername(), cursor.getString("name")));
        }
    }
    
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
    	Player player = event.getPlayer();
    	event.setQuitMessage(null);
    	
        Profile profile = Profile.getProfiles().remove(event.getPlayer().getUniqueId());
        profile.refreshDisplayName();
        
        profile.setNickName(null);
        
        profile.setLastSeen(System.currentTimeMillis());
        
        if (profile.isLoaded()) {
        	new BukkitRunnable() {
                
        		@Override
        		public void run() {
                    try {
                        profile.save();
                    } catch (Exception e) {
                        Bukkit.getServer().getLogger().log(Level.SEVERE, "Failed to save profile " + player.getName());
                    }
                }
            }.runTaskAsynchronously(CookiePlugin.getPlugin());
        }
    }
    
    @EventHandler
    public void onKick(PlayerKickEvent event) {
    	event.setLeaveMessage(null);
    }
    
    private void handleBan(AsyncPlayerPreLoginEvent event, Punishment punishment) {
        event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
        
        event.setKickMessage(punishment.getKickMessage());
    }
    
//    @EventHandler(priority = EventPriority.LOWEST)
//    public void onCommandProcess(PlayerCommandPreprocessEvent event) {
//        Player player = event.getPlayer();
//        
//        if(!this.plugin.getRootConfig().getConfig().getString("settings.server.name").equals("Hub")) {
//        	if (ProfileListener.BLOCKED_COMMANDS.contains(event.getMessage().toLowerCase())) {
//                Messager.sendMessage(player, "&cYou cannot perform this command.");
//                event.setCancelled(true);
//            }
//        }
//    }
}
