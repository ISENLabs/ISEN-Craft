package fr.isen.common.command;

public interface IsenSender<T> {
    void sendMessage(String message);
    boolean hasPermission(String permission);
    String getName();
    boolean isPlayer();

    T getHandle();
}
