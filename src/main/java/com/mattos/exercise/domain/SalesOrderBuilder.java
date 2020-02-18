package com.mattos.exercise.domain;

import java.util.List;

public abstract class SalesOrderBuilder {

    protected String id;
    protected String category;
    protected Double price;
    protected RewardScheme rewardScheme;
    protected String name;
    protected String size;
    protected List<String> addons;

    public SalesOrderBuilder withId(final String id) {
        this.id = id;
        return this;
    }

    public SalesOrderBuilder withCategory(final String category) {
        this.category = category;
        return this;
    }

    public SalesOrderBuilder withSale(final String name, final Double price, final RewardScheme rewardScheme, final String size, final List<String> addons) {
        this.price = price;
        this.rewardScheme= rewardScheme;
        this.name = name;
        this.size = size;
        this.addons = addons;
        return this;
    }

    public abstract SalesOrder build() ;



}
