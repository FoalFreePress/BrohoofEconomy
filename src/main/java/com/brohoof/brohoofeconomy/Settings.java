package com.brohoof.brohoofeconomy;

import org.bukkit.configuration.file.FileConfiguration;

public class Settings {

    public String dbPrefix;
    public String dbDatabase;
    public String dbHost;
    public String dbUser;
    public String dbPass;
    public int dbPort;
    public boolean showQuery;
    public boolean stackTraces;
    public String curName;
    public String curNamePlural;
    public String curNameSmall;
    public String curNameSmallPlural;
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
        showQuery = config.getBoolean("general.showquery");
        // SQL Settings
        dbHost = config.getString("database.host");
        dbPort = config.getInt("database.port");
        dbUser = config.getString("database.username");
        dbPass = config.getString("database.password");
        dbDatabase = config.getString("database.database");
        dbPrefix = config.getString("database.prefix");
        // Currency Settings
        curName = config.getString("economy.currencyName");
        curNamePlural = config.getString("economy.currencyPlural");
        curNameSmall = config.getString("economy.currencySmall");
        curNameSmallPlural = config.getString("economy.currencySmallPlura");
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
