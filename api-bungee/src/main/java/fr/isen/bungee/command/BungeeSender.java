package fr.isen.bungee.command;

import fr.isen.bungee.utils.MessageUtils;
import fr.isen.common.command.IsenSender;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class BungeeSender implements IsenSender<CommandSender> {

    private final CommandSender handle;

    public BungeeSender(CommandSender handle) {
        this.handle = handle;
    }

    @Override
    public void sendMessage(String message) {
        MessageUtils.sendMessage(handle, message);
    }

    @Override
    public boolean hasPermission(String permission) {
        return handle.hasPermission(permission);
    }

    @Override
    public String getName() {
        return handle.getName();
    }

    @Override
    public boolean isPlayer() {
        return handle instanceof ProxiedPlayer;
    }

    @Override
    public CommandSender getHandle() {
        return handle;
    }
}
