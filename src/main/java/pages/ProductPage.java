package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/** Represents a single product detail page. */
public class ProductPage {

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final By addToCartButton = By.cssSelector("input.add-to-cart-button");
    private final By successBar      = By.cssSelector("div.bar-notification.success");

    public ProductPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    /** Clicks "Add to cart" and waits for the green success notification bar. */
    public void addToCart() {
        wait.until(ExpectedConditions.elementToBeClickable(addToCartButton)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(successBar));
    }
}