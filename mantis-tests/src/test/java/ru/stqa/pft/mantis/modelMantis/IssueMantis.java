package ru.stqa.pft.mantis.modelMantis;


public class IssueMantis {

    private int id;
    private String summary;
    private String description;
    private Project project;

    public int getId() {
        return id;
    }

    public IssueMantis withId(int id) {
        this.id = id;
        return this;
    }

    public String getSummary() {
        return summary;
    }

    public IssueMantis withSummary(String summary) {
        this.summary = summary;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public IssueMantis withDescription(String description) {
        this.description = description;
        return this;
    }

    public Project getProject() {
        return project;
    }

    public IssueMantis withProject(Project project) {
        this.project = project;
        return this;
    }
}
