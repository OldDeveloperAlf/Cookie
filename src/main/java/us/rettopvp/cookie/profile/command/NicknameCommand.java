package us.rettopvp.cookie.profile.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import us.rettopvp.cookie.profile.Profile;
import us.rettopvp.cookie.util.command.CustomCommand;
import us.rettopvp.cookie.util.message.Messager;

public class NicknameCommand extends CustomCommand {

	public NicknameCommand() {
		super("nickname", "Sets a players nickname", new String[] {"nick"});
	}

	@Override
	public boolean onCommand(CommandSender commandsender, Command command, String label, String[] arguments) {
		if(!(commandsender instanceof Player)) {
			this.noConsole(commandsender);
			return true;
		}
		Player player = (Player) commandsender;
		
		Profile profile = Profile.getProfiles().get(player.getUniqueId());
		
		if(arguments.length == 0) {			
			if(profile.getNickName() == null) {
				Messager.sendMessage(commandsender, "&6You do not currently have a nickname, to set a nickname use:");
				Messager.sendMessage(commandsender, "&6Syntax: &f(/" + label + " <name/none>)");
				return true;
			}
			
			Messager.sendMessage(commandsender, "&6Your nickname is currently &r" + profile.getNickName() + " &6to reset your nickname use:");
			Messager.sendMessage(commandsender, "&6Syntax: &f(/" + label + " none)");
			return true;
		}
		
		String nickname = arguments[0];
		
		if(arguments.length == 1) {
			if(nickname.equalsIgnoreCase("none")) {
				profile.setNickName(null);
				profile.refreshDisplayName();
				Messager.sendMessage(commandsender, "&6Your nickname has been reset.");
				return true;
			}
			if(!nickname.equalsIgnoreCase(player.getName()) && (nickname.isEmpty() || nickname.length() > 16)) {
				Messager.sendMessage(commandsender, "&cInvalid nickname.");
				return true;
			}
			
			if(nickname.equalsIgnoreCase(player.getName())) {
				Messager.sendMessage(commandsender, "&cA player already exists with that name.");
				return true;
			}
			
			profile.setNickName(nickname);
			profile.refreshDisplayName();
			Messager.sendMessage(commandsender, "&6Your nickname is now &r" + profile.getNickName());
			return true;
		}
		
		
		return true;
	}
}
