package cz.vse.xhraa12.rukovoditel.selenium;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.Select;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

class Task {
    public String type;
    public String name;
    public String status;
    public String priority;
    public String description;

    public Task(String type, String name, String status, String priority, String description) {
        this.type = type;
        this.name = name;
        this.status = status;
        this.priority = priority;
        this.description = description;
    }
}

public class TasksTest {
    private ChromeDriver driver;
    private LoginTest auth;
    public final String projectName = "xhraa12";

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

    public void navigateToMyProjectPage() {
        // Navigate to Projects list
        By menuProjects = By.cssSelector("body > div.page-container > div.page-sidebar-wrapper.noprint > div > div > ul > li:nth-child(4) > a > i");
        Util.waitFor(driver, menuProjects)
            .click();

        // Find my project and click through on to detail
        String projectName = this.projectName;
        // Wait for title

        Util.waitFor(driver, By.cssSelector("body > div.page-container > div.page-content-wrapper > div > div > div.row > div > div:nth-child(2) > div > h3"));
        // Check if for filter reset
        By resetFilterSelector = By.cssSelector(".reset_search");
        boolean projectFilterApplied = Util.waitFor(driver, By.cssSelector("body"))
            .findElements(resetFilterSelector)
            .size() > 0;
        if (projectFilterApplied) {
            Util.waitFor(driver, resetFilterSelector)
                .click();
        }
        Util.waitFor(driver, By.xpath(String.format("//*[text()=\"%s\"]", projectName)))
            .click();
    }
    public void createTask(Task task) {
        Util.waitFor(driver, By.cssSelector("#fields_168"))
            .sendKeys(task.name);
        new Select(Util.waitFor(driver, By.cssSelector("#fields_167")))
            .selectByVisibleText(task.type);
        new Select(Util.waitFor(driver, By.cssSelector("#fields_169")))
            .selectByVisibleText(task.status);
        new Select(Util.waitFor(driver, By.cssSelector("#fields_170")))
            .selectByVisibleText(task.priority);
        // To edit description, we need to switch to iframe and back
        driver.switchTo().frame(driver.findElement(By.cssSelector("#cke_1_contents > iframe")));
        driver.findElement(By.cssSelector("body"))
            .sendKeys(task.description);
        driver.switchTo().defaultContent();
        // Submit the form
        Util.waitFor(driver, By.cssSelector("#items_form > div.modal-footer > button.btn.btn-primary.btn-primary-modal-action"))
            .click();
    }
    public void openCreateTaskDialogue() {
        // Click Add task
        Util.waitFor(driver, By.cssSelector("button.btn.btn-primary"))
            .click();
    }

    @Test
    public void deleteTaskTest() {
        // given
        driver.manage().window().maximize();
        auth.login(auth.username, auth.password);
        Task task = new Task(
            "Task",
            "xhraa12",
            "New",
            "Medium",
            "ABC"
        );
        // when
        this.navigateToMyProjectPage();
        this.openCreateTaskDialogue();
        // Fil in the form
        this.createTask(task);
        // Select Info i
        Util.waitFor(driver, By.xpath(String.format("//*[text()=\"%s\"]/../..//*[@title=\"Info\"]", task.name)))
            .click();
        // Check created info table data
        Assert.assertEquals(
            Util.waitFor(driver, By.cssSelector("body > div.page-container > div.page-content-wrapper > div > div > div.row > div > div:nth-child(3) > div.col-md-4 > div > div > div:nth-child(2) > table > tbody > tr.form-group-167 > td > div"))
                .getText(),
            task.type);
        Assert.assertEquals(
            Util.waitFor(driver, By.cssSelector("body > div.page-container > div.page-content-wrapper > div > div > div.row > div > div:nth-child(3) > div.col-md-4 > div > div > div:nth-child(2) > table > tbody > tr.form-group-169 > td > div"))
                .getText(),
            task.status);
        Assert.assertEquals(
            Util.waitFor(driver, By.cssSelector("body > div.page-container > div.page-content-wrapper > div > div > div.row > div > div:nth-child(3) > div.col-md-4 > div > div > div:nth-child(2) > table > tbody > tr.form-group-170 > td > div"))
                .getText(),
            task.priority);
        Assert.assertEquals(
            Util.waitFor(driver, By.cssSelector("body > div.page-container > div.page-content-wrapper > div > div > div.row > div > div:nth-child(3) > div.col-md-8.project-info > div.portlet.portlet-item-description > div.portlet-title > div.caption"))
                .getText(),
            task.name);
        Assert.assertEquals(
            Util.waitFor(driver, By.cssSelector("body > div.page-container > div.page-content-wrapper > div > div > div.row > div > div:nth-child(3) > div.col-md-8.project-info > div.portlet.portlet-item-description > div.portlet-body > div.item-content-box.ckeditor-images-content-prepare > div > div.content_box_content.fieldtype_textarea_wysiwyg"))
                .getText(),
            task.description);
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

    public void clearTasksFilters() {
        // If there is a thrashcan in Filters box
        List<WebElement> thrashcans = Util.waitFor(driver, By.cssSelector(".portlet.portlet-filters-preview.noprint"))
                .findElements(By.cssSelector(".fa.fa-trash-o"));
        if (thrashcans.size() > 0) {
            // Click it to remove all filters
            thrashcans.get(0).click();
        }
        Util.waitFor(driver, By.cssSelector(".select_all_items"));
    }
    public void resetTasks() {
        this.clearTasksFilters();
        // If there are no items, skip deletion
        int tasksCount = driver
            .findElements(By.cssSelector(".item_heading_link"))
            .size();
        if (tasksCount > 0) {
            // Delete all items
            // Check all items
            Util.waitFor(driver, By.cssSelector(".select_all_items"))
                    .click();
            // Open With Selected dropdown
            Util.waitFor(driver, By.cssSelector(".btn.btn-default.dropdown-toggle"))
                    .click();
            // Click Delete
            Util.waitFor(driver, By.cssSelector(".entitly-listing-buttons-left .fa.fa-trash-o"))
                    .click();
            // Confirm
            Util.waitFor(driver, By.cssSelector(".btn.btn-primary.btn-primary-modal-action"))
                    .click();
        }
    }
    @Test
    public void typeTasksTest() {
        // Given
        driver.manage().window().maximize();
        auth.login(auth.username, auth.password);
        Task[] data = {
                new Task("Task", "Task 1", "New", "Medium", ""),
                new Task("Task", "Task 2", "Open", "Medium", ""),
                new Task("Task", "Task 3", "Waiting", "Medium", ""),
                new Task("Task", "Task 4", "Done", "Medium", ""),
                new Task("Task", "Task 5", "Closed", "Medium", ""),
                new Task("Task", "Task 6", "Paid", "Medium", ""),
                new Task("Task", "Task 7", "Canceled", "Medium", ""),

        };
        this.navigateToMyProjectPage();
        this.resetTasks();
        // When
        // Create each task from data list
        for (Task task: data) {
            this.openCreateTaskDialogue();
            this.createTask(task);
            // We are back on the list
            Util.waitFor(driver, By.cssSelector("body > div.page-container > div.page-content-wrapper > div > div > div.row > div > div:nth-child(7) > div:nth-child(1) > div > button"));
        }
        // Apply default filters
        // Open Filter dropdown
        Util.waitFor(driver, By.cssSelector("body > div.page-container > div.page-content-wrapper > div > div > div.row > div > div.portlet.portlet-filters-preview.noprint > div.portlet-title > div.caption > div:nth-child(1) > button"))
            .click();
        // Select Default Filters
        Util.waitFor(driver, By.cssSelector("body > div.page-container > div.page-content-wrapper > div > div > div.row > div > div.portlet.portlet-filters-preview.noprint > div.portlet-title > div.caption > div:nth-child(1) > ul > li:nth-child(1) > a"))
            .click();
        // Wait for filter to apply
        Util.waitFor(driver, By.cssSelector(".filters-preview-condition-include"));
        // Then
        // Only three tasks are shown in the list
        Assert.assertEquals(
            driver
                .findElements(By.cssSelector(".item_heading_link"))
                .size(),
            3
        );

        // When
        // Change Status filter: Remove Open
        // Open Default filter dialogue
        Util.waitFor(driver, By.cssSelector(".filters-preview-condition-include"))
            .click();
        // Click "x" on Open filter to remove it
        Util.waitFor(driver, By.xpath("//*[@class=\"search-choice\"]/span[text()=\"Open\"]/../a"))
            .click();
        // Save to close the dialogue and apply new filter
        Util.waitFor(driver, By.cssSelector(".btn.btn-primary.btn-primary-modal-action"))
            .click();
        // Wait for filter to apply
        Util.waitFor(driver, By.cssSelector(".filters-preview-condition-include"));
        // Then
        // Only New and Waiting are in the list
        for (WebElement listItem: driver.findElements(By.cssSelector(".fieldtype_dropdown.field-169-td"))) {
            Assert.assertEquals(listItem.getText() == "New" || listItem.getText() == "Waiting", true);
        }

        // When
        // All filters are cleared
        this.clearTasksFilters();
        // Then
        // All created tasks appear
        for (Task task: data) {
            Assert.assertEquals(
                Util.waitFor(driver, By.xpath(String.format("//*[@class=\"item_heading_link\" and text()=\"%s\"]", task.name)))
                    .getText(),
                task.name
            );
        }
    }
}
