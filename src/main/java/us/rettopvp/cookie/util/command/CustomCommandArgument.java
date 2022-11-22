package us.rettopvp.cookie.util.command;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.ArrayUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Getter
public abstract class CustomCommandArgument {

    private String name;
    private String description;

    @Setter private boolean requiresPermission;

    private String[] aliases;

    public CustomCommandArgument(String name) {
        this(name, null);
    }

    public CustomCommandArgument(String name, String description) {
        this(name, description, ArrayUtils.EMPTY_STRING_ARRAY);
    }

    public CustomCommandArgument(String name, String description, String... aliases) {
        this.name = name;
        this.description = description;

        this.aliases = Arrays.copyOf(aliases, aliases.length);
    }

    public String getPermission(PluginCommand command) {
        return requiresPermission ? (command.getPermission() == null ? "cookie.command." + command.getName() + ".argument." + name : command.getPermission() + ".argument." + name) : null;
    }

    public abstract String getUsage(String label);

    public abstract boolean onCommand(CommandSender commandsender , Command command, String label, String[] arguments);

    public List<String> onTabComplete(CommandSender commandsender, Command command, String label, String[] arguments) {
        return Collections.emptyList();
    }
    
    public String getDescription() {
		return description;
	}
    
    public void setDescription(String description) {
		this.description = description;
	}
    
    public String[] getAliases() {
		return aliases;
	}
    
    public void setAliases(String[] aliases) {
		this.aliases = aliases;
	}
    
    public String getName() {
		return name;
	}
    
    public void setName(String name) {
		this.name = name;
	}
    
    public boolean isRequiresPermission() {
		return requiresPermission;
	}
    
    public void setRequiresPermission(boolean requiresPermission) {
		this.requiresPermission = requiresPermission;
	}
}