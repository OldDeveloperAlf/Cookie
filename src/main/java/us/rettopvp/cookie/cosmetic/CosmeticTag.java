package us.rettopvp.cookie.cosmetic;

import net.minecraft.util.org.apache.commons.lang3.StringEscapeUtils;
import us.rettopvp.cookie.util.message.Messager;
import us.rettopvp.cookie.util.message.color.CC;

public enum CosmeticTag {
	
    TAG_GOD("God", CC.BOLD + "God", false), 
    TAG_PRO("Pro", CC.BOLD + "Pro", false), 
    TAG_MASTER("Master", CC.BOLD + "Master", false),
    TAG_NOOB("Noob", CC.BOLD + "Noob", false), 
    TAG_CHEATER("Cheater", CC.BOLD + "Cheater", false), 
    TAG_HACKER("Hacker", CC.BOLD + "Hacker", false), 
    TAG_EZ("eZ", CC.BOLD + "eZ", false),
    TAG_L("L", CC.BOLD + "L", false),
    TAG_RETTO("Original RettoPvP", CC.BOLD + "Retto", false),
    TAG_FOUNDER("#iKl4b", CC.BOLD + "#iKl4b", false),
    
    ARABIC_STAR("Fancy Star", StringEscapeUtils.unescapeJava("\u06de"), true), 
    YIN_YANG("Yin Yang", StringEscapeUtils.unescapeJava("\u0fca"), true), 
    RADIOACTIVE("Radioactive", StringEscapeUtils.unescapeJava("\u2622"), true), 
    BIOHAZARD("Biohazard", StringEscapeUtils.unescapeJava("\u2623"), true), 
    GEAR("Gear", CC.BOLD + StringEscapeUtils.unescapeJava("\u2699"), true), 
    CHECK_MARK("Check Mark", CC.BOLD + StringEscapeUtils.unescapeJava("\u2713"), true), 
    X_MARK("X Mark", StringEscapeUtils.unescapeJava("\u2717"), true), 
    STAR_OF_DAVID("Star of David", StringEscapeUtils.unescapeJava("\u2721"), true), 
    MALTESE_CROSS("Maltese Cross", StringEscapeUtils.unescapeJava("\u2720"), true), 
    CIRCLED_STAR("Circled Star", StringEscapeUtils.unescapeJava("\u272a"), true), 
    POINTED_STAR("Pointed Star", StringEscapeUtils.unescapeJava("\u2726"), true), 
    FLORETTE("Florette", CC.BOLD + StringEscapeUtils.unescapeJava("\u273f"), true);
    
    private String name;
    private String display;
    private boolean icon;
    
    private CosmeticTag(String name, String display, boolean icon) {
        this.name = name;
        this.display = display;
        this.icon = icon;
    }
    
    public String getSelectionDisplay() {
        return Messager.translate("&e" + this.name + (this.icon ? (" &7(&d" + this.display + "&7)") : ""));
    }
    
    public String getName() {
        return this.name;
    }
     
    public String getDisplay() {
        return this.display;
    }
    
    public boolean isIcon() {
        return this.icon;
    }
}
