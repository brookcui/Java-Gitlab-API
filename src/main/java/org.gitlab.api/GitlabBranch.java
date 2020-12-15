package org.gitlab.api;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreType;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.IOException;
import java.util.Objects;


/**
 * This class is used to represent the gitlab branch.
 * <p>
 * This class also contains a {@link ProjectQuery} Class used to build query and get branches within a project.
 * <p>
 * This class supports create and delete methods.
 * <p>
 * Gitlab Web API: https://docs.gitlab.com/ee/api/branches.html
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class GitlabBranch extends GitlabComponent {
    @JsonProperty("name")
    private final String name;
    @JsonIgnore
    private final String ref;
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

    /**
     * Constructor of gitlab branch
     *
     * @param name name of the new branch
     * @param ref  reference sha of the commit to create branch from
     */
    GitlabBranch(@JsonProperty("name") String name, @JsonProperty("ref") String ref) {
        this.name = name;
        this.ref = ref;
    }

    /**
     * Issue a HTTP request to the Gitlab API endpoint to create this {@link GitlabBranch} based on
     * the fields in this {@link GitlabBranch} currently
     *
     * @return the created {@link GitlabBranch} component
     * @throws GitlabException if {@link IOException} occurs or the response code is not in [200,400)
     */
    public GitlabBranch create() {
        Body body = new Body()
                .putString("branch", name)
                .putString("ref", ref);
        return httpClient
                .post(String.format("/projects/%d/repository/branches", project.getId()), body, this);
    }

    /**
     * Issue a HTTP request to the Gitlab API endpoint to delete this {@link GitlabBranch}
     *
     * @return the {@link GitlabBranch} component before deleted
     * @throws GitlabException if {@link IOException} occurs or the response code is not in [200,400)
     */
    public GitlabBranch delete() {
        httpClient.delete(String.format("/projects/%d/repository/branches/%s", project.getId(), name));
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
     * @return whether or new commit can be pushed to current branch
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

    /**
     * Get the top level commit in current branch
     *
     * @return a {@link GitlabCommit} that represent the top level commit
     */
    public GitlabCommit getCommit() {
        return commit;
    }

    /**
     * Get the ref branch for this {@link GitlabBranch} when it is created
     *
     * @return a ref branch this {@link GitlabBranch} referenced when it is created
     */
    public String getRef() {
        return ref;
    }

    /**
     * The string representation of this {@link GitlabBranch}
     *
     * @return the string representation of this {@link GitlabBranch}
     */
    @Override
    public String toString() {
        return "GitlabBranch{" +
                "name=" + name +
                '}';
    }

    /**
     * Two {@link GitlabBranch}es are equal if and only if they belong to the same project and have the same branch name
     *
     * @param o the reference object with which to compare.
     * @return if the two branches belong to the same project and have the same branch name
     */
    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof GitlabBranch)) {
            return false;
        }
        GitlabBranch branch = (GitlabBranch) o;
        return Objects.equals(project, branch.project) && Objects.equals(name, branch.name);
    }

    /**
     * Two {@link GitlabBranch}es  will have the same hashcode if they belong to the same project and have the same branch name
     *
     * @return a hash code value for this object.
     */
    @Override
    public int hashCode() {
        return Objects.hash(project, name);
    }

    /**
     * Attach a project to this {@link GitlabBranch}
     * It will link the commit to the project as well
     *
     * @param project the project to be attached
     * @return this {@link GitlabBranch}
     */
    GitlabBranch withProject(GitlabProject project) {
        this.project = project;
        if (this.commit != null) {
            commit.withProject(project);
        }
        return this;
    }

    /**
     * Set a httpClient to the current {@link GitlabAPIClient}
     *
     * @param httpClient the http client used to make http request
     * @return {@link GitlabBranch} with the httpClient
     */
    @Override
    GitlabBranch withHttpClient(HttpClient httpClient) {
        super.withHttpClient(httpClient);
        return this;
    }


    /**
     * Class to query {@link GitlabBranch} in a given {@link GitlabProject}
     * <p>
     * Gitlab Web API: https://docs.gitlab.com/ee/api/branches.html#list-repository-branches
     * <p>
     * GET /projects/:id/repository/branches
     */
    @JsonIgnoreType
    public static class ProjectQuery extends GitlabQuery<GitlabBranch> {
        /**
         * The project to query {@link GitlabBranch} from
         */
        private final GitlabProject project;

        /**
         * Initialize the {@link ProjectQuery} with httpClienturation and the {@link GitlabProject}
         *
         * @param httpClient - the http client used to make request
         * @param project    - the project to be queried from
         */
        ProjectQuery(HttpClient httpClient, GitlabProject project) {
            super(httpClient, GitlabBranch[].class);
            this.project = project;
        }

        /**
         * Set search parameter to the current query
         *
         * @param search - string to be searched
         * @return this {@link ProjectQuery} with the given search parameter
         */
        public ProjectQuery withSearch(String search) {
            appendString("search", search);
            return this;
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
         * Get the URL suffix for the HTTP request
         * <p>
         * Gitlab Web API: https://docs.gitlab.com/ee/api/branches.html#list-repository-branches
         * <p>
         * GET /projects/:id/repository/branches
         *
         * @return The URL suffix to query {@link GitlabBranch} in the given {@link GitlabProject}
         */
        @Override
        String getTailUrl() {
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
