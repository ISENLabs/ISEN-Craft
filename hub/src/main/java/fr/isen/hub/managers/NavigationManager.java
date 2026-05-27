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
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class NavigationManager extends IManager<HubPlugin> {

    public static final String KEY_ITEM = "isen_hub_item";
    private static final int COMPASS_SLOT = 4;
    private static final int SURVIE_MENU_SLOT = 11;
    private static final int CREATIF_MENU_SLOT = 15;
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
        Inventory inv = Bukkit.createInventory(null, 27, LegacyComponentSerializer.legacySection().deserialize("§7ISEN - Menu"));

        String survieCount = MessageUtils.p(player, "%bungee_survie%");
        ItemStack survivalItem = new ItemBuilder(Material.DIAMOND_HOE)
                .name("&a&lSurvie")
                .lore(
                    "&7Mode de jeu immersif",
                    " ",
                    "&8┃ &fExplorez un monde vaste, récoltez",
                    "&8┃ &fdes ressources et bâtissez votre ville.",
                    " ",
                    "&8┃ &fVersion &8: &b&l1.21",
                    "&8┃ &fConnectés &8: &a" + survieCount,
                    " ",
                    "&2▶ &aCliquez pour rejoindre."
                )
                .storeString(plugin, KEY_ITEM, "survie")
                .enchant(Enchantment.UNBREAKING, 1)
                .hideAllAttributes()
                .build();

        String creatifCount = MessageUtils.p(player, "%bungee_creatif%");
        ItemStack pvpItem = new ItemBuilder(Material.GRASS_BLOCK)
                .name("&9&lCréatif")
                .lore(
                "&7Mode de jeu artistique",
                        " ",
                        "&8┃ &fLaissez libre cours à votre imagination",
                        "&8┃ &fsur des parcelles géantes et protégées.",
                        " ",
                        "&8┃ &fVersion &8: &b&l1.21",
                        "&8┃ &fConnectés &8: &a" + creatifCount,
                        " ",
                        "&2▶ &cMAINTENANCE"
                )
                .storeString(plugin, KEY_ITEM, "creatif")
                .enchant(Enchantment.UNBREAKING, 1)
                .hideAllAttributes()
                .build();

        inv.setItem(SURVIE_MENU_SLOT, survivalItem);
        inv.setItem(CREATIF_MENU_SLOT, pvpItem);

        ItemStack filler = new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).name(" ").build();
        for (int i = 0; i < inv.getSize(); i++) {
            if (inv.getItem(i) == null) {
                inv.setItem(i, filler);
            }
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
