package com.mattos.exercise.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.mattos.exercise.domain.accessories.SalesOrderDeserializer;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Document(collection = "orders")
public class SalesOrder {

    @Id
    protected String id;
    @Indexed
    protected String category;
    protected Double price;
    @JsonProperty("rewards_scheme")
    protected RewardScheme rewardScheme;
    protected String name;

    private Map<String, SalesOrderBuilder> buildersMap = new HashMap<>();

    public SalesOrder(){

        buildersMap.put("sandwich", new SandwichSalesOrder.Builder());
        buildersMap.put("retail", new RetailSalesOrder.Builder());
        buildersMap.put("drink", new DrinkSalesOrder.Builder());

    }


    public String getId() {
        return id;
    }

    public String getCategory() {
        return category;
    }

    public Double getPrice() {
        return price;
    }

    public RewardScheme getRewardScheme() {
        return rewardScheme;
    }

    public String getName() {
        return name;
    }

    public Double getPoints() { return getPrice() * (getRewardScheme().getRewardPercentage()) / 100.0D}

    public static <T extends SalesOrder>  T fromJson(String json) throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(SalesOrder.class, new SalesOrderDeserializer());
        mapper.registerModule(module);

        return (T)mapper.readValue(json, SalesOrder.class);
    }

    public static String toJson(SalesOrder salesOrder) {

        String generatedJson = "";
        try {
            generatedJson = new ObjectMapper().writeValueAsString(salesOrder);
        } catch (JsonProcessingException e){
            generatedJson = "badly generated";
        } finally {
            return generatedJson;
        }

    }

    public static SalesOrderBuilder getBuilderByCategory(String category){

        return new SalesOrder().buildersMap.get(category);

    }

}
