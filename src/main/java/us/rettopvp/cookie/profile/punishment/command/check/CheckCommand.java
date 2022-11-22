package us.rettopvp.cookie.profile.punishment.command.check;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import us.rettopvp.cookie.profile.Profile;
import us.rettopvp.cookie.profile.punishment.menu.PunishmentsMenu;
import us.rettopvp.cookie.util.command.CustomCommand;
import us.rettopvp.cookie.util.message.Messager;

public class CheckCommand extends CustomCommand {

	public CheckCommand() {
		super("check", "Get history of punishment to player's.", new String[] {"ce", "history"} );
	}
	
	@Override
	public boolean onCommand(CommandSender commandsender, Command command, String label, String[] arguments) {
		if(!(commandsender instanceof Player)) {
			this.noConsole(commandsender);
			return true;
		}
		
		if(!commandsender.hasPermission("cookie.command.check")) {
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
		new PunishmentsMenu(profile).openMenu(player);
		return true;
	}
}
