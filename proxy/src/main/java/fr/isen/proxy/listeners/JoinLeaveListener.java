package fr.isen.proxy.listeners;

import fr.isen.bungee.utils.MessageUtils;
import fr.isen.proxy.manager.ConfigManager;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class JoinLeaveListener implements Listener {

    private final ConfigManager configManager;

    public JoinLeaveListener(ConfigManager configManager) {
        this.configManager = configManager;
    }

    @EventHandler
    public void onJoin(PostLoginEvent event) {
        if (!configManager.getBoolean("join-leave.enabled", true)) return;
        String msg = configManager.getString("join-leave.join", "&a[+] &f{player} &aa rejoint le réseau.")
                .replace("{player}", event.getPlayer().getName());
        MessageUtils.broadcast(msg);
    }

    @EventHandler
    public void onLeave(PlayerDisconnectEvent event) {
        if (!configManager.getBoolean("join-leave.enabled", true)) return;
        String msg = configManager.getString("join-leave.leave", "&c[-] &f{player} &ca quitté le réseau.")
                .replace("{player}", event.getPlayer().getName());
        MessageUtils.broadcast(msg);
    }
}
