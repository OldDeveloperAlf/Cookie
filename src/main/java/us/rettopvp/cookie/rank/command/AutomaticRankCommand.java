package us.rettopvp.cookie.rank.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import us.rettopvp.cookie.CookiePlugin;
import us.rettopvp.cookie.util.command.CustomCommand;
import us.rettopvp.cookie.util.message.Messager;

public class AutomaticRankCommand extends CustomCommand {

	public AutomaticRankCommand() {
		super("automaticrank", "Automatically create all ranks, of the server, with their corresponding permissions.", new String[] {"autoranks"});
	}

	@Override
	public boolean onCommand(CommandSender commandsender, Command command, String label, String[] arguments) {
		// TODO Auto-generated method stub
		if(arguments.length == 0) {
			Messager.sendMessage(commandsender, "&aStarting procedures of Automatics ranks.");
			CookiePlugin.getPlugin().getAutomaticRank().runnable(commandsender);
			return true;
		}
		return true;
	}
}
