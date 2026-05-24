package fr.isen.hub.managers;

import fr.isen.common.config.IManager;
import fr.isen.hub.HubPlugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scoreboard.Criteria;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ScoreboardManager extends IManager<HubPlugin> implements Listener {

    private static final String OBJECTIVE_NAME = "isen-network";

    private final Map<UUID, Scoreboard> playerBoards = new HashMap<>();
    private Map<String, Integer> lastStats = new HashMap<>();
    private NetworkManager networkManager;

    public ScoreboardManager(HubPlugin plugin) {
        super(plugin, plugin.logger, "ScoreboardManager");
        plugin.registerListener(this);
    }

    public void setNetworkManager(NetworkManager networkManager) {
        this.networkManager = networkManager;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        createBoard(event.getPlayer());
        if (networkManager != null) {
            networkManager.sendRequest(event.getPlayer());
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        playerBoards.remove(event.getPlayer().getUniqueId());
    }

    private void createBoard(Player player) {
        Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
        Component title = LegacyComponentSerializer.legacyAmpersand().deserialize("&6=== ISEN-Craft ===");
        Objective obj = board.registerNewObjective(OBJECTIVE_NAME, Criteria.DUMMY, title);
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);
        setLines(board, player, lastStats);
        player.setScoreboard(board);
        playerBoards.put(player.getUniqueId(), board);
    }

    private void setLines(Scoreboard board, Player player, Map<String, Integer> stats) {
        Objective obj = board.getObjective(OBJECTIVE_NAME);
        if (obj == null) return;

        String group;
        try {
            net.luckperms.api.LuckPerms lp = net.luckperms.api.LuckPermsProvider.get();
            group = lp.getPlayerAdapter(Player.class).getUser(player)
                    .getCachedData().getMetaData().getPrimaryGroup();
        } catch (Exception e) {
            group = "inconnu";
            plugin.logger.log("LuckPerms indisponible — rang affiché comme 'inconnu'", "WARN");
        }

        obj.getScore(ChatColor.translateAlternateColorCodes('&', "&eJoueur: &f" + player.getName())).setScore(8);
        obj.getScore(ChatColor.translateAlternateColorCodes('&', "&eRang: &f" + group)).setScore(7);
        obj.getScore(" ").setScore(6);
        obj.getScore(ChatColor.translateAlternateColorCodes('&', "&8---------------")).setScore(5);
        obj.getScore("  ").setScore(4);

        int total = stats.values().stream().mapToInt(Integer::intValue).sum();
        String totalStr = stats.isEmpty() ? "--" : String.valueOf(total);
        obj.getScore(ChatColor.translateAlternateColorCodes('&', "&eRéseau: &f" + totalStr)).setScore(3);

        int score = 2;
        for (Map.Entry<String, Integer> entry : stats.entrySet()) {
            if (score < 0) {
                plugin.logger.log("Scoreboard limité à 3 serveurs — " + stats.size() + " serveurs reçus, les suivants ne sont pas affichés", "WARN");
                break;
            }
            obj.getScore(ChatColor.translateAlternateColorCodes('&', "&f" + entry.getKey() + ": " + entry.getValue())).setScore(score--);
        }
    }

    public void updateStats(Map<String, Integer> stats) {
        this.lastStats = stats;
        for (Player player : Bukkit.getOnlinePlayers()) {
            createBoard(player);
        }
    }
}
