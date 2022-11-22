package us.rettopvp.cookie.cosmetic.type;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import us.rettopvp.cookie.util.builder.ItemBuilder;
import us.rettopvp.cookie.util.message.color.CC;

public enum CosmeticEditType {
	
    TAG(new ItemBuilder(Material.NAME_TAG).name(CC.GOLD + CC.BOLD + "Tags").lore(Arrays.asList("", CC.WHITE + "Click here to select a Tag.")).build()), 
    COLOR(new ItemBuilder(Material.WOOL).durability(2).name(CC.GOLD + CC.BOLD + "Tags " + CC.WHITE + "(Colors)").lore(Arrays.asList("", CC.WHITE + "Click here to select a Tag Color.")).build());
    
    private ItemStack itemStack;
    
    public ItemStack getItemStack() {
        return this.itemStack;
    }
    
    private CosmeticEditType(ItemStack itemStack) {
        this.itemStack = itemStack;
    }
}
