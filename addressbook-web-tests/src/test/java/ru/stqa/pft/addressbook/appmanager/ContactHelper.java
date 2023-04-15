package ru.stqa.pft.addressbook.appmanager;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import ru.stqa.pft.addressbook.model.ContactData;
import ru.stqa.pft.addressbook.model.Contacts;

import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.*;

public class ContactHelper extends HelperBase {

    public ContactHelper(WebDriver wd) {
        super(wd);
    }

    public void submitNewContactCreation() {
        wd.findElement(By.xpath("//div[@id='content']/form/input[21]")).click();
    }

    public void fillContactForm(ContactData contactData, boolean creation) {
        wd.findElement(By.name("firstname")).click();
        wd.findElement(By.name("firstname")).clear();
        wd.findElement(By.name("firstname")).sendKeys(contactData.getFirstName());
        wd.findElement(By.name("middlename")).click();
        wd.findElement(By.name("middlename")).clear();
        wd.findElement(By.name("middlename")).sendKeys(contactData.getMiddleName());
        wd.findElement(By.name("lastname")).click();
        wd.findElement(By.name("lastname")).clear();
        wd.findElement(By.name("lastname")).sendKeys(contactData.getLastName());
        wd.findElement(By.name("mobile")).click();
        wd.findElement(By.name("mobile")).clear();
        wd.findElement(By.name("mobile")).sendKeys(contactData.getMobilePhone());
        wd.findElement(By.name("email")).click();
        wd.findElement(By.name("email")).clear();
        wd.findElement(By.name("email")).sendKeys(contactData.getEmail());
        //attach(By.name("photo"), contactData.getPhoto());

        if (creation) {
            if (contactData.getGroups().size() > 0) {
                assertTrue(contactData.getGroups().size() == 1);
                new Select(wd.findElement(By.name("new_group")))
                        .selectByVisibleText((contactData.getGroups().iterator().next().getName()));
            }
        } else {
            assertFalse(isElementPresent(By.name("new_group")));
        }
    }

    public void initNewContactCreation() {
        wd.findElement(By.linkText("add new")).click();
    }

    public void selectContact(int index) {
        wd.findElements(By.xpath("//input[@name='selected[]']")).get(index).click();
    }

    public void selectContactById(int id) {
        wd.findElement(By.cssSelector("input[value='" + id + "']")).click();
    }

    public void delete(int index) {
        selectContact(index);
        deleteSelectedContact();
        returnToHomePage();
    }

    public void delete(ContactData contact) {
        selectContactById(contact.getId());
        deleteSelectedContact();
        contactCache = null;
        returnToHomePage();
    }

    public void deleteSelectedContact() {
        wd.findElement(By.xpath("//input[@value='Delete']")).click();
        wd.switchTo().alert().accept();
    }

    public void initContactModification(int index) {
        wd.findElements(By.xpath("//img[@alt='Edit']")).get(index).click();
    }

    public void initContactModificationById(int id) {
        String searchString = "//a[@href='edit.php?id=REPLACE']";
        searchString = searchString.replaceAll("REPLACE", String.valueOf(id));
        wd.findElement(By.xpath(searchString)).click();
    }

    public void returnToHomePage() {
        wd.findElement(By.linkText("home")).click();
    }

    public void submitContactModification() {
        click(By.name("update"));
    }

    public void create(ContactData contact, boolean creation) {
        initNewContactCreation();
        fillContactForm(contact, creation);
        submitNewContactCreation();
        contactCache = null;
    }

    public boolean isThereAContact() {
        return isElementPresent(By.name("selected[]"));
    }

    public boolean isThereAGroupInContactCreationForm() {
        return isElementPresent(By.xpath("//select[@name='new_group']//option[text()='test1']"));
    }

    public boolean goToContactCreationPage() {
        return isElementPresent(By.name("add new"));
    }

    public int count() {
        return wd.findElements(By.name("selected[]")).size();
    }

    public List<ContactData> list() {
        List<ContactData> contacts = new ArrayList<ContactData>();
        List<WebElement> elements = wd.findElements(By.name("entry"));
        for (WebElement element : elements) {
            String lastName = String.valueOf(element.findElement(By.xpath(".//td[2]")).getText());
            String firstName = String.valueOf(element.findElement(By.xpath(".//td[3]")).getText());
            int id = Integer.parseInt(element.findElement(By.tagName("input")).getAttribute("value"));
            contacts.add(new ContactData().withId(id).withFirstName(firstName).withLastName(lastName));
        }
        return contacts;
    }

    private Contacts contactCache = null;

    public Contacts all() {
        if (contactCache != null) {
            return new Contacts(contactCache);
        }
        contactCache = new Contacts();
        List<WebElement> elements = wd.findElements(By.name("entry"));

        for (WebElement element : elements) {
            int id = Integer.parseInt(element.findElement(By.tagName("input")).getAttribute("value"));
            String lastName = String.valueOf(element.findElement(By.xpath(".//td[2]")).getText());
            String firstName = String.valueOf(element.findElement(By.xpath(".//td[3]")).getText());
            String allPhones = String.valueOf(element.findElement(By.xpath(".//td[6]")).getText());
            String allEmails = element.findElement(By.xpath(".//td[5]")).getText();
            String address = String.valueOf(element.findElement(By.xpath(".//td[4]")).getText());

            contactCache.add(new ContactData().withId(id).withFirstName(firstName).withLastName(lastName)
                    .withAllPhones(allPhones).withAllMails(allEmails).withAddress(address));
        }
        return new Contacts(contactCache);
    }

    public ContactData infoFromEditForm(ContactData contact) {
        initContactModificationById(contact.getId());
        String firstName = wd.findElement(By.name("firstname")).getAttribute("value");
        String lastName = wd.findElement(By.name("lastname")).getAttribute("value");
        String firstHomePhone = wd.findElement(By.name("home")).getAttribute("value");
        String secondHomePhone = wd.findElement(By.name("phone2")).getAttribute("value");
        String mobilePhone = wd.findElement(By.name("mobile")).getAttribute("value");
        String workPhone = wd.findElement(By.name("work")).getAttribute("value");
        String eMail = wd.findElement(By.name("email")).getAttribute("value");
        String secondEmail = wd.findElement(By.name("email2")).getAttribute("value");
        String thirdEmail = wd.findElement(By.name("email3")).getAttribute("value");
        String address = wd.findElement(By.name("address")).getAttribute("value");
        wd.navigate().back();
        return new ContactData().withId(contact.getId()).withFirstName(firstName).withLastName(lastName)
                .withFirstHomePhone(firstHomePhone).withSecondHomePhone(secondHomePhone).withMobilePhone(mobilePhone)
                .withWorkPhone(workPhone).withEmail(eMail).withSecondEmail(secondEmail).withThirdEmail(thirdEmail)
                .withAddress(address);
    }

    public void addContactToSelectedGroup() {
        wd.findElement(By.xpath("//input[@value='Add to']")).click();
    }

    public void selectGroupWithContacts() {
        click(By.name("group"));
        wd.findElement(By.name("option")).getAttribute("value");
    }

    public void selectGroupByName(String groupName){
        new Select(wd.findElement(By.xpath("//select[@name='to_group']")))
                .selectByVisibleText(groupName);
    }

    public void selectContactByFirstName(String contactFirstName){
        wd.findElement(By.xpath("//tr[@name='entry']/td[text()='" + contactFirstName
                + "']/parent::tr/td[1]")).click();
    }


    public void deleteContactFromSelectedGroup() {
        wd.findElement(By.xpath("//input[@value='Delete']")).click();
        wd.switchTo().alert().accept();
        assertTrue(isElementPresent(By.xpath("//*[text()='Record successful deleted']")));
    }

    public void goToGroupPageAfterAddingContact() {
        wd.findElement(By.xpath("//a[contains(@href, 'group=')]")).click();
    }
}
