package us.rettopvp.cookie.util.scoreboard.adapter;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;

import us.rettopvp.cookie.util.scoreboard.board.Board;

public interface BoardAdapter {

	String getTitle(Player player);

	List<String> getScoreboard(Player player, Board board);

	long getInterval();

	void onScoreboardCreate(Player player, Scoreboard board);

	void preLoop();

}