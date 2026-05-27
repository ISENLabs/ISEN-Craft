package fr.isen.bungee.utils;

import fr.isen.bungee.command.BungeeSender;
import fr.isen.common.command.IsenSender;
import fr.isen.common.utils.MessageConstants;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.List;

public class MessageUtils {

    public static TextComponent color(String text) {
        if (text == null) return new TextComponent("");
        return new TextComponent(TextComponent.fromLegacyText(text.replace('&', '§')));
    }

    public static void sendMessage(CommandSender sender, String text) {
        if (text == null || text.isBlank()) return;
        sender.sendMessage(color(text));
    }

    public static void sendMessage(CommandSender sender, List<String> text) {
        text.forEach(t -> sendMessage(sender, t));
    }

    public static void sendMessage(BungeeSender sender, String text) {
        sendMessage(sender.getHandle(), text);
    }

    public static void sendMessage(BungeeSender sender, List<String> text) {
        sendMessage(sender.getHandle(), text);
    }

    public static void broadcast(String text) {
        ProxyServer.getInstance().broadcast(color(text));
    }

    public static void clearChat() {
        for (int i = 0; i < MessageConstants.CLEAR_CHAT_LINES; i++) {
            broadcast("");
        }
    }
}