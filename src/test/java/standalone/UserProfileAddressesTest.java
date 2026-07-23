package standalone;

import base.DriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.List;

/**
 * User Profile & Addresses Test Suite
 */
public class UserProfileAddressesTest {

    private WebDriver driver;
    private WebDriverWait wait;
    private static final String BASE_URL = "https://demowebshop.tricentis.com/";

    private static final String TEST_EMAIL    = "test_user@example.com";
    private static final String TEST_PASSWORD = "Test@1234";

    @BeforeClass
    public void setUp() {
        this.driver = base.DriverManager.getDriver();
        this.wait = new WebDriverWait(this.driver, Duration.ofSeconds(15));
        login();
    }

    @AfterClass
    public void tearDown() {
        DriverManager.quitDriver();
    }

    private void login() {
        String uniqueEmail = "test_user_" + System.currentTimeMillis() + "@example.com";
        
        driver.get(BASE_URL + "register");
        
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("gender-male"))).click();
        driver.findElement(By.id("FirstName")).sendKeys("Test");
        driver.findElement(By.id("LastName")).sendKeys("User");
        driver.findElement(By.id("Email")).sendKeys(uniqueEmail);
        driver.findElement(By.id("Password")).sendKeys(TEST_PASSWORD);
        driver.findElement(By.id("ConfirmPassword")).sendKeys(TEST_PASSWORD);
        driver.findElement(By.id("register-button")).click();
        
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Log out")));
    }

    @Test(priority = 1, description = "PROF-AUTO-01: My Account page loads with profile fields")
    public void myAccountPageLoads() {
        driver.get(BASE_URL + "customer/info");

        WebElement firstNameField = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.id("FirstName"))
        );
        WebElement lastNameField  = driver.findElement(By.id("LastName"));
        WebElement emailField     = driver.findElement(By.id("Email"));

        Assert.assertTrue(firstNameField.isDisplayed(), "First Name field should be visible");
        Assert.assertTrue(lastNameField.isDisplayed(),  "Last Name field should be visible");
        Assert.assertTrue(emailField.isDisplayed(),     "Email field should be visible");
    }

    @Test(priority = 2, description = "PROF-AUTO-02: Update profile first name successfully")
    public void updateProfileFirstName() {
        driver.get(BASE_URL + "customer/info");

        WebElement firstNameField = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.id("FirstName"))
        );

        firstNameField.clear();
        firstNameField.sendKeys("UpdatedName");

        driver.findElement(By.cssSelector("input.save-customer-info-button")).click();

        // Wait for the page to reload by waiting for the field to be visible again
        // Note: Demo Web Shop doesn't always show a notification on profile update
        wait.until(ExpectedConditions.textToBePresentInElementValue(By.id("FirstName"), "UpdatedName"));

        WebElement updatedField = driver.findElement(By.id("FirstName"));
        Assert.assertEquals(updatedField.getAttribute("value"), "UpdatedName",
                "First name should be updated to 'UpdatedName'");
    }

    @Test(priority = 3, description = "PROF-AUTO-03: Addresses page loads with Add New Address option")
    public void addressesPageLoads() {
        driver.get(BASE_URL + "customer/addresses");

        WebElement pageTitle = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector("div.page-title h1")
                )
        );

        Assert.assertTrue(
                pageTitle.getText().toLowerCase().contains("address"),
                "Page title should contain 'address', got: " + pageTitle.getText()
        );

        WebElement addNewBtn = driver.findElement(
                By.cssSelector("input.add-address-button, a.add-address-button, button.add-address-button")
        );
        Assert.assertTrue(addNewBtn.isDisplayed(),
                "'Add new address' button should be visible on the Addresses page");
    }

    @Test(priority = 4, description = "PROF-AUTO-04: Add a new address successfully")
    public void addNewAddress() {
        driver.get(BASE_URL + "customer/addressadd");

        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.id("Address_FirstName"))).sendKeys("Test");

        driver.findElement(By.id("Address_LastName")).sendKeys("User");
        driver.findElement(By.id("Address_Email")).sendKeys(TEST_EMAIL);
        driver.findElement(By.id("Address_Company")).sendKeys("Test Co.");
        driver.findElement(By.id("Address_City")).sendKeys("Cairo");
        driver.findElement(By.id("Address_Address1")).sendKeys("123 Test Street");
        driver.findElement(By.id("Address_ZipPostalCode")).sendKeys("12345");
        driver.findElement(By.id("Address_PhoneNumber")).sendKeys("01000000000");

        Select countryDropdown = new Select(
                driver.findElement(By.id("Address_CountryId"))
        );
        countryDropdown.selectByVisibleText("Egypt");

        driver.findElement(By.cssSelector("input.save-address-button")).click();

        wait.until(ExpectedConditions.urlContains("customer/addresses"));

        List<WebElement> addressList = driver.findElements(
                By.cssSelector("div.address-list div.address-item")
        );

        Assert.assertTrue(addressList.size() > 0,
                "At least one address should appear in the list after adding");
    }

    @Test(priority = 5, description = "PROF-AUTO-05: Change password form shows error on mismatch")
    public void changePasswordMismatchShowsError() {
        driver.get(BASE_URL + "customer/changepassword");

        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.id("OldPassword"))).sendKeys(TEST_PASSWORD);

        driver.findElement(By.id("NewPassword")).sendKeys("NewPass@999");
        driver.findElement(By.id("ConfirmNewPassword")).sendKeys("DifferentPass@000");

        driver.findElement(By.cssSelector("input.change-password-button")).click();

        WebElement errorMessage = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector("div.validation-summary-errors, span.field-validation-error")
                )
        );

        Assert.assertTrue(errorMessage.isDisplayed(),
                "A validation error should appear when new passwords do not match");
    }
}