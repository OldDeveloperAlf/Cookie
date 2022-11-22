package us.rettopvp.cookie.util.command.base;

import org.apache.commons.lang.ArrayUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public abstract class CommandArgument {
	
    private String name;
    protected boolean isPlayerOnly;
    protected String description;
    protected String permission;
    protected String[] aliases;

    public CommandArgument(String name, String description) {
        this(name, description, (String) null);
    }

    public CommandArgument(String name, String description, String permission) {
        this(name, description, permission, ArrayUtils.EMPTY_STRING_ARRAY);
    }

    public CommandArgument(String name, String description, String[] aliases) {
        this(name, description, null, aliases);
    }

    public CommandArgument(String name, String description, String permission, String[] aliases) {
        this.isPlayerOnly = false;
        this.name = name;
        this.description = description;
        this.permission = permission;
        this.aliases = Arrays.copyOf(aliases, aliases.length);
    }

    public String getName() {
        return this.name;
    }

    public boolean isPlayerOnly() {
        return this.isPlayerOnly;
    }

    public String getDescription() {
        return this.description;
    }

    public String getPermission() {
        return this.permission;
    }

    public String[] getAliases() {
        if (this.aliases == null) {
            this.aliases = ArrayUtils.EMPTY_STRING_ARRAY;
        }
        return Arrays.copyOf(this.aliases, this.aliases.length);
    }

    public abstract String getUsage(String label);

    public abstract boolean onCommand(CommandSender commandSender, Command command, String label, String[] args);

    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return Collections.emptyList();
    }
}
