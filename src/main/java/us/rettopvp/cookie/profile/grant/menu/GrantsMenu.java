package us.rettopvp.cookie.profile.grant.menu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import us.rettopvp.cookie.profile.Profile;
import us.rettopvp.cookie.profile.grant.Grant;
import us.rettopvp.cookie.profile.grant.procedure.GrantProcedure;
import us.rettopvp.cookie.profile.grant.procedure.GrantProcedureStage;
import us.rettopvp.cookie.profile.grant.procedure.GrantProcedureType;
import us.rettopvp.cookie.util.builder.ItemBuilder;
import us.rettopvp.cookie.util.menu.Button;
import us.rettopvp.cookie.util.menu.pagination.PaginatedMenu;
import us.rettopvp.cookie.util.message.Messager;
import us.rettopvp.cookie.util.message.color.CC;

public class GrantsMenu extends PaginatedMenu {
	
    private Profile profile;
    
    @Override
    public String getPrePaginatedTitle(Player player) {
        return "&6Grants of " + this.profile.getColoredUsername();
    }
    
    @Override
    public Map<Integer, Button> getAllPagesButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<Integer, Button>();
        for (Grant grant : this.profile.getGrants()) {
            buttons.put(buttons.size(), new GrantInfoButton(this.profile, grant));
        }
        return buttons;
    }
    
    public GrantsMenu(Profile profile) {
        this.profile = profile;
    }
    
    private class GrantInfoButton extends Button {
    	
        private Profile profile;
        private Grant grant;
        
        @Override
        public ItemStack getButtonItem(Player player) {
            String addedBy = "Console";
            
            if (this.grant.getAddedBy() != null) {
                addedBy = "Could not fetch...";
                
                Profile addedByProfile = Profile.getByUuid(this.grant.getAddedBy());
                
                if (addedByProfile != null && addedByProfile.isLoaded()) {
                    addedBy = addedByProfile.getUsername();
                }
            }
            List<String> lore = new ArrayList<String>();
            lore.add(CC.MENU_BAR);
            
            lore.add("&6Issuer: &f" + addedBy);
            lore.add("&6Reason: &f" + this.grant.getAddedReason());
            lore.add("&6Date added: &f" + this.grant.getAddedAtDate());
            
            if (!this.grant.isRemoved()) {
                if (!this.grant.hasExpired()) {
                    lore.add("&6Expires at: &f" + this.grant.getExpiresAtDate());
                }
            } else {
                String removedBy = "Console";
                
                if (this.grant.getRemovedBy() != null) {
                    removedBy = "Could not fetch...";
                    
                    Profile removedByProfile = Profile.getByUuid(this.grant.getRemovedBy());
                    
                    if (removedByProfile != null && removedByProfile.isLoaded()) {
                        removedBy = removedByProfile.getUsername();
                    }
                }
                lore.add(CC.MENU_BAR);
                
                lore.add("&7[Removed] &6Issuer: &f" + removedBy);
                lore.add("&7[Removed] &6Reason: &f" + this.grant.getRemovedReason());
                lore.add("&7[Removed] &6Date removed: &f" + this.grant.getRemovedAtDate());
            }
            
            lore.add(CC.MENU_BAR);
            
            if (!this.grant.isRemoved()) {
                if(!this.grant.getRank().isDefaultRank()) {
                	lore.add("&fRight click to remove this grant");
                	lore.add(CC.MENU_BAR);
                }
            }
            return new ItemBuilder(Material.PAPER).name(this.grant.getRank().getColor() + this.grant.getRank().getDisplayName()).lore(lore).build();
        }
        
        @Override
        public void clicked(Player player, ClickType clickType) {
            if (clickType == ClickType.RIGHT && !this.grant.isRemoved() && !this.grant.getRank().isDefaultRank()) {
                GrantProcedure procedure = new GrantProcedure(player, this.profile, GrantProcedureType.REMOVE, GrantProcedureStage.REQUIRE_TEXT);
                procedure.setGrant(this.grant);
                Messager.sendMessage(player, "&eType a reason for removing this grant in chat...");
                player.closeInventory();
            }
        }
        
        public GrantInfoButton(Profile profile, Grant grant) {
            this.profile = profile;
            this.grant = grant;
        }
    }
}
