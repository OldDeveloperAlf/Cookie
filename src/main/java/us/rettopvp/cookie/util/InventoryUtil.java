package us.rettopvp.cookie.util;

import java.util.Map;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import com.google.common.base.Preconditions;

public class InventoryUtil {
	
    public static int DEFAULT_INVENTORY_WIDTH = 9;
    public static int MINIMUM_INVENTORY_HEIGHT = 1;
    public static int MINIMUM_INVENTORY_SIZE = 9;
    public static int MAXIMUM_INVENTORY_HEIGHT = 6;
    public static int MAXIMUM_INVENTORY_SIZE = 54;
    public static int MAXIMUM_SINGLE_CHEST_SIZE = 27;
    public static int MAXIMUM_DOUBLE_CHEST_SIZE = 54;

    public static ItemStack[] deepClone(ItemStack[] origin) {
        Preconditions.checkNotNull((Object) origin, (Object) "Origin cannot be null");
        ItemStack[] cloned = new ItemStack[origin.length];
        for (int i = 0; i < origin.length; ++i) {
            ItemStack next = origin[i];
            cloned[i] = ((next == null) ? null : next.clone());
        }
        return cloned;
    }

    public static int getSafestInventorySize(int initialSize) {
        return (initialSize + 8) / 9 * 9;
    }

    public static void removeItem(Inventory inventory, Material type, short data, int quantity) {
        ItemStack[] contents = inventory.getContents();
        boolean compareDamage = type.getMaxDurability() == 0;
        for (int i = quantity; i > 0; --i) {
            ItemStack[] var7 = contents;
            int var8 = contents.length;
            int var9 = 0;
            while (var9 < var8) {
                ItemStack content = var7[var9];
                if (content != null && content.getType() == type && (!compareDamage || content.getData().getData() == data)) {
                    if (content.getAmount() <= 1) {
                        inventory.removeItem(new ItemStack[]{content});
                        break;
                    }
                    content.setAmount(content.getAmount() - 1);
                    break;
                } else {
                    ++var9;
                }
            }
        }
    }

    public static int countAmount(Inventory inventory, Material type, short data) {
        ItemStack[] contents = inventory.getContents();
        boolean compareDamage = type.getMaxDurability() == 0;
        int counter = 0;
        ItemStack[] var6 = contents;
        for (int var7 = contents.length, var8 = 0; var8 < var7; ++var8) {
            ItemStack item = var6[var8];
            if (item != null && item.getType() == type && (!compareDamage || item.getData().getData() == data)) {
                counter += item.getAmount();
            }
        }
        return counter;
    }

    public static boolean isEmpty(Inventory inventory) {
        return isEmpty(inventory, true);
    }

    public static boolean isEmpty(Inventory inventory, boolean checkArmour) {
        boolean result = true;
        ItemStack[] armorContents;
        ItemStack[] contents2 = armorContents = inventory.getContents();
        for (int var6 = contents2.length, var7 = 0; var7 < var6; ++var7) {
            ItemStack content = armorContents[var7];
            if (content != null && content.getType() != Material.AIR) {
                result = false;
                break;
            }
        }
        if (!result) {
            return false;
        }
        if (checkArmour && inventory instanceof PlayerInventory) {
            ItemStack[] var8;
            armorContents = (var8 = ((PlayerInventory) inventory).getArmorContents());
            for (int var7 = armorContents.length, var9 = 0; var9 < var7; ++var9) {
                ItemStack content2 = var8[var9];
                if (content2 != null && content2.getType() != Material.AIR) {
                    result = false;
                    break;
                }
            }
        }
        return result;
    }

    public static boolean clickedTopInventory(InventoryDragEvent event) {
        InventoryView view = event.getView();
        Inventory topInventory = view.getTopInventory();
        if (topInventory == null) {
            return false;
        }
        boolean result = false;
        Set<Map.Entry<Integer, ItemStack>> entrySet = event.getNewItems().entrySet();
        int size = topInventory.getSize();
        for (Map.Entry<Integer, ItemStack> entry : entrySet) {
            if (entry.getKey() < size) {
                result = true;
                break;
            }
        }
        return result;
    }
}
