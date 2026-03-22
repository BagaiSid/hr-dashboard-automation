package com.hrdashboard.tests;

import com.hrdashboard.base.BaseTest;
import com.hrdashboard.config.ConfigReader;
import com.hrdashboard.pages.DashboardPage;
import com.hrdashboard.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class LoginTest extends BaseTest {

    private LoginPage loginPage;

    @BeforeMethod
    public void initPage() {
        loginPage = new LoginPage();
    }

    @Test(priority = 1, description = "Verify login page is displayed")
    public void verifyLoginPageIsDisplayed() {
        Assert.assertTrue(loginPage.isLoginPageDisplayed(),
                "Login page should be displayed");
    }

    @Test(priority = 2, description = "Verify successful login with valid credentials")
    public void verifySuccessfulLogin() {
        DashboardPage dashboardPage = loginPage.login(
                ConfigReader.get("admin.username"),
                ConfigReader.get("admin.password"));

        Assert.assertTrue(dashboardPage.isDashboardDisplayed(),
                "Dashboard should be displayed after login");
    }

    @Test(priority = 3, description = "Verify login fails with invalid credentials")
    public void verifyLoginWithInvalidCredentials() {
        loginPage.enterUsername("invalidUser");
        loginPage.enterPassword("invalidPass");
        loginPage.clickLoginExpectingFailure();

        Assert.assertTrue(loginPage.isErrorMessageDisplayed(),
                "Error message should be displayed for invalid credentials");
    }

    @Test(priority = 4, description = "Verify login fails with empty username")
    public void verifyLoginWithEmptyUsername() {
        loginPage.enterUsername("");
        loginPage.enterPassword("password123");
        loginPage.clickLoginExpectingFailure();

        Assert.assertTrue(loginPage.isErrorMessageDisplayed(),
                "Error message should be displayed for empty username");
    }

    @Test(priority = 5, description = "Verify login fails with empty password")
    public void verifyLoginWithEmptyPassword() {
        loginPage.enterUsername("admin");
        loginPage.enterPassword("");
        loginPage.clickLoginExpectingFailure();

        Assert.assertTrue(loginPage.isErrorMessageDisplayed(),
                "Error message should be displayed for empty password");
    }

    @DataProvider(name = "invalidCredentials")
    public Object[][] invalidCredentials() {
        return new Object[][]{
                {"admin", "wrongpassword"},
                {"wronguser", "admin123"},
                {"", "admin123"},
                {"admin", ""},
                {"", ""},
                {"admin@special", "pass<script>"}
        };
    }

    @Test(priority = 6, dataProvider = "invalidCredentials",
            description = "Verify login fails with various invalid credentials")
    public void verifyLoginWithMultipleInvalidCredentials(String username, String password) {
        loginPage.enterUsername(username);
        loginPage.enterPassword(password);
        loginPage.clickLoginExpectingFailure();

        Assert.assertTrue(loginPage.isErrorMessageDisplayed(),
                "Error message should be displayed for invalid credentials: "
                        + username + "/" + password);
    }
}
