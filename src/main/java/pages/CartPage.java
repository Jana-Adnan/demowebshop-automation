package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/** Represents the shopping cart page at /cart. */
public class CartPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By cartRows       = By.cssSelector("table.cart tbody tr");
    private final By updateCartBtn  = By.cssSelector("input[name='updatecart']");

    public CartPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    /** True if at least one product row exists in the cart table. */
    public boolean isCartNotEmpty() {
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(cartRows));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    /** Returns every product row currently in the cart table. */
    public List<WebElement> getCartItems() {
        return driver.findElements(cartRows);
    }

    /** Sets the quantity input for the row at the given index (0-based). */
    public void updateQuantity(int rowIndex, String quantity) {
        WebElement row = getCartItems().get(rowIndex);
        WebElement qtyInput = row.findElement(By.cssSelector("input.qty-input"));
        qtyInput.clear();
        qtyInput.sendKeys(quantity);
    }

    /** Checks the "Remove" checkbox for the row at the given index (0-based). */
    public void removeItem(int rowIndex) {
        WebElement row = getCartItems().get(rowIndex);
        WebElement removeCheckbox = row.findElement(By.cssSelector("input[name='removefromcart']"));
        if (!removeCheckbox.isSelected()) {
            removeCheckbox.click();
        }
    }
    /** Clicks "Update cart", applying any quantity changes / removals made above. */
    public void clickUpdateCart() {
        WebElement updateBtn = wait.until(ExpectedConditions.elementToBeClickable(updateCartBtn));
        updateBtn.click();
        wait.until(ExpectedConditions.stalenessOf(updateBtn));
    }
}