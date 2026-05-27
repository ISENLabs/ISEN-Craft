package fr.isen.proxy.listeners;

import fr.isen.bungee.utils.MessageUtils;
import fr.isen.proxy.manager.BanManager;
import fr.isen.proxy.manager.MuteManager;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class ModerationListener implements Listener {

    private final BanManager banManager;
    private final MuteManager muteManager;

    public ModerationListener(BanManager banManager, MuteManager muteManager) {
        this.banManager = banManager;
        this.muteManager = muteManager;
    }

    @EventHandler
    public void onPreLogin(PreLoginEvent event) {
        String name = event.getConnection().getName();
        banManager.getActiveBanReason(name).ifPresent(reason -> {
            event.setCancelReason(MessageUtils.color("&cVous êtes banni du réseau.\n&7Raison: &f" + reason));
            event.setCancelled(true);
        });
    }

    @EventHandler
    public void onChat(ChatEvent event) {
        if (event.isCommand()) return;
        if (!(event.getSender() instanceof ProxiedPlayer player)) return;
        muteManager.getActiveMuteReason(player.getName()).ifPresent(reason -> {
            MessageUtils.sendMessage(player, "&cVous êtes muté. Raison: &f" + reason);
            event.setCancelled(true);
        });
    }
}
