package checkout;

import base.DriverManager; // اتأكد من ده
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
import java.util.Random;

public class TestRegisteredCheckout {
    WebDriver driver;
    WebDriverWait wait;

    @BeforeMethod
    public void setUp() {
        System.out.println("Starting Brave Browser...");
        // استخدام المانجر الموحد فقط
        this.driver = DriverManager.getDriver();
        this.wait = new WebDriverWait(this.driver, Duration.ofSeconds(15));
    }

    @Test
    public void testRegisteredUserCheckout() {
        // مش محتاج تعرف driver تاني جوه التست، استخدم الـ field الموجود
        int randomNum = new Random().nextInt(99000) + 1000;
        String randomEmail = "ahmed_tester_" + randomNum + "@example.com";
        String password = "Password123!";

        System.out.println("Registering a new account on the fly (" + randomEmail + ")...");
        driver.get("https://demowebshop.tricentis.com/"); // لازم تدخل على الموقع الأول
        wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Register"))).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("gender-male"))).click();

        driver.findElement(By.id("FirstName")).sendKeys("Ahmed");
        driver.findElement(By.id("LastName")).sendKeys("Karam");
        driver.findElement(By.id("Email")).sendKeys(randomEmail);
        driver.findElement(By.id("Password")).sendKeys(password);
        driver.findElement(By.id("ConfirmPassword")).sendKeys(password);
        driver.findElement(By.id("register-button")).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".result")));

        System.out.println("Adding Laptop to Cart...");
        WebElement searchBox = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("small-searchterms")));
        searchBox.sendKeys("14.1-inch Laptop");
        driver.findElement(By.cssSelector("input.search-box-button")).click();

        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("input.product-box-add-to-cart-button"))).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("bar-notification")));

        System.out.println("Starting Checkout...");
        driver.get("https://demowebshop.tricentis.com/cart");
        wait.until(ExpectedConditions.elementToBeClickable(By.id("termsofservice"))).click();
        driver.findElement(By.id("checkout")).click();

        // ... باقي خطوات الـ Checkout زي ما هي بالظبط
        System.out.println("Processing Address and Payment...");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("BillingNewAddress_CountryId"))).sendKeys("Egypt");
        driver.findElement(By.id("BillingNewAddress_City")).sendKeys("Cairo");
        driver.findElement(By.id("BillingNewAddress_Address1")).sendKeys("Maadi, St 9");
        driver.findElement(By.id("BillingNewAddress_ZipPostalCode")).sendKeys("11431");
        driver.findElement(By.id("BillingNewAddress_PhoneNumber")).sendKeys("01012345678");

        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("#billing-buttons-container .new-address-next-step-button"))).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("#shipping-buttons-container .new-address-next-step-button"))).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.id("shippingoption_1"))).click();
        driver.findElement(By.cssSelector("#shipping-method-buttons-container .shipping-method-next-step-button")).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.id("paymentmethod_0"))).click();
        driver.findElement(By.cssSelector("#payment-method-buttons-container .payment-method-next-step-button")).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("#payment-info-buttons-container .payment-info-next-step-button"))).click();

        System.out.println("Confirming Registered Order...");
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("#confirm-order-buttons-container .confirm-order-next-step-button"))).click();

        wait.until(ExpectedConditions.urlContains("completed"));

        String successMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".title > strong"))).getText();
        Assert.assertEquals(successMessage, "Your order has been successfully processed!", "❌ Order failed");

        String orderNum = driver.findElement(By.cssSelector("ul.details li")).getText();
        System.out.println("Registered Checkout Passed! " + orderNum);
    }

    @AfterMethod
    public void tearDown() {
        System.out.println("Closing Browser...");
        DriverManager.quitDriver(); // استخدم المانجر للقفل
    }
}