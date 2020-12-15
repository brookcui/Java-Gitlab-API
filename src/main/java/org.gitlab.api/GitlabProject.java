package org.gitlab.api;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
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
 * This class serves as instance of Gitlab component Project.
 *
 * To create, update, or delete this issue, call {@code create()},
 * {@code update()}, or {@code delete()} explicitly.
 *
 * This supports query for projects globally that are visible by current user or
 * projects owned by a specific user. See {@link Query} and {@link UserQuery}.
 *
 * <p>
 * Gitlab Web API: https://docs.gitlab.com/ee/api/projects.html
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class GitlabProject extends GitlabComponent {
    @JsonProperty("id")
    private int id; // required
    private String description;
    @JsonProperty("name_with_namespace")
    private String nameWithNamespace;
    @JsonProperty("path_with_namespace")
    private String pathWithNamespace;
    @JsonProperty("default_branch")
    private String defaultBranch;
    private String visibility;
    @JsonProperty("ssh_url_to_repo")
    private String sshUrlToRepo;
    @JsonProperty("http_url_to_repo")
    private String httpUrlToRepo;
    @JsonProperty("web_url")
    private String webUrl;
    @JsonProperty("readme_url")
    private String readmeUrl;
    @JsonProperty("tag_list")
    private List<String> tagList = new ArrayList<>();
    @JsonProperty("owner")
    private GitlabUser owner;
    @JsonProperty("name")
    private String name; // required
    @JsonProperty("path")
    private String path;
    @JsonProperty("issues_enabled")
    private boolean issuesEnabled;
    @JsonProperty("open_issues_count")
    private int openIssuesCount;
    @JsonProperty("merge_requests_enabled")
    private boolean mergeRequestsEnabled;
    @JsonProperty("jobs_enabled")
    private boolean jobsEnabled;
    @JsonProperty("wiki_enabled")
    private boolean wikiEnabled;
    @JsonProperty("created_at")
    @JsonDeserialize(using = DateUtil.ZonedDeserializer.class)
    @JsonSerialize(using = DateUtil.ZonedSerializer.class)
    private ZonedDateTime createdAt;
    @JsonProperty("last_activity_at")
    @JsonDeserialize(using = DateUtil.ZonedDeserializer.class)
    @JsonSerialize(using = DateUtil.ZonedSerializer.class)
    private ZonedDateTime lastActivityAt;
    @JsonProperty("creator_id")
    private int creatorId;
    @JsonProperty("archived")
    private boolean archived;
    @JsonProperty("forks_count")
    private int forksCount;
    @JsonProperty("star_count")
    private int starCount;
    @JsonProperty("public_jobs")
    private boolean publicJobs;

    /**
     * Construct the {@link GitlabProject} with project name.
     *
     * @param name - the name of this project
     */
    GitlabProject(@JsonProperty("name") String name) {
        this.name = name;
    }

    static GitlabProject fromId(HttpClient httpClient, int id) {
        return httpClient.get("/projects/" + id, GitlabProject.class);
    }

    /**
     * Returns a string representation of this {@link GitlabProject} in the
     * format of Gitlab component type and project id and project name.
     *
     * @return a string representation of this {@link GitlabProject}
     */
    @Override
    public String toString() {
        return "GitlabProject{" +
                       "id=" + id +
                       ", name=" + name +
                       '}';
    }

    /**
     * Returns the hash code value for this {@link GitlabProject} identified
     * by project id.
     *
     * @return a hash code value for this object
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    /**
     * Compares the specified {@code Object} with this {@link GitlabProject}
     * for equality. Note that two {@link GitlabProject}s are equal if and only
     * if they have same project id.
     *
     * @param o object to be compared for equality with this {@link GitlabProject}
     * @return true if the specified Object is equal to this {@link GitlabProject}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GitlabProject)) {
            return false;
        }
        GitlabProject that = (GitlabProject) o;
        return id == that.id;
    }

    /**
     * Issues a HTTP request to Gitlab API endpoint to get issue based on given
     * issue internal id in this {@link GitlabProject}.
     *
     * <p>
     * https://docs.gitlab.com/ee/api/issues.html#single-project-issue
     * <p>
     * GET /projects/:id/issues/:issue_iid
     *
     * @param issueIId - the given issueIId
     * @return the {@link GitlabIssue} of the given issueIId
     * @throws GitlabException if {@link IOException} occurs or API endpoint fails
     * to give a valid response (response code within [200,400))
     */
    public GitlabIssue getIssue(int issueIId) {
        return httpClient.get(String.format("/projects/%d/issues/%d", id, issueIId), GitlabIssue.class)
                         .withProject(this);
    }

    /**
     * Returns a newly created {@link GitlabIssue} with given issue title that
     * can build parameters for the new issue and remember to call
     * {@link GitlabIssue#create()} to create this issue explicitly.
     *
     * @param title title of new issue
     * @return a {@link GitlabIssue} instance
     */
    public GitlabIssue newIssue(String title) {
        return new GitlabIssue(title).withHttpClient(httpClient).withProject(this);
    }


    /**
     * Issues a HTTP request to Gitlab API endpoint to get a single commit
     * based on given commit hash or name of a repository branch or tag from
     * this {@link GitlabProject}
     *
     * <p>
     * https://docs.gitlab.com/ee/api/commits.html#get-a-single-commit
     * <p>
     * GET /projects/:id/repository/commits/:sha
     *
     * @param sha - commit hash or name of a repository branch or tag
     * @return {@link GitlabCommit} of the sha
     * @throws GitlabException if {@link IOException} occurs or API endpoint fails
     * to give a valid response (response code within [200,400))
     */
    public GitlabCommit getCommit(String sha) {
        return httpClient.get(String.format("/projects/%d/repository/commits/%s", id, sha), GitlabCommit.class)
                         .withProject(this);
    }


    /**
     * Issues a HTTP request to Gitlab API endpoint to get a single branch
     * based on given branch name.
     *
     * <p>
     * https://docs.gitlab.com/ee/api/branches.html#get-single-repository-branch
     * <p>
     * GET /projects/:id/repository/branches/:branch
     *
     * @param name - the name of the branch
     * @return the {@link GitlabBranch} of given branch name
     * @throws GitlabException if {@link IOException} occurs or API endpoint fails
     * to give a valid response (response code within [200,400))
     */
    public GitlabBranch getBranch(String name) {
        return httpClient.get(String.format("/projects/%d/repository/branches/%s", id, name), GitlabBranch.class)
                         .withProject(this);
    }

    /**
     * Returns a newly created {@link GitlabBranch} with given branch name and
     * ref that can build parameters for the new branch and remember to call
     * {@link GitlabBranch#create()} to create this issue explicitly.
     *
     * @param name - the name of the new branch
     * @param ref  -  reference of the branch
     * @return a {@link GitlabBranch} instance
     */
    public GitlabBranch newBranch(String name, String ref) {
        return new GitlabBranch(name, ref).withHttpClient(httpClient).withProject(this);
    }

    /**
     * Returns a newly created {@link GitlabMergeRequest} with given source
     * branch and target branch and title that can build parameters for the new
     * merge request and remember to call {@link GitlabBranch#create()} to
     * create this merge request explicitly.
     *
     * @param sourceBranch - the source branch of the merge request
     * @param targetBranch - the target branch of the merge request
     * @param title        - the title of the merge request
     * @return a new {@link GitlabMergeRequest} instance created
     */
    public GitlabMergeRequest newMergeRequest(String sourceBranch, String targetBranch, String title) {
        return new GitlabMergeRequest(sourceBranch, targetBranch, title)
                .withHttpClient(httpClient)
                .withProject(this);
    }

    /**
     * Issues a HTTP request to Gitlab API endpoint to get a single merge
     * request based on given internal id in this {@link GitlabProject}.
     *
     * <p>
     * Gitlab Web API: https://docs.gitlab.com/ee/api/merge_requests.html#get-single-mr
     * <p>
     * GET /projects/:id/merge_requests/:merge_request_iid
     *
     * @param mergeRequestIId - internal id of the merge request
     * @return the {@link GitlabMergeRequest} of the given branch name
     * @throws GitlabException if {@link IOException} occurs or API endpoint fails
     * to give a valid response (response code within [200,400))
     */
    public GitlabMergeRequest getMergeRequest(int mergeRequestIId) {
        return httpClient.get(
                String.format("/projects/%d/merge_requests/%d", id, mergeRequestIId), GitlabMergeRequest.class)
                         .withProject(this);
    }

    /**
     * Returns a {@link GitlabUser.ProjectQuery} that can be used to query
     * users that belong to this project.
     *
     * <p>
     * Gitlab Web API: https://docs.gitlab.com/ee/api/projects.html#get-project-users
     * <p>
     * GET /projects/:id/users
     *
     * @return a {@link GitlabUser.ProjectQuery}
     */
    public GitlabUser.ProjectQuery getUsersQuery() {
        return new GitlabUser.ProjectQuery(httpClient, id);
    }

    /**
     * Returns a {@link GitlabBranch.ProjectQuery} that can be used to query
     * branches that belong to this project.
     *
     * <p>
     * Gitlab Web API: https://docs.gitlab.com/ee/api/branches.html#list-repository-branches
     * <p>
     * GET /projects/:id/repository/branches
     *
     * @return a {@link GitlabBranch.ProjectQuery}
     */
    public GitlabBranch.ProjectQuery getBranchesQuery() {
        return new GitlabBranch.ProjectQuery(httpClient, this);
    }

    /**
     * Returns a {@link GitlabCommit.ProjectQuery} that can be used to query
     * commits that belong to this project.
     *
     * <p>
     * Gitlab Web API: https://docs.gitlab.com/ee/api/commits.html#list-repository-commits
     * <p>
     * GET /projects/:id/repository/commits
     *
     * @return a {@link GitlabCommit.ProjectQuery}
     */
    public GitlabCommit.ProjectQuery getCommitsQuery() {
        return new GitlabCommit.ProjectQuery(httpClient, this);
    }

    /**
     * Returns a {@link GitlabIssue.ProjectQuery} that can be used to query
     * issues that belong to this project.
     * <p>
     * Gitlab Web API: https://docs.gitlab.com/ee/api/issues.html#list-project-issues
     * <p>
     * GET /projects/:id/issues
     *
     * @return a {@link GitlabIssue.ProjectQuery}
     */
    public GitlabIssue.ProjectQuery getIssuesQuery() {
        return new GitlabIssue.ProjectQuery(httpClient, this);
    }

    /**
     * Returns a {@link GitlabMergeRequest.ProjectQuery} that can be used to
     * query merge request that belong to this project.
     * <p>
     * Gitlab Web API: https://docs.gitlab.com/ee/api/merge_requests.html#list-project-merge-requests
     * <p>
     * GET /projects/:id/merge_requests
     *
     * @return a {@link GitlabMergeRequest.ProjectQuery}
     */
    public GitlabMergeRequest.ProjectQuery getMergeRequestsQuery() {
        return new GitlabMergeRequest.ProjectQuery(httpClient, this);
    }

    /**
     * Issues a HTTP request to Gitlab API endpoint to fork this
     * {@link GitlabProject} into current user's repo.
     *
     * <p>
     * https://docs.gitlab.com/ee/api/projects.html#fork-project
     * <p>
     * POST /projects/:id/fork
     *
     * @return the new GitlabProject which is the result of forking this project
     * @throws GitlabException if {@link IOException} occurs or API endpoint fails
     * to give a valid response (response code within [200,400))
     */
    public GitlabProject fork() {
        return httpClient.get(String.format("/projects/%d/fork", id), GitlabProject.class);
    }

    /**
     * Issues a HTTP request to Gitlab API endpoint to create a project based
     * on this {@link GitlabProject}.
     *
     * @return the created {@link GitlabProject} instance
     * @throws GitlabException if {@link IOException} occurs or API endpoint fails
     * to give a valid response (response code within [200,400))
     */
    public GitlabProject create() {
        return httpClient.post("/projects", new Body().putString("name", name), this);
    }

    /**
     * Issues a HTTP request to Gitlab API endpoint to delete a project
     * based on this project id of this {@link GitlabProject}.
     *
     * @return the {@link GitlabProject} instance before deleted
     * @throws GitlabException if {@link IOException} occurs or API endpoint fails
     * to give a valid response (response code within [200,400))
     */
    public GitlabProject delete() {
        httpClient.delete("/projects/" + id);
        return this;
    }

    /**
     * Issues a HTTP request to Gitlab API endpoint to update this
     * {@link GitlabProject} based on its current fields.
     *
     * @return the updated {@link GitlabProject} instance
     * @throws GitlabException if {@link IOException} occurs or API endpoint fails
     * to give a valid response (response code within [200,400))
     */
    public GitlabProject update() {
        Body body = new Body()
                .putString("name", name)
                .putString("path", path)
                .putString("description", description)
                .putString("default_branch", defaultBranch)
                .putString("visibility", visibility)
                .putStringArray("tag_list", tagList)
                .putBoolean("issues_enabled", issuesEnabled)
                .putBoolean("jobs_enabled", jobsEnabled)
                .putBoolean("wiki_enabled", wikiEnabled);
        return httpClient.put("/projects/" + id, body, this);
    }


    /**
     * Returns the ID of this project.
     *
     * @return id of this project
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the description of this project.
     *
     * @return description of this project
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns the name along with namespace.
     *
     * @return name along with namespace
     */
    public String getNameWithNamespace() {
        return nameWithNamespace;
    }

    /**
     * Returns the path along with namespace.
     *
     * @return path with namespace
     */
    public String getPathWithNamespace() {
        return pathWithNamespace;
    }

    /**
     * Returns the default branch of this project.
     *
     * @return default branch of this project
     */
    public String getDefaultBranch() {
        return defaultBranch;
    }

    /**
     * Returns the current visibility of this project.
     *
     * @return current visibility of this project
     */
    public String getVisibility() {
        return visibility;
    }

    /**
     * Returns the current ssh url to repo.
     *
     * @return ssh url to repo
     */
    public String getSshUrlToRepo() {
        return sshUrlToRepo;
    }

    /**
     * Returns the current http url to repo.
     *
     * @return http url to repo
     */
    public String getHttpUrlToRepo() {
        return httpUrlToRepo;
    }

    /**
     * Returns the web url of this project.
     *
     * @return web url of this project
     */
    public String getWebUrl() {
        return webUrl;
    }

    /**
     * Returns the web url of the README.
     *
     * @return web url of the README
     */
    public String getReadmeUrl() {
        return readmeUrl;
    }

    /**
     * Returns tags of this project.
     *
     * @return a list of tags of the project
     */
    public List<String> getTagList() {
        return tagList;
    }

    /**
     * Returns the owner of this project.
     *
     * @return owner of the project
     */
    public GitlabUser getOwner() {
        return owner;
    }

    /**
     * Returns the name of this project.
     *
     * @return name of this project
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the path of this project.
     *
     * @return path of this project
     */
    public String getPath() {
        return path;
    }

    /**
     * Tests if issues are enabled for this project.
     *
     * @return true if issues are enabled for this project
     */
    public boolean isIssuesEnabled() {
        return issuesEnabled;
    }

    /**
     * Returns the number of open issue counts.
     *
     * @return number of open issue counts
     */
    public int getOpenIssuesCount() {
        return openIssuesCount;
    }

    /**
     * Tests if merge requests are enabled for this project.
     *
     * @return true if merge requests are enabled for this project
     */
    public boolean isMergeRequestsEnabled() {
        return mergeRequestsEnabled;
    }

    /**
     * Tests if jobs are enabled for this project.
     *
     * @return true if jobs are enabled for this project
     */
    public boolean isJobsEnabled() {
        return jobsEnabled;
    }

    /**
     * Tests if wiki is enabled for this project.
     *
     * @return if wiki is enabled for this project
     */
    public boolean isWikiEnabled() {
        return wikiEnabled;
    }

    /**
     * Returns the created date of project.
     *
     * @return created date
     */
    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * Returns the date of last activity.
     *
     * @return date of last activity
     */
    public ZonedDateTime getLastActivityAt() {
        return lastActivityAt;
    }

    /**
     * Returns the id of the creator.
     *
     * @return id of the creator
     */
    public int getCreatorId() {
        return creatorId;
    }

    /**
     * Tests if this project has been archived.
     *
     * @return true if this project has been archived
     */
    public boolean isArchived() {
        return archived;
    }

    /**
     * Returns the number of forked counts of this project.
     *
     * @return the number of forked counts
     */
    public int getForksCount() {
        return forksCount;
    }

    /**
     * Returns the number of star count of this project.
     *
     * @return the number of star counts
     */
    public int getStarCount() {
        return starCount;
    }

    /**
     * Tests if this project is a public job.
     *
     * @return true if this project is a public job
     */
    public boolean isPublicJobs() {
        return publicJobs;
    }


    /*
     * Note that there will be no setters for id, nameWithNamespace,
     * ssh_url_to_repo, http_url_to_repo, web_url, readme_url, owner,
     * with_issues_enabled, open_issues_count, with_merge_requests_enabled,
     * created_at, last_activity_at, last_activity_at, archived, forks_count,
     * star_count, public_jobs because they are not modifiable.
     */

    /**
     * Sets the description of this project.
     *
     * @param description new description
     * @return updated {@link GitlabProject} with new description
     */
    public GitlabProject withDescription(String description) {
        this.description = description;
        return this;
    }

    /**
     * Sets the defaultBranch of this project.
     *
     * @param defaultBranch new defaultBranch
     * @return updated {@link GitlabProject} with new defaultBranch
     */
    public GitlabProject withDefaultBranch(String defaultBranch) {
        this.defaultBranch = defaultBranch;
        return this;
    }

    /**
     * Sets the visibility of this project.
     *
     * @param visibility new visibility
     * @return updated {@link GitlabProject} with new visibility
     */
    public GitlabProject withVisibility(String visibility) {
        this.visibility = visibility;
        return this;
    }

    /**
     * Sets the tag List of this project.
     *
     * @param tagList new list of tags
     * @return updated {@link GitlabProject} with new tag List
     */
    public GitlabProject withTagList(List<String> tagList) {
        this.tagList = tagList;
        return this;
    }

    /**
     * Sets the name of this project.
     *
     * @param name new name
     * @return updated {@link GitlabProject} with new name
     */
    public GitlabProject withName(String name) {
        Objects.requireNonNull(name);
        this.name = name;
        return this;
    }

    /**
     * Sets the path of this project.
     *
     * @param path new path
     * @return updated {@link GitlabProject} with new path
     */
    public GitlabProject withPath(String path) {
        this.path = path;
        return this;
    }

    /**
     * Enables this project's issues if issuesEnabled is true; otherwise,
     * disable issues.
     *
     * @param issuesEnabled whether issues are enabled
     * @return updated {@link GitlabProject} with whether issues are enabled
     */
    public GitlabProject withIssuesEnabled(boolean issuesEnabled) {
        this.issuesEnabled = issuesEnabled;
        return this;
    }

    /**
     * Enables this project's jobs if jobEnabled is true; otherwise, disable
     * jobs.
     *
     * @param jobsEnabled whether jobs are enabled
     * @return updated {@link GitlabProject} with whether jobs are enabled
     */
    public GitlabProject withJobsEnabled(boolean jobsEnabled) {
        this.jobsEnabled = jobsEnabled;
        return this;
    }

    /**
     * Enables this project's wiki if wikiEnabled is true; otherwise, disable
     * wiki.
     *
     * @param wikiEnabled whether wiki are enabled
     * @return updated {@link GitlabProject} with whether wiki are enabled
     */
    public GitlabProject withWikiEnabled(boolean wikiEnabled) {
        this.wikiEnabled = wikiEnabled;
        return this;
    }

    /**
     * Sets a httpClient to the current {@link GitlabAPIClient}.
     *
     * @param httpClient a httpclient used for making request
     * @return {@link GitlabProject} with the httpClient
     */
    @Override
    GitlabProject withHttpClient(HttpClient httpClient) {
        super.withHttpClient(httpClient);
        return this;
    }

    /**
     * This extends {@link GitlabQuery} and supports query global projects.
     *
     * Build this query with setters and call {@code query()} to execute query.
     *
     * <p>
     * Gitlab Web API: https://docs.gitlab.com/ee/api/projects.html#list-all-projects
     * <p>
     * GET /projects
     */
    @JsonIgnoreType
    public static class Query extends GitlabQuery<GitlabProject> {
        Query(HttpClient httpClient) {
            super(httpClient, GitlabProject[].class);
        }

        /**
         * Returns a query that returns only archived projects if archived is
         * true.
         *
         * @param archived archived status
         * @return this {@link Query} with the given archived status
         */
        public Query withArchived(boolean archived) {
            appendBoolean("archived", archived);
            return this;
        }

        /**
         * Returns a query that returns only projects with IDs greater than
         * given project id.
         *
         * @param idAfter a project id
         * @return this {@link Query} with the given project id
         */
        public Query withIdAfter(int idAfter) {
            appendInt("id_after", idAfter);
            return this;
        }

        /**
         * Returns a query that returns only projects with IDs less than given
         * project id.
         *
         * @param idBefore a project id
         * @return this {@link Query} with the given project id
         */
        public Query withIdBefore(int idBefore) {
            appendInt("id_before", idBefore);
            return this;
        }

        /**
         * Returns a query that returns only projects with last_activity after
         * specified time.
         *
         * @param lastActivityAfter a date of last activity
         * @return this {@link Query} with the given date
         */
        public Query withLastActivityAfter(ZonedDateTime lastActivityAfter) {
            appendDateTime("last_activity_after", lastActivityAfter);
            return this;
        }

        /**
         * Returns a query that returns only projects with last_activity before
         * specified time.
         *
         * @param lastActivityBefore a date of last activity
         * @return this {@link Query} with the given date
         */
        public Query withLastActivityBefore(ZonedDateTime lastActivityBefore) {
            appendDateTime("last_activity_before", lastActivityBefore);
            return this;
        }

        /**
         * Returns a query that returns only projects that the current user is
         * a member of.
         *
         * @param membership whether or not to limit by membership
         * @return this {@link Query} with the given boolean whether or not is membership
         */
        public Query withMembership(boolean membership) {
            appendBoolean("membership", membership);
            return this;
        }

        /**
         * Returns a query that matches current user's minimal access level.
         *
         * @param minAccessLevel minimal access level
         * @return this {@link Query} with the given boolean whether or not is membership
         */
        public Query withMinAccessLevel(int minAccessLevel) {
            appendInt("min_access_level", minAccessLevel);
            return this;
        }

        /**
         * Returns a query that returns results in given order.
         *
         * order can be any of id, name, path, created_at, updated_at,
         * last_activity_at fields. Default value is created_at.
         *
         * Note that admins can slo specify repository_size, storage_size,
         * packages_size or wiki_size fields.
         *
         * @param orderBy a way to order all of the responds
         * @return this {@link GitlabIssue.ProjectQuery} with orderby
         */
        /**
         * Set a order by toe the query and return  projects ordered by
         *
         * @param orderBy how to order the response
         * @return this {@link Query} with the order by
         */
        public Query withOrderBy(String orderBy) {
            appendString("order_by", orderBy);
            return this;
        }

        /**
         * Returns a query that matches projects owned by current user
         *
         * @param owned owned by current user or not
         * @return this {@link Query} with the owned
         */
        public Query withOwned(boolean owned) {
            appendBoolean("owned", owned);
            return this;
        }

        /**
         * Returns a query that returns projects that fail to match checksum
         * if repositoryChecksumFailed is true.
         *
         * @param repositoryChecksumFailed whether or not check sum failed
         * @return this {@link Query} with whether or not check sum failed
         */
        public Query withRepositoryChecksumFailed(boolean repositoryChecksumFailed) {
            appendBoolean("repository_checksum_failed", repositoryChecksumFailed);
            return this;
        }

        /**
         * Returns a query that returns projects stored on repository_storage.
         * (admins only)
         *
         * @param repositoryStorage whether repo is stored in repository storage
         * @return this {@link Query} with whether or project is in the repostory storage
         */
        public Query withRepositoryStorage(String repositoryStorage) {
            appendString("repository_storage", repositoryStorage);
            return this;
        }

        /**
         * Returns a query that includes ancestor namespaces when matching
         * search criteria if searchNamespaces is true. Default is false.
         *
         * @param searchNamespaces include ancestor namespaces or not
         * @return this {@link Query} with whether to include ancestor namespaces
         */
        public Query withSearchNamespaces(boolean searchNamespaces) {
            appendBoolean("search_namespaces", searchNamespaces);
            return this;
        }

        /**
         * Returns a query that searches projects against their title and
         * description.
         *
         * @param search keyword to be searched
         * @return this {@link GitlabIssue.ProjectQuery} with search
         */
        public Query withSearch(String search) {
            appendString("search", search);
            return this;
        }

        /**
         * Returns a query that returns results in simple version if simple is
         * true.
         *
         * @param simple whether or not o return simple version of the response
         * @return this {@link Query} with whether or not o return simple version of the response
         */
        public Query withSimple(boolean simple) {
            appendBoolean("simple", simple);
            return this;
        }

        /**
         * Returns a query that returns results in sorted order.
         *
         * Sort order can be any of "asc" or "desc". Default value is "desc".
         *
         * @param sort ways to sort the response
         * @return this {@link GitlabIssue.ProjectQuery} with sort
         */
        public Query withSort(String sort) {
            appendString("sort", sort);
            return this;
        }

        /**
         * Returns a query that returns projects starred by current user is
         * starred is true.
         *
         * @param starred whether to limit by projects starred by the current user
         * @return this {@link Query} with whether to limit by projects starred by the current user
         */
        public Query withStarred(boolean starred) {
            appendBoolean("starred", starred);
            return this;
        }

        /**
         * Returns a query that returns results with statistics if statistics
         * is true.
         *
         * @param statistics whether or not to include project statistics
         * @return this {@link Query} with whether or not to include project statistics
         */
        public Query withStatistics(boolean statistics) {
            appendBoolean("statistics", statistics);
            return this;
        }

        /**
         * Returns a query that sets visibility.
         *
         * visibility can be any of public, internal, or private.
         *
         * @param visibility visibility of the desired projects
         * @return this {@link Query} with given visibility
         */
        public Query withVisibility(String visibility) {
            appendString("visibility", visibility);
            return this;
        }

        /**
         * Returns a query that returns projects if the wiki checksum fails if
         * checkSumFailed is true.
         *
         * @param checkSumFailed whether to limit projects where the wiki checksum calculation has failed
         * @return this {@link Query} with whether to limit projects where the wiki checksum calculation has failed
         */
        public Query withCheckSumFailed(boolean checkSumFailed) {
            appendBoolean("wiki_checksum_failed", checkSumFailed);
            return this;
        }

        /**
         * Returns a query that returns results with custom attributes if
         * customAttributes is true.
         *
         * @param customAttributes whether to include custom attributes in the response
         * @return this {@link Query} with whether to include custom attributes in the response
         */
        public Query withCustomAttributes(boolean customAttributes) {
            appendBoolean("with_custom_attributes", customAttributes);
            return this;
        }

        /**
         * Returns a query that returns projects only if issues are enabled if
         * mergeRequestsEnabled is true.
         *
         * @param issuesEnabled whether to limit by enabled issues feature
         * @return this {@link Query} with whether to limit by enabled issues feature
         */
        public Query withIssuesEnabled(boolean issuesEnabled) {
            appendBoolean("with_issues_enabled", issuesEnabled);
            return this;
        }

        /**
         * Returns a query that returns projects only if merge requests are
         * enabled if mergeRequestsEnabled is true.
         *
         * @param mergeRequestsEnabled whether to limit by enabled merge request feature
         * @return this {@link Query} with whether to limit by enabled merge request  feature
         */
        public Query withMergeRequestsEnabled(boolean mergeRequestsEnabled) {
            appendBoolean("with_merge_requests_enabled", mergeRequestsEnabled);
            return this;
        }

        /**
         * Returns a query that matches given programming language.
         *
         * @param programmingLanguage intended programming language
         * @return this {@link Query} with the programming language
         */
        public Query withProgrammingLanguage(String programmingLanguage) {
            appendString("with_programming_language", programmingLanguage);
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
         * Returns the URL suffix for the HTTP request.
         *
         * <p>
         * Gitlab Web API: https://docs.gitlab.com/ee/api/projects.html#list-all-projects
         * <p>
         * GET /projects
         *
         * @return The URL suffix to query {@link GitlabProject}
         */
        @Override
        String getTailUrl() {
            return "/projects";
        }

        @Override
        void bind(GitlabProject component) {

        }
    }

    /**
     * Class to query {@link GitlabProject} owned by a specific user
     * <p>
     * Gitlab Web API: https://docs.gitlab.com/ee/api/projects.html#list-user-projects
     * <p>
     * GET /users/:user_id/projects
     */
    @JsonIgnoreType
    public static class UserQuery extends Query {
        private final String usernameOrId;

        UserQuery(HttpClient httpClient, String usernameOrId) {
            super(httpClient);
            this.usernameOrId = usernameOrId;
        }

        /**
         * Get the URL suffix for the HTTP request
         * <p>
         * Gitlab Web API: https://docs.gitlab.com/ee/api/projects.html#list-user-projects
         * <p>
         * GET /users/:user_id/projects
         *
         * @return The URL suffix to query {@link GitlabProject}
         */
        @Override
        String getTailUrl() {
            return String.format("/users/%s/projects", usernameOrId);
        }
    }
}
