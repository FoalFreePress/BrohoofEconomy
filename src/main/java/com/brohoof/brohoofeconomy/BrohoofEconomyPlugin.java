package com.brohoof.brohoofeconomy;

import org.bukkit.plugin.java.JavaPlugin;
import org.sweetiebelle.lib.SweetieLib;
import org.sweetiebelle.lib.connection.ConnectionManager;

import com.brohoof.brohoofeconomy.command.handlers.GiveMoneyCommandHandler;
import com.brohoof.brohoofeconomy.command.handlers.MoneyCommandHandler;
import com.brohoof.brohoofeconomy.command.handlers.MoneyTopCommandHandler;
import com.brohoof.brohoofeconomy.command.handlers.PayCommandHandler;

public class BrohoofEconomyPlugin extends JavaPlugin {

    private Settings settings;
    private Data data;
    private VaultAPI vaultAPI;
    private EventListener listener;
    private API api;

    /**
     * {@inheritDoc}
     */
    @Override
    public void onEnable() {
        ConnectionManager connectionManager = null;
        try {
            connectionManager = SweetieLib.getPlugin().getConnection();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        settings = new Settings(this);
        data = new Data(this, settings, connectionManager);
        api = new API(data);
        vaultAPI = new VaultAPI(this, settings, api);
        listener = new EventListener(settings, api);
        getServer().getPluginManager().registerEvents(listener, this);
        getCommand("money").setExecutor(new MoneyCommandHandler(settings, api));
        getCommand("moneytop").setExecutor(new MoneyTopCommandHandler(settings, api));
        getCommand("pay").setExecutor(new PayCommandHandler(settings, api));
        getCommand("givemoney").setExecutor(new GiveMoneyCommandHandler(settings, api));
    }

    /**
     * @return the Vault API
     */
    public VaultAPI getVaultAPI() {
        return vaultAPI;
    }
}
