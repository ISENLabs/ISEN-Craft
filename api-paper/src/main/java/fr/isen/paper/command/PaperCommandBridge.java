package fr.isen.paper.command;

import fr.isen.common.command.IsenCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PaperCommandBridge implements CommandExecutor, TabCompleter {

    private final IsenCommand isenCommand;

    public PaperCommandBridge(IsenCommand isenCommand) {
        this.isenCommand = isenCommand;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        isenCommand.execute(new PaperSender(sender), args);
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return isenCommand.tabComplete(new PaperSender(sender), args);
    }
}
