package us.rettopvp.cookie.util.command.base;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.WordUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import com.google.common.collect.ImmutableList;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import us.rettopvp.cookie.util.BukkitUtil;
import us.rettopvp.cookie.util.message.color.CC;

public abstract class ArgumentExecutor implements CommandExecutor, TabCompleter {
	
    protected List<CommandArgument> arguments;
    protected String label;

    public ArgumentExecutor(String label) {
        this.arguments = new ArrayList<CommandArgument>();
        this.label = label;
    }

    public boolean containsArgument(CommandArgument argument) {
        return this.arguments.contains(argument);
    }

    public void addArgument(CommandArgument argument) {
        this.arguments.add(argument);
    }

    public void removeArgument(CommandArgument argument) {
        this.arguments.remove(argument);
    }

    public CommandArgument getArgument(String id) {
        for (CommandArgument argument : this.arguments) {
            String name = argument.getName();
            if (name.equalsIgnoreCase(id) || Arrays.asList(argument.getAliases()).contains(id.toLowerCase())) {
                return argument;
            }
        }
        return null;
    }

    public String getLabel() {
        return this.label;
    }

    public List<CommandArgument> getArguments() {
        return ImmutableList.copyOf(this.arguments);
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] arguments) {
        if (arguments.length < 1) {
            sender.sendMessage(ChatColor.DARK_GRAY + BukkitUtil.STRAIGHT_LINE_DEFAULT.substring(0, 45));
            sender.sendMessage(CC.GOLD + CC.BOLD + WordUtils.capitalizeFully(command.getName()) + " Help");
            for (CommandArgument argument : this.arguments) {
                String permission = argument.getPermission();
                if (permission == null || sender.hasPermission(permission)) {
                    ClickEvent clickEvent = new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, argument.getUsage(label));
                    HoverEvent hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText(CC.YELLOW + "Click to run " + CC.GRAY + argument.getUsage(label)));
                    BaseComponent[] components = new ComponentBuilder(argument.getUsage(command.getName())).color(CC.fromBukkit(ChatColor.YELLOW)).event(clickEvent).event(hoverEvent).append(" - " + argument.getDescription()).event(clickEvent).event(hoverEvent).create();
                    if(sender instanceof Player){
                        ((Player)sender).spigot().sendMessage(components);
                    } else{
                        sender.sendMessage(BaseComponent.toLegacyText(components));
                    }
                }
            }
            sender.sendMessage(ChatColor.DARK_GRAY + BukkitUtil.STRAIGHT_LINE_DEFAULT.substring(0, 45));
            return true;
        }
        CommandArgument commandArgument = this.getArgument(arguments[0]);
        String permission = (commandArgument == null) ? null : commandArgument.getPermission();
        if (commandArgument == null || (permission != null && !sender.hasPermission(permission))) {
            sender.sendMessage(ChatColor.RED + WordUtils.capitalizeFully(this.label) + " sub-command " + arguments[0] + " not found.");
            return true;
        }
        commandArgument.onCommand(sender, command, label, arguments);
        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> results = new ArrayList<String>();
        if (args.length < 2) {
            for (CommandArgument argument : this.arguments) {
                String permission = argument.getPermission();
                if (permission == null || sender.hasPermission(permission)) {
                    results.add(argument.getName());
                }
            }
        } else {
            CommandArgument argument2 = this.getArgument(args[0]);
            if (argument2 == null) {
                return results;
            }
            String permission2 = argument2.getPermission();
            if (permission2 == null || sender.hasPermission(permission2)) {
                results = argument2.onTabComplete(sender, command, label, args);
                if (results == null) {
                    return null;
                }
            }
        }
        return BukkitUtil.getCompletions(args, results);
    }
}
