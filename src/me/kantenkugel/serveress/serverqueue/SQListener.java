package me.kantenkugel.serveress.serverqueue;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;

public class SQListener implements Listener {

	private ServerQueue sqplugin;
	
	public SQListener(ServerQueue plugin) {
		this.sqplugin = plugin;
	}
	
	@EventHandler
	public void PlayerLogin(PlayerLoginEvent event) {
		Player player = event.getPlayer();
		if(this.sqplugin.getServer().getOnlinePlayers().length >= this.sqplugin.normalslots) {
			if((player.hasPermission("serverqueue.vip") && this.sqplugin.reserveperm == true) || player.hasPermission("serverqueue.master") || this.sqplugin.vipplayers.contains((String) player.getName())) {
				if(this.sqplugin.getServer().getOnlinePlayers().length == this.sqplugin.maxplayers) {
					kicker(player, event);
				}
			} else {
				event.disallow(Result.KICK_FULL, "Server is full. If there are remaining slots, they are reserved for VIPs.");
			}
		}
	}
	
	private void kicker(Player pl, PlayerLoginEvent event) {
		if(((pl.hasPermission("serverqueue.master") || this.sqplugin.vipplayers.contains((String) pl.getName())) && this.sqplugin.kickvip) || (pl.hasPermission("serverqueue.vip") && this.sqplugin.kickperm)) {
			if(this.sqplugin.kicknotice == true) {
				if(!(this.sqplugin.joinhash.containsKey(pl.getName()))) {
					this.sqplugin.joinhash.put(pl.getName(), System.currentTimeMillis());
					event.disallow(Result.KICK_FULL, "Server is full. if you Join within the next 20 seconds, a player will get kicked.");
				} else if((System.currentTimeMillis() - this.sqplugin.joinhash.get(pl.getName())) > 1000 * 20) {
					this.sqplugin.joinhash.remove(pl.getName());
					this.sqplugin.joinhash.put(pl.getName(), System.currentTimeMillis());
					event.disallow(Result.KICK_FULL, "Server is full. if you Join within the next 20 seconds, a player will get kicked.");
				} else {
					this.sqplugin.joinhash.remove(pl.getName());
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
			} else {
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
		} else {
			event.disallow(Result.KICK_FULL, "The Server is currently full. Please try again soon");
		}
	}
}
