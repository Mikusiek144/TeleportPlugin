package com.mikusiek.teleportplugin;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class TpaRequest {
    private final Player requester;
    private final Player target;
    private final Location fixedLocation;
    public static final Map<Player, TpaRequest> requests = new HashMap<>();

    public TpaRequest(Player requester, Player target, Location fixedLocation) {
        this.requester = requester;
        this.target = target;
        this.fixedLocation = fixedLocation.clone();
    }

    public Location getFixedLocation() {
        return fixedLocation;
    }

    public Player getRequester() {
        return requester;
    }

    public Player getTarget() {
        return target;
    }
}
