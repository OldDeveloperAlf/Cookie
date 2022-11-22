package us.rettopvp.cookie.chat;

import us.rettopvp.cookie.chat.filter.ChatFilter;
import us.rettopvp.cookie.profile.punishment.Punishment;

public class ChatAttempt {
	
    private Response response;
    private ChatFilter filterFlagged;
    private Punishment punishment;
    
    public ChatAttempt(Response response) {
        this.response = response;
    }
    
    public ChatAttempt(Response response, ChatFilter filterFlagged) {
        this.response = response;
        this.filterFlagged = filterFlagged;
    }
    
    public ChatAttempt(Response response, Punishment punishment) {
        this.response = response;
        this.punishment = punishment;
    }
    
    public Response getResponse() {
        return this.response;
    }
    
    public ChatFilter getFilterFlagged() {
        return this.filterFlagged;
    }
    
    public Punishment getPunishment() {
        return this.punishment;
    }
    
    public void setResponse(Response response) {
        this.response = response;
    }
    
    public void setFilterFlagged(ChatFilter filterFlagged) {
        this.filterFlagged = filterFlagged;
    }
    
    public void setPunishment(Punishment punishment) {
        this.punishment = punishment;
    }
    
    public enum Response {
    	
        ALLOWED, 
        MESSAGE_FILTERED, 
        PLAYER_MUTED;
    }
}
