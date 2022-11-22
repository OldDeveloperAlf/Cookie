package us.rettopvp.cookie.util;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class LocationUtil {
	
    public static Location[] getFaces(Location start) {
        Location[] faces = { new Location(start.getWorld(), start.getX() + 1.0, start.getY(), start.getZ()), new Location(start.getWorld(), start.getX() - 1.0, start.getY(), start.getZ()), new Location(start.getWorld(), start.getX(), start.getY() + 1.0, start.getZ()), new Location(start.getWorld(), start.getX(), start.getY() - 1.0, start.getZ()) };
        return faces;
    }
    
    public static String serialize(Location location) {
        return location.getWorld().getName() + ":" + location.getX() + ":" + location.getY() + ":" + location.getZ() + ":" + location.getYaw() + ":" + location.getPitch();
    }
    
    public static Location deserialize(String source) {
        if (source == null) {
            return null;
        }
        String[] split = source.split(":");
        World world = Bukkit.getServer().getWorld(split[0]);
        if (world == null) {
            return null;
        }
        return new Location(world, Double.parseDouble(split[1]), Double.parseDouble(split[2]), Double.parseDouble(split[3]), Float.parseFloat(split[4]), Float.parseFloat(split[5]));
    }
}
