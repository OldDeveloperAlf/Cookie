package us.rettopvp.cookie.profile.grant.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import us.rettopvp.cookie.profile.Profile;
import us.rettopvp.cookie.profile.grant.menu.GrantsMenu;
import us.rettopvp.cookie.util.command.CustomCommand;
import us.rettopvp.cookie.util.message.Messager;

public class GrantsCommand extends CustomCommand {

	public GrantsCommand() {
		super("grants", "Open grants history.", new String[] {"gts"});
	}
	
	@Override
	public boolean onCommand(CommandSender commandsender, Command command, String label, String[] arguments) {
		if(!(commandsender instanceof Player)) {
			this.noConsole(commandsender);
			return true;
		}
		
		if(!commandsender.hasPermission("cookie.command.grants")) {
			Messager.sendMessage(commandsender, command.getPermissionMessage());
			return true;
		}
		
		if(arguments.length == 0) {
			this.setUsage(commandsender, label + " <player>");
			return true;
		}
		
		String profileName = arguments[0];
		Profile profile = Profile.getByUsername(profileName);
		if(profile == null || !profile.isLoaded()) {
			Messager.sendMessage(commandsender, "&cCould not resolve player information...");
			return true;
		}
		
		Player player = (Player) commandsender;
		new GrantsMenu(profile).openMenu(player);
		return true;
	}
}
