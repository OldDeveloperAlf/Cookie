package us.rettopvp.cookie.rank.command;

import java.util.StringJoiner;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import us.rettopvp.cookie.rank.Rank;
import us.rettopvp.cookie.util.JavaUtil;
import us.rettopvp.cookie.util.command.CustomCommand;
import us.rettopvp.cookie.util.message.Messager;
import us.rettopvp.cookie.util.message.color.CC;

public class RankCommand extends CustomCommand {

	public RankCommand() {
		super("rank", "Subcommands of rank's.");
	}

	@Override
	public boolean onCommand(CommandSender commandsender, Command command, String label, String[] arguments) {
		if(!commandsender.hasPermission("cookie.command.rank")) {
			Messager.sendMessage(commandsender, "&c" + command.getPermissionMessage());
			return true;
		}
		
		if(arguments.length == 0) {
			Messager.sendMessage(commandsender, CC.CHAT_BAR);
			Messager.sendMessage(commandsender, "&6&lRank Sub-commands.");
			Messager.sendMessage(commandsender, "&e/" + label + " create [rank] &7- &fCreate a Rank.");
			Messager.sendMessage(commandsender, "&e/" + label + " delete (rank) &7- &fDelete a Rank.");
			Messager.sendMessage(commandsender, "&e/" + label + " setprefix (rank) [prefix] &7- &fSet prefix of a Rank.");
			Messager.sendMessage(commandsender, "&e/" + label + " setcolor (rank) [COLOR] &7- &fSet color of a Rank.");
			Messager.sendMessage(commandsender, "&e/" + label + " setweight (rank) [weight] &7- &fSet weight of a Rank.");
			Messager.sendMessage(commandsender, "&e/" + label + " addperm (rank) [permission] &7- &fAdded permissions to a Rank.");
			Messager.sendMessage(commandsender, "&e/" + label + " delperm (rank) [permission] &7- &fDeleted permissions to a Rank.");
			Messager.sendMessage(commandsender, "&e/" + label + " inhert (parent) [child] &7- &fInherit a child Rank.");
			Messager.sendMessage(commandsender, "&e/" + label + " dump (rank) &7- &fDump a Rank's permissions.");
			Messager.sendMessage(commandsender, "&e/" + label + " list &7- &fList of the Rank's.");
			Messager.sendMessage(commandsender, CC.CHAT_BAR);
			return true;
		}
		if(arguments[0].equalsIgnoreCase("create") || arguments[0].equalsIgnoreCase("init")) {
			if(arguments.length != 2) {
				Messager.sendMessage(commandsender, "&cInvalid usage: /" + label + " create [name]");
				return true;
			}
			String rankName = arguments[1];
			
			if(Rank.getRankByDisplayName(rankName) != null) {
				Messager.sendMessage(commandsender, "&cA rank with that name already exists.");
				return true;
			}
			
			Rank rank = new Rank(rankName);
			rank.save();
			Messager.sendMessage(commandsender, "&eYou created a new global rank&7: &r" + rank.getColoredName());
			return true;
		}
		if(arguments[0].equalsIgnoreCase("delete") || arguments[0].equalsIgnoreCase("remove")) {
			if(arguments.length != 2) {
				Messager.sendMessage(commandsender, "&cInvalid usage: /" + label + " delete [name]");
				return true;
			}
			String rankName = arguments[1];
			
			Rank rank = Rank.getRankByDisplayName(rankName);
			if(rank == null) {
				Messager.sendMessage(commandsender, "&cA rank with that name does not exist.");
				return true;
			}
			rank.delete();
			Messager.sendMessage(commandsender, "&eYou deleted &r" + rank.getColoredName());
			return true;
		}
		if(arguments[0].equalsIgnoreCase("setprefix")) {
			if(arguments.length != 3) {
				Messager.sendMessage(commandsender, "&cInvalid usage: /" + label + " setprefix (rank) [prefix]");
				return true;
			}
			String rankName = arguments[1];
			
			Rank rank = Rank.getRankByDisplayName(rankName);
			if(rank == null) {
				Messager.sendMessage(commandsender, "&cA rank with that name does not exist.");
				return true;
			}
			
			String prefix = CC.translate(arguments[2]);
			
			rank.setPrefix(prefix);
			rank.save();
			Messager.sendMessage(commandsender, "&eYou updated the prefix of &r" + rank.getColoredName() + " &eto &r" + rank.getPrefix() + commandsender.getName());
			return true;
		}
		if(arguments[0].equalsIgnoreCase("setcolor")) {
			if(arguments.length != 3) {
				Messager.sendMessage(commandsender, "&cInvalid usage: /" + label + " setcolor (rank) [color]");
				return true;
			}
			String rankName = arguments[1];
			
			Rank rank = Rank.getRankByDisplayName(rankName);
			if(rank == null) {
				Messager.sendMessage(commandsender, "&cA rank with that name does not exist.");
				return true;
			}
			String oldcolor = rank.getColoredName();
			
			String color = arguments[2];
			try {
				rank.setColor(ChatColor.valueOf(color));
				rank.save();
			} catch (Exception e) {
				Messager.sendMessage(commandsender, "&cThis color is not valid");
				Messager.sendMessage(commandsender, "&cExample: RED, BLUE, AQUA, GREEN, YELLOW, GOLD, GRAY, WHITE, LIGHT_PURPLE, BLACK and DARK_(COLOR)");
				return true;
			}
			Messager.sendMessage(commandsender, "&eYou updated the color of " + oldcolor + " &eto " + rank.getColoredName());
			return true;
		}
		if(arguments[0].equalsIgnoreCase("setweight")) {
			if(arguments.length != 3) {
				Messager.sendMessage(commandsender, "&cInvalid usage: /" + label + " setweight (rank) [weight]");
				return true;
			}
			String rankName = arguments[1];
			
			Rank rank = Rank.getRankByDisplayName(rankName);
			if(rank == null) {
				Messager.sendMessage(commandsender, "&cA rank with that name does not exist.");
				return true;
			}
			
			Integer weight = JavaUtil.tryParseInt(arguments[2]);
			if(weight == null) {
				Messager.sendMessage(commandsender, "This is not valid number.");
				return true;
			}
			rank.setWeight(weight);
			rank.save();
			Messager.sendMessage(commandsender, "&eYou updated the weight of &r" + rank.getColoredName() + " &eto &d" + rank.getWeight());
			return true;
		}
		if(arguments[0].equalsIgnoreCase("addpermission") || arguments[0].equalsIgnoreCase("addperm")) {
			if(arguments.length != 3) {
				Messager.sendMessage(commandsender, "&cInvalid usage: /" + label + " addpermission (rank) [permission]");
				return true;
			}
			String rankName = arguments[1];
			
			Rank rank = Rank.getRankByDisplayName(rankName);
			if(rank == null) {
				Messager.sendMessage(commandsender, "&cA rank with that name does not exist.");
				return true;
			}
			
			String permission = arguments[2];
			if (rank.getPermissions().contains(permission)) {
				Messager.sendMessage(commandsender, "&c" + rankName +" already had the '" + permission + "' permission.");
				return true;
			}
			
			rank.getPermissions().add(permission);
			rank.save();
			Messager.sendMessage(commandsender, "&eYou added '&d" + permission + "&e' to &r" + rank.getColoredName() + "&e's permissions.");
			return true;
		}
		if(arguments[0].equalsIgnoreCase("removepermission") || arguments[0].equalsIgnoreCase("removeperm") || arguments[0].equalsIgnoreCase("delperm") || arguments[0].equalsIgnoreCase("deletepermission")) {
			if(arguments.length != 3) {
				Messager.sendMessage(commandsender, "&cInvalid usage: /" + label + " removepermission (rank) [permission]");
				return true;
			}
			String rankName = arguments[1];
			
			Rank rank = Rank.getRankByDisplayName(rankName);
			if(rank == null) {
				Messager.sendMessage(commandsender, "&cA rank with that name does not exist.");
				return true;
			}
			
			String permission = arguments[2];
			if (!rank.getPermissions().remove(permission)) {
				Messager.sendMessage(commandsender, "&c" + rankName +" did not have the '" + permission + "' permission.");
				return true;
			}
			
			rank.save();
			Messager.sendMessage(commandsender, "&eYou removed '&d" + permission + "&e' from &r" + rank.getColoredName() + "&e's permissions.");
			return true;
		}
		if(arguments[0].equalsIgnoreCase("list") || arguments[0].equalsIgnoreCase("who")) {
			Messager.sendMessage(commandsender, CC.CHAT_BAR);
			Messager.sendMessage(commandsender, "&6&lList of the ranks.");
			for(Rank ranks : Rank.getRanks().values()) {
				Messager.sendMessage(commandsender, " &f- &r" + ranks.getColoredName() + "&r" + (ranks.isDefaultRank() ? " (Default)" : "") + " (Weight: " + ranks.getWeight() + ")");
			}
			Messager.sendMessage(commandsender, CC.CHAT_BAR);
			return true;
		}
		if(arguments[0].equalsIgnoreCase("inheritance") || arguments[0].equalsIgnoreCase("inherit")) {
			if(arguments.length != 3) {
				Messager.sendMessage(commandsender, "&cInvalid usage: /" + label + " inheritance (parent) [child]");
				return true;
			}
			String parentName = arguments[1];
			
			Rank parent = Rank.getRankByDisplayName(parentName);
			if(parent == null) {
				Messager.sendMessage(commandsender, "&cParent rank with that name does not exist.");
				return true;
			}
			String childName = arguments[2];
			
			Rank child = Rank.getRankByDisplayName(childName);
			if(child == null) {
				Messager.sendMessage(commandsender, "&cChild rank with that name does not exist.");
				return true;
			}
			
			boolean removed = false;
			if(parent.getInherited().remove(child)) {
				removed = true;
			} else {
				parent.getInherited().add(child);
			}
			parent.save();
			Messager.sendMessage(commandsender, "&cYou made &r" + parent.getColoredName() + (removed ? " &cno longer inherit" : " &ainherit") + " &r" + child.getColoredName());
			return true;
		}
		if(arguments[0].equalsIgnoreCase("dump") || arguments[0].equalsIgnoreCase("info")) {
			if(arguments.length != 2) {
				Messager.sendMessage(commandsender, "&cInvalid usage: /" + label + " dump (rank)");
				return true;
			}
			String rankName = arguments[1];
			
			Rank rank = Rank.getRankByDisplayName(rankName);
			if(rank == null) {
				Messager.sendMessage(commandsender, "&cA rank with that name does not exist.");
				return true;
			}
			
			Messager.sendMessage(commandsender, CC.CHAT_BAR);
			Messager.sendMessage(commandsender, "&6Rank Information &7(&r" + rank.getColoredName() + "&7)");
			Messager.sendMessage(commandsender, "&eWeight&7: &r" + rank.getWeight());
			Messager.sendMessage(commandsender, "&ePrefix&7: &r" + rank.getPrefix() + "Example");
			Messager.sendMessage(commandsender, "&eColor&7: &r" + rank.getColor().name());
			Messager.sendMessage(commandsender, " ");
			Messager.sendMessage(commandsender, "&ePermissions: &r(" + rank.getAllPermissions().size() + ")");
			StringJoiner perm = new StringJoiner(", ");
			for(String permissions : rank.getAllPermissions()) {
				perm.add(permissions);
			}
			if(rank.getAllPermissions().size() > 0) {
				Messager.sendMessage(commandsender, "&r" + perm.toString());
			}
			Messager.sendMessage(commandsender, " ");
			Messager.sendMessage(commandsender, "&eInherits: &r(" + rank.getInherited().size() + ")");
			for(Rank inherit : rank.getInherited()) {
				Rank other = Rank.getRankByDisplayName(inherit.getDisplayName());
				if(other == null) {
					Messager.sendMessage(commandsender, " &f* &r" + inherit.getColoredName());
				} else {
					Messager.sendMessage(commandsender, " &f* &r" + other.getColoredName());
				}
			}
			Messager.sendMessage(commandsender, CC.CHAT_BAR);
		} else {
			Messager.sendMessage(commandsender, "&cInvalid sub-command");
		}
		return true;
	}
}
