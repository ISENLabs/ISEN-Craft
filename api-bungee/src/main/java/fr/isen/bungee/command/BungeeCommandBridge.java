package fr.isen.bungee.command;

import fr.isen.common.command.IsenCommand;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

public class BungeeCommandBridge extends Command implements TabExecutor {

    private final IsenCommand<BungeeSender> isenCommand;

    public BungeeCommandBridge(String name, IsenCommand<BungeeSender> isenCommand) {
        super(name);
        this.isenCommand = isenCommand;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        isenCommand.execute(new BungeeSender(sender), args);
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        return isenCommand.tabComplete(new BungeeSender(sender), args);
    }
}