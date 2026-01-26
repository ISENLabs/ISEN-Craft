package fr.isen.hub.managers.command;

import fr.isen.hub.utils.MessageUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class ICommand implements CommandExecutor, TabCompleter {

    private final Map<String, ICommandArgument> subCommands = new HashMap<>();
    private final String permission;

    public ICommand() {
        this.permission = null;
    }

    public ICommand(String permission) {
        this.permission = permission;
    }

    protected void registerArgument(ICommandArgument subCommand) {
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
            ICommandArgument sc = subCommands.get(subName);

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
