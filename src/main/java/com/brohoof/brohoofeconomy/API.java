package com.brohoof.brohoofeconomy;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class API {

    private Data data;

    API(Data data) {
        this.data = data;
    }
    
    public void createAccount(Account account) {
        Objects.requireNonNull(account);
        data.createAccount(account);
        
    }
    
    public Optional<Account> getAccount(String playerName) {
        Objects.requireNonNull(playerName);
        return data.getAccount(playerName);
    }
    
    public Optional<Account> getAccount(UUID playerUUID) {
        Objects.requireNonNull(playerUUID);
        return data.getAccount(playerUUID);
    }
    
    public ArrayList<Account> getAccounts() {
        return data.getAllAccounts();
    }
    
    private UUID getMD5Hash(String digest) {
        Objects.requireNonNull(digest);
        try {
            return UUID.nameUUIDFromBytes(MessageDigest.getInstance("MD5").digest(digest.getBytes("UTF-8")));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    public UUID getUUID(String playerName) {
        Objects.requireNonNull(playerName);
        Player ply = Bukkit.getPlayer(playerName);
        if (ply != null)
            return ply.getUniqueId();
        Optional<Account> op = getAccount(playerName);
        if (op.isPresent())
            return op.get().getUuid();
        return getMD5Hash(playerName);
    }

    public boolean hasAccount(String playerName) {
        Objects.requireNonNull(playerName);
        return data.getAccount(playerName).isPresent();
    }

    public boolean hasAccount(UUID playerUUID) {
        Objects.requireNonNull(playerUUID);
        return data.getAccount(playerUUID).isPresent();
    }

    public void saveAccount(Account account) {
        Objects.requireNonNull(account);
        data.saveAccount(account);
        
    }
}
