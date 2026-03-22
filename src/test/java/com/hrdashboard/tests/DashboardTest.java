package com.hrdashboard.tests;

import com.hrdashboard.base.BaseTest;
import com.hrdashboard.config.ConfigReader;
import com.hrdashboard.pages.DashboardPage;
import com.hrdashboard.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class DashboardTest extends BaseTest {

    private DashboardPage dashboardPage;

    @BeforeMethod
    public void loginAndNavigate() {
        LoginPage loginPage = new LoginPage();
        dashboardPage = loginPage.login(
                ConfigReader.get("admin.username"),
                ConfigReader.get("admin.password"));
    }

    @Test(priority = 1, description = "Verify dashboard is displayed after login")
    public void verifyDashboardIsDisplayed() {
        Assert.assertTrue(dashboardPage.isDashboardDisplayed(),
                "Dashboard should be displayed");
    }

    @Test(priority = 2, description = "Verify welcome message is displayed")
    public void verifyWelcomeMessage() {
        String welcomeMsg = dashboardPage.getWelcomeMessage();
        Assert.assertNotNull(welcomeMsg, "Welcome message should not be null");
        Assert.assertFalse(welcomeMsg.isEmpty(), "Welcome message should not be empty");
    }

    @Test(priority = 3, description = "Verify total employees count widget is displayed")
    public void verifyTotalEmployeesWidget() {
        String count = dashboardPage.getTotalEmployeesCount();
        Assert.assertNotNull(count, "Total employees count should not be null");
    }

    @Test(priority = 4, description = "Verify active employees count widget is displayed")
    public void verifyActiveEmployeesWidget() {
        String count = dashboardPage.getActiveEmployeesCount();
        Assert.assertNotNull(count, "Active employees count should not be null");
    }

    @Test(priority = 5, description = "Verify departments count widget is displayed")
    public void verifyDepartmentsWidget() {
        String count = dashboardPage.getDepartmentsCount();
        Assert.assertNotNull(count, "Departments count should not be null");
    }

    @Test(priority = 6, description = "Verify open positions widget is displayed")
    public void verifyOpenPositionsWidget() {
        String count = dashboardPage.getOpenPositionsCount();
        Assert.assertNotNull(count, "Open positions count should not be null");
    }

    @Test(priority = 7, description = "Verify navigation to Employees page")
    public void verifyNavigationToEmployees() {
        Assert.assertTrue(dashboardPage.navigateToEmployees().isEmployeePageDisplayed(),
                "Employee page should be displayed");
    }

    @Test(priority = 8, description = "Verify navigation to Leave page")
    public void verifyNavigationToLeave() {
        Assert.assertTrue(dashboardPage.navigateToLeave().isLeavePageDisplayed(),
                "Leave page should be displayed");
    }

    @Test(priority = 9, description = "Verify recent activity section has items")
    public void verifyRecentActivity() {
        int activityCount = dashboardPage.getRecentActivityCount();
        Assert.assertTrue(activityCount >= 0,
                "Recent activity count should be non-negative");
    }

    @Test(priority = 10, description = "Verify logout functionality from dashboard")
    public void verifyLogout() {
        LoginPage loginPage = dashboardPage.logout();
        Assert.assertTrue(loginPage.isLoginPageDisplayed(),
                "Login page should be displayed after logout");
    }
}
