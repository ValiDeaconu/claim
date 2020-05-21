package org.claimapp.server.dto;

import java.util.UUID;

public class MessageDTO<T> {
    private String command;
    private UUID lobbyId;
    private T arg;

    public MessageDTO(String command, UUID lobbyId, T arg) {
        this.command = command;
        this.lobbyId = lobbyId;
        this.arg = arg;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public UUID getLobbyId() {
        return lobbyId;
    }

    public void setLobbyId(UUID lobbyId) {
        this.lobbyId = lobbyId;
    }

    public T getArg() {
        return arg;
    }

    public void setArg(T arg) {
        this.arg = arg;
    }
}
