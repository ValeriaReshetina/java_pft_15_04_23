package ru.stqa.pft.mantis.modelBugify;

import java.util.Objects;

public class IssueBugify {
    private int id;
    private String subject;
    private String description;
    private String state_name;

    public String getState_name() {
        return state_name;
    }

    public void withStatus(String status) {
        this.state_name = status;
    }

    public int getId() {
        return id;
    }

    public IssueBugify withId(int id) {
        this.id = id;
        return this;
    }

    public String getSubject() {
        return subject;
    }

    public IssueBugify withSubject(String subject) {
        this.subject = subject;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public IssueBugify withDescription(String description) {
        this.description = description;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IssueBugify that = (IssueBugify) o;

        if (id != that.id) return false;
        if (!Objects.equals(subject, that.subject)) return false;
        if (!Objects.equals(description, that.description)) return false;
        return Objects.equals(state_name, that.state_name);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (subject != null ? subject.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (state_name != null ? state_name.hashCode() : 0);
        return result;
    }
}
