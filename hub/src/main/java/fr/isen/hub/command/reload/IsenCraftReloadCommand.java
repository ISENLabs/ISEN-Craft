package fr.isen.hub.command.reload;

import fr.isen.common.command.IsenCommand;
import fr.isen.hub.managers.ConfigManager;
import fr.isen.paper.command.PaperSender;
import fr.isen.paper.utils.MessageUtils;

import java.io.IOException;

public class IsenCraftReloadCommand extends IsenCommand<PaperSender> {

    private final ConfigManager configManager;

    public IsenCraftReloadCommand(ConfigManager configManager) {
        super("hub.admin");
        this.configManager = configManager;
    }

    @Override
    protected void run(PaperSender sender, String[] args) {
        try {
            configManager.reload();
            MessageUtils.sendMessage(sender, "&aConfiguration rechargée.");
        } catch (IOException e) {
            MessageUtils.sendMessage(sender, "&cErreur lors du rechargement de la configuration.");
            java.util.logging.Logger.getLogger(getClass().getName()).severe("Failed to reload configuration: " + e.getMessage());
        }
    }
}
