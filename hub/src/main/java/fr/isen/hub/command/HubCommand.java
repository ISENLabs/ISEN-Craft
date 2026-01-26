package fr.isen.hub.command;

import fr.isen.hub.command.args.SetSpawnArg;
import fr.isen.hub.managers.HubManager;
import fr.isen.paper.command.IsenCommand;
import org.bukkit.command.CommandSender;

public class HubCommand extends IsenCommand {

    public HubManager manager;

    public HubCommand(HubManager manager) {
        super("hub.admin");

        this.manager = manager;

        registerArgument(new SetSpawnArg(this));
    }

    @Override
    public void run(CommandSender sender, String[] args) {
        manager.sendHelp(sender);
    }
}
