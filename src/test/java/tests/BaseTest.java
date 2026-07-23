package tests;

import base.DriverManager;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import pages.CartPage;
import pages.HomePage;
import pages.LoginPage;
import pages.ProductPage;
import pages.RegisterPage;

public class BaseTest {

    protected HomePage home;
    protected RegisterPage register;
    protected LoginPage login;
    protected ProductPage product;
    protected CartPage cart;

    @BeforeMethod
    public void setup() {
        DriverManager.getDriver().get("https://demowebshop.tricentis.com/");

        home = new HomePage(DriverManager.getDriver());
        register = new RegisterPage(DriverManager.getDriver());
        login = new LoginPage(DriverManager.getDriver());
        product = new ProductPage(DriverManager.getDriver());
        cart = new CartPage(DriverManager.getDriver());
    }

    @AfterMethod
    public void tearDown() {
        DriverManager.quitDriver();
    }
}
