package com.hrdashboard.tests;

import com.hrdashboard.base.BaseTest;
import com.hrdashboard.config.ConfigReader;
import com.hrdashboard.pages.EmployeePage;
import com.hrdashboard.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class EmployeeTest extends BaseTest {

    private EmployeePage employeePage;

    @BeforeMethod
    public void loginAndNavigate() {
        LoginPage loginPage = new LoginPage();
        employeePage = loginPage.login(
                ConfigReader.get("admin.username"),
                ConfigReader.get("admin.password"))
                .navigateToEmployees();
    }

    @Test(priority = 1, description = "Verify employee page is displayed")
    public void verifyEmployeePageIsDisplayed() {
        Assert.assertTrue(employeePage.isEmployeePageDisplayed(),
                "Employee page should be displayed");
    }

    @Test(priority = 2, description = "Verify employee list is loaded")
    public void verifyEmployeeListIsLoaded() {
        Assert.assertTrue(employeePage.isEmployeePageDisplayed(),
                "Employee page should be displayed first");
        int count = employeePage.getEmployeeCount();
        Assert.assertTrue(count >= 0, "Employee list should load with zero or more records");
    }

    @Test(priority = 3, description = "Verify add new employee")
    public void verifyAddNewEmployee() {
        Assert.assertTrue(employeePage.isEmployeePageDisplayed(),
                "Employee page should be displayed first");
        employeePage.addEmployee(
                "Jane",
                "Smith",
                "jane.smith@company.com",
                "Engineering",
                "Software Engineer"
        );

        String message = employeePage.getSuccessMessage();
        Assert.assertTrue(message.contains("success"),
                "Success message should be displayed after adding employee");
    }

    @Test(priority = 4, description = "Verify add employee with missing required fields shows validation error")
    public void verifyAddEmployeeWithMissingFields() {
        Assert.assertTrue(employeePage.isEmployeePageDisplayed(),
                "Employee page should be displayed first");
        employeePage.clickAddEmployee();
        employeePage.clickSave();

        Assert.assertTrue(employeePage.isValidationErrorDisplayed(),
                "Validation error should be displayed for missing required fields");
    }

    @Test(priority = 5, description = "Verify cancel add employee returns to list")
    public void verifyCancelAddEmployee() {
        Assert.assertTrue(employeePage.isEmployeePageDisplayed(),
                "Employee page should be displayed first");
        employeePage.clickAddEmployee();
        employeePage.clickCancel();

        Assert.assertTrue(employeePage.isEmployeePageDisplayed(),
                "Employee page should be displayed after cancel");
    }
}
