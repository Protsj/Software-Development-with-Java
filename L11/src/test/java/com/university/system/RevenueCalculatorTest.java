package com.university.system;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import static org.junit.jupiter.api.Assertions.*;

public class RevenueCalculatorTest {

    @RegisterExtension
    RevenueCalculatorExtension ext = new RevenueCalculatorExtension(new RevenueCalculator());

    @Test
    void testCalculateRevenue() {
        TicketRevenue ticket = new TicketRevenue(15, 30);
        assertEquals(450, ext.calculator().calculate(ticket));
    }

    @Test
    void testInvalidRevenue() {
        assertThrows(IllegalArgumentException.class, () -> ext.calculator().calculate(null));
    }
}
