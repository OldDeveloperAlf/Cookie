package us.rettopvp.cookie.profile.punishment.command;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import us.rettopvp.cookie.CookiePlugin;
import us.rettopvp.cookie.network.packet.PacketBroadcastPunishment;
import us.rettopvp.cookie.profile.Profile;
import us.rettopvp.cookie.profile.punishment.Punishment;
import us.rettopvp.cookie.profile.punishment.PunishmentType;
import us.rettopvp.cookie.util.command.CustomCommand;
import us.rettopvp.cookie.util.message.Messager;
import us.rettopvp.cookie.util.message.color.CC;

public class WarnCommand extends CustomCommand {

	public WarnCommand() {
		super("warn", "Warn a player");
	}
	
	@Override
	public boolean onCommand(CommandSender commandsender, Command command, String label, String[] arguments) {
		if(!commandsender.hasPermission("cookie.command.warn")) {
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
		
		Player player = Bukkit.getPlayer(profileName);
	
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
		Punishment punishment = new Punishment(UUID.randomUUID(), PunishmentType.WARN, System.currentTimeMillis(), reason, -1L);
		if(commandsender instanceof Player) {
			punishment.setAddedBy(((Player)commandsender).getUniqueId());
		}
		
		profile.getPunishments().add(punishment);
		profile.save();
		
		if(player != null) {
			Messager.sendMessage(player, "&cYou have been warned by " + staffName + " for: &f" + reason);
		}
		
		CookiePlugin.getPlugin().getHandle().sendPacket(new PacketBroadcastPunishment(punishment, staffName, profile.getColoredUsername(), profile.getUuid(), silent));
		return true;
	}
}
