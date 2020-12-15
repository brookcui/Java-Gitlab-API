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
 * This class is used to represent the gitlab user.
 * <p>
 * This class also contains a {@link ProjectQuery} Class used to build query and get users in a project, as well as a
 * {@link Query} class to build query to get users globally.
 * <p>
 * This class is read only.
 * <p>
 * Gitlab Web API: https://docs.gitlab.com/ee/api/users.html
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class GitlabUser extends GitlabComponent {
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
     * Constructor a gitlab user instance
     *
     * @param id id of the user
     */
    GitlabUser(@JsonProperty("id") int id) {
        this.id = id;
    }

    /**
     * Get a {@link GitlabProject.UserQuery} that can be used to query projects accessible by the current authenticated user
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
     * Get the id of the current user
     *
     * @return id of the current user
     */
    public int getId() {
        return id;
    }

    /**
     * Get the username of the current user
     *
     * @return username of the current user
     */
    public String getUsername() {
        return username;
    }

    /**
     * Get the name of the current user
     *
     * @return name of the current user
     */
    public String getName() {
        return name;
    }

    /**
     * Get the state of the current user
     *
     * @return state of the current user
     */
    public String getState() {
        return state;
    }

    /**
     * Get the avatar url of the current user
     *
     * @return avatar url of the current user
     */
    public String getAvatarUrl() {
        return avatarUrl;
    }

    /**
     * Get the web url of the current user
     *
     * @return web url of the current user
     */
    public String getWebUrl() {
        return webUrl;
    }

    /**
     * Get created date of the current user
     *
     * @return created date of the current user
     */
    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * Get the bio of the current user
     *
     * @return the bio of the current user
     */
    public String getBio() {
        return bio;
    }

    /**
     * Get the bio in html of the current user
     *
     * @return bio in html of the current user
     */
    public String getBioHtml() {
        return bioHtml;
    }

    /**
     * Get the public email of the current user
     *
     * @return public email of the current user
     */
    public String getPublicEmail() {
        return publicEmail;
    }

    /**
     * Get the skype id of the current user
     *
     * @return skype id of the current user
     */
    public String getSkype() {
        return skype;
    }

    /**
     * Get the linkedin account of the current user
     *
     * @return linkedin account of the current user
     */
    public String getLinkedin() {
        return linkedin;
    }

    /**
     * Get the twitter account of the current user
     *
     * @return twitter account of the current user
     */
    public String getTwitter() {
        return twitter;
    }

    /**
     * Get the website url of the current user
     *
     * @return the website url of the current user
     */
    public String getWebsiteUrl() {
        return websiteUrl;
    }

    /**
     * Get the organization of the current user
     *
     * @return organization of the current user
     */
    public String getOrganization() {
        return organization;
    }

    /**
     * Get the job title of the current user
     *
     * @return job title of the current user
     */
    public String getJobTitle() {
        return jobTitle;
    }

    /**
     * The string representation of this {@link GitlabUser}
     *
     * @return the string representation of this {@link GitlabUser}
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
     * Two {@link GitlabUser}s are equal if and only if they have the same id
     *
     * @param o the reference object with which to compare.
     * @return if and only if they have the same id
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GitlabUser that = (GitlabUser) o;
        return id == that.id;
    }

    /**
     * Two {@link GitlabUser}s will have the same hashcode if they the same id
     *
     * @return a hash code value for this object.
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }


    /**
     * Set a httpClient to the current {@link GitlabAPIClient}
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
     * Class to query {@link GitlabUser}
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
         * Set pagination on top of the query
         *
         * @param pagination pagination object that defines page number and size
         * @return this {@link Query} with the given pagination object
         */
        @Override
        public Query withPagination(Pagination pagination) {
            appendPagination(pagination);
            return this;
        }

        /**
         * Set a username to the query and limit the user by username
         *
         * @param username username to query
         * @return this {@link Query} with the username
         */
        public Query withUsername(String username) {
            appendString("username", username);
            return this;
        }

        /**
         * Set whether to query active user
         *
         * @param active whether user is active
         * @return this {@link Query} with whether user is active
         */
        public Query withActive(boolean active) {
            appendBoolean("active", active);
            return this;
        }

        /**
         * Set whether to query blocked user
         *
         * @param blocked whether user is blocked
         * @return this {@link Query} with whether user is blocked
         */
        public Query withBlocked(boolean blocked) {
            appendBoolean("blocked", blocked);
            return this;
        }

        /**
         * Set whether to exclude internal which excludes alert bot and support bot
         *
         * @param excludeInternal whether to excludes alert bot and support bot
         * @return this {@link Query} with whether user is blocked
         */
        public Query withExcludeInternal(boolean excludeInternal) {
            appendBoolean("exclude_internal", excludeInternal);
            return this;
        }

        /**
         * Get the URL suffix for the HTTP request
         *
         * @return The URL suffix to query {@link GitlabUser}
         */
        @Override
        String getTailUrl() {
            return "/users";
        }

        @Override
        void bind(GitlabUser component) {

        }
    }

    /**
     * Class to query {@link GitlabUser} with httpClienturation and the {@link GitlabProject}
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
         * Set a search parameter to the query to search for a specific user
         *
         * @param search specific user
         * @return this {@link ProjectQuery} with the user specified
         */
        public ProjectQuery withSearch(String search) {
            appendString("search", search);
            return this;
        }

        /**
         * Set a list of user ids to filter out
         *
         * @param skipUsers users to be skipped
         * @return this {@link ProjectQuery} with the list of users to be skipped
         */
        public ProjectQuery withSkipUsers(List<Integer> skipUsers) {
            appendInts("skip_users", skipUsers);
            return this;
        }

        /**
         * Get the URL suffix for the HTTP request
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
        void bind(GitlabUser component) {

        }
    }
}
