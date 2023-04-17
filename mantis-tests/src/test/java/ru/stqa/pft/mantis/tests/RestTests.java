package ru.stqa.pft.mantis.tests;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import org.apache.http.client.fluent.Executor;
import org.apache.http.client.fluent.Request;
import org.apache.http.message.BasicNameValuePair;
import org.testng.annotations.Test;
import ru.stqa.pft.mantis.modelBugify.IssueBugify;

import javax.xml.rpc.ServiceException;
import java.io.IOException;
import java.util.Set;

import static org.testng.Assert.assertEquals;
import static ru.stqa.pft.mantis.tests.TestBase.app;

public class RestTests extends TestBase {

    @Test
    public void testCreateIssue() throws IOException, ServiceException {
        skipIfNotFixedForBugify(12);
        Set<IssueBugify> oldIssues = app.rest().getIssues();
        IssueBugify newIssue = new IssueBugify().withSubject("Test IssueBugify").withDescription("New test IssueBugify");
        int issueId = app.rest().createIssue(newIssue);
        Set<IssueBugify> newIssues = app.rest().getIssues();
        oldIssues.add(newIssue.withId(issueId));
        assertEquals(newIssues, oldIssues);
    }


}
