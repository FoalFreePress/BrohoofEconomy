package com.brohoof.brohoofeconomy;

import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.ServicesManager;

import net.milkbowl.vault.economy.Economy;

public class VaultHook {

    private BrohoofEconomyPlugin plugin;
    private VaultAPI vaultAPI;

    public VaultHook(BrohoofEconomyPlugin plugin, VaultAPI vaultAPI) {
        this.plugin = plugin;
        this.vaultAPI = vaultAPI;
    }
    
    
    public void hook() {
        final ServicesManager sm = plugin.getServer().getServicesManager();
        sm.register(Economy.class, vaultAPI, plugin, ServicePriority.High);
    }
    
    
    public void unHook() {
        final ServicesManager sm = plugin.getServer().getServicesManager();
        sm.unregister(Economy.class, vaultAPI);
    }

}
