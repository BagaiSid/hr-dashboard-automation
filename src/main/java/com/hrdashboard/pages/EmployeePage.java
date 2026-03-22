package com.hrdashboard.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

public class EmployeePage extends BasePage {

    @FindBy(css = ".employee-page-title")
    private WebElement pageTitle;

    @FindBy(id = "searchEmployee")
    private WebElement searchInput;

    @FindBy(id = "searchBtn")
    private WebElement searchButton;

    @FindBy(id = "addEmployeeBtn")
    private WebElement addEmployeeButton;

    @FindBy(css = ".employee-table tbody tr")
    private List<WebElement> employeeRows;

    @FindBy(id = "firstName")
    private WebElement firstNameInput;

    @FindBy(id = "lastName")
    private WebElement lastNameInput;

    @FindBy(id = "email")
    private WebElement emailInput;

    @FindBy(id = "department")
    private WebElement departmentDropdown;

    @FindBy(id = "position")
    private WebElement positionInput;

    @FindBy(id = "saveEmployeeBtn")
    private WebElement saveButton;

    @FindBy(id = "cancelBtn")
    private WebElement cancelButton;

    @FindBy(css = ".success-message")
    private WebElement successMessage;

    @FindBy(css = ".validation-error")
    private WebElement validationError;

    @FindBy(css = ".employee-table tbody tr:first-child .delete-btn")
    private WebElement firstDeleteButton;

    @FindBy(css = ".employee-table tbody tr:first-child .edit-btn")
    private WebElement firstEditButton;

    @FindBy(id = "confirmDeleteBtn")
    private WebElement confirmDeleteButton;

    public boolean isEmployeePageDisplayed() {
        return isDisplayed(pageTitle);
    }

    public EmployeePage searchEmployee(String name) {
        type(searchInput, name);
        click(searchButton);
        return this;
    }

    public EmployeePage clickAddEmployee() {
        click(addEmployeeButton);
        return this;
    }

    public EmployeePage enterFirstName(String firstName) {
        type(firstNameInput, firstName);
        return this;
    }

    public EmployeePage enterLastName(String lastName) {
        type(lastNameInput, lastName);
        return this;
    }

    public EmployeePage enterEmail(String email) {
        type(emailInput, email);
        return this;
    }

    public EmployeePage selectDepartment(String department) {
        Select select = new Select(departmentDropdown);
        select.selectByVisibleText(department);
        return this;
    }

    public EmployeePage enterPosition(String position) {
        type(positionInput, position);
        return this;
    }

    public EmployeePage clickSave() {
        click(saveButton);
        return this;
    }

    public EmployeePage clickCancel() {
        click(cancelButton);
        return this;
    }

    public EmployeePage addEmployee(String firstName, String lastName,
                                     String email, String department,
                                     String position) {
        clickAddEmployee();
        enterFirstName(firstName);
        enterLastName(lastName);
        enterEmail(email);
        selectDepartment(department);
        enterPosition(position);
        clickSave();
        return this;
    }

    public int getEmployeeCount() {
        return employeeRows.size();
    }

    public String getSuccessMessage() {
        return getText(successMessage);
    }

    public boolean isValidationErrorDisplayed() {
        return isDisplayed(validationError);
    }

    public EmployeePage clickEditFirstEmployee() {
        click(firstEditButton);
        return this;
    }

    public EmployeePage clickDeleteFirstEmployee() {
        click(firstDeleteButton);
        return this;
    }

    public EmployeePage confirmDelete() {
        click(confirmDeleteButton);
        return this;
    }
}
