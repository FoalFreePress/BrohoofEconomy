package com.brohoof.brohoofeconomy;

import java.util.ArrayList;
import java.util.Optional;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandHandler {

    private BrohoofEconomyPlugin plugin;

    public CommandHandler(BrohoofEconomyPlugin plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        switch (command.getName().toLowerCase()) {
            case "money": {
                switch (args.length) {
                    case 0: {
                        // They did just /money
                        showBalance(sender, sender.getName());
                        return true;
                    }
                    case 1: {
                        if (sender.hasPermission("brohoofecon.money.other")) {
                            showBalance(sender, args[0]);
                            return true;
                        }
                        sender.sendMessage(ChatColor.RED + "You do not have permission.");
                        return true;
                    }
                }
                return false;
            }
            case "moneytop": {
                moneyTop(sender);
                return true;
            }
            case "balance": {
                switch (args.length) {
                    case 0: {
                        // They did just /balance
                        showBalance(sender, sender.getName());
                        return true;
                    }
                    case 1: {
                        if (sender.hasPermission("brohoofecon.money.other")) {
                            showBalance(sender, args[0]);
                            return true;
                        }
                        sender.sendMessage(ChatColor.RED + "You do not have permission.");
                        return true;
                    }
                }
                return false;
            }
            case "pay": {
                if (args.length < 2)
                    return false;
                pay(sender, sender.getName(), args[0], Double.valueOf(args[1]));
                return true;
            }
            case "givemoney": {
                if (args.length < 2)
                    return false;
                if (!sender.hasPermission("brohoofecon.give"))
                    return false;
                give(sender, args[0], Double.valueOf(args[1]));
            }
        }
        return false;
    }

    private void give(CommandSender sender, String name, double cash) {
        Optional<Account> op = plugin.getData().getAccount(name);
        if (!op.isPresent()) {
            sender.sendMessage(ChatColor.RED + "Target not found.");
            return;
        }
        Account target = op.get();
        target.setCash(target.getCash() + cash);
        plugin.getData().saveAccount(target);
        sender.sendMessage(ChatColor.DARK_GREEN + "Gave " + cash + " to " + target.getName());
    }

    private void moneyTop(CommandSender sender) {
        ArrayList<Account> accounts = plugin.getData().getAllAccounts();
        accounts.sort(null);
        sender.sendMessage(ChatColor.LIGHT_PURPLE + "------------------------------------");
        int max = 10 > accounts.size() ? accounts.size() : 10;
        for (int i = 0; i < max; i++) {
            Account acc = accounts.get(i);
            sender.sendMessage(String.format("#%d:" + ChatColor.LIGHT_PURPLE + "%20s" + ChatColor.RESET + " - " + ChatColor.DARK_GREEN + "%10.2f", i + 1, acc.getName(), acc.getCash()));
        }
        sender.sendMessage(ChatColor.LIGHT_PURPLE + "------------------------------------");
    }

    private void pay(CommandSender sender, String sourceCash, String toPay, double cashToSend) {
        Optional<Account> sourceO = plugin.getData().getAccount(sourceCash);
        Optional<Account> targetO = plugin.getData().getAccount(toPay);
        if (sourceO.isPresent() && targetO.isPresent()) {
            Account source = sourceO.get();
            Account target = targetO.get();
            if(source.equals(target))
            {
                sender.sendMessage(ChatColor.RED + "You can't send money to yourself!");
                return;
            }
            if(cashToSend <= 0.0)
            {
                sender.sendMessage(ChatColor.RED + "You can't send negative money!");
                return;
            }
            if (source.getCash() >= cashToSend) {
                source.setCash(source.getCash() - cashToSend);
                target.setCash(target.getCash() + cashToSend);
                plugin.getData().saveAccount(source);
                plugin.getData().saveAccount(target);
                sender.sendMessage(String.format(ChatColor.DARK_GREEN + "You've sent %10.2f "  + plugin.getSettings().curNamePlural + " from " + target.getName(), cashToSend));
                Player pTarget = Bukkit.getPlayer(target.getUuid());
                if (pTarget != null)
                    pTarget.sendMessage(String.format(ChatColor.DARK_GREEN + "You've recieved %10.2f " + plugin.getSettings().curNamePlural + " from " + source.getName(), cashToSend));
            } else
                sender.sendMessage(ChatColor.RED + "You do not have enough cash to send!");
        } else
            sender.sendMessage(ChatColor.RED + "Either the target or the source was not found.");
    }

    private void showBalance(CommandSender toSend, String target) {
        Optional<Account> acco = plugin.getData().getAccount(target);
        if (acco.isPresent()) {
            Account acc = acco.get();
            toSend.sendMessage(String.format(ChatColor.LIGHT_PURPLE + acc.getName() + ChatColor.RESET + " has " + ChatColor.DARK_GREEN + "%10.2f", acc.getCash()));
        } else
            toSend.sendMessage(ChatColor.RED + "No account by that username found!");
    }
}
