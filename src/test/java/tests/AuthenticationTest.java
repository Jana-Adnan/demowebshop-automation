package tests;

import org.testng.Assert;
import org.testng.annotations.Test;

public class AuthenticationTest extends BaseTest {

    @Test
    public void registerLoginAndAddToCart() {

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
    }
}
