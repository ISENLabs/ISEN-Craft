package fr.isen.hub.managers;

import fr.isen.common.config.IManager;
import fr.isen.hub.HubPlugin;
import io.papermc.paper.scoreboard.numbers.NumberFormat;
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
    private static final int PING_WARN_MS = 80;
    private static final int PING_BAD_MS = 150;
    private static final int MAX_SERVERS_DISPLAY = 3;
    private static final long NETWORK_REQUEST_DELAY_TICKS = 20L;

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
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                if (event.getPlayer().isOnline()) networkManager.sendRequest(event.getPlayer());
            }, NETWORK_REQUEST_DELAY_TICKS);
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
        obj.numberFormat(NumberFormat.blank());
        setLines(board, player, lastStats);
        player.setScoreboard(board);
        playerBoards.put(player.getUniqueId(), board);
    }

    private void setLines(Scoreboard board, Player player, Map<String, Integer> stats) {
        Objective obj = board.getObjective(OBJECTIVE_NAME);
        if (obj == null) return;

        String rang = player.isOp() ? "&cAdmin" : "&fISEN";
        String serverName = plugin.configManager.getString("server-name", "Hub");
        String pingColor = pingColor(player.getPing());

        obj.getScore(ChatColor.translateAlternateColorCodes('&', "&eJoueur: &f" + player.getName())).setScore(10);
        obj.getScore(ChatColor.translateAlternateColorCodes('&', "&eRang: " + rang)).setScore(9);
        obj.getScore(ChatColor.translateAlternateColorCodes('&', "&ePing: " + pingColor + player.getPing() + "ms")).setScore(8);
        obj.getScore(ChatColor.translateAlternateColorCodes('&', "&eServeur: &f" + serverName)).setScore(7);
        obj.getScore(" ").setScore(6);
        obj.getScore(ChatColor.translateAlternateColorCodes('&', "&8---------------")).setScore(5);
        obj.getScore("  ").setScore(4);

        int total = stats.values().stream().mapToInt(Integer::intValue).sum();
        String totalStr = stats.isEmpty() ? "--" : String.valueOf(total);
        obj.getScore(ChatColor.translateAlternateColorCodes('&', "&eNetwork: &f" + totalStr)).setScore(3);

        int score = MAX_SERVERS_DISPLAY - 1;
        for (Map.Entry<String, Integer> entry : stats.entrySet()) {
            if (score < 0) {
                plugin.logger.log("Scoreboard limité à " + MAX_SERVERS_DISPLAY + " serveurs — " + stats.size() + " serveurs reçus, les suivants ne sont pas affichés", "WARN");
                break;
            }
            obj.getScore(ChatColor.translateAlternateColorCodes('&', "&f" + entry.getKey() + ": " + entry.getValue())).setScore(score--);
        }
    }

    private String pingColor(int ping) {
        if (ping < PING_WARN_MS) return "&a";
        if (ping < PING_BAD_MS) return "&e";
        return "&c";
    }

    public void updateStats(Map<String, Integer> stats) {
        this.lastStats = stats;
        for (Player player : Bukkit.getOnlinePlayers()) {
            Scoreboard board = playerBoards.get(player.getUniqueId());
            if (board == null) {
                createBoard(player);
            } else {
                refreshBoard(board, player, stats);
            }
        }
    }

    private void refreshBoard(Scoreboard board, Player player, Map<String, Integer> stats) {
        Objective obj = board.getObjective(OBJECTIVE_NAME);
        if (obj == null) return;
        for (String entry : board.getEntries()) {
            board.resetScores(entry);
        }
        setLines(board, player, stats);
    }

    public void refreshPlayer(Player player) {
        Scoreboard board = playerBoards.get(player.getUniqueId());
        if (board == null) return;
        refreshBoard(board, player, lastStats);
    }
}
