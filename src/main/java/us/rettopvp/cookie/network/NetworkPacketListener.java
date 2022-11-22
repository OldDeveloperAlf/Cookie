package us.rettopvp.cookie.network;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.scheduler.BukkitRunnable;

import us.rettopvp.cookie.CookiePlugin;
import us.rettopvp.cookie.handle.packet.handler.IncomingPacketHandler;
import us.rettopvp.cookie.handle.packet.listener.PacketListener;
import us.rettopvp.cookie.network.event.ReceiveStaffChatEvent;
import us.rettopvp.cookie.network.packet.PacketAddGrant;
import us.rettopvp.cookie.network.packet.PacketBroadcast;
import us.rettopvp.cookie.network.packet.PacketBroadcastPunishment;
import us.rettopvp.cookie.network.packet.PacketBroadcastRaw;
import us.rettopvp.cookie.network.packet.PacketDeleteGrant;
import us.rettopvp.cookie.network.packet.PacketDeleteRank;
import us.rettopvp.cookie.network.packet.PacketRefreshRank;
import us.rettopvp.cookie.network.packet.PacketReportPlayer;
import us.rettopvp.cookie.network.packet.PacketRequestPlayer;
import us.rettopvp.cookie.network.packet.PacketStaffChat;
import us.rettopvp.cookie.network.packet.PacketStaffJoinNetwork;
import us.rettopvp.cookie.profile.Profile;
import us.rettopvp.cookie.profile.grant.Grant;
import us.rettopvp.cookie.profile.grant.event.GrantAppliedEvent;
import us.rettopvp.cookie.profile.grant.event.GrantExpireEvent;
import us.rettopvp.cookie.profile.punishment.Punishment;
import us.rettopvp.cookie.rank.Rank;
import us.rettopvp.cookie.util.message.Messager;
import us.rettopvp.cookie.util.message.color.CC;

public class NetworkPacketListener implements PacketListener {
	
    private CookiePlugin plugin;
    
    public NetworkPacketListener(CookiePlugin cookiePlugin) {
        this.plugin = cookiePlugin;
    }
    
    @IncomingPacketHandler
    public void onAddGrant(PacketAddGrant packet) {
        Player player = Bukkit.getPlayer(packet.getPlayerUuid());
        
        Grant grant = packet.getGrant();
        
        if(player == null) {
        	return;
        }
        
        Profile profile = Profile.getProfiles().get(player.getUniqueId());
        
        profile.getGrants().removeIf(other -> other.getUuid().equals(grant.getUuid()));
        profile.getGrants().add(grant);
        
        new GrantAppliedEvent(player, grant);
    }
    
    @IncomingPacketHandler
    public void onBroadcastMessage(PacketBroadcast packet) {
		Messager.broadcastMessage("&6[Broadcast] &7[" + packet.getServerName() + "] &r" + packet.getChatMessage());
	}
    
    @IncomingPacketHandler
    public void onBroadcastMessageRaw(PacketBroadcastRaw packet) {
    	Messager.broadcastMessage("&r" + packet.getChatMessage());
    }
    
    @IncomingPacketHandler
    public void onDeleteGrant(PacketDeleteGrant packet) {
        Player player = Bukkit.getPlayer(packet.getPlayerUuid());
        Grant grant = packet.getGrant();
        if (player != null) {
            Profile profile = Profile.getProfiles().get(player.getUniqueId());    
            
            profile.getGrants().removeIf(other -> other.getUuid().equals(grant.getUuid()));
            new GrantExpireEvent(player, grant);
        }
    }
    
    @IncomingPacketHandler
    public void onBroadcastPunishment(PacketBroadcastPunishment packet) {
        Punishment punishment = packet.getPunishment();
        
        punishment.broadcast(packet.getStaff(), packet.getTarget(), packet.isSilent());
        
        Player player = Bukkit.getPlayer(packet.getTargetUuid());
        
        if(player == null) {
        	return;
        }
        
        Profile profile = Profile.getProfiles().get(player.getUniqueId());
    	
        if(profile == null) {
        	return;
        }
        profile.getPunishments().removeIf(other -> other.getUuid().equals(punishment.getUuid())); 
        profile.getPunishments().add(punishment);
        
        if (punishment.getType().isBan()) {
            new BukkitRunnable() {
                
            	@Override
            	public void run() {
                    player.kickPlayer(punishment.getKickMessage());
                }
            }.runTask(this.plugin);
        }
    }
    
    @IncomingPacketHandler
    public void onRankRefresh(PacketRefreshRank packet) {
        Rank rank = Rank.getRankByUuid(packet.getUuid());
        
        if (rank == null) {
            rank = new Rank(packet.getUuid(), packet.getName());
        }
        
        rank.load();
    }
    
    @IncomingPacketHandler
    public void onRankDelete(PacketDeleteRank packet) {
        Rank.getRanks().remove(packet.getUuid());
    }
    
    @IncomingPacketHandler
    public void onStaffChat(PacketStaffChat packet) {
        for(Player onlinePlayer : Bukkit.getOnlinePlayers()) {
        	if(onlinePlayer.hasPermission("cookie.staff.bypass")) {
        		ReceiveStaffChatEvent event = new ReceiveStaffChatEvent(onlinePlayer);
                
            	this.plugin.getServer().getPluginManager().callEvent((Event)event);
                
            	if (!event.isCancelled()) {
                    onlinePlayer.sendMessage(CC.GOLD + "[S] " + CC.GRAY + "[" + packet.getServerName() + "] " + CC.RESET + packet.getPlayerName() + CC.WHITE + ": " + CC.RESET + packet.getChatMessage()); 
            	}
        	}
        }
    }
    
    @IncomingPacketHandler
    public void onStaffJoinNetwork(PacketStaffJoinNetwork packet) {
        for(Player players : Bukkit.getOnlinePlayers()) {
        	if(players.hasPermission("cookie.staff.bypass")) {
        		players.sendMessage(CC.GOLD + "[S] " + CC.RESET + packet.getPlayerName() + CC.YELLOW + " has joined to " + CC.PINK + packet.getServerName());
        	}
        }
    }
    
    @IncomingPacketHandler
    public void onReportPlayer(PacketReportPlayer packet) {
    	for(Player players : Bukkit.getOnlinePlayers()) {
    		if(players.hasPermission("cookie.staff.bypass")) {
    			Messager.sendMessage(players, "&c[R] &7[" + packet.getServerName() + "] &r" + packet.getTargetName() + " &ewas reported by &r" + packet.getSenderName() + " &efor &f" + packet.getReason());
    		}
    	}
    }
    
    @IncomingPacketHandler
    public void onRequestPlayer(PacketRequestPlayer packet) {
    	for(Player players : Bukkit.getOnlinePlayers()) {
    		if(players.hasPermission("cookie.staff.bypass")) {
    			Messager.sendMessage(players, "&a[R] &7[" + packet.getServerName() + "] &r" + packet.getSenderName() + " &erequires assistance from a staff for &f" + packet.getReason());
    		}
    	}
    }
}
