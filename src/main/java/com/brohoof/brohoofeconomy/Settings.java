package com.brohoof.brohoofeconomy;

import org.bukkit.configuration.file.FileConfiguration;

public class Settings {

    public String dbPrefix;
    public boolean stackTraces;
    public String curName;
    public String curNamePlural;
    public String curSymbol;
    public double startAmount;
    private BrohoofEconomyPlugin plugin;
    private FileConfiguration config;

    public Settings(final BrohoofEconomyPlugin plugin) {
        this.plugin = plugin;
        plugin.saveDefaultConfig();
        config = plugin.getConfig();
        readSettings();
    }

    /**
     * Reads settings
     *
     * @param pConfig
     *            the config
     */
    protected void readSettings() {
        // Debug Settings
        stackTraces = config.getBoolean("general.printStackTraces");
        // SQL Settings
        dbPrefix = config.getString("database.prefix");
        // Currency Settings
        curName = config.getString("economy.currencyName");
        curNamePlural = config.getString("economy.currencyPlural");
        curSymbol = config.getString("economy.currencySymbol");
        startAmount = config.getDouble("economy.startAmount");
    }

    /**
     * Reloads settings
     */
    public void reloadSettings() {
        plugin.reloadConfig();
        config = plugin.getConfig();
        readSettings();
    }
}
