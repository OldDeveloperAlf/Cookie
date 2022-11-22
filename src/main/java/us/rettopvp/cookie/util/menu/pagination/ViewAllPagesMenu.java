package us.rettopvp.cookie.util.menu.pagination;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.entity.Player;
import lombok.NonNull;
import us.rettopvp.cookie.util.menu.Button;
import us.rettopvp.cookie.util.menu.Menu;
import us.rettopvp.cookie.util.menu.button.BackButton;

public class ViewAllPagesMenu extends Menu {
	
    @NonNull
    PaginatedMenu menu;
    
    @Override
    public String getTitle(Player player) {
        return "Jump to page";
    }
    
    @Override
    public Map<Integer, Button> getButtons(Player player) {
        HashMap<Integer, Button> buttons = new HashMap<Integer, Button>();
        buttons.put(0, new BackButton(this.menu));
        int index = 10;
        for (int i = 1; i <= this.menu.getPages(player); ++i) {
            buttons.put(index++, new JumpToPageButton(i, this.menu, this.menu.getPage() == i));
            if ((index - 8) % 9 == 0) {
                index += 2;
            }
        }
        return buttons;
    }
    
    @Override
    public boolean isAutoUpdate() {
        return true;
    }
    
    public ViewAllPagesMenu(@NonNull PaginatedMenu menu) {
        if (menu == null) {
            throw new NullPointerException("menu is marked @NonNull but is null");
        }
        this.menu = menu;
    }
    
    @NonNull
    public PaginatedMenu getMenu() {
        return this.menu;
    }
}
