package fr.isen.paper.command;

import fr.isen.common.command.IsenSender;
import fr.isen.paper.utils.MessageUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PaperSender implements IsenSender<CommandSender> {

    private final CommandSender handle;

    public PaperSender(CommandSender handle) {
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
        return handle instanceof Player;
    }

    @Override
    public CommandSender getHandle() {
        return handle;
    }
}
