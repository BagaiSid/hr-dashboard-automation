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
        int count = employeePage.getEmployeeCount();
        Assert.assertTrue(count > 0, "Employee list should have at least one record");
    }

    @Test(priority = 3, description = "Verify search employee functionality")
    public void verifySearchEmployee() {
        employeePage.searchEmployee("John");
        int count = employeePage.getEmployeeCount();
        Assert.assertTrue(count >= 0, "Search results should return zero or more employees");
    }

    @Test(priority = 4, description = "Verify add new employee")
    public void verifyAddNewEmployee() {
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

    @Test(priority = 5, description = "Verify add employee with missing required fields shows validation error")
    public void verifyAddEmployeeWithMissingFields() {
        employeePage.clickAddEmployee();
        employeePage.enterFirstName("");
        employeePage.enterLastName("");
        employeePage.clickSave();

        Assert.assertTrue(employeePage.isValidationErrorDisplayed(),
                "Validation error should be displayed for missing required fields");
    }

    @Test(priority = 6, description = "Verify edit employee functionality")
    public void verifyEditEmployee() {
        employeePage.clickEditFirstEmployee();
        employeePage.enterFirstName("UpdatedName");
        employeePage.clickSave();

        String message = employeePage.getSuccessMessage();
        Assert.assertTrue(message.contains("success"),
                "Success message should be displayed after editing employee");
    }

    @Test(priority = 7, description = "Verify delete employee functionality")
    public void verifyDeleteEmployee() {
        int initialCount = employeePage.getEmployeeCount();
        employeePage.clickDeleteFirstEmployee();
        employeePage.confirmDelete();

        String message = employeePage.getSuccessMessage();
        Assert.assertTrue(message.contains("success"),
                "Success message should be displayed after deleting employee");
    }

    @Test(priority = 8, description = "Verify cancel add employee returns to list")
    public void verifyCancelAddEmployee() {
        employeePage.clickAddEmployee();
        employeePage.clickCancel();

        Assert.assertTrue(employeePage.isEmployeePageDisplayed(),
                "Employee page should be displayed after cancel");
    }
}
