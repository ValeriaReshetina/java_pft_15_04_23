package ru.stqa.pft.mantis.tests;

import biz.futureware.mantis.rpc.soap.client.IssueData;
import org.openqa.selenium.remote.BrowserType;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import ru.stqa.pft.mantis.appmanager.ApplicationManager;
import ru.stqa.pft.mantis.modelBugify.IssueBugify;

import javax.xml.rpc.ServiceException;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.util.Set;

public class TestBase {

    protected static final ApplicationManager app =
            new ApplicationManager(System.getProperty("browser", BrowserType.CHROME));

    static {
        try {
            app.init();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeSuite(alwaysRun = true)
    public void setUp() throws Exception {
        app.init();
        app.ftp().upload(new File("src/test/resources/config_inc.php"),
                "config_inc.php", "config_inc.php.bak");
    }

    @AfterSuite(alwaysRun = true)
    public void tearDown() throws IOException {
        app.ftp().restore("config_inc.php.bak", "config_inc.php");
        app.stop();
    }

    public boolean isIssueOpenForMantis(int issueId) throws MalformedURLException, ServiceException, RemoteException {
        IssueData issueData = app.soap().getIssueData(issueId);
        String resolutionStringResult = issueData.getResolution().getName();
        if (resolutionStringResult.equals("fixed")) {
            return false;
        } else {
            return true;
        }
    }

    public void skipIfNotFixedForMantis(int issueId) throws MalformedURLException, ServiceException, RemoteException {
        if (isIssueOpenForMantis(issueId)) {
            throw new SkipException("Ignored because of issue " + issueId);
        }
    }

    public void skipIfNotFixedForBugify(int issueId) throws IOException, ServiceException {
        if (isIssueOpenForBugify(issueId)) {
            throw new SkipException("Ignored because of issue " + issueId);
        }
    }

    private boolean isIssueOpenForBugify(int issueId) throws IOException {
        Set<IssueBugify> allIssues = app.rest().getIssues();

        boolean issueWasFoundInList = false;

        for (IssueBugify issue : allIssues) {
            if (issue.getId() == issueId){
                issueWasFoundInList = true;
            }
        }
        // По логике Bugify, любое issue закрытое или разрешенное, перестает выдаваться в списке. В списке такое
        // issue будет отсутствовать (значит issue закрыто, в ином случае - открыто).
        return issueWasFoundInList;
    }
}
