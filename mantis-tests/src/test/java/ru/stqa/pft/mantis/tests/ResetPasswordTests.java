package ru.stqa.pft.mantis.tests;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.lanwen.verbalregex.VerbalExpression;
import ru.stqa.pft.mantis.appmanager.AdminHelper;
import ru.stqa.pft.mantis.modelMantis.MailMessage;
import ru.stqa.pft.mantis.modelMantis.UserData;
import ru.stqa.pft.mantis.modelMantis.Users;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;

import static org.testng.Assert.assertTrue;


public class ResetPasswordTests extends TestBase {

    private SessionFactory sessionFactory;
    AdminHelper adminHelper = new AdminHelper(app);

    @BeforeMethod
    public void setUp() throws Exception {
        super.setUp();
        app.getDriver();
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
        try {
            sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        } catch (Exception e) {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }

    @Test
    public void testForChangingPassword() throws MessagingException, IOException, InterruptedException {
        long nowTime = System.currentTimeMillis();

        String userName = String.format("user%s", nowTime);
        String userPassword = "password";
        String email = String.format("user%s@localhost", nowTime);

        app.jamesMailAgent().createUser(userName, userPassword);
        app.registration().start(userName, email);

        List<MailMessage>mailMessages = app.jamesMailAgent().waitForMail(userName, userPassword, 60000);
        String confirmationLink = getConfirmationLink(mailMessages, email);

        app.registration().finishRegistration(confirmationLink, "password");

        app.jamesMailAgent().drainEmail(userName, userPassword);

        Users usersSet = app.db().users();
        Integer createdUserId = null;

        for (UserData user : usersSet) {
            if (user.getUsername().equals(userName)){
                createdUserId = user.getId();
            }
        }
        Assert.assertNotNull(createdUserId, "Can't get created user ID");

        String newUserPassword = String.format("password%s", nowTime);

        adminHelper.authorization();
        adminHelper.goToUsersControlPanel();
        adminHelper.selectUserById(createdUserId);
        adminHelper.resetCurrentUserPassword();

        mailMessages = app.jamesMailAgent().waitForMail(userName, userPassword, 60000);
        String resetPasswordLink = getConfirmationLink(mailMessages, email);

        adminHelper.changePassword(resetPasswordLink, userName, newUserPassword, newUserPassword);
        assertTrue(app.newSession().login(userName, newUserPassword));
    }

    public String getConfirmationLink(List<MailMessage> mailMessages, String email) {
        MailMessage mailMessage = mailMessages.stream().filter((m) -> m.to.equals(email)).findFirst().get();
        VerbalExpression regex = VerbalExpression.regex().find("http://").nonSpace().oneOrMore().build();
        return regex.getText(mailMessage.text);
    }
}
