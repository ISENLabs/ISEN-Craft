package fr.isen.hub.managers;

import fr.isen.common.config.IManager;
import fr.isen.hub.HubPlugin;
import fr.isen.paper.utils.MessageUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitTask;

import java.util.List;

public class TabListManager extends IManager<HubPlugin> implements Listener {

    private BukkitTask refreshTask;

    public TabListManager(HubPlugin plugin) {
        super(plugin, plugin.logger, "TabListManager");
        plugin.registerListener(this);
        scheduleRefresh();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        updateTab(event.getPlayer());
    }

    public void updateTab(Player player) {
        if (!plugin.configManager.getBoolean("tab-list.enabled", true)) return;

        List<String> headerLines = plugin.configManager.getStringList("tab-list.header");
        List<String> footerLines = plugin.configManager.getStringList("tab-list.footer");

        String headerRaw = String.join("\n", headerLines);
        String footerRaw = String.join("\n", footerLines);

        headerRaw = MessageUtils.p(player, headerRaw).replace("%player%", player.getName());
        footerRaw = MessageUtils.p(player, footerRaw).replace("%player%", player.getName());

        Component header = LegacyComponentSerializer.legacyAmpersand().deserialize(headerRaw);
        Component footer = LegacyComponentSerializer.legacyAmpersand().deserialize(footerRaw);

        player.sendPlayerListHeaderAndFooter(header, footer);
    }

    public void updateAll() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            updateTab(player);
        }
    }

    private void scheduleRefresh() {
        int ticks = plugin.configManager.getInt("tab-list.refresh-ticks", 40);
        if (ticks <= 0) return;
        refreshTask = Bukkit.getScheduler().runTaskTimer(plugin, this::updateAll, ticks, ticks);
    }

    public void shutdown() {
        if (refreshTask != null) refreshTask.cancel();
    }
}
