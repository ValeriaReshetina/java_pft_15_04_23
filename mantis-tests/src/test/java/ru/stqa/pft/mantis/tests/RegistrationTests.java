package ru.stqa.pft.mantis.tests;

import org.testng.annotations.Test;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;

import ru.lanwen.verbalregex.VerbalExpression;
import ru.stqa.pft.mantis.modelMantis.MailMessage;

import static org.testng.Assert.*;

public class RegistrationTests extends TestBase {

    //@BeforeMethod
    public void StartMailServer() {
        app.mail().start();

    }

    @Test
    public void testRegistration() throws MessagingException, IOException {
        long now = System.currentTimeMillis();
        String user = String.format("user%s", now);
        String password = "password";
        String email = String.format("user%s@localhost", now);
        app.jamesMailAgent().createUser(user, password);
        app.registration().start(user, email);
        //List<MailMessage> mailMessages = app.mail().waitForMail(2, 1000);
        List<MailMessage>mailMessages = app.jamesMailAgent().waitForMail(user, password, 60000);
        String confirmationLink = findConfirmationLink(mailMessages, email);
        app.registration().finishRegistration(confirmationLink, "password");
        assertTrue(app.newSession().login(user, password));
    }

    private String findConfirmationLink(List<MailMessage> mailMessages, String email) {
        MailMessage mailMessage = mailMessages.stream().filter((m) -> m.to.equals(email)).findFirst().get();
        VerbalExpression regex = VerbalExpression.regex().find("http://").nonSpace().oneOrMore().build();
        return regex.getText(mailMessage.text);
    }

    //@AfterMethod (alwaysRun = true)
    public void stopMailServer() {
        app.mail().stop();
    }

}
