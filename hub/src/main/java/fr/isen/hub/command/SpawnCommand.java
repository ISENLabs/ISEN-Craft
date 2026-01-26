package fr.isen.hub.command;

import fr.isen.common.command.IsenCommand;
import fr.isen.hub.managers.HubManager;
import fr.isen.paper.command.PaperSender;
import fr.isen.paper.utils.MessageUtils;
import org.bukkit.entity.Player;

public class SpawnCommand extends IsenCommand<PaperSender> {

    public HubManager manager;

    public SpawnCommand(HubManager manager) {
        super();
        this.manager = manager;
    }

    @Override
    public void run(PaperSender sender, String[] args) {
        if(!sender.isPlayer()) {
            MessageUtils.sendMessage(sender ,"&cTu ne peux pas faire ça.");
            return;
        }

        Player player = (Player) sender.getHandle();
        manager.teleportToSpawn(player);
    }
}
