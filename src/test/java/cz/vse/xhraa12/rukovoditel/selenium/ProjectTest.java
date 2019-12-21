package cz.vse.xhraa12.rukovoditel.selenium;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ProjectTest {
    private ChromeDriver driver;
    private LoginTest auth;

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
        auth = new LoginTest();
        auth.setDriver(driver);
    }

    @After
    public void tearDown() {
        driver.close();
    }

    @Test
    public void invalidProjectTest() {
        // given
        driver.manage().window().maximize();
        auth.login(auth.username, auth.password);
        // when
        Util.waitFor(driver, By.cssSelector("body > div.page-container > div.page-sidebar-wrapper.noprint > div > div > ul > li:nth-child(4) > a > i"))
            .click();
        Util.waitFor(driver, By.cssSelector("body > div.page-container > div.page-content-wrapper > div > div > div.row > div > div:nth-child(7) > div:nth-child(1) > div > button"))
            .click();
        new Select(Util.waitFor(driver, By.cssSelector("#fields_156")))
            .selectByVisibleText("Urgent");
        new Select(Util.waitFor(driver, By.cssSelector("#fields_157")))
            .selectByVisibleText("New");
        Util.waitFor(driver, By.cssSelector("#items_form > div.modal-footer > button.btn.btn-primary.btn-primary-modal-action"))
            .click();
        // then
        Assert.assertEquals(
            Util.waitFor(driver, By.cssSelector("#fields_158-error"))
                .getText(),
            "This field is required!"
        );
    }
    @Test
    public void validProjectTest() {
        // given
        driver.manage().window().maximize();
        auth.login(auth.username, auth.password);
        // when
        By menuProjects = By.cssSelector("body > div.page-container > div.page-sidebar-wrapper.noprint > div > div > ul > li:nth-child(4) > a > i");
        String projectName = "xhraa12-test1";
        // Navigate to Projects from the menu
        Util.waitFor(driver, menuProjects)
            .click();
        // Click create
        // Handle modal dialogue form
        Util.waitFor(driver, By.cssSelector("body > div.page-container > div.page-content-wrapper > div > div > div.row > div > div:nth-child(7) > div:nth-child(1) > div > button"))
            .click();
        // Fill in the data
        new Select(Util.waitFor(driver, By.cssSelector("#fields_156")))
            .selectByVisibleText("High");
        new Select(Util.waitFor(driver, By.cssSelector("#fields_157")))
            .selectByVisibleText("New");
        Util.waitFor(driver, By.cssSelector("#fields_158"))
            .sendKeys(projectName);
        Date now = new Date();
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
        Util.waitFor(driver, By.cssSelector("#fields_159"))
            .sendKeys(sd.format(now));
        // Submit the form
        Util.waitFor(driver, By.cssSelector("#items_form > div.modal-footer > button.btn.btn-primary.btn-primary-modal-action"))
            .click();
        Util.waitFor(driver, By.cssSelector("body > div.page-container > div.page-content-wrapper > div > div > div.row > div > div:nth-child(2) > div > h3"));
        // Navigate back to the Projects list from the menu
        Util.waitFor(driver, menuProjects)
            .click();
        // Click 🗑️ from the list
        String selector = String.format("//*[text()=\"%s\"]/../..//*[@title=\"Delete\"]", projectName);
        Util.waitFor(driver, By.xpath(selector))
            .click();
        // Handle modal confirmation dialogue
        // Confirm deletion
        Util.waitFor(driver, By.cssSelector("#delete_item_form > div.modal-body > div.single-checkbox > label"))
            .click();
        // Delete
        Util.waitFor(driver, By.cssSelector("#delete_item_form > div.modal-footer > button.btn.btn-primary.btn-primary-modal-action"))
            .click();
        // then
        // Back on the Projects list
        String headerText = Util.waitFor(driver, By.cssSelector("body > div.page-container > div.page-content-wrapper > div > div > div.row > div > div:nth-child(2) > div > h3"))
            .getText();
        Assert.assertEquals(headerText, "Projects");
        // My project is not found
        List<WebElement> myProjects = driver.findElements(By.xpath(selector));
        Assert.assertEquals(myProjects.size(), 0);
    }
}
