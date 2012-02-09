package me.kantenkugel.serveress.serverqueue;

import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class ServerQueue extends JavaPlugin {

	int maxplayers, normalslots, reservedslots;
	boolean reserveperm, kickperm, kickvip, kicknotice;
	List<Object> vipplayers;
	HashMap<String, Long> joinhash = new HashMap<String, Long>();
	public final Logger logger = Logger.getLogger("Minecraft");
	PluginDescriptionFile pdf;
	
	public void onEnable() {
		this.pdf = this.getDescription();
		PluginManager pm = getServer().getPluginManager();
		this.maxplayers = getServer().getMaxPlayers();
		this.getConfig().options().copyDefaults(true);
		this.saveDefaultConfig();
		this.saveConfig();
		loadconfigfile();
		pm.registerEvents(new SQListener(this), this);
		this.logger.info("[" + this.pdf.getName() + "] There are " + this.getServer().getOnlinePlayers().length + "/" + this.maxplayers + "(" + this.normalslots + "+" + this.reservedslots + ") Players online.");
		this.logger.info("[" + this.pdf.getName() + "] v" + this.pdf.getVersion() + " is enabled!");
	}
	
	public void onDisable() {
		this.logger.info("[" + this.pdf.getName() + "] is disabled!");
	}
	
	private void loadconfigfile() {
		this.reloadConfig();
		this.reservedslots = this.getConfig().getInt("ServerQueue.SlotsToReserve");
		this.normalslots = this.maxplayers - this.reservedslots;
		this.reserveperm = this.getConfig().getBoolean("ServerQueue.PermReserve");
		this.kickperm = this.getConfig().getBoolean("ServerQueue.Kick.Perm");
		this.kickvip = this.getConfig().getBoolean("ServerQueue.Kick.VIP");
		this.kicknotice = this.getConfig().getBoolean("ServerQueue.Kick.Notice");
		this.vipplayers = this.getConfig().getList("VIPs");
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String cmdlabel, String[] args) {
		if(cmdlabel.equalsIgnoreCase("sq") || cmdlabel.equalsIgnoreCase("serverqueue")) {
			if(sender != this.getServer().getConsoleSender() && !(sender.hasPermission("serverqueue.cmd"))) return noperm((Player) sender);
			if(args.length == 1) {
				switch(args[0]) {
				case "reload":
					if(sender != this.getServer().getConsoleSender() && !(sender.hasPermission("serverqueue.reload"))) return noperm((Player) sender);
					this.logger.info("["+this.pdf.getName()+"] Reloading Config-File!");
					if(sender != this.getServer().getConsoleSender()) sender.sendMessage("["+this.pdf.getName()+"] Reloading Config-File!");
					loadconfigfile();
					break;
				case "status":
					if(sender != this.getServer().getConsoleSender() && !(sender.hasPermission("serverqueue.status"))) return noperm((Player) sender);
					if(sender == this.getServer().getConsoleSender()) this.logger.info("["+this.pdf.getName()+"] Status: Players online: "+this.getServer().getOnlinePlayers().length+"/"+this.maxplayers+"("+(this.maxplayers - this.reservedslots)+"+"+this.reservedslots+")");
					else sender.sendMessage("["+this.pdf.getName()+"] Status: Players online: "+this.getServer().getOnlinePlayers().length+"/"+this.maxplayers+"("+(this.maxplayers - this.reservedslots)+"+"+this.reservedslots+")");
					break;
				default:
					return false;
				}
				return true;
				
			} else return false;
			
		} else return false;
	}
	
	private boolean noperm(Player pl) {
		pl.sendMessage(ChatColor.RED+"Your not allowed to do that");
		return true;
	}
}
