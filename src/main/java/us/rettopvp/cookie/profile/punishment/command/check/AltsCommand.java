package us.rettopvp.cookie.profile.punishment.command.check;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import us.rettopvp.cookie.profile.Profile;
import us.rettopvp.cookie.util.command.CustomCommand;
import us.rettopvp.cookie.util.message.Messager;

public class AltsCommand extends CustomCommand {

	public AltsCommand() {
		super("alts", "Get a list of a player's alts.", new String[] {"altertnates"});
	}
	
	@Override
	public boolean onCommand(CommandSender commandsender, Command command, String label, String[] arguments) {
		if(!commandsender.hasPermission("cookie.command.alts")) {
			Messager.sendMessage(commandsender, command.getPermissionMessage());
			return true;
		}
		
		if(arguments.length == 0) {
			this.setUsage(commandsender, label + " <player>");
			return true;
		}
		
		String profileName = arguments[0];
		if(arguments.length == 1) {
			Profile profile = Profile.getByUsername(profileName);
			if(profile == null || !profile.isLoaded()) {
				Messager.sendMessage(commandsender, "&cCould not resolve player information...");
				return true;
			}
			
			List<Profile> alts = new ArrayList<Profile>();
			for(UUID uuidAlt : profile.getKnownAlts()) {
				Profile profileAlt = Profile.getByUuid(uuidAlt);
				if(profileAlt != null && profileAlt.isLoaded()) {
					alts.add(profileAlt);
				}
			}
			if(alts.isEmpty()) {
				Messager.sendMessage(commandsender, "&cThis player has no known alt accounts.");
			} else {
				StringBuilder buidler = new StringBuilder();
				for(Profile profileAlt : alts) {
					buidler.append(profileAlt.getUsername());
					buidler.append(", ");
				}
				Messager.sendMessage(commandsender, "&6Alts&f: &r" + buidler.toString());
			}
			return true;
		}
		return true;
	}
}
