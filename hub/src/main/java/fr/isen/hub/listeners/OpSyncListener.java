package fr.isen.hub.listeners;

import fr.isen.hub.HubPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.scheduler.BukkitTask;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Collection;

public class OpSyncListener implements Listener {

    public static final String CHANNEL = "fr.isen:opsync";
    private static final long SYNC_INITIAL_DELAY_TICKS = 200L;
    private static final long SYNC_PERIOD_TICKS = 1200L;
    private static final long OP_COMMAND_DELAY_TICKS = 2L;

    private final HubPlugin plugin;
    private BukkitTask syncTask;

    public OpSyncListener(HubPlugin plugin) {
        this.plugin = plugin;
        plugin.getServer().getMessenger().registerOutgoingPluginChannel(plugin, CHANNEL);
        scheduleSyncTask();
    }

    private void scheduleSyncTask() {
        syncTask = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                sendOpSync(player, player.getName(), player.isOp());
            }
        }, SYNC_INITIAL_DELAY_TICKS, SYNC_PERIOD_TICKS);
    }

    public void shutdown() {
        if (syncTask != null && !syncTask.isCancelled()) {
            syncTask.cancel();
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Bukkit.getScheduler().runTaskLater(plugin, () ->
                sendOpSync(player, player.getName(), player.isOp()), OP_COMMAND_DELAY_TICKS);
    }

    @EventHandler
    public void onOpCommand(PlayerCommandPreprocessEvent event) {
        String message = event.getMessage().toLowerCase().trim();
        if (!message.startsWith("/op ") && !message.startsWith("/deop ")) return;

        String[] args = event.getMessage().trim().split("\\s+");
        if (args.length < 2) return;

        String targetPlayer = args[1];
        boolean isOp = message.startsWith("/op ");

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            sendOpSync(event.getPlayer(), targetPlayer, isOp);
            refreshTargetBoard(targetPlayer);
        }, OP_COMMAND_DELAY_TICKS);
    }

    @EventHandler
    public void onServerCommand(ServerCommandEvent event) {
        String command = event.getCommand().toLowerCase().trim();
        if (!command.startsWith("op ") && !command.startsWith("deop ")) return;

        String[] args = event.getCommand().trim().split("\\s+");
        if (args.length < 2) return;

        String targetPlayer = args[1];
        boolean isOp = command.startsWith("op ");

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            Collection<? extends Player> online = Bukkit.getOnlinePlayers();
            if (online.isEmpty()) {
                plugin.logger.log("OpSync: aucun joueur en ligne pour relayer le message", "WARN");
                return;
            }
            sendOpSync(online.iterator().next(), targetPlayer, isOp);
            refreshTargetBoard(targetPlayer);
        }, OP_COMMAND_DELAY_TICKS);
    }

    private void refreshTargetBoard(String playerName) {
        Player target = Bukkit.getPlayer(playerName);
        if (target != null && plugin.scoreboardManager != null) {
            plugin.scoreboardManager.refreshPlayer(target);
        }
    }

    private void sendOpSync(Player carrier, String targetPlayer, boolean isOp) {
        if (!carrier.isOnline()) return;
        try {
            ByteArrayOutputStream b = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(b);
            out.writeUTF(targetPlayer);
            out.writeBoolean(isOp);
            carrier.sendPluginMessage(plugin, CHANNEL, b.toByteArray());
        } catch (IOException e) {
            plugin.logger.log("Erreur envoi OpSync", "ERROR");
        }
    }
}
