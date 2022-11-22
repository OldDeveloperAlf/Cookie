package us.rettopvp.cookie.util.menu.button;

import org.bukkit.event.inventory.ClickType;
import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import us.rettopvp.cookie.util.builder.ItemBuilder;
import us.rettopvp.cookie.util.menu.Button;
import us.rettopvp.cookie.util.menu.Menu;
import us.rettopvp.cookie.util.message.color.CC;

import org.bukkit.entity.Player;

public class BackButton extends Button {
	
    private Menu back;
    
    @Override
    public ItemStack getButtonItem(Player player) {
        return new ItemBuilder(Material.REDSTONE).name(CC.RED + CC.BOLD + "Back").lore(Arrays.asList(CC.RED + "Click here to return to", CC.RED + "the previous menu.")).build();
    }
    
    @Override
    public void clicked(Player player, ClickType clickType) {
        Button.playNeutral(player);
        this.back.openMenu(player);
    }
    
    public BackButton(Menu back) {
        this.back = back;
    }
}
