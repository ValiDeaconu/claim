package org.claimapp.server.dto;

public class UserScoreClaimDTO {

    private UserDTO userDTO;
    private int score;
    private boolean calledClaim;

    public UserScoreClaimDTO() {
    }

    public UserScoreClaimDTO(UserDTO userDTO, int score, boolean calledClaim) {
        this.userDTO = userDTO;
        this.score = score;
        this.calledClaim = calledClaim;
    }

    public UserDTO getUserDTO() {
        return userDTO;
    }

    public void setUserDTO(UserDTO userDTO) {
        this.userDTO = userDTO;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public boolean isCalledClaim() {
        return calledClaim;
    }

    public void setCalledClaim(boolean calledClaim) {
        this.calledClaim = calledClaim;
    }
}
