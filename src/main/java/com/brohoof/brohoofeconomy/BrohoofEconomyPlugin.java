package com.brohoof.brohoofeconomy;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class BrohoofEconomyPlugin extends JavaPlugin {

    private Settings settings;
    private Data data;
    private CommandHandler commandHandler;
    private VaultAPI vaultAPI;
    private EventListener listener;

    /**
     * {@inheritDoc}
     */
    @Override
    public void onEnable() {
        settings = new Settings(this);
        data = new Data(this);
        commandHandler = new CommandHandler(this);
        vaultAPI = new VaultAPI(this);
        listener = new EventListener(this);
        getServer().getPluginManager().registerEvents(listener, this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        return commandHandler.onCommand(sender, command, label, args);
    }

    /**
     * @return the settings
     */
    public Settings getSettings() {
        return settings;
    }

    /**
     * @return the Vault API
     */
    public VaultAPI getAPI() {
        return vaultAPI;
    }

    /**
     * @return the data
     */
    public Data getData() {
        return data;
    }

    /**
     * @return the commandHandler
     */
    public CommandHandler getCommandHandler() {
        return commandHandler;
    }

    public UUID getUUID(String string) {
        Player ply = Bukkit.getPlayer(string);
        if (ply != null)
            return ply.getUniqueId();
        Optional<Account> op = data.getAccount(string);
        if (op.isPresent())
            return op.get().getUuid();
        return getMD5Hash(string);
    }

    public UUID getMD5Hash(String digest) {
        try {
            return UUID.nameUUIDFromBytes(MessageDigest.getInstance("MD5").digest(digest.getBytes("UTF-8")));
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException e) {
            data.error(e);
            return null;
        }
    }
}
