package com.brohoof.brohoofeconomy;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import net.milkbowl.vault.economy.AbstractEconomy;
import net.milkbowl.vault.economy.EconomyResponse;

public class VaultAPI extends AbstractEconomy {

    private BrohoofEconomyPlugin plugin;
    private API api;
    private Settings settings;

    public VaultAPI(BrohoofEconomyPlugin brohoofEconomyPlugin, Settings settings, API api) {
        plugin = brohoofEconomyPlugin;
        this.settings = settings;
        this.api = api;
    }

    @Override
    public boolean isEnabled() {
        return plugin.isEnabled();
    }

    @Override
    public String getName() {
        return plugin.getName();
    }

    @Override
    public boolean hasBankSupport() {
        return false;
    }

    @Override
    public int fractionalDigits() {
        return -1;
    }

    @Override
    public String format(double amount) {
        return String.format("%.2lf", amount);
    }

    @Override
    public String currencyNamePlural() {
        return settings.curNamePlural;
    }

    @Override
    public String currencyNameSingular() {
        return settings.curName;
    }

    @Override
    public boolean hasAccount(String playerName) {
        return api.hasAccount(playerName);
    }

    @Override
    public boolean hasAccount(String playerName, String worldName) {
        return hasAccount(playerName);
    }

    @Override
    public double getBalance(String playerName) {
        Optional<Account> account = api.getAccount(playerName);
        if (account.isPresent())
            return account.get().getCash();
        return 0;
    }

    @Override
    public double getBalance(String playerName, String world) {
        return getBalance(playerName);
    }

    @Override
    public boolean has(String playerName, double amount) {
        Optional<Account> account = api.getAccount(playerName);
        if (account.isPresent())
            if (account.get().getCash() >= amount)
                return true;
        return false;
    }

    @Override
    public boolean has(String playerName, String worldName, double amount) {
        return has(playerName, amount);
    }

    @Override
    public EconomyResponse withdrawPlayer(String playerName, double amount) {
        Optional<Account> op = api.getAccount(playerName);
        if (!op.isPresent())
            return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "That player was no found!");
        Account acc = op.get();
        acc.setCash(acc.getCash() - amount);
        api.saveAccount(acc);
        return new EconomyResponse(amount, acc.getCash(), EconomyResponse.ResponseType.SUCCESS, "0");
    }

    @Override
    public EconomyResponse withdrawPlayer(String playerName, String worldName, double amount) {
        return withdrawPlayer(playerName, amount);
    }

    @Override
    public EconomyResponse depositPlayer(String playerName, double amount) {
        Optional<Account> op = api.getAccount(playerName);
        if (!op.isPresent())
            return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "That player was no found!");
        Account acc = op.get();
        acc.setCash(acc.getCash() + amount);
        api.saveAccount(acc);
        return new EconomyResponse(amount, acc.getCash(), EconomyResponse.ResponseType.SUCCESS, "0");
    }

    @Override
    public EconomyResponse depositPlayer(String playerName, String worldName, double amount) {
        return depositPlayer(playerName, amount);
    }

    @Override
    public EconomyResponse createBank(String name, String player) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "");
    }

    @Override
    public EconomyResponse deleteBank(String name) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "");
    }

    @Override
    public EconomyResponse bankBalance(String name) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "");
    }

    @Override
    public EconomyResponse bankHas(String name, double amount) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "");
    }

    @Override
    public EconomyResponse bankWithdraw(String name, double amount) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "");
    }

    @Override
    public EconomyResponse bankDeposit(String name, double amount) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "");
    }

    @Override
    public EconomyResponse isBankOwner(String name, String playerName) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "");
    }

    @Override
    public EconomyResponse isBankMember(String name, String playerName) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "");
    }

    @Override
    public List<String> getBanks() {
        return new ArrayList<String>(0);
    }

    @Override
    public boolean createPlayerAccount(String playerName) {
        api.saveAccount(new Account(0, api.getUUID(playerName), playerName, 0.0));
        return true;
    }

    @Override
    public boolean createPlayerAccount(String playerName, String worldName) {
        return createPlayerAccount(playerName);
    }
}
