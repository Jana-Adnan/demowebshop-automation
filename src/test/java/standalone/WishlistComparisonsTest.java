package standalone;

import base.DriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.List;

/**
 * Wishlist & Comparisons Test Suite
 */
public class WishlistComparisonsTest {

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

    private void goToWishlistableProduct() {
        driver.get(BASE_URL + "black-white-diamond-heart");
    }

    @Test(priority = 1, description = "WSH-AUTO-01: Add product to wishlist while logged in")
    public void addProductToWishlist() {
        goToWishlistableProduct();

        WebElement addToWishlistBtn = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.cssSelector("input[id^='add-to-wishlist-button-']")
                )
        );
        addToWishlistBtn.click();

        WebElement notification = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector("div.bar-notification.success p.content")
                )
        );
        Assert.assertTrue(
                notification.getText().contains("wishlist"),
                "Success notification should mention wishlist, got: " + notification.getText()
        );
    }

    @Test(priority = 2, description = "WSH-AUTO-02: Product appears on wishlist page after adding",
            dependsOnMethods = "addProductToWishlist")
    public void wishlistPageShowsAddedProduct() {
        driver.get(BASE_URL + "wishlist");

        List<WebElement> wishlistItems = wait.until(
                ExpectedConditions.presenceOfAllElementsLocatedBy(
                        By.cssSelector("table.cart tbody tr")
                )
        );

        Assert.assertTrue(
                wishlistItems.size() > 0,
                "Wishlist page should contain at least one product"
        );
    }

    @Test(priority = 3, description = "WSH-AUTO-03: Add two products to comparison list")
    public void addProductsToComparison() {
        // First product
        driver.get(BASE_URL + "build-your-cheap-own-computer");
        wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector("input.button-2.add-to-compare-list-button")
        )).click();
        
        // Wait for redirect to compare products page
        wait.until(ExpectedConditions.urlContains("compareproducts"));

        // Second product
        driver.get(BASE_URL + "build-your-own-computer");
        wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector("input.button-2.add-to-compare-list-button")
        )).click();
        
        // Wait for redirect to compare products page
        wait.until(ExpectedConditions.urlContains("compareproducts"));

        driver.get(BASE_URL + "compareproducts");

        List<WebElement> comparedProducts = wait.until(
                ExpectedConditions.presenceOfAllElementsLocatedBy(
                        By.cssSelector("table.compare-products-table tr.product-name td a")
                )
        );

        Assert.assertTrue(
                comparedProducts.size() >= 2,
                "Comparison page should show at least 2 products, found: " + comparedProducts.size()
        );
    }

    @Test(priority = 4, description = "WSH-AUTO-04: Remove product from wishlist",
            dependsOnMethods = "wishlistPageShowsAddedProduct")
    public void removeProductFromWishlist() {
        driver.get(BASE_URL + "wishlist");

        // التعديل: استخدام اسم السليكتور الصحيح
        WebElement removeCheckbox = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.cssSelector("input[name='removefromcart']")
                )
        );
        removeCheckbox.click();

        driver.findElement(
                By.cssSelector("input.button-2.update-wishlist-button")
        ).click();

        wait.until(ExpectedConditions.presenceOfElementLocated(
                By.cssSelector("div.wishlist-content")
        ));

        List<WebElement> remainingItems = driver.findElements(
                By.cssSelector("table.cart tbody tr")
        );

        Assert.assertEquals(remainingItems.size(), 0,
                "Wishlist should be empty after removing the product");
    }

    @Test(priority = 5, description = "WSH-AUTO-05: Clear product comparison list",
            dependsOnMethods = "addProductsToComparison")
    public void clearComparisonList() {
        driver.get(BASE_URL + "compareproducts");

        WebElement clearButton = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.cssSelector("a.clear-list")
                )
        );
        clearButton.click();

        WebElement emptyMessage = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector("div.page-body")
                )
        );

        Assert.assertTrue(
                emptyMessage.getText().toLowerCase().contains("no items") ||
                        emptyMessage.getText().toLowerCase().contains("empty"),
                "Compare page should show empty state after clearing"
        );
    }
}