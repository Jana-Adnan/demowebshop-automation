package tests;

import org.testng.Assert;
import org.testng.annotations.Test;

public class CartOperationsTest extends BaseTest {

    @Test
    public void fullCartOperationsFlow() {

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
                "Cart is empty"
        );

        int itemsBefore = cart.getCartItems().size();

        Assert.assertTrue(
                itemsBefore > 0,
                "No products found in cart"
        );

        cart.updateQuantity(0, "3");
        cart.clickUpdateCart();

        home.waitForPageLoad();

        cart.removeItem(0);
        cart.clickUpdateCart();

        home.waitForPageLoad();

        int itemsAfter = cart.getCartItems().size();

        Assert.assertEquals(
                itemsAfter,
                itemsBefore - 1,
                "Item was not removed from cart"
        );
    }
}
