package us.rettopvp.cookie.util.nametag;

import java.util.Iterator;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import net.minecraft.util.org.apache.commons.lang3.StringEscapeUtils;
import us.rettopvp.cookie.util.message.color.CC;

public class NameTags {
	
    private static String PREFIX = "nt_team_";
    private static ChatColor[] COLORS;
    
    static {
        COLORS = new ChatColor[] { ChatColor.RED, ChatColor.GREEN, ChatColor.BLUE, ChatColor.AQUA, ChatColor.LIGHT_PURPLE, ChatColor.DARK_PURPLE, ChatColor.GOLD, ChatColor.YELLOW };
    }
    
    public static void setup(Player player) {
        Scoreboard scoreboard = player.getScoreboard();
        if (scoreboard.equals(Bukkit.getServer().getScoreboardManager().getMainScoreboard())) {
            scoreboard = Bukkit.getServer().getScoreboardManager().getNewScoreboard();
        }
        for (ChatColor color : NameTags.COLORS) {
            String teamName = getTeamName(color);
            Team team = scoreboard.getTeam(teamName);
            if (team == null) {
                team = scoreboard.registerNewTeam(teamName);
            }
            team.setPrefix(color.toString());
            Iterator<String> entryIterator = team.getEntries().iterator();
            while (entryIterator.hasNext()) {
                entryIterator.remove();
            }
        }
        player.setScoreboard(scoreboard);
    }
    
    public static void color(Player player, Player other, ChatColor color, boolean showHealth) {
        if (player.equals(other)) {
            return;
        }
        Team team = player.getScoreboard().getTeam(getTeamName(color));
        if (team == null) {
            team = player.getScoreboard().registerNewTeam(getTeamName(color));
            team.setPrefix(color.toString());
        }
        if (!team.hasEntry(other.getName())) {
            reset(player, other);
            team.addEntry(other.getName());
            if (showHealth) {
                Objective objective = player.getScoreboard().getObjective(DisplaySlot.BELOW_NAME);
                if (objective == null) {
                    objective = player.getScoreboard().registerNewObjective("showhealth", "health");
                }
                objective.setDisplaySlot(DisplaySlot.BELOW_NAME);
                objective.setDisplayName(CC.RED + StringEscapeUtils.unescapeJava("\u2764"));
                objective.getScore(other.getName()).setScore((int)Math.floor(other.getHealth() / 2.0));
            }
        }
    }
    
    public static void reset(Player player, Player other) {
        if (player != null && other != null && !player.equals(other)) {
            Objective objective = player.getScoreboard().getObjective(DisplaySlot.BELOW_NAME);
            if (objective != null) {
                objective.unregister();
            }
            for (Team team : player.getScoreboard().getTeams()) {
                team.removeEntry(other.getName());
            }
        }
    }
    
    private static String getTeamName(ChatColor color) {
        return PREFIX + color.ordinal();
    }
}
