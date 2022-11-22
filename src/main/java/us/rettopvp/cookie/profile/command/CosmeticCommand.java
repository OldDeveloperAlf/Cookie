package us.rettopvp.cookie.profile.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import us.rettopvp.cookie.cosmetic.menu.EditTagMenu;
import us.rettopvp.cookie.cosmetic.menu.SelectColorMenu;
import us.rettopvp.cookie.cosmetic.type.CosmeticEditType;
import us.rettopvp.cookie.util.command.CustomCommand;
import us.rettopvp.cookie.util.message.Messager;

public class CosmeticCommand extends CustomCommand {

	public CosmeticCommand() {
		super("cosmetic", "Open cosmetic menus.");
	}
	
	@Override
	public boolean onCommand(CommandSender commandsender, Command command, String label, String[] arguments) {
		if(!(commandsender instanceof Player)) {
			this.noConsole(commandsender);
			return true;
		}
		
		if(!commandsender.hasPermission("cookie.command.cosmetic")) {
			Messager.sendMessage(commandsender, command.getPermissionMessage());
			return true;
		}
		
		if(arguments.length == 0) {
			this.setUsage(commandsender, label + " <tag|color>");
			return true;
		}
		
		Player player = (Player) commandsender;
		
		if(arguments[0].equalsIgnoreCase("tag")) {
			new EditTagMenu().openMenu(player);
			return true;
		}
		
		if(arguments[0].equalsIgnoreCase("color")) {
			new SelectColorMenu(CosmeticEditType.COLOR).openMenu(player);
			return true;
		} else {
			this.setInvalidSubcommand(commandsender, arguments[0]);
		}
		return true;
	}
}
