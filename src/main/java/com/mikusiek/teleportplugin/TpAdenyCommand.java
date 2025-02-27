package com.mikusiek.teleportplugin;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TpAdenyCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Tylko gracze mogą używać tej komendy!");
            return true;
        }

        Player player = (Player) sender;

        if (args.length != 1) {
            player.sendMessage(Utils.colorize("&cUżycie: /tpadeny <gracz> lub /tpadeny *"));
            return true;
        }

        if (args[0].equals("*")) {
            if (!TpaRequest.requests.containsKey(player)) {
                player.sendMessage(Utils.colorize("&cNie masz żadnych próśb o teleportację!"));
                return true;
            }

            TpaRequest request = TpaRequest.requests.remove(player);
            request.getRequester().sendMessage(Utils.colorize("&cTwoja prośba o teleportację została odrzucona!"));
            player.sendMessage(Utils.colorize("&aOdrzuciłeś wszystkie prośby o teleportację."));
            return true;
        }

        Player requester = Bukkit.getPlayer(args[0]);
        if (requester == null || !TpaRequest.requests.containsKey(player)) {
            player.sendMessage(Utils.colorize("&cNie masz prośby o teleportację od tego gracza!"));
            return true;
        }

        TpaRequest.requests.remove(player);
        requester.sendMessage(Utils.colorize("&cTwoja prośba o teleportację została odrzucona!"));
        player.sendMessage(Utils.colorize("&aOdrzuciłeś prośbę o teleportację od " + requester.getName() + "."));

        return true;
    }
}
