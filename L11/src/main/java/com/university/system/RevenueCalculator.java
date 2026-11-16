package com.university.system;

public class RevenueCalculator {
    private boolean running = false;

    public void reset() {
        running = false;
    }

    public void start() {
        running = true;
    }

    public void stop() {
        running = false;
    }

    public int calculate(TicketRevenue ticket) {
        if (ticket == null) {
            throw new IllegalArgumentException("Об'єкт TicketRevenue не може бути null");
        }
        return ticket.getTotal();
    }
}
