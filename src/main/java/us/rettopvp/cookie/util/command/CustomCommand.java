package us.rettopvp.cookie.util.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.WordUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import com.google.common.collect.ImmutableList;

import lombok.Getter;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import us.rettopvp.cookie.util.BukkitUtil;
import us.rettopvp.cookie.util.message.Messager;
import us.rettopvp.cookie.util.message.color.CC;

@Getter
public abstract class CustomCommand implements CommandExecutor, TabCompleter {

    private String name;
    private String description;
    private String[] aliases;
    private List<CustomCommandArgument> arguments = new ArrayList<>();

    public CustomCommand(String name) {
        this(name, null);
    }

    public CustomCommand(String name, String description) {
        this(name, description, ArrayUtils.EMPTY_STRING_ARRAY);
    }

    public CustomCommand(String name, String description, String... aliases) {
        this.name = name;
        this.description = description;
        this.aliases = Arrays.copyOf(aliases, aliases.length);
    }

    @Override
    public boolean onCommand(CommandSender commandsender, Command command, String label, String[] arguments) {
        if (arguments.length == 0) {
            commandsender.sendMessage(ChatColor.RED + "Available sub-command(s) for '" + label + "' are " + ChatColor.GRAY +
                    this.arguments.stream().filter(argument -> {
                                String permission = argument.getPermission((PluginCommand) command);
                                return permission == null || commandsender.hasPermission(permission);
                            }
                    ).map(CustomCommandArgument::getName).collect(Collectors.joining(ChatColor.GRAY + ", ")) + ChatColor.RED + ".");
            commandsender.sendMessage(ChatColor.RED + "You must specify a sub-command.");
            return true;
        }
        CustomCommandArgument argument = getArgument(arguments[0]);
        String permission = argument == null ? null : argument.getPermission((PluginCommand) command);
        if (argument == null || permission != null && !commandsender.hasPermission(permission)) {
            commandsender.sendMessage(ChatColor.RED + WordUtils.capitalizeFully(name) + " sub-command '" + arguments[0] + "' not found.");
        } else {
            argument.onCommand(commandsender, command, label, arguments);
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandsender, Command command, String label, String[] arguments) {
        List<String> results = new ArrayList<>();
        if (arguments.length < 2) {
            for (CustomCommandArgument argument : this.arguments) {
                String permission = argument.getPermission((PluginCommand) command);
                if (permission == null || commandsender.hasPermission(permission)) {
                    results.add(argument.getName());
                }
            }
            if (results.isEmpty()) {
                return null;
            }
        } else {
            CustomCommandArgument argument = getArgument(arguments[0]);
            if (argument == null) {
                return results;
            }
            String permission = argument.getPermission((PluginCommand) command);
            if (permission == null || commandsender.hasPermission(permission)) {
                results = argument.onTabComplete(commandsender, command, label, arguments);
                if (results == null) {
                    return null;
                }
            }
        }
        return BukkitUtil.getCompletions(arguments, results);
    }

    public List<CustomCommandArgument> getArguments() {
        return ImmutableList.copyOf(arguments);
    }

    public void addArgument(CustomCommandArgument argument) {
        arguments.add(argument);
    }

    public void removeArgument(CustomCommandArgument argument) {
        arguments.remove(argument);
    }

    public CustomCommandArgument getArgument(String name) {
        return arguments.stream().filter(argument -> argument.getName().equalsIgnoreCase(name) || Arrays.asList(argument.getAliases()).contains(name.toLowerCase())).findFirst().orElse(null);
    }

    public void setUsage(CommandSender commandsender, String usage) {
        ClickEvent clickEvent = new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/" + usage);
        HoverEvent hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText(CC.translate("&6Description&7: &f" + this.description + "\n" + "&6Usage&7: &f/" + usage + "\n\n" + "&7Click to run command.")));
        BaseComponent[] components = new ComponentBuilder(CC.translate("&cInvalid usage: /" + usage)).event(clickEvent).event(hoverEvent).create();
        if(commandsender instanceof Player) {
        	((Player)commandsender).spigot().sendMessage(components);
        } else {
        	commandsender.sendMessage(BaseComponent.toLegacyText(components) + CC.GRAY + " - " + CC.WHITE + this.description);
        }
    }
    
    public void setInvalidSubcommand(CommandSender commandsender, String argument) {
    	Messager.sendMessage(commandsender, "&cInvalid '" + argument +  "' sub-command.");
    }
    
    public void playerNotFound(CommandSender commandsender, String argument) {
    	Messager.sendMessage(commandsender, "&cPlayer named '" + argument + "' could not be found.");
    }
    
    public void noConsole(CommandSender commandsender) {
    	Messager.sendMessage(commandsender, "&cYou can not execute this command from the console.");
    }
    
    public String[] getAliases() {
		return aliases;
	}
    
    public void setAliases(String[] aliases) {
		this.aliases = aliases;
	}
    
    public String getDescription() {
		return description;
	}
    
    public void setDescription(String description) {
		this.description = description;
	}
    
    public String getName() {
		return name;
	}
}