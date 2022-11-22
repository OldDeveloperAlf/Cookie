package us.rettopvp.cookie.chat.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import us.rettopvp.cookie.CookiePlugin;
import us.rettopvp.cookie.network.packet.PacketStaffChat;
import us.rettopvp.cookie.profile.Profile;
import us.rettopvp.cookie.util.command.CustomCommand;
import us.rettopvp.cookie.util.file.cursor.ConfigCursor;
import us.rettopvp.cookie.util.message.Messager;

public class StaffChatCommand extends CustomCommand {

	public StaffChatCommand() {
		super("staffchat", "Enters staff chat mode.", new String[] {"sc", "ac", "gsc"});
	}
	
	@Override
	public boolean onCommand(CommandSender commandsender, Command command, String label, String[] arguments) {
		if(!(commandsender instanceof Player)) {
			this.noConsole(commandsender);
			return true;
		}
		
		if(!commandsender.hasPermission("cookie.command.staffchat")) {
			Messager.sendMessage(commandsender, command.getPermissionMessage());
			return true;
		}
		
		if(arguments.length == 0) {
			this.setUsage(commandsender, label + " <message...>");
			return true;
		}
		
		StringBuilder message = new StringBuilder();
        for (int i = 0; i < arguments.length; ++i) {
            message.append(arguments[i]).append(" ");
        }
		
		Player player = (Player) commandsender;
		Profile profile = Profile.getProfiles().get(player.getUniqueId());
		
		ConfigCursor cursor = new ConfigCursor(CookiePlugin.getPlugin().getRootConfig(), "settings.server");
		
		CookiePlugin.getPlugin().getHandle().sendPacket(new PacketStaffChat(profile.getColoredUsername(), cursor.getString("name"), message.toString()));
		return true;
	}
}
