package com.hrdashboard.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class DashboardPage extends BasePage {

    @FindBy(css = ".dashboard-header")
    private WebElement dashboardHeader;

    @FindBy(css = ".welcome-message")
    private WebElement welcomeMessage;

    @FindBy(css = ".total-employees .count")
    private WebElement totalEmployeesCount;

    @FindBy(css = ".active-employees .count")
    private WebElement activeEmployeesCount;

    @FindBy(css = ".departments .count")
    private WebElement departmentsCount;

    @FindBy(css = ".open-positions .count")
    private WebElement openPositionsCount;

    @FindBy(css = ".nav-employees")
    private WebElement employeesNavLink;

    @FindBy(css = ".nav-departments")
    private WebElement departmentsNavLink;

    @FindBy(css = ".nav-leave")
    private WebElement leaveNavLink;

    @FindBy(css = ".nav-recruitment")
    private WebElement recruitmentNavLink;

    @FindBy(css = ".logout-btn")
    private WebElement logoutButton;

    @FindBy(css = ".recent-activity-item")
    private List<WebElement> recentActivityItems;

    public boolean isDashboardDisplayed() {
        return isDisplayed(dashboardHeader);
    }

    public String getWelcomeMessage() {
        return getText(welcomeMessage);
    }

    public String getTotalEmployeesCount() {
        return getText(totalEmployeesCount);
    }

    public String getActiveEmployeesCount() {
        return getText(activeEmployeesCount);
    }

    public String getDepartmentsCount() {
        return getText(departmentsCount);
    }

    public String getOpenPositionsCount() {
        return getText(openPositionsCount);
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
        click(logoutButton);
        return new LoginPage();
    }

    public int getRecentActivityCount() {
        return recentActivityItems.size();
    }
}
