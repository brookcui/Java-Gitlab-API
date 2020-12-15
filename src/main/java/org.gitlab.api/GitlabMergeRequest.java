package org.gitlab.api;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreType;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * This class serves as instance of Gitlab component Merge Request.
 *
 * To create, update, or delete this issue, call {@code create()},
 * {@code update()}, or {@code delete()} explicitly.
 *
 * This supports query for merge requests globally or within a
 * {@link GitlabProject}. See {@link Query} and {@link ProjectQuery}
 * respectively.
 *
 * Gitlab Web API: https://docs.gitlab.com/ee/api/merge_requests.html
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class GitlabMergeRequest extends GitlabComponent {
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

    /**
     * Constructs the {@link GitlabMergeRequest} from source branch to target
     * branch with given tile.
     *
     * @param sourceBranch source branch
     * @param targetBranch target branch
     * @param title        title of this merge request
     */
    GitlabMergeRequest(@JsonProperty("source_branch") String sourceBranch,
                       @JsonProperty("target_branch") String targetBranch,
                       @JsonProperty("title") String title) {
        this.sourceBranch = sourceBranch;
        this.targetBranch = targetBranch;
        this.title = title;
    }

    /**
     * Returns a string representation of this {@link GitlabMergeRequest} in
     * the format of Gitlab component type and internal id and merge request
     * title.
     *
     * @return a string representation of this {@link GitlabMergeRequest}
     */
    @Override
    public String toString() {
        return "GitlabMergeRequest{" +
                       "iid=" + iid +
                       ", title=" + title +
                       '}';
    }

    /**
     * Returns the hash code value for this {@link GitlabMergeRequest}
     * identified by its belonged project and merge request id.
     *
     * @return a hash code value for this object
     */
    @Override
    public int hashCode() {
        return Objects.hash(projectId, id);
    }

    /**
     * Compares the specified {@code Object} with this
     * {@link GitlabMergeRequest} for equality. Note that two
     * {@link GitlabMergeRequest}s are equal if and only if they belong to the
     * same project and have the same merge request id.
     *
     * @param o object to be compared for equality with this {@link GitlabMergeRequest}
     * @return true if the specified Object is equal to this {@link GitlabMergeRequest}
     */
    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof GitlabMergeRequest)) {
            return false;
        }
        GitlabMergeRequest that = (GitlabMergeRequest) o;
        return projectId == that.projectId && id == that.id;
    }

    /**
     * Issues a HTTP request to Gitlab API endpoint to create a merge request
     * based on this {@link GitlabMergeRequest}.
     *
     * @return the created {@link GitlabMergeRequest} instance
     * @throws GitlabException if {@link IOException} occurs or API endpoint fails
     * to give a valid response (response code within [200,400))
     */
    public GitlabMergeRequest create() {
        Body body = new Body()
                .putString("source_branch", sourceBranch)
                .putString("target_branch", targetBranch)
                .putString("title", title)
                .putIntArray("assignee_ids", assignees.stream().mapToInt(GitlabUser::getId).toArray())
                .putString("description", description)
                .putStringArray("labels", labels);

        return httpClient
                .post(String.format("/projects/%d/merge_requests", projectId), body,
                        this);
    }

    /**
     * Issues a HTTP request to Gitlab API endpoint to delete this
     * {@link GitlabMergeRequest} based on internal id.
     *
     * @return the {@link GitlabMergeRequest} instance before deleted
     * @throws GitlabException if {@link IOException} occurs or API endpoint fails
     * to give a valid response (response code within [200,400))
     */
    public GitlabMergeRequest delete() {
        httpClient.delete(String.format("/projects/%d/merge_requests/%d", projectId, iid));
        return this;
    }

    /**
     * Issues a HTTP request to Gitlab API endpoint to update this
     * {@link GitlabMergeRequest} based on its current fields.
     *
     * @return the updated {@link GitlabMergeRequest} component
     * @throws GitlabException if {@link IOException} occurs or API endpoint fails
     * to give a valid response (response code within [200,400))
     */
    public GitlabMergeRequest update() {
        Body body = new Body()
                .putString("target_branch", targetBranch)
                .putString("title", title)
                .putIntArray("assignee_ids", assignees.stream().mapToInt(GitlabUser::getId).toArray())
                .putString("description", description)
                .putStringArray("labels", labels);
        return httpClient
                .put(String.format("/projects/%d/merge_requests/%d", projectId, iid), body, this);
    }

    /**
     * Issues a HTTP request to Gitlab API endpoint to user who participated in
     * this {@link GitlabMergeRequest}.
     *
     * @return a list of {@link GitlabUser} that participated in this merge request
     * @throws GitlabException if {@link IOException} occurs or the response code is not in [200,400)
     */
    public List<GitlabUser> getAllParticipants() {
        return httpClient.getList(
                String.format("/projects/%d/merge_requests/%d/participants", projectId, iid), GitlabUser[].class);
    }

    /**
     * Issues a HTTP request to Gitlab API endpoint to get commits within this
     * {@link GitlabMergeRequest}.
     *
     * @return a list of {@link GitlabCommit} in this merge request
     * @throws GitlabException if {@link IOException} occurs or the response code is not in [200,400)
     */
    public List<GitlabCommit> getAllCommits() {
        return httpClient.getList(String
                .format("/projects/%d/merge_requests/%d/commits", projectId, iid), GitlabCommit[].class);
    }

    /**
     * Issues a HTTP request to Gitlab API endpoint to get issues that will be
     * closed after this {@link GitlabMergeRequest} has been merged.
     *
     * @return a list of {@link GitlabIssue} that will be closed after commit is merged
     * @throws GitlabException if {@link IOException} occurs or the response code is not in [200,400)
     */
    public List<GitlabIssue> getAllIssuesClosedByMerge() {
        return httpClient.getList(String
                .format("/projects/%d/merge_requests/%d/closes_issues", projectId, iid), GitlabIssue[].class);
    }

    /**
     * Issues a HTTP request to Gitlab API endpoint to accept this
     * {@link GitlabMergeRequest}.
     *
     * @return {@link GitlabMergeRequest} after merge request has been accepted
     * @throws GitlabException if {@link IOException} occurs or the response code is not in [200,400)
     */
    public GitlabMergeRequest accept() {
        return httpClient.put(String
                .format("/projects/%d/merge_requests/%d/merge", projectId, iid), null, this);
    }

    /**
     * Issues a HTTP request to Gitlab API endpoint to approve this
     * {@link GitlabMergeRequest}
     *
     * @return {@link GitlabMergeRequest} after merge request has been approved
     * @throws GitlabException if {@link IOException} occurs or the response code is not in [200,400)
     */
    public GitlabMergeRequest approve() {
        return httpClient.post(String
                .format("/projects/%d/merge_requests/%d/approve", projectId, iid), null, this);
    }

    /**
     * Issues a HTTP request to Gitlab API endpoint to decline this
     * {@link GitlabMergeRequest}.
     *
     * @return {@link GitlabMergeRequest} after merge request has been decline
     * @throws GitlabException if {@link IOException} occurs or the response code is not in [200,400)
     */
    public GitlabMergeRequest decline() {
        return httpClient.post(String
                .format("/projects/%d/merge_requests/%d/unapprove", projectId, iid), null, this);
    }

    /**
     * Returns the project id that this merge request belongs to.
     *
     * @return project id
     */
    public int getProjectId() {
        return projectId;
    }

    /**
     * Returns the author of the merge request.
     *
     * @return {@link GitlabUser} of this merge request
     */
    public GitlabUser getAuthor() {
        return author;
    }

    /**
     * Returns the description of this merge request.
     *
     * @return description of the merge request
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns the current state of the merge request.
     *
     * @return current state of the merge request
     */
    public String getState() {
        return state;
    }

    /**
     * Returns users that are assigned to the merge request.
     *
     * @return a list of {@link GitlabUser} that are assigned to the merge request
     */
    public List<GitlabUser> getAssignees() {
        return assignees;
    }

    /**
     * Returns the number of up votes that this merge request contain.
     *
     * @return number of up votes
     */
    public int getUpvotes() {
        return upvotes;
    }

    /**
     * Returns the number of down votes that this merge request contain.
     *
     * @return number of down votes
     */
    public int getDownvotes() {
        return downvotes;
    }

    /**
     * Returns the number of merge request count.
     *
     * @return number of merge request count
     */
    public int getMergeRequestCount() {
        return mergeRequestCount;
    }

    /**
     * Returns the title of the merge request.
     *
     * @return Title of this merge request
     */
    public String getTitle() {
        return title;
    }

    /**
     * Returns the time when the merge request is updated.
     *
     * @return time when the merge request is updated
     */
    public ZonedDateTime getUpdatedAt() {
        return updatedAt;
    }

    /**
     * Returns the time when the merge request is created.
     *
     * @return time when the merge request is created
     */
    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * Returns the time when the merge request is closed.
     *
     * @return time when the merge request is closed
     */
    public ZonedDateTime getClosedAt() {
        return closedAt;
    }

    /**
     * Returns the {@link GitlabUser} who closed the merge request
     *
     * @return {@link GitlabUser} who closed the merge request
     */
    public GitlabUser getClosedBy() {
        return closedBy;
    }

    /**
     * Tests if this merge request has been subscribed by current user.
     *
     * @return true if this merge request has been subscribed by current user
     */
    public boolean isSubscribed() {
        return subscribed;
    }

    /**
     * Returns the web url to this merge request.
     *
     * @return web url to this merge request
     */
    public String getWebUrl() {
        return webUrl;
    }

    /**
     * Returns the target branch that this merge request has.
     *
     * @return target branch in the merge request
     */
    public String getTargetBranch() {
        return targetBranch;
    }

    /**
     * Returns the source branch that this merge request has.
     *
     * @return source branch in the merge request
     */
    public String getSourceBranch() {
        return sourceBranch;
    }

    /**
     * Returns the project that this merge request belongs to.
     *
     * @return the {@link GitlabProject} that this merge request belongs to
     */
    public GitlabProject getProject() {
        if (project == null) {
            project = GitlabProject.fromId(httpClient, projectId);
        }
        return project;
    }

    /**
     * Returns the id of this merge request.
     *
     * @return the id of this merge request
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the internal id of this merge request
     *
     * @return the internal id of this merge request
     */
    public int getIid() {
        return iid;
    }

    /**
     * Attaches a project to this {@link GitlabMergeRequest}.
     *
     * @param project the project to be attached
     * @return this {@link GitlabMergeRequest}
     */
    GitlabMergeRequest withProject(GitlabProject project) {
        Objects.requireNonNull(project);
        this.project = project;
        this.projectId = project.getId();
        return this;
    }

    /**
     * Returns all of the labels within this merge request.
     *
     * @return a list of labels
     */
    public List<String> getLabels() {
        return labels;
    }

    /**
     * Sets the title to this merge request.
     *
     * @param title the title of the merge request
     * @return a {@link GitlabMergeRequest} with given title
     */
    public GitlabMergeRequest withTitle(String title) {
        this.title = title;
        return this;
    }

    /**
     * Sets the description to this merge request.
     *
     * @param description the description of the merge request
     * @return a {@link GitlabMergeRequest} with given description
     */
    public GitlabMergeRequest withDescription(String description) {
        this.description = description;
        return this;
    }

    /**
     * Sets the description to the list of {@link GitlabUser} as assignees.
     *
     * @param assignees a list of {@link GitlabUser}
     * @return a {@link GitlabMergeRequest} with list of assignees
     */
    public GitlabMergeRequest withAssignees(List<GitlabUser> assignees) {
        this.assignees = assignees;
        return this;
    }

    /**
     * Sets the targetBranch to this merge request.
     *
     * @param targetBranch the target branch of the merge request
     * @return a {@link GitlabMergeRequest} with given targetBranch
     */
    public GitlabMergeRequest withTargetBranch(String targetBranch) {
        this.targetBranch = targetBranch;
        return this;
    }

    /**
     * Set a httpClient to the current {@link GitlabAPIClient}
     *
     * @param httpClient the http client used to make request
     * @return {@link GitlabMergeRequest} with the httpClient
     */
    @Override
    GitlabMergeRequest withHttpClient(HttpClient httpClient) {
        super.httpClient = httpClient;
        return this;
    }

    /**
     * This extends {@link GitlabQuery} and supports query merge requests
     * within a {@link GitlabProject} with searching scope and range.
     *
     * <p>
     * Gitlab Web API: https://docs.gitlab.com/ee/api/merge_requests.html#list-project-merge-requests
     * <p>
     * GET /projects/:id/merge_requests
     */
    @JsonIgnoreType
    public static class ProjectQuery extends GitlabQuery<GitlabMergeRequest> {
        private final GitlabProject project;

        ProjectQuery(HttpClient httpClient, GitlabProject project) {
            super(httpClient, GitlabMergeRequest[].class);
            this.project = project;
        }

        /**
         * Returns a query that specifies page number and size to return based
         * on given pagination.
         *
         * @param pagination pagination object that defines page number and size
         * @return this {@link ProjectQuery} with given pagination
         */
        @Override
        public ProjectQuery withPagination(Pagination pagination) {
            appendPagination(pagination);
            return this;
        }

        /**
         * Returns a query that matches given project id.
         *
         * @param id the id of a project
         * @return this {@link ProjectQuery} with project id
         */
        public ProjectQuery withId(String id) {
            appendString("id", id);
            return this;
        }

        /**
         * Returns a query that matches given internal ids.
         *
         * @param iids list of iid
         * @return this {@link ProjectQuery} with list of iids
         */
        public ProjectQuery withIids(List<Integer> iids) {
            appendInts("iids[]", iids);
            return this;
        }

        /**
         * Returns a query that matches given state string.
         *
         * state can be any of opened, closed, locked, merged.
         *
         * @param state state of the merge requests
         * @return this {@link ProjectQuery} with given state
         */
        public ProjectQuery withState(String state) {
            appendString("state", state);
            return this;
        }

        /**
         * Returns a query that returns results in given order.
         *
         * orderBy can be any of created_at or updated_at fields. Default value
         * is created_at.
         *
         * @param orderBy how to order the return request
         * @return this {@link ProjectQuery} with given order by
         */
        public ProjectQuery withOrderBy(String orderBy) {
            appendString("order_by", orderBy);
            return this;
        }

        /**
         * Returns a query that returns results in sorted order specified by sort string.
         *
         * sort can be any of "asc" or "desc". Default value is "desc".
         *
         * @param sort how to sort the return request
         * @return this {@link ProjectQuery} with given sort
         */
        public ProjectQuery withSort(String sort) {
            appendString("sort", sort);
            return this;
        }

        /**
         * Returns a query that matches given milestone title.
         *
         * None returns merge requests with no milestone. Any returns merge
         * requests that have an assigned milestone.
         *
         * @param milestone milestone to of all the resulting merge request
         * @return this {@link ProjectQuery} with given milestone
         */
        public ProjectQuery withMilestone(String milestone) {
            appendString("milestone", milestone);
            return this;
        }

        /**
         * Returns a query that matches given view.
         *
         * view can be any of "simple" or "normal". If simple, returns iid,
         * URL, title, description, and basic state of merge requests.
         *
         * @param view parameter on whether to return simple or normal view
         * @return this {@link ProjectQuery} with given view
         */
        public ProjectQuery withView(String view) {
            appendString("view", view);
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
         * Returns a query that returns detailed labels on each merge request
         * if withLabelDetails is true.
         *
         * If withLabelsDetails is true, response will return more details for
         * each label in labels field: :name, :color, :description,
         * :description_html, :text_color. Default value is false.
         *
         * @param withLabelsDetails whether or not to return labels with detail
         * @return this {@link ProjectQuery} with given boolean value withLabelsDetails
         */
        public ProjectQuery withWithLabelsDetails(boolean withLabelsDetails) {
            appendBoolean("with_labels_details", withLabelsDetails);
            return this;
        }

        /**
         * Returns a query that performs merge status recheck asynchronously if
         * withMergeStatusRecheck is true.
         *
         * @param withMergeStatusRecheck whether or not to asynchronously recalculate state
         * @return this {@link ProjectQuery} with given boolean value withMergeStatusRecheck
         */
        public ProjectQuery withWithMergeStatusRecheck(boolean withMergeStatusRecheck) {
            appendBoolean("with_merge_status_recheck", withMergeStatusRecheck);
            return this;
        }

        /**
         * Returns a query that returns only merge requests created on or after
         * given time.
         *
         * @param createdAfter get all merge requests after the date
         * @return this {@link ProjectQuery} with given created after
         */
        public ProjectQuery withCreatedAfter(ZonedDateTime createdAfter) {
            appendDateTime("created_after", createdAfter);
            return this;
        }

        /**
         * Returns a query that returns only merge requests created on or
         * before given time.
         *
         * @param createdBefore get all merge requests before the date
         * @return this {@link ProjectQuery} with given created before
         */
        public ProjectQuery withCreatedBefore(ZonedDateTime createdBefore) {
            appendDateTime("created_before", createdBefore);
            return this;
        }

        /**
         * Returns a query that returns only merge requests updated on or after
         * given time.
         *
         * @param updatedAfter get all merge requests updated after the date
         * @return this {@link ProjectQuery} with given updated after
         */
        public ProjectQuery withUpdatedAfter(ZonedDateTime updatedAfter) {
            appendDateTime("updated_after", updatedAfter);
            return this;
        }

        /**
         * Returns a query that returns only merge requests updated on or
         * before given time.
         *
         * @param updatedBefore get all merge requests updated before the date
         * @return this {@link ProjectQuery} with given updated before
         */
        public ProjectQuery withUpdatedBefore(ZonedDateTime updatedBefore) {
            appendDateTime("updated_before", updatedBefore);
            return this;
        }

        /**
         * Returns a query that matches given scope.
         *
         * @param scope scope of the merge request
         * @return this {@link ProjectQuery} with given scope
         */
        public ProjectQuery withScope(String scope) {
            appendString("scope", scope);
            return this;
        }

        /**
         * Returns a query that returns only merge requests authored by user
         * specified by authorId.
         *
         * @param authorId id of the author
         * @return this {@link ProjectQuery} with given author id
         */
        public ProjectQuery withAuthorId(int authorId) {
            appendInt("author_id", authorId);
            return this;
        }

        /**
         * Returns a query that returns only merge requests authored by user
         * specified by authorUsername.
         *
         * @param authorUsername id of the author
         * @return this {@link ProjectQuery} with given author username
         */
        public ProjectQuery withAuthorUsername(String authorUsername) {
            appendString("author_username", authorUsername);
            return this;
        }

        /**
         * Returns a query that returns only merge requests assigned to user
         * specified by assigneeId.
         *
         * @param assigneeId id of the assignee
         * @return this {@link ProjectQuery} with given author username
         */
        public ProjectQuery withAssigneeId(int assigneeId) {
            appendInt("assignee_id", assigneeId);
            return this;
        }

        /**
         * Returns a query that returns only merge requests approved by given
         * users.
         *
         * @param approverIds list of approvers
         * @return this {@link ProjectQuery} with given list of approvers
         */
        public ProjectQuery withApproverIds(List<Integer> approverIds) {
            appendInts("approver_ids", approverIds);
            return this;
        }

        /**
         * Returns a query that returns only merge requests approved by given
         * users specified by ids.
         *
         * None returns merge requests with no approvals. Any returns merge
         * requests with an approval.
         *
         * @param approvedByIds list of user id that approved the merge request
         * @return this {@link ProjectQuery} with given approver ids
         */
        public ProjectQuery withApprovedByIds(String approvedByIds) {
            appendString("approved_by_ids", approvedByIds);
            return this;
        }

        /**
         * Returns a query that matches my reaction emoji.
         *
         * None returns issues not given a reaction. Any returns issues given
         * at least one reaction.
         *
         * @param myReactionEmoji a emoji represented by a string
         * @return this {@link ProjectQuery} with given emoji
         */
        public ProjectQuery withMyReactionEmoji(String myReactionEmoji) {
            appendString("my_reaction_emoji", myReactionEmoji);
            return this;
        }

        /**
         * Returns a query that returns merge requests sourcing from given
         * branch.
         *
         * @param sourceBranch the source branch
         * @return this {@link ProjectQuery} with given source branch
         */
        public ProjectQuery withSourceBranch(String sourceBranch) {
            appendString("source_branch", sourceBranch);
            return this;
        }

        /**
         * Returns a query that returns merge requests targeting at given
         * branch.
         *
         * @param targetBranch the target branch
         * @return this {@link ProjectQuery} with given target branch
         */
        public ProjectQuery withTargetBranch(String targetBranch) {
            appendString("target_branch", targetBranch);
            return this;
        }

        /**
         * Returns a query that searches merge requests against their title and
         * description.
         *
         * @param search search keyword
         * @return this {@link ProjectQuery} with given search keyword
         */
        public ProjectQuery withSearch(String search) {
            appendString("search", search);
            return this;
        }

        /**
         * Returns a query that filters merge requests against their wip
         * status.
         *
         * If wip is yes, return only WIP merge requests, otherwise return
         * non WIP merge requests.
         *
         * @param wip wip status
         * @return this {@link ProjectQuery} with given wip status
         */
        public ProjectQuery withWip(String wip) {
            appendString("wip", wip);
            return this;
        }

        /**
         * Returns the URL suffix for the HTTP request.
         *
         * <p>
         * Gitlab Web API: https://docs.gitlab.com/ee/api/merge_requests.html#list-project-merge-requests
         * <p>
         * GET /projects/:id/merge_requests
         *
         * @return The URL suffix to query {@link GitlabMergeRequest} in the given {@link GitlabProject}
         */
        @Override
        String getTailUrl() {
            return String.format("/projects/%d/merge_requests", project.getId());
        }

        /**
         * Binds the branch with the given {@link GitlabProject} after the
         * response is parsed.
         *
         * @param component - one {@link GitlabMergeRequest} from the response
         */
        @Override
        void bind(GitlabMergeRequest component) {
            component.withProject(project);
        }
    }

    /**
     * This extends {@link GitlabQuery} and supports query global
     * {@link GitlabMergeRequest}s with searching scope and range.
     *
     * Build this query with setters and call {@code query()} to execute query.
     *
     * <p>
     * Gitlab Web API: https://docs.gitlab.com/ee/api/merge_requests.html#list-merge-requests
     * <p>
     * GET /merge_requests
     */
    @JsonIgnoreType
    public static class Query extends GitlabQuery<GitlabMergeRequest> {

        Query(HttpClient httpClient) {
            super(httpClient, GitlabMergeRequest[].class);
        }

        /**
         * Returns a query that specifies page number and size to return based
         * on given pagination.
         *
         * @param pagination pagination object that defines page number and size
         * @return this {@link ProjectQuery} with given pagination
         */
        @Override
        public Query withPagination(Pagination pagination) {
            appendPagination(pagination);
            return this;
        }

        /**
         * Returns a query that matches given state string.
         *
         * state can be any of opened, closed, locked, merged.
         *
         * @param state state of the merge requests
         * @return this {@link ProjectQuery} with given state
         */
        public Query withState(String state) {
            appendString("state", state);
            return this;
        }

        /**
         * Returns a query that returns results in given order.
         *
         * orderBy can be any of created_at or updated_at fields. Default value
         * is created_at.
         *
         * @param orderBy how to order the return request
         * @return this {@link ProjectQuery} with given order by
         */
        public Query withOrderBy(String orderBy) {
            appendString("order_by", orderBy);
            return this;
        }

        /**
         * Returns a query that returns results in sorted order specified by sort string.
         *
         * sort can be any of "asc" or "desc". Default value is "desc".
         *
         * @param sort how to sort the return request
         * @return this {@link ProjectQuery} with given sort
         */
        public Query withSort(String sort) {
            appendString("sort", sort);
            return this;
        }

        /**
         * Returns a query that matches given milestone title.
         *
         * None returns merge requests with no milestone. Any returns merge
         * requests that have an assigned milestone.
         *
         * @param milestone milestone to of all the resulting merge request
         * @return this {@link ProjectQuery} with given milestone
         */
        public Query withMilestone(String milestone) {
            appendString("milestone", milestone);
            return this;
        }

        /**
         * Returns a query that matches given view.
         *
         * view can be any of "simple" or "normal". If simple, returns iid,
         * URL, title, description, and basic state of merge requests.
         *
         * @param view parameter on whether to return simple or normal view
         * @return this {@link ProjectQuery} with given view
         */
        public Query withView(String view) {
            appendString("view", view);
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
         * Returns a query that returns detailed labels on each merge request
         * if withLabelDetails is true.
         *
         * If withLabelsDetails is true, response will return more details for
         * each label in labels field: :name, :color, :description,
         * :description_html, :text_color. Default value is false.
         *
         * @param withLabelsDetails whether or not to return labels with detail
         * @return this {@link ProjectQuery} with given boolean value withLabelsDetails
         */
        public Query withWithLabelsDetails(boolean withLabelsDetails) {
            appendBoolean("with_labels_details", withLabelsDetails);
            return this;
        }

        /**
         * Returns a query that performs merge status recheck asynchronously if
         * withMergeStatusRecheck is true.
         *
         * @param withMergeStatusRecheck whether or not to asynchronously recalculate state
         * @return this {@link ProjectQuery} with given boolean value withMergeStatusRecheck
         */
        public Query withWithMergeStatusRecheck(boolean withMergeStatusRecheck) {
            appendBoolean("with_merge_status_recheck", withMergeStatusRecheck);
            return this;
        }

        /**
         * Returns a query that returns only merge requests created on or after
         * given time.
         *
         * @param createdAfter get all merge requests after the date
         * @return this {@link ProjectQuery} with given created after
         */
        public Query withCreatedAfter(ZonedDateTime createdAfter) {
            appendDateTime("created_after", createdAfter);
            return this;
        }

        /**
         * Returns a query that returns only merge requests created on or
         * before given time.
         *
         * @param createdBefore get all merge requests before the date
         * @return this {@link ProjectQuery} with given created before
         */
        public Query withCreatedBefore(ZonedDateTime createdBefore) {
            appendDateTime("created_before", createdBefore);
            return this;
        }

        /**
         * Returns a query that returns only merge requests updated on or after
         * given time.
         *
         * @param updatedAfter get all merge requests updated after the date
         * @return this {@link ProjectQuery} with given updated after
         */
        public Query withUpdatedAfter(ZonedDateTime updatedAfter) {
            appendDateTime("updated_after", updatedAfter);
            return this;
        }

        /**
         * Returns a query that returns only merge requests updated on or
         * before given time.
         *
         * @param updatedBefore get all merge requests updated before the date
         * @return this {@link ProjectQuery} with given updated before
         */
        public Query withUpdatedBefore(ZonedDateTime updatedBefore) {
            appendDateTime("updated_before", updatedBefore);
            return this;
        }

        /**
         * Returns a query that matches given scope.
         *
         * @param scope scope of the merge request
         * @return this {@link ProjectQuery} with given scope
         */
        public Query withScope(String scope) {
            appendString("scope", scope);
            return this;
        }

        /**
         * Returns a query that returns only merge requests authored by user
         * specified by authorId.
         *
         * @param authorId id of the author
         * @return this {@link ProjectQuery} with given author id
         */
        public Query withAuthorId(int authorId) {
            appendInt("author_id", authorId);
            return this;
        }

        /**
         * Returns a query that returns only merge requests authored by user
         * specified by authorUsername.
         *
         * @param authorUsername id of the author
         * @return this {@link ProjectQuery} with given author username
         */
        public Query withAuthorUsername(String authorUsername) {
            appendString("author_username", authorUsername);
            return this;
        }

        /**
         * Returns a query that returns only merge requests assigned to user
         * specified by assigneeId.
         *
         * @param assigneeId id of the assignee
         * @return this {@link ProjectQuery} with given author username
         */
        public Query withAssigneeId(int assigneeId) {
            appendInt("assignee_id", assigneeId);
            return this;
        }

        /**
         * Returns a query that returns only merge requests approved by given
         * users.
         *
         * @param approverIds list of approvers
         * @return this {@link ProjectQuery} with given list of approvers
         */
        public Query withApproverIds(List<Integer> approverIds) {
            appendInts("approver_ids", approverIds);
            return this;
        }

        /**
         * Returns a query that returns only merge requests approved by given
         * users specified by ids.
         *
         * None returns merge requests with no approvals. Any returns merge
         * requests with an approval.
         *
         * @param approvedByIds list of user id that approved the merge request
         * @return this {@link ProjectQuery} with given approver ids
         */
        public Query withApprovedByIds(List<Integer> approvedByIds) {
            appendInts("approved_by_ids", approvedByIds);
            return this;
        }

        /**
         * Returns a query that matches my reaction emoji.
         *
         * None returns issues not given a reaction. Any returns issues given
         * at least one reaction.
         *
         * @param myReactionEmoji a emoji represented by a string
         * @return this {@link ProjectQuery} with given emoji
         */
        public Query withMyReactionEmoji(String myReactionEmoji) {
            appendString("my_reaction_emoji", myReactionEmoji);
            return this;
        }

        /**
         * Returns a query that returns merge requests sourcing from given
         * branch.
         *
         * @param sourceBranch the source branch
         * @return this {@link ProjectQuery} with given source branch
         */
        public Query withSourceBranch(String sourceBranch) {
            appendString("source_branch", sourceBranch);
            return this;
        }

        /**
         * Returns a query that returns merge requests targeting at given
         * branch.
         *
         * @param targetBranch the target branch
         * @return this {@link ProjectQuery} with given target branch
         */
        public Query withTargetBranch(String targetBranch) {
            appendString("target_branch", targetBranch);
            return this;
        }

        /**
         * Returns a query that searches merge requests against their title and
         * description.
         *
         * @param search search keyword
         * @return this {@link ProjectQuery} with given search keyword
         */
        public Query withSearch(String search) {
            appendString("search", search);
            return this;
        }

        /**
         * Returns a quert that modifies the scope of the search attribute,
         * title, description, or a string joining them with comma.
         *
         * Default is title, description.
         *
         * @param in scope of the search
         * @return this {@link GitlabIssue.Query} with scope
         */
        public Query withIn(String in) {
            appendString("in", in);
            return this;
        }

        /**
         * Returns a query that filters merge requests against their wip
         * status.
         *
         * If wip is yes, return only WIP merge requests, otherwise return
         * non WIP merge requests.
         *
         * @param wip wip status
         * @return this {@link ProjectQuery} with given wip status
         */
        public Query withWip(String wip) {
            appendString("wip", wip);
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
         * @return this {@link GitlabIssue.ProjectQuery} with string value not
         */
        public Query withNot(String not) {
            appendString("not", not);
            return this;
        }

        /**
         * Returns a query that returns merge requests deployed to given
         * environment.
         *
         * @param environment specific environment to filter
         * @return the query with the given environment
         */
        public Query withEnvironment(String environment) {
            appendString("environment", environment);
            return this;
        }

        /**
         * Returns a query that returns merge requests deployed before given
         * datetime.
         *
         * @param deployedBefore a date to add to the query
         * @return the query with the given date
         */
        public Query withDeployedBefore(ZonedDateTime deployedBefore) {
            appendDateTime("deployed_before", deployedBefore);
            return this;
        }

        /**
         * Returns a query that returns merge requests deployed after given
         * datetime.
         *
         * @param deployedAfter a date to add to the query
         * @return the query with the given date
         */
        public Query withDeployedAfter(ZonedDateTime deployedAfter) {
            appendDateTime("deployed_after", deployedAfter);
            return this;
        }

        /**
         * Returns the URL suffix for the HTTP request.
         *
         * <p>
         * Gitlab Web API: https://docs.gitlab.com/ee/api/merge_requests.html#list-merge-requests
         * <p>
         * GET /merge_requests
         *
         * @return The URL suffix to query {@link GitlabMergeRequest}
         */
        @Override
        String getTailUrl() {
            return "/merge_requests";
        }

        @Override
        void bind(GitlabMergeRequest component) {}
    }
}
