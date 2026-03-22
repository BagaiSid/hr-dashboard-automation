package com.hrdashboard.pages;

import com.hrdashboard.driver.DriverManager;
import com.hrdashboard.utils.WaitUtil;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

public abstract class BasePage {

    protected WebDriver driver;

    protected BasePage() {
        this.driver = DriverManager.getDriver();
        PageFactory.initElements(driver, this);
    }

    public String getPageTitle() {
        return driver.getTitle();
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    protected void click(WebElement element) {
        WaitUtil.waitForClickable(element).click();
    }

    protected void type(WebElement element, String text) {
        WaitUtil.waitForVisibility(element);
        element.clear();
        element.sendKeys(text);
    }

    protected String getText(WebElement element) {
        return WaitUtil.waitForVisibility(element).getText();
    }

    protected boolean isDisplayed(WebElement element) {
        try {
            return WaitUtil.waitForVisibility(element).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}
