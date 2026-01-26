package fr.isen.bungee.logger;

import fr.isen.common.logger.IsenLogger;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Plugin;

public class BungeeLogger extends IsenLogger {

    public BungeeLogger(Plugin plugin) {
        super(plugin.getDescription().getName());
    }

    @Override
    protected void print(String message) {
        ProxyServer.getInstance().getConsole().sendMessage(new TextComponent(message));
    }
}
