package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/** Represents the "Register" page at /register. */
public class RegisterPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By genderMale     = By.id("gender-male");
    private final By firstName      = By.id("FirstName");
    private final By lastName       = By.id("LastName");
    private final By email          = By.id("Email");
    private final By password       = By.id("Password");
    private final By confirmPassword = By.id("ConfirmPassword");
    private final By registerButton = By.id("register-button");
    private final By resultMessage  = By.cssSelector("div.result");

    public RegisterPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    /**
     * Fills and submits the registration form with a generic First/Last name
     * (male, default) and the given email/password. Used by AuthenticationTest,
     * CartOperationsTest and MultiProductCartTest, which only vary the email.
     */
    public void register(String emailAddress, String pwd) {
        wait.until(ExpectedConditions.elementToBeClickable(genderMale)).click();

        driver.findElement(firstName).sendKeys("Test");
        driver.findElement(lastName).sendKeys("User");
        driver.findElement(email).sendKeys(emailAddress);
        driver.findElement(password).sendKeys(pwd);
        driver.findElement(confirmPassword).sendKeys(pwd);
        wait.until(ExpectedConditions.elementToBeClickable(registerButton)).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(resultMessage));
    }
    /** Returns the "Your registration completed" success text shown after registering. */
    public String getSuccessMessage() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(resultMessage)).getText();
    }
}