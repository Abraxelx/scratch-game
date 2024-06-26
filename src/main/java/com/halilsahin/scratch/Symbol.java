package com.halilsahin.scratch;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Symbol {
    @JsonProperty("reward_multiplier")
    private double rewardMultiplier;

    @JsonProperty("type")
    private String type;

    @JsonProperty("impact")
    private String impact;

    @JsonProperty("extra")
    private int extra;
}
