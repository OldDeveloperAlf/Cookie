package us.rettopvp.cookie.util.menu.pagination;

import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.meta.ItemMeta;

import us.rettopvp.cookie.util.menu.Button;

import java.util.List;
import java.util.Arrays;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.entity.Player;

public class PageButton extends Button {
	
    private int mod;
    private PaginatedMenu menu;
    
    @Override
    public ItemStack getButtonItem(Player player) {
        ItemStack itemStack = new ItemStack(Material.ARROW);
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (this.hasNext(player)) {
            itemMeta.setDisplayName((this.mod > 0) ? (ChatColor.GREEN + "Next page") : (ChatColor.RED + "Previous page"));
        }
        else {
            itemMeta.setDisplayName(ChatColor.RED + ((this.mod > 0) ? "Last page" : "First page"));
        }
        itemMeta.setLore((List)Arrays.asList(ChatColor.GRAY + "Click here to", ChatColor.GRAY + "jump to a page"));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
    
    @Override
    public void clicked(Player player, ClickType clickType) {
        if (clickType == ClickType.RIGHT) {
            new ViewAllPagesMenu(this.menu).openMenu(player);
            Button.playNeutral(player);
        }
        else if (this.hasNext(player)) {
            this.menu.modPage(player, this.mod);
            Button.playNeutral(player);
        }
        else {
            Button.playFail(player);
        }
    }
    
    private boolean hasNext(Player player) {
        int pg = this.menu.getPage() + this.mod;
        return pg > 0 && this.menu.getPages(player) >= pg;
    }
    
    public PageButton(int mod, PaginatedMenu menu) {
        this.mod = mod;
        this.menu = menu;
    }
}
