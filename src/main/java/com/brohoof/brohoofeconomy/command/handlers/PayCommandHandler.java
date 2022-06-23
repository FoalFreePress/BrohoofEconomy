package com.brohoof.brohoofeconomy.command.handlers;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.brohoof.brohoofeconomy.API;
import com.brohoof.brohoofeconomy.Settings;
import com.brohoof.brohoofeconomy.command.PayCommand;

public class PayCommandHandler extends AbstractCommandHandler {

    private PayCommand payCommand;

    public PayCommandHandler(Settings settings, API api) {
        payCommand = new PayCommand(settings, api);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("pay")) {
            if (args.length < 2)
                return false;
            return payCommand.execute(sender, args[0], Double.valueOf(args[1]));
        }
        return false;
    }
}
