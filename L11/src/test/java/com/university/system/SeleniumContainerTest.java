package com.university.system;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.selenium.BrowserWebDriverContainer;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Testcontainers
public class SeleniumContainerTest {

    @Container
    static BrowserWebDriverContainer chrome = new BrowserWebDriverContainer(
            "selenium/standalone-chrome:4.51.0-20261010");

    @Test
    void checkTheSiteOut() {
        var driver = new RemoteWebDriver(chrome.getSeleniumAddress(), new ChromeOptions());
        try {
            driver.get("data:text/html,<h1>University</h1>");
            WebElement title = driver.findElement(By.tagName("h1"));
            assertEquals("University", title.getText());
        } finally {
            driver.quit();
        }
    }
}
