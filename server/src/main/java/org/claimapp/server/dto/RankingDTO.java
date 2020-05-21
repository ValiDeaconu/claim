package org.claimapp.server.dto;

import java.util.List;

public class RankingDTO {

    private List<UserScoreClaimDTO> winners;
    private List<UserScoreClaimDTO> losers;

    public RankingDTO() {
    }

    public RankingDTO(List<UserScoreClaimDTO> winners, List<UserScoreClaimDTO> losers) {
        this.winners = winners;
        this.losers = losers;
    }

    public List<UserScoreClaimDTO> getWinners() {
        return winners;
    }

    public void setWinners(List<UserScoreClaimDTO> winners) {
        this.winners = winners;
    }

    public List<UserScoreClaimDTO> getLosers() {
        return losers;
    }

    public void setLosers(List<UserScoreClaimDTO> losers) {
        this.losers = losers;
    }
}
