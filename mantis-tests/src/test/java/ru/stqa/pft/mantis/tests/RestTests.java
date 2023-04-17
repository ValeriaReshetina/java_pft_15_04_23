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

import java.io.IOException;
import java.util.Set;

import static org.testng.Assert.assertEquals;

public class RestTests {

    @Test
    public void testCreateIssue() throws IOException {
        Set<IssueBugify> oldIssues = getIssues();
        IssueBugify newIssue = new IssueBugify().withSubject("Test IssueBugify").withDescription("New test IssueBugify");
        int issueId = createIssue(newIssue);
        Set<IssueBugify> newIssues = getIssues();
        oldIssues.add(newIssue.withId(issueId));
        assertEquals(newIssues, oldIssues);
    }

    private Set<IssueBugify> getIssues() throws IOException {
        String json = getExecutor().execute(Request.Get("https://bugify.stqa.ru/api/issues.json?pageSize=50000"))
                .returnContent().asString();
        JsonElement parsed = new JsonParser().parse(json);
        JsonElement issues = parsed.getAsJsonObject().get("issues");
        return new Gson().fromJson(issues, new TypeToken<Set<IssueBugify>>() {}.getType());
    }

    private Executor getExecutor() {
        return Executor.newInstance().auth("b31e382ca8445202e66b03aaf31508a3", "");
    }

    private int createIssue(IssueBugify newIssue) throws IOException {
        String json = getExecutor().execute(Request.Post("https://bugify.stqa.ru/api/issues.json")
                        .bodyForm(new BasicNameValuePair("subject", newIssue.getSubject()),
                                new BasicNameValuePair("description", newIssue.getDescription())))
                        .returnContent().asString();
        JsonElement parsed = new JsonParser().parse(json);
        return parsed.getAsJsonObject().get("issue_id").getAsInt();
    }
}
