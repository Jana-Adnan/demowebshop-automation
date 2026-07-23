package reporting;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

/**
 * TestNG listener that builds an HTML report (Reports/Professional_Checkout_Report.html)
 * summarizing pass/fail status for every test method that ran.
 * Wired in via testng.xml under <listeners>.
 */
public class ExtentReportListener implements ITestListener {
    private static ExtentReports extent;
    private static ExtentTest test;

    @Override
    public void onStart(ITestContext context) {
        ExtentSparkReporter spark = new ExtentSparkReporter("Reports/Professional_Checkout_Report.html");
        spark.config().setReportName("DEPI Final Project - DemoWebShop Automation");
        spark.config().setDocumentTitle("Automation Results");

        extent = new ExtentReports();
        extent.attachReporter(spark);
        extent.setSystemInfo("Tester", "Omar Hassan & Team");
        extent.setSystemInfo("Environment", "QA");
    }

    @Override
    public void onTestStart(ITestResult result) {
        test = extent.createTest(result.getMethod().getMethodName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        test.pass("Test Passed Successfully! ✅");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        test.fail("Test Failed! ❌ " + result.getThrowable());
    }

    @Override
    public void onFinish(ITestContext context) {
        extent.flush();
    }
}
