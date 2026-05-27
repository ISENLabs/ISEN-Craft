package fr.isen.proxy.command.mute;

import fr.isen.bungee.command.BungeeSender;
import fr.isen.common.command.IsenCommand;
import fr.isen.common.command.IsenCommandArgument;

public class MuteCommand extends IsenCommand<BungeeSender> {

    private final IsenCommandArgument<BungeeSender> action;

    public MuteCommand(IsenCommandArgument<BungeeSender> action) {
        super("proxy.admin");
        this.action = action;
    }

    @Override
    protected void run(BungeeSender sender, String[] args) {
        action.execute(sender, args);
    }
}
