package base;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

/**
 * Central place that creates and destroys the WebDriver instance.
 * Using a ThreadLocal means each test method/thread gets its own driver,
 * which keeps tests from interfering with each other if you ever run
 * them in parallel.
 */
public class DriverManager {

    private static final ThreadLocal<WebDriver> driverThread = new ThreadLocal<>();

    /** Returns the current thread's WebDriver, creating it if it doesn't exist yet. */
    public static WebDriver getDriver() {
        if (driverThread.get() == null) {
            WebDriverManager.chromedriver().setup();

            ChromeOptions options = new ChromeOptions();

            WebDriver driver = new ChromeDriver(options);
            driver.manage().window().maximize();
            driverThread.set(driver);
        }
        return driverThread.get();
    }

    /** Closes the browser and clears the driver for the current thread. */
    public static void quitDriver() {
        WebDriver driver = driverThread.get();
        if (driver != null) {
            driver.quit();
            driverThread.remove();
        }
    }
}