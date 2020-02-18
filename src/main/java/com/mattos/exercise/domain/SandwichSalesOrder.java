package com.mattos.exercise.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SandwichSalesOrder extends SalesOrder{

    private List<String> addons;

    public List<String> getAddons() { return addons; }

    public static class Builder extends SalesOrderBuilder {

        public SandwichSalesOrder build() {

            SandwichSalesOrder result = new SandwichSalesOrder();
            result.id = id;
            result.category = category;
            result.price = price;
            result.rewardScheme= rewardScheme;
            result.name = name;
            result.addons = addons;
            return result;
        }
    }

}
