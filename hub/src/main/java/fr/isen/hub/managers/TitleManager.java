package fr.isen.hub.managers;

import fr.isen.common.config.IManager;
import fr.isen.hub.HubPlugin;
import fr.isen.paper.utils.MessageUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.time.Duration;

public class TitleManager extends IManager<HubPlugin> {

    public TitleManager(HubPlugin plugin) {
        super(plugin, plugin.logger, "TitleManager");
    }

    public void showWelcomeTitle(Player player) {
        String titleRaw = plugin.configManager.getString("welcome-title.title", "Bienvenue sur ISEN-Craft");
        String subtitleRaw = plugin.configManager.getString("welcome-title.subtitle", "&eRavi de vous revoir, &f%player% !");
        int fadeIn = plugin.configManager.getInt("welcome-title.fade-in", 10);
        int stay = plugin.configManager.getInt("welcome-title.stay", 40);
        int fadeOut = plugin.configManager.getInt("welcome-title.fade-out", 10);

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            if (!player.isOnline()) {
                return;
            }

            String formattedTitle = MessageUtils.p(player, titleRaw).replace("%player%", player.getName());
            String formattedSubtitle = MessageUtils.p(player, subtitleRaw).replace("%player%", player.getName());

            Component titleComp = LegacyComponentSerializer.legacyAmpersand().deserialize(formattedTitle);
            Component subtitleComp = LegacyComponentSerializer.legacyAmpersand().deserialize(formattedSubtitle);

            Title.Times times = Title.Times.times(
                    Duration.ofMillis(fadeIn * 50L),
                    Duration.ofMillis(stay * 50L),
                    Duration.ofMillis(fadeOut * 50L)
            );

            Title titleObject = Title.title(titleComp, subtitleComp, times);
            player.showTitle(titleObject);
        }, 10L);
    }
}
