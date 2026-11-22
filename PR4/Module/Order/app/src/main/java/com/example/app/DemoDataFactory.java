package com.example.app;

import com.example.product.model.Clothing;
import com.example.product.model.Electronics;
import com.github.javafaker.Faker;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.IntStream;

/** Builds random products with JavaFaker and the Lombok-generated builders. */
public final class DemoDataFactory {
    private final Faker faker;

    public DemoDataFactory(long seed) {
        this.faker = new Faker(new Random(seed));
    }

    public List<Electronics> electronics(int count) {
        return IntStream.range(0, count)
                .mapToObj(i -> Electronics.builder()
                        .id(UUID.randomUUID())
                        .name(faker.commerce().productName())
                        .price(BigDecimal.valueOf(faker.number().randomDouble(2, 20, 2000)))
                        .brand(faker.company().name())
                        .warrantyMonths(faker.number().numberBetween(6, 36))
                        .build())
                .toList();
    }

    public List<Clothing> clothing(int count) {
        return IntStream.range(0, count)
                .mapToObj(i -> Clothing.builder()
                        .id(UUID.randomUUID())
                        .name(faker.commerce().productName())
                        .price(BigDecimal.valueOf(faker.number().randomDouble(2, 15, 300)))
                        .size(faker.options().option("XS", "S", "M", "L", "XL"))
                        .material(faker.options().option("Cotton", "Wool", "Denim", "Polyester"))
                        .build())
                .toList();
    }
}
