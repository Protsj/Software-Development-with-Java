package com.university.system;

import java.math.BigDecimal;

public class TicketRevenue {
    private static final int TICKET_PRICE = 50;
    private final int tickets;
    private final int price;

    public TicketRevenue() {
        this.tickets = 0;
        this.price = TICKET_PRICE;
    }

    public TicketRevenue(int tickets, int price) {
        this.tickets = tickets;
        this.price = price;
    }

    public int getTotal() {
        return tickets * price;
    }

    public BigDecimal estimateTotalRevenue(int numberOfTicketsSold) {
        if (numberOfTicketsSold < 0 || numberOfTicketsSold > 100) {
            throw new IllegalArgumentException("Невірна кількість квитків: " + numberOfTicketsSold);
        }
        return new BigDecimal(TICKET_PRICE * numberOfTicketsSold);
    }
}
