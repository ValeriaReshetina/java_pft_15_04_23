package ru.stqa.pft.addressbook.tests;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.ContactData;
import ru.stqa.pft.addressbook.model.GroupData;
import ru.stqa.pft.addressbook.model.Groups;

import java.util.Arrays;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class ContactInfoTests extends TestBase {

    @BeforeMethod
    public void ensurePreconditions() {
        String groupName = "test1";
        Groups groups = app.db().groups();

        if (!app.group().isThereAGroup(groupName)) {
            app.goTo().groupPage();
            app.group().create(new GroupData().withName(groupName));
            app.goTo().homePage();
        }
        if (app.contact().list().size() == 0) {
            app.contact().create(new ContactData().withFirstName("Валерия").withMiddleName("Евгеньевна")
                    .withLastName("Решетина").withAddress("Yessentuki, Oktyabrskaya 337, 357600").
                    withMobile("+7(988)1120310").withEmail("flyingscarlett@yandex.ru").
                    inGroup(groups.iterator().next()), true);
        }
    }

    @Test
    public void testContactInfo() {
        app.goTo().homePage();
        ContactData contact = app.contact().all().iterator().next();
        ContactData contactInfoFromEditForm = app.contact().infoFromEditForm(contact);
        assertThat(contact.getAllPhones(), equalTo(mergePhones(contactInfoFromEditForm)));
        assertThat(contact.getAllEmails(), equalTo(mergeMails(contactInfoFromEditForm)));

        assertThat(contact.getAddress(), equalTo(contactInfoFromEditForm.getAddress()));
    }

    private String mergeMails(ContactData contact) {
        return Arrays.asList(contact.getEmail(), contact.getSecondEmail(), contact.getThirdEmail())
                .stream().filter((s) -> !s.equals("")).collect(Collectors.joining("\n"));
    }

    private String mergePhones(ContactData contact) {
        return Arrays.asList(contact.getFirstHomePhone(), contact.getSecondHomePhone(),
                        contact.getMobilePhone(), contact.getWorkPhone())
                .stream().filter((s) -> !s.equals(""))
                .map(ContactInfoTests::phoneCleaned)
                .collect(Collectors.joining("\n"));
    }

    public static String phoneCleaned(String phone) {
        return phone.replaceAll("\\s", "").replaceAll("[-()]", "");
    }
}
