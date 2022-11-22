package us.rettopvp.cookie.chat.filter.impl;

import java.util.Arrays;
import java.util.List;

import us.rettopvp.cookie.CookiePlugin;
import us.rettopvp.cookie.chat.filter.ChatFilter;

public class ContainsFilter extends ChatFilter {
	
    private List<String> filteredWords;
	
    public ContainsFilter(CookiePlugin plugin) {
        super(plugin, null);

        this.filteredWords = Arrays.asList(
        		"shit", "faggot", "fuck", "nigger", "nigga", "slut", "bitch",
        		"L", "Ez", "noob", "puto", "verga", "mierda", "mrd", "pendejo",
        		"hdp");
    }
    
    @Override
    public boolean isFiltered(String message, String[] words) {
        for (String word : words) {
        	for(String filteredWord : this.filteredWords) {
        		if(word.equals(filteredWord)) {
        			return true;
        		}
        	}
        }
        return false;
    }
}
