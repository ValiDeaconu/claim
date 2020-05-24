package org.claimapp.common.dto;

public class MessageDTO<T> {
    private String command;
    private Long lobbyId;
    private T arg;

    public MessageDTO(String command, Long lobbyId, T arg) {
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

    public Long getLobbyId() {
        return lobbyId;
    }

    public void setLobbyId(Long lobbyId) {
        this.lobbyId = lobbyId;
    }

    public T getArg() {
        return arg;
    }

    public void setArg(T arg) {
        this.arg = arg;
    }
}
