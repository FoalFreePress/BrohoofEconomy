package com.brohoof.brohoofeconomy.command;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.brohoof.brohoofeconomy.API;
import com.brohoof.brohoofeconomy.Account;
import com.brohoof.brohoofeconomy.Settings;

public class MoneyTopCommand extends AbstractCommand {

    public MoneyTopCommand(Settings settings, API api) {
        super(settings, api);
    }
    
    
    public boolean execute(CommandSender sender) {
        ArrayList<Account> accounts = api.getAccounts();
        accounts.sort(null);
        sender.sendMessage(ChatColor.LIGHT_PURPLE + "------------------------------------");
        int max = 10 > accounts.size() ? accounts.size() : 10;
        for (int i = 0; i < max; i++) {
            Account acc = accounts.get(i);
            sender.sendMessage(String.format("#%d:" + ChatColor.LIGHT_PURPLE + "%20s" + ChatColor.RESET + " - " + ChatColor.DARK_GREEN + "%10.2f", i + 1, acc.getName(), acc.getCash()));
        }
        sender.sendMessage(ChatColor.LIGHT_PURPLE + "------------------------------------");
        return true;
    }
}
