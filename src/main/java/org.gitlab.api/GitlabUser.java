package org.gitlab.api;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreType;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;

/**
 * This class serves as instance of Gitlab component User.
 *
 * This doesn't support create, update, and delete operations.
 *
 * This supports query for users globally or users within a project. See
 * {@link Query} and {@link ProjectQuery}.
 *
 * <p>
 * Gitlab Web API: https://docs.gitlab.com/ee/api/users.html
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class GitlabUser extends GitlabComponent {
    @JsonProperty(value = "id")
    private final int id;
    @JsonProperty(value = "username")
    private String username;
    @JsonProperty(value = "name")
    private String name;
    @JsonProperty(value = "state")
    private String state;
    @JsonProperty(value = "avatar_url")
    private String avatarUrl;
    @JsonProperty(value = "web_url")
    private String webUrl;
    @JsonProperty(value = "created_at")
    @JsonDeserialize(using = DateUtil.ZonedDeserializer.class)
    @JsonSerialize(using = DateUtil.ZonedSerializer.class)
    private ZonedDateTime createdAt;
    @JsonProperty(value = "bio")
    private String bio;
    @JsonProperty(value = "bio_html")
    private String bioHtml;
    @JsonProperty(value = "public_email")
    private String publicEmail;
    @JsonProperty(value = "skype")
    private String skype;
    @JsonProperty(value = "linkedin")
    private String linkedin;
    @JsonProperty(value = "twitter")
    private String twitter;
    @JsonProperty(value = "website_url")
    private String websiteUrl;
    @JsonProperty(value = "organization")
    private String organization;
    @JsonProperty(value = "job_title")
    private String jobTitle;

    /**
     * Constructor the {@link GitlabUser} instance.
     *
     * @param id id of the user
     */
    GitlabUser(@JsonProperty("id") int id) {
        this.id = id;
    }

    /**
     * Returns a string representation of this {@link GitlabUser} in the
     * format of Gitlab component type and user id and username.
     *
     * @return a string representation of this {@link GitlabUser}
     */
    @Override
    public String toString() {
        return "GitlabUser{" +
                       "id=" + id +
                       ", username=" + username +
                       ", name=" + name +
                       '}';
    }

    /**
     * Returns the hash code value for this {@link GitlabUser} identified
     * by user id.
     *
     * @return a hash code value for this object
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    /**
     * Compares the specified {@code Object} with this {@link GitlabUser}
     * for equality. Note that two {@link GitlabUser}s are equal if and only
     * if they have same user id.
     *
     * @param o object to be compared for equality with this {@link GitlabUser}
     * @return true if the specified Object is equal to this {@link GitlabUser}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GitlabUser)) {
            return false;
        }
        GitlabUser that = (GitlabUser) o;
        return id == that.id;
    }

    /**
     * Returns a {@link GitlabProject.UserQuery} that can be used to query
     * projects accessible by current user.
     *
     * <p>
     * Gitlab Web API: https://docs.gitlab.com/ee/api/projects.html#list-user-projects
     * <p>
     * GET /users/:user_id/projects
     *
     * @return a {@link GitlabProject.UserQuery}
     */
    public GitlabProject.UserQuery getUserProjectsQuery() {
        return new GitlabProject.UserQuery(httpClient, String.valueOf(id));
    }

    /**
     * Returns the id of this user.
     *
     * @return id of this user
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the username of this user.
     *
     * @return username of this user
     */
    public String getUsername() {
        return username;
    }

    /**
     * Returns the name of this user.
     *
     * @return name of this user
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the state of this user.
     *
     * @return state of this user
     */
    public String getState() {
        return state;
    }

    /**
     * Returns the avatar url of this user.
     *
     * @return avatar url of this user
     */
    public String getAvatarUrl() {
        return avatarUrl;
    }

    /**
     * Returns the web url of this user.
     *
     * @return web url of this user
     */
    public String getWebUrl() {
        return webUrl;
    }

    /**
     * Get created date of this user.
     *
     * @return created date of this user
     */
    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * Returns the bio of this user.
     *
     * @return the bio of this user
     */
    public String getBio() {
        return bio;
    }

    /**
     * Returns the bio in html of this user.
     *
     * @return bio in html of this user
     */
    public String getBioHtml() {
        return bioHtml;
    }

    /**
     * Returns the public email of this user.
     *
     * @return public email of this user
     */
    public String getPublicEmail() {
        return publicEmail;
    }

    /**
     * Returns the skype id of this user.
     *
     * @return skype id of this user
     */
    public String getSkype() {
        return skype;
    }

    /**
     * Returns the linkedin account of this user.
     *
     * @return linkedin account of this user
     */
    public String getLinkedin() {
        return linkedin;
    }

    /**
     * Returns the twitter account of this user.
     *
     * @return twitter account of this user
     */
    public String getTwitter() {
        return twitter;
    }

    /**
     * Returns the website url of this user.
     *
     * @return the website url of this user
     */
    public String getWebsiteUrl() {
        return websiteUrl;
    }

    /**
     * Returns the organization of this user.
     *
     * @return organization of this user
     */
    public String getOrganization() {
        return organization;
    }

    /**
     * Returns the job title of this user.
     *
     * @return job title of this user
     */
    public String getJobTitle() {
        return jobTitle;
    }

    /**
     * Sets a httpClient to the current {@link GitlabAPIClient}.
     *
     * @param httpClient the {@link HttpClient} to be used
     * @return {@link GitlabUser} with the httpClient
     */
    @Override
    GitlabUser withHttpClient(HttpClient httpClient) {
        super.withHttpClient(httpClient);
        return this;
    }

     /**
     * This extends {@link GitlabQuery} and supports query global users.
     *
     * Build this query with setters and call {@code query()} to execute query.
     *
     * <p>
     * Gitlab Web API: https://docs.gitlab.com/ee/api/users.html#list-users
     * <p>
     * GET /users
     */
    @JsonIgnoreType
    public static class Query extends GitlabQuery<GitlabUser> {

        Query(HttpClient httpClient) {
            super(httpClient, GitlabUser[].class);
        }

        /**
        * Returns a query that specifies page number and size to return based
        * on given pagination.
        *
        * @param pagination pagination object that defines page number and size
        * @return this {@link GitlabBranch.ProjectQuery} with given pagination
        */
        @Override
        public Query withPagination(Pagination pagination) {
            appendPagination(pagination);
            return this;
        }

        /**
         * Returns a query that matches given username.
         *
         * @param username username to query
         * @return this {@link Query} with the username
         */
        public Query withUsername(String username) {
            appendString("username", username);
            return this;
        }

        /**
         * Returns a query that returns users who are active if active is true.
         *
         * @param active whether user is active
         * @return this {@link Query} with whether user is active
         */
        public Query withActive(boolean active) {
            appendBoolean("active", active);
            return this;
        }

        /**
         * Returns a query that returns users who are blocked if blocked is
         * true.
         *
         * @param blocked whether user is blocked
         * @return this {@link Query} with whether user is blocked
         */
        public Query withBlocked(boolean blocked) {
            appendBoolean("blocked", blocked);
            return this;
        }

        /**
         * Returns a query that excludes internal which excludes alert bot and
         * support bot if excludeInternal is true.
         *
         * @param excludeInternal whether to excludes alert bot and support bot
         * @return this {@link Query} with whether user is blocked
         */
        public Query withExcludeInternal(boolean excludeInternal) {
            appendBoolean("exclude_internal", excludeInternal);
            return this;
        }

        /**
         * Returns the URL suffix for the HTTP request.
         *
         * @return The URL suffix to query {@link GitlabUser}
         */
        @Override
        String getTailUrl() {
            return "/users";
        }

        @Override
        void bind(GitlabUser component) {}
    }

    /**
     * This extends {@link GitlabQuery} and supports query users within a
     * {@link GitlabProject}.
     *
     * Build this query with setters and call {@code query()} to execute query.
     *
     * <p>
     * Gitlab Web API: https://docs.gitlab.com/ee/api/projects.html#get-project-users
     * <p>
     * GET /projects/:id/users
     */
    @JsonIgnoreType
    public static class ProjectQuery extends GitlabQuery<GitlabUser> {
        private final int projectId;

        ProjectQuery(HttpClient httpClient, int projectId) {
            super(httpClient, GitlabUser[].class);
            this.projectId = projectId;
        }

        /**
         * Returns a query that specifies page number and size to return based
         * on given pagination.
         *
         * @param pagination pagination object that defines page number and size
         * @return this {@link GitlabBranch.ProjectQuery} with given pagination
         */
        @Override
        public ProjectQuery withPagination(Pagination pagination) {
            appendPagination(pagination);
            return this;
        }

        /**
         * Returns a query that searches users against keyword.
         *
         * @param search keyword to be searched
         * @return this {@link GitlabIssue.ProjectQuery} with search
         */
        public ProjectQuery withSearch(String search) {
            appendString("search", search);
            return this;
        }

        /**
         * Returns a query that filters out given user ids.
         *
         * @param skipUsers users to be skipped
         * @return this {@link ProjectQuery} with the list of users to be skipped
         */
        public ProjectQuery withSkipUsers(List<Integer> skipUsers) {
            appendInts("skip_users", skipUsers);
            return this;
        }

        /**
         * Returns the URL suffix for the HTTP request.
         *
         * <p>
         * Gitlab Web API: https://docs.gitlab.com/ee/api/projects.html#get-project-users
         * <p>
         * GET /projects/:id/users
         *
         * @return The URL suffix to query {@link GitlabUser} in the given {@link GitlabProject}
         */
        @Override
        String getTailUrl() {
            return String.format("/projects/%d/users", projectId);
        }

        @Override
        void bind(GitlabUser component) {}
    }
}
