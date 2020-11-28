package models;

import core.Pagination;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * @throws IllegalArgumentException
 * @throws NullPointerException
 * @throws UnsupportedOperationException
 * @throws IOException
 */
public class GitlabProject {
    private int id; // required
    private String description;
    private String nameWithNamespace;
    private String defaultBranch;
    private String visibility;
    private String sshUrlToRepo;
    private String httpUrlToRepo;
    private String webUrl;
    private String readmeUrl;
    private List<String> tagList;
    private GitlabUser owner;
    private String name; // required
    private String path;
    private boolean issuesEnabled;
    private int openIssuesCount;
    private boolean mergeRequestsEnabled;
    private boolean jobsEnabled;
    private boolean wikiEnabled;
    private LocalDateTime createdAt;
    private LocalDateTime lastActivityAt;
    private int creatorId;
    private boolean archived;
    private int forksCount;
    private int starCount;
    private boolean publicJobs;

    /**
     * Construct the project with name
     * TODO: package private or protected
     *
     * @param name
     */
    public GitlabProject(String name) {
        this.name = name;
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
        return null; // TODO
    }

    /**
     * Construct a new issue from this project of the given name
     * No HTTP request will be issued until you call {@link GitlabIssue#create()}
     *
     * @param name - the name of the issue
     * @return the new issue from this project
     */
    public GitlabIssue newIssue(String name) {
        return null;
    }


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
        return null; // TODO
    }

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
     * @param branchName - the name of the branch
     * @return the {@link GitlabBranch} of the given branch name
     * @throws IOException
     */
    public GitlabBranch getBranch(String branchName) throws IOException {
        return null; // TODO
    }

    /**
     * Create a new branch from this project
     * No HTTP request will be issued until you call {@link GitlabBranch#create()}
     *
     * @param name - the name of the new branch
     * @param ref  - Branch name or commit SHA to create branch from.
     * @return
     */
    public GitlabBranch newBranch(String name, String ref) {
        return null;
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
        return this; // TODO
    }

    /**
     * Delete this {@link GitlabProject}
     * It will send HTTP requests to the endpoint
     *
     * @return the previous {@link GitlabProject} before deleting
     */
    public GitlabProject delete() throws IOException {
        return this; // TODO
    }

    /**
     * Update this {@link GitlabProject}
     * It will send HTTP requests to the endpoint
     *
     * @return the updated {@link GitlabProject}
     */
    public GitlabProject update() throws IOException {
        return this; // TODO
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

    public GitlabProject withNameWithNamespace(String nameWithNamespace) {
        this.nameWithNamespace = nameWithNamespace;
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

    /**
     * TODO: change the string representation form
     *
     * @return
     */
    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GitlabProject that = (GitlabProject) o;
        return id == that.id &&
                issuesEnabled == that.issuesEnabled &&
                openIssuesCount == that.openIssuesCount &&
                mergeRequestsEnabled == that.mergeRequestsEnabled &&
                jobsEnabled == that.jobsEnabled &&
                wikiEnabled == that.wikiEnabled &&
                creatorId == that.creatorId &&
                archived == that.archived &&
                forksCount == that.forksCount &&
                starCount == that.starCount &&
                publicJobs == that.publicJobs &&
                Objects.equals(description, that.description) &&
                Objects.equals(nameWithNamespace, that.nameWithNamespace) &&
                Objects.equals(defaultBranch, that.defaultBranch) &&
                Objects.equals(visibility, that.visibility) &&
                Objects.equals(sshUrlToRepo, that.sshUrlToRepo) &&
                Objects.equals(httpUrlToRepo, that.httpUrlToRepo) &&
                Objects.equals(webUrl, that.webUrl) &&
                Objects.equals(readmeUrl, that.readmeUrl) &&
                Objects.equals(tagList, that.tagList) &&
                Objects.equals(owner, that.owner) &&
                Objects.equals(name, that.name) &&
                Objects.equals(path, that.path) &&
                Objects.equals(createdAt, that.createdAt) &&
                Objects.equals(lastActivityAt, that.lastActivityAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
