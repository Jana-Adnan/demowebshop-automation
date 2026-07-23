package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/**
 * Represents the homepage / header area that is present on every page
 * of demowebshop.tricentis.com (top nav, register/login/logout links,
 * mini-cart link).
 */
public class HomePage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By registerLink = By.cssSelector("a.ico-register");
    private final By loginLink    = By.cssSelector("a.ico-login");
    private final By logoutLink   = By.cssSelector("a.ico-logout");
    private final By cartLink     = By.cssSelector("a.ico-cart");

    // A known, stable simple product on the demo site (product id 1).
    private static final String SIMPLE_PRODUCT_URL =
            "https://demowebshop.tricentis.com/141-inch-laptop";

    public HomePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    /** Clicks the "Register" link in the header. */
    public void goToRegister() {
        wait.until(ExpectedConditions.elementToBeClickable(registerLink)).click();
    }

    /** Clicks the "Log in" link in the header. */
    public void goToLogin() {
        wait.until(ExpectedConditions.elementToBeClickable(loginLink)).click();
    }

    /** Clicks the "Log out" link in the header. */
    public void logout() {
        wait.until(ExpectedConditions.elementToBeClickable(logoutLink)).click();
    }

    /** True if the "Log out" link is visible, meaning a user is currently logged in. */
    public boolean isLogoutVisible() {
        List<WebElement> logoutElements = driver.findElements(logoutLink);
        return !logoutElements.isEmpty() && logoutElements.get(0).isDisplayed();
    }

    /** Navigates straight to a known simple product page (id 1: Build your own computer). */
    public void openSimpleProduct() {
        driver.get(SIMPLE_PRODUCT_URL);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.product-essential")));
    }

    /** Clicks the shopping cart link in the header to go to /cart. */
    public void goToCart() {
        wait.until(ExpectedConditions.elementToBeClickable(cartLink)).click();
    }

    /** Generic wait used after navigation/AJAX actions: waits for <body> to be present. */
    public void waitForPageLoad() {
        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
    }
}