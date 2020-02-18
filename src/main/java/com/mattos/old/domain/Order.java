package com.mattos.old.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.io.IOException;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {

    private String id;
    private String category;
    private Double price;
    @JsonProperty("rewards_scheme")
    private String rewardScheme;
    private String name;
    private String size;
    private String[] addons;


    public String getId() {
        return id;
    }

    public String getCategory() {
        return category;
    }

    public Double getPrice() {
        return price;
    }

    public String getRewardScheme() {
        return rewardScheme;
    }

    public String getName() {
        return name;
    }

    public String getSize() {
        return size;
    }

    public String[] getAddons() {
        return addons;
    }

    public static Order fromJson(String json) throws IOException {
        return new ObjectMapper().readValue(json, Order.class);
    }

    public static String toJson(Order order) {

        String generatedJson = "";
        try {
            generatedJson = new ObjectMapper().writeValueAsString(order);
        } catch (JsonProcessingException e){
            generatedJson = "badly generated";
        } finally {
            return generatedJson;
        }

    }

    public static class Builder {
        private String id;
        private String category;
        private Double price;
        private String rewardScheme;
        private String name;
        private String size;
        private String[] addons;

        public Builder withId(final String id) {
            this.id = id;
            return this;
        }

        public Builder withCategory(final String category) {
            this.category = category;
            return this;
        }

        public Builder withSale(final String name, final Double price, final String rewardScheme, final String size, final String[] addons) {
            this.price = price;
            this.rewardScheme= rewardScheme;
            this.name = name;
            this.size = size;
            this.addons = addons;
            return this;
        }

        public Order build() {

            Order result = new Order();
            result.id = id;
            result.category = category;
            result.price = price;
            result.rewardScheme= rewardScheme;
            result.name = name;
            result.size = size;
            result.addons = addons;
            return result;
        }
    }

}
