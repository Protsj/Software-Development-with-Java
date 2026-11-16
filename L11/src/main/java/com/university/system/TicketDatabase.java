package com.university.system;

public interface TicketDatabase {
    Ticket findByName(String name);
    void insert(Ticket ticket);
    int count();
}
