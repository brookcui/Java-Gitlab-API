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
public class GitlabMergeRequest extends GitlabComponent{
    private int id; // required, url of the project
    private int iid;
    private int projectId;

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

    private String targetBranch; // required
    private String sourceBranch; // required

    @JsonIgnore
    private final GitlabProject project;

    public GitlabMergeRequest(@JsonProperty("project") GitlabProject project,
                              @JsonProperty("source_branch") String sourceBranch,
                              @JsonProperty("target_branch") String targetBranch,
                              @JsonProperty("title") String title) {
        this.project = project;
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
        return this; // TODO
    }

    public GitlabMergeRequest delete() throws IOException {
        return this; // TODO
    }

    public GitlabMergeRequest update() throws IOException {
        return this; // TODO
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

    //TODO: diff??

}
