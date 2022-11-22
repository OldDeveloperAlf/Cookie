package us.rettopvp.cookie.cosmetic.menu;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import us.rettopvp.cookie.cosmetic.type.CosmeticEditType;
import us.rettopvp.cookie.profile.Profile;
import us.rettopvp.cookie.util.builder.ItemBuilder;
import us.rettopvp.cookie.util.builder.ItemMaker;
import us.rettopvp.cookie.util.menu.Button;
import us.rettopvp.cookie.util.menu.Menu;
import us.rettopvp.cookie.util.message.color.CC;

public class EditTagMenu extends Menu {
    
	@Override
	public int getSize() {
		return 9 * 6;
	}
	
    @Override
    public String getTitle(Player player) {
        return CC.GOLD + CC.BOLD + "Edit your Tag";
    }
    
    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<Integer, Button>();
        buttons.put(12 -1, new EditButton(CosmeticEditType.TAG));
        buttons.put(16 -1, new EditButton(CosmeticEditType.COLOR));
        buttons.put(41 -1, new ResetTagButton());
        buttons.put(46 -1, new BackButton());
        return buttons;
    }
    
    private class EditButton extends Button {
        private CosmeticEditType editType;
        
        @Override
        public ItemStack getButtonItem(Player player) {
            return this.editType.getItemStack();
        }
        
        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            if (this.editType == CosmeticEditType.TAG) {
                new SelectTagMenu().openMenu(player);
            }
            else {
                new SelectColorMenu(CosmeticEditType.TAG).openMenu(player);
            }
        }
        
        public EditButton(CosmeticEditType editType) {
            this.editType = editType;
        }
    }
    
    private class ResetTagButton extends Button {
    	
        @Override
        public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(Material.NETHER_STAR).name(CC.YELLOW + CC.BOLD + "Reset Tag").lore(Arrays.asList("", CC.YELLOW + "Click here to reset your tag.")).build();
        }
        
        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            Profile profile = Profile.getProfiles().get(player.getUniqueId());
            profile.setTag(null);
            profile.setTagColor(null);
            player.closeInventory();
            player.sendMessage(CC.YELLOW + "You reset your tag.");
        }
    }
    
    private class BackButton extends Button {
    	
    	@Override
    	public ItemStack getButtonItem(Player player) {
    		return new ItemMaker(Material.CARPET).setTitle("&e&lGo Back").setLore(" ", "&eClick here to go back main page.").build();
    	}
    	
    	@Override
    	public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
    		new CosmeticMenu().openMenu(player);
    	}
    }
}
