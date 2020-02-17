package com.mattos.exercise.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "rewards")
public class RewardSchemaConfig {

    private Map<String, Double> schemes;

    public Double getRewardPercentage(String scheme){
        return this.schemes.get(scheme);
    }
}
