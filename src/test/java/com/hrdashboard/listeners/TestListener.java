package com.hrdashboard.listeners;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.hrdashboard.driver.DriverManager;
import com.hrdashboard.reports.ExtentReportManager;
import com.hrdashboard.utils.ScreenRecorderUtil;
import com.hrdashboard.utils.ScreenshotUtil;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.File;

public class TestListener implements ITestListener {

    private static final ExtentReports extent = ExtentReportManager.getInstance();
    private static final ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();

    @Override
    public void onTestStart(ITestResult result) {
        String testName = result.getTestClass().getRealClass().getSimpleName()
                + " :: " + result.getMethod().getMethodName();
        ExtentTest test = extent.createTest(testName);
        test.assignCategory(result.getTestClass().getRealClass().getSimpleName());
        extentTest.set(test);

        ScreenRecorderUtil.startRecording(result.getMethod().getMethodName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        try {
            if (DriverManager.getDriver() != null) {
                String base64 = ScreenshotUtil.takeScreenshotBase64();
                extentTest.get().pass("Test passed successfully");
                extentTest.get().addScreenCaptureFromBase64String(base64, "Final State");
            } else {
                extentTest.get().pass("Test passed successfully");
            }
        } catch (Exception e) {
            extentTest.get().pass("Test passed (screenshot unavailable)");
        }
        stopRecordingAndAttach();
    }

    @Override
    public void onTestFailure(ITestResult result) {
        try {
            if (DriverManager.getDriver() != null) {
                ScreenshotUtil.takeScreenshot(result.getName());
                String base64 = ScreenshotUtil.takeScreenshotBase64();
                extentTest.get().fail(result.getThrowable());
                extentTest.get().addScreenCaptureFromBase64String(base64, "Failure Screenshot");
            } else {
                extentTest.get().fail(result.getThrowable());
            }
        } catch (Exception e) {
            extentTest.get().fail(result.getThrowable());
        }
        stopRecordingAndAttach();
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        extentTest.get().skip("Test skipped");
        if (result.getThrowable() != null) {
            extentTest.get().skip(result.getThrowable());
        }
        stopRecordingAndAttach();
    }

    @Override
    public void onStart(ITestContext context) {
        // Suite started
    }

    @Override
    public void onFinish(ITestContext context) {
        ExtentReportManager.flush();
    }

    public static ExtentTest getExtentTest() {
        return extentTest.get();
    }

    private void stopRecordingAndAttach() {
        File recording = ScreenRecorderUtil.stopRecording();
        if (recording != null && recording.exists()) {
            extentTest.get().info("Recording saved: " + recording.getName());
        }
    }
}
