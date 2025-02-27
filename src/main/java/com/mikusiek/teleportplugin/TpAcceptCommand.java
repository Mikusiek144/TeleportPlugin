package com.mikusiek.teleportplugin;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class TpAcceptCommand implements CommandExecutor {

    private static final int TELEPORT_DELAY = 5;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Tylko gracze mogą używać tej komendy!");
            return true;
        }

        Player player = (Player) sender;

        if (args.length != 1) {
            player.sendMessage(Utils.colorize("&cUżycie: /tpaccept <gracz> lub /tpaccept *"));
            return true;
        }

        if (args[0].equals("*")) {
            if (!TpaRequest.requests.containsKey(player)) {
                player.sendMessage(Utils.colorize("&cNie masz żadnych próśb o teleportację!"));
                return true;
            }

            TpaRequest request = TpaRequest.requests.get(player);
            startTeleportCountdown(request);
            return true;
        }

        Player requester = Bukkit.getPlayer(args[0]);
        if (requester == null || !TpaRequest.requests.containsKey(player)) {
            player.sendMessage(Utils.colorize("&cNie masz prośby o teleportację od tego gracza!"));
            return true;
        }

        TpaRequest request = TpaRequest.requests.get(player);
        startTeleportCountdown(request);

        return true;
    }

    private void startTeleportCountdown(TpaRequest request) {
        Player requester = request.getRequester();
        Player target = request.getTarget();

        target.sendMessage(Utils.colorize("&aZaakceptowałeś teleportację! Gracz " + requester.getName() + " zostanie teleportowany za " + TELEPORT_DELAY + " sekund."));

        requester.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, TELEPORT_DELAY * 20, 0, false, false));

        new BukkitRunnable() {
            int timeLeft = TELEPORT_DELAY;

            @Override
            public void run() {
                if (timeLeft <= 0) {
                    requester.teleport(request.getFixedLocation());
                    requester.sendMessage(Utils.colorize("&aZostałeś teleportowany do " + target.getName() + "!"));
                    requester.removePotionEffect(PotionEffectType.BLINDNESS); 
                    cancel();
                    return;
                }

                requester.sendTitle(Utils.colorize("&eTeleportacja za..."), Utils.colorize("&c" + timeLeft + " sekund"), 0, 20, 10);
                timeLeft--;
            }
        }.runTaskTimer(Main.getInstance(), 0, 20);
    }
}
