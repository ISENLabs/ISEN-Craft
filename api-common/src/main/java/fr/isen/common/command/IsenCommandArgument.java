package fr.isen.common.command;

import java.util.List;

public interface IsenCommandArgument<S extends IsenSender<?>> {
    void execute(S sender, String[] args);

    String getName();
    String getDescription();
    String getSyntax();
    default String getPermission() {
        return null;
    }

    boolean isPlayerOnly();

    default List<String> onTabComplete(S sender, String[] args) {
        return List.of();
    }
}
