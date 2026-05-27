package fr.isen.hub.managers;

import fr.isen.common.config.IManager;
import fr.isen.hub.HubPlugin;
import fr.isen.hub.listeners.ConnectionListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class LogManager extends IManager<HubPlugin> {

    private File logFile;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public LogManager(HubPlugin plugin) {
        super(plugin, plugin.logger, "LogManager");

        File dataFolder = plugin.getDataFolder();
        File logsDir = new File(dataFolder, "logs");
        if (!logsDir.exists()) {
            logsDir.mkdirs();
        }

        this.logFile = new File(logsDir, "connections.log");
        if (!logFile.exists()) {
            try {
                logFile.createNewFile();
            } catch (java.io.IOException e) {
                plugin.logger.log("Could not create connections.log file!", "ERROR");
                e.printStackTrace();
            }
        }

        plugin.registerListener(new ConnectionListener(plugin, this));
    }

    private synchronized void writeLog(String line) {
        try (FileWriter fw = new FileWriter(logFile, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            out.println(line);
        } catch (java.io.IOException e) {
            plugin.logger.log("Could not write to connections.log!", "ERROR");
            e.printStackTrace();
        }
    }

    public void logJoin(Player player) {
        String timestamp = LocalDateTime.now(ZoneId.systemDefault()).format(formatter);
        String logLine = String.format("[%s] JOIN | %s | %s", timestamp, player.getName(), player.getUniqueId().toString());
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> writeLog(logLine));
    }

    public void logQuit(Player player) {
        String timestamp = LocalDateTime.now(ZoneId.systemDefault()).format(formatter);
        String logLine = String.format("[%s] QUIT | %s | %s", timestamp, player.getName(), player.getUniqueId().toString());
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> writeLog(logLine));
    }
}
