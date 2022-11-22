package us.rettopvp.cookie.cosmetic.menu;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import us.rettopvp.cookie.cosmetic.type.CosmeticEditType;
import us.rettopvp.cookie.util.builder.ItemMaker;
import us.rettopvp.cookie.util.menu.Button;
import us.rettopvp.cookie.util.menu.Menu;
import us.rettopvp.cookie.util.message.color.CC;

public class CosmeticMenu extends Menu {
	
	@Override
	public int getSize() {
		return 9 * 5;
	}
	
    @Override
    public String getTitle(Player player) {
        return CC.GOLD + "Choose a Cosmetic";
    }
    
    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<Integer, Button>();
        buttons.put(12 -1, new SelectTagButton());
        buttons.put(16 -1, new SelectColorButton());
        buttons.put(32 -1, new ExitButton());
        return buttons;
    }
    
    private class SelectTagButton extends Button {

		@Override
		public ItemStack getButtonItem(Player player) {
			return new ItemMaker(Material.NAME_TAG).setTitle("&e&lSelect a Tags").setLore(" ", "&dClick here to open tags menu.").build();
		}
		
		@Override
		public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
			new EditTagMenu().openMenu(player);
		}
    	
    }
    
    private class SelectColorButton extends Button {
    	
    	@Override
		public ItemStack getButtonItem(Player player) {
			return new ItemMaker(Material.PAPER).setTitle("&e&lSelect a Colors").setLore(" ", "&dClick here to open colors menu.").build();
		}
        
        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            new SelectColorMenu(CosmeticEditType.COLOR).openMenu(player);
        }
    }
    
    private class ExitButton extends Button {
    	
    	@Override
		public ItemStack getButtonItem(Player player) {
			return new ItemMaker(Material.NETHER_STAR).setTitle("&e&lExit").setLore(" ", "&eClick here to exit of cosmetics.").build();
		}
        
        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            player.closeInventory();
        }
    }
}
