package com.university.system;

import java.util.HashMap;

public class FakeTicketDatabase implements TicketDatabase {
    private final HashMap<Integer, Ticket> tickets = new HashMap<>();
    private Integer nextId = 1;

    @Override
    public Ticket findByName(String name) {
        return tickets.values().stream()
                .filter(ticket -> ticket.getName().equals(name))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void insert(Ticket ticket) {
        tickets.put(nextId++, ticket);
    }

    @Override
    public int count() {
        return tickets.size();
    }
}
