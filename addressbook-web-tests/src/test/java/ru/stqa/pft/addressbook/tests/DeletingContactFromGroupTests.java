package ru.stqa.pft.addressbook.tests;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.appmanager.DbHelper;
import ru.stqa.pft.addressbook.model.ContactData;
import ru.stqa.pft.addressbook.model.GroupData;
import java.util.concurrent.ThreadLocalRandom;

public class DeletingContactFromGroupTests extends TestBase {

    private SessionFactory sessionFactory;
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
        app.contact().selectContactByFirstName(randomContactFirstName);
        app.contact().selectGroupByName(randomGroupName);
        app.contact().addContactToSelectedGroup();
    }

    @Test
    public void deletingContactFromGroupTest() {
        verifyGroupListInUI();
        verifyContactListInUI();

        Session session = sessionFactory.openSession();
        session.beginTransaction();

        Assert.assertNotEquals(dbHelper.isGroupExistInDBByGroupName(randomGroupName), false,
                "Created group not found via DB by name: " + randomGroupName);

        Integer createdContactId = dbHelper.getContactIdByFirstName(randomContactFirstName);

        Assert.assertNotEquals(createdContactId, null,
                "CreatedContactId for contact via DB: " + randomContactFirstName + " is null");

        app.contact().goToGroupPageAfterAddingContact();
        app.contact().selectContactByFirstName(randomContactFirstName);
        app.contact().deleteContactFromSelectedGroup();

        ContactData createdContactFromGroup = dbHelper.findContactInGroup(randomGroupName, createdContactId);

        Assert.assertNull(createdContactFromGroup, "ContactID: " + createdContactId +
                " found in " + randomGroupName);
    }
}