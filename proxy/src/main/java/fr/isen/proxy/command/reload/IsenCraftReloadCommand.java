package fr.isen.proxy.command.reload;

import fr.isen.bungee.command.BungeeSender;
import fr.isen.bungee.utils.MessageUtils;
import fr.isen.common.command.IsenCommand;
import fr.isen.proxy.manager.ConfigManager;

import java.io.IOException;

public class IsenCraftReloadCommand extends IsenCommand<BungeeSender> {

    private final ConfigManager configManager;

    public IsenCraftReloadCommand(ConfigManager configManager) {
        super("proxy.admin");
        this.configManager = configManager;
    }

    @Override
    protected void run(BungeeSender sender, String[] args) {
        try {
            configManager.reload();
            MessageUtils.sendMessage(sender, "&aConfiguration rechargée.");
        } catch (IOException e) {
            MessageUtils.sendMessage(sender, "&cErreur lors du rechargement de la configuration.");
            java.util.logging.Logger.getLogger(getClass().getName()).severe("Failed to reload configuration: " + e.getMessage());
        }
    }
}
