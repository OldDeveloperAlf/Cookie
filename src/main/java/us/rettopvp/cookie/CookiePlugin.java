package us.rettopvp.cookie;

import java.lang.reflect.Type;
import java.util.List;
import java.util.stream.Stream;

import org.bukkit.Bukkit;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;

import net.minecraft.util.com.google.gson.Gson;
import net.minecraft.util.com.google.gson.reflect.TypeToken;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import us.rettopvp.cookie.chat.Chat;
import us.rettopvp.cookie.chat.command.BroadcastCommand;
import us.rettopvp.cookie.chat.command.BroadcastRawCommand;
import us.rettopvp.cookie.chat.command.StaffChatCommand;
import us.rettopvp.cookie.chat.listener.ChatListener;
import us.rettopvp.cookie.handle.Handle;
import us.rettopvp.cookie.handle.packet.listener.PacketListener;
import us.rettopvp.cookie.network.NetworkPacketListener;
import us.rettopvp.cookie.network.packet.PacketAddGrant;
import us.rettopvp.cookie.network.packet.PacketBroadcast;
import us.rettopvp.cookie.network.packet.PacketBroadcastPunishment;
import us.rettopvp.cookie.network.packet.PacketBroadcastRaw;
import us.rettopvp.cookie.network.packet.PacketDeleteGrant;
import us.rettopvp.cookie.network.packet.PacketDeleteRank;
import us.rettopvp.cookie.network.packet.PacketRefreshRank;
import us.rettopvp.cookie.network.packet.PacketStaffChat;
import us.rettopvp.cookie.network.packet.PacketStaffJoinNetwork;
import us.rettopvp.cookie.profile.Profile;
import us.rettopvp.cookie.profile.command.CosmeticCommand;
import us.rettopvp.cookie.profile.command.NicknameCommand;
import us.rettopvp.cookie.profile.command.ProfileDebugCommand;
import us.rettopvp.cookie.profile.grant.command.GrantCommand;
import us.rettopvp.cookie.profile.grant.command.GrantsCommand;
import us.rettopvp.cookie.profile.grant.listener.GrantListener;
import us.rettopvp.cookie.profile.listener.ProfileListener;
import us.rettopvp.cookie.profile.punishment.command.BanCommand;
import us.rettopvp.cookie.profile.punishment.command.BlacklistCommand;
import us.rettopvp.cookie.profile.punishment.command.KickCommand;
import us.rettopvp.cookie.profile.punishment.command.MuteCommand;
import us.rettopvp.cookie.profile.punishment.command.TempBanCommand;
import us.rettopvp.cookie.profile.punishment.command.WarnCommand;
import us.rettopvp.cookie.profile.punishment.command.check.AltsCommand;
import us.rettopvp.cookie.profile.punishment.command.check.CheckCommand;
import us.rettopvp.cookie.profile.punishment.command.undo.UnBanCommand;
import us.rettopvp.cookie.profile.punishment.command.undo.UnMuteCommand;
import us.rettopvp.cookie.profile.punishment.listener.PunishmentListener;
import us.rettopvp.cookie.rank.AutomaticRank;
import us.rettopvp.cookie.rank.Rank;
import us.rettopvp.cookie.rank.command.AutomaticRankCommand;
import us.rettopvp.cookie.rank.command.RankCommand;
import us.rettopvp.cookie.util.PersistableLocation;
import us.rettopvp.cookie.util.command.CommandManager;
import us.rettopvp.cookie.util.command.CustomCommand;
import us.rettopvp.cookie.util.cuboid.Cuboid;
import us.rettopvp.cookie.util.cuboid.NamedCuboid;
import us.rettopvp.cookie.util.file.FileConfig;
import us.rettopvp.cookie.util.file.cursor.ConfigCursor;
import us.rettopvp.cookie.util.handler.SignHandler;
import us.rettopvp.cookie.util.menu.MenuListener;
import us.rettopvp.cookie.util.scoreboard.BoardManager;
import us.rettopvp.cookie.uuid.UUIDCache;

public class CookiePlugin extends JavaPlugin {
	
	/* Gson, Util's */
    public static Gson GSON;
    public static Type LIST_STRING_TYPE;
    
    /* Configurations */
    private FileConfig rootConfig;
    
    /* Database Mongo, Redis*/
    private MongoDatabase mongoDatabase;
    private JedisPool jedisPool;

    /* Managers */
    private UUIDCache uuidCache;    
    private Chat chat;
    private Handle handle;
    private CommandManager command;
    private SignHandler signHandler;
    private AutomaticRank automaticRank;
    private BoardManager boardManager;
    
    static {
        CookiePlugin.GSON = new Gson();
        CookiePlugin.LIST_STRING_TYPE = new TypeToken<List<String>>() { }.getType();
    }
    
    public void onEnable() {        
    	this.configurations();
    	this.serialization();
    	
    	this.databases();
    	this.managers();
    	
    	this.packets();
    	this.packetsListeners();
    	
    	this.listeners();
    	this.commands();
    	
    	this.others();
    	this.runnables();
    }
    
    public void configurations() {
    	this.rootConfig = new FileConfig("config.yml");
    }
    
    private void serialization() {
    	ConfigurationSerialization.registerClass(PersistableLocation.class);
    	ConfigurationSerialization.registerClass(Cuboid.class);
        ConfigurationSerialization.registerClass(NamedCuboid.class);
    }
    
    public void databases() {
    	ConfigCursor cursorMongo = new ConfigCursor(rootConfig, "database.mongo");
    	if(cursorMongo.getBoolean("authentication.enabled")) {
    		this.mongoDatabase = new MongoClient(new ServerAddress(cursorMongo.getString("host"), cursorMongo.getInt("port")), MongoCredential.createCredential(cursorMongo.getString("authentication.username"), "admin", cursorMongo.getString("authentication.password").toCharArray()), MongoClientOptions.builder().build()).getDatabase("cookie");
    	} else {
    		this.mongoDatabase = new MongoClient(cursorMongo.getString("host"), cursorMongo.getInt("port")).getDatabase("cookie");
    	}
    	ConfigCursor cursorRedis = new ConfigCursor(rootConfig, "database.redis");
    	this.jedisPool = new JedisPool(cursorRedis.getString("host"), cursorRedis.getInt("port"));
    	if(cursorRedis.getBoolean("authentication.enabled")) {
    		try (Jedis jedis = this.jedisPool.getResource()) {
    			jedis.auth(cursorRedis.getString("authentication.password"));
    		}
    	}
    	
        this.handle = new Handle("cookie", cursorRedis.getString("host"), cursorRedis.getInt("port"), cursorRedis.getBoolean("authentication.enabled") ? cursorRedis.getString("authentication.password") : null);
    }
    
    public void managers() {
    	this.uuidCache = new UUIDCache(this);
        this.chat = new Chat(this);
        this.command = new CommandManager();
        this.signHandler = new SignHandler(this);
        this.automaticRank = new AutomaticRank(this);   
    }
    
    private void packets() {
    	this.reigisterPackets(
    			PacketAddGrant.class,
    			PacketBroadcast.class,
    			PacketBroadcastPunishment.class,
    			PacketDeleteGrant.class,
    			PacketDeleteRank.class,
    			PacketRefreshRank.class,
    			PacketStaffChat.class,
    			PacketStaffJoinNetwork.class,
    			PacketBroadcastRaw.class);
    }
    
    public void reigisterPackets(Class<?>... packets) {
    	Stream.of(packets).forEach(packet -> this.handle.registerPacket(packet));
    }
    
    private void packetsListeners() {
    	this.registerPacketListeners(
    			new NetworkPacketListener(this));
    }
    
    public void registerPacketListeners(PacketListener... packetlisteners) {
    	Stream.of(packetlisteners).forEach(packetlistener -> this.handle.registerListener(packetlistener));
    }
    
    private void listeners() {
    	this.registerListeners(
    			new ProfileListener(this),
    			new MenuListener(this),
    			new ChatListener(this),
    			new GrantListener(this),
    			new PunishmentListener(this));
    }
    
    public void registerListeners(Listener... listeners) {
    	Stream.of(listeners).forEach(listener -> this.getServer().getPluginManager().registerEvents(listener, this));
    }
    
    private void commands() {
    	this.registerCommands(
    			new RankCommand(),
    			new GrantCommand(),
    			new GrantsCommand(),
    			new AltsCommand(),
    			new ProfileDebugCommand(),
    			new StaffChatCommand(),
    			new BanCommand(),
    			new TempBanCommand(),
    			new BlacklistCommand(),
    			new MuteCommand(),
    			new WarnCommand(),
    			new KickCommand(),
    			new UnBanCommand(),
    			new UnMuteCommand(),
    			new CheckCommand(),
    			new BroadcastCommand(),
    			new BroadcastRawCommand(),
    			new CosmeticCommand(),
    			new AutomaticRankCommand(),
    			new NicknameCommand());
    }
    
    public void registerCommands(CustomCommand... commands) {
    	Stream.of(commands).forEach(command -> this.command.registerCommand(command, this));
    }
    
    private void others() {
    	Rank.init();
    	Profile.init();
    }
    
    private void runnables() {
    	new BukkitRunnable() {
            
        	@Override
        	public void run() {
                for (Profile profile : Profile.getProfiles().values()) {
                    profile.checkGrants();
                }
            }
        }.runTaskTimerAsynchronously(this, 20L, 20L);
        
        new BukkitRunnable() {
			
			@Override
			public void run() {
				Bukkit.getOnlinePlayers().forEach(players -> {
					Profile profile = Profile.getProfiles().get(players.getUniqueId());
					profile.setupPermissionsAttachment(CookiePlugin.this, players);
				});
			}
		}.runTaskTimerAsynchronously(this, 20L, 20L);
    }
    
    public void setBoardManager(BoardManager boardManager) {
        this.boardManager = boardManager;
        long interval = this.boardManager.getAdapter().getInterval();
        this.getServer().getScheduler().runTaskTimerAsynchronously(this, this.boardManager, interval, interval);
    }
    
    public void onDisable() {
    	try {
            this.jedisPool.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.signHandler.cancelTasks(null);
    }

    public static CookiePlugin getPlugin() {
        return (CookiePlugin) JavaPlugin.getPlugin(CookiePlugin.class);
    }
    
    public FileConfig getRootConfig() {
		return this.rootConfig;
	}
   
    public MongoDatabase getMongoDatabase() {
        return this.mongoDatabase;
    }
    
    public JedisPool getJedisPool() {
        return this.jedisPool;
    }
    
    public UUIDCache getUuidCache() {
        return this.uuidCache;
    }

    public Chat getChat() {
        return this.chat;
    }
    
    public Handle getHandle() {
        return this.handle;
    }
    
    public CommandManager getCommandRegistrer() {
		return this.command;
    }
    
    public SignHandler getSignHandler() {
		return signHandler;
	}
    
    public AutomaticRank getAutomaticRank() {
		return automaticRank;
	}
}
