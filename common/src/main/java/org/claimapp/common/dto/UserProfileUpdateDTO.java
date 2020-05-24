package org.claimapp.common.dto;

public class UserProfileUpdateDTO {
    private Long id;
    private int profileAssetIndex;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getProfileAssetIndex() {
        return profileAssetIndex;
    }

    public void setProfileAssetIndex(int profileAssetIndex) {
        this.profileAssetIndex = profileAssetIndex;
    }
}
