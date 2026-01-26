package fr.isen.hub.command;

import fr.isen.common.command.IsenCommand;
import fr.isen.hub.command.args.SetSpawnArg;
import fr.isen.hub.managers.HubManager;
import fr.isen.paper.command.PaperSender;

public class HubCommand extends IsenCommand<PaperSender> {

    public HubManager manager;

    public HubCommand(HubManager manager) {
        super("hub.admin");

        this.manager = manager;

        registerArgument(new SetSpawnArg(this));
    }

    @Override
    public void run(PaperSender sender, String[] args) {
        manager.sendHelp(sender);
    }
}
