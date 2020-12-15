package org.gitlab.api;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreType;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * This class serves as instance of Gitlab component Issue.
 *
 * To create, update, or delete this issue, call {@code create()},
 * {@code update()}, or {@code delete()} explicitly.
 *
 * This supports query for issues globally or within {@link GitlabProject}.
 * See {@link Query} and {@link ProjectQuery} respectively.
 *
 * Gitlab Web API: https://docs.gitlab.com/ee/api/issues.html
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class GitlabIssue extends GitlabComponent {
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
    @JsonDeserialize(using = DateUtil.ZonedDeserializer.class)
    @JsonSerialize(using = DateUtil.ZonedSerializer.class)
    private ZonedDateTime updatedAt;
    @JsonProperty("created_at")
    @JsonDeserialize(using = DateUtil.ZonedDeserializer.class)
    @JsonSerialize(using = DateUtil.ZonedSerializer.class)
    private ZonedDateTime createdAt;
    @JsonProperty("closed_at")
    @JsonDeserialize(using = DateUtil.ZonedDeserializer.class)
    @JsonSerialize(using = DateUtil.ZonedSerializer.class)
    private ZonedDateTime closedAt;
    @JsonProperty("closed_by")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
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
     * Constructs the {@link GitlabIssue} with title.
     *
     * @param title this issue title
     */
    GitlabIssue(@JsonProperty("title") String title) {
        this.title = title;
    }

    /**
     * Returns a string representation of this {@link GitlabIssue} in the
     * format of Gitlab component type and project id and issue title.
     *
     * @return a string representation of this {@link GitlabIssue}
     */
    @Override
    public String toString() {
        return "GitlabIssue{" +
                       "iid=" + iid +
                       ", title=" + title +
                       '}';
    }

    /**
     * Returns the hash code value for this {@link GitlabIssue} identified by
     * its belonged project and issue id.
     *
     * @return a hash code value for this object
     */
    @Override
    public int hashCode() {
        return Objects.hash(projectId, iid);
    }

    /**
     * Compares the specified {@code Object} with this {@link GitlabIssue}
     * for equality. Note that two {@link GitlabIssue}s are equal if and only
     * if they belong to the same project and have the same issue id.
     *
     * @param o object to be compared for equality with this {@link GitlabIssue}
     * @return true if the specified Object is equal to this {@link GitlabIssue}
     */
    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof GitlabIssue)) {
            return false;
        }
        GitlabIssue that = (GitlabIssue) o;
        return projectId == that.projectId && iid == that.iid;
    }

    /**
     * Issues a HTTP request to Gitlab API endpoint to create an issue for its
     * belonged project based on this {@link GitlabIssue}.
     *
     * @return the created {@link GitlabIssue} instance
     * @throws GitlabException if {@link IOException} occurs or API endpoint fails
     * to give a valid response (response code within [200,400))
     */
    public GitlabIssue create() {
        Body body = new Body()
                .putString("title", title)
                .putIntArray("assignee_ids", assignees.stream().mapToInt(GitlabUser::getId).toArray())
                .putStringArray("labels", labels)
                .putString("description", description)
                .putDate("due_date", dueDate);
        return httpClient.post(String.format("/projects/%d/issues", projectId), body, this);
    }

    /**
     * Issues a HTTP request to Gitlab API endpoint to delete this
     * {@link GitlabIssue} from its belonged project based on issue id.
     *
     * @return the {@link GitlabIssue} instance before deleted
     * @throws GitlabException if {@link IOException} occurs or API endpoint fails
     * to give a valid response (response code within [200,400))
     */
    public GitlabIssue delete() {
        httpClient.delete(String.format("/projects/%d/issues/%d", projectId, iid));
        return this;
    }

    /**
     * Issues a HTTP request to Gitlab API endpoint to update this
     * {@link GitlabIssue} based on its current fields.
     *
     * @return the updated {@link GitlabIssue} instance
     * @throws GitlabException if {@link IOException} occurs or API endpoint fails
     * to give a valid response (response code within [200,400))
     */
    public GitlabIssue update() {
        Body body = new Body()
                .putString("title", title)
                .putIntArray("assignee_ids", assignees.stream().mapToInt(GitlabUser::getId).toArray())
                .putString("description", description)
                .putStringArray("labels", labels)
                .putDate("due_date", dueDate);
        return httpClient.put(String.format("/projects/%d/issues/%d", projectId, iid), body, this);
    }

    /**
     * Issues a HTTP request to Gitlab API endpoint to close this
     * {@link GitlabIssue}.
     *
     * Gitlab Web API: https://docs.gitlab.com/ee/api/issues.html#edit-issue
     *
     * @return {@link GitlabIssue} after it is closed
     * @throws GitlabException if {@link IOException} occurs or API endpoint fails
     * to give a valid response (response code within [200,400))
     */
    public GitlabIssue close() {
        Body body = new Body().putString("state_event", "close");
        return httpClient.put(String.format("/projects/%d/issues/%d", projectId, iid), body, this);
    }

    /**
     * Issues a HTTP request to Gitlab API endpoint to reopen this
     * {@link GitlabIssue}.
     *
     * Gitlab Web API: https://docs.gitlab.com/ee/api/issues.html#edit-issue
     *
     * @return {@link GitlabIssue} after it is closed
     * @throws GitlabException if {@link IOException} occurs or API endpoint fails
     * to give a valid response (response code within [200,400))
     */
    public GitlabIssue reopen() {
        Body body = new Body().putString("state_event", "reopen");
        return httpClient.put(String.format("/projects/%d/issues/%d", projectId, iid), body,
                this);
    }

    /**
     * Issues a HTTP request to Gitlab API endpoint to get all related
     * {@link GitlabMergeRequest} within this {@link GitlabIssue}.
     *
     * Gitlab Web API: https://docs.gitlab.com/ee/api/issues.html#list-merge-requests-related-to-issue
     *
     * @return list of {@link GitlabMergeRequest} thats related to current issue
     * @throws GitlabException if {@link IOException} occurs or the response code is not in [200,400)
     */
    public List<GitlabMergeRequest> getRelatedMergeRequests() {
        List<GitlabMergeRequest> mergeRequests = httpClient.getList(
                String.format("/projects/%d/issues/%d/related_merge_requests", projectId, iid),
                GitlabMergeRequest[].class);
        mergeRequests.forEach(mergeRequest -> mergeRequest.withProject(getProject()));
        return mergeRequests;
    }

    /**
     * Issues a HTTP request to Gitlab API endpoint to get all related
     * {@link GitlabMergeRequest} that will be close by this
     * {@link GitlabIssue} on merge.
     *
     * Gitlab Web API: https://docs.gitlab.com/ee/api/issues.html#list-merge-requests-that-will-close-issue-on-merge
     *
     * @return list of {@link GitlabMergeRequest} that will close this {@link GitlabIssue} on merge
     * @throws GitlabException if {@link IOException} occurs or the response code is not in [200,400)
     */
    public List<GitlabMergeRequest> getMergeRequestsClosedOnMerge() {
        List<GitlabMergeRequest> mergeRequests = httpClient
                .getList(String.format("/projects/%d/issues/%d/closed_by", projectId, iid),
                        GitlabMergeRequest[].class);
        mergeRequests.forEach(mergeRequest -> mergeRequest.withProject(getProject()));
        return mergeRequests;
    }

    /**
     * Returns the project that this issue belongs to.
     *
     * @return the {@link GitlabProject}
     */
    public GitlabProject getProject() {
        if (project == null) {
            project = GitlabProject.fromId(httpClient, projectId);
        }
        return project;
    }

    /**
     * Returns the internal id of this issue.
     *
     * @return the internal id of this issue
     */
    public int getIid() {
        return iid;
    }

    /**
     * Returns the author of this issue.
     *
     * @return author of this issue
     */
    public GitlabUser getAuthor() {
        return author;
    }

    /**
     * Returns the description of this issue.
     *
     * @return description of this issue
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns the state of this issue.
     *
     * @return the state string of this issue
     */
    public String getState() {
        return state;
    }

    /**
     * Returns a list of assignees of this issue.
     *
     * @return a list of assignees
     */
    public List<GitlabUser> getAssignees() {
        return assignees;
    }

    /**
     * Returns the number of up votes of this issue.
     *
     * @return the number of up votes of this issue
     */
    public int getUpvotes() {
        return upvotes;
    }

    /**
     * Returns the number of down votes of this issue.
     *
     * @return the number of down votes of this issue
     */
    public int getDownvotes() {
        return downvotes;
    }

    /**
     * Returns the number of merge requests in this issue.
     *
     * @return the number of merge requests in this issue
     */
    public int getMergeRequestCount() {
        return mergeRequestCount;
    }

    /**
     * Returns the title of this issue.
     *
     * @return title of this issue
     */
    public String getTitle() {
        return title;
    }

    /**
     * Returns the date when this issue was updated.
     *
     * @return the date when this issue was updated
     */
    public ZonedDateTime getUpdatedAt() {
        return updatedAt;
    }

    /**
     * Returns the date when this issue was created.
     *
     * @return the date when this issue was created
     */
    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * Returns the date when this issue was closed.
     *
     * @return the date when issue was closed
     */
    public ZonedDateTime getClosedAt() {
        return closedAt;
    }

    /**
     * Returns the user who closed this issue.
     *
     * @return {@link GitlabUser} who closed this issue
     */
    public GitlabUser getClosedBy() {
        return closedBy;
    }

    /**
     * Tests if current user has subscribed to this issue.
     *
     * @return true if current user has subscribed to this issue
     */
    public boolean isSubscribed() {
        return subscribed;
    }

    /**
     * Returns the due date of this issue.
     *
     * @return due date of this issue
     */
    public LocalDate getDueDate() {
        return dueDate;
    }

    /**
     * Returns the web url of this issue.
     *
     * @return web url of this issue
     */
    public String getWebUrl() {
        return webUrl;
    }

    /**
     * Tests if this issue has tasks
     *
     * @return true if this issue has tasks
     */
    public boolean hasTasks() {
        return hasTasks;
    }

    /**
     * Returns the epic id of this issue.
     *
     * @return epic id of this issue
     */
    public int getEpicId() {
        return epicId;
    }

    /**
     * Returns the project id of this issue.
     *
     * @return project id of this issue
     */
    public int getProjectId() {
        return projectId;
    }

    /**
     * Returns the list of labels of this issue.
     *
     * @return a list of labels of this issue
     */
    public List<String> getLabels() {
        return labels;
    }

    /**
     * Returns the id of this issue.
     *
     * @return id of this issue
     */
    public int getId() {
        return id;
    }

    /**
     * Attaches a project to this {@link GitlabIssue}.
     *
     * @param project the project to be attached
     * @return this {@link GitlabIssue}
     */
    GitlabIssue withProject(GitlabProject project) {
        Objects.requireNonNull(project);
        this.project = project;
        this.projectId = project.getId();
        return this;
    }

    /*
     * Note that there will be no setters for projectId, id, author, upvotes,
     * downvotes, mergeRequestCount, updatedAt, createdAt, closedAt, closedBy,
     * subscribed, webUrl, hasTasks, epicId because they are not modifiable.
     */

    /**
     * Sets the description of this issue.
     *
     * @param description new description
     * @return issue with new description
     */
    public GitlabIssue withDescription(String description) {
        this.description = description;
        return this;
    }

    /**
     * Sets the assignees of this issue.
     *
     * @param assignees new assignees
     * @return issue with new assignees
     */
    public GitlabIssue withAssignees(List<GitlabUser> assignees) {
        this.assignees = assignees;
        return this;
    }

    /**
     * Sets the title of this issue.
     *
     * @param title new title
     * @return issue with new title
     */
    public GitlabIssue withTitle(String title) {
        this.title = title;
        return this;
    }

    /**
     * Sets the dueDate of this issue.
     *
     * @param dueDate new dueDate
     * @return issue with new dueDate
     */
    public GitlabIssue withDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
        return this;
    }

    /**
     * Sets the labels of this issue.
     *
     * @param labels new labels
     * @return issue with new labels
     */
    public GitlabIssue withLabels(List<String> labels) {
        this.labels = labels;
        return this;
    }


    /**
     * Sets a httpClient to the this {@link GitlabIssue}.
     *
     * @param httpClient a http client used for making http request
     * @return {@link GitlabIssue} with httpClient
     */
    @Override
    GitlabIssue withHttpClient(HttpClient httpClient) {
        super.withHttpClient(httpClient);
        return this;
    }

    /**
     * This extends {@link GitlabQuery} and supports query issues within a
     * {@link GitlabProject} with searching scope and range.
     *
     * Build this query with setters and call {@code query()} to execute query.
     *
     * <p>
     * Gitlab Web API: https://docs.gitlab.com/ee/api/issues.html#list-project-issues
     * <p>
     * GET /projects/:id/issues
     */
    @JsonIgnoreType
    public static class ProjectQuery extends GitlabQuery<GitlabIssue> {
        private final GitlabProject project;

        ProjectQuery(HttpClient httpClient, GitlabProject project) {
            super(httpClient, GitlabIssue[].class);
            this.project = project;
        }

        /**
         * Returns a query that returns issues assigned to the given user id.
         *
         * This should not be used with {@code withAssigneeUsernames}
         * simultaneously.
         *
         * None returns unassigned issues. Any returns issues with an assignee.
         *
         * @param assigneeId id of the assignee
         * @return this {@link ProjectQuery} with given assignee
         */
        public ProjectQuery withAssigneeId(int assigneeId) {
            appendInt("assignee_id", assigneeId);
            return this;
        }

        /**
         * Returns a query that returns issues assigned to the given username.
         *
         * This should not be used with {@code withAssigneeId} simultaneously.
         *
         * In GitLab CE, the assignee_username array should only contain a
         * single value. Otherwise, an invalid parameter error is returned.
         *
         * @param assigneeUsernames id of the assignee
         * @return this {@link ProjectQuery} with given assignees
         */
        public ProjectQuery withAssigneeUsernames(List<String> assigneeUsernames) {
            appendStrings("assignee_username", assigneeUsernames);
            return this;
        }

        /**
         * Returns a query that returns issues authored by user specified by
         * authorId.
         *
         * This should not be used with {@code withAuthorUsername} simultaneously.
         *
         * Combine with scope=all or scope=assigned_to_me.
         *
         * @param authorId id of the author
         * @return this {@link ProjectQuery} with given author
         */
        public ProjectQuery withAuthorId(int authorId) {
            appendInt("author_id", authorId);
            return this;
        }

        /**
         * Returns a query that returns issues authored by user specified by
         * username.
         *
         * This should not be used with {@code withAuthorId} simultaneously.
         *
         * @param authorUsername username of the author
         * @return this {@link ProjectQuery} with given author
         */
        public ProjectQuery withAuthorUsername(String authorUsername) {
            appendString("author_username", authorUsername);
            return this;
        }

        /**
         * Returns a query that filters confidential or public issues
         * if confidential is true.
         *
         * @param confidential whether or not to filter confidential or public issues
         * @return this {@link ProjectQuery} with confidential
         */
        public ProjectQuery withConfidential(boolean confidential) {
            appendBoolean("confidential", confidential);
            return this;
        }

        /**
         * Returns a query that returns issues created on or after given date.
         *
         * @param createdAfter date to get issues since this date
         * @return this {@link ProjectQuery} with date
         */
        public ProjectQuery withCreatedAfter(ZonedDateTime createdAfter) {
            appendDateTime("created_after", createdAfter);
            return this;
        }

        /**
         * Returns a query that returns issues created on or before given date.
         *
         * @param createdBefore date to get issues until this date
         * @return this {@link ProjectQuery} with date
         */
        public ProjectQuery withCreatedBefore(ZonedDateTime createdBefore) {
            appendDateTime("created_before", createdBefore);
            return this;
        }

        /**
         * Returns a query that returns issues matching the du date.
         *
         * Due date can be any of 0 (no due date), overdue, week, month or
         * next_month_and_previous_two_weeks.
         *
         * @param dueDate due date
         * @return this {@link ProjectQuery} with due date
         */
        public ProjectQuery withDueDate(String dueDate) {
            appendString("due_date", dueDate);
            return this;
        }

        /**
         * Returns a query that only returns issues having the given iid
         *
         * @param iids list of internal ids
         * @return this {@link ProjectQuery} with iids
         */
        public ProjectQuery withIids(List<Integer> iids) {
            appendInts("iids", iids);
            return this;
        }

        /**
         * Returns a query that matches given milestone title.
         *
         * None lists all issues with no milestone. Any lists all issues that
         * have an assigned milestone.
         *
         * @param milestone mile stone title
         * @return this {@link ProjectQuery} with milestone
         */
        public ProjectQuery withMilestone(String milestone) {
            appendString("milestone", milestone);
            return this;

        }

        /**
         * Returns a query that matches given labels.
         *
         * Labels are represented as comma-separated list of label names.
         * None lists all issues with no labels. Any lists all issues with at
         * least one label.
         *
         * @param labels list of labels
         * @return this {@link ProjectQuery} with list of labels
         */
        public ProjectQuery withLabels(List<String> labels) {
            appendStrings("labels", labels);
            return this;

        }

        /**
         * Returns a query that matches my reaction emoji.
         *
         * None returns issues not given a reaction. Any returns issues given
         * at least one reaction.
         *
         * @param myReactionEmoji reaction image
         * @return this {@link ProjectQuery} with reaction emoji
         */
        public ProjectQuery withMyReactionEmoji(String myReactionEmoji) {
            appendString("my_reaction_emoji", myReactionEmoji);
            return this;
        }

        /**
         * Returns a query that only returns issues from non-archived projects.
         *
         * If nonArchived is false, the response returns issues from both
         * archived and non-archived projects. Default value is true.
         *
         * @param nonArchived whether the responses are archived
         * @return this {@link ProjectQuery} with boolean
         */
        public ProjectQuery withNonArchived(boolean nonArchived) {
            appendBoolean("non_archived", nonArchived);
            return this;
        }

        /**
         * Returns a query that returns issues that do not match the parameters
         * supplied.
         *
         * Accepts: labels, milestone, author_id, author_username, assignee_id,
         * assignee_username, my_reaction_emoji.
         *
         * @param not whether to include such filter
         * @return this {@link ProjectQuery} with boolean
         */
        public ProjectQuery withNot(String not) {
            appendString("not", not);
            return this;
        }

        /**
         * Returns a query that returns results in given order.
         *
         * Order can be any of created_at, updated_at, priority, due_date,
         * relative_position, label_priority, milestone_due, popularity, weight
         * fields. Default value is created_at.
         *
         * @param orderBy a way to order all of the responds
         * @return this {@link ProjectQuery} with orderby
         */
        public ProjectQuery withOrderBy(String orderBy) {
            appendString("order_by", orderBy);
            return this;
        }

        /**
         * Returns a query that returns issues for given scope.
         *
         * Scope can be any of: created_by_me, assigned_to_me or all.
         * Defaults to created_by_me. For versions before 11.0, use the now
         * deprecated created-by-me or assigned-to-me scopes instead.
         *
         * @param scope different scope in gitlab
         * @return this {@link ProjectQuery} with scope
         */
        public ProjectQuery withScope(String scope) {
            appendString("scope", scope);
            return this;
        }

        /**
         * Returns a query that searches issues against their title and
         * description.
         *
         * @param search keyword to be searched
         * @return this {@link ProjectQuery} with search
         */
        public ProjectQuery withSearch(String search) {
            appendString("search", search);
            return this;
        }

        /**
         * Returns a query that returns results in sorted order.
         *
         * Sort order can be any of "asc" or "desc". Default value is "desc".
         *
         * @param sort ways to sort the response
         * @return this {@link ProjectQuery} with sort
         */
        public ProjectQuery withSort(String sort) {
            appendString("sort", sort);
            return this;
        }

        /**
         * Returns a query that returns issues updated on or after given time.
         *
         * @param updatedAfter date that all issue should be updated after
         * @return this {@link ProjectQuery} with date
         */
        public ProjectQuery withUpdatedAfter(ZonedDateTime updatedAfter) {
            appendDateTime("updated_after", updatedAfter);
            return this;
        }

        /**
         * Returns a query that returns issues updated on or before given time.
         *
         * @param updatedBefore date that all issue should be updated before
         * @return this {@link ProjectQuery} with date
         */
        public ProjectQuery withUpdatedBefore(ZonedDateTime updatedBefore) {
            appendDateTime("updated_before", updatedBefore);
            return this;
        }

        /**
         * Returns a query that returns issues with specified weight.
         *
         * None returns issues with no weight assigned. Any returns issues
         * with a weight assigned.
         *
         * @param weight weight parameter
         * @return this {@link ProjectQuery} with weight
         */
        public ProjectQuery withWeight(int weight) {
            appendInt("weight", weight);
            return this;
        }

        /**
         * Returns a query that returns issues with label details if
         * labelsDetails is true.
         *
         * @param labelsDetails whether or not to return labels with detail
         * @return this {@link ProjectQuery} with labelsDetails
         */
        public ProjectQuery withLabelsDetails(boolean labelsDetails) {
            appendBoolean("with_labels_details", labelsDetails);
            return this;
        }

        /**
         * Returns a query that specifies page number and size to return based
         * on given pagination.
         *
         * @param pagination pagination object that defines page number and size
         * @return this {@link GitlabBranch.ProjectQuery} with given pagination
         */
        @Override
        public ProjectQuery withPagination(Pagination pagination) {
            appendPagination(pagination);
            return this;
        }

        /**
         * Returns the URL suffix for the HTTP request.
         *
         * <p>
         * Gitlab Web API: https://docs.gitlab.com/ee/api/issues.html#list-project-issues
         * <p>
         * GET /projects/:id/issues
         *
         * @return The URL suffix to query {@link GitlabIssue} in the given {@link GitlabProject}
         */
        @Override
        String getTailUrl() {
            return String.format("/projects/%d/issues", project.getId());
        }

        /**
         * Binds the branch with given {@link GitlabProject} after the
         * response is parsed.
         *
         * @param component - one {@link GitlabIssue} from the response
         */
        @Override
        void bind(GitlabIssue component) {
            component.withProject(project);
        }

    }

    /**
     * This extends {@link GitlabQuery} and supports query global
     * {@link GitlabIssue}s with searching scope and range.
     *
     * Build this query with setters and call {@code query()} to execute query.
     *
     * <p>
     * Gitlab Web API: https://docs.gitlab.com/ee/api/issues.html#list-issues
     * <p>
     * GET /issues
     */
    @JsonIgnoreType
    public static class Query extends GitlabQuery<GitlabIssue> {

        Query(HttpClient httpClient) {
            super(httpClient, GitlabIssue[].class);
        }

        /**
         * Returns a query that returns issues assigned to the given user id.
         *
         * This should not be used with {@code withAssigneeUsernames}
         * simultaneously.
         *
         * None returns unassigned issues. Any returns issues with an assignee.
         *
         * @param assigneeId id of the assignee
         * @return this {@link ProjectQuery} with given assignee
         */
        public Query withAssigneeId(int assigneeId) {
            appendInt("assignee_id", assigneeId);
            return this;
        }

        /**
         * Returns a query that returns issues assigned to the given username.
         *
         * This should not be used with {@code withAssigneeId} simultaneously.
         *
         * In GitLab CE, the assignee_username array should only contain a
         * single value. Otherwise, an invalid parameter error is returned.
         *
         * @param assigneeUsernames id of the assignee
         * @return this {@link ProjectQuery} with given assignees
         */
        public Query withAssigneeUsernames(List<String> assigneeUsernames) {
            appendStrings("assignee_username", assigneeUsernames);
            return this;
        }

        /**
         * Returns a query that returns issues authored by user specified by
         * authorId.
         *
         * This should not be used with {@code withAuthorUsername} simultaneously.
         *
         * Combine with scope=all or scope=assigned_to_me.
         *
         * @param authorId id of the author
         * @return this {@link ProjectQuery} with given author
         */
        public Query withAuthorId(int authorId) {
            appendInt("author_id", authorId);
            return this;
        }

        /**
         * Returns a query that returns issues authored by user specified by
         * username.
         *
         * This should not be used with {@code withAuthorId} simultaneously.
         *
         * @param authorUsername username of the author
         * @return this {@link ProjectQuery} with given author
         */
        public Query withAuthorUsername(String authorUsername) {
            appendString("author_username", authorUsername);
            return this;
        }

        /**
         * Returns a query that filters confidential or public issues
         * if confidential is true.
         *
         * @param confidential whether or not to filter confidential or public issues
         * @return this {@link ProjectQuery} with confidential
         */
        public Query withConfidential(boolean confidential) {
            appendBoolean("confidential", confidential);
            return this;
        }

        /**
         * Returns a query that returns issues created on or after given date.
         *
         * @param createdAfter date to get issues since this date
         * @return this {@link ProjectQuery} with date
         */
        public Query withCreatedAfter(ZonedDateTime createdAfter) {
            appendDateTime("created_after", createdAfter);
            return this;
        }

        /**
         * Returns a query that returns issues created on or before given date.
         *
         * @param createdBefore date to get issues until this date
         * @return this {@link ProjectQuery} with date
         */
        public Query withCreatedBefore(ZonedDateTime createdBefore) {
            appendDateTime("created_before", createdBefore);
            return this;
        }

        /**
         * Returns a query that returns issues matching the du date.
         *
         * Due date can be any of 0 (no due date), overdue, week, month or
         * next_month_and_previous_two_weeks.
         *
         * @param dueDate due date
         * @return this {@link ProjectQuery} with due date
         */
        public Query withDueDate(String dueDate) {
            appendString("due_date", dueDate);
            return this;
        }

        /**
         * Returns a query that only returns issues having the given iid
         *
         * @param iids list of internal ids
         * @return this {@link ProjectQuery} with iids
         */
        public Query withIids(List<Integer> iids) {
            appendInts("iids", iids);
            return this;

        }

        /**
         * Returns a quert that modifies the scope of the search attribute,
         * title, description, or a string joining them with comma.
         *
         * Default is title, description.
         *
         * @param in scope of the search
         * @return this {@link Query} with scope
         */
        public Query withIn(String in) {
            appendString("in", in);
            return this;

        }

        /**
         * Returns a query that returns issues assigned to given iteration ID.
         *
         * None returns issues that do not belong to an iteration. Any returns
         * issues that belong to an iteration.
         *
         * This should not be used with {@code withIterationTitle}
         * simultaneously.
         *
         * @param iterationId iteration id
         * @return this {@link Query} with iteration id
         */
        public Query withIterationId(int iterationId) {
            appendInt("iteration_id", iterationId);
            return this;

        }

        /**
         * Returns a query that returns issues assigned to given iteration Title.
         *
         * None returns issues that do not belong to an iteration. Any returns
         * issues that belong to an iteration.
         *
         * This should not be used with {@code withIterationId}
         * simultaneously.
         *
         * @param iterationTitle iteration title
         * @return this {@link Query} with iteration title
         */
        public Query withIterationTitle(String iterationTitle) {

            appendString("iteration_title", iterationTitle);
            return this;

        }

        /**
         * Returns a query that matches for milestone title.
         *
         * None lists all issues with no milestone. Any lists all issues that
         * have an assigned milestone.
         *
         * @param milestone mile stone title
         * @return this {@link ProjectQuery} with milestone
         */
        public Query withMilestone(String milestone) {
            appendString("milestone", milestone);
            return this;

        }

        /**
         * Returns a query that matches given labels.
         *
         * Labels are represented as comma-separated list of label names.
         * None lists all issues with no labels. Any lists all issues with at
         * least one label.
         *
         * @param labels list of labels
         * @return this {@link ProjectQuery} with list of labels
         */
        public Query withLabels(List<String> labels) {
            appendStrings("labels", labels);
            return this;

        }

        /**
         * Returns a query that matches my reaction emoji.
         *
         * None returns issues not given a reaction. Any returns issues given
         * at least one reaction.
         *
         * @param myReactionEmoji reaction image
         * @return this {@link ProjectQuery} with reaction emoji
         */
        public Query withMyReactionEmoji(String myReactionEmoji) {
            appendString("my_reaction_emoji", myReactionEmoji);
            return this;
        }

        /**
         * Returns a query that only returns issues from non-archived projects.
         *
         * If nonArchived is false, the response returns issues from both
         * archived and non-archived projects. Default value is true.
         *
         * @param nonArchived whether the responses are archived
         * @return this {@link ProjectQuery} with boolean
         */
        public Query withNonArchived(boolean nonArchived) {
            appendBoolean("non_archived", nonArchived);
            return this;
        }

        /**
         * Returns a query that returns issues that do not match the parameters
         * supplied.
         *
         * Accepts: labels, milestone, author_id, author_username, assignee_id,
         * assignee_username, my_reaction_emoji.
         *
         * @param not whether to include such filter
         * @return this {@link ProjectQuery} with string value not
         */
        public Query withNot(String not) {
            appendString("not", not);
            return this;
        }

        /**
         * Returns a query that returns results in given order.
         *
         * Order can be any of created_at, updated_at, priority, due_date,
         * relative_position, label_priority, milestone_due, popularity, weight
         * fields. Default value is created_at.
         *
         * @param orderBy a way to order all of the responds
         * @return this {@link ProjectQuery} with orderby
         */
        public Query withOrderBy(String orderBy) {
            appendString("order_by", orderBy);
            return this;
        }

        /**
         * Returns a query that returns issues for given scope.
         *
         * Scope can be any of: created_by_me, assigned_to_me or all.
         * Defaults to created_by_me. For versions before 11.0, use the now
         * deprecated created-by-me or assigned-to-me scopes instead.
         *
         * @param scope different scope in gitlab
         * @return this {@link ProjectQuery} with scope
         */
        public Query withScope(String scope) {
            appendString("scope", scope);
            return this;
        }

        /**
         * Returns a query that searches issues against their title and
         * description.
         *
         * @param search keyword to be searched
         * @return this {@link ProjectQuery} with search
         */
        public Query withSearch(String search) {
            appendString("search", search);
            return this;
        }

        /**
         * Returns a query that returns results in sorted order.
         *
         * Sort order can be any of "asc" or "desc". Default value is "desc".
         *
         * @param sort ways to sort the response
         * @return this {@link ProjectQuery} with sort
         */
        public Query withSort(String sort) {
            appendString("sort", sort);
            return this;
        }

        /**
         * Returns a query that returns issues updated on or after given time.
         *
         * @param updatedAfter date that all issue should be updated after
         * @return this {@link ProjectQuery} with date
         */
        public Query withUpdatedAfter(ZonedDateTime updatedAfter) {
            appendDateTime("updated_after", updatedAfter);
            return this;
        }

        /**
         * Returns a query that returns issues updated on or before given time.
         *
         * @param updatedBefore date that all issue should be updated before
         * @return this {@link ProjectQuery} with date
         */
        public Query withUpdatedBefore(ZonedDateTime updatedBefore) {
            appendDateTime("updated_before", updatedBefore);
            return this;
        }

        /**
         * Returns a query that returns issues with specified weight.
         *
         * None returns issues with no weight assigned. Any returns issues
         * with a weight assigned.
         *
         * @param weight weight parameter
         * @return this {@link ProjectQuery} with weight
         */
        public Query withWeight(int weight) {
            appendInt("weight", weight);
            return this;
        }

        /**
         * Returns a query that returns issues with label details if
         * labelsDetails is true.
         *
         * @param labelsDetails whether or not to return labels with detail
         * @return this {@link ProjectQuery} with boolean
         */
        public Query withLabelsDetails(boolean labelsDetails) {
            appendBoolean("with_labels_details", labelsDetails);
            return this;
        }

        /**
         * Returns a query that specifies page number and size to return based
         * on given pagination.
         *
         * @param pagination pagination object that defines page number and size
         * @return this {@link GitlabBranch.ProjectQuery} with given pagination
         */
        @Override
        public Query withPagination(Pagination pagination) {
            appendPagination(pagination);
            return this;
        }

        /**
         * Returns the URL suffix for the HTTP request
         *
         * <p>
         * Gitlab Web API: https://docs.gitlab.com/ee/api/issues.html#list-issues
         * <p>
         * GET /issues
         *
         * @return The URL suffix to query {@link GitlabIssue}
         */
        @Override
        String getTailUrl() {
            return "/issues";
        }

        @Override
        void bind(GitlabIssue component) {}
    }
}
