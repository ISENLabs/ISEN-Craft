package fr.isen.hub.managers;

import fr.isen.common.config.IManager;
import fr.isen.hub.HubPlugin;
import fr.isen.hub.listeners.NavigationListener;
import fr.isen.paper.utils.BungeeUtils;
import fr.isen.paper.utils.ItemBuilder;
import fr.isen.paper.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class NavigationManager extends IManager<HubPlugin> {

    public static final String KEY_ITEM = "isen_hub_item";
    private final BungeeUtils bungeeUtils;

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

        player.getInventory().setItem(4, item);
    }

    public void openMenu(Player player) {
        Inventory inv = Bukkit.createInventory(null, 27, "§7ISEN - Menu");

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

        inv.setItem(11, survivalItem);
        inv.setItem(15, pvpItem);

        ItemStack filler = new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).name(" ").build();
        for (int i = 0; i < inv.getSize(); i++) {
            if (inv.getItem(i) == null) {
                inv.setItem(i, filler);
            }
        }

        player.openInventory(inv);
    }

    public void connect(Player player, String serverName) {
        player.closeInventory();
        MessageUtils.sendMessage(player, "&2Redirection vers " + serverName + "...");
        bungeeUtils.connect(player, serverName);
    }
}
