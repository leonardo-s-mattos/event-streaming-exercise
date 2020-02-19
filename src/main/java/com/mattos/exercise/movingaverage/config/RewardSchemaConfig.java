package com.mattos.exercise.movingaverage.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "rewards")
public class RewardSchemaConfig {

    private Map<String, Double> schemes;

    public Double getRewardPercentage(String scheme){
        return this.schemes.get(scheme);
    }

    public void setSchemes(Map<String, Double> schemes) {
        this.schemes = schemes;
    }
}
