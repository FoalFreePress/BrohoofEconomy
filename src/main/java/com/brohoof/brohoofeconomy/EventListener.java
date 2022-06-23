package com.brohoof.brohoofeconomy;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class EventListener implements Listener {

    private API api;
    private Settings settings;

    public EventListener(Settings settings, API api) {
        this.settings = settings;
        this.api = api;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerLoginEvent(final PlayerJoinEvent event) {
        Player ply = event.getPlayer();
        if (!api.hasAccount(event.getPlayer().getUniqueId()))
            api.createAccount(new Account(0, ply.getUniqueId(), ply.getName(), settings.startAmount));
    }
}
