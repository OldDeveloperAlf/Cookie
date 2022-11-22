package us.rettopvp.cookie.util.cuboid;

import java.util.Map;

import org.bukkit.Location;
import org.bukkit.World;

public class NamedCuboid extends Cuboid {
	
    protected String name;

    public NamedCuboid() {
        super();
    }

    public NamedCuboid(Cuboid other) {
        super(other.getWorld(), other.x1, other.y1, other.z1, other.x2, other.y2, other.z2);
    }

    public NamedCuboid(World world, int x1, int y1, int z1, int x2, int y2, int z2) {
        super(world, x1, y1, z1, x2, y2, z2);
    }

    public NamedCuboid(Location location) {
        super(location, location);
    }

    public NamedCuboid(Location first, Location second) {
        super(first, second);
    }

    public NamedCuboid(Map map) {
        super(map);
        this.name = (String) map.get("name");
    }

    @Override
    public Map serialize() {
        Map map = super.serialize();
        map.put("name", this.name);
        return map;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public NamedCuboid clone() {
        return (NamedCuboid) super.clone();
    }
}
