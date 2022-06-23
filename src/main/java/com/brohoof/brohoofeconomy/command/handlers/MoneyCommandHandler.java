package com.brohoof.brohoofeconomy.command.handlers;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.brohoof.brohoofeconomy.API;
import com.brohoof.brohoofeconomy.Settings;
import com.brohoof.brohoofeconomy.command.BalanceCommand;

public class MoneyCommandHandler extends AbstractCommandHandler {

    private BalanceCommand balanceCommand;

    public MoneyCommandHandler(Settings settings, API api) {
        balanceCommand = new BalanceCommand(settings, api);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("money")) {
            switch (args.length) {
                case 0: {
                    // They did just /money
                    return balanceCommand.execute(sender, sender.getName());
                }
                case 1: {
                    if (sender.hasPermission("brohoofecon.money.other")) {
                        return balanceCommand.execute(sender, args[0]);
                    }
                    sender.sendMessage(ChatColor.RED + "You do not have permission.");
                    return true;
                }
            }
            return false;
        }
        return false;
    }
}
