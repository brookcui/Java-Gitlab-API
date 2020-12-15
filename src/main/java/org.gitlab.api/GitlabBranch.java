package org.gitlab.api;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreType;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.IOException;
import java.util.Objects;


/**
 * This class serves as instance of Gitlab component Branch.
 *
 * To create or delete this branch, call {@code create()} or {@code delete()}
 * explicitly. This doesn't support update.
 *
 * This supports query for branches within {@link GitlabProject}. See
 * {@link ProjectQuery}.
 *
 * Gitlab Web API: https://docs.gitlab.com/ee/api/branches.html
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class GitlabBranch extends GitlabComponent {
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
     * Constructs the {@link GitlabBranch} instance with name and ref.
     *
     * @param name branch name
     * @param ref  reference sha of the commit to create branch from
     */
    GitlabBranch(@JsonProperty("name") String name, @JsonProperty("ref") String ref) {
        this.name = name;
        this.ref = ref;
    }

    /**
     * Returns a string representation of this {@link GitlabBranch} in the
     * format of Gitlab component type and name.
     *
     * @return a string representation of this {@link GitlabBranch}
     */
    @Override
    public String toString() {
        return "GitlabBranch{" +
                       "name=" + name +
                       '}';
    }

    /**
     * Returns the hash code value for this {@link GitlabBranch} identified by
     * its belonged project and branch name.
     *
     * @return a hash code value for this object
     */
    @Override
    public int hashCode() {
        return Objects.hash(project, name);
    }

    /**
     * Compares the specified {@code Object} with this {@link GitlabBranch}
     * for equality. Note that two {@link GitlabBranch}es are equal if and only
     * if they belong to the same project and have the same branch name.
     *
     * @param o object to be compared for equality with this {@link GitlabBranch}
     * @return true if the specified Object is equal to this {@link GitlabBranch}
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
     * Issues a HTTP request to Gitlab API endpoint to create a branch for its
     * belonged project based on this {@link GitlabBranch}.
     *
     * @return the created {@link GitlabBranch} instance
     * @throws GitlabException if {@link IOException} occurs or API endpoint fails
     * to give a valid response (response code within [200,400))
     */
    public GitlabBranch create() {
        Body body = new Body()
                .putString("branch", name)
                .putString("ref", ref);
        return httpClient
                .post(String.format("/projects/%d/repository/branches", project.getId()), body, this);
    }

    /**
     * Issues a HTTP request to Gitlab API endpoint to delete this
     * {@link GitlabBranch} from its belonged project based on branch name.
     *
     * @return the {@link GitlabBranch} instance before deleted
     * @throws GitlabException if {@link IOException} occurs or API endpoint fails
     * to give a valid response (response code within [200,400))
     */
    public GitlabBranch delete() {
        httpClient.delete(String.format("/projects/%d/repository/branches/%s", project.getId(), name));
        return this;
    }

    /**
     * Returns the project that this {@link GitlabBranch} belongs to.
     *
     * @return the {@link GitlabProject} instance
     */
    public GitlabProject getProject() {
        return project;
    }

    /**
     * Returns this branch's name.
     *
     * @return name of the branch
     */
    public String getName() {
        return name;
    }

    /**
     * Tests if this branch has been merged.
     *
     * @return true if this branch has been merged; otherwise, false
     */
    public boolean isMerged() {
        return merged;
    }

    /**
     * Tests if this branch is being protected.
     *
     * @return true if this branch is being protected; otherwise, false
     */
    public boolean isProtected() {
        return isProtected;
    }

    /**
     * Tests if this branch is a default branch.
     *
     * @return true if this branch is default; otherwise, false
     */
    public boolean isDefault() {
        return isDefault;
    }

    /**
     * Tests if new commit can be pushed to this branch.
     *
     * @return true if new commit can be pushed to this branch; otherwise, false
     */
    public boolean canPush() {
        return canPush;
    }

    /**
     * Returns the web url of the this branch
     *
     * @return web url string of the this branch
     */
    public String getWebUrl() {
        return webUrl;
    }

    /**
     * Returns the top level commit of this branch.
     *
     * @return a {@link GitlabCommit} that represent the top level commit
     */
    public GitlabCommit getCommit() {
        return commit;
    }

    /**
     * Returns the ref branch when this {@link GitlabBranch} was created.
     *
     * @return a ref this {@link GitlabBranch} references
     */
    public String getRef() {
        return ref;
    }

    /**
     * Attaches given project to this {@link GitlabBranch} and also links this
     * branch's commit to to the project as well
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
     * Sets a httpClient to the this {@link GitlabBranch}.
     *
     * @param httpClient HTTP client helper to make http requests
     * @return {@link GitlabBranch} with the httpClient
     */
    @Override
    GitlabBranch withHttpClient(HttpClient httpClient) {
        super.withHttpClient(httpClient);
        return this;
    }

    /**
     * This extends {@link GitlabQuery} and supports query for
     * {@link GitlabBranch}es within a {@link GitlabProject} with searching
     * scope and range.
     *
     * Build this query with setters and call {@code query()} to execute query.
     *
     * <p>
     * Gitlab Web API: https://docs.gitlab.com/ee/api/branches.html#list-repository-branches
     * <p>
     * GET /projects/:id/repository/branches
     */
    @JsonIgnoreType
    public static class ProjectQuery extends GitlabQuery<GitlabBranch> {
        /**
         * The project to query {@link GitlabBranch} from.
         */
        private final GitlabProject project;

        /**
         * Constructs the {@link ProjectQuery} instance.
         *
         * @param httpClient - the http client used to make request
         * @param project    - the project to be queried from
         */
        ProjectQuery(HttpClient httpClient, GitlabProject project) {
            super(httpClient, GitlabBranch[].class);
            this.project = project;
        }

        /**
         * Returns a query that sets search parameter.
         *
         * @param search - string to be searched
         * @return this {@link ProjectQuery} with search parameter
         */
        public ProjectQuery withSearch(String search) {
            appendString("search", search);
            return this;
        }

        /**
         * Returns a query that specifies page number and size to return based
         * on given pagination.
         *
         * @param pagination pagination object that defines page number and size
         * @return this {@link ProjectQuery} with given pagination
         */
        @Override
        public ProjectQuery withPagination(Pagination pagination) {
            appendPagination(pagination);
            return this;
        }

        /**
         * Returns the URL suffix for this HTTP request.
         *
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
         * Binds the branch with the given {@link GitlabProject} after the response is parsed.
         *
         * @param component - one {@link GitlabBranch} from the response
         */
        @Override
        void bind(GitlabBranch component) {
            component.withProject(project);
        }
    }
}
