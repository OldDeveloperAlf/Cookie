package us.rettopvp.cookie.chat.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import us.rettopvp.cookie.CookiePlugin;
import us.rettopvp.cookie.network.packet.PacketBroadcast;
import us.rettopvp.cookie.util.command.CustomCommand;
import us.rettopvp.cookie.util.file.cursor.ConfigCursor;
import us.rettopvp.cookie.util.message.Messager;

public class BroadcastCommand extends CustomCommand {

	public BroadcastCommand() {
		super("broadcast", "Broadcast a message to the server.", new String[] {"bc"});
	}

	@Override
	public boolean onCommand(CommandSender commandsender, Command command, String label, String[] arguments) {
		if(!commandsender.hasPermission("cookie.command.broadcast")) {
			Messager.sendMessage(commandsender, command.getPermissionMessage());
			return true;
		}
		
		if(arguments.length < 1) {
			this.setUsage(commandsender, label + " <message...>");
			return true;
		}
		
		ConfigCursor cursor = new ConfigCursor(CookiePlugin.getPlugin().getRootConfig(), "settings.server");
		CookiePlugin.getPlugin().getHandle().sendPacket(new PacketBroadcast(cursor.getString("name"), this.getMessage(arguments)));
		return true;
	}
	
	private String getMessage(String[] args) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < args.length; ++i) {
            sb.append(args[i]).append(" ");
        }
        return sb.toString();
    }
}
