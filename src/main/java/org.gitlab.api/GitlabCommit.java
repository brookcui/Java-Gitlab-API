package org.gitlab.api;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreType;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * This class serves as instance of Gitlab component Commit.
 *
 * This doesn't support create, update, and delete operations.
 *
 * This supports query for commits within {@link GitlabProject}. See
 * {@link ProjectQuery}.
 *
 * Gitlab Web API: https://docs.gitlab.com/ee/api/commits.html
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class GitlabCommit extends GitlabComponent {
    @JsonProperty("id")
    private final String id;
    @JsonProperty("parent_ids")
    private final List<String> parentIds = new ArrayList<>();
    @JsonProperty("short_id")
    private String shortId;
    @JsonProperty("title")
    private String title;
    @JsonProperty("author_name")
    private String authorName;
    @JsonProperty("author_email")
    private String authorEmail;
    @JsonProperty("committer_name")
    private String committerName;
    @JsonProperty("committer_email")
    private String committerEmail;
    @JsonProperty("created_at")
    @JsonDeserialize(using = DateUtil.OffsetDeserializer.class)
    @JsonSerialize(using = DateUtil.OffsetSerializer.class)
    private ZonedDateTime createdAt;
    @JsonProperty("message")
    private String message;
    @JsonProperty("committed_date")
    @JsonDeserialize(using = DateUtil.OffsetDeserializer.class)
    @JsonSerialize(using = DateUtil.OffsetSerializer.class)
    private ZonedDateTime committedDate;
    @JsonProperty("authored_date")
    @JsonDeserialize(using = DateUtil.OffsetDeserializer.class)
    @JsonSerialize(using = DateUtil.OffsetSerializer.class)
    private ZonedDateTime authoredDate;
    @JsonProperty("status")
    private String status;
    @JsonProperty("web_url")
    private String webUrl;
    @JsonIgnore
    private GitlabProject project;

    /**
     * Constructs the {@link GitlabCommit} instance with SHA.
     *
     * @param id sha of the commit
     */
    GitlabCommit(@JsonProperty("id") String id) {
        this.id = id;
    }

    /**
     * Returns a string representation of this {@link GitlabCommit} in the
     * format of Gitlab component type and id and also parent Id.
     *
     * @return a string representation of this {@link GitlabCommit}
     */
    @Override
    public String toString() {
        return "GitlabCommit{" +
                "id=" + id +
                ", parentIds=" + parentIds +
                '}';
    }

    /**
     * Returns the hash code value for this {@link GitlabCommit} identified by
     * its belonged project and Id.
     *
     * @return a hash code value for this object
     */
    @Override
    public int hashCode() {
        return Objects.hash(project, id);
    }

    /**
     * Compares the specified {@code Object} with this {@link GitlabCommit}
     * for equality. Note that two {@link GitlabCommit}s are equal if and only
     * if they belong to the same project and have the same Id.
     *
     * @param o object to be compared for equality with this {@link GitlabCommit}
     * @return true if the specified Object is equal to this {@link GitlabCommit}
     */
    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof GitlabCommit)) {
            return false;
        }
        GitlabCommit commit = (GitlabCommit) o;
        return Objects.equals(commit.project, this.project) && Objects.equals(commit.id, this.id);
    }

    /**
     * Returns the full commit hash of this commit.
     *
     * @return the commit hash of this commit
     */
    public String getId() {
        return id;
    }

    /**
     * Returns title of this commit.
     *
     * @return the title string of this commit.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Returns the short version of the commit hash.
     *
     * @return the short version of the commit hash of this commit
     */
    public String getShortId() {
        return shortId;
    }

    /**
     * Returns the name of the commit author.
     *
     * @return author name string
     */
    public String getAuthorName() {
        return authorName;
    }

    /**
     * Returns the email of the commit author.
     *
     * @return author email string
     */
    public String getAuthorEmail() {
        return authorEmail;
    }

    /**
     * Returns the name of the committer.
     *
     * @return name of the committer
     */
    public String getCommitterName() {
        return committerName;
    }

    /**
     * Returns the email of the committer.
     *
     * @return email of the committer
     */
    public String getCommitterEmail() {
        return committerEmail;
    }

    /**
     * Returns the date when the commit is created.
     *
     * @return the date when the commit is created
     */
    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * Returns the commit message of the commit.
     *
     * @return commit message of this commit
     */
    public String getMessage() {
        return message;
    }

    /**
     * Returns the committed date of this commit.
     *
     * @return the committed date
     */
    public ZonedDateTime getCommittedDate() {
        return committedDate;
    }

    /**
     * Returns the authored date of this commit.
     *
     * @return the authored date
     */
    public ZonedDateTime getAuthoredDate() {
        return authoredDate;
    }

    /**
     * Returns the list SHA of the parent commits.
     *
     * @return a list of commit SHA
     */
    public List<String> getParentIds() {
        return parentIds;
    }

    /**
     * Returns the status of the commit.
     *
     * @return the status string of the commit
     */
    public String getStatus() {
        return status;
    }

    /**
     * Returns the web url of the commit.
     *
     * @return the web url of the commit
     */
    public String getWebUrl() {
        return webUrl;
    }

    /**
     * Returns the project that current commit belongs to.
     *
     * @return the {@link GitlabProject} that this commit belongs to
     */
    public GitlabProject getProject() {
        return project;
    }

    /**
     * Sets a httpClient to the this {@link GitlabCommit}.
     *
     * @param httpClient HTTP client helper to make http requests
     * @return {@link GitlabCommit} with the httpClient
     */
    @Override
    GitlabCommit withHttpClient(HttpClient httpClient) {
        super.withHttpClient(httpClient);
        return this;
    }

    /**
     * Attaches a project to this {@link GitlabCommit}.
     *
     * @param project the project to be attached
     * @return this {@link GitlabCommit}
     */
    GitlabCommit withProject(GitlabProject project) {
        this.project = project;
        return this;
    }

    /**
     * This extends {@link GitlabQuery} and supports query for
     * {@link GitlabCommit}s within a {@link GitlabProject} with searching
     * scope and range.
     *
     * Build this query with setters and call {@code query()} to execute query.
     *
     * <p>
     * Gitlab Web API: https://docs.gitlab.com/ee/api/commits.html#list-repository-commits
     * <p>
     * GET /projects/:id/repository/commits
     */
    @JsonIgnoreType
    public static class ProjectQuery extends GitlabQuery<GitlabCommit> {
        private final GitlabProject project;

        ProjectQuery(HttpClient httpClient, GitlabProject project) {
            super(httpClient, GitlabCommit[].class);
            this.project = project;
        }

        /**
         * Returns a query that will match with given ref name.
         *
         * @param refName name of a repository branch, tag or revision range, or if not given the default branch
         * @return this {@link ProjectQuery} with ref name
         */
        public ProjectQuery withRefName(String refName) {
            appendString("ref_name", refName);
            return this;
        }

        /**
         * Returns a query that will only return commits after or on given date.
         *
         * @param since date in in ISO 8601 format YYYY-MM-DDTHH:MM:SSZ
         * @return this {@link ProjectQuery} with given since date
         */
        public ProjectQuery withSince(ZonedDateTime since) {
            appendDateTime("since", since);
            return this;
        }

        /**
         * Returns a query that will only return commits before or on given date.
         *
         * @param until date in in ISO 8601 format YYYY-MM-DDTHH:MM:SSZ
         * @return this {@link ProjectQuery} with given until date
         */
        public ProjectQuery withUntil(ZonedDateTime until) {
            appendDateTime("until", until);
            return this;
        }

        /**
         * Returns a query that will match with given file path.
         *
         * @param path the file path
         * @return this {@link ProjectQuery} with given path
         */
        public ProjectQuery withPath(String path) {
            appendString("path", path);
            return this;
        }

        /**
         * Returns a query that will return stats of each commit in responses.
         *
         * @param withStats stat to add to the query
         * @return this {@link ProjectQuery} with given stats
         */
        public ProjectQuery withStats(boolean withStats) {
            appendBoolean("with_stats", withStats);
            return this;
        }

        /**
         * Returns a query that will only return the first parent commit upon
         * seeing a merge commit.
         *
         * @param firstParent whether or not to follow only the first parent
         * @return this {@link ProjectQuery} with given first parent
         */
        public ProjectQuery withFirstParent(boolean firstParent) {
            appendBoolean("first_parent", firstParent);
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
         * Returns a query that sets commits order in given order.
         * <p>
         * Possible values: default, topo. Defaults to default, the commits are shown in reverse chronological order.
         *
         * @param order order of all the commits
         * @return this {@link ProjectQuery} the order
         */
        public ProjectQuery withOrder(String order) {
            appendString("order", order);
            return this;
        }

        /**
         * Returns the URL suffix for this HTTP request.
         *
         * <p>
         * Gitlab Web API: https://docs.gitlab.com/ee/api/commits.html#list-repository-commits
         * <p>
         * GET /projects/:id/repository/commits
         *
         * @return The URL suffix to query {@link GitlabCommit} in the given {@link GitlabProject}
         */
        @Override
        String getTailUrl() {
            return String.format("/projects/%d/repository/commits", project.getId());
        }

        /**
         * Binds the commit with the given {@link GitlabProject} after the response is parsed
         *
         * @param component - one {@link GitlabCommit} from the response
         */
        @Override
        void bind(GitlabCommit component) {
            component.withProject(project);
        }
    }
}
