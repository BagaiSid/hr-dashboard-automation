package com.hrdashboard.base;

import com.hrdashboard.config.ConfigReader;
import com.hrdashboard.driver.DriverManager;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.time.Duration;

public class BaseTest {

    @BeforeMethod
    public void setUp() {
        DriverManager.initDriver();
        DriverManager.getDriver().get(ConfigReader.getBaseUrl() + "/login.html");
        new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(10))
                .until(ExpectedConditions.urlContains("/login.html"));
    }

    @AfterMethod
    public void tearDown() {
        DriverManager.quitDriver();
    }
}
