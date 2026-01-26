package fr.isen.paper.command;

import fr.isen.paper.utils.MessageUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

public abstract class IsenCommand implements CommandExecutor, TabCompleter {

    private final Map<String, IsenCommandArgument> subCommands = new HashMap<>();
    private final String permission;

    public IsenCommand() {
        this.permission = null;
    }

    public IsenCommand(String permission) {
        this.permission = permission;
    }

    protected void registerArgument(IsenCommandArgument subCommand) {
        subCommands.put(subCommand.getName().toLowerCase(), subCommand);
    }

    public abstract void run(CommandSender sender, String[] args);

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (permission != null && !sender.hasPermission(permission)) {
            MessageUtils.sendMessage(sender, "&cTu n'as pas la permission.");
            return true;
        }

        if (args.length == 0) {
            run(sender, args);
            return true;
        }

        String subName = args[0].toLowerCase();
        if (subCommands.containsKey(subName)) {
            IsenCommandArgument sc = subCommands.get(subName);

            if (sc.getPermission() != null && !sender.hasPermission(sc.getPermission())) {
                MessageUtils.sendMessage(sender, "&cTu n'as pas la permission.");
                return true;
            }

            String[] subArgs = Arrays.copyOfRange(args, 1, args.length);
            sc.execute(sender, subArgs);
            return true;
        }

        run(sender, args);
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
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

        return null;
    }
}