package standalone;

import base.DriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.Duration;

/**
 * Categories & Navigation Test Suite
 */
public class CategoriesNavigationTest {

    private WebDriver driver;
    private WebDriverWait wait;
    private static final String BASE_URL = "https://demowebshop.tricentis.com/";

    @BeforeClass
    public void setUp() {
        // استخدام المانجر الموحد
        this.driver = base.DriverManager.getDriver();
        this.driver.manage().window().maximize();
        this.wait = new WebDriverWait(this.driver, Duration.ofSeconds(15));
    }

    @AfterClass
    public void tearDown() {
        // استخدام ميثود الـ quit الموحدة
        DriverManager.quitDriver();
    }

    private void hoverAndClick(String hoverLinkText, String clickLinkText) {
        Actions actions = new Actions(driver);

        WebElement hoverTarget = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//ul[contains(@class, 'top-menu')]/li/a[contains(text(), '" + hoverLinkText + "')]")
                )
        );

        // التعديل: إضافة pause لضمان ظهور القائمة المنسدلة قبل الضغط
        actions.moveToElement(hoverTarget)
                .pause(Duration.ofMillis(500))
                .perform();

        WebElement clickTarget = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.xpath("//ul[contains(@class, 'top-menu')]//a[contains(text(), '" + clickLinkText + "')]")
                )
        );
        clickTarget.click();
    }

    @Test(priority = 1, description = "NAV-AUTO-01: Navigate to Computers → Desktops sub-category")
    public void navigateToDesktopsSubCategory() {
        driver.get(BASE_URL);

        hoverAndClick("Computers", "Desktops");

        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(
                currentUrl.contains("desktops"),
                "URL should contain 'desktops' but was: " + currentUrl
        );

        WebElement heading = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector("div.page-title h1")
                )
        );
        Assert.assertEquals(heading.getText().trim(), "Desktops",
                "Page heading should be 'Desktops'");
    }

    @Test(priority = 2, description = "TC-NAV-03: Navigate to Computers → Notebooks sub-category")
    public void navigateToNotebooksSubCategory() {
        driver.get(BASE_URL);

        hoverAndClick("Computers", "Notebooks");

        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(
                currentUrl.contains("notebooks"),
                "URL should contain 'notebooks' but was: " + currentUrl
        );

        WebElement heading = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector("div.page-title h1")
                )
        );
        Assert.assertEquals(heading.getText().trim(), "Notebooks",
                "Page heading should be 'Notebooks'");
    }

    @Test(priority = 3, description = "NAV-AUTO-02: Navigate to Electronics category from top nav")
    public void navigateToElectronicsCategory() {
        driver.get(BASE_URL);

        WebElement electronicsLink = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.xpath("//ul[contains(@class, 'top-menu')]/li/a[contains(text(), 'Electronics')]")
                )
        );
        electronicsLink.click();

        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(
                currentUrl.contains("electronics"),
                "URL should contain 'electronics' but was: " + currentUrl
        );

        WebElement heading = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector("div.page-title h1")
                )
        );
        Assert.assertEquals(heading.getText().trim(), "Electronics",
                "Page heading should be 'Electronics'");
    }

    @Test(priority = 4, description = "TC-NAV-05: Navigate to Apparel & Shoes category")
    public void navigateToApparelCategory() {
        driver.get(BASE_URL);

        WebElement apparelLink = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.xpath("//ul[contains(@class, 'top-menu')]/li/a[contains(text(), 'Apparel')]")
                )
        );
        apparelLink.click();

        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(
                currentUrl.contains("apparel"),
                "URL should contain 'apparel' but was: " + currentUrl
        );

        WebElement heading = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector("div.page-title h1")
                )
        );
        Assert.assertFalse(heading.getText().isEmpty(),
                "Category heading should not be empty");
    }

    @Test(priority = 5, description = "NAV-AUTO-03: Breadcrumb navigation back to Home from Desktops")
    public void breadcrumbNavigationBackToHome() {
        driver.get(BASE_URL);

        hoverAndClick("Computers", "Desktops");

        wait.until(ExpectedConditions.urlContains("desktops"));

        WebElement homeBreadcrumb = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.cssSelector("div.breadcrumb a[href='/']")
                )
        );
        homeBreadcrumb.click();

        wait.until(ExpectedConditions.urlToBe(BASE_URL));
        Assert.assertEquals(driver.getCurrentUrl(), BASE_URL,
                "Clicking Home breadcrumb should redirect to homepage");
    }

    @Test(priority = 6, description = "TC-NAV-08: Desktops category page shows at least one product")
    public void categoryPageShowsProducts() {
        driver.get(BASE_URL);

        hoverAndClick("Computers", "Desktops");

        wait.until(ExpectedConditions.presenceOfElementLocated(
                By.cssSelector("div.product-item")
        ));

        java.util.List<WebElement> products = driver.findElements(
                By.cssSelector("div.product-item")
        );

        Assert.assertTrue(products.size() > 0,
                "Desktops category should display at least one product, found: " + products.size());
    }

    @Test(priority = 7, description = "NAV-AUTO-04: Logo click returns to homepage")
    public void logoClickReturnsToHomepage() {
        driver.get(BASE_URL);

        hoverAndClick("Computers", "Desktops");
        wait.until(ExpectedConditions.urlContains("desktops"));

        WebElement logo = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.cssSelector("div.header-logo a")
                )
        );
        logo.click();

        wait.until(ExpectedConditions.urlToBe(BASE_URL));
        Assert.assertEquals(driver.getCurrentUrl(), BASE_URL,
                "Logo click should redirect to homepage");
    }

    @Test(priority = 8, description = "TC-NAV-02: Browser tab title updates when navigating to Desktops")
    public void browserTitleUpdatesOnCategoryNavigation() {
        driver.get(BASE_URL);

        hoverAndClick("Computers", "Desktops");

        wait.until(ExpectedConditions.titleContains("Desktops"));
        Assert.assertTrue(
                driver.getTitle().contains("Desktops"),
                "Browser tab title should contain 'Desktops', actual: " + driver.getTitle()
        );
    }
}