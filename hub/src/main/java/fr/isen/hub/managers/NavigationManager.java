package fr.isen.hub.managers;

import fr.isen.common.config.IManager;
import fr.isen.hub.HubPlugin;
import fr.isen.hub.listeners.NavigationListener;
import fr.isen.paper.utils.BungeeUtils;
import fr.isen.paper.utils.ItemBuilder;
import fr.isen.paper.utils.MessageUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class NavigationManager extends IManager<HubPlugin> {

    public static final String KEY_ITEM = "isen_hub_item";
    private static final int COMPASS_SLOT = 4;
    private final BungeeUtils bungeeUtils;
    private final Map<UUID, Long> cooldowns = new HashMap<>();

    public NavigationManager(HubPlugin plugin, BungeeUtils bungeeUtils) {
        super(plugin, plugin.logger, "NavigationManager");

        this.bungeeUtils = bungeeUtils;

        plugin.registerListener(new NavigationListener(plugin, this));
    }

    public void giveCompass(Player player) {
        player.getInventory().clear();

        ItemStack item = new ItemBuilder(Material.NETHER_STAR)
            .name("&9&lNavigation &8&l▪ &7&lClic-Droit")
            .lore("&7Cliquez pour &aouvrir &7le sélecteur de serveur !")
            .storeString(plugin, KEY_ITEM, "compass")
            .build();

        player.getInventory().setItem(COMPASS_SLOT, item);
    }

    public void openMenu(Player player) {
        String titleRaw = plugin.configManager.getString("navigation.menu-title", "&7ISEN - Menu");
        int size = plugin.configManager.getInt("navigation.menu-size", 27);

        Inventory inv = Bukkit.createInventory(null, size,
                LegacyComponentSerializer.legacyAmpersand().deserialize(titleRaw));

        List<Map<?, ?>> servers = plugin.configManager.getMapList("navigation.servers");
        for (Map<?, ?> entry : servers) {
            String key = String.valueOf(entry.get("key"));
            Object slotObj = entry.get("slot");
            int slot = slotObj instanceof Number ? ((Number) slotObj).intValue() : 0;
            String materialName = entry.containsKey("material") ? String.valueOf(entry.get("material")) : "PAPER";
            String displayName = entry.containsKey("display-name") ? String.valueOf(entry.get("display-name")) : key;

            @SuppressWarnings("unchecked")
            List<String> loreRaw = entry.containsKey("lore") ? (List<String>) entry.get("lore") : List.of();

            Material material = Material.matchMaterial(materialName);
            if (material == null) material = Material.PAPER;

            List<String> lore = new ArrayList<>();
            for (String line : loreRaw) {
                lore.add(MessageUtils.p(player, line));
            }

            ItemStack item = new ItemBuilder(material)
                    .name(displayName)
                    .lore(lore)
                    .storeString(plugin, KEY_ITEM, key)
                    .enchant(Enchantment.UNBREAKING, 1)
                    .hideAllAttributes()
                    .build();

            if (slot >= 0 && slot < size) inv.setItem(slot, item);
        }

        String fillerMatName = plugin.configManager.getString("navigation.filler-material", "GRAY_STAINED_GLASS_PANE");
        Material fillerMat = Material.matchMaterial(fillerMatName);
        if (fillerMat == null) fillerMat = Material.GRAY_STAINED_GLASS_PANE;
        ItemStack filler = new ItemBuilder(fillerMat).name(" ").build();
        for (int i = 0; i < inv.getSize(); i++) {
            if (inv.getItem(i) == null) inv.setItem(i, filler);
        }

        player.openInventory(inv);
    }

    public void connect(Player player, String serverName) {
        if ("compass".equals(serverName)) return;

        int cooldownSeconds = plugin.configManager.getInt("settings.navigation-cooldown-seconds", 3);
        long cooldownMs = cooldownSeconds * 1000L;
        long now = System.currentTimeMillis();

        Long lastConnect = cooldowns.get(player.getUniqueId());
        if (lastConnect != null && now - lastConnect < cooldownMs) {
            long remaining = (cooldownMs - (now - lastConnect) + 999) / 1000;
            Component msg = LegacyComponentSerializer.legacyAmpersand()
                    .deserialize("&cVeuillez attendre &e" + remaining + "s &cavant de changer de serveur.");
            player.sendActionBar(msg);
            return;
        }

        cooldowns.put(player.getUniqueId(), now);
        player.closeInventory();

        Component transferMsg = LegacyComponentSerializer.legacyAmpersand()
                .deserialize("&aTransfert vers &f" + serverName + "&a en cours...");
        player.sendActionBar(transferMsg);

        bungeeUtils.connect(player, serverName);
    }

    public void removeCooldown(UUID uuid) {
        cooldowns.remove(uuid);
    }
}
