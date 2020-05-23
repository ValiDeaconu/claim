package org.claimapp.server.entity;

public class User {

    private Long id;
    private String username;
    private int profileAssetIndex;

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

    public int getProfileAssetIndex() {
        return profileAssetIndex;
    }

    public void setProfileAssetIndex(int profileAssetIndex) {
        this.profileAssetIndex = profileAssetIndex;
    }
}
