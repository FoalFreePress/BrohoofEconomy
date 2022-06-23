package com.brohoof.brohoofeconomy.command;

import java.util.Optional;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.brohoof.brohoofeconomy.API;
import com.brohoof.brohoofeconomy.Account;
import com.brohoof.brohoofeconomy.Settings;

public class BalanceCommand extends AbstractCommand {

    public BalanceCommand(Settings settings, API api) {
        super(settings, api);
    }

    public boolean execute(CommandSender toSend, String target) {
        Optional<Account> acco = api.getAccount(target);
        if (acco.isPresent()) {
            Account acc = acco.get();
            toSend.sendMessage(String.format(ChatColor.LIGHT_PURPLE + acc.getName() + ChatColor.RESET + " has " + settings.curSymbol + ChatColor.DARK_GREEN + "%10.2f", acc.getCash()));
            return true;
        }
        toSend.sendMessage(ChatColor.RED + "No account by that username found!");
        return true;
    }
}
