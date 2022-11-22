package us.rettopvp.cookie.profile.grant.command;

import java.util.UUID;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import us.rettopvp.cookie.CookiePlugin;
import us.rettopvp.cookie.network.packet.PacketAddGrant;
import us.rettopvp.cookie.profile.Profile;
import us.rettopvp.cookie.profile.grant.Grant;
import us.rettopvp.cookie.profile.grant.event.GrantAppliedEvent;
import us.rettopvp.cookie.rank.Rank;
import us.rettopvp.cookie.util.DurationUtil;
import us.rettopvp.cookie.util.command.CustomCommand;
import us.rettopvp.cookie.util.message.Messager;
import us.rettopvp.cookie.util.message.color.CC;

public class GrantCommand extends CustomCommand {

	public GrantCommand() {
		super("grant", "Grant a player rank.", new String[] {"gt"});
	}
	
	@Override
	public boolean onCommand(CommandSender commandsender, Command command, String label, String[] arguments) {
		if(!commandsender.hasPermission("cookie.command.grant")) {
			Messager.sendMessage(commandsender, command.getPermissionMessage());
			return true;
		}
		
		if(arguments.length < 4) {
			this.setUsage(commandsender, label + " <player> <rank> <duration> <reason>");
			return true;
		}
		
		String profileName = arguments[0];
		Profile profile = Profile.getByUsername(profileName);
		if(profile == null || !profile.isLoaded()) {
			Messager.sendMessage(commandsender, "&cCould not resolve player information...");
			return true;
		}
		
		String rankName = arguments[1];
		Rank rank = Rank.getRankByDisplayName(rankName);
		if(rank == null) {
			Messager.sendMessage(commandsender, "&cA rank with that name does not exist.");
			return true;
		}
		
		String durationTime = arguments[2];
		DurationUtil duration = DurationUtil.fromString(durationTime);
		if(duration.getValue() == -1L) {
			Messager.sendMessage(commandsender, "&cThat duration is not valid.");
			Messager.sendMessage(commandsender, "&cTimes: 1d2h (1 day, and 2 hours) / perm - permanent");
			return true;
		}
		
		UUID addedBy = (commandsender instanceof Player) ? ((Player)commandsender).getUniqueId() : null;
		Grant grant = new Grant(UUID.randomUUID(), rank, addedBy, System.currentTimeMillis(), this.getMessage(arguments), duration.getValue());
		
		profile.getGrants().add(grant);
		profile.save();
		profile.activateNextGrant();
		
		CookiePlugin.getPlugin().getHandle().sendPacket(new PacketAddGrant(profile.getUuid(), grant));
		
		Messager.sendMessage(commandsender, "&eYou applied a &r" + rank.getColoredName() + "&e grant to &r" + profile.getColoredUsername());
		
		Player player = profile.getPlayer();
		if(player != null) {
			new GrantAppliedEvent(player, grant).call();
			profile.refreshDisplayName();
			player.kickPlayer(CC.translate("&cYou have been granted a rank " + rank.getDisplayName() + ", please re-join the server."));
		}
		return true;
	}
	
	private String getMessage(String[] args) {
        StringBuilder sb = new StringBuilder();
        for (int i = 3; i < args.length; ++i) {
            sb.append(args[i]).append(" ");
        }
        return sb.toString();
    }
}
