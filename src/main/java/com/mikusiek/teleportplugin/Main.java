package com.mikusiek.teleportplugin;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    private static Main instance;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        getCommand("tpa").setExecutor(new TpaCommand());
        getCommand("tpaccept").setExecutor(new TpAcceptCommand());
        getCommand("tpadeny").setExecutor(new TpAdenyCommand());

        getCommand("tpa").setTabCompleter(new TabCompleterHandler());
        getCommand("tpaccept").setTabCompleter(new TabCompleterHandler());
        getCommand("tpadeny").setTabCompleter(new TabCompleterHandler());

        getLogger().info("TeleportPlugin został włączony!");
    }

    public static Main getInstance() {
        return instance;
    }
}