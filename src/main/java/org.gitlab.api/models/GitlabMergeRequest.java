package org.gitlab.api.models;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.gitlab.api.core.Pagination;
import org.gitlab.api.http.GitlabHTTPRequestor;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY,
        getterVisibility = JsonAutoDetect.Visibility.NONE
)
public class GitlabMergeRequest extends GitlabComponent {
    @JsonProperty(value = "id", access = JsonProperty.Access.WRITE_ONLY)
    private int id; // required, url of the project

    @JsonProperty(value = "iid", access = JsonProperty.Access.WRITE_ONLY)
    private int iid;
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
    @JsonProperty(value = "web_url", access = JsonProperty.Access.WRITE_ONLY)
    private String webUrl;
    @JsonProperty(value = "target_branch")
    private String targetBranch; // required
    @JsonProperty(value = "source_branch")
    private String sourceBranch; // required

    @JsonIgnore
    private GitlabProject project;

    GitlabMergeRequest withProject(GitlabProject project) {
        this.project = project;
        return this;
    }

    public GitlabMergeRequest(@JsonProperty("source_branch") String sourceBranch,
                              @JsonProperty("target_branch") String targetBranch,
                              @JsonProperty("title") String title) {
        this.sourceBranch = sourceBranch;
        this.targetBranch = targetBranch;
        this.title = title;
    }

    @Override
    public GitlabMergeRequest withHTTPRequestor(GitlabHTTPRequestor requestor) {
        super.withHTTPRequestor(requestor);
        return this;
    }

    // create a new gitlab issue
    public GitlabMergeRequest create() throws IOException {
        return getHTTPRequestor().post(String.format("/projects/%d/merge_requests", project.getId()), this);
    }

    public GitlabMergeRequest delete() throws IOException {
        getHTTPRequestor().delete(String.format("/projects/%d/merge_requests/%d", project.getId(), getIid()));
        return this;
    }

    public GitlabMergeRequest update() throws IOException {
        return getHTTPRequestor().put(String.format("/projects/%d/merge_requests/%d", project.getId(), getIid()), this);
    }

    public List<GitlabUser> getAllParticipants() throws IOException {
        return null; // TODO
    }

    public List<GitlabUser> getAllParticipants(Pagination pagination) throws IOException {
        return null; // TODO
    }

    public List<GitlabCommit> getAllCommits() throws IOException {
        return null; // TODO
    }

    public List<GitlabCommit> getAllCommits(Pagination pagination) throws IOException {
        return null; // TODO
    }

    public List<GitlabIssue> getAllIssues() throws IOException {
        return null; // TODO
    }

    public List<GitlabIssue> getAllIssues(Pagination pagination) throws IOException {
        return null; // TODO
    }

    public GitlabMergeRequest accept() throws IOException {
        return this;
    }

    public GitlabMergeRequest approve() throws IOException {
        return this;
    }

    public GitlabMergeRequest decline() throws IOException {
        return this;
    }

    public GitlabMergeRequest withTitle(String title) {
        this.title = title;
        return this;
    }
    //TODO: diff??


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

    public String getWebUrl() {
        return webUrl;
    }

    public String getTargetBranch() {
        return targetBranch;
    }

    public String getSourceBranch() {
        return sourceBranch;
    }

    public GitlabProject getProject() {
        return project;
    }

    public int getId() {
        return id;
    }

    public int getIid() {
        return iid;
    }
}
