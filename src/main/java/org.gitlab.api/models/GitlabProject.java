package models;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *
 * @throws IllegalArgumentException
 * @throws NullPointerException
 * @throws UnsupportedOperationException
 * @throws IOException
 */
public class GitlabProject {
    private int id; // required
    private String description;
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
    private boolean snippetsEnabled;
    private LocalDateTime createdAt;
    private LocalDateTime lastActivityAt;
    private int creatorId;
    private GitlabPermission permissions;
    private boolean archived;
    private int forksCount;
    private int starCount;
    private boolean publicJobs;

    /*
     * Getters
     */

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
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

    public boolean isSnippetsEnabled() {
        return snippetsEnabled;
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

    public GitlabPermission getPermissions() {
        return permissions;
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
     */

    // FIXME: do we need to return self?
    // FIXME: setX or withX?
    public GitlabProject withDescription(String description) {
        this.description = description;
        return this;
    }

    // TODO: all other setters

    public static class Builder {
        private int id; // required but private
        private String description;
        private String defaultBranch;
        private String visibility;
        private String sshUrlToRepo;
        private String httpUrlToRepo;
        private String webUrl;
        private String readmeUrl;
        private List<String> tagList;
        private GitlabUser owner;
        private String name; // required and public
        private String path;
        private boolean issuesEnabled;
        private int openIssuesCount;
        private boolean mergeRequestsEnabled;
        private boolean jobsEnabled;
        private boolean wikiEnabled;
        private boolean snippetsEnabled;
        private LocalDateTime createdAt;
        private LocalDateTime lastActivityAt;
        private int creatorId;
        private GitlabPermission permissions;
        private boolean archived;
        private int forksCount;
        private int starCount;
        private boolean publicJobs;

        public Builder(String name) {
            this.id = 0; // TODO
            this.name = name;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder defaultBranch(String defaultBranch) {
            this.defaultBranch = defaultBranch;
            return this;
        }

        public Builder visibility(String visibility) {
            this.visibility = visibility;
            return this;
        }

        public Builder sshUrlToRepo(String sshUrlToRepo) {
            this.sshUrlToRepo = sshUrlToRepo;
            return this;
        }

        public Builder httpUrlToRepo(String httpUrlToRepo) {
            this.httpUrlToRepo = httpUrlToRepo;
            return this;
        }

        public Builder webUrl(String webUrl) {
            this.webUrl = webUrl;
            return this;
        }

        public Builder readmeUrl(String readmeUrl) {
            this.readmeUrl = readmeUrl;
            return this;
        }

        public Builder tagList(List<String> tagList) {
            this.tagList = new ArrayList<>(tagList);
            return this;
        }

        public Builder owner(GitlabUser owner) {
            this.owner = owner;
            return this;
        }

        public Builder path(String path) {
            this.path = path;
            return this;
        }

        public Builder setIssuesEnabled() {
            this.issuesEnabled = true;
            return this;
        }

        public Builder openIssuesCount(int openIssuesCount) {
            this.openIssuesCount = openIssuesCount;
            return this;
        }

        public Builder setMergeRequestsEnabled() {
            this.mergeRequestsEnabled = true;
            return this;
        }

        public Builder setJobsEnabled() {
            this.jobsEnabled = true;
            return this;
        }

        public Builder setWikiEnabled() {
            this.wikiEnabled = true;
            return this;
        }

        public Builder setSnippetsEnabled() {
            this.snippetsEnabled = true;
            return this;
        }

        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder lastActivityAt(LocalDateTime lastActivityAt) {
            this.lastActivityAt = lastActivityAt;
            return this;
        }

        public Builder creatorId(int creatorId) {
            this.creatorId = creatorId;
            return this;
        }

        public Builder permissions(GitlabPermission permissions) {
            this.permissions = permissions;
            return this;
        }

        public Builder setArchived() {
            this.archived = true;
            return this;
        }

        public Builder forksCount(int forksCount) {
            this.forksCount = forksCount;
            return this;
        }

        public Builder starCount(int starCount) {
            this.starCount = starCount;
            return this;
        }

        public Builder setPublicJobs() {
            this.publicJobs = true;
            return this;
        }

        public GitlabProject build() {
            return new GitlabProject(this);
        }
    }

    private GitlabProject(Builder builder) {
        this.id = builder.id; // required
        this.description = builder.description;
        this.defaultBranch = builder.defaultBranch;
        this.visibility = builder.visibility;
        this.sshUrlToRepo = builder.sshUrlToRepo;
        this.httpUrlToRepo = builder.httpUrlToRepo;
        this.webUrl = builder.webUrl;
        this.readmeUrl = builder.readmeUrl;
        this.tagList = builder.tagList;
        this.owner = builder.owner;
        this.name = builder.name; // required
        this.path = builder.path;
        this.issuesEnabled = builder.issuesEnabled;
        this.openIssuesCount = builder.openIssuesCount;
        this.mergeRequestsEnabled = builder.mergeRequestsEnabled;
        this.jobsEnabled = builder.jobsEnabled;
        this.wikiEnabled = builder.wikiEnabled;
        this.snippetsEnabled = builder.snippetsEnabled;
        this.createdAt = builder.createdAt;
        this.lastActivityAt = builder.lastActivityAt;
        this.creatorId = builder.creatorId;
        this.permissions = builder.permissions;
        this.archived = builder.archived;
        this.forksCount = builder.forksCount;
        this.starCount = builder.starCount;
        this.publicJobs = builder.publicJobs;
    }

    /*
     * Users
     */
    public List<GitlabUser> getProjectUsers() throws  IOException {
        return null; // TODO
    }

    /*
     * Issues
     */

    public List<GitlabIssue> getProjectIssues() throws IOException {
        return null; // TODO
    }

    public GitlabIssue getSingleProjectIssue(int issueId) throws IOException {
        return null; // TODO
    }

    public void createIssue(GitlabIssue issue) throws IOException {
        return; // TODO
    }

    public void updateIssue(GitlabIssue issue) throws IOException {
        return; // TODO
    }

    public void deleteIssue(int issueId) throws IOException {
        return; // TODO
    }

    /*
     * Commits
     */

    public List<GitlabCommit> getCommits() throws IOException {
        return null; // TODO
    }

    public GitlabCommit GetSingleCommit(int commitId, int sha) throws IOException {
        return null; // TODO
    }

    /*
     * Branches
     */

    public List<GitlabBranch> getAllBranches() throws IOException {
        return null; // TODO
    }

    public GitlabBranch getSingleBranch(String branchName) throws IOException {
        return null; // TODO
    }

    public void createBranch(GitlabBranch branch) throws IOException {
        return; // TODO
    }

    /*
     * note that you cannot edit a branch within a repo
     */

    public void deleteBranch(String branchName) throws IOException {
        return; // TODO
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof GitlabProject)) {
            return false;
        }
        GitlabProject project = (GitlabProject) o;
        return project.id == this.id;
    }
}
