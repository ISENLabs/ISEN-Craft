package fr.isen.paper.command;

import org.bukkit.command.CommandSender;

import java.util.List;

public interface IsenCommandArgument {
    void execute(CommandSender sender, String[] args);

    default List<String> onTabComplete(CommandSender sender, String[] args) {
        return List.of();
    }

    String getName();

    default String getPermission() {
        return null;
    }
}
