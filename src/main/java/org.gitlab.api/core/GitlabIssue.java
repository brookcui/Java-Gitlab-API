package org.gitlab.api.core;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.gitlab.api.http.Body;
import org.gitlab.api.http.Config;
import org.gitlab.api.http.GitlabHttpClient;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class GitlabIssue implements GitlabComponent {

    @JsonIgnore
    private Config config;
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

    @Override
    public Config getConfig() {
        return config;
    }

    @Override
    public GitlabIssue withConfig(Config config) {
        this.config = config;
        return this;
    }

    public List<String> getLabels() {
        return labels;
    }

    GitlabIssue withProject(GitlabProject project) {
        this.project = project;
        this.projectId = project.getId();
        return this;
    }

    // create a new gitlab issue
    public GitlabIssue create() {
        Body body = new Body()
                .putString("title", title)
                .putIntArray("assignee_ids", assignees.stream().mapToInt(GitlabUser::getId).toArray())
                .putStringArray("labels", labels)
                .putString("description", description)
                .putDate("due_date", dueDate);
        return GitlabHttpClient.post(config, String.format("/projects/%d/issues", projectId), body, this);
    }

    public GitlabIssue delete() {
        GitlabHttpClient.delete(config, String.format("/projects/%d/issues/%d", projectId, iid));
        return this;
    }

    public GitlabIssue update() {
        Body body = new Body()
                .putString("title", title)
                .putIntArray("assignee_ids", assignees.stream().mapToInt(GitlabUser::getId).toArray())
                .putString("description", description)
                .putStringArray("labels", labels)
                .putDate("due_date", dueDate);
        return GitlabHttpClient.put(config, String.format("/projects/%d/issues/%d", projectId, iid), body, this);
    }

    public GitlabIssue close() {
        Body body = new Body().putString("state_event", "close");
        return GitlabHttpClient.put(config, String.format("/projects/%d/issues/%d", projectId, iid), body, this);
    }

    public GitlabIssue reopen() {
        Body body = new Body().putString("state_event", "reopen");
        return GitlabHttpClient.put(config, String.format("/projects/%d/issues/%d", projectId, iid), body, this);
    }

    public List<GitlabMergeRequest> getRelatedMergeRequests() {
        List<GitlabMergeRequest> mergeRequests = GitlabHttpClient
                .getList(config,
                        String.format("/projects/%d/issues/%d/related_merge_requests", projectId, iid),
                        GitlabMergeRequest[].class);
        mergeRequests.forEach(mergeRequest -> mergeRequest.withProject(getProject()));
        return mergeRequests;
    }

    /**
     * Todo a bettername
     *
     * @return
     */
    public List<GitlabMergeRequest> getMergeRequestsClosedOnMerge() {
        List<GitlabMergeRequest> mergeRequests = GitlabHttpClient
                .getList(config,
                        String.format("/projects/%d/issues/%d/closed_by", projectId, iid),
                        GitlabMergeRequest[].class);
        mergeRequests.forEach(mergeRequest -> mergeRequest.withProject(getProject()));
        return mergeRequests;
    }

    /**
     * Lazily initialized project field
     *
     * @return
     * @throws IOException
     */
    public GitlabProject getProject() {
        if (project == null) {
            project = GitlabProject.fromId(config, projectId);
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
    // TODO: Add note and PR feature later?

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

    public static class ProjectQuery extends GitlabQuery<GitlabIssue> {
        private final int projectId;

        public ProjectQuery(Config config, int projectId) {
            super(config, GitlabIssue[].class);
            this.projectId = projectId;
        }

        public ProjectQuery withAssigneeId(int assigneeId) {
            appendInt("assignee_id", assigneeId);
            return this;
        }

        public ProjectQuery withAssigneeUsernames(List<String> assigneeUsernames) {
            appendStrings("assignee_username", assigneeUsernames);
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

        public ProjectQuery withConfidential(boolean confidential) {
            appendBoolean("confidential", confidential);
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

        /**
         * Due date could be 0(no due date), overdue, week, month or next_month_and_previous_two_weeks according to
         * gitlab api.
         *
         * @param dueDate due date
         * @return
         */
        public ProjectQuery withDueDate(String dueDate) {
            appendString("due_date", dueDate);
            return this;
        }

        public ProjectQuery withIids(List<Integer> iids) {
            appendInts("iids", iids);
            return this;

        }

        public ProjectQuery withMilestone(String milestone) {
            appendString("milestone", milestone);
            return this;

        }

        public ProjectQuery withLabels(List<String> labels) {
            appendStrings("labels", labels);
            return this;

        }

        public ProjectQuery withMyReactionEmoji(String myReactionEmoji) {
            appendString("my_reaction_emoji", myReactionEmoji);
            return this;
        }

        public ProjectQuery withNonArchived(boolean nonArchived) {
            appendBoolean("non_archived", nonArchived);
            return this;
        }

        public ProjectQuery withNot(String not) {
            appendString("not", not);
            return this;
        }

        public ProjectQuery withOrderBy(String orderBy) {
            appendString("order_by", orderBy);
            return this;
        }

        public ProjectQuery withScope(String scope) {
            appendString("scope", scope);
            return this;
        }

        public ProjectQuery withSearch(String search) {
            appendString("search", search);
            return this;
        }

        public ProjectQuery withSort(String sort) {
            appendString("sort", sort);
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

        public ProjectQuery withWeight(int weight) {
            appendInt("weight", weight);
            return this;
        }

        public ProjectQuery withLabelsDetails(boolean labelsDetails) {
            appendBoolean("with_labels_details", labelsDetails);
            return this;
        }

        @Override
        public ProjectQuery withPagination(Pagination pagination) {
            appendPagination(pagination);
            return this;
        }

        @Override
        public String getUrlPrefix() {
            return String.format("/projects/%d/issues", projectId);
        }

    }

    public static class Query extends GitlabQuery<GitlabIssue> {

        Query(Config config) {
            super(config, GitlabIssue[].class);
        }

        public Query withAssigneeId(int assigneeId) {
            appendInt("assignee_id", assigneeId);
            return this;
        }

        public Query withAssigneeUsernames(List<String> assigneeUsernames) {
            appendStrings("assignee_username", assigneeUsernames);
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

        public Query withConfidential(boolean confidential) {
            appendBoolean("confidential", confidential);
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

        /**
         * Due date could be 0(no due date), overdue, week, month or next_month_and_previous_two_weeks according to
         * gitlab api.
         *
         * @param dueDate due date
         * @return
         */
        public Query withDueDate(String dueDate) {
            appendString("due_date", dueDate);
            return this;
        }

        public Query withIids(List<Integer> iids) {
            appendInts("iids", iids);
            return this;

        }

        public Query withIn(String in) {
            appendString("in", in);
            return this;

        }

        public Query withIterationId(int iterationId) {
            appendInt("iteration_id", iterationId);
            return this;

        }

        public Query withIterationTitle(String iterationTitle) {

            appendString("iteration_title", iterationTitle);
            return this;

        }

        public Query withMilestone(String milestone) {
            appendString("milestone", milestone);
            return this;

        }

        public Query withLabels(List<String> labels) {
            appendStrings("labels", labels);
            return this;

        }

        public Query withMyReactionEmoji(String myReactionEmoji) {
            appendString("my_reaction_emoji", myReactionEmoji);
            return this;
        }

        public Query withNonArchived(boolean nonArchived) {
            appendBoolean("non_archived", nonArchived);
            return this;
        }

        public Query withNot(String not) {
            appendString("not", not);
            return this;
        }

        public Query withOrderBy(String orderBy) {
            appendString("order_by", orderBy);
            return this;
        }

        public Query withScope(String scope) {
            appendString("scope", scope);
            return this;
        }

        public Query withSearch(String search) {
            appendString("search", search);
            return this;
        }

        public Query withSort(String sort) {
            appendString("sort", sort);
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

        public Query withWeight(int weight) {
            appendInt("weight", weight);
            return this;
        }

        public Query withLabelsDetails(boolean labelsDetails) {
            appendBoolean("with_labels_details", labelsDetails);
            return this;
        }

        @Override
        public Query withPagination(Pagination pagination) {
            appendPagination(pagination);
            return this;
        }

        @Override
        public String getUrlPrefix() {
            return "/issues";
        }
    }

}
