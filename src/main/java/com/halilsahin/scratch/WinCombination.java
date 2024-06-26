package com.halilsahin.scratch;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class WinCombination {
    @JsonProperty("reward_multiplier")
    private double rewardMultiplier;

    @JsonProperty("when")
    private String whenCondition;

    @JsonProperty("count")
    private int count;

    @JsonProperty("group")
    private String group;

    @JsonProperty("covered_areas")
    private String[][] coveredAreas;
}
