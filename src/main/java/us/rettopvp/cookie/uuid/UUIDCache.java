package us.rettopvp.cookie.uuid;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.UUID;
import java.util.logging.Level;

import org.json.simple.parser.ParseException;

import redis.clients.jedis.Jedis;
import us.rettopvp.cookie.CookiePlugin;
import us.rettopvp.cookie.misc.Misc;

public class UUIDCache extends Misc {
	
    public UUIDCache(CookiePlugin cookiePlugin) {
        super(cookiePlugin);
    }
    
    public UUID getUuid(String name) {
        try (Jedis jedis = this.plugin.getJedisPool().getResource()) {
            String uuid = jedis.hget("uuid-cache:name-to-uuid", name.toLowerCase());
            if (uuid != null) {
                return UUID.fromString(uuid);
            }
        } catch (Exception e) {
            this.plugin.getLogger().log(Level.WARNING, "Could not connect to redis");
        }
        try {
            UUID uuid2 = getFromMojang(name);
            if (uuid2 != null) {
                this.update(name, uuid2);
                return uuid2;
            }
        } catch (Exception e) {
            this.plugin.getLogger().log(Level.WARNING, "Could not fetch from Mojang API");
        }
        return null;
    }
    
    public void update(String name, UUID uuid) {
        try (Jedis jedis = this.plugin.getJedisPool().getResource()) {
            jedis.hset("uuid-cache:name-to-uuid", name.toLowerCase(), uuid.toString());
            jedis.hset("uuid-cache:uuid-to-name", uuid.toString(), name);
        }
    }
    
    private static UUID getFromMojang(String name) throws IOException, ParseException {
        URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + name);
        URLConnection conn = url.openConnection();
        conn.setDoOutput(true);
        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line = reader.readLine();
        if (line == null) {
            return null;
        }
        String[] id = line.split(",");
        String part = id[0];
        part = part.substring(7, 39);
        return UUID.fromString(String.valueOf(part).replaceAll("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})", "$1-$2-$3-$4-$5"));
    }
}
