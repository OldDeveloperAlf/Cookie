package us.rettopvp.cookie.profile.grant.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import us.rettopvp.cookie.CookiePlugin;
import us.rettopvp.cookie.misc.MiscListener;
import us.rettopvp.cookie.network.packet.PacketDeleteGrant;
import us.rettopvp.cookie.profile.grant.Grant;
import us.rettopvp.cookie.profile.grant.event.GrantAppliedEvent;
import us.rettopvp.cookie.profile.grant.event.GrantExpireEvent;
import us.rettopvp.cookie.profile.grant.procedure.GrantProcedure;
import us.rettopvp.cookie.profile.grant.procedure.GrantProcedureStage;
import us.rettopvp.cookie.profile.grant.procedure.GrantProcedureType;
import us.rettopvp.cookie.util.TimeUtil;
import us.rettopvp.cookie.util.callback.TypeCallback;
import us.rettopvp.cookie.util.menu.Button;
import us.rettopvp.cookie.util.menu.menus.ConfirmMenu;
import us.rettopvp.cookie.util.message.Messager;
import us.rettopvp.cookie.util.message.color.CC;

public class GrantListener extends MiscListener {
	
    public GrantListener(CookiePlugin cookiePlugin) {
        super(cookiePlugin);
    }
    
    @EventHandler
    public void onGrantAppliedEvent(GrantAppliedEvent event) {
        Player player = event.getPlayer();
        
        Grant grant = event.getGrant();
        Messager.sendMessage(player, "&eYou have been applied in grant &r" + grant.getRank().getColoredName() + "&e for &6" + (grant.getDuration() == 2147483647L ? "forever" : TimeUtil.millisToRoundedTime(System.currentTimeMillis() - grant.getAddedAt() + grant.getDuration())));
    }
    
    @EventHandler
    public void onGrantExpireEvent(GrantExpireEvent event) {
        Player player = event.getPlayer();
        
        Grant grant = event.getGrant();
        
        Messager.sendMessage(player, "&eYour &r" + grant.getRank().getColoredName() + "&e grant has expired.");
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent event) {
    	Player player = event.getPlayer();
        if (!player.hasPermission("cookie.staff.grant")) {
            return;
        }
        
        GrantProcedure procedure = GrantProcedure.getByPlayer(event.getPlayer());
        if (procedure != null && procedure.getStage() == GrantProcedureStage.REQUIRE_TEXT) {
            event.setCancelled(true);
            
            if (event.getMessage().equalsIgnoreCase("cancel")) {
                GrantProcedure.getProcedures().remove(procedure);
                Messager.sendMessage(player, "&cYou have cancelled the grant procedure.");
                return;
            }
            
            if (procedure.getType() == GrantProcedureType.REMOVE) {
                new ConfirmMenu(CC.YELLOW + "Delete this grant?", new TypeCallback<Boolean>() {
                    
                	@Override
                    public void callback(Boolean data) {
                        if (data) {
                            procedure.getGrant().setRemovedBy(event.getPlayer().getUniqueId());
                            procedure.getGrant().setRemovedAt(System.currentTimeMillis());
                            procedure.getGrant().setRemovedReason(event.getMessage());
                            procedure.getGrant().setRemoved(true);
                            
                            procedure.finish();
                            
                            if(procedure.getRecipient().getPlayer() != null) {
                            	procedure.getRecipient().refreshDisplayName();
                            	procedure.getRecipient().getPlayer().kickPlayer(CC.translate("&cYou have removed the rank " + procedure.getGrant().getRank().getDisplayName() + ", please re-join the server."));
                            }
                            
                            Messager.sendMessage(player, "&eThe grant has been removed.");
                            
                            CookiePlugin.getPlugin().getHandle().sendPacket(new PacketDeleteGrant(procedure.getRecipient().getUuid(), procedure.getGrant()));
                        } else {
                            procedure.cancel();
                            Messager.sendMessage(player, "&cYou did not confirm to remove the grant.");
                        }
                    }
                }, true, new Button[0]) {
                    
                	@Override
                    public void onClose(Player player) {
                        if (!this.isClosedByMenu()) {
                            procedure.cancel();
                            
                            Messager.sendMessage(player, "&cYou did not confirm to remove the grant.");
                        }
                    }
                }.openMenu(event.getPlayer());
            }
        }
    }
}
