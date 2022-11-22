package us.rettopvp.cookie.profile.punishment.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import us.rettopvp.cookie.CookiePlugin;
import us.rettopvp.cookie.misc.MiscListener;
import us.rettopvp.cookie.profile.punishment.procedure.PunishmentProcedure;
import us.rettopvp.cookie.profile.punishment.procedure.PunishmentProcedureStage;
import us.rettopvp.cookie.profile.punishment.procedure.PunishmentProcedureType;
import us.rettopvp.cookie.util.callback.TypeCallback;
import us.rettopvp.cookie.util.menu.Button;
import us.rettopvp.cookie.util.menu.menus.ConfirmMenu;
import us.rettopvp.cookie.util.message.Messager;
import us.rettopvp.cookie.util.message.color.CC;

public class PunishmentListener extends MiscListener {
	
    public PunishmentListener(CookiePlugin cookiePlugin) {
        super(cookiePlugin);
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent event) {
    	Player player = event.getPlayer();
    	
        if (!player.hasPermission("cookie.staff.bypass")) {
            return;
        }
        
        PunishmentProcedure procedure = PunishmentProcedure.getByPlayer(event.getPlayer());
        
        if (procedure != null && procedure.getStage() == PunishmentProcedureStage.REQUIRE_TEXT) {
            event.setCancelled(true);
            
            if (event.getMessage().equalsIgnoreCase("cancel")) {
                PunishmentProcedure.getProcedures().remove(procedure);
                Messager.sendMessage(player, "&cYou have cancelled the punishment procedure.");
                return;
            }
            
            if (procedure.getType() == PunishmentProcedureType.PARDON) {
                new ConfirmMenu(CC.YELLOW + "Pardon this punishment?", new TypeCallback<Boolean>() {
                    
                	@Override
                    public void callback(Boolean data) {
                        if (data) {
                            procedure.getPunishment().setPardonedBy(event.getPlayer().getUniqueId());
                            procedure.getPunishment().setPardonedAt(System.currentTimeMillis());
                            procedure.getPunishment().setPardonedReason(event.getMessage());
                            procedure.getPunishment().setPardoned(true);
                            
                            procedure.finish();
                            
                            Messager.sendMessage(player, "&eThe punishment has been pardoned.");
                        } else {
                            Messager.sendMessage(player, "&cYou did not confirm to pardon the punishment.");
                        }
                    }
                }, true, new Button[0]) {
                    
                	@Override
                    public void onClose(Player player) {
                        if (!this.isClosedByMenu()) {
                            Messager.sendMessage(player, "&cYou did not confirm to pardon the punishment.");
                        }
                    }
                }.openMenu(event.getPlayer());
            }
        }
    }
}
