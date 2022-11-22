package us.rettopvp.cookie.profile.grant.procedure;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.entity.Player;

import us.rettopvp.cookie.profile.Profile;
import us.rettopvp.cookie.profile.grant.Grant;

public class GrantProcedure {
	
    private static Set<GrantProcedure> procedures;
    private Player issuer;
    private Profile recipient;
    private GrantProcedureType type;
    private GrantProcedureStage stage;
    private Grant grant;
    
    static {
        procedures = new HashSet<GrantProcedure>();
    }
    
    public GrantProcedure(Player issuer, Profile recipient, GrantProcedureType type, GrantProcedureStage stage) {
        this.issuer = issuer;
        this.recipient = recipient;
        this.type = type;
        this.stage = stage;
        GrantProcedure.procedures.add(this);
    }
    
    public void finish() {
        this.recipient.save();
        GrantProcedure.procedures.remove(this);
    }
    
    public void cancel() {
        GrantProcedure.procedures.remove(this);
    }
    
    public static GrantProcedure getByPlayer(Player player) {
        for (GrantProcedure procedure : GrantProcedure.procedures) {
            if (procedure.issuer.equals(player)) {
                return procedure;
            }
        }
        return null;
    }
    
    public static Set<GrantProcedure> getProcedures() {
        return GrantProcedure.procedures;
    }
    
    public Player getIssuer() {
        return this.issuer;
    }
    
    public Profile getRecipient() {
        return this.recipient;
    }
    
    public GrantProcedureType getType() {
        return this.type;
    }
    
    public GrantProcedureStage getStage() {
        return this.stage;
    }
    
    public Grant getGrant() {
        return this.grant;
    }
    
    public void setGrant(Grant grant) {
        this.grant = grant;
    }
}
