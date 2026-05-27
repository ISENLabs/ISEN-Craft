package fr.isen.hub.command.profil;

import fr.isen.common.command.IsenCommand;
import fr.isen.hub.HubPlugin;
import fr.isen.hub.model.PlayerProfile;
import fr.isen.paper.command.PaperSender;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class ProfilCommand extends IsenCommand<PaperSender> {

    private static final SimpleDateFormat DATE_FMT = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    private final HubPlugin plugin;

    public ProfilCommand(HubPlugin plugin) {
        super();
        this.plugin = plugin;
    }

    @Override
    protected void run(PaperSender sender, String[] args) {
        if (!sender.isPlayer()) {
            sender.sendMessage("&cCommande réservée aux joueurs.");
            return;
        }
        Player senderPlayer = (Player) sender.getHandle();

        UUID targetUuid;
        String targetName;

        if (args.length == 0) {
            targetUuid = senderPlayer.getUniqueId();
            targetName = senderPlayer.getName();
        } else {
            if (!senderPlayer.isOp()) {
                sender.sendMessage("&cVous n'avez pas la permission de voir le profil des autres joueurs.");
                return;
            }
            Player onlineTarget = Bukkit.getPlayer(args[0]);
            if (onlineTarget == null) {
                sender.sendMessage("&cJoueur introuvable ou hors ligne.");
                return;
            }
            targetUuid = onlineTarget.getUniqueId();
            targetName = onlineTarget.getName();
        }

        if (plugin.playerProfileManager == null) {
            sender.sendMessage("&cLe système de profils est indisponible.");
            return;
        }

        PlayerProfile profile = plugin.playerProfileManager.getProfile(targetUuid);
        if (profile == null) {
            sender.sendMessage("&cAucun profil trouvé pour &f" + targetName + "&c.");
            return;
        }

        String firstJoin = DATE_FMT.format(new Date(profile.getFirstJoinEpoch()));
        String lastSeen = DATE_FMT.format(new Date(profile.getLastSeenEpoch()));

        sender.sendMessage("&8&m--------------------");
        sender.sendMessage("&6Profil de &f" + profile.getName());
        sender.sendMessage("&ePremière connexion &8: &f" + firstJoin);
        sender.sendMessage("&eDernière vue &8: &f" + lastSeen);
        sender.sendMessage("&eConnexions totales &8: &f" + profile.getTotalJoins());
        sender.sendMessage("&8&m--------------------");
    }
}
