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
 * This class is used to represent the gitlab commit.
 * <p>
 * This class also contains a {@link ProjectQuery} Class used to build query and get commits within a project.
 * <p>
 * This class is immutable and cannot be modified.
 * <p>
 * Gitlab Web API: https://docs.gitlab.com/ee/api/commits.html
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class GitlabCommit extends GitlabComponent {
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
     * Constructor of the gitlab commit
     *
     * @param id sha of the commit
     */
    GitlabCommit(@JsonProperty("id") String id) {
        this.id = id;
    }

    /**
     * The string representation of this {@link GitlabCommit}
     *
     * @return the string representation of this {@link GitlabCommit}
     */
    @Override
    public String toString() {
        return "GitlabCommit{" +
                "id=" + id +
                ", parentIds=" + parentIds +
                '}';
    }

    /**
     * Two {@link GitlabCommit}s will have the same hashcode if they belong to the same project and have the same id
     *
     * @return a hash code value for this object.
     */
    @Override
    public int hashCode() {
        return Objects.hash(project, id);
    }

    /**
     * Two {@link GitlabProject}s are equal if and only if they belong to the same project and have the same id
     *
     * @param o the reference object with which to compare.
     * @return if the two commits belong to the same project and have the same commits
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
     * Get the full commit hash of the current commit
     *
     * @return the the full commit hash of the current commit
     */
    public String getId() {
        return id;
    }

    /**
     * Get the current title of the commit
     *
     * @return the title of the commit
     */
    public String getTitle() {
        return title;
    }

    /**
     * The the short version of the commit hash
     *
     * @return short version of the commit hash
     */
    public String getShortId() {
        return shortId;
    }

    /**
     * Get the name of the author
     *
     * @return author name
     */
    public String getAuthorName() {
        return authorName;
    }

    /**
     * Get the email of the author
     *
     * @return author email
     */
    public String getAuthorEmail() {
        return authorEmail;
    }

    /**
     * Get the name of the committer
     *
     * @return name of the committer
     */
    public String getCommitterName() {
        return committerName;
    }

    /**
     * Get the email of the committer
     *
     * @return email of the committer
     */
    public String getCommitterEmail() {
        return committerEmail;
    }

    /**
     * Get the date on when the commit is created
     *
     * @return the date on when the commit is created
     */
    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * Get the commit message of the commit
     *
     * @return message of the commit
     */
    public String getMessage() {
        return message;
    }

    /**
     * Get the date that the commit is committed
     *
     * @return Get the date that the commit is committed
     */
    public ZonedDateTime getCommittedDate() {
        return committedDate;
    }

    /**
     * Get the date that a commit is authored
     *
     * @return the date that a commit is authored
     */
    public ZonedDateTime getAuthoredDate() {
        return authoredDate;
    }

    /**
     * Get the list sha of the parent commits
     *
     * @return list of commit sha
     */
    public List<String> getParentIds() {
        return parentIds;
    }

    /**
     * Get the status of the commit
     *
     * @return status of the commit
     */
    public String getStatus() {
        return status;
    }

    /**
     * Get the web url of the commit
     *
     * @return web url of the commit
     */
    public String getWebUrl() {
        return webUrl;
    }

    /**
     * Get the project that current commit belongs to
     *
     * @return The {@link GitlabProject} that current commit belongs to
     */
    public GitlabProject getProject() {
        return project;
    }

    /**
     * @param httpClient httpClient used to make http requests
     * @return {@link GitlabCommit} with the httpClient
     */
    @Override
    GitlabCommit withHttpClient(HttpClient httpClient) {
        super.withHttpClient(httpClient);
        return this;
    }

    /**
     * Attach a project to this {@link GitlabCommit}
     *
     * @param project the project to be attached
     * @return this {@link GitlabCommit}
     */
    GitlabCommit withProject(GitlabProject project) {
        this.project = project;
        return this;
    }

    /**
     * Class to query {@link GitlabCommit} in a given {@link GitlabProject}
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
         * Add the ref name to the query
         *
         * @param refName name of a repository branch, tag or revision range, or if not given the default branch
         * @return {@link ProjectQuery} with the ref name
         */
        public ProjectQuery withRefName(String refName) {
            appendString("ref_name", refName);
            return this;
        }

        /**
         * add a date to the query and only commits after or on this date will be returned
         *
         * @param since date in in ISO 8601 format YYYY-MM-DDTHH:MM:SSZ
         * @return {@link ProjectQuery} with the since
         */
        public ProjectQuery withSince(ZonedDateTime since) {
            appendDateTime("since", since);
            return this;
        }

        /**
         * Set date to the query and only commits before or on this date will be returned
         *
         * @param until date in in ISO 8601 format YYYY-MM-DDTHH:MM:SSZ
         * @return {@link ProjectQuery} with the before
         */
        public ProjectQuery withUntil(ZonedDateTime until) {
            appendDateTime("until", until);
            return this;
        }

        /**
         * Set file path to the query
         *
         * @param path the file path
         * @return {@link ProjectQuery} with the the file path
         */
        public ProjectQuery withPath(String path) {
            appendString("path", path);
            return this;
        }

        /**
         * Set stat to the query, stats about each commit will be added to the response
         *
         * @param withStats stat to add to the query
         * @return {@link ProjectQuery} with the stats
         */
        public ProjectQuery withStats(boolean withStats) {
            appendBoolean("with_stats", withStats);
            return this;
        }

        /**
         * Set boolean to indicate whether to follow only the first parent commit upon seeing a merge commit
         *
         * @param firstParent whether or not to follow only the first parent
         * @return {@link ProjectQuery} with the the boolean
         */
        public ProjectQuery withFirstParent(boolean firstParent) {
            appendBoolean("first_parent", firstParent);
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
         * List commits in order.
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
         * Get the URL suffix for the HTTP request
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
         * Bind the commit with the given {@link GitlabProject} after the response is parsed
         *
         * @param component - one {@link GitlabCommit} from the response
         */
        @Override
        void bind(GitlabCommit component) {
            component.withProject(project);
        }
    }
}
