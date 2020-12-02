package org.gitlab.api.models;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.gitlab.api.core.Pagination;
import org.gitlab.api.http.Body;
import org.gitlab.api.http.GitlabHTTPRequestor;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY,
        getterVisibility = JsonAutoDetect.Visibility.NONE
)
public class GitlabMergeRequest extends GitlabComponent {
    @JsonProperty("id")
    private int id; // required, url of the project

    @JsonProperty("iid")
    private int iid;
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
    @JsonProperty("web_url")
    private String webUrl;
    @JsonProperty("target_branch")
    private String targetBranch; // required
    @JsonProperty("source_branch")
    private final String sourceBranch; // required
    @JsonProperty("labels")
    private List<String> labels = new ArrayList<>(); // required
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
        Body body = new Body()
                .putString("source_branch", sourceBranch)
                .putString("target_branch", targetBranch)
                .putString("title", title)
                .putIntArray("assignee_ids", assignees.stream().mapToInt(GitlabUser::getId).toArray())
                .putString("description", description)
                .putStringArray("labels", labels);

        return getHTTPRequestor().post(String.format("/projects/%d/merge_requests", project.getId()), body, this);
    }

    public GitlabMergeRequest delete() throws IOException {
        getHTTPRequestor().delete(String.format("/projects/%d/merge_requests/%d", project.getId(), iid));
        return this;
    }

    public GitlabMergeRequest update() throws IOException {
        Body body = new Body()
                .putString("target_branch", targetBranch)
                .putString("title", title)
                .putIntArray("assignee_ids", assignees.stream().mapToInt(GitlabUser::getId).toArray())
                .putString("description", description)
                .putStringArray("labels", labels);
        return getHTTPRequestor()
                .put(String.format("/projects/%d/merge_requests/%d", project.getId(), iid), body, this);
    }

    public List<GitlabUser> getAllParticipants() throws IOException {
        return getHTTPRequestor().getList(String
                .format("/projects/%d/merge_requests/%d/participants", project.getId(), iid), GitlabUser[].class);
    }

    public List<GitlabUser> getAllParticipants(Pagination pagination) throws IOException {
        return null; // TODO
    }

    public List<GitlabCommit> getAllCommits() throws IOException {
        return getHTTPRequestor().getList(String
                .format("/projects/%d/merge_requests/%d/commits", project.getId(), iid), GitlabCommit[].class);
    }

    public List<GitlabCommit> getAllCommits(Pagination pagination) throws IOException {
        return null; // TODO
    }

    public List<GitlabIssue> getAllIssues() throws IOException {
        return getHTTPRequestor().getList(String
                .format("/projects/%d/merge_requests/%d/closes_issues", project.getId(), iid), GitlabIssue[].class);
    }

    public List<GitlabIssue> getAllIssues(Pagination pagination) throws IOException {
        return null; // TODO
    }

    public GitlabMergeRequest accept() throws IOException {
        return getHTTPRequestor().put(String
                .format("/projects/%d/merge_requests/%d/merge", project.getId(), iid), null, this);
    }

    public GitlabMergeRequest approve() throws IOException {
        return getHTTPRequestor().post(String
                .format("/projects/%d/merge_requests/%d/approve", project.getId(), iid), null, this);
    }

    public GitlabMergeRequest decline() throws IOException {
        return getHTTPRequestor().post(String
                .format("/projects/%d/merge_requests/%d/unapprove", project.getId(), iid), null, this);
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

    public GitlabMergeRequest withDescription(String description) {
        this.description = description;
        return this;
    }

    public GitlabMergeRequest withAssignees(List<GitlabUser> assignees) {
        this.assignees = assignees;
        return this;
    }

    public GitlabMergeRequest withTargetBranch(String targetBranch) {
        this.targetBranch = targetBranch;
        return this;
    }

    public List<String> getLabels() {
        return labels;
    }

    @Override
    public String toString() {
        return "GitlabMergeRequest{" +
                "id=" + id +
                ", iid=" + iid +
                ", author=" + author +
                ", description=" + description +
                ", state=" + state +
                ", assignees=" + assignees +
                ", upvotes=" + upvotes +
                ", downvotes=" + downvotes +
                ", mergeRequestCount=" + mergeRequestCount +
                ", title=" + title +
                ", updatedAt=" + updatedAt +
                ", createdAt=" + createdAt +
                ", closedAt=" + closedAt +
                ", closedBy=" + closedBy +
                ", subscribed=" + subscribed +
                ", webUrl=" + webUrl +
                ", targetBranch=" + targetBranch +
                ", sourceBranch=" + sourceBranch +
                ", labels=" + labels +
                '}';
    }
}
