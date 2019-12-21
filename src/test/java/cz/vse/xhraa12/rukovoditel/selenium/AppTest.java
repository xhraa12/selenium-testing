package cz.vse.xhraa12.rukovoditel.selenium;

import org.junit.*;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.UUID;

/**
 * Unit test for simple App.
 */
public class AppTest {
    private ChromeDriver driver;

    @Before
    public void init() {
        ChromeOptions cho = new ChromeOptions();

        boolean runOnTravis = false;
        if (runOnTravis) {
            cho.addArguments("headless");
        } else {
            System.setProperty("webdriver.chrome.driver", "src/test/resources/drivers/chromedriver.exe");
        }
//        ChromeDriverService service = new ChromeDriverService()
        driver = new ChromeDriver(cho);
//        driver.manage().window().maximize();
    }

    @After
    public void tearDown() {
//        driver.close();
    }


    @Ignore
    @Test
    public void alzaTest() throws InterruptedException {
        driver.get("https://www.alza.cz/");
        WebElement searchInput = driver.findElement(By.cssSelector("#edtSearch"));
        searchInput.sendKeys("ubiquiti unifi");

        WebDriverWait wait = new WebDriverWait(driver, 1);
        wait.until(ExpectedConditions.presenceOfElementLocated( By.cssSelector("#ui-id-1>li.t7") ));

        List<WebElement> searchItems = driver.findElements(By.cssSelector("#ui-id-1>li.t3"));
        Assert.assertEquals(3, searchItems.size());
        driver.quit();
    }
}
