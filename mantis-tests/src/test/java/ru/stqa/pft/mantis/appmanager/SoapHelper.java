package ru.stqa.pft.mantis.appmanager;

import biz.futureware.mantis.rpc.soap.client.*;
import ru.stqa.pft.mantis.modelMantis.IssueMantis;
import ru.stqa.pft.mantis.modelMantis.Project;

import javax.xml.rpc.ServiceException;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class SoapHelper {
    private ApplicationManager app;

    public SoapHelper(ApplicationManager app) {
        this.app = app;
    }

    public Set<Project> getProjects() throws MalformedURLException, ServiceException, RemoteException {
        MantisConnectPortType mantisConnectPort = getMantisConnect();
        ProjectData[] projects = mantisConnectPort
                .mc_projects_get_user_accessible("administrator", "root");
        return Arrays.asList(projects).stream().map((p) -> new Project()
                .withId(p.getId().intValue()).withName(p.getName())).collect(Collectors.toSet());
    }

    private static MantisConnectPortType getMantisConnect() throws ServiceException, MalformedURLException {
        MantisConnectPortType mantisConnectPort = new MantisConnectLocator()
                .getMantisConnectPort(new URL("http://localhost/mantisbt-2.25.6/api/soap/mantisconnect.php"));
        return mantisConnectPort;
    }

    public IssueMantis addIssue(IssueMantis issueMantis) throws MalformedURLException, ServiceException, RemoteException {
        MantisConnectPortType mantisConnectPortType = getMantisConnect();
        String[] categories = mantisConnectPortType.mc_project_get_categories(
                "administrator", "root", BigInteger.valueOf(issueMantis.getProject().getId()));
        IssueData issueData = new IssueData();
        issueData.setSummary(issueMantis.getSummary());
        issueData.setDescription(issueMantis.getDescription());
        issueData.setProject(new ObjectRef(BigInteger.valueOf(
                issueMantis.getProject().getId()), issueMantis.getProject().getName()));
        issueData.setCategory(categories[0]);
        BigInteger issueId = mantisConnectPortType.mc_issue_add(
                "administrator", "root", issueData);
        IssueData createdIssueData = mantisConnectPortType.mc_issue_get(
                "administrator", "root", issueId);
        return new IssueMantis().withId(createdIssueData.getId().intValue()).withSummary(createdIssueData.getSummary())
                .withDescription(createdIssueData.getDescription())
                .withProject(new Project().withId(createdIssueData.getProject().getId().intValue())
                        .withName(createdIssueData.getProject().getName()));
    }

    public IssueData getIssueData(int issueId) throws MalformedURLException, ServiceException, RemoteException {
        MantisConnectPortType mantisConnectPortType = getMantisConnect();
        return mantisConnectPortType.mc_issue_get(
                        "administrator",
                        "root",
                        BigInteger.valueOf(issueId));
    }
}
