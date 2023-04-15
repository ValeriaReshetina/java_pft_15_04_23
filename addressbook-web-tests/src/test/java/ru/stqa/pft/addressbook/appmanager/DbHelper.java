package ru.stqa.pft.addressbook.appmanager;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import ru.stqa.pft.addressbook.model.ContactData;
import ru.stqa.pft.addressbook.model.Contacts;
import ru.stqa.pft.addressbook.model.GroupData;
import ru.stqa.pft.addressbook.model.Groups;

import java.util.List;

public class DbHelper {

    private final SessionFactory sessionFactory;

    public DbHelper() {
        // A SessionFactory is set up once for an application!
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure() // configures settings from hibernate.cfg.xml
                .build();
        sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
    }

    public Groups groups() {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        List<GroupData> result = session.createQuery("from GroupData").list();
        session.getTransaction().commit();
        session.close();
        return new Groups(result);
    }

    public Contacts contacts() {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        List<ContactData> result = session.createQuery("from ContactData").list();
        session.getTransaction().commit();
        session.close();
        return new Contacts(result);
    }

    public Integer getContactIdByFirstName(String firstName) {
        Contacts contacts = contacts();
        Integer contactId = null;

        for (ContactData contact : contacts.delegate()) {
            if (contact.getFirstName().equals(firstName)) {
                contactId = contact.getId();
            }
        }
        return contactId;
    }

    public boolean isGroupExistInDBByGroupName(String groupNameToFind) {
        Groups allGroups = groups();
        boolean groupFoundViaDB = false;

        for(GroupData group : allGroups) {
            if (group.getName().equals(groupNameToFind)) {
                groupFoundViaDB = true;
            }
        }
        return groupFoundViaDB;
    }

    public ContactData findContactInGroup(String groupNameToFind, int contactId) {
        Groups allGroups = groups();
        GroupData groupToOperate = null;

        for(GroupData group : allGroups) {
            if (group.getName().equals(groupNameToFind)) {
                groupToOperate = group;
            }
        }
        if (groupToOperate == null){
            return  null;
        }

        Contacts contactsInOperatedGroup = groupToOperate.getContacts();

        for(ContactData contactData : contactsInOperatedGroup) {
            if (contactData.getId() == contactId) {
                return contactData;
            }
        }
        return null;
    }
}
