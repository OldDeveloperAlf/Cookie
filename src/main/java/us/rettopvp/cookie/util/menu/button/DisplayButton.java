package us.rettopvp.cookie.util.menu.button;

import org.bukkit.event.inventory.ClickType;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import us.rettopvp.cookie.util.menu.Button;

public class DisplayButton extends Button {
	
    private ItemStack itemStack;
    private boolean cancel;
    
    @Override
    public ItemStack getButtonItem(Player player) {
        if (this.itemStack == null) {
            return new ItemStack(Material.AIR);
        }
        return this.itemStack;
    }
    
    @Override
    public boolean shouldCancel(Player player, ClickType clickType) {
        return this.cancel;
    }
    
    public DisplayButton(ItemStack itemStack, boolean cancel) {
        this.itemStack = itemStack;
        this.cancel = cancel;
    }
    
    public ItemStack getItemStack() {
        return this.itemStack;
    }
    
    public boolean isCancel() {
        return this.cancel;
    }
    
    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }
    
    public void setCancel(boolean cancel) {
        this.cancel = cancel;
    }
}
