package com.brohoof.brohoofeconomy.command;

import java.util.Optional;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.brohoof.brohoofeconomy.API;
import com.brohoof.brohoofeconomy.Account;
import com.brohoof.brohoofeconomy.Settings;

public class PayCommand extends AbstractCommand {

    private GiveMoneyCommand giveMoneyCommand;

    public PayCommand(Settings settings, API api) {
        super(settings, api);
        giveMoneyCommand = new GiveMoneyCommand(settings, api);
    }

    public boolean execute(CommandSender sender, String targetPlayerName, double cashToSend) {
        if (sender instanceof Player) {
            Account source = api.getAccount(((Player) sender).getUniqueId()).get();
            Optional<Account> targetO = api.getAccount(targetPlayerName);
            if (targetO.isPresent()) {
                Account target = targetO.get();
                if (source.equals(target)) {
                    sender.sendMessage(ChatColor.RED + "You can't send money to yourself!");
                    return true;
                }
                if (cashToSend <= 0.0) {
                    sender.sendMessage(ChatColor.RED + "You can't send negative money!");
                    return true;
                }
                if (source.getCash() >= cashToSend) {
                    source.setCash(source.getCash() - cashToSend);
                    target.setCash(target.getCash() + cashToSend);
                    api.saveAccount(source);
                    api.saveAccount(target);
                    sender.sendMessage(String.format(ChatColor.DARK_GREEN + "You've sent %10.2f " + settings.curNamePlural + " from " + target.getName(), cashToSend));
                    Player pTarget = Bukkit.getPlayer(target.getUuid());
                    if (pTarget != null)
                        pTarget.sendMessage(String.format(ChatColor.DARK_GREEN + "You've recieved %10.2f " + settings.curNamePlural + " from " + source.getName(), cashToSend));
                    return true;
                }
                sender.sendMessage(ChatColor.RED + "You do not have enough cash to send!");
                return true;
            }
        }
        return giveMoneyCommand.execute(sender, targetPlayerName, cashToSend);
    }
}
