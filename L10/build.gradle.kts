plugins {
    `java-library`
    application
}

group = "com.university"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

testing {
    suites {
        named<JvmTestSuite>("test") {
            useJUnitJupiter("6.1.3")
        }
    }
}

tasks.register("universityTask") {
    println("Налаштовуємо завдання для проєкту...")
    doLast {
        println("Вітаємо від Gradle та університету!")
    }
}