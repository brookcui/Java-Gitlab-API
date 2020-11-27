package models;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GitlabIssue {
    private int id; // required
//    private GitlabMilestone milestone;
    private GitlabUser author;
    private String description;
    private String state;
    private int iid; // required
    private List<GitlabUser> assignees;
//    private List<GitlabLabel> labels;
    private int upvotes;
    private int downvotes;
    private int mergeRequestCount;
    private String title; // required
    private LocalDateTime updatedAt;
    private LocalDateTime createdAt;
    private LocalDateTime closedAt;
    private GitlabUser closedBy;
    private boolean subscribed;
    private LocalDateTime dueDate;
    private String webUrl;
    private boolean hasTasks;
    private int epicId;

    @Deprecated
    public static class Builder {
        private int id; // required
//        private GitlabMilestone milestone;
        private GitlabUser author;
        private String description;
        private String state;
        private int iid; // required
        private List<GitlabUser> assignees;
//        private List<GitlabLabel> labels;
        private int upvotes;
        private int downvotes;
        private int mergeRequestCount;
        private String title; // required
        private LocalDateTime updatedAt;
        private LocalDateTime createdAt;
        private LocalDateTime closedAt;
        private GitlabUser closedBy;
        private boolean subscribed;
        private LocalDateTime dueDate;
        private String webUrl;
        private boolean hasTasks;
        private int epicId;

        public Builder(String title) {
            this.id = 0; // FIXME
            this.iid = 0; // FIXME
            this.title = title;
            this.createdAt = LocalDateTime.now();
        }

//        public Builder milestone(GitlabMilestone milestone) {
//            this.milestone = milestone;
//            return this;
//        }

        public Builder author(GitlabUser author) {
            this.author = author;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder state(String state) {
            this.state = state;
            return this;
        }

        public Builder assignees(List<GitlabUser> assignees) {
            this.assignees = new ArrayList<>(assignees);
            return this;
        }

//        public Builder labels(List<GitlabLabel> labels) {
//            this.labels = new ArrayList<>(labels);
//            return this;
//        }

        public Builder upvotes(int upvotes) {
            this.upvotes = upvotes;
            return this;
        }

        public Builder downvotes(int downvotes) {
            this.downvotes = downvotes;
            return this;
        }

        public Builder mergeRequestCount(int mergeRequestCount) {
            this.mergeRequestCount = mergeRequestCount;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder closedAt(LocalDateTime closedAt) {
            this.closedAt = closedAt;
            return this;
        }

        public Builder closedBy(GitlabUser closedBy) {
            this.closedBy = closedBy;
            return this;
        }

        public Builder setSubscribed() {
            this.subscribed = true;
            return this;
        }

        public Builder dueDate(LocalDateTime dueDate) {
            this.dueDate = dueDate;
            return this;
        }

        public Builder webUrl(String webUrl) {
            this.webUrl = webUrl;
            return this;
        }

        public Builder setHasTasks() {
            this.hasTasks = true;
            return this;
        }

        public Builder epicId(int epicId) {
            this.epicId = epicId;
            return this;
        }

        public GitlabIssue build() {
            return new GitlabIssue(this);
        }
    }

    @Deprecated
    private GitlabIssue(Builder builder) {
        this.id = builder.id; // required
//        this.milestone = builder.milestone;
        this.author = builder.author;
        this.description = builder.description;
        this.state = builder.state;
        this.iid = builder.iid; // required
        this.assignees = builder.assignees;
//        this.labels = builder.labels;
        this.upvotes = builder.upvotes;
        this.downvotes = builder.downvotes;
        this.mergeRequestCount = builder.mergeRequestCount;
        this.title = builder.title; // required
        this.updatedAt = builder.updatedAt;
        this.createdAt = builder.createdAt;
        this.closedAt = builder.closedAt;
        this.closedBy = builder.closedBy;
        this.subscribed = builder.subscribed;
        this.dueDate = builder.dueDate;
        this.webUrl = builder.webUrl;
        this.hasTasks = builder.hasTasks;
        this.epicId = builder.epicId;
    }

    // TODO: public constructor
    // TODO: withXXX() for all necessary public and modifiable fields


    // create a new gitlab issue
    public GitlabIssue create() throws IOException {
        return this; // TODO
    }

    public GitlabIssue delete() throws IOException {
        return this; // TODO
    }

    public GitlabIssue update() throws IOException {
        return this; // TODO
    }


    // TODO: Add note and PR feature later?


    @Override
    public String toString() {
        return title;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, iid);
    }

    @Override
    // TODO: compare all fields for equals
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof GitlabIssue)) {
            return false;
        }
        GitlabIssue issue = (GitlabIssue) o;
        return issue.id == this.id && issue.iid == this.iid;
    }
}
