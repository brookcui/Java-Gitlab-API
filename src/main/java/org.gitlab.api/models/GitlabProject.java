package org.gitlab.api.models;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.gitlab.api.core.Pagination;
import org.gitlab.api.http.GitlabHTTPRequestor;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @throws IllegalArgumentException
 * @throws NullPointerException
 * @throws UnsupportedOperationException
 * @throws IOException
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY,
        getterVisibility = JsonAutoDetect.Visibility.NONE
)
public class GitlabProject extends GitlabComponent {
    private int id; // required
    private String description;
    @JsonProperty(value = "name_with_namespace", access = JsonProperty.Access.WRITE_ONLY)
    private String nameWithNamespace;

    @JsonProperty(value = "path_with_namespace", access = JsonProperty.Access.WRITE_ONLY)
    private String pathWithNamespace;

    @JsonProperty(value = "default_branch", access = JsonProperty.Access.WRITE_ONLY)
    private String defaultBranch;

    private String visibility;

    @JsonProperty(value = "ssh_url_to_repo", access = JsonProperty.Access.WRITE_ONLY)
    private String sshUrlToRepo;

    @JsonProperty(value = "http_url_to_repo", access = JsonProperty.Access.WRITE_ONLY)
    private String httpUrlToRepo;

    @JsonProperty(value = "web_url", access = JsonProperty.Access.WRITE_ONLY)
    private String webUrl;

    @JsonProperty(value = "readme_url", access = JsonProperty.Access.WRITE_ONLY)
    private String readmeUrl;

    @JsonProperty("tag_list")
    private List<String> tagList = new ArrayList<>();

    @JsonProperty(value = "owner", access = JsonProperty.Access.WRITE_ONLY)
    private GitlabUser owner;

    @JsonProperty(value = "name")
    private String name; // required

    @JsonProperty(value = "path")
    private String path;

    @JsonProperty(value = "issues_enabled", access = JsonProperty.Access.WRITE_ONLY)
    private boolean issuesEnabled;

    @JsonProperty(value = "open_issues_count", access = JsonProperty.Access.WRITE_ONLY)
    private int openIssuesCount;

    @JsonProperty("merge_requests_enabled")
    private boolean mergeRequestsEnabled;

    @JsonProperty("jobs_enabled")
    private boolean jobsEnabled;

    @JsonProperty("wiki_enabled")
    private boolean wikiEnabled;

    @JsonProperty(value = "created_at", access = JsonProperty.Access.WRITE_ONLY)
    private LocalDateTime createdAt;

    @JsonProperty(value = "last_activity_at", access = JsonProperty.Access.WRITE_ONLY)
    private LocalDateTime lastActivityAt;

    @JsonProperty(value = "creator_id", access = JsonProperty.Access.WRITE_ONLY)
    private int creatorId;

    @JsonProperty(value = "archived", access = JsonProperty.Access.WRITE_ONLY)
    private boolean archived;

    @JsonProperty(value = "forks_count", access = JsonProperty.Access.WRITE_ONLY)
    private int forksCount;

    @JsonProperty(value = "star_count", access = JsonProperty.Access.WRITE_ONLY)
    private int starCount;

    @JsonProperty(value = "public_jobs", access = JsonProperty.Access.WRITE_ONLY)
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

    @Override
    public GitlabProject withHTTPRequestor(GitlabHTTPRequestor requestor) {
        super.withHTTPRequestor(requestor);
        if (owner != null) {
            owner.withHTTPRequestor(requestor);
        }
        return this;
    }

    /**
     * Get the users list of this project, using the default pagination
     * GET /projects/:id/users
     * https://docs.gitlab.com/ee/api/projects.html#get-project-users
     *
     * @return a list of {@link GitlabUser}s
     * @throws IOException
     */
    public List<GitlabUser> getUsers() throws IOException {
        return null; // TODO
    }

    /**
     * Get all the issues of this project, using the default pagination
     * https://docs.gitlab.com/ee/api/issues.html#list-project-issues
     * GET /projects/:id/issues
     *
     * @return a list of {@link GitlabIssue}s
     * @throws IOException
     */
    public List<GitlabIssue> getAllIssues() throws IOException {
        return null; // TODO
    }

    /**
     * Get all the issues of this project, using the given pagination
     * https://docs.gitlab.com/ee/api/issues.html#list-project-issues
     * GET /projects/:id/issues
     *
     * @param pagination - the given pagination
     * @return a list of {@link GitlabIssue}s
     * @throws IOException
     */
    public List<GitlabIssue> getAllIssues(Pagination pagination) throws IOException {
        return null; // TODO
    }

    /**
     * Get the issue based on the given issueIId (The internal ID of a project’s issue)
     * https://docs.gitlab.com/ee/api/issues.html#single-project-issue
     * GET /projects/:id/issues/:issue_iid
     *
     * @param issueIId - the given issueIId
     * @return the {@link GitlabIssue} of the given issueIId
     * @throws IOException
     */
    public GitlabIssue getIssue(int issueIId) throws IOException {
        GitlabIssue issue = newIssue(null);
        return getHTTPRequestor().get(String.format("/projects/%d/issues/%d", id, issueIId), issue);
    }

    /**
     * Construct a new issue from this project of the given name
     * No HTTP request will be issued until you call {@link GitlabIssue#create()}
     *
     * @param title - the title of the issue
     * @return the new issue from this project
     */
    public GitlabIssue newIssue(String title) {
        return new GitlabIssue(title).withHTTPRequestor(getHTTPRequestor()).withProject(this);
    }

    /*
     * Commits
     */
    // TODO: branch.getAllCommits()? project.getAllCommits("branch1")?

    /**
     * Get all the commits of this project in the default branch, using the default pagination
     * https://docs.gitlab.com/ee/api/commits.html#list-repository-commits
     * GET /projects/:id/repository/commits
     *
     * @return a list of {@link GitlabCommit}s
     * @throws IOException
     */
    public List<GitlabCommit> getAllCommits() throws IOException {
        return null; // TODO
    }

    /**
     * Get all the commits of this project in the default branch, using the given pagination
     * https://docs.gitlab.com/ee/api/commits.html#list-repository-commits
     * GET /projects/:id/repository/commits
     *
     * @param pagination - the given pagination
     * @return a list of {@link GitlabCommit}s
     * @throws IOException
     */
    public List<GitlabCommit> getAllCommits(Pagination pagination) throws IOException {
        return null; // TODO
    }

    /**
     * TODO: rename sha?
     * Get a single commit based on the given commit hash or name of a repository branch or tag
     * https://docs.gitlab.com/ee/api/commits.html#get-a-single-commit
     * GET /projects/:id/repository/commits/:sha
     *
     * @param sha - commit hash or name of a repository branch or tag
     * @return
     * @throws IOException
     */
    public GitlabCommit getCommit(String sha) throws IOException {
        GitlabCommit commit = new GitlabCommit(sha).withHTTPRequestor(getHTTPRequestor()).withProject(this);
        return getHTTPRequestor().get(String.format("/projects/%d/repository/commits/%s", id, sha), commit);
    }

    /*
     * Branches
     */

    /**
     * Get a list of repository branches from a project, sorted by name alphabetically, using the default pagination
     * https://docs.gitlab.com/ee/api/branches.html#list-repository-branches
     * GET /projects/:id/repository/branches
     *
     * @return the list of {@link GitlabBranch}s
     * @throws IOException
     */
    public List<GitlabBranch> getAllBranches() throws IOException {
        return null; // TODO
    }

    /**
     * Get a list of repository branches from a project, sorted by name alphabetically, using the given pagination
     * https://docs.gitlab.com/ee/api/branches.html#list-repository-branches
     * GET /projects/:id/repository/branches
     *
     * @param pagination - the given pagination
     * @return the list of {@link GitlabBranch}s
     * @throws IOException
     */
    public List<GitlabBranch> getAllBranches(Pagination pagination) throws IOException {
        return null; // TODO
    }

    /**
     * Get a single project repository branch based on the branch name
     * https://docs.gitlab.com/ee/api/branches.html#get-single-repository-branch
     * GET /projects/:id/repository/branches/:branch
     *
     * @param name - the name of the branch
     * @return the {@link GitlabBranch} of the given branch name
     * @throws IOException
     */
    public GitlabBranch getBranch(String name) throws IOException {
        GitlabBranch branch = newBranch(name);
        return getHTTPRequestor().get(String.format("/projects/%d/repository/branches/%s", id, name), branch);
    }

    /**
     * Create a new branch from this project
     * No HTTP request will be issued until you call {@link GitlabBranch#()}
     *
     * @param name - the name of the new branch
     * @return
     */
    public GitlabBranch newBranch(String name) {
        return new GitlabBranch(name).withHTTPRequestor(getHTTPRequestor()).withProject(this);
    }

    public GitlabMergeRequest newMergeRequest(String sourceBranch, String targetBranch, String title) {
        return new GitlabMergeRequest(sourceBranch, targetBranch, title)
                .withHTTPRequestor(getHTTPRequestor()).withProject(this);

    }

    public GitlabMergeRequest getMergeRequest(int mergeRequestIId) throws IOException {
        GitlabMergeRequest mergeRequest = newMergeRequest(null, null, null);
        return getHTTPRequestor()
                .get(String.format("/projects/%d/merge_requests/%d", id, mergeRequestIId), mergeRequest);
    }

    /**
     * Fork the project into current user's repo
     * https://docs.gitlab.com/ee/api/projects.html#fork-project
     * POST /projects/:id/fork
     *
     * @return the new GitlabProject which is the result of forking this project
     */
    public GitlabProject fork() throws IOException {
        return this; // TODO
    }

    /**
     * Create a new {@link GitlabProject} with the given fields
     * It will send HTTP requests to the endpoint
     *
     * @return the new {@link GitlabProject} after creating
     */
    public GitlabProject create() throws IOException {
        Map<String, String> map = new HashMap<String, String>() {{
            put("name", name);
        }};
        return getHTTPRequestor().post("/projects", map, this);
    }

    /**
     * Delete this {@link GitlabProject}
     * It will send HTTP requests to the endpoint
     *
     * @return the previous {@link GitlabProject} before deleting
     */
    public GitlabProject delete() throws IOException {
        getHTTPRequestor().delete("/projects/" + id);
        return this;
    }

    /**
     * Update this {@link GitlabProject}
     * It will send HTTP requests to the endpoint
     *
     * @return the updated {@link GitlabProject}
     */
    public GitlabProject update() throws IOException {
        return getHTTPRequestor().put("/projects/" + id, this);
    }


    /*
     * Getters
     */
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


    /*
     * Setters
     * There will be no setter for id, nameWithNamespace, ssh_url_to_repo, http_url_to_repo, web_url, readme_url,
     * owner, with_issues_enabled, open_issues_count, with_merge_requests_enabled, created_at, last_activity_at,
     * last_activity_at, archived, forks_count, star_count, public_jobs
     *
     */


    public GitlabProject withDescription(String description) {
        this.description = description;
        return this;
    }

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

}
