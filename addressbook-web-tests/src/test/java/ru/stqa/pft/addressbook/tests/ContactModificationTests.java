package ru.stqa.pft.addressbook.tests;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.ContactData;
import ru.stqa.pft.addressbook.model.Contacts;
import ru.stqa.pft.addressbook.model.Groups;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

public class ContactModificationTests extends TestBase {

    @BeforeMethod
    public void ensurePreconditions() {
        Groups groups = app.db().groups();
        if (app.db().contacts().size() == 0) {
            app.contact().create(new ContactData().withFirstName("Валерия").withMiddleName("Евгеньевна")
                    .withLastName("Решетина").withMobile("+7(988)1120310")
                    .withEmail("flyingscarlett@yandex.ru").inGroup(groups.iterator().next()), true);
        }
    }

    @Test
    public void testContactModification() {
        Contacts before = app.db().contacts();
        ContactData modifiedContact = before.iterator().next();
        app.contact().initContactModificationById(modifiedContact.getId());
        ContactData contact = new ContactData().withId(modifiedContact.getId()).withFirstName("Valeria")
                .withMiddleName("Evgenyevna").withLastName("Reshetina").withMobile("89881120310")
                .withEmail("lera.reshetina.96@mail.ru");
        app.contact().fillContactForm(contact, false);
        app.contact().submitContactModification();
        app.contact().returnToHomePage();
        assertThat(app.contact().count(), equalTo(before.size()));
        Contacts after = app.db().contacts();
        assertThat(after, equalTo(before.without(modifiedContact).withAdded(contact)));
        verifyContactListInUI();
    }
}
