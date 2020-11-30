package org.gitlab.api.models;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.gitlab.api.http.Body;
import org.gitlab.api.http.GitlabHTTPRequestor;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY,
        getterVisibility = JsonAutoDetect.Visibility.NONE
)
public class GitlabIssue extends GitlabComponent {
    @JsonIgnore
    private GitlabProject project;

    @JsonProperty("id")
    private int id; // required, issue id === iid
    @JsonProperty("iid")
    private int iid; // required, issue id === iid

    @JsonProperty("project_id")
    private int projectId;
    @JsonProperty("author")
    private GitlabUser author;
    @JsonProperty("description")
    private String description;
    @JsonProperty("state")
    private String state;
    @JsonProperty("assignees")
    private List<GitlabUser> assignees = new ArrayList<>();
    @JsonProperty("upvotes")
    private int upvotes;
    @JsonProperty("downvotes")
    private int downvotes;
    @JsonProperty("merge_requests_count")
    private int mergeRequestCount;
    private String title; // required
    @JsonProperty("labels")
    private List<String> labels = new ArrayList<>();
    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;
    @JsonProperty("created_at")
    private LocalDateTime createdAt;
    @JsonProperty("closed_at")
    private LocalDateTime closedAt;
    @JsonProperty("closed_by")
    private GitlabUser closedBy;
    @JsonProperty("subscribed")
    private boolean subscribed;
    @JsonProperty("due_date")
    //@JsonDeserialize(using = DateDeserializer.class)
    private LocalDate dueDate;
    @JsonProperty("web_url")
    private String webUrl;
    @JsonProperty("has_tasks")
    private boolean hasTasks;
    @JsonProperty("epic_id")
    private int epicId;

    /**
     * Construct the issue with name
     * TODO: package private or protected
     *
     * @param title
     */
    public GitlabIssue(@JsonProperty("title") String title) {
        this.title = title;
    }

    public List<String> getLabels() {
        return labels;
    }

    GitlabIssue withProject(GitlabProject project) {
        this.project = project;
        this.projectId = project.getId();
        return this;
    }

    @Override
    public GitlabIssue withHTTPRequestor(GitlabHTTPRequestor requestor) {
        super.withHTTPRequestor(requestor);
        return this;
    }

    // create a new gitlab issue
    public GitlabIssue create() throws IOException {
        Body body = new Body()
                .putString("title", title)
                .putIntArray("assignee_ids", assignees.stream().mapToInt(GitlabUser::getId).toArray())
                .putStringArray("labels", labels)
                .putString("description", description)
                .putDate("due_date", dueDate);
        return getHTTPRequestor().post(String.format("/projects/%d/issues", projectId), body, this);
    }

    public GitlabIssue delete() throws IOException {
        getHTTPRequestor().delete(String.format("/projects/%d/issues/%d", projectId, iid));
        return this;
    }

    public GitlabIssue update() throws IOException {
        Body body = new Body()
                .putString("title", title)
                .putIntArray("assignee_ids", assignees.stream().mapToInt(GitlabUser::getId).toArray())
                .putString("description", description)
                .putStringArray("labels", labels)
                .putDate("due_date", dueDate);
        return getHTTPRequestor().put(String.format("/projects/%d/issues/%d", projectId, iid), body, this);
    }

    public GitlabIssue close() throws IOException {
        Body body = new Body().putString("state_event", "close");
        return getHTTPRequestor().put(String.format("/projects/%d/issues/%d", projectId, iid), body, this);
    }

    public GitlabIssue reopen() throws IOException {
        Body body = new Body().putString("state_event", "reopen");
        return getHTTPRequestor().put(String.format("/projects/%d/issues/%d", projectId, iid), body, this);
    }

    /**
     * Lazily initialized project field
     *
     * @return
     * @throws IOException
     */
    public GitlabProject getProject() throws IOException {
        if (project == null) {
            project = GitlabProject.fromId(getHTTPRequestor(), projectId);
        }
        return project;
    }

    public int getIid() {
        return iid;
    }

    public GitlabUser getAuthor() {
        return author;
    }

    public String getDescription() {
        return description;
    }

    public String getState() {
        return state;
    }

    public List<GitlabUser> getAssignees() {
        return assignees;
    }

    public int getUpvotes() {
        return upvotes;
    }

    public int getDownvotes() {
        return downvotes;
    }

    public int getMergeRequestCount() {
        return mergeRequestCount;
    }

    public String getTitle() {
        return title;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getClosedAt() {
        return closedAt;
    }

    public GitlabUser getClosedBy() {
        return closedBy;
    }

    public boolean isSubscribed() {
        return subscribed;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public boolean hasTasks() {
        return hasTasks;
    }

    public int getEpicId() {
        return epicId;
    }

    public int getProjectId() {
        return projectId;
    }

    public boolean isHasTasks() {
        return hasTasks;
    }

    /*
     * Setters
     * There will be no setter for projectId, id, author, upvotes, downvotes, mergeRequestCount, updatedAt, createdAt,
     * closedAt, closedBy, subscribed, webUrl, hasTasks, epicId
     *
     */
    public GitlabIssue withDescription(String description) {
        this.description = description;
        return this;
    }

    public GitlabIssue withAssignees(List<GitlabUser> assignees) {
        this.assignees = assignees;
        return this;
    }

    public GitlabIssue withTitle(String title) {
        this.title = title;
        return this;
    }

    public GitlabIssue withDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
        return this;
    }

    public GitlabIssue withLabels(List<String> labels) {
        this.labels = labels;
        return this;
    }
    // TODO: Add note and PR feature later?


    @Override
    public String toString() {
        return "GitlabIssue{" +
                "id=" + id +
                ", iid=" + iid +
                ", projectId=" + projectId +
                ", author=" + author +
                ", description=" + description +
                ", state=" + state +
                ", assignees=" + assignees +
                ", upvotes=" + upvotes +
                ", downvotes=" + downvotes +
                ", mergeRequestCount=" + mergeRequestCount +
                ", title=" + title +
                ", labels=" + labels +
                ", updatedAt=" + updatedAt +
                ", createdAt=" + createdAt +
                ", closedAt=" + closedAt +
                ", closedBy=" + closedBy +
                ", subscribed=" + subscribed +
                ", dueDate=" + dueDate +
                ", webUrl=" + webUrl +
                ", hasTasks=" + hasTasks +
                ", epicId=" + epicId +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(project, iid);
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
        GitlabIssue that = (GitlabIssue) o;
        return project == that.project &&
                iid == that.iid &&
                upvotes == that.upvotes &&
                downvotes == that.downvotes &&
                mergeRequestCount == that.mergeRequestCount &&
                epicId == that.epicId &&
                subscribed == that.subscribed &&
                hasTasks == that.hasTasks &&
                Objects.equals(author, that.author) &&
                Objects.equals(description, that.description) &&
                Objects.equals(state, that.state) &&
                Objects.equals(title, that.title) &&
                Objects.equals(assignees, that.assignees) &&
                Objects.equals(updatedAt, that.updatedAt) &&
                Objects.equals(createdAt, that.createdAt) &&
                Objects.equals(closedAt, that.closedAt) &&
                Objects.equals(closedBy, that.closedBy) &&
                Objects.equals(dueDate, that.dueDate) &&
                Objects.equals(webUrl, that.webUrl);
    }

    public int getId() {
        return id;
    }
}
