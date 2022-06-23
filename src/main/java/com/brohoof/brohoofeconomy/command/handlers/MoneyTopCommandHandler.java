package com.brohoof.brohoofeconomy.command.handlers;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.brohoof.brohoofeconomy.API;
import com.brohoof.brohoofeconomy.Settings;
import com.brohoof.brohoofeconomy.command.MoneyTopCommand;

public class MoneyTopCommandHandler extends AbstractCommandHandler {

    private MoneyTopCommand moneyTopCommand;

    public MoneyTopCommandHandler(Settings settings, API api) {
        moneyTopCommand = new MoneyTopCommand(settings, api);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("moneytop")) {
            return moneyTopCommand.execute(sender);
        }
        return false;
    }
}
