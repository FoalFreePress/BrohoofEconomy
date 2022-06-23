package com.brohoof.brohoofeconomy.command;

import java.util.Optional;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.brohoof.brohoofeconomy.API;
import com.brohoof.brohoofeconomy.Account;
import com.brohoof.brohoofeconomy.Settings;

public class GiveMoneyCommand extends AbstractCommand {

    public GiveMoneyCommand(Settings settings, API api) {
        super(settings, api);
    }

    public boolean execute(CommandSender sender, String playerName, double cash) {
        Optional<Account> op = api.getAccount(playerName);
        if (!op.isPresent()) {
            sender.sendMessage(ChatColor.RED + "Target not found.");
            return true;
        }
        Account target = op.get();
        target.setCash(target.getCash() + cash);
        api.saveAccount(target);
        sender.sendMessage(ChatColor.DARK_GREEN + "Gave " + cash + " to " + target.getName());
        return true;
    }
}
