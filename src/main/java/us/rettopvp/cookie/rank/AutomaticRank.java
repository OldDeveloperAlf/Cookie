package us.rettopvp.cookie.rank;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;

import us.rettopvp.cookie.CookiePlugin;
import us.rettopvp.cookie.util.message.Messager;
import us.rettopvp.cookie.util.message.color.CC;

public class AutomaticRank {
	
	private CookiePlugin plugin;
	
	private int ticks;
	private boolean active;
	private RespondeRank responde;
	
	public AutomaticRank(CookiePlugin plugin) {
		this.plugin = plugin;
		this.active = false;
		this.ticks = 60;
	}

	public void runnable(CommandSender commandsender) {
		this.active = true;
		new BukkitRunnable() {
			
			@Override
			public void run() {
				if(active) {
					--ticks;
					if(ticks != 0) {
						if(ticks == 50) {
							Messager.sendMessage(commandsender, "&6These are the default ranks, create or register.");
							StringJoiner ranks = new StringJoiner(", ");
							for(RankType rank : RankType.values()) {
								ranks.add(rank.getName());
							}
							Messager.sendMessage(commandsender, "(" + ranks.toString() + ")");
							Messager.sendMessage(commandsender, "&6If there is a problem, with this system, please contact our developer.");
						} else if(ticks == 45) { 
							Messager.sendMessage(commandsender, "&6Creation of ranks in process &f(%10).");
						} else if(ticks == 40) {
							Messager.sendMessage(commandsender, "&6Creation of ranks in process &f(%20).");
						} else if(ticks == 35) {
							Messager.sendMessage(commandsender, "&6Creation of ranks in process &f(%30).");
						} else if(ticks == 30) {
							Messager.sendMessage(commandsender, "&6Creation of ranks in process &f(%40).");
						} else if(ticks == 25) {
							Messager.sendMessage(commandsender, "&6Creation of ranks in process &f(%50).");
						} else if(ticks == 20) {
							Messager.sendMessage(commandsender, "&6Creation of ranks in process &f(%60).");
						} else if(ticks == 15) {
							Messager.sendMessage(commandsender, "&6Creation of ranks in process &f(%70).");
						} else if(ticks == 10) {
							Messager.sendMessage(commandsender, "&6Creation of ranks in process &f(%80).");
						} else if(ticks == 5) {
							Messager.sendMessage(commandsender, "&6Creation of ranks in process &f(%90).");
						} else if(ticks == 3) {
							Messager.sendMessage(commandsender, "&6Creation of ranks in process &f(%100).");
						} else if(ticks == 1) {
							try {
								createRanks();
								
								Messager.sendMessage(commandsender, "&6The ranks have been created correctly. down the details.");
								Messager.sendMessage(commandsender, "&f(Success: &a20&f), (Failed: &c1&f)");
								Messager.sendMessage(commandsender, "&cFailed&7: &fThe rank Default is already created.");
							} catch (Exception e) {
								Messager.sendMessage(commandsender, "&cFailed to create ranks, please contact to developer.");
								Messager.sendMessage(commandsender, "&cError:" + e.getMessage());
							}
							active = false;
							ticks = 60;
						}
					} else {
						ticks = 60;
						active = false;
					}
				}
			}
		}.runTaskTimer(this.plugin, 20L, 20L);
	}
	
	public void createRanks() {
		this.createRank(PermissionType.KITMAP, RankType.FOUNDER);
		this.createRank(PermissionType.KITMAP, RankType.DEVELOPER);
		this.createRank(PermissionType.KITMAP, RankType.OWNER);
		this.createRank(PermissionType.KITMAP, RankType.CO_OWNER);
		this.createRank(PermissionType.KITMAP, RankType.MANAGER);
		this.createRank(PermissionType.KITMAP, RankType.PLATFORM_ADMIN);
		this.createRank(PermissionType.KITMAP, RankType.SR_ADMINISTRATOR);
		this.createRank(PermissionType.KITMAP, RankType.ADMINISTRATOR);
		this.createRank(PermissionType.KITMAP, RankType.SR_MODERATOR);
		this.createRank(PermissionType.KITMAP, RankType.MODERATOR_PLUS);
		this.createRank(PermissionType.KITMAP, RankType.MODERATOR);
		this.createRank(PermissionType.KITMAP, RankType.TRIAL_MOD);
		this.createRank(PermissionType.KITMAP, RankType.BUILDER);
		this.createRank(PermissionType.KITMAP, RankType.FAMOUS);
		this.createRank(PermissionType.KITMAP, RankType.YOUTUBER);
		this.createRank(PermissionType.KITMAP, RankType.RETTO_PLUS);
		this.createRank(PermissionType.KITMAP, RankType.RETTO);
		this.createRank(PermissionType.KITMAP, RankType.GALEO);
		this.createRank(PermissionType.KITMAP, RankType.LIBO);
		this.createRank(PermissionType.KITMAP, RankType.CILO);
		this.createRank(PermissionType.KITMAP, RankType.DEFAULT);
	}
	
	public void createRank(PermissionType permissionType, RankType rankType) {
		if(Rank.getRankByDisplayName(rankType.getName()) != null) {
			return;
		}
		switch (rankType) {
			case FOUNDER: {
				Rank rank = new Rank(rankType.getName());
				rank.setColor(ChatColor.DARK_RED);
				rank.setPrefix(CC.translate("&6[&4" + rankType.getName() + "&6] &4"));
				rank.setWeight(1000);
				
				for(String permissions : this.getPermissions(permissionType, rankType)) {
					rank.addPermission(permissions);
				}
				
				rank.save();
				break;
			}
			case DEVELOPER: {
				Rank rank = new Rank(rankType.getName());
				rank.setColor(ChatColor.AQUA);
				rank.setPrefix(CC.translate("&6[&b" + rankType.getName() + "&6] &b"));
				rank.setWeight(950);
				
				for(String permissions : this.getPermissions(permissionType, rankType)) {
					rank.addPermission(permissions);
				}
				
				rank.save();
				break;
			}
			case OWNER: {
				Rank rank = new Rank(rankType.getName());
				rank.setColor(ChatColor.DARK_RED);
				rank.setPrefix(CC.translate("&6[&4" + rankType.getName() + "&6] &4"));
				rank.setWeight(900);
				
				for(String permissions : this.getPermissions(permissionType, rankType)) {
					rank.addPermission(permissions);
				}
				
				rank.save();
				break;
			}
			case CO_OWNER: {
				Rank rank = new Rank(rankType.getName());
				rank.setColor(ChatColor.DARK_RED);
				rank.setPrefix(CC.translate("&6[&4" + rankType.getName() + "&6] &4"));
				rank.setWeight(850);
				
				for(String permissions : this.getPermissions(permissionType, rankType)) {
					rank.addPermission(permissions);
				}
				
				rank.save();
				break;
			}
			case MANAGER: {
				Rank rank = new Rank(rankType.getName());
				rank.setColor(ChatColor.GOLD);
				rank.setPrefix(CC.translate("&e[&6" + rankType.getName() + "&e] &6"));
				rank.setWeight(800);
				
				for(String permissions : this.getPermissions(permissionType, rankType)) {
					rank.addPermission(permissions);
				}
				
				rank.save();
				break;
			}
			case PLATFORM_ADMIN: {
				Rank rank = new Rank(rankType.getName());
				rank.setColor(ChatColor.DARK_RED);
				rank.setPrefix(CC.translate("&6[&4" + rankType.getName() + "&6] &4"));
				rank.setWeight(750);
				
				for(String permissions : this.getPermissions(permissionType, rankType)) {
					rank.addPermission(permissions);
				}
				
				rank.save();
				break;
			}
			case SR_ADMINISTRATOR: {
				Rank rank = new Rank(rankType.getName());
				rank.setColor(ChatColor.RED);
				rank.setPrefix(CC.translate("&6[&c" + rankType.getName() + "&6] &c"));
				rank.setWeight(700);
				
				for(String permissions : this.getPermissions(permissionType, rankType)) {
					rank.addPermission(permissions);
				}
				
				rank.save();
				break;
			}
			case ADMINISTRATOR: {
				Rank rank = new Rank(rankType.getName());
				rank.setColor(ChatColor.RED);
				rank.setPrefix(CC.translate("&6[&c" + rankType.getName() + "&6] &c"));
				rank.setWeight(650);
				
				for(String permissions : this.getPermissions(permissionType, rankType)) {
					rank.addPermission(permissions);
				}
				
				rank.save();
				break;
			}
			case SR_MODERATOR: {
				Rank rank = new Rank(rankType.getName());
				rank.setColor(ChatColor.DARK_GREEN);
				rank.setPrefix(CC.translate("&6[&2" + rankType.getName() + "&6] &2"));
				rank.setWeight(600);
				
				for(String permissions : this.getPermissions(permissionType, rankType)) {
					rank.addPermission(permissions);
				}
				
				rank.save();
				break;
			}
			case MODERATOR_PLUS: {
				Rank rank = new Rank(rankType.getName());
				rank.setColor(ChatColor.AQUA);
				rank.setPrefix(CC.translate("&6[&b" + rankType.getName().replace("+", "") + "&a+&6] &b"));
				rank.setWeight(550);
				
				for(String permissions : this.getPermissions(permissionType, rankType)) {
					rank.addPermission(permissions);
				}
				
				rank.save();
				break;
			}
			case MODERATOR: {
				Rank rank = new Rank(rankType.getName());
				rank.setColor(ChatColor.AQUA);
				rank.setPrefix(CC.translate("&6[&b" + rankType.getName() + "&6] &b"));
				rank.setWeight(500);
				
				for(String permissions : this.getPermissions(permissionType, rankType)) {
					rank.addPermission(permissions);
				}
				
				rank.save();
				break;
			}
			case TRIAL_MOD: {
				Rank rank = new Rank(rankType.getName());
				rank.setColor(ChatColor.YELLOW);
				rank.setPrefix(CC.translate("&6[&e" + rankType.getName() + "&6] &e"));
				rank.setWeight(450);
				
				for(String permissions : this.getPermissions(permissionType, rankType)) {
					rank.addPermission(permissions);
				}
				
				rank.save();
				break;
			}
			case BUILDER: {
				Rank rank = new Rank(rankType.getName());
				rank.setColor(ChatColor.GREEN);
				rank.setPrefix(CC.translate("&6[&a" + rankType.getName() + "&6] &a"));
				rank.setWeight(400);
				
				for(String permissions : this.getPermissions(permissionType, rankType)) {
					rank.addPermission(permissions);
				}
				
				rank.save();
				break;
			}
			case FAMOUS: {
				Rank rank = new Rank(rankType.getName());
				rank.setColor(ChatColor.LIGHT_PURPLE);
				rank.setPrefix(CC.translate("&6[&d&o" + rankType.getName() + "&6] &d&o"));
				rank.setWeight(350);
				
				for(String permissions : this.getPermissions(permissionType, rankType)) {
					rank.addPermission(permissions);
				}
				
				rank.save();
				break;
			}
			case YOUTUBER: {
				Rank rank = new Rank(rankType.getName());
				rank.setColor(ChatColor.LIGHT_PURPLE);
				rank.setPrefix(CC.translate("&6[&b&o" + rankType.getName() + "&6] &b&o"));
				rank.setWeight(300);
				
				for(String permissions : this.getPermissions(permissionType, rankType)) {
					rank.addPermission(permissions);
				}
				
				rank.save();
				break;
			}
			case RETTO_PLUS: {
				Rank rank = new Rank(rankType.getName());
				rank.setColor(ChatColor.GOLD);
				rank.setPrefix(CC.translate("&e[&6&l" + rankType.getName().replace("+", "") + "&a+&e] &6&l"));
				rank.setWeight(250);
				
				for(String permissions : this.getPermissions(permissionType, rankType)) {
					rank.addPermission(permissions);
				}
				
				rank.save();
				break;
			}
			case RETTO: {
				Rank rank = new Rank(rankType.getName());
				rank.setColor(ChatColor.GOLD);
				rank.setPrefix(CC.translate("&e[&6&l" + rankType.getName() + "&e] &6&l"));
				rank.setWeight(200);
				
				for(String permissions : this.getPermissions(permissionType, rankType)) {
					rank.addPermission(permissions);
				}
				
				rank.save();
				break;
			}
			case GALEO: {
				Rank rank = new Rank(rankType.getName());
				rank.setColor(ChatColor.AQUA);
				rank.setPrefix(CC.translate("&6[&b&l" + rankType.getName() + "&6] &b&l"));
				rank.setWeight(150);
				
				for(String permissions : this.getPermissions(permissionType, rankType)) {
					rank.addPermission(permissions);
				}
				
				rank.save();
				break;
			}
			case LIBO: {
				Rank rank = new Rank(rankType.getName());
				rank.setColor(ChatColor.DARK_GREEN);
				rank.setPrefix(CC.translate("&6[&2&l" + rankType.getName() + "&6] &2&l"));
				rank.setWeight(100);
				
				for(String permissions : this.getPermissions(permissionType, rankType)) {
					rank.addPermission(permissions);
				}
				
				rank.save();
				break;
			}
			case CILO: {
				Rank rank = new Rank(rankType.getName());
				rank.setColor(ChatColor.GRAY);
				rank.setPrefix(CC.translate("&6[&7&l" + rankType.getName() + "&6] &7&l"));
				rank.setWeight(50);
				
				for(String permissions : this.getPermissions(permissionType, rankType)) {
					rank.addPermission(permissions);
				}
				
				rank.save();
				break;
			}
			case DEFAULT: {
				Rank rank = Rank.getRankByDisplayName(rankType.getName());
				rank.setColor(ChatColor.WHITE);
				rank.setPrefix(CC.translate("&6"));
				rank.setWeight(30);
				
				for(String permissions : this.getPermissions(permissionType, rankType)) {
					rank.addPermission(permissions);
				}
				
				rank.save();
				break;
			}
			default:
				break;
		}
	}
	
	public List<String> getPermissions(PermissionType permissionType, RankType rankType) {
		switch (permissionType) {
		case BUNKERS: {
			break;
		}
		/*
		 * 	Here you must add all the permissions of the HCFactions mode, also called as HCTeams
		 * */
		case HCFACTIONS: {
			switch (rankType) {
				case FOUNDER: {
					List<String> permissions = new ArrayList<String>();
					
					return permissions;
				}
				case DEVELOPER: {
					List<String> permissions = new ArrayList<String>();
					return permissions;
				}
				case OWNER: {
					List<String> permissions = new ArrayList<String>();
					return permissions;
				}
				case CO_OWNER: {
					List<String> permissions = new ArrayList<String>();
					return permissions;
				}
				case MANAGER: {
					List<String> permissions = new ArrayList<String>();
					return permissions;
				}
				case PLATFORM_ADMIN: {
					List<String> permissions = new ArrayList<String>();
					return permissions;
				}
				case SR_ADMINISTRATOR: {
					List<String> permissions = new ArrayList<String>();
					return permissions;
				}
				case ADMINISTRATOR: {
					List<String> permissions = new ArrayList<String>();
					return permissions;
				}
				case SR_MODERATOR: {
					List<String> permissions = new ArrayList<String>();
					return permissions;
				}
				case MODERATOR_PLUS: {
					List<String> permissions = new ArrayList<String>();
					return permissions;
				}
				case MODERATOR: {
					List<String> permissions = new ArrayList<String>();
					return permissions;
				}
				case TRIAL_MOD: {
					List<String> permissions = new ArrayList<String>();
					return permissions;
				}
				case BUILDER: {
					List<String> permissions = new ArrayList<String>();
					return permissions;
				}
				case FAMOUS: {
					List<String> permissions = new ArrayList<String>();
					return permissions;
				}
				case YOUTUBER: {
					List<String> permissions = new ArrayList<String>();
					return permissions;
				}
				case RETTO_PLUS: {
					List<String> permissions = new ArrayList<String>();
					return permissions;
				}
				case RETTO: {
					List<String> permissions = new ArrayList<String>();
					return permissions;
				}
				case GALEO: {
					List<String> permissions = new ArrayList<String>();
					return permissions;
				}
				case LIBO: {
					List<String> permissions = new ArrayList<String>();
					return permissions;
				}
				case CILO: {
					List<String> permissions = new ArrayList<String>();
					return permissions;
				}
				case DEFAULT: {
					List<String> permissions = new ArrayList<String>();
					return permissions;
				}
				default:
					break;
			}
			break;
		}
		/*
		 * 	Here you must add all the permissions of the Kits mode, also called as KitMap
		 * */
		case KITMAP: {
			switch (rankType) {
			case FOUNDER: {
				List<String> permissions = new ArrayList<String>();
				return permissions;
			}
			case DEVELOPER: {
				List<String> permissions = new ArrayList<String>();
				return permissions;
			}
			case OWNER: {
				List<String> permissions = new ArrayList<String>();
				return permissions;
			}
			case CO_OWNER: {
				List<String> permissions = new ArrayList<String>();
				return permissions;
			}
			case MANAGER: {
				List<String> permissions = new ArrayList<String>();
				return permissions;
			}
			case PLATFORM_ADMIN: {
				List<String> permissions = new ArrayList<String>();
				return permissions;
			}
			case SR_ADMINISTRATOR: {
				List<String> permissions = new ArrayList<String>();
				return permissions;
			}
			case ADMINISTRATOR: {
				List<String> permissions = new ArrayList<String>();
				return permissions;
			}
			case SR_MODERATOR: {
				List<String> permissions = new ArrayList<String>();
				return permissions;
			}
			case MODERATOR_PLUS: {
				List<String> permissions = new ArrayList<String>();
				return permissions;
			}
			case MODERATOR: {
				List<String> permissions = new ArrayList<String>();
				return permissions;
			}
			case TRIAL_MOD: {
				List<String> permissions = new ArrayList<String>();
				return permissions;
			}
			case BUILDER: {
				List<String> permissions = new ArrayList<String>();
				return permissions;
			}
			case FAMOUS: {
				List<String> permissions = new ArrayList<String>();
				return permissions;
			}
			case YOUTUBER: {
				List<String> permissions = new ArrayList<String>();
				return permissions;
			}
			case RETTO_PLUS: {
				List<String> permissions = new ArrayList<String>();
				return permissions;
			}
			case RETTO: {
				List<String> permissions = new ArrayList<String>();
				return permissions;
			}
			case GALEO: {
				List<String> permissions = new ArrayList<String>();
				return permissions;
			}
			case LIBO: {
				List<String> permissions = new ArrayList<String>();
				return permissions;
			}
			case CILO: {
				List<String> permissions = new ArrayList<String>();
				return permissions;
			}
			case DEFAULT: {
				List<String> permissions = new ArrayList<String>();
				return permissions;
			}
			default:
				break;
		}
			break;
		}
		case KOHI_SG: {
			break;
		}
		case PRACTICE: {
			break;
		}
		default:
			break;
		}
		return null;
	}
	
	public int getTicks() {
		return ticks;
	}
	
	public void setTicks(int ticks) {
		this.ticks = ticks;
	}
	
	public RespondeRank getResponde() {
		return responde;
	}
	
	public void setResponde(RespondeRank responde) {
		this.responde = responde;
	}
	
	public boolean isActive() {
		return active;
	}
	
	public void setActive(boolean active) {
		this.active = active;
	}
	
	public enum RespondeRank {
		STARTING,
		LOADING,
		FINISH;
	}
	
	public enum PermissionType {
		
		HCFACTIONS("HCTeams", 1),
		KITMAP("KitMap", 2),
		PRACTICE("Practice", 3),
		BUNKERS("Bunkers", 4),
		KOHI_SG("KohiSG", 5);
	
		private String name;
		private int id;
		
		private PermissionType(String name, int id) {
			this.name = name;
			this.id = id;
		}
		
		public String getName() {
			return name;
		}
		
		public int getId() {
			return id;
		}
	}
	
	public enum RankType {
		
		FOUNDER("Founder", true, true),
		DEVELOPER("Developer", true, true),
		OWNER("Owner", true, true),
		CO_OWNER("Co-Owner", true, true),
		MANAGER("Manager", true, true),
		PLATFORM_ADMIN("Platform-Admin", true, true),
		SR_ADMINISTRATOR("Sr.Administrator", true, true),
		ADMINISTRATOR("Administrator", true, true),
		SR_MODERATOR("Sr.Moderator", true, true),
		MODERATOR_PLUS("Moderator+", true, true),
		MODERATOR("Moderator", true, true),
		TRIAL_MOD("Trial-Mod", true, true),
		BUILDER("Builder", true, true),
		FAMOUS("Famous", false, true),
		YOUTUBER("Youtuber", false, true), 
		RETTO_PLUS("Retto+", false, true),
		RETTO("Retto", false, true),
		GALEO("Galeo", false, true),
		LIBO("Libo", false, true),
		CILO("Cilo", false, true),
		DEFAULT("Default", false, true);
		
		private String name;
		private boolean staff;
		private boolean loaded;
		
		private RankType(String name, boolean staff, boolean loaded) {
			this.name = name;
			this.staff = staff;
			this.loaded = loaded;
		}
		
		public String getName() {
			return name;
		}
		
		public boolean isStaff() {
			return staff;
		}
		
		public boolean isLoaded() {
			return loaded;
		}
	}
}
