package fr.isen.hub.command.args;

import fr.isen.hub.command.HubCommand;
import fr.isen.hub.managers.command.ICommandArgument;
import fr.isen.hub.utils.MessageUtils;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetSpawnArg implements ICommandArgument {

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
    public void execute(CommandSender sender, String[] args) {
        if(!(sender instanceof Player)) {
            MessageUtils.sendMessage(sender ,"&cTu ne peux pas faire ça.");
            return;
        }

        Player player = ((Player) sender).getPlayer();

        Location spawn = player.getLocation();
        parent.manager.setSpawn(spawn);

        MessageUtils.sendMessage(sender, "&eSpawn redéfini.");
    }
}
