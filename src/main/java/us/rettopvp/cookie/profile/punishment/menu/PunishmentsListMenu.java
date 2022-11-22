package us.rettopvp.cookie.profile.punishment.menu;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import us.rettopvp.cookie.profile.Profile;
import us.rettopvp.cookie.profile.punishment.Punishment;
import us.rettopvp.cookie.profile.punishment.PunishmentType;
import us.rettopvp.cookie.profile.punishment.procedure.PunishmentProcedure;
import us.rettopvp.cookie.profile.punishment.procedure.PunishmentProcedureStage;
import us.rettopvp.cookie.profile.punishment.procedure.PunishmentProcedureType;
import us.rettopvp.cookie.util.TimeUtil;
import us.rettopvp.cookie.util.builder.ItemBuilder;
import us.rettopvp.cookie.util.menu.Button;
import us.rettopvp.cookie.util.menu.pagination.PaginatedMenu;
import us.rettopvp.cookie.util.message.Messager;
import us.rettopvp.cookie.util.message.color.CC;

public class PunishmentsListMenu extends PaginatedMenu {
	
    private Profile profile;
    
    private PunishmentType punishmentType;
    
    @Override
    public String getPrePaginatedTitle(Player player) {
        return "&6" + this.punishmentType.getTypeData().getReadable() + " &7- &f" + this.profile.getColoredUsername();
    }
    
    @Override
    public Map<Integer, Button> getAllPagesButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<Integer, Button>();
        for (Punishment punishment : this.profile.getPunishments()) {
            if (punishment.getType() == this.punishmentType) {
                buttons.put(buttons.size(), new PunishmentInfoButton(punishment));
            }
        }
        return buttons;
    }
    
    public PunishmentsListMenu(Profile profile, PunishmentType punishmentType) {
        this.profile = profile;
        this.punishmentType = punishmentType;
    }
    
    private class PunishmentInfoButton extends Button {
    	
        private Punishment punishment;
        
        @Override
        public ItemStack getButtonItem(Player player) {
            String addedBy = "Console";
            if (this.punishment.getAddedBy() != null) {
                try {
                    Profile addedByProfile = Profile.getByUuid(this.punishment.getAddedBy());
                    addedBy = addedByProfile.getUsername();
                }
                catch (Exception e) {
                    addedBy = "Could not fetch...";
                }
            }
            List<String> lore = new ArrayList<String>();
            lore.add(CC.MENU_BAR);
            lore.add("&6Issuer: &f" + addedBy);
            lore.add("&6Reason: &f" + this.punishment.getAddedReason());
            if (this.punishment.isActive() && !this.punishment.isPermanent() && this.punishment.getDuration() != -1L) {
                lore.add("&6Time Left: &f" + this.punishment.getTimeRemaining());
            }
            lore.add("&6Date added: &f" + this.punishment.getAddedAtFormatted());
            if (this.punishment.isPardoned()) {
                String removedBy = "Console";
                if (this.punishment.getPardonedBy() != null) {
                    try {
                        Profile removedByProfile = Profile.getByUuid(this.punishment.getPardonedBy());
                        removedBy = removedByProfile.getUsername();
                    } catch (Exception e) {
                        removedBy = "Could not fetch...";
                    }
                }
                lore.add(CC.MENU_BAR);
                lore.add("&e[Removed] &6Issuer: &f" + removedBy);
                lore.add("&e(Removed) &6Reason: &f" + this.punishment.getPardonedReason());
                lore.add("&e[Removed] &6Date removed: &f" + TimeUtil.dateToString(new Date(this.punishment.getPardonedAt())));
            }
            lore.add(CC.MENU_BAR);
            if (!this.punishment.isPardoned() && this.punishment.getType().isCanBePardoned()) {
                lore.add("&fRight click to pardon this punishment");
                lore.add(CC.MENU_BAR);
            }
            return new ItemBuilder(Material.PAPER).name("&6" + TimeUtil.dateToString(new Date(this.punishment.getAddedAt()))).lore(lore).build();
        }
        
        @Override
        public void clicked(Player player, ClickType clickType) {
            if (clickType == ClickType.RIGHT && !this.punishment.isPardoned() && this.punishment.getType().isCanBePardoned()) {
                PunishmentProcedure procedure = new PunishmentProcedure(player, PunishmentsListMenu.this.profile, PunishmentProcedureType.PARDON, PunishmentProcedureStage.REQUIRE_TEXT);
                procedure.setPunishment(this.punishment);
                Messager.sendMessage(player, "&eType a reason for pardoning this punishment in chat...");
                player.closeInventory();
            }
        }
        
        public PunishmentInfoButton(Punishment punishment) {
            this.punishment = punishment;
        }
    }
}
