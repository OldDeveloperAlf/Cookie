package us.rettopvp.cookie.cosmetic;

import us.rettopvp.cookie.util.message.color.CC;

public enum CosmeticColor {
	
    DARK_RED("Dark Red", CC.DARK_RED, 14), 
    RED("Red", CC.RED, 14), 
    PURPLE("Purple", CC.DARK_PURPLE, 10), 
    PINK("Pink", CC.PINK, 2), 
    GOLD("Gold", CC.GOLD, 1), 
    YELLOW("Yellow", CC.YELLOW, 4), 
    GREEN("Green", CC.GREEN, 5), 
    DARK_GREEN("Dark Green", CC.DARK_GREEN, 13), 
    LIGHT_BLUE("Light Blue", CC.AQUA, 11), 
    AQUA("Aqua", CC.DARK_AQUA, 9), 
    BLUE("Blue", CC.BLUE, 3),
	WHITE("White", CC.WHITE, 0);
    
    private String name;
    private String display;
    private int variant;
    
    private CosmeticColor(String name, String display, int variant) {
        this.name = name;
        this.display = display;
        this.variant = variant;
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getDisplay() {
        return this.display;
    }
    
    public int getVariant() {
        return this.variant;
    }
}
