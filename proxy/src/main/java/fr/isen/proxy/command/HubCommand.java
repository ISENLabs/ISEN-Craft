package fr.isen.proxy.command;

import fr.isen.bungee.command.BungeeSender;
import fr.isen.bungee.utils.MessageUtils;
import fr.isen.common.command.IsenCommand;
import fr.isen.proxy.manager.HubManager;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class HubCommand extends IsenCommand<BungeeSender> {

    private final HubManager manager;

    public HubCommand(HubManager manager) {
        super();

        this.manager = manager;
    }

    @Override
    public void run(BungeeSender sender, String[] args) {
        if(!sender.isPlayer()) {
            MessageUtils.sendMessage(sender, "&cTu ne peux pas faire ça.");
            return;
        }

        manager.sendToHub((ProxiedPlayer) sender.getHandle(), "hub");
    }
}
