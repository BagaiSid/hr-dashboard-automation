package com.hrdashboard.utils;

import com.hrdashboard.driver.DriverManager;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import com.hrdashboard.config.ConfigReader;

public class WaitUtil {

    private WaitUtil() {
    }

    private static WebDriverWait getWait() {
        return new WebDriverWait(DriverManager.getDriver(),
                Duration.ofSeconds(ConfigReader.getExplicitWait()));
    }

    public static WebElement waitForVisibility(WebElement element) {
        return getWait().until(ExpectedConditions.visibilityOf(element));
    }

    public static WebElement waitForClickable(WebElement element) {
        return getWait().until(ExpectedConditions.elementToBeClickable(element));
    }

    public static boolean waitForInvisibility(WebElement element) {
        return getWait().until(ExpectedConditions.invisibilityOf(element));
    }

    public static void waitForTitleContains(String title) {
        getWait().until(ExpectedConditions.titleContains(title));
    }
}
