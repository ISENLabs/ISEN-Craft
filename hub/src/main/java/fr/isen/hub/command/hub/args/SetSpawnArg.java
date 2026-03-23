package fr.isen.hub.command.hub.args;

import fr.isen.common.command.IsenCommandArgument;
import fr.isen.hub.command.hub.HubCommand;
import fr.isen.paper.command.PaperSender;
import fr.isen.paper.utils.MessageUtils;
import org.bukkit.Location;
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
    public boolean isPlayerOnly() {
        return true;
    }

    @Override
    public String getSyntax() {
        return "/ihub setspawn";
    }

    @Override
    public String getDescription() {
        return "Redéfinir le spawn du hub";
    }

    @Override
    public String getPermission() {
        return "hub.admin";
    }

    @Override
    public void execute(PaperSender sender, String[] args) {
        Player player = (Player) sender.getHandle();

        Location spawn = player.getLocation();
        parent.manager.setSpawn(spawn);

        MessageUtils.sendMessage(sender, "&eSpawn redéfini.");
    }
}
