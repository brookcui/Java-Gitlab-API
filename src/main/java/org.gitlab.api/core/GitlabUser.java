package org.gitlab.api.core;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.gitlab.api.http.Config;
import org.gitlab.api.http.GitlabHttpClient;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * This class is used to represent the gitlab user model. It contains a config object inorder to make appropriate
 * http request. all of the fields that tagged with JsonProperty are mapped to fields in the gitlab web page.
 * This class also contains a ProjectQuery Class used to build query and get users in a project, as well as a Query
 * class to build query to get users.
 *
 * This class implements GitlabComponent since only read is being supported.
 * <p>
 * Gitlab Web API: https://docs.gitlab.com/ee/api/users.html
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class GitlabUser implements GitlabComponent {

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
    private LocalDateTime createdAt;
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

    @JsonIgnore
    private Config config;

    GitlabUser(@JsonProperty("id") int id) {
        this.id = id;
    }

    /**
     * Get the all projects by current user.
     * <p>
     * Gitlab Web API: https://docs.gitlab.com/ee/api/projects.html#list-user-projects
     * GET /users/:user_id/projects
     *
     * @return A list of {@link GitlabProject} belong to current user
     */
    public List<GitlabProject> getUserProjects() {
        return GitlabHttpClient.getList(config, String.format("/users/%d/projects", id), GitlabProject[].class);

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
    public LocalDateTime getCreatedAt() {
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
        if (!(o instanceof GitlabUser)) {
            return false;
        }
        GitlabUser that = (GitlabUser) o;
        return id == that.id &&
                Objects.equals(username, that.username) &&
                Objects.equals(state, that.state) &&
                Objects.equals(avatarUrl, that.avatarUrl) &&
                Objects.equals(webUrl, that.webUrl) &&
                Objects.equals(createdAt, that.createdAt) &&
                Objects.equals(bio, that.bio) &&
                Objects.equals(bioHtml, that.bioHtml) &&
                Objects.equals(publicEmail, that.publicEmail) &&
                Objects.equals(skype, that.skype) &&
                Objects.equals(linkedin, that.linkedin) &&
                Objects.equals(twitter, that.twitter) &&
                Objects.equals(websiteUrl, that.websiteUrl) &&
                Objects.equals(organization, that.organization) &&
                Objects.equals(jobTitle, that.jobTitle);
    }

    /**
     * Get the config that is stored in current {@link GitlabUser}
     *
     * @return the config with user detail
     */
    @Override
    public Config getConfig() {
        return config;
    }

    /**
     * Add a config to the current {@link GitlabAPIClient}
     *
     * @param config a config with user details
     * @return {@link GitlabUser} with the config
     */
    @Override
    public GitlabUser withConfig(Config config) {
        this.config = config;
        return this;
    }

    /**
     * Class to query {@link GitlabUser}
     * Gitlab Web API: https://docs.gitlab.com/ee/api/users.html#list-users
     */
    public static class Query extends GitlabQuery<GitlabUser> {
        Query(Config config) {
            super(config, GitlabUser[].class);
        }

        /**
         * Add pagination on top of the query
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
         * Add a username to the query and limit the user by username
         *
         * @param username username to query
         * @return this {@link Query} with the username
         */
        public Query withUsername(String username) {
            appendString("username", username);
            return this;
        }

        /**
         * Add whether to query active user
         *
         * @param active whether user is active
         * @return this {@link Query} with whether user is active
         */
        public Query withActive(boolean active) {
            appendBoolean("active", active);
            return this;
        }

        /**
         * Add whether to query blocked user
         *
         * @param blocked whether user is blocked
         * @return this {@link Query} with whether user is blocked
         */
        public Query withBlocked(boolean blocked) {
            appendBoolean("blocked", blocked);
            return this;
        }

        /**
         * Add whether to exclude internal which excludes alert bot and support bot
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
        public String getTailUrl() {
            return "/users";
        }

        @Override
        void bind(GitlabUser component) {

        }
    }

    /**
     * Class to query {@link GitlabUser} with configuration and the {@link GitlabProject}
     * Gitlab Web API: https://docs.gitlab.com/ee/api/projects.html#get-project-users
     */
    public static class ProjectQuery extends GitlabQuery<GitlabUser> {
        private final int projectId;

        ProjectQuery(Config config, int projectId) {
            super(config, GitlabUser[].class);
            this.projectId = projectId;
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
         * Add a search parameter to the query to search for a specific user
         *
         * @param search specific user
         * @return this {@link ProjectQuery} with the user specified
         */
        public ProjectQuery withSearch(String search) {
            appendString("search", search);
            return this;
        }

        /**
         * Add a list of user ids to filter out
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
         *
         * @return The URL suffix to query {@link GitlabUser} in the given {@link GitlabProject}
         */
        @Override
        public String getTailUrl() {
            return String.format("/projects/%d/users", projectId);
        }

        @Override
        void bind(GitlabUser component) {

        }
    }
}
