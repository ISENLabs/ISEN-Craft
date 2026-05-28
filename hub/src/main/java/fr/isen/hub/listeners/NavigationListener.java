package fr.isen.hub.listeners;

import fr.isen.hub.HubPlugin;
import fr.isen.hub.managers.NavigationManager;
import fr.isen.paper.utils.MessageUtils;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class NavigationListener implements Listener {

    private final HubPlugin plugin;
    private final NavigationManager manager;

    public NavigationListener(HubPlugin plugin, NavigationManager manager) {
        this.plugin = plugin;
        this.manager = manager;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        manager.giveCompass(player);
        String welcome = plugin.configManager.getString("messages.welcome", "&aBonjour, %player%! Bienvenue sur ISEN-Craft.")
                .replace("%player%", player.getName());
        MessageUtils.sendMessage(player, welcome);
        plugin.titleManager.showWelcomeTitle(player);
    }
    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        manager.giveCompass(event.getPlayer());
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        if (item == null || item.getType() == Material.AIR) return;
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        if (item.hasItemMeta()) {
            PersistentDataContainer pdc = item.getItemMeta().getPersistentDataContainer();
            NamespacedKey key = new NamespacedKey(plugin, NavigationManager.KEY_ITEM);

            if (pdc.has(key, PersistentDataType.STRING)) {
                String value = pdc.get(key, PersistentDataType.STRING);

                if ("compass".equals(value)) {
                    event.setCancelled(true);
                    manager.openMenu(player);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        manager.removeCooldown(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;

        ItemStack item = event.getCurrentItem();
        if (item == null || !item.hasItemMeta()) return;

        PersistentDataContainer pdc = item.getItemMeta().getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(plugin, NavigationManager.KEY_ITEM);

        if (pdc.has(key, PersistentDataType.STRING)) {
            event.setCancelled(true);

            String serverName = pdc.get(key, PersistentDataType.STRING);
            manager.connect(player, serverName);
        } else {
            if (LegacyComponentSerializer.legacySection().serialize(event.getView().title()).contains("ISEN - Menu")) {
                event.setCancelled(true);
            }
        }
    }
}
