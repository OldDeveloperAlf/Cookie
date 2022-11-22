package us.rettopvp.cookie.util.scoreboard;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import us.rettopvp.cookie.util.scoreboard.adapter.BoardAdapter;
import us.rettopvp.cookie.util.scoreboard.board.Board;
import us.rettopvp.cookie.util.scoreboard.entry.BoardEntry;

public class BoardManager extends BukkitRunnable {
	
    private JavaPlugin plugin;
    private Map<UUID, Board> playerBoards;
    private BoardAdapter adapter;
    
    public BoardManager(JavaPlugin plugin, BoardAdapter adapter) {
        this.playerBoards = new HashMap<UUID, Board>();
        this.plugin = plugin;
        this.adapter = adapter;
    }
    
    public void run() {
        this.adapter.preLoop();
        for (Player player : this.plugin.getServer().getOnlinePlayers()) {
            Board board = this.playerBoards.get(player.getUniqueId());
            if (board == null) {
                continue;
            }
            try {
                Scoreboard scoreboard = board.getScoreboard();
                List<String> scores = this.adapter.getScoreboard(player, board);
                if (scores != null) {
                    Collections.reverse(scores);
                    Objective objective = board.getObjective();
                    if (!objective.getDisplayName().equals(this.adapter.getTitle(player))) {
                        objective.setDisplayName(this.adapter.getTitle(player));
                    }
                    if (scores.isEmpty()) {
                        Iterator<BoardEntry> iter = board.getEntries().iterator();
                        while (iter.hasNext()) {
                            BoardEntry boardEntry = iter.next();
                            boardEntry.remove();
                            iter.remove();
                        }
                        continue;
                    }
                    int i = 0;
                Label_0211:
                    while (i < scores.size()) {
                        String text = scores.get(i);
                        int position = i + 1;
                        while (true) {
                            for (BoardEntry boardEntry2 : new LinkedList<BoardEntry>(board.getEntries())) {
                                Score score = objective.getScore(boardEntry2.getKey());
                                if (score != null && boardEntry2.getText().equals(text) && score.getScore() == position) {
                                    ++i;
                                    continue Label_0211;
                                }
                            }
                            Iterator<BoardEntry> iter2 = board.getEntries().iterator();
                            while (iter2.hasNext()) {
                                BoardEntry boardEntry2 = iter2.next();
                                int entryPosition = scoreboard.getObjective(DisplaySlot.SIDEBAR).getScore(boardEntry2.getKey()).getScore();
                                if (entryPosition > scores.size()) {
                                    boardEntry2.remove();
                                    iter2.remove();
                                }
                            }
                            int positionToSearch = position - 1;
                            BoardEntry entry = board.getByPosition(positionToSearch);
                            if (entry == null) {
                                new BoardEntry(board, text).send(position);
                            }
                            else {
                                entry.setText(text).setup().send(position);
                            }
                            if (board.getEntries().size() > scores.size()) {
                                iter2 = board.getEntries().iterator();
                                while (iter2.hasNext()) {
                                    BoardEntry boardEntry3 = iter2.next();
                                    if (!scores.contains(boardEntry3.getText()) || Collections.frequency(board.getBoardEntriesFormatted(), boardEntry3.getText()) > 1) {
                                        boardEntry3.remove();
                                        iter2.remove();
                                    }
                                }
                            }
                            continue;
                        }
                    }
                }
                else if (!board.getEntries().isEmpty()) {
                    board.getEntries().forEach(BoardEntry::remove);
                    board.getEntries().clear();
                }
                this.adapter.onScoreboardCreate(player, scoreboard);
                player.setScoreboard(scoreboard);
            }
            catch (Exception e) {
                e.printStackTrace();
                this.plugin.getLogger().severe("Something went wrong while updating " + player.getName() + "'s scoreboard " + board + " - " + board.getAdapter() + ")");
            }
        }
    }
    
    public JavaPlugin getPlugin() {
        return this.plugin;
    }
    
    public Map<UUID, Board> getPlayerBoards() {
        return this.playerBoards;
    }
    
    public BoardAdapter getAdapter() {
        return this.adapter;
    }
}
