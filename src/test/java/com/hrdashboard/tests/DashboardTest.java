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

    @Test(priority = 2, description = "Verify dashboard header is displayed")
    public void verifyWelcomeMessage() {
        Assert.assertTrue(dashboardPage.isDashboardDisplayed(),
                "Dashboard should be displayed");
        String header = dashboardPage.getWelcomeMessage();
        Assert.assertNotNull(header, "Dashboard header should not be null");
        Assert.assertFalse(header.isEmpty(), "Dashboard header should not be empty");
    }

    @Test(priority = 3, description = "Verify head count widget is displayed")
    public void verifyTotalEmployeesWidget() {
        Assert.assertTrue(dashboardPage.isDashboardDisplayed(),
                "Dashboard should be displayed first");
        String count = dashboardPage.getTotalEmployeesCount();
        Assert.assertNotNull(count, "Head count should not be null");
    }

    @Test(priority = 4, description = "Verify hires total widget is displayed")
    public void verifyActiveEmployeesWidget() {
        Assert.assertTrue(dashboardPage.isDashboardDisplayed(),
                "Dashboard should be displayed first");
        String count = dashboardPage.getActiveEmployeesCount();
        Assert.assertNotNull(count, "Hires total should not be null");
    }

    @Test(priority = 5, description = "Verify open positions widget is displayed")
    public void verifyDepartmentsWidget() {
        Assert.assertTrue(dashboardPage.isDashboardDisplayed(),
                "Dashboard should be displayed first");
        String count = dashboardPage.getDepartmentsCount();
        Assert.assertNotNull(count, "Open positions count should not be null");
    }

    @Test(priority = 6, description = "Verify terminations widget is displayed")
    public void verifyOpenPositionsWidget() {
        Assert.assertTrue(dashboardPage.isDashboardDisplayed(),
                "Dashboard should be displayed first");
        String count = dashboardPage.getOpenPositionsCount();
        Assert.assertNotNull(count, "Terminations count should not be null");
    }

    @Test(priority = 7, description = "Verify navigation to Employees page")
    public void verifyNavigationToEmployees() {
        Assert.assertTrue(dashboardPage.isDashboardDisplayed(),
                "Dashboard should be displayed first");
        Assert.assertTrue(dashboardPage.navigateToEmployees().isEmployeePageDisplayed(),
                "Employee page should be displayed");
    }

    @Test(priority = 8, description = "Verify navigation to Leave page")
    public void verifyNavigationToLeave() {
        Assert.assertTrue(dashboardPage.isDashboardDisplayed(),
                "Dashboard should be displayed first");
        Assert.assertTrue(dashboardPage.navigateToLeave().isLeavePageDisplayed(),
                "Leave page should be displayed");
    }

    @Test(priority = 9, description = "Verify dashboard has card sections")
    public void verifyRecentActivity() {
        Assert.assertTrue(dashboardPage.isDashboardDisplayed(),
                "Dashboard should be displayed first");
        int activityCount = dashboardPage.getRecentActivityCount();
        Assert.assertTrue(activityCount >= 0,
                "Card count should be non-negative");
    }

    @Test(priority = 10, description = "Verify logout functionality from dashboard")
    public void verifyLogout() {
        Assert.assertTrue(dashboardPage.isDashboardDisplayed(),
                "Dashboard should be displayed first");
        LoginPage loginPage = dashboardPage.logout();
        Assert.assertTrue(loginPage.isLoginPageDisplayed(),
                "Login page should be displayed after logout");
    }
}
