package fr.isen.common.command;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class IsenCommand<S extends IsenSender<?>> {
    private final Map<String, IsenCommandArgument<S>> subCommands = new HashMap<>();
    private final String permission;

    public IsenCommand() {
        this.permission = null;
    }

    public IsenCommand(String permission) {
        this.permission = permission;
    }

    protected void registerArgument(IsenCommandArgument<S> subCommand) {
        subCommands.put(subCommand.getName().toLowerCase(), subCommand);
    }

    protected abstract void run(S sender, String[] args);

    public void execute(S sender, String[] args) {
        if (permission != null && !sender.hasPermission(permission)) {
            sender.sendMessage("&cTu n'as pas la permission.");
            return;
        }

        if (args.length == 0) {
            run(sender, args);
            return;
        }

        String subName = args[0].toLowerCase();
        if(
                subName.equalsIgnoreCase("help")
            || subName.equalsIgnoreCase("h")
            || subName.equalsIgnoreCase("?")
        ) {
            sendHelpMessage(sender);
            return;
        }

        if (subCommands.containsKey(subName)) {
            IsenCommandArgument<S> sc = subCommands.get(subName);

            if(sc.isPlayerOnly() && !sender.isPlayer()) {
                sender.sendMessage("&cTu ne peux pas faire ça.");
                return;
            }

            if (sc.getPermission() != null && !sender.hasPermission(sc.getPermission())) {
                sender.sendMessage("&cTu n'as pas la permission.");
                return;
            }

            String[] subArgs = Arrays.copyOfRange(args, 1, args.length);
            sc.execute(sender, subArgs);
            return;
        }

        run(sender, args);
    }

    public List<String> tabComplete(S sender, String[] args) {
        if (args.length == 1) {
            return subCommands.keySet().stream()
                    .filter(s -> s.startsWith(args[0].toLowerCase()))
                    .collect(Collectors.toList());
        }

        if (args.length > 1) {
            String subName = args[0].toLowerCase();
            if (subCommands.containsKey(subName)) {
                return subCommands.get(subName).onTabComplete(sender, Arrays.copyOfRange(args, 1, args.length));
            }
        }
        return List.of();
    }

    protected void sendHelpMessage(S sender) {
        sender.sendMessage(" ");
        sender.sendMessage("&8&m----------------------------------------");
        sender.sendMessage("             &c&lISEN-Craft");
        sender.sendMessage(" ");

        boolean hasAnyCommand = false;

        for (IsenCommandArgument<S> subCommand : subCommands.values()) {
            if (subCommand.getPermission() == null || subCommand.getPermission().isEmpty() || sender.hasPermission(subCommand.getPermission())) {

                if (subCommand.isPlayerOnly() && !sender.isPlayer()) {
                    sender.sendMessage(" &8» &c" + subCommand.getSyntax() + " &8- &7(Joueur Uniquement)");
                } else {
                    sender.sendMessage(" &8» &e" + subCommand.getSyntax() + " &8- &7" + subCommand.getDescription());
                }
                hasAnyCommand = true;
            }
        }

        if (!hasAnyCommand) {
            sender.sendMessage(" &cVous n'avez accès à aucune commande.");
        }

        sender.sendMessage("&8&m----------------------------------------");
        sender.sendMessage(" ");
    }
}
