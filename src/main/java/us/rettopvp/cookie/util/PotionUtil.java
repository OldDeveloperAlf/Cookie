package us.rettopvp.cookie.util;

import org.bukkit.potion.PotionEffectType;

public class PotionUtil {
	
    public static String getName(PotionEffectType potionEffectType) {
        if (potionEffectType.getName().equalsIgnoreCase("fire_resistance")) {
            return "Fire Resistance";
        }
        if (potionEffectType.getName().equalsIgnoreCase("speed")) {
            return "Speed";
        }
        if (potionEffectType.getName().equalsIgnoreCase("weakness")) {
            return "Weakness";
        }
        if (potionEffectType.getName().equalsIgnoreCase("slowness")) {
            return "Slowness";
        }
        return "Unknown";
    }
}
