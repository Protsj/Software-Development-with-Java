package com.university.system;

import java.math.BigDecimal;

public class Ticket {
    private final String clientName;
    private final Price price;
    private final BigDecimal discountMultiplier;

    public Ticket(String clientName, BigDecimal fixedPrice) {
        this.clientName = clientName;
        this.price = () -> fixedPrice;
        this.discountMultiplier = new BigDecimal("0.9");
    }

    public Ticket(Price price) {
        this("Клієнт", price, new BigDecimal("0.9"));
    }

    public Ticket(Price price, BigDecimal discountMultiplier) {
        this("Клієнт", price, discountMultiplier);
    }

    public Ticket(String clientName, Price price, BigDecimal discountMultiplier) {
        this.clientName = clientName;
        this.price = price;
        this.discountMultiplier = discountMultiplier;
    }

    public String getName() {
        return clientName;
    }

    public BigDecimal getDiscountPrice() {
        return price.getInitialPrice().multiply(discountMultiplier);
    }
}
