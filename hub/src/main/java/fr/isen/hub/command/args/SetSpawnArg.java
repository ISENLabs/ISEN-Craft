package fr.isen.hub.command.args;

import fr.isen.common.command.IsenCommandArgument;
import fr.isen.common.command.IsenSender;
import fr.isen.hub.command.HubCommand;
import fr.isen.paper.command.PaperSender;
import fr.isen.paper.utils.MessageUtils;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetSpawnArg implements IsenCommandArgument<PaperSender> {

    private final HubCommand parent;

    public SetSpawnArg(HubCommand parent) {
        this.parent = parent;
    }

    @Override
    public String getName() {
        return "setspawn";
    }

    @Override
    public String getPermission() {
        return "hub.admin";
    }

    @Override
    public void execute(PaperSender sender, String[] args) {
        if(!sender.isPlayer()) {
            MessageUtils.sendMessage(sender ,"&cTu ne peux pas faire ça.");
            return;
        }

        Player player = (Player) sender.getHandle();

        Location spawn = player.getLocation();
        parent.manager.setSpawn(spawn);

        MessageUtils.sendMessage(sender, "&eSpawn redéfini.");
    }
}
