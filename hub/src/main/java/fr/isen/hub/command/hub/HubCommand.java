package fr.isen.hub.command.hub;

import fr.isen.common.command.IsenCommand;
import fr.isen.hub.command.hub.args.SetSpawnArg;
import fr.isen.hub.managers.HubManager;
import fr.isen.paper.command.PaperSender;

public class HubCommand extends IsenCommand<PaperSender> {

    public HubManager manager;

    public HubCommand(HubManager manager) {
        super();

        this.manager = manager;

        registerArgument(new SetSpawnArg(this));
    }

    @Override
    protected void run(PaperSender sender, String[] args) {
        sendHelpMessage(sender);
    }
}
