package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/** Represents the "Log in" page at /login. */
public class LoginPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By emailField  = By.id("Email");
    private final By passwordField = By.id("Password");
    private final By loginButton = By.cssSelector("input.login-button");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    /** Fills the email/password fields and clicks "Log in". */
    public void login(String email, String password) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(emailField)).sendKeys(email);
        driver.findElement(passwordField).sendKeys(password);
        wait.until(ExpectedConditions.elementToBeClickable(loginButton)).click();
    }
}