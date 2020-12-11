package org.gitlab.api.core;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.gitlab.api.http.Body;
import org.gitlab.api.http.Config;
import org.gitlab.api.http.GitlabHttpClient;

import java.util.Objects;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class GitlabBranch implements GitlabComponent {

    @JsonProperty("name")
    private final String name;
    @JsonIgnore
    private Config config;
    @JsonProperty("merged")
    private boolean merged;
    @JsonProperty("protected")
    private boolean isProtected; // for "protected"
    @JsonProperty("default")
    private boolean isDefault; // for "default"
    @JsonProperty("can_push")
    private boolean canPush;
    @JsonProperty("web_url")
    private String webUrl;
    @JsonProperty("commit")
    private GitlabCommit commit; // corresponds to branch name or commit SHA to create branch from
    @JsonIgnore
    private GitlabProject project;

    GitlabBranch(@JsonProperty("name") String name) {
        this.name = name;
    }

    @Override
    public Config getConfig() {
        return config;
    }

    @Override
    public GitlabBranch withConfig(Config config) {
        this.config = config;
        return this;
    }

    public String getName() {
        return name;
    }

    public boolean isMerged() {
        return merged;
    }

    public boolean isProtected() {
        return isProtected;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public boolean canPush() {
        return canPush;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public GitlabCommit getCommit() {
        return commit;
    }

    public GitlabBranch create(String ref) {
        Body body = new Body()
                .putString("branch", name)
                .putString("ref", ref);
        return GitlabHttpClient
                .post(config, String.format("/projects/%d/repository/branches", project.getId()), body, this);
    }

    public GitlabBranch delete() {
        GitlabHttpClient.delete(config, String.format("/projects/%d/repository/branches/%s", project.getId(), name));
        return this;
    }

    @Override
    public String toString() {
        return "GitlabBranch{" +
                "name=" + name +
                ", merged=" + merged +
                ", isProtected=" + isProtected +
                ", isDefault=" + isDefault +
                ", canPush=" + canPush +
                ", webUrl=" + webUrl +
                ", commit=" + commit +
                ", project=" + project +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GitlabBranch)) {
            return false;
        }
        GitlabBranch that = (GitlabBranch) o;
        return merged == that.merged &&
                isProtected == that.isProtected &&
                isDefault == that.isDefault &&
                canPush == that.canPush &&
                name.equals(that.name) &&
                webUrl.equals(that.webUrl) &&
                commit.equals(that.commit);
    }

    /**
     * Get the {@link GitlabProject} that current the {@link GitlabBranch} belongs to
     *
     * @return the {@link GitlabProject} that current the {@link GitlabBranch} belongs to
     */
    public GitlabProject getProject() {
        return project;
    }

    GitlabBranch withProject(GitlabProject project) {
        this.project = project;
        if (this.commit != null) {
            commit.withProject(project);
        }
        return this;
    }

    /**
     * Query class for the @link GitlabBranch} to support query operations
     * Gitlab Web API: https://docs.gitlab.com/ee/api/branches.html
     */
    public static class Query extends GitlabQuery<GitlabBranch> {

        private final int projectId;

        Query(Config config, int projectId) {
            super(config, GitlabBranch[].class);
            this.projectId = projectId;
        }

        /**
         * Add search parameter to the current query
         *
         * @param search - string to be searched
         * @return Current {@link GitlabBranch} with search parameter
         */
        public Query withSearch(String search) {
            appendString("search", search);
            return this;
        }

        /**
         * add pagination on top of the query
         *
         * @param pagination pagination object that defines page number and size
         * @return Current {@link GitlabBranch} with the given peganation object
         */
        @Override
        public Query withPagination(Pagination pagination) {
            appendPagination(pagination);
            return this;
        }

        /**
         * Get the URL prefix for the HTTP request
         * @return The URL for current {@link GitlabBranch}
         */
        @Override
        public String getUrlPrefix() {
            return String.format("/projects/%d/repository/branches", projectId);
        }
    }

}
