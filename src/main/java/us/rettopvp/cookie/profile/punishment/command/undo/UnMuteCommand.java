package us.rettopvp.cookie.profile.punishment.command.undo;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import us.rettopvp.cookie.CookiePlugin;
import us.rettopvp.cookie.network.packet.PacketBroadcastPunishment;
import us.rettopvp.cookie.profile.Profile;
import us.rettopvp.cookie.profile.punishment.Punishment;
import us.rettopvp.cookie.util.command.CustomCommand;
import us.rettopvp.cookie.util.message.Messager;
import us.rettopvp.cookie.util.message.color.CC;

public class UnMuteCommand extends CustomCommand {

	public UnMuteCommand() {
		super("unmute", "UnMute a player");
	}
	
	@Override
	public boolean onCommand(CommandSender commandsender, Command command, String label, String[] arguments) {
		if(!commandsender.hasPermission("&c" + command.getPermissionMessage())) {
			Messager.sendMessage(commandsender, command.getPermissionMessage());
			return true;
		}
		
		if(arguments.length < 2) {
			this.setUsage(commandsender, label + " <player> <reason> <-s>");
			return true;
		}
		
		String profileName = arguments[0];
		Profile profile = Profile.getByUsername(profileName);
		if(profile == null || !profile.isLoaded()) {
			Messager.sendMessage(commandsender, "&cCould not resolve player information...");
			return true;
		}
		
		if(profile.getActiveMute() == null) {
			Messager.sendMessage(commandsender, "&cThat player is not muted.");
			return true;
		}
		
		boolean silent = arguments[arguments.length - 1].equalsIgnoreCase("-s");
		
		StringBuilder sb = new StringBuilder();

        for (int i = 1; i < (silent ? arguments.length - 1 : arguments.length); i++) {
            sb.append(arguments[i]).append(" ");
        }

        String reason = sb.toString().trim();

        if (reason.equalsIgnoreCase("-s")) {
           	Messager.sendMessage(commandsender, "&cPlease provide a valid reason.");
            return true;
        }
        
		String staffName = (commandsender instanceof Player) ? Profile.getProfiles().get(((Player) commandsender).getUniqueId()).getColoredUsername() : (CC.DARK_RED + "Console");
		Punishment punishment = profile.getActiveMute();
		
		punishment.setPardonedAt(System.currentTimeMillis());
		punishment.setPardonedReason(reason);
		punishment.setPardoned(true);
		
		if(commandsender instanceof Player) {
			punishment.setPardonedBy(((Player)commandsender).getUniqueId());
		}
		
		profile.save();
		CookiePlugin.getPlugin().getHandle().sendPacket(new PacketBroadcastPunishment(punishment, staffName, profile.getColoredUsername(), profile.getUuid(), silent));
		return true;
	}
}
