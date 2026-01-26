package fr.isen.hub.command;

import fr.isen.hub.managers.HubManager;
import fr.isen.hub.managers.command.ICommand;
import fr.isen.hub.utils.MessageUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpawnCommand extends ICommand {

    public HubManager manager;

    public SpawnCommand(HubManager manager) {
        super();
        this.manager = manager;
    }

    @Override
    public void run(CommandSender sender, String[] args) {
        if(!(sender instanceof Player)) {
            MessageUtils.sendMessage(sender ,"&cTu ne peux pas faire ça.");
            return;
        }

        Player player = ((Player) sender).getPlayer();
        manager.teleportToSpawn(player);
    }
}
