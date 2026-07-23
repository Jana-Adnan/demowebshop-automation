package checkout;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;

public class TestCheckoutValidation {
    WebDriver driver;
    WebDriverWait wait;

    @BeforeMethod
    public void setUp() {
        System.out.println("Starting Brave Browser for Negative Test...");
        this.driver = base.DriverManager.getDriver();
        this.wait = new WebDriverWait(this.driver, Duration.ofSeconds(10));
    }

    @Test
    public void testEmptyBillingFieldsValidation() {
        WebDriver driver = this.driver;
        WebDriverWait wait = this.wait;

        System.out.println("Adding an item to the cart...");
        driver.get("https://demowebshop.tricentis.com/computing-and-internet");
        wait.until(ExpectedConditions.elementToBeClickable(By.id("add-to-cart-button-13"))).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("p.content")));

        System.out.println("Proceeding to Checkout...");
        driver.get("https://demowebshop.tricentis.com/cart");
        wait.until(ExpectedConditions.elementToBeClickable(By.id("termsofservice"))).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.id("checkout"))).click();

        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("input.checkout-as-guest-button"))).click();

        System.out.println("Leaving mandatory fields empty and clicking Continue...");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("BillingNewAddress_FirstName"))).sendKeys("Ahmed");
        driver.findElement(By.id("BillingNewAddress_LastName")).sendKeys("Karam");
        driver.findElement(By.id("BillingNewAddress_Email")).sendKeys("ahmed.karam@example.com");
        driver.findElement(By.id("BillingNewAddress_CountryId")).sendKeys("Egypt");

        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#BillingNewAddress_StateProvinceId option")));

        WebElement continueBtn = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("#billing-buttons-container .new-address-next-step-button")));
        continueBtn.click();

        System.out.println("Verifying Error Messages...");
        String cityError = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[contains(text(), 'City is required')]"))).getText();
        String phoneError = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[contains(text(), 'Phone is required')]"))).getText();

        Assert.assertEquals(cityError, "City is required", "City error message not found!");
        Assert.assertEquals(phoneError, "Phone is required", "Phone error message not found!");

        System.out.println("Negative Test Passed! Validation messages appeared successfully.");
    }

    @AfterMethod
    public void tearDown() {
        System.out.println("Closing Browser...");
        base.DriverManager.quitDriver();
    }
}
