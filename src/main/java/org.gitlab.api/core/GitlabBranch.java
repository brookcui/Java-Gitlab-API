package org.gitlab.api.core;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.gitlab.api.http.Body;
import org.gitlab.api.http.Config;
import org.gitlab.api.http.GitlabHttpClient;

import java.util.Objects;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class GitlabBranch implements GitlabWritableComponent<GitlabBranch> {

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
    @JsonIgnore
    private String ref;

    GitlabBranch(@JsonProperty("name") String name, @JsonProperty("ref") String ref) {
        this.name = name;
        this.ref = ref;
    }

    @Override
    public GitlabBranch create() {
        Body body = new Body()
                .putString("branch", name)
                .putString("ref", ref);
        return GitlabHttpClient
                .post(config, String.format("/projects/%d/repository/branches", project.getId()), body, this);
    }

    @Override
    public GitlabBranch delete() {
        GitlabHttpClient.delete(config, String.format("/projects/%d/repository/branches/%s", project.getId(), name));
        return this;
    }

    /**
     * Get the {@link GitlabProject} that current the {@link GitlabBranch} belongs to
     *
     * @return the {@link GitlabProject} that current the {@link GitlabBranch} belongs to
     */
    public GitlabProject getProject() {
        return project;
    }

    // -- getters --

    /**
     * Get name of the branch
     *
     * @return name of the branch
     */
    public String getName() {
        return name;
    }

    /**
     * Get whether or not this branch is merged
     *
     * @return whether current branch is merged
     */
    public boolean isMerged() {
        return merged;
    }

    /**
     * Get whether or not current branch is a protected branch
     *
     * @return whether current branch is a protected or not
     */
    public boolean isProtected() {
        return isProtected;
    }

    /**
     * Get whether or not current branch is a default branch
     *
     * @return whether current branch is a default or not
     */
    public boolean isDefault() {
        return isDefault;
    }

    /**
     * Get whether or new commit can be pushed to current branch
     *
     * @return whether whether or new commit can be pushed to current branch
     */
    public boolean canPush() {
        return canPush;
    }

    /**
     * Get the web url of the current branch
     *
     * @return web url of the current branch
     */
    public String getWebUrl() {
        return webUrl;
    }

    public GitlabCommit getCommit() {
        return commit;
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


    GitlabBranch withProject(GitlabProject project) {
        this.project = project;
        if (this.commit != null) {
            commit.withProject(project);
        }
        return this;
    }

    /**
     * Get the config that is stored in current {@link GitlabBranch}
     *
     * @return the config with user detail
     */
    @Override
    public Config getConfig() {
        return config;
    }

    /**
     * Add a config to the current {@link GitlabAPIClient}
     * @param config a config with user details
     * @return {@link GitlabBranch} with the config
     */
    @Override
    public GitlabBranch withConfig(Config config) {
        this.config = config;
        return this;
    }

    /**
     * Class to query {@link GitlabBranch} in a given {@link GitlabProject}
     * Gitlab Web API: https://docs.gitlab.com/ee/api/branches.html#list-repository-branches
     */
    public static class ProjectQuery extends GitlabQuery<GitlabBranch> {
        /**
         * The project to query {@link GitlabBranch} from
         */
        private final GitlabProject project;

        /**
         * Initialize the {@link ProjectQuery} with configuration and the {@link GitlabProject}
         *
         * @param config  - the configuration to be used
         * @param project - the project to be queried from
         */
        ProjectQuery(Config config, GitlabProject project) {
            super(config, GitlabBranch[].class);
            this.project = project;
        }

        /**
         * Add search parameter to the current query
         *
         * @param search - string to be searched
         * @return this {@link ProjectQuery} with the given search parameter
         */
        public ProjectQuery withSearch(String search) {
            appendString("search", search);
            return this;
        }

        /**
         * Add pagination on top of the query
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
         * Get the URL suffix for the HTTP request
         *
         * @return The URL suffix to query {@link GitlabBranch} in the given {@link GitlabProject}
         */
        @Override
        public String getUrlSuffix() {
            return String.format("/projects/%d/repository/branches", project.getId());
        }

        /**
         * Bind the branch with the given {@link GitlabProject} after the response is parsed
         *
         * @param component - one {@link GitlabBranch} from the response
         */
        @Override
        void bind(GitlabBranch component) {
            component.withProject(project);
        }
    }

}
