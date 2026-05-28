package fr.isen.proxy.command.ban;

import fr.isen.bungee.command.BungeeSender;
import fr.isen.common.command.IsenCommand;
import fr.isen.common.command.IsenCommandArgument;

public class BanCommand extends IsenCommand<BungeeSender> {

    private final IsenCommandArgument<BungeeSender> action;

    public BanCommand(IsenCommandArgument<BungeeSender> action) {
        super("proxy.admin");
        this.action = action;
    }

    @Override
    protected void run(BungeeSender sender, String[] args) {
        action.execute(sender, args);
    }
}
