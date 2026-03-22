package com.hrdashboard.tests;

import com.hrdashboard.base.BaseTest;
import com.hrdashboard.config.ConfigReader;
import com.hrdashboard.pages.LeavePage;
import com.hrdashboard.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class LeaveTest extends BaseTest {

    private LeavePage leavePage;

    @BeforeMethod
    public void loginAndNavigate() {
        LoginPage loginPage = new LoginPage();
        leavePage = loginPage.login(
                ConfigReader.get("admin.username"),
                ConfigReader.get("admin.password"))
                .navigateToLeave();
    }

    @Test(priority = 1, description = "Verify leave page is displayed")
    public void verifyLeavePageIsDisplayed() {
        Assert.assertTrue(leavePage.isLeavePageDisplayed(),
                "Leave page should be displayed");
    }

    @Test(priority = 2, description = "Verify leave balance is displayed")
    public void verifyLeaveBalanceDisplayed() {
        String annualBalance = leavePage.getAnnualLeaveBalance();
        String sickBalance = leavePage.getSickLeaveBalance();

        Assert.assertNotNull(annualBalance, "Annual leave balance should not be null");
        Assert.assertNotNull(sickBalance, "Sick leave balance should not be null");
    }

    @Test(priority = 3, description = "Verify apply for annual leave")
    public void verifyApplyForAnnualLeave() {
        leavePage.applyForLeave(
                "Annual Leave",
                "2026-04-01",
                "2026-04-05",
                "Family vacation"
        );

        String message = leavePage.getSuccessMessage();
        Assert.assertTrue(message.contains("success"),
                "Success message should be displayed after applying for leave");
    }

    @Test(priority = 4, description = "Verify apply for sick leave")
    public void verifyApplyForSickLeave() {
        leavePage.applyForLeave(
                "Sick Leave",
                "2026-04-10",
                "2026-04-11",
                "Medical appointment"
        );

        String message = leavePage.getSuccessMessage();
        Assert.assertTrue(message.contains("success"),
                "Success message should be displayed after applying for sick leave");
    }

    @Test(priority = 5, description = "Verify leave request list is displayed")
    public void verifyLeaveRequestList() {
        int count = leavePage.getLeaveRequestCount();
        Assert.assertTrue(count >= 0, "Leave request count should be non-negative");
    }

    @Test(priority = 6, description = "Verify leave request status")
    public void verifyLeaveRequestStatus() {
        String status = leavePage.getFirstLeaveStatus();
        Assert.assertNotNull(status, "Leave status should not be null");
        Assert.assertTrue(
                status.equals("Pending") || status.equals("Approved") || status.equals("Rejected"),
                "Leave status should be Pending, Approved, or Rejected");
    }
}
