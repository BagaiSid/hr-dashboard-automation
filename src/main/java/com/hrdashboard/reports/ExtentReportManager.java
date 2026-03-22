package com.hrdashboard.reports;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class ExtentReportManager {

    private static ExtentReports extent;

    private ExtentReportManager() {}

    public static synchronized ExtentReports getInstance() {
        if (extent == null) {
            extent = new ExtentReports();

            ExtentSparkReporter spark = new ExtentSparkReporter("target/extent-reports/TestExecutionReport.html");
            spark.config().setTheme(Theme.DARK);
            spark.config().setDocumentTitle("HR Dashboard - Test Execution Report");
            spark.config().setReportName("Test Execution Coverage Report");
            spark.config().setTimeStampFormat("yyyy-MM-dd HH:mm:ss");

            extent.attachReporter(spark);
            extent.setSystemInfo("Application", "HR Dashboard");
            extent.setSystemInfo("Browser", System.getProperty("browser", "chrome"));
            extent.setSystemInfo("Base URL", System.getProperty("base.url", "http://localhost:4000"));
            extent.setSystemInfo("OS", System.getProperty("os.name") + " " + System.getProperty("os.version"));
            extent.setSystemInfo("Java", System.getProperty("java.version"));
        }
        return extent;
    }

    public static synchronized void flush() {
        if (extent != null) {
            extent.flush();
        }
    }
}
