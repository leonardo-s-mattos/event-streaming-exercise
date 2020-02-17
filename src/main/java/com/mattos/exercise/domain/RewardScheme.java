package com.mattos.exercise.domain;

public enum RewardScheme {

    BLUE (5D),
    GREEN (10D),
    YELLOW (15D);

    private final Double rewardPercentage;

    private RewardScheme(final Double rewardPercentage) {
        this.rewardPercentage = rewardPercentage;
    }

    public Double getRewardPercentage() { return rewardPercentage; }
}
