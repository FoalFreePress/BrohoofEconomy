package com.brohoof.brohoofeconomy;

import java.util.Optional;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class EventListener implements Listener {
    
    private BrohoofEconomyPlugin plugin;
    public EventListener(BrohoofEconomyPlugin plugin) {
        this.plugin = plugin;
    }
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerLoginEvent(final PlayerJoinEvent event) {
        Player ply = event.getPlayer();
        Optional<Account> opA = plugin.getData().getAccount(ply.getUniqueId());
        if(opA.isPresent())
            return;
        plugin.getData().saveAccount(new Account(0, ply.getUniqueId(), ply.getName(), plugin.getSettings().startAmount));
    }
}
