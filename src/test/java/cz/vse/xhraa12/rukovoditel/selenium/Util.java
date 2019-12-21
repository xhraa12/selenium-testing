package cz.vse.xhraa12.rukovoditel.selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Util {
    static WebElement waitFor(WebDriver driver, By by) {
        return new WebDriverWait(driver, 2)
            .until(ExpectedConditions.presenceOfElementLocated(by));
    }
}
