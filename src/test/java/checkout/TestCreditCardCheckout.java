package checkout;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;

public class TestCreditCardCheckout {
    WebDriver driver;
    WebDriverWait wait;

    @BeforeMethod
    public void setUp() {
        System.out.println("Starting Brave Browser for Credit Card Test...");
        this.driver = base.DriverManager.getDriver();
        this.wait = new WebDriverWait(this.driver, Duration.ofSeconds(10));
    }

    @Test
    public void testCreditCardCheckout() {
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

        System.out.println("Filling Billing Address...");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("BillingNewAddress_FirstName"))).sendKeys("Ahmed");
        driver.findElement(By.id("BillingNewAddress_LastName")).sendKeys("Karam");
        driver.findElement(By.id("BillingNewAddress_Email")).sendKeys("ahmed.karam.cc@example.com");
        driver.findElement(By.id("BillingNewAddress_CountryId")).sendKeys("Egypt");

        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#BillingNewAddress_StateProvinceId option")));

        driver.findElement(By.id("BillingNewAddress_City")).sendKeys("Cairo");
        driver.findElement(By.id("BillingNewAddress_Address1")).sendKeys("Maadi, St 9");
        driver.findElement(By.id("BillingNewAddress_ZipPostalCode")).sendKeys("11431");
        driver.findElement(By.id("BillingNewAddress_PhoneNumber")).sendKeys("01012345678");
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("#billing-buttons-container .new-address-next-step-button"))).click();

        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("#shipping-buttons-container .new-address-next-step-button"))).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("#shipping-method-buttons-container .shipping-method-next-step-button"))).click();

        System.out.println("Selecting Credit Card Payment Method...");
        wait.until(ExpectedConditions.elementToBeClickable(By.id("paymentmethod_2"))).click();
        driver.findElement(By.cssSelector("#payment-method-buttons-container .payment-method-next-step-button")).click();

        System.out.println("Entering Credit Card Details...");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("CardholderName"))).sendKeys("Ahmed Karam");
        driver.findElement(By.id("CardNumber")).sendKeys("4111111111111111");

        new Select(driver.findElement(By.id("ExpireMonth"))).selectByVisibleText("05");
        new Select(driver.findElement(By.id("ExpireYear"))).selectByVisibleText("2026");

        driver.findElement(By.id("CardCode")).sendKeys("123");
        driver.findElement(By.cssSelector("#payment-info-buttons-container .payment-info-next-step-button")).click();

        System.out.println("Confirming Order...");
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("#confirm-order-buttons-container .confirm-order-next-step-button"))).click();

        wait.until(ExpectedConditions.urlContains("completed"));
        String successMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".title > strong"))).getText();
        Assert.assertEquals(successMessage, "Your order has been successfully processed!", "❌ Credit Card Order failed!");

        String orderNumber = driver.findElement(By.cssSelector("ul.details li")).getText();
        System.out.println("Credit Card Test Passed! " + orderNumber);
    }

    @AfterMethod
    public void tearDown() {
        System.out.println("Closing Browser...");
        base.DriverManager.quitDriver();
    }
}
