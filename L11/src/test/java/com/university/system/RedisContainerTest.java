package com.university.system;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.output.OutputFrame;
import org.testcontainers.containers.output.ToStringConsumer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import redis.clients.jedis.RedisClient;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Testcontainers
public class RedisContainerTest {

    @Container
    static GenericContainer<?> redis = new GenericContainer<>("redis:8.10.3-alpine")
            .withExposedPorts(6379);

    public static ToStringConsumer consumer = new ToStringConsumer();

    @BeforeAll
    public static void setUp() {
        redis.followOutput(consumer, OutputFrame.OutputType.STDOUT, OutputFrame.OutputType.STDERR);
    }

    @Test
    void cached() {
        try (var client = RedisClient.create(redis.getHost(), redis.getMappedPort(6379))) {
            client.set("price", "20");
            assertEquals("20", client.get("price"));
        }
    }

    @AfterAll
    public static void tearDown() throws IOException {
        Path log = Path.of("build", "tc.log");
        Files.createDirectories(log.getParent());
        Files.writeString(log, consumer.toUtf8String(), StandardCharsets.UTF_8);
    }
}
