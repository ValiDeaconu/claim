package org.claimapp.common.dto;

public class UserDTO {
    private Long id;
    private String username;
    private String wins;
    private String loss;
    private int profileAssetIndex;

    public UserDTO() {
    }

    public UserDTO(Long id, String username) {
        this.id = id;
        this.username = username;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getWins() {
        return wins;
    }

    public void setWins(String wins) {
        this.wins = wins;
    }

    public String getLoss() {
        return loss;
    }

    public void setLoss(String loss) {
        this.loss = loss;
    }

    public int getProfileAssetIndex() {
        return profileAssetIndex;
    }

    public void setProfileAssetIndex(int profileAssetIndex) {
        this.profileAssetIndex = profileAssetIndex;
    }
}
