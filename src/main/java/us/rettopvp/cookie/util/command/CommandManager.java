package us.rettopvp.cookie.util.command;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandMap;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.SimplePluginManager;

public class CommandManager {
	
    private static PluginCommand getCommand(String name, Plugin plugin) {
        PluginCommand command = null;
        try {
            Constructor<PluginCommand> constructor = PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class);
            constructor.setAccessible(true);
            command = constructor.newInstance(name, plugin);
        } catch (SecurityException | IllegalArgumentException | IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return command;
    }
    
    private static CommandMap getCommandMap() {
        CommandMap commandMap = null;
        try {
            if (Bukkit.getPluginManager() instanceof SimplePluginManager) {
                Field field = SimplePluginManager.class.getDeclaredField("commandMap");
                field.setAccessible(true);
                commandMap = (CommandMap) field.get(Bukkit.getPluginManager());
            }
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return commandMap;
    }

    public void registerCommand(CustomCommand customCommand, Plugin plugin) {
        PluginCommand command = getCommand(customCommand.getName(), plugin);
        command.setPermissionMessage(ChatColor.RED + "I'm sorry but you do not have permission to execute this command, contact to administrator.");
        if (customCommand.getDescription() != null) {
            command.setDescription(customCommand.getDescription());
        }
        command.setAliases(Arrays.asList(customCommand.getAliases()));
        command.setExecutor(customCommand);
        command.setTabCompleter(customCommand);
        getCommandMap().register(customCommand.getName(), command);
    }
}
