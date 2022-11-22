package us.rettopvp.cookie.util.menu.pagination;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;

import us.rettopvp.cookie.util.menu.Button;
import us.rettopvp.cookie.util.menu.Menu;

public abstract class PaginatedMenu extends Menu {
	
    private int page;
    
    public PaginatedMenu() {
        this.page = 1;
        this.setUpdateAfterClick(false);
    }
    
    @Override
    public String getTitle(Player player) {
        return this.getPrePaginatedTitle(player) + " &7(page " + this.page + ")";
    }
    
    public void modPage(Player player, int mod) {
        this.page += mod;
        this.getButtons().clear();
        this.openMenu(player);
    }
    
    public int getPages(Player player) {
        int buttonAmount = this.getAllPagesButtons(player).size();
        if (buttonAmount == 0) {
            return 1;
        }
        return (int)Math.ceil(buttonAmount / this.getMaxItemsPerPage(player));
    }
    
    @Override
    public Map<Integer, Button> getButtons(Player player) {
        int minIndex = (this.page - 1) * this.getMaxItemsPerPage(player);
        int maxIndex = this.page * this.getMaxItemsPerPage(player);
        HashMap<Integer, Button> buttons = new HashMap<Integer, Button>();
        buttons.put(0, new PageButton(-1, this));
        buttons.put(8, new PageButton(1, this));
        for (Map.Entry<Integer, Button> entry : this.getAllPagesButtons(player).entrySet()) {
            int ind = entry.getKey();
            if (ind >= minIndex && ind < maxIndex) {
                ind -= this.getMaxItemsPerPage(player) * (this.page - 1) - 9;
                buttons.put(ind, entry.getValue());
            }
        }
        Map<Integer, Button> global = this.getGlobalButtons(player);
        if (global != null) {
            for (Map.Entry<Integer, Button> gent : global.entrySet()) {
                buttons.put(gent.getKey(), gent.getValue());
            }
        }
        return buttons;
    }
    
    public int getMaxItemsPerPage(Player player) {
        return 18;
    }
    
    public Map<Integer, Button> getGlobalButtons(Player player) {
        return null;
    }
    
    public abstract String getPrePaginatedTitle(Player player);
    
    public abstract Map<Integer, Button> getAllPagesButtons(Player player);
    
    public int getPage() {
        return this.page;
    }
}
