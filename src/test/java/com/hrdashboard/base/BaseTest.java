package com.hrdashboard.base;

import com.hrdashboard.config.ConfigReader;
import com.hrdashboard.driver.DriverManager;
import com.hrdashboard.utils.ScreenshotUtil;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public class BaseTest {

    @BeforeMethod
    public void setUp() {
        DriverManager.initDriver();
        DriverManager.getDriver().get(ConfigReader.getBaseUrl());
    }

    @AfterMethod
    public void tearDown(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE) {
            ScreenshotUtil.takeScreenshot(result.getName());
        }
        DriverManager.quitDriver();
    }
}
