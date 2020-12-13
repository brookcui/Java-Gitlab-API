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

/**
 * This class is used to represent the gitlab issue model. It contains a config object inorder to make appropriate
 * http request. all of the fields that tagged with JsonProperty are mapped to fields in the gitlab web page.
 * This class also contains a ProjectQuery Class used to build query and get issues within a project and Query class
 * to get issues.
 * <p>
 * This class implements GitlabModifiableComponent to support create, read, update and delete.
 * <p>
 * Gitlab Web API: https://docs.gitlab.com/ee/api/issues.html
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class GitlabIssue implements GitlabModifiableComponent<GitlabIssue> {

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
     *
     * @param title
     */
    GitlabIssue(@JsonProperty("title") String title) {
        this.title = title;
    }

    /**
     * Issue a HTTP request to the Gitlab API endpoint to create this {@link GitlabIssue} based on
     * the fields in this {@link GitlabIssue} currently
     *
     * @return the created {@link GitlabIssue} component
     * @throws GitlabException if {@link IOException} occurs or the response code is not in [200,400)
     */
    @Override
    public GitlabIssue create() {
        Body body = new Body()
                .putString("title", title)
                .putIntArray("assignee_ids", assignees.stream().mapToInt(GitlabUser::getId).toArray())
                .putStringArray("labels", labels)
                .putString("description", description)
                .putDate("due_date", dueDate);
        return GitlabHttpClient.post(config, String.format("/projects/%d/issues", projectId), body, this);
    }

    /**
     * Issue a HTTP request to the Gitlab API endpoint to delete this {@link GitlabIssue}
     *
     * @return the {@link GitlabIssue} component before deleted
     * @throws GitlabException if {@link IOException} occurs or the response code is not in [200,400)
     */
    @Override
    public GitlabIssue delete() {
        GitlabHttpClient.delete(config, String.format("/projects/%d/issues/%d", projectId, iid));
        return this;
    }

    /**
     * Issue a HTTP request to the Gitlab API endpoint to update this {@link GitlabIssue} based on
     * the fields in this {@link GitlabIssue} currently
     *
     * @return the updated {@link GitlabIssue} component
     * @throws GitlabException if {@link IOException} occurs or the response code is not in [200,400)
     */
    @Override
    public GitlabIssue update() {
        Body body = new Body()
                .putString("title", title)
                .putIntArray("assignee_ids", assignees.stream().mapToInt(GitlabUser::getId).toArray())
                .putString("description", description)
                .putStringArray("labels", labels)
                .putDate("due_date", dueDate);
        return GitlabHttpClient.put(config, String.format("/projects/%d/issues/%d", projectId, iid), body, this);
    }

    /**
     * This method will do a HTTP request and close an open issue.
     * Gitlab Web API: https://docs.gitlab.com/ee/api/issues.html#edit-issue
     *
     * @return {@link GitlabIssue} after it is closed
     * @throws GitlabException if {@link IOException} occurs or the response code is not in [200,400)
     */
    public GitlabIssue close() {
        Body body = new Body().putString("state_event", "close");
        return GitlabHttpClient.put(config, String.format("/projects/%d/issues/%d", projectId, iid), body, this);
    }

    /**
     * This method will do a HTTP request and reopen a closed issue.
     * Gitlab Web API: https://docs.gitlab.com/ee/api/issues.html#edit-issue
     *
     * @return {@link GitlabIssue} after it is closed
     * @throws GitlabException if {@link IOException} occurs or the response code is not in [200,400)
     */
    public GitlabIssue reopen() {
        Body body = new Body().putString("state_event", "reopen");
        return GitlabHttpClient.put(config, String.format("/projects/%d/issues/%d", projectId, iid), body, this);
    }

    /**
     * This method will do a HTTP request and get all related merge requests
     * Gitlab Web API: https://docs.gitlab.com/ee/api/issues.html#list-merge-requests-related-to-issue
     *
     * @return list of {@link GitlabMergeRequest} thats related to current issue
     * @throws GitlabException if {@link IOException} occurs or the response code is not in [200,400)
     */
    public List<GitlabMergeRequest> getRelatedMergeRequests() {
        List<GitlabMergeRequest> mergeRequests = GitlabHttpClient
                .getList(config,
                        String.format("/projects/%d/issues/%d/related_merge_requests", projectId, iid),
                        GitlabMergeRequest[].class);
        mergeRequests.forEach(mergeRequest -> mergeRequest.withProject(getProject()));
        return mergeRequests;
    }

    /**
     * This method will do a HTTP request and get all merge requests that will be closed upon close
     * Gitlab Web API: https://docs.gitlab.com/ee/api/issues.html#list-merge-requests-that-will-close-issue-on-merge
     *
     * @return list of {@link GitlabMergeRequest} that will be closed upon close
     * @throws GitlabException if {@link IOException} occurs or the response code is not in [200,400)
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
     * @return a {@link GitlabProject} created from id
     */
    public GitlabProject getProject() {
        if (project == null) {
            project = GitlabProject.fromId(config, projectId);
        }
        return project;
    }

    /**
     * Get the internal id of the issue
     *
     * @return the internal id of the issue
     */
    public int getIid() {
        return iid;
    }

    /**
     * Get the author of the issue
     *
     * @return author of the issue
     */
    public GitlabUser getAuthor() {
        return author;
    }

    /**
     * Get the description of the issue
     *
     * @return description of the issue
     */
    public String getDescription() {
        return description;
    }

    /**
     * Get the state of the issue
     *
     * @return current state of the issue
     */
    public String getState() {
        return state;
    }

    /**
     * Get a list of assginees of the issue
     *
     * @return list of assginees
     */
    public List<GitlabUser> getAssignees() {
        return assignees;
    }

    /**
     * Get number of up votes of the issue
     *
     * @return number of up votes of the issue
     */
    public int getUpvotes() {
        return upvotes;
    }

    /**
     * Get number of down votes of the issue
     *
     * @return number of down votes of the issue
     */
    public int getDownvotes() {
        return downvotes;
    }

    /**
     * Get the number of merge requests in the issue
     *
     * @return the number of merge requests in the issue
     */
    public int getMergeRequestCount() {
        return mergeRequestCount;
    }

    /**
     * Get the title of the issue
     *
     * @return title of the issue
     */
    public String getTitle() {
        return title;
    }

    /**
     * Get the date when issue is updated
     *
     * @return the date when issue is updated
     */
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    /**
     * Get the date when issue is created
     *
     * @return date when issue is created
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * Get the date when issue is closed
     *
     * @return get the date when issue is closed
     */
    public LocalDateTime getClosedAt() {
        return closedAt;
    }

    /**
     * Get the user who closed the issue
     *
     * @return {@link GitlabUser} who closed the issue
     */
    public GitlabUser getClosedBy() {
        return closedBy;
    }

    /**
     * Get whether current user is subscribed to the issue
     *
     * @return whether current user is subscribed to the issue
     */
    public boolean isSubscribed() {
        return subscribed;
    }

    /**
     * Get the due date of the issue
     *
     * @return due date of the issue
     */
    public LocalDate getDueDate() {
        return dueDate;
    }

    /**
     * Get the web url of the issue
     *
     * @return web url of the issue
     */
    public String getWebUrl() {
        return webUrl;
    }

    /**
     * Get whether current issue have tasks
     *
     * @return whether current issue have tasks
     */
    public boolean hasTasks() {
        return hasTasks;
    }

    /**
     * Get the epic id of the current issue
     *
     * @return epic id of the current issue
     */
    public int getEpicId() {
        return epicId;
    }

    /**
     * Get the project id of the current issue
     *
     * @return project id of the current issue
     */
    public int getProjectId() {
        return projectId;
    }

    /**
     * Get all the labels of the current issue
     *
     * @return all the labels of the current issue
     */
    public List<String> getLabels() {
        return labels;
    }

    /**
     * Get the id of the current issue
     *
     * @return id of the current issue
     */
    public int getId() {
        return id;
    }

    GitlabIssue withProject(GitlabProject project) {
        this.project = project;
        this.projectId = project.getId();
        return this;
    }

    /*
     * Setters
     * There will be no setter for projectId, id, author, upvotes, downvotes, mergeRequestCount, updatedAt, createdAt,
     * closedAt, closedBy, subscribed, webUrl, hasTasks, epicId
     *
     */

    /**
     * Set the description of the current issue
     *
     * @param description new description
     * @return issue with new description
     */
    public GitlabIssue withDescription(String description) {
        this.description = description;
        return this;
    }

    /**
     * Set the assignees of the current issue
     *
     * @param assignees new assignees
     * @return issue with new assignees
     */
    public GitlabIssue withAssignees(List<GitlabUser> assignees) {
        this.assignees = assignees;
        return this;
    }

    /**
     * Set the title of the current issue
     *
     * @param title new title
     * @return issue with new title
     */
    public GitlabIssue withTitle(String title) {
        this.title = title;
        return this;
    }

    /**
     * Set the dueDate of the current issue
     *
     * @param dueDate new dueDate
     * @return issue with new dueDate
     */
    public GitlabIssue withDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
        return this;
    }

    /**
     * Set the labels of the current issue
     *
     * @param labels new labels
     * @return issue with new labels
     */
    public GitlabIssue withLabels(List<String> labels) {
        this.labels = labels;
        return this;
    }

    /**
     * Get the config that is stored in current {@link GitlabUser}
     *
     * @return the config with user detail
     */
    @Override
    public Config getConfig() {
        return config;
    }

    /**
     * Add a config to the current {@link GitlabAPIClient}
     *
     * @param config a config with user details
     * @return {@link GitlabIssue} with the config
     */
    @Override
    public GitlabIssue withConfig(Config config) {
        this.config = config;
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

    @Override
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

    /**
     * Class to query {@link GitlabIssue} in a given {@link GitlabProject}
     * Gitlab Web API: https://docs.gitlab.com/ee/api/issues.html#list-project-issues
     */
    public static class ProjectQuery extends GitlabQuery<GitlabIssue> {
        private final GitlabProject project;

        ProjectQuery(Config config, GitlabProject project) {
            super(config, GitlabIssue[].class);
            this.project = project;
        }

        /**
         * add a parameter to return issues assigned to the given user id. Mutually exclusive with assignee_username.
         * None returns unassigned issues. Any returns issues with an assignee.
         *
         * @param assigneeId id of the assignee
         * @return this {@link ProjectQuery} with the given assignee id
         */
        public ProjectQuery withAssigneeId(int assigneeId) {
            appendInt("assignee_id", assigneeId);
            return this;
        }

        /**
         * add a list of username to return issues assigned to the given username. Similar to assignee_id and mutually
         * exclusive with assignee_id. In GitLab CE, the assignee_username array should only contain a single value.
         * Otherwise, an invalid parameter error is returned.
         *
         * @param assigneeUsernames id of the assignee
         * @return this {@link ProjectQuery} with the given assigneeUsernames
         */
        public ProjectQuery withAssigneeUsernames(List<String> assigneeUsernames) {
            appendStrings("assignee_username", assigneeUsernames);
            return this;
        }

        /**
         * add a author id to return issues created by the given user id. Mutually exclusive with author_username.
         * Combine with scope=all or scope=assigned_to_me
         *
         * @param authorId id of the author
         * @return this {@link ProjectQuery} with the given author id
         */
        public ProjectQuery withAuthorId(int authorId) {
            appendInt("author_id", authorId);
            return this;
        }

        /**
         * add a author id to return issues created by the given user username. Mutually exclusive with
         * author_username. Combine with scope=all or scope=assigned_to_me
         *
         * @param authorUsername username of the author
         * @return this {@link ProjectQuery} with the given author authorUsername
         */
        public ProjectQuery withAuthorUsername(String authorUsername) {
            appendString("author_username", authorUsername);
            return this;
        }

        /**
         * add a whether or not to filter confidential or public issues
         *
         * @param confidential whether or not to filter confidential or public issues
         * @return this {@link ProjectQuery} with the boolean
         */
        public ProjectQuery withConfidential(boolean confidential) {
            appendBoolean("confidential", confidential);
            return this;
        }

        /**
         * Add a date to return issues created on or after the given time.
         *
         * @param createdAfter a date to get issues after this date
         * @return this {@link ProjectQuery} with the date
         */
        public ProjectQuery withCreatedAfter(LocalDateTime createdAfter) {
            appendDateTime("created_after", createdAfter);
            return this;
        }

        /**
         * Add a date to return issues created on or before the given time.
         *
         * @param createdBefore a date to get issues before this date
         * @return this {@link ProjectQuery} with the date
         */
        public ProjectQuery withCreatedBefore(LocalDateTime createdBefore) {
            appendDateTime("created_before", createdBefore);
            return this;
        }

        /**
         * Due date could be 0(no due date), overdue, week, month or next_month_and_previous_two_weeks according to
         * gitlab api.
         *
         * @param dueDate due date
         * @return this {@link ProjectQuery} with the due date
         */
        public ProjectQuery withDueDate(String dueDate) {
            appendString("due_date", dueDate);
            return this;
        }

        /**
         * Return only the issues having the given iid
         *
         * @param iids list of internal ids
         * @return this {@link ProjectQuery} with the iids
         */
        public ProjectQuery withIids(List<Integer> iids) {
            appendInts("iids", iids);
            return this;
        }

        /**
         * The milestone title. None lists all issues with no milestone. Any lists all issues that have an assigned milestone.
         *
         * @param milestone mile stone title
         * @return this {@link ProjectQuery} with the milestone
         */
        public ProjectQuery withMilestone(String milestone) {
            appendString("milestone", milestone);
            return this;

        }

        /**
         * Comma-separated list of label names, issues must have all labels to be returned. None lists all issues with
         * no labels. Any lists all issues with at least one label.
         *
         * @param labels list of labels
         * @return this {@link ProjectQuery} with the list of labels
         */
        public ProjectQuery withLabels(List<String> labels) {
            appendStrings("labels", labels);
            return this;

        }

        /**
         * Return issues reacted by the authenticated user by the given emoji. None returns issues not given a reaction.
         * Any returns issues given at least one reaction.
         *
         * @param myReactionEmoji reaction image
         * @return this {@link ProjectQuery} with the reaction emoji
         */
        public ProjectQuery withMyReactionEmoji(String myReactionEmoji) {
            appendString("my_reaction_emoji", myReactionEmoji);
            return this;
        }

        /**
         * return issues only from non-archived projects. If false, the response returns issues from both archived
         * and non-archived projects. Default is true
         *
         * @param nonArchived whether the respod are arichived
         * @return this {@link ProjectQuery} with the boolean
         */
        public ProjectQuery withNonArchived(boolean nonArchived) {
            appendBoolean("non_archived", nonArchived);
            return this;
        }

        /**
         * Return issues that do not match the parameters supplied. Accepts: labels, milestone, author_id,
         * author_username, assignee_id, assignee_username, my_reaction_emoji
         *
         * @param not whether to include such filter
         * @return this {@link ProjectQuery} with the boolean
         */
        public ProjectQuery withNot(String not) {
            appendString("not", not);
            return this;
        }

        /**
         * Return issues ordered by created_at, updated_at, priority, due_date, relative_position, label_priority,
         * milestone_due, popularity, weight fields. Default is created_at
         *
         * @param orderBy a way to order all of the responds
         * @return this {@link ProjectQuery} with the orderby
         */
        public ProjectQuery withOrderBy(String orderBy) {
            appendString("order_by", orderBy);
            return this;
        }

        /**
         * Return issues for the given scope: created_by_me, assigned_to_me or all. Defaults to created_by_me
         * For versions before 11.0, use the now deprecated created-by-me or assigned-to-me scopes instead.
         *
         * @param scope different scope in gitlab
         * @return this {@link ProjectQuery} with the scope
         */
        public ProjectQuery withScope(String scope) {
            appendString("scope", scope);
            return this;
        }

        /**
         * Search issues against their title and description
         *
         * @param search keyword to be searched
         * @return this {@link ProjectQuery} with the search
         */
        public ProjectQuery withSearch(String search) {
            appendString("search", search);
            return this;
        }

        /**
         * Return issues sorted in asc or desc order. Default is desc
         *
         * @param sort ways to sort the response
         * @return this {@link ProjectQuery} with the sort
         */
        public ProjectQuery withSort(String sort) {
            appendString("sort", sort);
            return this;
        }

        /**
         * Return issues updated on or after the given time.
         *
         * @param updatedAfter a date that all issue should be updated after
         * @return this {@link ProjectQuery} with the date
         */
        public ProjectQuery withUpdatedAfter(LocalDateTime updatedAfter) {
            appendDateTime("updated_after", updatedAfter);
            return this;
        }

        /**
         * Return issues updated on or before the given time.
         *
         * @param updatedBefore a date that all issue should be updated before
         * @return this {@link ProjectQuery} with the date
         */
        public ProjectQuery withUpdatedBefore(LocalDateTime updatedBefore) {
            appendDateTime("updated_before", updatedBefore);
            return this;
        }

        /**
         * Return issues with the specified weight. None returns issues with no weight assigned. Any returns issues
         * with a weight assigned.
         *
         * @param weight weight parameter
         * @return this {@link ProjectQuery} with the weight
         */
        public ProjectQuery withWeight(int weight) {
            appendInt("weight", weight);
            return this;
        }

        /**
         * Add whether or not to return labels with detail
         *
         * @param labelsDetails whether or not to return labels with detail
         * @return this {@link ProjectQuery} with the boolean
         */
        public ProjectQuery withLabelsDetails(boolean labelsDetails) {
            appendBoolean("with_labels_details", labelsDetails);
            return this;
        }

        /**
         * Add pagination on top of the query
         *
         * @param pagination pagination object that defines page number and size
         * @return this {@link ProjectQuery} with the given pagination object
         */
        @Override
        public ProjectQuery withPagination(Pagination pagination) {
            appendPagination(pagination);
            return this;
        }

        /**
         * Get the URL suffix for the HTTP request
         *
         * @return The URL suffix to query {@link GitlabIssue} in the given {@link GitlabProject}
         */
        @Override
        public String getTailUrl() {
            return String.format("/projects/%d/issues", project.getId());
        }

        /**
         * Bind the branch with the given {@link GitlabProject} after the response is parsed
         *
         * @param component - one {@link GitlabIssue} from the response
         */
        @Override
        void bind(GitlabIssue component) {
            component.withProject(project);
        }

    }

    /**
     * Class to query {@link GitlabIssue}
     * Gitlab Web API: https://docs.gitlab.com/ee/api/issues.html#list-issues
     */
    public static class Query extends GitlabQuery<GitlabIssue> {

        Query(Config config) {
            super(config, GitlabIssue[].class);
        }

        /**
         * add a parameter to return issues assigned to the given user id. Mutually exclusive with assignee_username.
         * None returns unassigned issues. Any returns issues with an assignee.
         *
         * @param assigneeId id of the assignee
         * @return this {@link Query} with the given assignee id
         */
        public Query withAssigneeId(int assigneeId) {
            appendInt("assignee_id", assigneeId);
            return this;
        }

        /**
         * add a list of username to return issues assigned to the given username. Similar to assignee_id and mutually
         * exclusive with assignee_id. In GitLab CE, the assignee_username array should only contain a single value.
         * Otherwise, an invalid parameter error is returned.
         *
         * @param assigneeUsernames id of the assignee
         * @return this {@link Query} with the given assigneeUsernames
         */
        public Query withAssigneeUsernames(List<String> assigneeUsernames) {
            appendStrings("assignee_username", assigneeUsernames);
            return this;
        }

        /**
         * add a author id to return issues created by the given user id. Mutually exclusive with author_username.
         * Combine with scope=all or scope=assigned_to_me
         *
         * @param authorId id of the author
         * @return this {@link Query} with the given author id
         */
        public Query withAuthorId(int authorId) {
            appendInt("author_id", authorId);
            return this;
        }

        /**
         * add a author id to return issues created by the given user username. Mutually exclusive with
         * author_username. Combine with scope=all or scope=assigned_to_me
         *
         * @param authorUsername username of the author
         * @return this {@link Query} with the given author authorUsername
         */
        public Query withAuthorUsername(String authorUsername) {
            appendString("author_username", authorUsername);
            return this;
        }

        /**
         * add a whether or not to filter confidential or public issues
         *
         * @param confidential whether or not to filter confidential or public issues
         * @return this {@link Query} with the boolean
         */
        public Query withConfidential(boolean confidential) {
            appendBoolean("confidential", confidential);
            return this;
        }

        /**
         * Add a date to return issues created on or after the given time.
         *
         * @param createdAfter a date to get issues after this date
         * @return this {@link Query} with the date
         */
        public Query withCreatedAfter(LocalDateTime createdAfter) {
            appendDateTime("created_after", createdAfter);
            return this;
        }

        /**
         * Add a date to return issues created on or before the given time.
         *
         * @param createdBefore a date to get issues before this date
         * @return this {@link Query} with the date
         */
        public Query withCreatedBefore(LocalDateTime createdBefore) {
            appendDateTime("created_before", createdBefore);
            return this;
        }

        /**
         * Due date could be 0(no due date), overdue, week, month or next_month_and_previous_two_weeks according to
         * gitlab api.
         *
         * @param dueDate due date
         * @return this {@link Query} with the due date
         */
        public Query withDueDate(String dueDate) {
            appendString("due_date", dueDate);
            return this;
        }

        /**
         * Return only the issues having the given iid
         *
         * @param iids list of internal ids
         * @return this {@link Query} with the iids
         */
        public Query withIids(List<Integer> iids) {
            appendInts("iids", iids);
            return this;

        }

        /**
         * Modify the scope of the search attribute. title, description, or a string joining them with comma.
         * Default is title,description
         *
         * @param in scope of the search
         * @return this {@link Query} with the scope
         */
        public Query withIn(String in) {
            appendString("in", in);
            return this;

        }

        /**
         * Return issues assigned to the given iteration ID. None returns issues that do not belong to an iteration.
         * Any returns issues that belong to an iteration. Mutually exclusive with iteration_title.
         * @param iterationId iteration id
         * @return this {@link Query} with the iteration id
         */
        public Query withIterationId(int iterationId) {
            appendInt("iteration_id", iterationId);
            return this;

        }

        /**
         * Return issues assigned to the given iteration ID. None returns issues that do not belong to an iteration.
         * Any returns issues that belong to an iteration. Mutually exclusive with iteration_title.
         *
         * @param iterationTitle iteration title
         * @return this {@link Query} with the iteration title
         */
        public Query withIterationTitle(String iterationTitle) {

            appendString("iteration_title", iterationTitle);
            return this;

        }

        /**
         * The milestone title. None lists all issues with no milestone. Any lists all issues that have an assigned milestone.
         *
         * @param milestone mile stone title
         * @return this {@link Query} with the milestone
         */
        public Query withMilestone(String milestone) {
            appendString("milestone", milestone);
            return this;

        }

        /**
         * Comma-separated list of label names, issues must have all labels to be returned. None lists all issues with
         * no labels. Any lists all issues with at least one label.
         *
         * @param labels list of labels
         * @return this {@link Query} with the list of labels
         */
        public Query withLabels(List<String> labels) {
            appendStrings("labels", labels);
            return this;

        }

        /**
         * Return issues reacted by the authenticated user by the given emoji. None returns issues not given a reaction.
         * Any returns issues given at least one reaction.
         *
         * @param myReactionEmoji reaction image
         * @return this {@link Query} with the reaction emoji
         */
        public Query withMyReactionEmoji(String myReactionEmoji) {
            appendString("my_reaction_emoji", myReactionEmoji);
            return this;
        }

        /**
         * return issues only from non-archived projects. If false, the response returns issues from both archived
         * and non-archived projects. Default is true
         *
         * @param nonArchived whether the respod are arichived
         * @return this {@link Query} with the boolean
         */
        public Query withNonArchived(boolean nonArchived) {
            appendBoolean("non_archived", nonArchived);
            return this;
        }

        /**
         * Return issues that do not match the parameters supplied. Accepts: labels, milestone, author_id,
         * author_username, assignee_id, assignee_username, my_reaction_emoji
         *
         * @param not whether to include such filter
         * @return this {@link Query} with the boolean
         */
        public Query withNot(String not) {
            appendString("not", not);
            return this;
        }

        /**
         * Return issues ordered by created_at, updated_at, priority, due_date, relative_position, label_priority,
         * milestone_due, popularity, weight fields. Default is created_at
         *
         * @param orderBy a way to order all of the responds
         * @return this {@link Query} with the orderby
         */
        public Query withOrderBy(String orderBy) {
            appendString("order_by", orderBy);
            return this;
        }

        /**
         * Return issues for the given scope: created_by_me, assigned_to_me or all. Defaults to created_by_me
         * For versions before 11.0, use the now deprecated created-by-me or assigned-to-me scopes instead.
         *
         * @param scope different scope in gitlab
         * @return this {@link Query} with the scope
         */
        public Query withScope(String scope) {
            appendString("scope", scope);
            return this;
        }

        /**
         * Search issues against their title and description
         *
         * @param search keyword to be searched
         * @return this {@link Query} with the search
         */
        public Query withSearch(String search) {
            appendString("search", search);
            return this;
        }

        /**
         * Return issues sorted in asc or desc order. Default is desc
         *
         * @param sort ways to sort the response
         * @return this {@link Query} with the sort
         */
        public Query withSort(String sort) {
            appendString("sort", sort);
            return this;
        }

        /**
         * Return issues updated on or after the given time.
         *
         * @param updatedAfter a date that all issue should be updated after
         * @return this {@link Query} with the date
         */
        public Query withUpdatedAfter(LocalDateTime updatedAfter) {
            appendDateTime("updated_after", updatedAfter);
            return this;
        }

        /**
         * Return issues updated on or before the given time.
         *
         * @param updatedBefore a date that all issue should be updated before
         * @return this {@link Query} with the date
         */
        public Query withUpdatedBefore(LocalDateTime updatedBefore) {
            appendDateTime("updated_before", updatedBefore);
            return this;
        }

        /**
         * Return issues with the specified weight. None returns issues with no weight assigned. Any returns issues
         * with a weight assigned.
         *
         * @param weight weight parameter
         * @return this {@link Query} with the weight
         */
        public Query withWeight(int weight) {
            appendInt("weight", weight);
            return this;
        }

        /**
         * Add whether or not to return labels with detail
         *
         * @param labelsDetails whether or not to return labels with detail
         * @return this {@link Query} with the boolean
         */
        public Query withLabelsDetails(boolean labelsDetails) {
            appendBoolean("with_labels_details", labelsDetails);
            return this;
        }

        /**
         * Add pagination on top of the query
         *
         * @param pagination pagination object that defines page number and size
         * @return this {@link Query} with the given pagination object
         */
        @Override
        public Query withPagination(Pagination pagination) {
            appendPagination(pagination);
            return this;
        }

        /**
         * Get the URL suffix for the HTTP request
         *
         * @return The URL suffix to query {@link GitlabIssue}
         */
        @Override
        public String getTailUrl() {
            return "/issues";
        }

        @Override
        void bind(GitlabIssue component) {
        }
    }
}
