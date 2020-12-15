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
 * This class is used to represent the gitlab merge request.
 * <p>
 * This class also contains a {@link ProjectQuery} Class used to build query and get merge requests within a project
 * and a {@link Query} class to get merge requests globally.
 * <p>
 * This class supports create, read, update and delete.
 * <p>
 * Gitlab Web API: https://docs.gitlab.com/ee/api/merge_requests.html
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class GitlabMergeRequest extends GitlabComponent {

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
     * Constructor of the gitlab merge request
     *
     * @param sourceBranch source branch
     * @param targetBranch target branch
     * @param title        title of the merge request
     */
    GitlabMergeRequest(@JsonProperty("source_branch") String sourceBranch,
                       @JsonProperty("target_branch") String targetBranch,
                       @JsonProperty("title") String title) {
        this.sourceBranch = sourceBranch;
        this.targetBranch = targetBranch;
        this.title = title;
    }

    /**
     * Issue a HTTP request to the Gitlab API endpoint to create this {@link GitlabMergeRequest} based on
     * the fields in this {@link GitlabMergeRequest} currently
     *
     * @return the created {@link GitlabMergeRequest} component
     * @throws GitlabException if {@link IOException} occurs or the response code is not in [200,400)
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
     * Issue a HTTP request to the Gitlab API endpoint to delete this {@link GitlabMergeRequest}
     *
     * @return the {@link GitlabMergeRequest} component before deleted
     * @throws GitlabException if {@link IOException} occurs or the response code is not in [200,400)
     */
    public GitlabMergeRequest delete() {
        httpClient.delete(String.format("/projects/%d/merge_requests/%d", projectId, iid));
        return this;
    }

    /**
     * Issue a HTTP request to the Gitlab API endpoint to update this {@link GitlabMergeRequest} based on
     * the fields in this {@link GitlabMergeRequest} currently
     *
     * @return the updated {@link GitlabMergeRequest} component
     * @throws GitlabException if {@link IOException} occurs or the response code is not in [200,400)
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
     * Issue a HTTP request to the Gitlab API endpoint to get a list of {@link GitlabUser} that participated in this {@link GitlabMergeRequest}
     *
     * @return list of {@link GitlabUser} that participated in the current merge request
     * @throws GitlabException if {@link IOException} occurs or the response code is not in [200,400)
     */
    public List<GitlabUser> getAllParticipants() {
        return httpClient.getList(
                String.format("/projects/%d/merge_requests/%d/participants", projectId, iid), GitlabUser[].class);
    }

    /**
     * Issue a HTTP request to the Gitlab API endpoint to get a list of {@link GitlabCommit} that committed in this {@link GitlabMergeRequest}
     *
     * @return list of {@link GitlabCommit} in the current merge request
     * @throws GitlabException if {@link IOException} occurs or the response code is not in [200,400)
     */
    public List<GitlabCommit> getAllCommits() {
        return httpClient.getList(String
                .format("/projects/%d/merge_requests/%d/commits", projectId, iid), GitlabCommit[].class);
    }

    /**
     * Issue a HTTP request to the Gitlab API endpoint to get a list of {@link GitlabIssue} that  will be closed after this {@link GitlabMergeRequest} is merged
     *
     * @return list of {@link GitlabIssue} that will be closed after commit is merged
     * @throws GitlabException if {@link IOException} occurs or the response code is not in [200,400)
     */
    public List<GitlabIssue> getAllIssuesClosedByMerge() {
        return httpClient.getList(String
                .format("/projects/%d/merge_requests/%d/closes_issues", projectId, iid), GitlabIssue[].class);
    }

    /**
     * Issue a HTTP request to the Gitlab API endpoint to accept this {@link GitlabMergeRequest}
     *
     * @return {@link GitlabMergeRequest} after merge request has been accepted
     * @throws GitlabException if {@link IOException} occurs or the response code is not in [200,400)
     */
    public GitlabMergeRequest accept() {
        return httpClient.put(String
                .format("/projects/%d/merge_requests/%d/merge", projectId, iid), null, this);
    }

    /**
     * Issue a HTTP request to the Gitlab API endpoint to accept this {@link GitlabMergeRequest}
     *
     * @return {@link GitlabMergeRequest} after merge request has been approved
     * @throws GitlabException if {@link IOException} occurs or the response code is not in [200,400)
     */
    public GitlabMergeRequest approve() {
        return httpClient.post(String
                .format("/projects/%d/merge_requests/%d/approve", projectId, iid), null, this);
    }

    /**
     * Issue a HTTP request to the Gitlab API endpoint to decline this {@link GitlabMergeRequest}
     *
     * @return {@link GitlabMergeRequest} after merge request has been decline
     * @throws GitlabException if {@link IOException} occurs or the response code is not in [200,400)
     */
    public GitlabMergeRequest decline() {
        return httpClient.post(String
                .format("/projects/%d/merge_requests/%d/unapprove", projectId, iid), null, this);
    }

    /**
     * Get the project id that the current merge request belongs to
     *
     * @return project id
     */
    public int getProjectId() {
        return projectId;
    }

    /**
     * Get the author of the merge request
     *
     * @return {@link GitlabUser} of the current merge request
     */
    public GitlabUser getAuthor() {
        return author;
    }

    /**
     * Get the description of the current merge request
     *
     * @return description of the merge request
     */
    public String getDescription() {
        return description;
    }

    /**
     * Get the current state of the merge request
     *
     * @return current state of the merge request
     */
    public String getState() {
        return state;
    }

    /**
     * Get a list of {@link GitlabUser} that are assigned to the merge request
     *
     * @return list of {@link GitlabUser} that are assigned to the merge request
     */
    public List<GitlabUser> getAssignees() {
        return assignees;
    }

    /**
     * Get the number of up votes that current merge request contain
     *
     * @return number of up votes
     */
    public int getUpvotes() {
        return upvotes;
    }

    /**
     * Get the number of down votes that current merge request contain
     *
     * @return number of down votes
     */
    public int getDownvotes() {
        return downvotes;
    }

    /**
     * Get the number of merge request count
     *
     * @return number of merge request count
     */
    public int getMergeRequestCount() {
        return mergeRequestCount;
    }

    /**
     * Get the title of the merge request
     *
     * @return Title of the current merge request
     */
    public String getTitle() {
        return title;
    }

    /**
     * Get the time when the merge request is updated
     *
     * @return time when the merge request is updated
     */
    public ZonedDateTime getUpdatedAt() {
        return updatedAt;
    }

    /**
     * Get the time when the merge request is created
     *
     * @return time when the merge request is created
     */
    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * Get the time when the merge request is closed
     *
     * @return time when the merge request is closed
     */
    public ZonedDateTime getClosedAt() {
        return closedAt;
    }

    /**
     * Get the {@link GitlabUser} who closed the merge request
     *
     * @return {@link GitlabUser} who closed the merge request
     */
    public GitlabUser getClosedBy() {
        return closedBy;
    }

    /**
     * Get whether or not current user is subscribed to a merge request
     *
     * @return whether or not current user is subscribed to a merge request
     */
    public boolean isSubscribed() {
        return subscribed;
    }

    /**
     * Get the web url to the current merge commit
     *
     * @return web url to the current merge commit
     */
    public String getWebUrl() {
        return webUrl;
    }

    /**
     * Get the target branch that current merge commit has
     *
     * @return target branch in the merge commit
     */
    public String getTargetBranch() {
        return targetBranch;
    }

    /**
     * Get the source branch that current merge commit has
     *
     * @return source branch in the merge commit
     */
    public String getSourceBranch() {
        return sourceBranch;
    }

    /**
     * Get the {@link GitlabProject} that current merge commit belongs to
     *
     * @return the {@link GitlabProject} that current merge commit belongs to
     */
    public GitlabProject getProject() {
        if (project == null) {
            project = GitlabProject.fromId(httpClient, projectId);
        }
        return project;
    }

    /**
     * Get the id of the merge request
     *
     * @return id of the merge request
     */
    public int getId() {
        return id;
    }

    /**
     * Get the internal id of the merge request
     *
     * @return internal id of the merge request
     */
    public int getIid() {
        return iid;
    }

    /**
     * Attach a project to this {@link GitlabMergeRequest}
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
     * Get all of the labels in current merge request
     *
     * @return a list of labels
     */
    public List<String> getLabels() {
        return labels;
    }

    /**
     * Set/update the title to the current merge request
     *
     * @param title title of the merge
     * @return a {@link GitlabMergeRequest} with given title
     */
    public GitlabMergeRequest withTitle(String title) {
        this.title = title;
        return this;
    }

    /**
     * Set/update the description to the current merge request
     *
     * @param description description of the merge
     * @return a {@link GitlabMergeRequest} with given description
     */
    public GitlabMergeRequest withDescription(String description) {
        this.description = description;
        return this;
    }

    /**
     * Set/update the description to the list of {@link GitlabUser} as assignees
     *
     * @param assignees list of {@link GitlabUser}
     * @return a {@link GitlabMergeRequest} with list of assignees
     */
    public GitlabMergeRequest withAssignees(List<GitlabUser> assignees) {
        this.assignees = assignees;
        return this;
    }

    /**
     * Set/update the targetBranch to the current merge request
     *
     * @param targetBranch targetBranch of the merge
     * @return a {@link GitlabMergeRequest} with given targetBranch
     */
    public GitlabMergeRequest withTargetBranch(String targetBranch) {
        this.targetBranch = targetBranch;
        return this;
    }

    /**
     * The string representation of this {@link GitlabMergeRequest}
     *
     * @return the string representation of this {@link GitlabMergeRequest}
     */
    @Override
    public String toString() {
        return "GitlabMergeRequest{" +
                "iid=" + iid +
                ", title=" + title +
                '}';
    }

    /**
     * Two {@link GitlabMergeRequest}s are equal if and only if they belong to the same project and have the same id
     *
     * @param o the reference object with which to compare.
     * @return if the two merge request belong to the same project and have the same merge request id
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GitlabMergeRequest that = (GitlabMergeRequest) o;
        return projectId == that.projectId && id == that.id;
    }

    /**
     * Two {@link GitlabMergeRequest}s will have the same hashcode if they belong to the same project and have the same id
     *
     * @return a hash code value for this object.
     */
    @Override
    public int hashCode() {
        return Objects.hash(projectId, id);
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
     * Class to query {@link GitlabMergeRequest} in a given {@link GitlabProject}
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
         * Set pagination on top of the query
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
         * Set a id of the project
         *
         * @param id the id of a project
         * @return the project query with project id
         */
        public ProjectQuery withId(String id) {
            appendString("id", id);
            return this;
        }

        /**
         * Set a list of iids to the query so the result will return request having the given iid
         *
         * @param iids list of iid
         * @return the project query with list of iids
         */
        public ProjectQuery withIids(List<Integer> iids) {
            appendInts("iids[]", iids);
            return this;
        }

        /**
         * Set a state to the query to return all merge requests or just those that are opened, closed, locked,
         * or merged
         *
         * @param state state of the merge requests
         * @return the project query with the given state
         */
        public ProjectQuery withState(String state) {
            appendString("state", state);
            return this;
        }

        /**
         * Set an order by to return requests ordered by created_at or updated_at fields. Default is created_at
         *
         * @param orderBy how to order the return request
         * @return the project query with the given order by
         */
        public ProjectQuery withOrderBy(String orderBy) {
            appendString("order_by", orderBy);
            return this;
        }

        /**
         * Set a sort to return requests sorted in asc or desc order. Default is desc
         *
         * @param sort how to sort the return request
         * @return the project query with the given sort
         */
        public ProjectQuery withSort(String sort) {
            appendString("sort", sort);
            return this;
        }

        /**
         * Set a milestone to Return merge requests for a specific milestone. None returns merge requests with no
         * milestone. Any returns merge requests that have an assigned milestone.
         *
         * @param milestone milestone to of all the resulting merge request
         * @return the project query with the given milestone
         */
        public ProjectQuery withMilestone(String milestone) {
            appendString("milestone", milestone);
            return this;
        }

        /**
         * Set a view, If simple, returns the iid, URL, title, description, and basic state of merge request
         *
         * @param view parameter on whether to return siomple or normal view
         * @return the project query with the given view
         */
        public ProjectQuery withView(String view) {
            appendString("view", view);
            return this;
        }

        /**
         * Set a list of labels, Return merge requests matching a comma separated list of labels. None lists all merge
         * requests with no labels. Any lists all merge requests with at least one label. No+Label (Deprecated) lists
         * all merge requests with no labels. Predefined names are case-insensitive.
         *
         * @param labels list of labels to add to the query
         * @return the project query with the given list of labels
         */
        public ProjectQuery withLabels(List<String> labels) {
            appendStrings("labels", labels);
            return this;
        }

        /**
         * Set whether or not to return detail labels on each merge request. If true, response will return more details
         * for each label in labels field: :name, :color, :description, :description_html, :text_color. Default is false.
         *
         * @param withLabelsDetails whether or not to return labels with detail
         * @return the project query with the given boolean
         */
        public ProjectQuery withWithLabelsDetails(boolean withLabelsDetails) {
            appendBoolean("with_labels_details", withLabelsDetails);
            return this;
        }

        /**
         * Set whether or not to asynchronously recalculate state
         *
         * @param withMergeStatusRecheck whether or not to asynchronously recalculate state
         * @return the project query with the given boolean
         */
        public ProjectQuery withWithMergeStatusRecheck(boolean withMergeStatusRecheck) {
            appendBoolean("with_merge_status_recheck", withMergeStatusRecheck);
            return this;
        }

        /**
         * Set parameter to get merge requests created on or after the given time.
         *
         * @param createdAfter get all merge requests after the date
         * @return the project query with the given created after
         */
        public ProjectQuery withCreatedAfter(ZonedDateTime createdAfter) {
            appendDateTime("created_after", createdAfter);
            return this;
        }

        /**
         * Set parameter to get merge requests created on or before the given time.
         *
         * @param createdBefore get all merge requests before the date
         * @return the project query with the given created before
         */
        public ProjectQuery withCreatedBefore(ZonedDateTime createdBefore) {
            appendDateTime("created_before", createdBefore);
            return this;
        }

        /**
         * Set parameter to get merge requests updated on or after the given time.
         *
         * @param updatedAfter get all merge requests updated after the date
         * @return the project query with the given updated after
         */
        public ProjectQuery withUpdatedAfter(ZonedDateTime updatedAfter) {
            appendDateTime("updated_after", updatedAfter);
            return this;
        }

        /**
         * Set parameter to get merge requests updated on or before the given time.
         *
         * @param updatedBefore get all merge requests updated before the date
         * @return the project query with the given updated before
         */
        public ProjectQuery withUpdatedBefore(ZonedDateTime updatedBefore) {
            appendDateTime("updated_before", updatedBefore);
            return this;
        }

        /**
         * Set a scope to the query and return merge request for the given scope
         *
         * @param scope scope of the merge request
         * @return the project query with the given scope
         */
        public ProjectQuery withScope(String scope) {
            appendString("scope", scope);
            return this;
        }

        /**
         * Set a author id to the query and return all merge requests created by the author id
         *
         * @param authorId id of the author
         * @return the project query with the given author id
         */
        public ProjectQuery withAuthorId(int authorId) {
            appendInt("author_id", authorId);
            return this;
        }

        /**
         * Set a author username to the query and return all merge requests created by the author username
         *
         * @param authorUsername id of the author
         * @return the project query with the given author username
         */
        public ProjectQuery withAuthorUsername(String authorUsername) {
            appendString("author_username", authorUsername);
            return this;
        }

        /**
         * Set a assignee id to the query and return all merge requests assigned to the user
         *
         * @param assigneeId id of the assignee
         * @return the project query with the given author username
         */
        public ProjectQuery withAssigneeId(int assigneeId) {
            appendInt("assignee_id", assigneeId);
            return this;
        }

        /**
         * Set a list of approver ids to the query and Returns merge requests which have specified all the users with
         * the given ids as individual approvers.
         *
         * @param approverIds list of approvers
         * @return the project query with the given list of approvers
         */
        public ProjectQuery withApproverIds(List<Integer> approverIds) {
            appendInts("approver_ids", approverIds);
            return this;
        }

        /**
         * Set a list of user ids to the query to Returns merge requests which have been approved by all the users with
         * the given ids (Max: 5). None returns merge requests with no approvals. Any returns merge requests with
         * an approval.
         *
         * @param approvedByIds list of user id that approved the merge request
         * @return the project query with given approver ids
         */
        public ProjectQuery withApprovedByIds(String approvedByIds) {
            appendString("approved_by_ids", approvedByIds);
            return this;
        }

        /**
         * Set reaction emoji to the query to Return merge requests reacted by the authenticated user by the given
         * emoji. None returns issues not given a reaction.
         *
         * @param myReactionEmoji a emoji represented by a string
         * @return the project query with given emoji
         */
        public ProjectQuery withMyReactionEmoji(String myReactionEmoji) {
            appendString("my_reaction_emoji", myReactionEmoji);
            return this;
        }

        /**
         * Set source branch and Return merge requests with the given source branch
         *
         * @param sourceBranch the source branch
         * @return the project query with given source branch
         */
        public ProjectQuery withSourceBranch(String sourceBranch) {
            appendString("source_branch", sourceBranch);
            return this;
        }

        /**
         * Set target branch and Return merge requests with the given target branch
         *
         * @param targetBranch the target branch
         * @return the project query with given target branch
         */
        public ProjectQuery withTargetBranch(String targetBranch) {
            appendString("target_branch", targetBranch);
            return this;
        }

        /**
         * Set search to the query so the request will return merge requests against their title and description
         *
         * @param search search keyword
         * @return the project query with given search keyword
         */
        public ProjectQuery withSearch(String search) {
            appendString("search", search);
            return this;
        }

        /**
         * Set wip status to query and Filter merge requests against their wip status. yes to return only WIP merge
         * requests, no to return non WIP merge requests
         *
         * @param wip wip status
         * @return the project query with given wip status
         */
        public ProjectQuery withWip(String wip) {
            appendString("wip", wip);
            return this;
        }

        /**
         * Get the URL suffix for the HTTP request
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
         * Bind the branch with the given {@link GitlabProject} after the response is parsed
         *
         * @param component - one {@link GitlabMergeRequest} from the response
         */
        @Override
        void bind(GitlabMergeRequest component) {
            component.withProject(project);
        }

    }

    /**
     * Class to query {@link GitlabMergeRequest} globally
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
         * Set pagination on top of the query
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
         * Set a state to the query to return all merge requests or just those that are opened, closed, locked,
         * or merged
         *
         * @param state state of the merge requests
         * @return the query with the given state
         */
        public Query withState(String state) {
            appendString("state", state);
            return this;
        }

        /**
         * Set an order by to return requests ordered by created_at or updated_at fields. Default is created_at
         *
         * @param orderBy how to order the return request
         * @return the query with the given order by
         */
        public Query withOrderBy(String orderBy) {
            appendString("order_by", orderBy);
            return this;
        }

        /**
         * Set a sort to return requests sorted in asc or desc order. Default is desc
         *
         * @param sort how to sort the return request
         * @return the query with the given sort
         */
        public Query withSort(String sort) {
            appendString("sort", sort);
            return this;
        }

        /**
         * Set a milestone to Return merge requests for a specific milestone. None returns merge requests with no
         * milestone. Any returns merge requests that have an assigned milestone.
         *
         * @param milestone milestone to of all the resulting merge request
         * @return the query with the given milestone
         */
        public Query withMilestone(String milestone) {
            appendString("milestone", milestone);
            return this;
        }

        /**
         * Set a view, If simple, returns the iid, URL, title, description, and basic state of merge request
         *
         * @param view parameter on whether to return siomple or normal view
         * @return the query with the given view
         */
        public Query withView(String view) {
            appendString("view", view);
            return this;
        }

        /**
         * Set a list of labels, Return merge requests matching a comma separated list of labels. None lists all merge
         * requests with no labels. Any lists all merge requests with at least one label. No+Label (Deprecated) lists
         * all merge requests with no labels. Predefined names are case-insensitive.
         *
         * @param labels list of labels to add to the query
         * @return the query with the given list of labels
         */
        public Query withLabels(List<String> labels) {
            appendStrings("labels", labels);
            return this;
        }

        /**
         * Set whether or not to return detail labels on each merge request. If true, response will return more details
         * for each label in labels field: :name, :color, :description, :description_html, :text_color. Default is false.
         *
         * @param withLabelsDetails whether or not to return labels with detail
         * @return the query with the given boolean
         */
        public Query withWithLabelsDetails(boolean withLabelsDetails) {
            appendBoolean("with_labels_details", withLabelsDetails);
            return this;
        }

        /**
         * Set whether or not to asynchronously recalculate state
         *
         * @param withMergeStatusRecheck whether or not to asynchronously recalculate state
         * @return the project query with the given boolean
         */
        public Query withWithMergeStatusRecheck(boolean withMergeStatusRecheck) {
            appendBoolean("with_merge_status_recheck", withMergeStatusRecheck);
            return this;
        }

        /**
         * Set parameter to get merge requests created on or after the given time.
         *
         * @param createdAfter get all merge requests after the date
         * @return the query with the given created after
         */
        public Query withCreatedAfter(ZonedDateTime createdAfter) {
            appendDateTime("created_after", createdAfter);
            return this;
        }

        /**
         * Set parameter to get merge requests created on or before the given time.
         *
         * @param createdBefore get all merge requests before the date
         * @return the query with the given created before
         */
        public Query withCreatedBefore(ZonedDateTime createdBefore) {
            appendDateTime("created_before", createdBefore);
            return this;
        }

        /**
         * Set parameter to get merge requests updated on or after the given time.
         *
         * @param updatedAfter get all merge requests updated after the date
         * @return the query with the given updated after
         */
        public Query withUpdatedAfter(ZonedDateTime updatedAfter) {
            appendDateTime("updated_after", updatedAfter);
            return this;
        }

        /**
         * Set parameter to get merge requests updated on or before the given time.
         *
         * @param updatedBefore get all merge requests updated before the date
         * @return the query with the given updated before
         */
        public Query withUpdatedBefore(ZonedDateTime updatedBefore) {
            appendDateTime("updated_before", updatedBefore);
            return this;
        }

        /**
         * Set a scope to the query and return merge request for the given scope
         *
         * @param scope scope of the merge request
         * @return the query with the given scope
         */
        public Query withScope(String scope) {
            appendString("scope", scope);
            return this;
        }

        /**
         * Set a author id to the query and return all merge requests created by the author id
         *
         * @param authorId id of the author
         * @return the query with the given author id
         */
        public Query withAuthorId(int authorId) {
            appendInt("author_id", authorId);
            return this;
        }

        /**
         * Set a author username to the query and return all merge requests created by the author username
         *
         * @param authorUsername id of the author
         * @return the query with the given author username
         */
        public Query withAuthorUsername(String authorUsername) {
            appendString("author_username", authorUsername);
            return this;
        }

        /**
         * Set a assignee id to the query and return all merge requests assigned to the user
         *
         * @param assigneeId id of the assignee
         * @return the query with the given author username
         */
        public Query withAssigneeId(int assigneeId) {
            appendInt("assignee_id", assigneeId);
            return this;
        }

        /**
         * Set a list of approver ids to the query and Returns merge requests which have specified all the users with
         * the given ids as individual approvers.
         *
         * @param approverIds list of approvers
         * @return the query with the given list of approvers
         */
        public Query withApproverIds(List<Integer> approverIds) {
            appendInts("approver_ids", approverIds);
            return this;
        }

        /**
         * Set a list of user ids to the query to Returns merge requests which have been approved by all the users with
         * the given ids (Max: 5). None returns merge requests with no approvals. Any returns merge requests with
         * an approval.
         *
         * @param approvedByIds list of user id that approved the merge request
         * @return the query with given approver ids
         */
        public Query withApprovedByIds(List<Integer> approvedByIds) {
            appendInts("approved_by_ids", approvedByIds);
            return this;
        }

        /**
         * Set reaction emoji to the query to Return merge requests reacted by the authenticated user by the given
         * emoji. None returns issues not given a reaction.
         *
         * @param myReactionEmoji a emoji represented by a string
         * @return the query with given emoji
         */
        public Query withMyReactionEmoji(String myReactionEmoji) {
            appendString("my_reaction_emoji", myReactionEmoji);
            return this;
        }

        /**
         * Set source branch and Return merge requests with the given source branch
         *
         * @param sourceBranch the source branch
         * @return the query with given source branch
         */
        public Query withSourceBranch(String sourceBranch) {
            appendString("source_branch", sourceBranch);
            return this;
        }

        /**
         * Set target branch and Return merge requests with the given target branch
         *
         * @param targetBranch the target branch
         * @return the query with given target branch
         */
        public Query withTargetBranch(String targetBranch) {
            appendString("target_branch", targetBranch);
            return this;
        }

        /**
         * Set search to the query so the request will return merge requests against their title and description
         *
         * @param search search keyword
         * @return the query with given search keyword
         */
        public Query withSearch(String search) {
            appendString("search", search);
            return this;
        }

        /**
         * Set in parameter to Modify the scope of the search attribute. title, description, or a string joining
         * them with comma. Default is title,description
         *
         * @param in scope of the search
         * @return the query with given scope
         */
        public Query withIn(String in) {
            appendString("in", in);
            return this;
        }

        /**
         * Set wip status to query and Filter merge requests against their wip status. yes to return only WIP merge
         * requests, no to return non WIP merge requests
         *
         * @param wip wip status
         * @return the query with given wip status
         */
        public Query withWip(String wip) {
            appendString("wip", wip);
            return this;
        }

        /**
         * Set a parameter so the return merge request that do not match the parameters supplied. Accepts: labels,
         * milestone, author_id, author_username, assignee_id, assignee_username, my_reaction_emoji
         *
         * @param not parameter to filter merge requests
         * @return the query with the given parameter
         */
        public Query withNot(String not) {
            appendString("not", not);
            return this;
        }

        /**
         * Set a parameter for the environment to returns merge requests deployed to the given environment.
         *
         * @param environment specific environment to filter
         * @return the query with the given environment
         */
        public Query withEnvironment(String environment) {
            appendString("environment", environment);
            return this;
        }

        /**
         * Set a date parameter to return merge requests deployed before the given datetime.
         *
         * @param deployedBefore a date to add to the query
         * @return the query with the given date
         */
        public Query withDeployedBefore(ZonedDateTime deployedBefore) {
            appendDateTime("deployed_before", deployedBefore);
            return this;
        }

        /**
         * Set a date parameter to return merge requests deployed after the given datetime.
         *
         * @param deployedAfter a date to add to the query
         * @return the query with the given date
         */
        public Query withDeployedAfter(ZonedDateTime deployedAfter) {
            appendDateTime("deployed_after", deployedAfter);
            return this;
        }

        /**
         * Get the URL suffix for the HTTP request
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
        void bind(GitlabMergeRequest component) {
        }
    }

}
