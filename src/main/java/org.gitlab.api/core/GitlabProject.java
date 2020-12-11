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
public class GitlabProject implements GitlabComponent {
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

    @Override
    public Config getConfig() {
        return config;
    }

    @Override
    public GitlabProject withConfig(Config config) {
        this.config = config;
        return this;
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
     * @return
     */
    public GitlabBranch newBranch(String name) {
        return new GitlabBranch(name).withConfig(config).withProject(this);
    }

    public GitlabMergeRequest newMergeRequest(String sourceBranch, String targetBranch, String title) {
        return new GitlabMergeRequest(sourceBranch, targetBranch, title)
                .withConfig(config)
                .withProject(this);

    }

    public GitlabMergeRequest getMergeRequest(int mergeRequestIId) {
        return GitlabHttpClient.get(config, String.format(
                "/projects/%d/merge_requests/%d", id, mergeRequestIId), GitlabMergeRequest.class).withProject(this);
    }


    public GitlabUser.ProjectQuery users() {
        return new GitlabUser.ProjectQuery(config, id);
    }

    public GitlabBranch.Query branches() {
        return new GitlabBranch.Query(config, id);
    }

    public GitlabCommit.Query commits() {
        return new GitlabCommit.Query(config, id);
    }

    public GitlabIssue.ProjectQuery issues() {
        return new GitlabIssue.ProjectQuery(config, id);
    }

    public GitlabMergeRequest.ProjectQuery mergeRequests() {
        return new GitlabMergeRequest.ProjectQuery(config, id);
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
    public GitlabProject create() {
        return GitlabHttpClient.post(config, "/projects", new Body().putString("name", name), this);
    }

    /**
     * Delete this {@link GitlabProject}
     * It will send HTTP requests to the endpoint
     *
     * @return the previous {@link GitlabProject} before deleting
     */
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


    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getNameWithNamespace() {
        return nameWithNamespace;
    }

    public String getPathWithNamespace() {
        return pathWithNamespace;
    }

    public String getDefaultBranch() {
        return defaultBranch;
    }

    public String getVisibility() {
        return visibility;
    }

    public String getSshUrlToRepo() {
        return sshUrlToRepo;
    }

    public String getHttpUrlToRepo() {
        return httpUrlToRepo;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public String getReadmeUrl() {
        return readmeUrl;
    }

    public List<String> getTagList() {
        return tagList;
    }

    public GitlabUser getOwner() {
        return owner;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public boolean isIssuesEnabled() {
        return issuesEnabled;
    }

    public int getOpenIssuesCount() {
        return openIssuesCount;
    }

    public boolean isMergeRequestsEnabled() {
        return mergeRequestsEnabled;
    }

    public boolean isJobsEnabled() {
        return jobsEnabled;
    }

    public boolean isWikiEnabled() {
        return wikiEnabled;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getLastActivityAt() {
        return lastActivityAt;
    }

    public int getCreatorId() {
        return creatorId;
    }

    public boolean isArchived() {
        return archived;
    }

    public int getForksCount() {
        return forksCount;
    }

    public int getStarCount() {
        return starCount;
    }

    public boolean isPublicJobs() {
        return publicJobs;
    }

    public GitlabProject withDescription(String description) {
        this.description = description;
        return this;
    }


    /*
     * Setters
     * There will be no setter for id, nameWithNamespace, ssh_url_to_repo, http_url_to_repo, web_url, readme_url,
     * owner, with_issues_enabled, open_issues_count, with_merge_requests_enabled, created_at, last_activity_at,
     * last_activity_at, archived, forks_count, star_count, public_jobs
     *
     */

    public GitlabProject withDefaultBranch(String defaultBranch) {
        this.defaultBranch = defaultBranch;
        return this;
    }

    public GitlabProject withVisibility(String visibility) {
        this.visibility = visibility;
        return this;
    }

    public GitlabProject withTagList(List<String> tagList) {
        this.tagList = tagList;
        return this;
    }

    public GitlabProject withName(String name) {
        Objects.requireNonNull(name);
        this.name = name;
        return this;
    }

    public GitlabProject withPath(String path) {
        this.path = path;
        return this;
    }

    public GitlabProject withIssuesEnabled(boolean issuesEnabled) {
        this.issuesEnabled = issuesEnabled;
        return this;
    }

    public GitlabProject withJobsEnabled(boolean jobsEnabled) {
        this.jobsEnabled = jobsEnabled;
        return this;
    }


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

    public static class Query extends GitlabQuery<GitlabProject> {
        Query(Config config) {
            super(config, GitlabProject[].class);
        }

        public Query withArchived(boolean archived) {
            appendBoolean("archived", archived);
            return this;
        }


        public Query withIdAfter(int idAfter) {
            appendInt("id_after", idAfter);
            return this;
        }

        public Query withIdBefore(int idBefore) {
            appendInt("id_before", idBefore);
            return this;
        }

        public Query withLastActivityAfter(LocalDateTime lastActivityAfter) {
            appendDateTime("last_activity_after", lastActivityAfter);
            return this;
        }

        public Query withLastActivityBefore(LocalDateTime lastActivityBefore) {
            appendDateTime("last_activity_before", lastActivityBefore);
            return this;
        }

        public Query withMembership(boolean membership) {
            appendBoolean("membership", membership);
            return this;
        }

        public Query withMinAccessLevel(int minAccessLevel) {
            appendInt("min_access_level", minAccessLevel);
            return this;
        }

        public Query withOrderBy(String orderBy) {
            appendString("order_by", orderBy);
            return this;
        }

        public Query withOwned(boolean owned) {
            appendBoolean("owned", owned);
            return this;
        }

        public Query withRepositoryChecksumFailed(boolean repositoryChecksumFailed) {
            appendBoolean("repository_checksum_failed", repositoryChecksumFailed);
            return this;
        }

        public Query withRepositoryStorage(String repositoryStorage) {
            appendString("repository_storage", repositoryStorage);
            return this;
        }

        public Query withSearchNamespaces(boolean searchNamespaces) {
            appendBoolean("search_namespaces", searchNamespaces);
            return this;
        }

        public Query withSearch(String search) {
            appendString("search", search);
            return this;
        }

        public Query withSimple(boolean simple) {
            appendBoolean("simple", simple);
            return this;
        }

        public Query withSort(String sort) {
            appendString("sort", sort);
            return this;
        }

        public Query withStarred(boolean starred) {
            appendBoolean("starred", starred);
            return this;
        }

        public Query withStatistics(boolean statistics) {
            appendBoolean("statistics", statistics);
            return this;
        }

        public Query withVisibility(String visibility) {
            appendString("visibility", visibility);
            return this;
        }

        public Query withCheckSumFailed(boolean checkSumFailed) {
            appendBoolean("wiki_checksum_failed", checkSumFailed);
            return this;
        }

        public Query withCustomAttributes(boolean customAttributes) {
            appendBoolean("with_custom_attributes", customAttributes);
            return this;
        }

        public Query withIssuesEnabled(boolean issuesEnabled) {
            appendBoolean("with_issues_enabled", issuesEnabled);
            return this;
        }

        public Query withMergeRequestsEnabled(boolean mergeRequestsEnabled) {
            appendBoolean("with_merge_requests_enabled", mergeRequestsEnabled);
            return this;
        }

        public Query withProgrammingLanguage(String programmingLanguage) {
            appendString("with_programming_language", programmingLanguage);
            return this;
        }

        @Override
        public Query withPagination(Pagination pagination) {
            appendPagination(pagination);
            return this;
        }

        @Override
        public String getUrlPrefix() {
            return "/projects";
        }
    }

}
