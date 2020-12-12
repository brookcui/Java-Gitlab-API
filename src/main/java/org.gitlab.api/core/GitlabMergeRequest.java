package org.gitlab.api.core;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.gitlab.api.http.Body;
import org.gitlab.api.http.Config;
import org.gitlab.api.http.GitlabHttpClient;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class GitlabMergeRequest implements GitlabModifiableComponent<GitlabMergeRequest> {

    @JsonProperty("source_branch")
    private final String sourceBranch; // required

    @JsonProperty("id")
    private int id; // required, url of the project
    @JsonProperty("iid")
    private int iid;
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
    @JsonProperty("labels")
    private List<String> labels = new ArrayList<>(); // required
    @JsonIgnore
    private GitlabProject project;
    @JsonIgnore
    private Config config;


    GitlabMergeRequest(@JsonProperty("source_branch") String sourceBranch,
                       @JsonProperty("target_branch") String targetBranch,
                       @JsonProperty("title") String title) {
        this.sourceBranch = sourceBranch;
        this.targetBranch = targetBranch;
        this.title = title;
    }

    /**
     * Invoke the http request and create the current {@link GitlabMergeRequest}
     *
     * @return {@link GitlabMergeRequest} that's been created
     */
    @Override
    public GitlabMergeRequest create() {
        Body body = new Body()
                .putString("source_branch", sourceBranch)
                .putString("target_branch", targetBranch)
                .putString("title", title)
                .putIntArray("assignee_ids", assignees.stream().mapToInt(GitlabUser::getId).toArray())
                .putString("description", description)
                .putStringArray("labels", labels);

        return GitlabHttpClient
                .post(config, String.format("/projects/%d/merge_requests", projectId), body,
                        this);
    }

    /**
     * Invoke the http request and delete the current {@link GitlabMergeRequest}
     *
     * @return {@link GitlabMergeRequest} that's been created
     */
    @Override
    public GitlabMergeRequest delete() {
        GitlabHttpClient.delete(config, String.format("/projects/%d/merge_requests/%d", projectId, iid));
        return this;
    }

    /**
     * Invoke the http request and update the current {@link GitlabMergeRequest}
     *
     * @return {@link GitlabMergeRequest} that's been created
     */
    @Override
    public GitlabMergeRequest update() {
        Body body = new Body()
                .putString("target_branch", targetBranch)
                .putString("title", title)
                .putIntArray("assignee_ids", assignees.stream().mapToInt(GitlabUser::getId).toArray())
                .putString("description", description)
                .putStringArray("labels", labels);
        return GitlabHttpClient
                .put(config, String.format("/projects/%d/merge_requests/%d", projectId, iid), body, this);
    }

    /**
     * Get a list of {@link GitlabUser} that participated in the current merge request
     *
     * @return list of {@link GitlabUser} that participated in the current merge request
     */
    public List<GitlabUser> getAllParticipants() {
        return GitlabHttpClient.getList(config,
                String.format("/projects/%d/merge_requests/%d/participants", projectId, iid), GitlabUser[].class);
    }

    /**
     * Get a list of {@link GitlabCommit} that commits in the current merge request
     *
     * @return list of {@link GitlabCommit} in the current merge request
     */
    public List<GitlabCommit> getAllCommits() {
        return GitlabHttpClient.getList(config, String
                .format("/projects/%d/merge_requests/%d/commits", projectId, iid), GitlabCommit[].class);
    }

    /**
     * Get a list of {@link GitlabIssue} that will be closed after commit is merged
     *
     * @return list of {@link GitlabIssue} that will be closed after commit is merged
     */
    public List<GitlabIssue> getAllIssuesClosedByMerge() {
        return GitlabHttpClient.getList(config, String
                .format("/projects/%d/merge_requests/%d/closes_issues", projectId, iid), GitlabIssue[].class);
    }

    /**
     * Accept the current merge request
     *
     * @return {@link GitlabMergeRequest} after merge request has been accepted
     */
    public GitlabMergeRequest accept() {
        return GitlabHttpClient.put(config, String
                .format("/projects/%d/merge_requests/%d/merge", projectId, iid), null, this);
    }

    /**
     * Approve the current merge request
     *
     * @return {@link GitlabMergeRequest} after merge request has been approved
     */
    public GitlabMergeRequest approve() {
        return GitlabHttpClient.post(config, String
                .format("/projects/%d/merge_requests/%d/approve", projectId, iid), null, this);
    }

    /**
     * Decline the current merge request
     *
     * @return {@link GitlabMergeRequest} after merge request has been decline
     */
    public GitlabMergeRequest decline() {
        return GitlabHttpClient.post(config, String
                .format("/projects/%d/merge_requests/%d/unapprove", projectId, iid), null, this);
    }

    /**
     * Get the current project ID 
     * @return
     */
    public int getProjectId() {
        return projectId;
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
        if (project == null) {
            project = GitlabProject.fromId(config, projectId);
        }
        return project;
    }

    public int getId() {
        return id;
    }

    public int getIid() {
        return iid;
    }

    GitlabMergeRequest withProject(GitlabProject project) {
        Objects.requireNonNull(project);
        this.project = project;
        this.projectId = project.getId();
        return this;
    }

    public GitlabMergeRequest withTitle(String title) {
        this.title = title;
        return this;
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

    @Override
    public Config getConfig() {
        return config;
    }

    @Override
    public GitlabMergeRequest withConfig(Config config) {
        this.config = config;
        return this;
    }

    public static class ProjectQuery extends GitlabQuery<GitlabMergeRequest> {
        private final GitlabProject project;

        ProjectQuery(Config config, GitlabProject project) {
            super(config, GitlabMergeRequest[].class);
            this.project = project;
        }

        @Override
        public ProjectQuery withPagination(Pagination pagination) {
            appendPagination(pagination);
            return this;
        }

        public ProjectQuery withId(String id) {
            appendString("id", id);
            return this;
        }

        public ProjectQuery withIids(List<Integer> iids) {
            appendInts("iids[]", iids);
            return this;
        }

        public ProjectQuery withState(String state) {
            appendString("state", state);
            return this;
        }

        public ProjectQuery withOrderBy(String orderBy) {
            appendString("order_by", orderBy);
            return this;
        }

        public ProjectQuery withSort(String sort) {
            appendString("sort", sort);
            return this;
        }

        public ProjectQuery withMilestone(String milestone) {
            appendString("milestone", milestone);
            return this;
        }

        public ProjectQuery withView(String view) {
            appendString("view", view);
            return this;
        }

        public ProjectQuery withLabels(List<String> labels) {
            appendStrings("labels", labels);
            return this;
        }

        public ProjectQuery withWithLabelsDetails(boolean withLabelsDetails) {
            appendBoolean("with_labels_details", withLabelsDetails);
            return this;
        }

        public ProjectQuery withWithMergeStatusRecheck(boolean withMergeStatusRecheck) {
            appendBoolean("with_merge_status_recheck", withMergeStatusRecheck);
            return this;
        }

        public ProjectQuery withCreatedAfter(LocalDateTime createdAfter) {
            appendDateTime("created_after", createdAfter);
            return this;
        }

        public ProjectQuery withCreatedBefore(LocalDateTime createdBefore) {
            appendDateTime("created_before", createdBefore);
            return this;
        }

        public ProjectQuery withUpdatedAfter(LocalDateTime updatedAfter) {
            appendDateTime("updated_after", updatedAfter);
            return this;
        }

        public ProjectQuery withUpdatedBefore(LocalDateTime updatedBefore) {
            appendDateTime("updated_before", updatedBefore);
            return this;
        }

        public ProjectQuery withScope(String scope) {
            appendString("scope", scope);
            return this;
        }


        public ProjectQuery withAuthorId(int authorId) {
            appendInt("author_id", authorId);
            return this;
        }

        public ProjectQuery withAuthorUsername(String authorUsername) {
            appendString("author_username", authorUsername);
            return this;
        }

        public ProjectQuery withAssigneeId(int assigneeId) {
            appendInt("assignee_id", assigneeId);
            return this;
        }

        public ProjectQuery withApproverIds(List<Integer> approverIds) {
            appendInts("approver_ids", approverIds);
            return this;
        }

        public ProjectQuery withApprovedByIds(String approvedByIds) {
            appendString("approved_by_ids", approvedByIds);
            return this;
        }

        public ProjectQuery withMyReactionEmoji(String myReactionEmoji) {
            appendString("my_reaction_emoji", myReactionEmoji);
            return this;
        }

        public ProjectQuery withSourceBranch(String sourceBranch) {
            appendString("source_branch", sourceBranch);
            return this;
        }

        public ProjectQuery withTargetBranch(String targetBranch) {
            appendString("target_branch", targetBranch);
            return this;
        }

        public ProjectQuery withSearch(String search) {
            appendString("search", search);
            return this;
        }

        public ProjectQuery withWip(String wip) {
            appendString("wip", wip);
            return this;
        }

        @Override
        public String getUrlSuffix() {
            return String.format("/projects/%d/merge_requests", project.getId());
        }

        @Override
        void bind(GitlabMergeRequest component) {
            component.withProject(project);
        }

    }

    public static class Query extends GitlabQuery<GitlabMergeRequest> {

        Query(Config config) {
            super(config, GitlabMergeRequest[].class);
        }

        @Override
        public Query withPagination(Pagination pagination) {
            appendPagination(pagination);
            return this;
        }

        public Query withState(String state) {
            appendString("state", state);
            return this;
        }

        public Query withOrderBy(String orderBy) {
            appendString("order_by", orderBy);
            return this;
        }

        public Query withSort(String sort) {
            appendString("sort", sort);
            return this;
        }

        public Query withMilestone(String milestone) {
            appendString("milestone", milestone);
            return this;
        }

        public Query withView(String view) {
            appendString("view", view);
            return this;
        }

        public Query withLabels(List<String> labels) {
            appendStrings("labels", labels);
            return this;
        }

        public Query withWithLabelsDetails(boolean withLabelsDetails) {
            appendBoolean("with_labels_details", withLabelsDetails);
            return this;
        }

        public Query withWithMergeStatusRecheck(boolean withMergeStatusRecheck) {
            appendBoolean("with_merge_status_recheck", withMergeStatusRecheck);
            return this;
        }

        public Query withCreatedAfter(LocalDateTime createdAfter) {
            appendDateTime("created_after", createdAfter);
            return this;
        }

        public Query withCreatedBefore(LocalDateTime createdBefore) {
            appendDateTime("created_before", createdBefore);
            return this;
        }

        public Query withUpdatedAfter(LocalDateTime updatedAfter) {
            appendDateTime("updated_after", updatedAfter);
            return this;
        }

        public Query withUpdatedBefore(LocalDateTime updatedBefore) {
            appendDateTime("updated_before", updatedBefore);
            return this;
        }

        public Query withScope(String scope) {
            appendString("scope", scope);
            return this;
        }

        public Query withAuthorId(int authorId) {
            appendInt("author_id", authorId);
            return this;
        }

        public Query withAuthorUsername(String authorUsername) {
            appendString("author_username", authorUsername);
            return this;
        }

        public Query withAssigneeId(int assigneeId) {
            appendInt("assignee_id", assigneeId);
            return this;
        }

        public Query withApproverIds(List<Integer> approverIds) {
            appendInts("approver_ids", approverIds);
            return this;
        }

        public Query withApprovedByIds(List<Integer> approvedByIds) {
            appendInts("approved_by_ids", approvedByIds);
            return this;
        }

        public Query withMyReactionEmoji(String myReactionEmoji) {
            appendString("my_reaction_emoji", myReactionEmoji);
            return this;
        }

        public Query withSourceBranch(String sourceBranch) {
            appendString("source_branch", sourceBranch);
            return this;
        }

        public Query withTargetBranch(String targetBranch) {
            appendString("target_branch", targetBranch);
            return this;
        }

        public Query withSearch(String search) {
            appendString("search", search);
            return this;
        }

        public Query withIn(String in) {
            appendString("in", in);
            return this;
        }

        public Query withWip(String wip) {
            appendString("wip", wip);
            return this;
        }

        public Query withNot(String not) {
            appendString("not", not);
            return this;
        }

        public Query withEnvironment(String environment) {
            appendString("environment", environment);
            return this;
        }

        public Query withDeployedBefore(LocalDateTime deployedBefore) {
            appendDateTime("deployed_before", deployedBefore);
            return this;
        }

        public Query withDeployedAfter(LocalDateTime deployedAfter) {
            appendDateTime("deployed_after", deployedAfter);
            return this;
        }

        @Override
        public String getUrlSuffix() {
            return "/merge_requests";
        }

        @Override
        void bind(GitlabMergeRequest component) {

        }
    }

}
