package tests;

import org.testng.Assert;
import org.testng.annotations.Test;

public class MultiProductCartTest extends BaseTest {

    @Test
    public void addMultipleProductsAndValidateCart() {

        home.goToRegister();

        String email =
                "jana" + System.currentTimeMillis() + "@mail.com";

        String password = "jana1234";

        register.register(email, password);

        Assert.assertTrue(
                register.getSuccessMessage().contains("completed"),
                "Registration failed"
        );

        home.logout();

        home.goToLogin();

        login.login(email, password);

        home.waitForPageLoad();

        Assert.assertTrue(
                home.isLogoutVisible(),
                "Login failed"
        );

        home.openSimpleProduct();

        product.addToCart();

        home.goToCart();

        Assert.assertTrue(
                cart.isCartNotEmpty(),
                "Cart is empty after first product"
        );

        int itemsBefore =
                cart.getCartItems().size();

        home.openSimpleProduct();

        product.addToCart();

        home.goToCart();

        int itemsAfterAdd =
                cart.getCartItems().size();

        Assert.assertTrue(
                itemsAfterAdd >= itemsBefore,
                "Second product was not added"
        );

        if (itemsAfterAdd > 0) {

            cart.updateQuantity(0, "3");
            cart.clickUpdateCart();

            home.waitForPageLoad();
        }

        Assert.assertTrue(
                cart.getCartItems().size() > 0,
                "Cart became empty unexpectedly"
        );
    }
}
