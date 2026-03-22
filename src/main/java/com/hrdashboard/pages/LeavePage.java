package com.hrdashboard.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

public class LeavePage extends BasePage {

    @FindBy(css = ".leave-page-title")
    private WebElement pageTitle;

    @FindBy(id = "applyLeaveBtn")
    private WebElement applyLeaveButton;

    @FindBy(id = "leaveType")
    private WebElement leaveTypeDropdown;

    @FindBy(id = "startDate")
    private WebElement startDateInput;

    @FindBy(id = "endDate")
    private WebElement endDateInput;

    @FindBy(id = "leaveReason")
    private WebElement reasonTextarea;

    @FindBy(id = "submitLeaveBtn")
    private WebElement submitLeaveButton;

    @FindBy(css = ".leave-table tbody tr")
    private List<WebElement> leaveRows;

    @FindBy(css = ".leave-balance .annual")
    private WebElement annualLeaveBalance;

    @FindBy(css = ".leave-balance .sick")
    private WebElement sickLeaveBalance;

    @FindBy(css = ".success-message")
    private WebElement successMessage;

    @FindBy(css = ".leave-table tbody tr:first-child .status")
    private WebElement firstLeaveStatus;

    public boolean isLeavePageDisplayed() {
        return isDisplayed(pageTitle);
    }

    public LeavePage clickApplyLeave() {
        click(applyLeaveButton);
        return this;
    }

    public LeavePage selectLeaveType(String leaveType) {
        Select select = new Select(leaveTypeDropdown);
        select.selectByVisibleText(leaveType);
        return this;
    }

    public LeavePage enterStartDate(String startDate) {
        type(startDateInput, startDate);
        return this;
    }

    public LeavePage enterEndDate(String endDate) {
        type(endDateInput, endDate);
        return this;
    }

    public LeavePage enterReason(String reason) {
        type(reasonTextarea, reason);
        return this;
    }

    public LeavePage submitLeaveRequest() {
        click(submitLeaveButton);
        return this;
    }

    public LeavePage applyForLeave(String leaveType, String startDate,
                                    String endDate, String reason) {
        clickApplyLeave();
        selectLeaveType(leaveType);
        enterStartDate(startDate);
        enterEndDate(endDate);
        enterReason(reason);
        submitLeaveRequest();
        return this;
    }

    public String getAnnualLeaveBalance() {
        return getText(annualLeaveBalance);
    }

    public String getSickLeaveBalance() {
        return getText(sickLeaveBalance);
    }

    public int getLeaveRequestCount() {
        return leaveRows.size();
    }

    public String getSuccessMessage() {
        return getText(successMessage);
    }

    public String getFirstLeaveStatus() {
        return getText(firstLeaveStatus);
    }
}
