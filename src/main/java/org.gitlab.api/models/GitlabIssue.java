package org.gitlab.api.models;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.gitlab.api.http.GitlabHTTPRequestor;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY,
        getterVisibility = JsonAutoDetect.Visibility.NONE
)
public class GitlabIssue extends GitlabComponent{
    private int projectId; // required, project id === id
    private int id; // required, issue id === iid

    @JsonProperty(value = "author", access = JsonProperty.Access.WRITE_ONLY)
    private GitlabUser author;
    @JsonProperty("description")
    private String description;
    @JsonProperty("state")
    private String state;
    @JsonProperty("assignees")
    private List<GitlabUser> assignees;
    @JsonProperty(value = "upvotes", access = JsonProperty.Access.WRITE_ONLY)
    private int upvotes;
    @JsonProperty(value = "downvotes", access = JsonProperty.Access.WRITE_ONLY)
    private int downvotes;
    @JsonProperty(value = "merge_requests_count", access = JsonProperty.Access.WRITE_ONLY)
    private int mergeRequestCount;
    private String title; // required
    @JsonProperty(value = "updated_at", access = JsonProperty.Access.WRITE_ONLY)
    private LocalDateTime updatedAt;
    @JsonProperty(value = "created_at", access = JsonProperty.Access.WRITE_ONLY)
    private LocalDateTime createdAt;
    @JsonProperty(value = "closed_at", access = JsonProperty.Access.WRITE_ONLY)
    private LocalDateTime closedAt;
    @JsonProperty(value = "closed_by", access = JsonProperty.Access.WRITE_ONLY)
    private GitlabUser closedBy;
    @JsonProperty(value = "subscribed", access = JsonProperty.Access.WRITE_ONLY)
    private boolean subscribed;
    @JsonProperty("due_date")
    private LocalDateTime dueDate;
    @JsonProperty(value = "web_url", access = JsonProperty.Access.WRITE_ONLY)
    private String webUrl;
    @JsonProperty(value = "has_tasks", access = JsonProperty.Access.WRITE_ONLY)
    private boolean hasTasks;
    @JsonProperty(value = "epic_id", access = JsonProperty.Access.WRITE_ONLY)
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


    @Override
    public GitlabIssue withHTTPRequestor(GitlabHTTPRequestor requestor) {
        super.withHTTPRequestor(requestor);
        return this;
    }

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

    /*
     * Getters
     */
    public int getProjectId() {
        return projectId;
    }

    public int getId() {
        return id;
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

    public LocalDateTime getDueDate() {
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

    public GitlabIssue withState(String state) {
        this.state = state;
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

    public GitlabIssue withDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
        return this;
    }

    // TODO: Add note and PR feature later?


    @Override
    public String toString() {
        return title;
    }

    @Override
    public int hashCode() {
        return Objects.hash(projectId, id);
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
        return projectId == that.projectId &&
                id == that.id &&
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
}
