package com.mikusiek.teleportplugin;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class TpaCommand implements CommandExecutor {

    private static final HashMap<UUID, Long> cooldowns = new HashMap<>();
    private static final int COOLDOWN_TIME = 3;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Tylko gracze mogą używać tej komendy!");
            return true;
        }

        Player player = (Player) sender;

        if (cooldowns.containsKey(player.getUniqueId())) {
            long timeLeft = (cooldowns.get(player.getUniqueId()) / 1000) + COOLDOWN_TIME - (System.currentTimeMillis() / 1000);
            if (timeLeft > 0) {
                player.sendMessage(Utils.colorize("&cMusisz poczekać " + timeLeft + " sekund przed ponownym użyciem /tpa!"));
                return true;
            }
        }

        if (args.length != 1) {
            player.sendMessage(Utils.colorize("&cUżycie: /tpa <gracz>"));
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null || !target.isOnline()) {
            player.sendMessage(Utils.colorize("&cGracz nie znaleziony!"));
            return true;
        }

        if (player.equals(target)) {
            player.sendMessage(Utils.colorize("&cNie możesz wysłać prośby o teleportację do siebie!"));
            return true;
        }

        cooldowns.put(player.getUniqueId(), System.currentTimeMillis());

        TpaRequest request = new TpaRequest(player, target, target.getLocation());
        TpaRequest.requests.put(target, request);

        player.sendMessage(Utils.colorize("&aWysłałeś prośbę o teleportację do " + target.getName() + "."));
        target.sendMessage(Utils.colorize("&e" + player.getName() + " wysłał ci prośbę o teleportację. Wpisz /tpaccept " + player.getName() + " lub /tpadeny " + player.getName()));

        return true;
    }
}
