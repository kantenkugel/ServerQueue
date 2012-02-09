package me.kantenkugel.serveress.serverqueue;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;

public class SQListener implements Listener {

	private ServerQueue sqplugin;													//ServerQueue instance
	
	public SQListener(ServerQueue plugin) {
		this.sqplugin = plugin;
	}
	
	@EventHandler
	public void PlayerLogin(PlayerLoginEvent event) {
		Player player = event.getPlayer();											//get joining player
		if(this.sqplugin.getServer().getOnlinePlayers().length >= this.sqplugin.normalslots) {//all normal slots used?
			//check if vip and allowed to join
			if((player.hasPermission("serverqueue.vip") && this.sqplugin.reserveperm == true) || player.hasPermission("serverqueue.master") || this.sqplugin.vipplayers.contains((String) player.getName())) {
				//if all slots are used, try to kick
				if(this.sqplugin.getServer().getOnlinePlayers().length == this.sqplugin.maxplayers) {
					kicker(player, event);											//seperate function
				}
				//else show them default server full message
			} else {																//if no vip, dont allow connection
				event.disallow(Result.KICK_FULL, "Server is full. If there are remaining slots, they are reserved for VIPs.");
			}
		}
	}
	
	private void kicker(Player pl, PlayerLoginEvent event) {						//kicks players for vips
		//first check if the joining player is allowed to kick others
		if(((pl.hasPermission("serverqueue.master") || this.sqplugin.vipplayers.contains((String) pl.getName())) && this.sqplugin.kickvip) || (pl.hasPermission("serverqueue.vip") && this.sqplugin.kickperm)) {
		
			if(this.sqplugin.kicknotice == true) {									//if notice is enabled
				if(!(this.sqplugin.joinhash.containsKey(pl.getName()))) {			//not in hashmap, registering with current timestamp
					this.sqplugin.joinhash.put(pl.getName(), System.currentTimeMillis());
					event.disallow(Result.KICK_FULL, "Server is full. if you Join within the next 20 seconds, a player will get kicked.");
				} else if((System.currentTimeMillis() - this.sqplugin.joinhash.get(pl.getName())) > 1000 * 20) {//waited to long... registering again
					this.sqplugin.joinhash.remove(pl.getName());
					this.sqplugin.joinhash.put(pl.getName(), System.currentTimeMillis());
					event.disallow(Result.KICK_FULL, "Server is full. if you Join within the next 20 seconds, a player will get kicked.");
				} else {																						//kicking other player
					this.sqplugin.joinhash.remove(pl.getName());
					for(Player online: this.sqplugin.getServer().getOnlinePlayers()) {
						//check if the target player is vip or has the permission serverqueue.nokick
						if(!(online.hasPermission("serverqueue.nokick") || online.hasPermission("serverqueue.master") || online.hasPermission("serverqueue.vip") || this.sqplugin.vipplayers.contains(online.getName()))) {
							this.sqplugin.logger.info("["+this.sqplugin.pdf.getName()+"] Kicking player "+online.getName()+" for a VIP.");
							online.kickPlayer("You have been kicked for a VIP");
							break;
						}
					}
					if(this.sqplugin.getServer().getOnlinePlayers().length == this.sqplugin.maxplayers) {		//no player was kicked... notify joining player
						event.disallow(Result.KICK_FULL, "No kickable Player Found... please try again later");
					} else {																					//slot opened... allowing join
						event.allow();
					}
				}
			} else {																							//no notify... just kick
				for(Player online: this.sqplugin.getServer().getOnlinePlayers()) {
					if(!(online.hasPermission("serverqueue.nokick") || online.hasPermission("serverqueue.master") || online.hasPermission("serverqueue.vip") || this.sqplugin.vipplayers.contains(online.getName()))) {
						this.sqplugin.logger.info("["+this.sqplugin.pdf.getName()+"] Kicking player "+online.getName()+" for a VIP.");
						online.kickPlayer("You have been kicked for a VIP");
						break;
					}
				}
				if(this.sqplugin.getServer().getOnlinePlayers().length == this.sqplugin.maxplayers) {
					event.disallow(Result.KICK_FULL, "No kickable Player Found... please try again later");
				} else {
					event.allow();
				}
			}
		}
	}
}
