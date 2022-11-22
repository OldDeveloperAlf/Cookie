package us.rettopvp.cookie.util.menu.button;

import org.bukkit.Sound;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.meta.ItemMeta;

import us.rettopvp.cookie.util.callback.TypeCallback;
import us.rettopvp.cookie.util.menu.Button;
import us.rettopvp.cookie.util.menu.Menu;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.entity.Player;

public class ConfirmationButton extends Button {
	
    private boolean confirm;
    private TypeCallback<Boolean> callback;
    private boolean closeAfterResponse;
    
    @Override
    public ItemStack getButtonItem(Player player) {
        ItemStack itemStack = new ItemStack(Material.WOOL, 1, (short)(this.confirm ? 5 : 14));
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(this.confirm ? (ChatColor.GREEN + "Confirm") : (ChatColor.RED + "Cancel"));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
    
    @Override
    public void clicked(Player player, ClickType clickType) {
        if (this.confirm) {
            player.playSound(player.getLocation(), Sound.NOTE_PIANO, 20.0f, 0.1f);
        }
        else {
            player.playSound(player.getLocation(), Sound.DIG_GRAVEL, 20.0f, 0.1f);
        }
        if (this.closeAfterResponse) {
            Menu menu = Menu.currentlyOpenedMenus.get(player.getName());
            if (menu != null) {
                menu.setClosedByMenu(true);
            }
            player.closeInventory();
        }
        this.callback.callback(this.confirm);
    }
    
    public ConfirmationButton(boolean confirm, TypeCallback<Boolean> callback, boolean closeAfterResponse) {
        this.confirm = confirm;
        this.callback = callback;
        this.closeAfterResponse = closeAfterResponse;
    }
}
