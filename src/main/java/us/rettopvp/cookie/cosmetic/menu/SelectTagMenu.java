package us.rettopvp.cookie.cosmetic.menu;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import us.rettopvp.cookie.cosmetic.CosmeticTag;
import us.rettopvp.cookie.profile.Profile;
import us.rettopvp.cookie.util.builder.ItemBuilder;
import us.rettopvp.cookie.util.builder.ItemMaker;
import us.rettopvp.cookie.util.menu.Button;
import us.rettopvp.cookie.util.menu.Menu;
import us.rettopvp.cookie.util.message.Messager;
import us.rettopvp.cookie.util.message.color.CC;

public class SelectTagMenu extends Menu {
	
	@Override
	public int getSize() {
		return 9 * 6;
	}
	
    @Override
    public String getTitle(final Player player) {
        return CC.GOLD + "Select a Tag";
    }
    
    @Override
    public Map<Integer, Button> getButtons(final Player player) {
        final Map<Integer, Button> buttons = new HashMap<Integer, Button>();
        for (final CosmeticTag tag : CosmeticTag.values()) {
            buttons.put(buttons.size(), new SelectTagButton(tag));
        }
        buttons.put(41 -1, new ResetTagButton());
        buttons.put(46 -1, new BackButton());
        return buttons;
    }
    
    private class SelectTagButton extends Button {
    	
        private CosmeticTag tag;
        
        @Override
        public ItemStack getButtonItem(final Player player) {
            return new ItemBuilder(Material.NAME_TAG).name(this.tag.getSelectionDisplay()).lore(Arrays.asList("", Messager.translate("&eClick here to select &d" + this.tag.getName() + "&e."))).build();
        }
        
        @Override
        public void clicked(final Player player, final int slot, final ClickType clickType, final int hotbarButton) {
            final Profile nucleusPlayer = Profile.getProfiles().get(player.getUniqueId());
            nucleusPlayer.setTag(this.tag);
            player.closeInventory();
            player.sendMessage(Messager.translate("&eYou set your tag to: &r" + this.tag.getSelectionDisplay()));
        }
        
        public SelectTagButton(final CosmeticTag tag) {
            this.tag = tag;
        }
    }
    
    private class ResetTagButton extends Button {
        @Override
        public ItemStack getButtonItem(final Player player) {
            return new ItemBuilder(Material.NETHER_STAR).name(CC.YELLOW + CC.BOLD + "Reset Tag").lore(Arrays.asList("", CC.YELLOW + "Click here to reset your tag.")).build();
        }
        
        @Override
        public void clicked(final Player player, final int slot, final ClickType clickType, final int hotbarButton) {
            final Profile nucleusPlayer = Profile.getProfiles().get(player.getUniqueId());
            nucleusPlayer.setTag(null);
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
