package cz.vse.xhraa12.rukovoditel.selenium;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.Select;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class TasksTest {
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
    public void deleteTaskTest() {
        // given
        driver.manage().window().maximize();
        auth.login(auth.username, auth.password);
        // when
        // Navigate to Projects list
        By menuProjects = By.cssSelector("body > div.page-container > div.page-sidebar-wrapper.noprint > div > div > ul > li:nth-child(4) > a > i");
        Util.waitFor(driver, menuProjects)
            .click();
        // Find my project and click through on to detail
        String projectName = "xhraa12";
        String taskName = "xhraa12";
        String selector = String.format("//*[text()=\"%s\"]", projectName);
        Util.waitFor(driver, By.xpath(selector))
            .click();
        // Click Add task
        Util.waitFor(driver, By.cssSelector("body > div.page-container > div.page-content-wrapper > div > div > div.row > div > div:nth-child(7) > div:nth-child(1) > div > button"))
            .click();
        // Fil in the form
        String taskType = "Task";
        String taskStatus = "New";
        String taskPriority = "Medium";
        String taskDescription = "ABC";
        Util.waitFor(driver, By.cssSelector("#fields_168"))
            .sendKeys(taskName);
        new Select(Util.waitFor(driver, By.cssSelector("#fields_167")))
            .selectByVisibleText(taskType);
        new Select(Util.waitFor(driver, By.cssSelector("#fields_169")))
            .selectByVisibleText(taskStatus);
        new Select(Util.waitFor(driver, By.cssSelector("#fields_170")))
            .selectByVisibleText(taskPriority);
        // To edit description, we need to switch to iframe and back
        driver.switchTo().frame(driver.findElement(By.cssSelector("#cke_1_contents > iframe")));
        driver.findElement(By.cssSelector("body"))
            .sendKeys(taskDescription);
        driver.switchTo().defaultContent();
        // Submit the form
        Util.waitFor(driver, By.cssSelector("#items_form > div.modal-footer > button.btn.btn-primary.btn-primary-modal-action"))
            .click();
        // Select Info i
        Util.waitFor(driver, By.xpath(String.format("//*[text()=\"%s\"]/../..//*[@title=\"Info\"]", taskName)))
            .click();
        // Check created info table data
        Assert.assertEquals(
            Util.waitFor(driver, By.cssSelector("body > div.page-container > div.page-content-wrapper > div > div > div.row > div > div:nth-child(3) > div.col-md-4 > div > div > div:nth-child(2) > table > tbody > tr.form-group-167 > td > div"))
                .getText(),
            taskType);
        Assert.assertEquals(
            Util.waitFor(driver, By.cssSelector("body > div.page-container > div.page-content-wrapper > div > div > div.row > div > div:nth-child(3) > div.col-md-4 > div > div > div:nth-child(2) > table > tbody > tr.form-group-169 > td > div"))
                .getText(),
            taskStatus);
        Assert.assertEquals(
            Util.waitFor(driver, By.cssSelector("body > div.page-container > div.page-content-wrapper > div > div > div.row > div > div:nth-child(3) > div.col-md-4 > div > div > div:nth-child(2) > table > tbody > tr.form-group-170 > td > div"))
                .getText(),
            taskPriority);
        Assert.assertEquals(
            Util.waitFor(driver, By.cssSelector("body > div.page-container > div.page-content-wrapper > div > div > div.row > div > div:nth-child(3) > div.col-md-8.project-info > div.portlet.portlet-item-description > div.portlet-title > div.caption"))
                .getText(),
            taskName);
        Assert.assertEquals(
            Util.waitFor(driver, By.cssSelector("body > div.page-container > div.page-content-wrapper > div > div > div.row > div > div:nth-child(3) > div.col-md-8.project-info > div.portlet.portlet-item-description > div.portlet-body > div.item-content-box.ckeditor-images-content-prepare > div > div.content_box_content.fieldtype_textarea_wysiwyg"))
                .getText(),
            taskDescription);
        // Select Delete from More actions
        Util.waitFor(driver, By.cssSelector("body > div.page-container > div.page-content-wrapper > div > div > div.row > div > div:nth-child(3) > div.col-md-8.project-info > div.portlet.portlet-item-description > div.portlet-body > div.prolet-body-actions > ul > li:nth-child(2) > div > button"))
            .click();
        Util.waitFor(driver, By.cssSelector("body > div.page-container > div.page-content-wrapper > div > div > div.row > div > div:nth-child(3) > div.col-md-8.project-info > div.portlet.portlet-item-description > div.portlet-body > div.prolet-body-actions > ul > li:nth-child(2) > div > ul > li:nth-child(2) > a"))
            .click();
        // Delete
        Util.waitFor(driver, By.cssSelector("#delete_item_form > div.modal-footer > button.btn.btn-primary.btn-primary-modal-action"))
            .click();
        // then
        // Task is deleted and we are back on the list
        Assert.assertEquals(
            Util.waitFor(driver, By.cssSelector("body > div.page-container > div.page-content-wrapper > div > div > div.row > div > div:nth-child(2) > div > h3"))
                .getText(),
            "Tasks"
        );
    }
}
