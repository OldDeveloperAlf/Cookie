package us.rettopvp.cookie.profile.punishment.procedure;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.entity.Player;

import us.rettopvp.cookie.profile.Profile;
import us.rettopvp.cookie.profile.punishment.Punishment;

public class PunishmentProcedure {
	
    private static Set<PunishmentProcedure> procedures;
    private Player issuer;
    private Profile recipient;
    private PunishmentProcedureType type;
    private PunishmentProcedureStage stage;
    private Punishment punishment;
    
    static {
        procedures = new HashSet<PunishmentProcedure>();
    }
    
    public PunishmentProcedure(Player issuer, Profile recipient, PunishmentProcedureType type, PunishmentProcedureStage stage) {
        this.issuer = issuer;
        this.recipient = recipient;
        this.type = type;
        this.stage = stage;
        PunishmentProcedure.procedures.add(this);
    }
    
    public void finish() {
        this.recipient.save();
        PunishmentProcedure.procedures.remove(this);
    }
    
    public static PunishmentProcedure getByPlayer(Player player) {
        for (PunishmentProcedure procedure : PunishmentProcedure.procedures) {
            if (procedure.issuer.equals(player)) {
                return procedure;
            }
        }
        return null;
    }
    
    public static Set<PunishmentProcedure> getProcedures() {
        return PunishmentProcedure.procedures;
    }
    
    public Player getIssuer() {
        return this.issuer;
    }
    
    public Profile getRecipient() {
        return this.recipient;
    }
    
    public PunishmentProcedureType getType() {
        return this.type;
    }
    
    public PunishmentProcedureStage getStage() {
        return this.stage;
    }
    
    public Punishment getPunishment() {
        return this.punishment;
    }
    
    public void setPunishment(Punishment punishment) {
        this.punishment = punishment;
    }
}
