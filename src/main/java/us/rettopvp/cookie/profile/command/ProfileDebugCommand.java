package us.rettopvp.cookie.profile.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;

import us.rettopvp.cookie.profile.Profile;
import us.rettopvp.cookie.profile.grant.Grant;
import us.rettopvp.cookie.util.command.CustomCommand;
import us.rettopvp.cookie.util.message.Messager;
import us.rettopvp.cookie.util.message.color.CC;

public class ProfileDebugCommand extends CustomCommand {

	public ProfileDebugCommand() {
		super("profiledebug", "Check information about a player.", new String[] {"whois"});
	}
	
	@Override
	public boolean onCommand(CommandSender commandsender, Command command, String label, String[] arguments) {
		if(!commandsender.hasPermission("cookie.command.profiledebug")) {
			Messager.sendMessage(commandsender, command.getPermissionMessage());
			return true;
		}
		
		if(arguments.length == 0) {
			this.setUsage(commandsender, label + " <player>");
			return true;
		}
		
		String targetName = arguments[0];
		
		Player target = Bukkit.getPlayer(targetName);
		if(target == null) {
			Messager.sendMessage(commandsender, "&cThe player must be connected to the server, in order to perform this.");
			return true;
		}
		
		Profile profile = Profile.getProfiles().get(target.getUniqueId());
		if(profile == null) {
			Messager.sendMessage(commandsender, "&cAn error occurred while loading this profile.");
			return true;
		}
		
		Messager.sendMessage(commandsender, CC.CHAT_BAR);
		Messager.sendMessage(commandsender, "&r" + profile.getColoredUsername() + " &6Information profile.");
		Messager.sendMessage(commandsender, "&6Fly&7: &f" + target.getFlySpeed());
		Messager.sendMessage(commandsender, "&6Speed&7: &f" + target.getWalkSpeed());
		Messager.sendMessage(commandsender, "&6Health&7: &f" + ((Damageable)target).getHealth() + "/" + ((Damageable)target).getMaxHealth());
		Messager.sendMessage(commandsender, "&6Hunger&7: &f" + target.getFoodLevel() + "/20 (" + target.getSaturation() + " saturation)");
		Messager.sendMessage(commandsender, "&6Operator&7: &f" + (target.isOp() ? "&aYes" : "&cNo"));
		Messager.sendMessage(commandsender, "&6Handle Latency&7: &f" + ((CraftPlayer)target).getHandle().ping + "ms");
		Messager.sendMessage(commandsender, "&6IP Address&7: &f" + target.getAddress().getHostName());
		Messager.sendMessage(commandsender, "&6Rank&7: &f" + profile.getActiveRank().getColoredName());
		Messager.sendMessage(commandsender, "&6Grants&7: ");
		for (Grant grant : profile.getGrants()) {
			Messager.sendMessage(commandsender, " &f- &r" + grant.getRank().getColoredName() + (grant.isRemoved() ? " &c(Removed)" : (grant.hasExpired() ? " &c(Expired)" : " &a(Active)")));
		}
		Messager.sendMessage(commandsender, CC.CHAT_BAR);
		return true;
	}
}
