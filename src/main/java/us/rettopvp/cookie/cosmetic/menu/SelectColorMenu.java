package us.rettopvp.cookie.cosmetic.menu;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import us.rettopvp.cookie.cosmetic.CosmeticColor;
import us.rettopvp.cookie.cosmetic.type.CosmeticEditType;
import us.rettopvp.cookie.profile.Profile;
import us.rettopvp.cookie.util.builder.ItemBuilder;
import us.rettopvp.cookie.util.menu.Button;
import us.rettopvp.cookie.util.menu.Menu;
import us.rettopvp.cookie.util.message.Messager;
import us.rettopvp.cookie.util.message.color.CC;

public class SelectColorMenu extends Menu {
	
    private CosmeticEditType editType;
    
    @Override
    public String getTitle(Player player) {
        if (this.editType == CosmeticEditType.TAG) {
            return CC.GOLD + CC.BOLD + "Select a Tag Color";
        }
        return CC.GOLD + CC.BOLD + "Select a Color";
    }
    
    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<Integer, Button>();
        for (CosmeticColor color : CosmeticColor.values()) {
            buttons.put(buttons.size(), new SelectColorButton(color, this.editType));
        }
        if(this.editType == CosmeticEditType.COLOR) {
        	buttons.put(32 -1, new ResetColorButton());
        }
        return buttons;
    }
    
    public SelectColorMenu(CosmeticEditType editType) {
        this.editType = editType;
    }
    
    private class SelectColorButton extends Button {
    	
        private CosmeticColor color;
        
        private CosmeticEditType editType;
        
        @Override
        public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(Material.WOOL).durability(this.color.getVariant()).name(this.color.getDisplay() + this.color.getName()).lore(Arrays.asList("", Messager.translate("&eClick here to select &r" + this.color.getDisplay() + this.color.getName() + "&e."))).build();
        }
        
        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            Profile profile = Profile.getProfiles().get(player.getUniqueId());
            if (this.editType == CosmeticEditType.TAG) {
                profile.setTagColor(this.color);
            } else {
                profile.setColor(this.color);
            }
            player.closeInventory();
            player.sendMessage(Messager.translate("&eYou set your color to: &r" + this.color.getDisplay() + this.color.getName()));
        }
        
        public SelectColorButton(CosmeticColor color, CosmeticEditType editType) {
            this.color = color;
            this.editType = editType;
        }
    }
    
    private class ResetColorButton extends Button {
    	
        @Override
        public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(Material.WOOL).name(CC.YELLOW + CC.BOLD + "Reset Color").lore(Arrays.asList("", CC.YELLOW + "Click here to reset your color.")).build();
        }
        
        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
        	Profile profile = Profile.getProfiles().get(player.getUniqueId());
            profile.setColor(null);
            player.closeInventory();
            player.sendMessage(CC.YELLOW + "You reset your color.");
        }
    }
}
