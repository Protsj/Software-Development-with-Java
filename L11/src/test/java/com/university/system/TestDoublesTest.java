package com.university.system;

import org.junit.jupiter.api.Test;
import org.mockito.MockMakers;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TestDoublesTest {

    @Test
    public void dummyObjectTest() {
        String dummyName = "Вася"; // Пустушка для задоволення сигнатури[cite: 12]
        Ticket ticket = new Ticket(dummyName, new BigDecimal("10"));
        assertEquals(new BigDecimal("9.0"), ticket.getDiscountPrice());
    }

    @Test
    public void stubObjectTest() {
        Price price = new StubPrice(); // Заглушка[cite: 12]
        Ticket ticket = new Ticket(price);
        assertEquals(new BigDecimal("9.0"), ticket.getDiscountPrice());
    }

    @Test
    public void fakeDatabaseTest() {
        TicketDatabase db = new FakeTicketDatabase(); // Сурогат БД[cite: 12]
        Ticket ticket = new Ticket("Артем", new BigDecimal("50"));
        db.insert(ticket);

        assertEquals(1, db.count());
        assertNotNull(db.findByName("Артем"));
    }

    @Test
    public void mockObjectWithMockitoTest() {
        // Макет інтерфейсу Price через PROXY (без потреби в Java-агенті на JDK 27)[cite: 12]
        Price price = mock(Price.class, withSettings().mockMaker(MockMakers.PROXY));
        when(price.getInitialPrice()).thenReturn(new BigDecimal("10"));

        Ticket ticket = new Ticket(price, new BigDecimal("0.9"));
        assertEquals(new BigDecimal("9.0"), ticket.getDiscountPrice());
        verify(price).getInitialPrice();
    }
}
