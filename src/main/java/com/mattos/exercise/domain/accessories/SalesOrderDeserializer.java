package com.mattos.exercise.domain.accessories;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.mattos.exercise.domain.RewardScheme;
import com.mattos.exercise.domain.SalesOrder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SalesOrderDeserializer extends JsonDeserializer<SalesOrder> {


    public SalesOrder deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException, JsonProcessingException {

        ObjectCodec oc = jsonParser.getCodec();
        JsonNode node = oc.readTree(jsonParser);

        final String id = node.has("id")?node.get("id").asText():"";
        final String name = node.has("name")?node.get("name").asText():"";
        final String category = node.has("category")?node.get("category").asText():"";
        final Double price = node.has("price")?node.get("price").asDouble():0d;
        final RewardScheme rewardsScheme = node.has("rewards_scheme")?
                RewardScheme.valueOf(node.get("rewards_scheme").asText().toUpperCase()):null;
        final String size = node.has("size")?node.get("size").asText():null;


        List<String> addons = null;
        if(node.has("addons")){
            addons = new ArrayList<>();
            for (Iterator<JsonNode> it = node.get("addons").elements(); it.hasNext(); ) {
                JsonNode childNode = it.next();
                addons.add(childNode.asText());
            }
        }


        return  (SalesOrder.getBuilderByCategory(category)).withId(id).withCategory(category).withSale(name, price, rewardsScheme, size, addons).build();

    }

}
