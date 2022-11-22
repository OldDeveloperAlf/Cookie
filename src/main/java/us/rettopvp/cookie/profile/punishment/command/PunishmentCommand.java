package us.rettopvp.cookie.profile.punishment.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import us.rettopvp.cookie.util.command.CustomCommand;

public class PunishmentCommand extends CustomCommand {

	public PunishmentCommand() {
		super("punishment", "Punishment manage of the player.", new String[] {"punish"});
	}
	
	@Override
	public boolean onCommand(CommandSender commandsender, Command command, String label, String[] arguments) {
		
		return true;
	}
}
