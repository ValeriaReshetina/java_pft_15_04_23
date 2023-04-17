package ru.stqa.pft.mantis.tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import ru.stqa.pft.mantis.modelMantis.IssueMantis;
import ru.stqa.pft.mantis.modelMantis.Project;

import javax.xml.rpc.ServiceException;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.util.Set;

public class SoapTests extends TestBase {
    @Test
    public void testMantisGetProjects() throws MalformedURLException, ServiceException, RemoteException {
        skipIfNotFixedForMantis(1);
        Set<Project> projects = app.soap().getProjects();
        System.out.println(projects.size());
        for (Project project : projects) {
            System.out.println(project.getName());
        }
    }

    @Test
    public void testMantisCreateIssue() throws MalformedURLException, ServiceException, RemoteException {
        skipIfNotFixedForMantis(1);
        Set<Project> projects = app.soap().getProjects();
        IssueMantis issueMantis = new IssueMantis().withSummary("Test issue").withDescription("Test issue description")
                .withProject(projects.iterator().next());
        IssueMantis created = app.soap().addIssue(issueMantis);
        Assert.assertEquals(issueMantis.getSummary(), created.getSummary());
    }
}
