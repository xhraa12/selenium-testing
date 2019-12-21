package cz.vse.xhraa12.rukovoditel.selenium;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginTest {
    private ChromeDriver driver;

    public final String username = "rukovoditel";
    public final String password = "vse456ru";

    @Before
    public void init() {
        ChromeOptions cho = new ChromeOptions();

        boolean runOnTravis = false;
        if (runOnTravis) {
            cho.addArguments("headless");
        } else {
            System.setProperty("webdriver.chrome.driver", "src/test/resources/drivers/chromedriver.exe");
        }
        driver = new ChromeDriver(cho);
    }
    public void setDriver(ChromeDriver driver) {
        this.driver = driver;
    }

    @After
    public void tearDown() {
        driver.close();
    }

    public void login(String username, String password) {
        driver.get("https://digitalnizena.cz/rukovoditel/index.php?module=users/login");
        WebElement inputUsername = driver.findElement(By.cssSelector("#login_form > div:nth-child(2) > div > input"));
        WebElement inputPassword = driver.findElement(By.cssSelector("#login_form > div:nth-child(3) > div > input"));
        WebElement buttonLogin = driver.findElement(By.cssSelector("#login_form > div.form-actions > button"));

        inputUsername.sendKeys(username);
        inputPassword.sendKeys(password);
        buttonLogin.click();
        WebDriverWait wait = new WebDriverWait(driver, 1);
        wait.until(ExpectedConditions.or(
            ExpectedConditions.presenceOfElementLocated( By.cssSelector("body > div.header.navbar.navbar-inverse.navbar-fixed-top.noprint > div > ul > li.dropdown.user > a > span")),
            ExpectedConditions.presenceOfElementLocated( By.cssSelector("body > div.content.content-login > div") )
        ));
    }

    @Test
    public void validLoginTest() {
        // given
        // when
        login(username, password);
        // then
        driver.findElementByCssSelector("body > div.header.navbar.navbar-inverse.navbar-fixed-top.noprint > div > ul > li.dropdown.user > a > span");
    }

    @Test
    public void invalidLoginTest() {
        // given
        // when
        login("hovadina", "bejkarna");
        // then
        driver.findElementByCssSelector("body > div.content.content-login > div");
    }

    @Test
    public void logoutTest() {
        // given
        login(username, password);
        // when
        WebElement profileDropdown = driver.findElement(By.cssSelector("body > div.header.navbar.navbar-inverse.navbar-fixed-top.noprint > div > ul > li.dropdown.user > a"));
        WebElement logoffLink = driver.findElement(By.cssSelector("body > div.header.navbar.navbar-inverse.navbar-fixed-top.noprint > div > ul > li.dropdown.user > ul > li:nth-child(5) > a"));
        profileDropdown.click();
        logoffLink.click();
        // then
        WebDriverWait wait = new WebDriverWait(driver, 1);
        wait.until(ExpectedConditions.presenceOfElementLocated( By.cssSelector("body > div.content.content-login > h3") ));
    }
}
