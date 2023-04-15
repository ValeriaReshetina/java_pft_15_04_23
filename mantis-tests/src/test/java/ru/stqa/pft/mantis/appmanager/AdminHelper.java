package ru.stqa.pft.mantis.appmanager;


import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class AdminHelper extends HelperBase {

    public AdminHelper(ApplicationManager app) {
        super(app);
    }

    public void authorization() throws InterruptedException {
        //app.wd.findElement(By.xpath("//a[@href='login_page.php']"));
        new WebDriverWait(app.getDriver(), 10).until(driver -> driver.findElement(
                        By.xpath("//input[contains(@placeholder, 'Пользователь')]"))).click();

        type(By.xpath("//input[contains(@placeholder, 'Пользователь')]"), "administrator");
        app.wd.findElement(By.xpath("//input[contains(@value, 'Вход')]")).click();
        type(By.xpath("//input[contains(@placeholder, 'Пароль')]"), "root");
        app.wd.findElement(By.xpath("//input[contains(@value, 'Вход')]")).click();
        //wd.switchTo().alert().accept();
    }

    public void goToUsersControlPanel() {
        app.wd.findElement(By.xpath("//a[contains(@href, 'manage_overview')]")).click();
        app.wd.findElement(By.xpath("//a[contains(@href, 'manage_user_page')]")).click();
    }

    public void selectUserById(int id) {
        WebDriverWait wait = new WebDriverWait(app.getDriver(), 2);
        String locatorToFindUser = "//a[@href='manage_user_edit_page.php?user_id=" + id + "']";

        List<WebElement> listOfUserToSelectResults;

        listOfUserToSelectResults = wd.findElements(By.xpath(locatorToFindUser));

        if (listOfUserToSelectResults.size() != 0){
            listOfUserToSelectResults.get(0).click();
            return;
        }

        String currentURL = wd.getCurrentUrl();

        while (listOfUserToSelectResults.size() == 0){
            wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//*[contains(text(), 'След')]"))).click();

            String newUrl = wd.getCurrentUrl();

            if (newUrl.equals(currentURL)){
                break;
            }
            else {
                currentURL = newUrl;
            }

            listOfUserToSelectResults = wd.findElements(By.xpath(locatorToFindUser));
        }

        listOfUserToSelectResults.get(0).click();

//        wd.findElements(By.xpath("//*[contains(text(), 'След')]");
//
//        if(wd.findElements(By.xpath("//*[contains(text(), 'След')]")).size() > 0){
//
//        }
//        else {
//            new WebDriverWait(app.getDriver(), 2)
//                    .until(driver -> driver.findElement(By.xpath(locatorToFindUser))).click();
//        }
    }

    public void resetCurrentUserPassword() {
//        String xpath = "//a[contains(@href, 'manage_user_edit_page') and (text()='USER_NAME')]"
//                .replace("USER_NAME", username);
//        app.wd.findElement(By.xpath(xpath)).click();
        app.wd.findElement(By.xpath("//input[@value='Сбросить пароль']")).click();
        isElementPresent(By.xpath("//*[contains(text(), 'адресу электронной почты')]"));
    }

    public void changePassword(String confirmationLink, String userName, String newPassword, String confirmNewPassword) {
        wd.get(confirmationLink);
        type(By.xpath("//*[@id='realname']"), userName);
        type(By.xpath("//*[@id='password']"), newPassword);
        type(By.xpath("//*[@id='password-confirm']"), confirmNewPassword);
        wd.findElement(By.xpath("//*[@type='submit']")).click();
    }
}
