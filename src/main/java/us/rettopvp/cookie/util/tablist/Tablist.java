package us.rettopvp.cookie.util.tablist;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import lombok.Getter;
import net.minecraft.util.com.google.common.collect.Table;
import net.minecraft.util.org.apache.commons.lang3.StringUtils;

public class Tablist {

	public static final String[] TAB_NAMES;

	@Getter
	private final ClientVersion version;

	@Getter
	private final Player player;

	@Getter
	private boolean initiated;

	private Scoreboard scoreboard;

	public Tablist(Player player) {
		this.player = player;
		this.version = ClientVersion.getVersion(player);
		Scoreboard scoreboard = player.getScoreboard();
		if (scoreboard == Bukkit.getScoreboardManager().getMainScoreboard()) {
			scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
		}
		player.setScoreboard(scoreboard);
		this.scoreboard = scoreboard;
		addFakePlayers();
		update();
	}

	public void sendPacket(Player player, Object packet) {
		Object handle = ReflectionConstants.GET_HANDLE_METHOD.invoke(player);
		Object connection = ReflectionConstants.PLAYER_CONNECTION.get(handle);
		ReflectionConstants.SEND_PACKET.invoke(connection, packet);
	}

	public Tablist update() {
		TablistManager manager = TablistManager.INSTANCE;
		if (!initiated || manager == null) return this;
		Table<Integer, Integer, String> entries = manager.getSupplier().getEntries(player);
		for (int i = 0; i < 60; i ++) {
			int x = i % 3;
			int y = i / 3;
			String text = entries.get(x, y);
			if (text == null) {
				text = "";
			}
			String name = TAB_NAMES[i];
			Team team = scoreboard.getTeam(name);
			if (team == null) {
				team = scoreboard.registerNewTeam(name);
			}
			if (!team.hasEntry(name)) {
				team.addEntry(name);
			}
			String prefix = "", suffix = "";
			if (text.length() < 17) {
				prefix = text;
			} else {
				String left = text.substring(0, 16), right = text.substring(16, text.length());
				if (left.endsWith("§")) {
					left = left.substring(0, left.length() - 1);
				}
				String last = ChatColor.getLastColors(left);
				right = last + right;
				prefix = left;
				suffix = StringUtils.left(right, 16);
			}
			team.setPrefix(prefix);
			team.setSuffix(suffix);
		}
		return this;
	}

	public Tablist hideRealPlayers() {
		if (!initiated)
			return this;
		Bukkit.getOnlinePlayers().forEach(other -> {
			if (!player.canSee(other))
				return;
			Object packet = ReflectionConstants.TAB_PACKET_CONSTRUCTOR.invoke();
			ReflectionConstants.TAB_PACKET_NAME.set(packet, other.getName());
			ReflectionConstants.TAB_PACKET_ACTION.set(packet, 4);
			sendPacket(player, packet);
		});
		return this;
	}

	public Tablist hideFakePlayers() {
		if (!initiated) return this;
		for (int i = 0; i < 60; i ++) {
			Object packet = ReflectionConstants.TAB_PACKET_CONSTRUCTOR.invoke();
			ReflectionConstants.TAB_PACKET_NAME.set(packet, TAB_NAMES[i]);
			ReflectionConstants.TAB_PACKET_ACTION.set(packet, 4);
			sendPacket(player, packet);
		}
		return this;
	}

	public Tablist addFakePlayers() {
		if (initiated) return this;
		for (int i = 0; i < 60; i ++) {
			Object packet = ReflectionConstants.TAB_PACKET_CONSTRUCTOR.invoke();
			ReflectionConstants.TAB_PACKET_NAME.set(packet, TAB_NAMES[i]);
			ReflectionConstants.TAB_PACKET_ACTION.set(packet, 0);
			sendPacket(player, packet);
		}
		initiated = true;
		return this;
	}

	public void clear() {
		player.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
		scoreboard.getTeams().forEach(team -> team.unregister());
		scoreboard = null;
		System.gc();
	}

	static {
		TAB_NAMES = new String[80];
		for (int i = 0; i < 60; i ++) {
			int x = i % 3;
			int y = i / 3;
			String name = (x > 9 ? ("§" + String.valueOf(x).toCharArray()[0] + "§" + String.valueOf(x).toCharArray()[1]) : "§0§" + x) + (y > 9 ? ("§" + String.valueOf(y).toCharArray()[0] + "§" + String.valueOf(y).toCharArray()[1]) : "§0§" + String.valueOf(y).toCharArray()[0]);
			TAB_NAMES[i] = name;
		}
	}

}
