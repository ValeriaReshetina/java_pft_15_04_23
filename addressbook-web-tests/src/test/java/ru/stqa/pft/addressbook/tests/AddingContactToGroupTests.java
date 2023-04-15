package ru.stqa.pft.addressbook.tests;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.appmanager.DbHelper;
import ru.stqa.pft.addressbook.model.ContactData;
import ru.stqa.pft.addressbook.model.Contacts;
import ru.stqa.pft.addressbook.model.GroupData;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static org.hamcrest.MatcherAssert.assertThat;

public class AddingContactToGroupTests extends TestBase {

    private String randomContactFirstName = "testName" + ThreadLocalRandom.current().nextInt(1, 10000);
    private String randomGroupName = "groupName" + ThreadLocalRandom.current().nextInt(1, 10000);
    private DbHelper dbHelper = new DbHelper();

    @BeforeMethod
    public void ensurePreconditions() {
        app.contact().create(new ContactData().withFirstName(randomContactFirstName).withMiddleName("Евгеньевна")
                    .withLastName("Решетина").withAddress("Ессентуки, Октябрьская 337, 357600").
                    withMobile("+7(988)1120310").withEmail("flyingscarlett@yandex.ru"),true);
        app.goTo().groupPage();
        app.group().create(new GroupData().withName(randomGroupName));
        app.goTo().homePage();
    }

    @Test
    public void addingContactToGroupTest() {
        app.goTo().homePage();
        app.contact().selectContactByFirstName(randomContactFirstName);
        app.contact().selectGroupByName(randomGroupName);
        app.contact().addContactToSelectedGroup();

        verifyGroupListInUI();
        verifyContactListInUI();

        Integer createdContactId = dbHelper.getContactIdByFirstName(randomContactFirstName);

        Assert.assertNotEquals(createdContactId, null,
                "Can't get contactId for contact via DB: " + randomContactFirstName);

        Assert.assertNotEquals(dbHelper.isGroupExistInDBByGroupName(randomGroupName), false,
                "Created group not found via DB by name: " + randomGroupName);

        ContactData createdContactFromGroup = dbHelper.findContactInGroup(randomGroupName, createdContactId);

        Assert.assertNotEquals(createdContactFromGroup, null,
                "Created contact with id: " + createdContactId + " not found in: " + randomGroupName);

        Assert.assertEquals(createdContactFromGroup.getFirstName(), randomContactFirstName,
                "Contact found in group " + randomGroupName + " have wrong firstName");
    }
}
