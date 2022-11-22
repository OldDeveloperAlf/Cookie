package us.rettopvp.cookie.chat.util;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;

public class ChatComponentExtras {
	
    private HoverEvent hoverEvent;
    
    private ClickEvent clickEvent;
    
    public ChatComponentExtras(HoverEvent hoverEvent, ClickEvent clickEvent) {
        this.hoverEvent = hoverEvent;
        this.clickEvent = clickEvent;
    }
    
    public HoverEvent getHoverEvent() {
        return this.hoverEvent;
    }
    
    public ClickEvent getClickEvent() {
        return this.clickEvent;
    }
    
    public void setHoverEvent(HoverEvent hoverEvent) {
        this.hoverEvent = hoverEvent;
    }
    
    public void setClickEvent(ClickEvent clickEvent) {
        this.clickEvent = clickEvent;
    }
}
