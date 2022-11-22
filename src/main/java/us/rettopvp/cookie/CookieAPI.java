package us.rettopvp.cookie;

import org.bukkit.entity.Player;

import us.rettopvp.cookie.profile.Profile;
import us.rettopvp.cookie.util.message.color.CC;

public class CookieAPI {
	
    public static String getPlayerRank(Player player, boolean colored) {
    	Profile profile = Profile.getProfiles().get(player.getUniqueId());
    	return CC.translate((colored ? profile.getActiveGrant().getRank().getColoredName() : profile.getActiveGrant().getRank().getDisplayName()));
    }
    
    public static String getPlayerAndPrefix(Player player) {
    	Profile profile = Profile.getProfiles().get(player.getUniqueId());
    	return CC.translate(profile.getActiveGrant().getRank().getPrefix() + (profile.getNickName() == null ? player.getName() : profile.getNickName()));
    }
    
    public static String getPlayerColoredName(Player player) {
    	Profile profile = Profile.getProfiles().get(player.getUniqueId());
    	return CC.translate(profile.getColoredUsername());
    }
    
    public static String getPrefixdPlayer(Player player) {
        Profile profile = Profile.getProfiles().get(player.getUniqueId());
        String prefix = profile.getActiveGrant().getRank().getPrefix();
        String color = null;
        String tag = null;
        if (player.hasPermission("cookie.cosmetic.color") && profile.getColor() != null) {
            color = profile.getColor().getDisplay();
        }
        if (player.hasPermission("cookie.cosmetic.tag") && profile.getTag() != null) {
            if (profile.getTagColor() != null) {
                tag = CC.translate("&e[" + profile.getTagColor().getDisplay() + profile.getTag().getDisplay() + "&e]&r");
            } else {
                tag = CC.translate("&e[" + profile.getTag().getDisplay() + "&e]&r");
            }
        }
        return CC.translate(((tag == null) ? "" : (tag + " ")) + prefix + ((color == null) ? "" : (color)) + (profile.getNickName() == null ? player.getName() : profile.getNickName()));
    }
    
    public static String getColoredName(Player player) {
        Profile profile = Profile.getProfiles().get(player.getUniqueId());
        if (player.hasPermission("cookie.cosmetic.color") && profile.getColor() != null) {
            return CC.translate(profile.getColor().getDisplay() + (profile.getNickName() == null ? player.getName() : profile.getNickName()));
        }
        return CC.translate(profile.getActiveGrant().getRank().getColoredName() + (profile.getNickName() == null ? player.getName() : profile.getNickName()));
    }
}
