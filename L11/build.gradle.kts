plugins {
    java
}

group = "com.university.system"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:6.1.3"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation("org.mockito:mockito-core:5.24.0")
    testImplementation(platform("org.testcontainers:testcontainers-bom:2.0.5"))
    testImplementation("org.testcontainers:testcontainers-junit-jupiter")
    testImplementation("org.testcontainers:testcontainers-postgresql")
    testImplementation("org.testcontainers:testcontainers-selenium")
    testImplementation("redis.clients:jedis:8.0.2")
    testImplementation("org.postgresql:postgresql:42.7.14")
    testImplementation("org.seleniumhq.selenium:selenium-java:4.52.0")
}

tasks.test {
    useJUnitPlatform()
}