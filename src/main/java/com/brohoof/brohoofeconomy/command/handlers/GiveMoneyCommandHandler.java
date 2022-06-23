package com.brohoof.brohoofeconomy.command.handlers;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.brohoof.brohoofeconomy.API;
import com.brohoof.brohoofeconomy.Settings;
import com.brohoof.brohoofeconomy.command.GiveMoneyCommand;

public class GiveMoneyCommandHandler extends AbstractCommandHandler {

    private GiveMoneyCommand giveMoneyCommand;

    public GiveMoneyCommandHandler(Settings settings, API api) {
        giveMoneyCommand = new GiveMoneyCommand(settings, api);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("givemoney")) {
            if (args.length < 2)
                return false;
            if (!sender.hasPermission("brohoofecon.give"))
                return false;
            double cashToGive;
            try {
                cashToGive = Double.valueOf(args[1]);
            } catch (NumberFormatException e) {
                sender.sendMessage(ChatColor.RED + "Invalid double: " + args[1]);
                return true;
            }
            giveMoneyCommand.execute(sender, args[0], cashToGive);
        }
        return false;
    }
}
