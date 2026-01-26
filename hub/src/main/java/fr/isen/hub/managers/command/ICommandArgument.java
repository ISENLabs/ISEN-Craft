package fr.isen.hub.managers.command;

import org.bukkit.command.CommandSender;

import java.util.List;

public interface ICommandArgument {
    void execute(CommandSender sender, String[] args);

    default List<String> onTabComplete(CommandSender sender, String[] args) {
        return List.of();
    }

    String getName();

    default String getPermission() {
        return null;
    }
}
