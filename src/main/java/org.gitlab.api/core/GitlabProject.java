package org.gitlab.api.core;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.gitlab.api.http.Body;
import org.gitlab.api.http.Config;
import org.gitlab.api.http.GitlabHttpClient;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @throws IllegalArgumentException
 * @throws NullPointerException
 * @throws UnsupportedOperationException
 * @throws IOException
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class GitlabProject implements GitlabModifiableComponent<GitlabModifiableComponent> {
    @JsonIgnore
    private Config config;
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
    private LocalDateTime createdAt;
    @JsonProperty("last_activity_at")
    private LocalDateTime lastActivityAt;
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
     * Construct the project with name
     * TODO: package private or protected
     *
     * @param name - the name of the new project
     */
    public GitlabProject(@JsonProperty("name") String name) {
        this.name = name;
    }

    static GitlabProject fromId(Config config, int id) {
        return GitlabHttpClient.get(config, "/projects/" + id, GitlabProject.class);
    }

    /**
     * Get the issue based on the given issueIId (The internal ID of a project’s issue)
     * https://docs.gitlab.com/ee/api/issues.html#single-project-issue
     * GET /projects/:id/issues/:issue_iid
     *
     * @param issueIId - the given issueIId
     * @return the {@link GitlabIssue} of the given issueIId
     */
    public GitlabIssue getIssue(int issueIId) {
        return GitlabHttpClient.get(config, String.format("/projects/%d/issues/%d", id, issueIId), GitlabIssue.class)
                .withProject(this);
    }

    /**
     * Construct a new issue from this project of the given name
     * No HTTP request will be issued until you call {@link GitlabIssue#create()}
     *
     * @param title - the title of the issue
     * @return the new issue from this project
     */
    public GitlabIssue newIssue(String title) {
        return new GitlabIssue(title).withConfig(config).withProject(this);
    }


    /**
     * TODO: rename sha?
     * Get a single commit based on the given commit hash or name of a repository branch or tag
     * https://docs.gitlab.com/ee/api/commits.html#get-a-single-commit
     * GET /projects/:id/repository/commits/:sha
     *
     * @param sha - commit hash or name of a repository branch or tag
     * @return
     */
    public GitlabCommit getCommit(String sha) {
        return GitlabHttpClient.get(config, String.format("/projects/%d/repository/commits/%s", id, sha),
                GitlabCommit.class).withProject(this);
    }


    /**
     * Get a single project repository branch based on the branch name
     * https://docs.gitlab.com/ee/api/branches.html#get-single-repository-branch
     * GET /projects/:id/repository/branches/:branch
     *
     * @param name - the name of the branch
     * @return the {@link GitlabBranch} of the given branch name
     */
    public GitlabBranch getBranch(String name) {
        return GitlabHttpClient
                .get(config, String.format("/projects/%d/repository/branches/%s", id, name), GitlabBranch.class)
                .withProject(this);
    }

    /**
     * Create a new branch from this project
     * No HTTP request will be issued until you call {@link GitlabBranch#()}
     *
     * @param name - the name of the new branch
     * @return a new {@link GitlabBranch} instance created with name
     */
    public GitlabBranch newBranch(String name, String ref) {
        return new GitlabBranch(name, ref).withConfig(config).withProject(this);
    }

    /**
     * Create a new merge request from this project
     * No HTTP request will be issued until you call {@link GitlabMergeRequest#()}
     *
     * @param sourceBranch - the source branch of the merge request
     * @param targetBranch - the target branch of the merge request
     * @param title        - the title of the merge request
     * @return a new {@link GitlabMergeRequest} instance created
     */
    public GitlabMergeRequest newMergeRequest(String sourceBranch, String targetBranch, String title) {
        return new GitlabMergeRequest(sourceBranch, targetBranch, title)
                .withConfig(config)
                .withProject(this);
    }

    /**
     * Get a single merge request based on the internal id
     * Gitlab Web API: https://docs.gitlab.com/ee/api/merge_requests.html#get-single-mr
     * GET /projects/:id/merge_requests/:merge_request_iid
     *
     * @param mergeRequestIId - internal id of the merge request
     * @return the {@link GitlabMergeRequest} of the given branch name
     */
    public GitlabMergeRequest getMergeRequest(int mergeRequestIId) {
        return GitlabHttpClient.get(config, String.format(
                "/projects/%d/merge_requests/%d", id, mergeRequestIId), GitlabMergeRequest.class).withProject(this);
    }

    /**
     * Get the users list of a project
     * Gitlab Web API: https://docs.gitlab.com/ee/api/projects.html#get-project-users
     * GET /projects/:id/users
     *
     * @return A list of {@link GitlabUser} of the current project
     */
    public GitlabUser.ProjectQuery users() {
        return new GitlabUser.ProjectQuery(config, id);
    }

    /**
     * Get a list of repository branches from current project, sorted by name alphabetically
     * Gitlab Web API: https://docs.gitlab.com/ee/api/branches.html#list-repository-branches
     * GET /projects/:id/repository/branches
     *
     * @return a list of branches from current project
     */
    public GitlabBranch.ProjectQuery branches() {
        return new GitlabBranch.ProjectQuery(config, this);
    }

    /**
     * Get a list of repository commits in a project
     * Gitlab Web API: https://docs.gitlab.com/ee/api/commits.html#list-repository-commits
     * GET /projects/:id/repository/commits
     *
     * @return a list of commits in current project
     */
    public GitlabCommit.ProjectQuery commits() {
        return new GitlabCommit.ProjectQuery(config, this);
    }

    /**
     * Get a list of current project's issues
     * Gitlab Web API: https://docs.gitlab.com/ee/api/issues.html#list-project-issues
     * GET /projects/:id/issues
     *
     * @return a list of current project's issues
     */
    public GitlabIssue.ProjectQuery issues() {
        return new GitlabIssue.ProjectQuery(config, this);
    }

    /**
     * Get a list of current project's merge requests
     * Gitlab Web API: https://docs.gitlab.com/ee/api/merge_requests.html#list-project-merge-requests
     * GET /projects/:id/merge_requests
     *
     * @return a list of current project's merge requests
     */
    public GitlabMergeRequest.ProjectQuery mergeRequests() {
        return new GitlabMergeRequest.ProjectQuery(config, this);
    }


    /**
     * Fork the project into current user's repo
     * https://docs.gitlab.com/ee/api/projects.html#fork-project
     * POST /projects/:id/fork
     *
     * @return the new GitlabProject which is the result of forking this project
     */
    public GitlabProject fork() {
        return GitlabHttpClient.get(config, String.format("/projects/%d/fork", id), GitlabProject.class);
    }

    /**
     * Create a new {@link GitlabProject} with the given fields
     * It will send HTTP requests to the endpoint
     *
     * @return the new {@link GitlabProject} after creating
     */
    @Override
    public GitlabProject create() {
        return GitlabHttpClient.post(config, "/projects", new Body().putString("name", name), this);
    }

    /**
     * Delete this {@link GitlabProject}
     * It will send HTTP requests to the endpoint
     *
     * @return the previous {@link GitlabProject} before deleting
     */
    @Override
    public GitlabProject delete() {
        GitlabHttpClient.delete(config, "/projects/" + id);
        return this;
    }

    /**
     * Update this {@link GitlabProject}
     * It will send HTTP requests to the endpoint
     *
     * @return the updated {@link GitlabProject}
     */
    @Override
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
        return GitlabHttpClient.put(config, "/projects/" + id, body, this);
    }


    /**
     * Get the ID of the project
     *
     * @return id of the project
     */
    public int getId() {
        return id;
    }

    /**
     * Get the description of the project
     *
     * @return description of the project
     */
    public String getDescription() {
        return description;
    }

    /**
     * Get the name along with namespace
     *
     * @return name along with namespace
     */
    public String getNameWithNamespace() {
        return nameWithNamespace;
    }

    /**
     * Get the path along with namespace
     *
     * @return path with namespace
     */
    public String getPathWithNamespace() {
        return pathWithNamespace;
    }

    /**
     * Get the default branch of the project
     *
     * @return default branch of the project
     */
    public String getDefaultBranch() {
        return defaultBranch;
    }

    /**
     * Get the current visibility of the project
     *
     * @return current visibility of the project
     */
    public String getVisibility() {
        return visibility;
    }

    /**
     * get the current ssh url to repo
     *
     * @return ssh url to repo
     */
    public String getSshUrlToRepo() {
        return sshUrlToRepo;
    }

    /**
     * get the current http url to repo
     *
     * @return http url to repo
     */
    public String getHttpUrlToRepo() {
        return httpUrlToRepo;
    }

    /**
     * Get the web url of the project
     *
     * @return web url of the project
     */
    public String getWebUrl() {
        return webUrl;
    }

    /**
     * Get the web url of the readme
     *
     * @return web url of the readme
     */
    public String getReadmeUrl() {
        return readmeUrl;
    }

    /**
     * Get a list of tags of the project
     *
     * @return list of tags of the porject
     */
    public List<String> getTagList() {
        return tagList;
    }

    /**
     * get the owner of the project
     *
     * @return owner of the porject
     */
    public GitlabUser getOwner() {
        return owner;
    }

    /**
     * Get the name of the project
     *
     * @return name of the project
     */
    public String getName() {
        return name;
    }

    /**
     * get the path of the project
     *
     * @return path of the project
     */
    public String getPath() {
        return path;
    }

    /**
     * Get if issues are enabled
     *
     * @return if issues are enabled
     */
    public boolean isIssuesEnabled() {
        return issuesEnabled;
    }

    /**
     * Get the number of open issue counts
     *
     * @return number of open issue counts
     */
    public int getOpenIssuesCount() {
        return openIssuesCount;
    }

    /**
     * Get whether merge requests are enabled
     *
     * @return whether merge requests are enabled
     */
    public boolean isMergeRequestsEnabled() {
        return mergeRequestsEnabled;
    }

    /**
     * Get whether jobs are enabled
     *
     * @return whether jobs are enabled
     */
    public boolean isJobsEnabled() {
        return jobsEnabled;
    }

    /**
     * Get whether Wiki is enabled for the project
     *
     * @return whether wiki is enabled
     */
    public boolean isWikiEnabled() {
        return wikiEnabled;
    }

    /**
     * Get the created date of project
     *
     * @return created date
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * Get the date of last activity
     *
     * @return date of last activity
     */
    public LocalDateTime getLastActivityAt() {
        return lastActivityAt;
    }

    /**
     * Get the id of the creator
     *
     * @return id of the creator
     */
    public int getCreatorId() {
        return creatorId;
    }

    /**
     * Get whether or not project is archived
     *
     * @return whether project is archived
     */
    public boolean isArchived() {
        return archived;
    }

    /**
     * Get number of forked counts of the current project
     *
     * @return number of forked counts
     */
    public int getForksCount() {
        return forksCount;
    }

    /**
     * Get number of star count of current project
     *
     * @return number of star counts
     */
    public int getStarCount() {
        return starCount;
    }

    /**
     * Get whether current project is a public job
     *
     * @return whether current project is a public job
     */
    public boolean isPublicJobs() {
        return publicJobs;
    }


    /*
     * Setters
     * There will be no setter for id, nameWithNamespace, ssh_url_to_repo, http_url_to_repo, web_url, readme_url,
     * owner, with_issues_enabled, open_issues_count, with_merge_requests_enabled, created_at, last_activity_at,
     * last_activity_at, archived, forks_count, star_count, public_jobs
     *
     */

    /**
     * Set the description of current project
     *
     * @param description new description
     * @return updated {@link GitlabProject} with new description
     */
    public GitlabProject withDescription(String description) {
        this.description = description;
        return this;
    }

    /**
     * Set the defaultBranch of current project
     *
     * @param defaultBranch new defaultBranch
     * @return updated {@link GitlabProject} with new defaultBranch
     */
    public GitlabProject withDefaultBranch(String defaultBranch) {
        this.defaultBranch = defaultBranch;
        return this;
    }

    /**
     * Set the visibility of current project
     *
     * @param visibility new visibility
     * @return updated {@link GitlabProject} with new visibility
     */
    public GitlabProject withVisibility(String visibility) {
        this.visibility = visibility;
        return this;
    }

    /**
     * Set the tag List of current project
     *
     * @param tagList new list of tags
     * @return updated {@link GitlabProject} with new tag List
     */
    public GitlabProject withTagList(List<String> tagList) {
        this.tagList = tagList;
        return this;
    }

    /**
     * Set the name of current project
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
     * Set the path of current project
     *
     * @param path new path
     * @return updated {@link GitlabProject} with new path
     */
    public GitlabProject withPath(String path) {
        this.path = path;
        return this;
    }

    /**
     * Set the whether issues are enabled
     *
     * @param issuesEnabled whether issues are enabled
     * @return updated {@link GitlabProject} with whether issues are enabled
     */
    public GitlabProject withIssuesEnabled(boolean issuesEnabled) {
        this.issuesEnabled = issuesEnabled;
        return this;
    }

    /**
     * Set the whether jobs are enabled
     *
     * @param jobsEnabled whether jobs are enabled
     * @return updated {@link GitlabProject} with whether jobs are enabled
     */
    public GitlabProject withJobsEnabled(boolean jobsEnabled) {
        this.jobsEnabled = jobsEnabled;
        return this;
    }

    /**
     * Set the whether wiki are enabled
     *
     * @param wikiEnabled whether wiki are enabled
     * @return updated {@link GitlabProject} with whether wiki are enabled
     */
    public GitlabProject withWikiEnabled(boolean wikiEnabled) {
        this.wikiEnabled = wikiEnabled;
        return this;
    }


    @Override
    public String toString() {
        return "GitlabProject{" +
                "id=" + id +
                ", description=" + description +
                ", nameWithNamespace=" + nameWithNamespace +
                ", pathWithNamespace=" + pathWithNamespace +
                ", defaultBranch=" + defaultBranch +
                ", visibility=" + visibility +
                ", sshUrlToRepo=" + sshUrlToRepo +
                ", httpUrlToRepo=" + httpUrlToRepo +
                ", webUrl=" + webUrl +
                ", readmeUrl=" + readmeUrl +
                ", tagList=" + tagList +
                ", owner=" + owner +
                ", name=" + name +
                ", path=" + path +
                ", issuesEnabled=" + issuesEnabled +
                ", openIssuesCount=" + openIssuesCount +
                ", mergeRequestsEnabled=" + mergeRequestsEnabled +
                ", jobsEnabled=" + jobsEnabled +
                ", wikiEnabled=" + wikiEnabled +
                ", createdAt=" + createdAt +
                ", lastActivityAt=" + lastActivityAt +
                ", creatorId=" + creatorId +
                ", archived=" + archived +
                ", forksCount=" + forksCount +
                ", starCount=" + starCount +
                ", publicJobs=" + publicJobs +
                '}';
    }

    /**
     * Get the config that is stored in current {@link GitlabProject}
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
     * @return {@link GitlabProject} with the config
     */
    @Override
    public GitlabProject withConfig(Config config) {
        this.config = config;
        return this;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GitlabProject project = (GitlabProject) o;
        return id == project.id &&
                issuesEnabled == project.issuesEnabled &&
                openIssuesCount == project.openIssuesCount &&
                mergeRequestsEnabled == project.mergeRequestsEnabled &&
                jobsEnabled == project.jobsEnabled &&
                wikiEnabled == project.wikiEnabled &&
                creatorId == project.creatorId &&
                archived == project.archived &&
                forksCount == project.forksCount &&
                starCount == project.starCount &&
                publicJobs == project.publicJobs &&
                Objects.equals(description, project.description) &&
                Objects.equals(nameWithNamespace, project.nameWithNamespace) &&
                Objects.equals(pathWithNamespace, project.pathWithNamespace) &&
                Objects.equals(defaultBranch, project.defaultBranch) &&
                Objects.equals(visibility, project.visibility) &&
                Objects.equals(sshUrlToRepo, project.sshUrlToRepo) &&
                Objects.equals(httpUrlToRepo, project.httpUrlToRepo) &&
                Objects.equals(webUrl, project.webUrl) &&
                Objects.equals(readmeUrl, project.readmeUrl) &&
                Objects.equals(tagList, project.tagList) &&
                Objects.equals(owner, project.owner) &&
                Objects.equals(name, project.name) &&
                Objects.equals(path, project.path) &&
                Objects.equals(createdAt, project.createdAt) &&
                Objects.equals(lastActivityAt, project.lastActivityAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    /**
     * Class to query {@link GitlabProject}
     * Gitlab Web API: https://docs.gitlab.com/ee/api/projects.html#list-all-projects
     */
    public static class Query extends GitlabQuery<GitlabProject> {
        Query(Config config) {
            super(config, GitlabProject[].class);
        }

        /**
         * Add a archived parameter to limit the result by archived status
         *
         * @param archived archived status
         * @return this {@link Query} with the given archived status
         */
        public Query withArchived(boolean archived) {
            appendBoolean("archived", archived);
            return this;
        }

        /**
         * Add a id to limit the results to projects with IDs greater than the specified ID
         *
         * @param idAfter a project id
         * @return this {@link Query} with the given project id
         */
        public Query withIdAfter(int idAfter) {
            appendInt("id_after", idAfter);
            return this;
        }

        /**
         * Add a id to limit the results to projects with IDs less than the specified ID
         *
         * @param idBefore a project id
         * @return this {@link Query} with the given project id
         */
        public Query withIdBefore(int idBefore) {
            appendInt("id_before", idBefore);
            return this;
        }

        /**
         * Add a date to the query adn limit results to projects with last_activity after specified time
         *
         * @param lastActivityAfter a date of last activity
         * @return this {@link Query} with the given date
         */
        public Query withLastActivityAfter(LocalDateTime lastActivityAfter) {
            appendDateTime("last_activity_after", lastActivityAfter);
            return this;
        }

        /**
         * Add a date to the query adn limit results to projects with last_activity before specified time
         *
         * @param lastActivityBefore a date of last activity
         * @return this {@link Query} with the given date
         */
        public Query withLastActivityBefore(LocalDateTime lastActivityBefore) {
            appendDateTime("last_activity_before", lastActivityBefore);
            return this;
        }

        /**
         * Add a membership to the query and limit by projects that the current user is a member of
         *
         * @param membership whether or not to limit by membership
         * @return this {@link Query} with the given boolean whether or not is membership
         */
        public Query withMembership(boolean membership) {
            appendBoolean("membership", membership);
            return this;
        }

        /**
         * Add a minimal access level to the query and limit by current user minimal access level.
         *
         * @param minAccessLevel minimal access level
         * @return this {@link Query} with the given boolean whether or not is membership
         */
        public Query withMinAccessLevel(int minAccessLevel) {
            appendInt("min_access_level", minAccessLevel);
            return this;
        }

        /**
         * Add a order by toe the query and return  projects ordered by id, name, path, created_at, updated_at, or
         * last_activity_at fields. repository_size, storage_size, packages_size or wiki_size fields are only allowed
         * for admins. Default is created_at.
         *
         * @param orderBy how to order the response
         * @return this {@link Query} with the order by
         */
        public Query withOrderBy(String orderBy) {
            appendString("order_by", orderBy);
            return this;
        }

        /**
         * Add whether to retrieve project with owen by current user
         *
         * @param owned owned by current user or not
         * @return this {@link Query} with the owned
         */
        public Query withOwned(boolean owned) {
            appendBoolean("owned", owned);
            return this;
        }

        /**
         * Add whether to limit projects where the repository checksum calculation has failed
         *
         * @param repositoryChecksumFailed whether or not check sum failed
         * @return this {@link Query} with whether or not check sum failed
         */
        public Query withRepositoryChecksumFailed(boolean repositoryChecksumFailed) {
            appendBoolean("repository_checksum_failed", repositoryChecksumFailed);
            return this;
        }

        /**
         * Add whether to limit results to projects stored on repository_storage. (admins only)
         *
         * @param repositoryStorage whether repo is stored in repository storage
         * @return this {@link Query} with whether or project is in the repostory storage
         */
        public Query withRepositoryStorage(String repositoryStorage) {
            appendString("repository_storage", repositoryStorage);
            return this;
        }

        /**
         * whether or not to include ancestor namespaces when matching search criteria. Default is false
         *
         * @param searchNamespaces include ancestor namespaces or not
         * @return this {@link Query} with whether to include ancestor namespaces
         */
        public Query withSearchNamespaces(boolean searchNamespaces) {
            appendBoolean("search_namespaces", searchNamespaces);
            return this;
        }

        /**
         * Add a search to the query and return list of projects matching the search criteria
         *
         * @param search a search key word
         * @return this {@link Query} with the search keyword
         */
        public Query withSearch(String search) {
            appendString("search", search);
            return this;
        }

        /**
         * Add whether or not to return simple version of the response
         *
         * @param simple whether or not o return simple version of the response
         * @return this {@link Query} with whether or not o return simple version of the response
         */
        public Query withSimple(boolean simple) {
            appendBoolean("simple", simple);
            return this;
        }

        /**
         * Add a sort to return projects sorted in asc or desc order. Default is desc
         *
         * @param sort how to sort the response
         * @return this {@link Query} with the given sort
         */
        public Query withSort(String sort) {
            appendString("sort", sort);
            return this;
        }

        /**
         * Add whether to limit by projects starred by the current user
         *
         * @param starred whether to limit by projects starred by the current user
         * @return this {@link Query} with whether to limit by projects starred by the current user
         */
        public Query withStarred(boolean starred) {
            appendBoolean("starred", starred);
            return this;
        }

        /**
         * Add whether or not to include project statistics
         *
         * @param statistics whether or not to include project statistics
         * @return this {@link Query} with whether or not to include project statistics
         */
        public Query withStatistics(boolean statistics) {
            appendBoolean("statistics", statistics);
            return this;
        }

        /**
         * Add a visibility to limit by visibility public, internal, or private
         *
         * @param visibility visibility of the desired projects
         * @return this {@link Query} with given visibility
         */
        public Query withVisibility(String visibility) {
            appendString("visibility", visibility);
            return this;
        }

        /**
         * Add whether to limit projects where the wiki checksum calculation has failed
         *
         * @param checkSumFailed whether to limit projects where the wiki checksum calculation has failed
         * @return this {@link Query} with whether to limit projects where the wiki checksum calculation has failed
         */
        public Query withCheckSumFailed(boolean checkSumFailed) {
            appendBoolean("wiki_checksum_failed", checkSumFailed);
            return this;
        }

        /**
         * Add whether to include custom attributes in the response
         *
         * @param customAttributes whether to include custom attributes in the response
         * @return this {@link Query} with whether to include custom attributes in the response
         */
        public Query withCustomAttributes(boolean customAttributes) {
            appendBoolean("with_custom_attributes", customAttributes);
            return this;
        }

        /**
         * Add whether to limit by enabled issues feature
         *
         * @param issuesEnabled whether to limit by enabled issues feature
         * @return this {@link Query} with whether to limit by enabled issues feature
         */
        public Query withIssuesEnabled(boolean issuesEnabled) {
            appendBoolean("with_issues_enabled", issuesEnabled);
            return this;
        }

        /**
         * Add whether to limit by enabled merge request feature
         *
         * @param mergeRequestsEnabled whether to limit by enabled merge request  feature
         * @return this {@link Query} with whether to limit by enabled merge request  feature
         */
        public Query withMergeRequestsEnabled(boolean mergeRequestsEnabled) {
            appendBoolean("with_merge_requests_enabled", mergeRequestsEnabled);
            return this;
        }

        /**
         * add a programming language to limit by projects which use the given programming language
         *
         * @param programmingLanguage intended programming language
         * @return this {@link Query} with the programming language
         */
        public Query withProgrammingLanguage(String programmingLanguage) {
            appendString("with_programming_language", programmingLanguage);
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
         * @return The URL suffix to query {@link GitlabProject}
         */
        @Override
        public String getUrlSuffix() {
            return "/projects";
        }

        @Override
        void bind(GitlabProject component) {

        }
    }

}
