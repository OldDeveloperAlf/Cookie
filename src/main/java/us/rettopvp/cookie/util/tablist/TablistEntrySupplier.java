package us.rettopvp.cookie.util.tablist;

import org.bukkit.entity.Player;

import net.minecraft.util.com.google.common.collect.Table;

public interface TablistEntrySupplier {

	Table<Integer, Integer, String> getEntries(Player player);
}
