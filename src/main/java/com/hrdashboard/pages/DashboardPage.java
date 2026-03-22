package com.hrdashboard.pages;

import com.hrdashboard.driver.DriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class DashboardPage extends BasePage {

    @FindBy(css = ".header h1")
    private WebElement dashboardHeader;

    @FindBy(id = "headCount")
    private WebElement headCountStat;

    @FindBy(id = "hiresTotal")
    private WebElement hiresTotalStat;

    @FindBy(id = "openTotal")
    private WebElement openTotalStat;

    @FindBy(id = "termTotal")
    private WebElement termTotalStat;

    @FindBy(id = "applicantsTotal")
    private WebElement applicantsTotalStat;

    @FindBy(css = ".nav-bar a[href='/employees.html']")
    private WebElement employeesNavLink;

    @FindBy(css = ".nav-bar a[href='/leave.html']")
    private WebElement leaveNavLink;

    public boolean isDashboardDisplayed() {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(10))
                    .until(ExpectedConditions.urlContains("/index.html"));
            return isDisplayed(dashboardHeader);
        } catch (Exception e) {
            return false;
        }
    }

    public String getWelcomeMessage() {
        return getText(dashboardHeader);
    }

    public String getTotalEmployeesCount() {
        return getText(headCountStat);
    }

    public String getActiveEmployeesCount() {
        return getText(hiresTotalStat);
    }

    public String getDepartmentsCount() {
        return getText(openTotalStat);
    }

    public String getOpenPositionsCount() {
        return getText(termTotalStat);
    }

    public EmployeePage navigateToEmployees() {
        click(employeesNavLink);
        return new EmployeePage();
    }

    public LeavePage navigateToLeave() {
        click(leaveNavLink);
        return new LeavePage();
    }

    public LoginPage logout() {
        try {
            WebElement logoutBtn = driver.findElement(
                    By.cssSelector(".nav-user-info button[onclick='logout()']"));
            logoutBtn.click();
        } catch (Exception e) {
            ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("logout()");
        }
        return new LoginPage();
    }

    public int getRecentActivityCount() {
        try {
            return driver.findElements(By.cssSelector(".card")).size();
        } catch (Exception e) {
            return 0;
        }
    }
}
