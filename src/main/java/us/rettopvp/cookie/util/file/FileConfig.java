package us.rettopvp.cookie.util.file;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import us.rettopvp.cookie.CookiePlugin;

@Getter
@RequiredArgsConstructor
public class FileConfig {
	
    public String name;
	public File file;
	private FileConfiguration config;
	
	public FileConfig(JavaPlugin plugin, String fileName) {
        this.file = new File(plugin.getDataFolder(), fileName);
        if (!this.file.exists()) {
            this.file.getParentFile().mkdirs();
            if (plugin.getResource(fileName) == null) {
                try {
                    this.file.createNewFile();
                } catch (IOException e) {
                    plugin.getLogger().severe("Failed to create new file " + fileName);
                }
            } else {
                plugin.saveResource(fileName, false);
            }
        }
        this.config = (FileConfiguration)YamlConfiguration.loadConfiguration(this.file);
    }
    
    public FileConfig(String name) {
        this.name = name;
        this.file = new File(CookiePlugin.getPlugin().getDataFolder(), name);
        if (!this.file.exists()) {
            this.file.getParentFile().mkdirs();
            if (CookiePlugin.getPlugin().getResource(name) == null) {
                try {
                    this.file.createNewFile();
                } catch (IOException e) {
                	CookiePlugin.getPlugin().getLogger().severe("Failed to create new file " + name);
                }
            } else {
            	CookiePlugin.getPlugin().saveResource(name, false);
            }
        }
        this.config = YamlConfiguration.loadConfiguration(this.file);
    }
    
    public FileConfig(File file, String fileName) {
        this.file = new File(file, fileName);
        if (!this.file.exists()) {
            this.file.getParentFile().mkdirs();
            if (CookiePlugin.getPlugin().getResource(fileName) == null) {
                try {
                    this.file.createNewFile();
                } catch (IOException e) {
                	CookiePlugin.getPlugin().getLogger().severe("Failed to create new file " + fileName);
                }
            } else {
            	CookiePlugin.getPlugin().saveResource(fileName, false);
            }
        }
        this.config = YamlConfiguration.loadConfiguration(this.file);
    }
    
    public FileConfiguration getConfig() {
        return this.config;
    }
    
    public void save() {
        try {
            this.getConfig().save(this.file);
            this.getConfig().load(this.file);
        } catch (IOException | InvalidConfigurationException e) {
            Bukkit.getLogger().severe("Could not save config file " + this.file.toString());
            e.printStackTrace();
        }
    }
}
