package fr.isen.paper.utils;

import java.util.List;
import java.util.stream.Collectors;

import fr.isen.paper.command.PaperSender;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MessageUtils {
    public static String color(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }
    public static List<String> color(List<String> text) {
        return text.stream().map(MessageUtils::color).collect(Collectors.toList());
    }

    public static String p(Player player, String text) {
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") == null) return text;
        return PlaceholderAPI.setPlaceholders(player, text);
    }

    public static void sendMessage(Player player, String text) {
        if (text != null && !text.isBlank()) player.sendMessage(MessageUtils.color(text));
    }
    public static void sendMessage(Player player, List<String> text) {
        text.forEach(t -> sendMessage(player, t));
    }

    public static void sendMessage(CommandSender sender, String text) {
        if (text != null && !text.isBlank()) sender.sendMessage(color(text));
    }
    public static void sendMessage(CommandSender sender, List<String> text) {
        text.forEach(t -> sendMessage(sender, t));
    }

    public static void sendMessage(PaperSender sender, String text) {
        if (text != null && !text.isBlank()) sender.sendMessage(text);
    }
    public static void sendMessage(PaperSender sender, List<String> text) {
        text.forEach(t -> sendMessage(sender, t));
    }

    public static void broadcast(String text) {
        Bukkit.broadcastMessage(color(text));
    }
    public static void broadcast(List<String> text) {
        text.forEach(MessageUtils::broadcast);
    }

    private static final int CLEAR_CHAT_LINES = 150;

    public static void clearChat() {
        for (int i = 0; i < CLEAR_CHAT_LINES; i++) {
            broadcast("");
        }
    }
}