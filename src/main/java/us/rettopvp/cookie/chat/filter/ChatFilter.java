package us.rettopvp.cookie.chat.filter;

import us.rettopvp.cookie.CookiePlugin;
import us.rettopvp.cookie.misc.Misc;

public abstract class ChatFilter extends Misc {
	
    public ChatFilter(CookiePlugin cookiePlugin, String command) {
        super(cookiePlugin);
    }
    
    public abstract boolean isFiltered(String message, String[] words);
}
