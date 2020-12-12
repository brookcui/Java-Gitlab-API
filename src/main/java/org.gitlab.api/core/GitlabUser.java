package org.gitlab.api.core;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.gitlab.api.http.Config;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

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

    // TODO: Find a better way to maintain consistency between all other models
    public static GitlabUser fromId(int id) {
        return null;
    }

    public List<GitlabProject> getUserProjects() {
        return null; // TODO
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getState() {
        return state;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public String getBio() {
        return bio;
    }

    public String getBioHtml() {
        return bioHtml;
    }

    public String getPublicEmail() {
        return publicEmail;
    }

    public String getSkype() {
        return skype;
    }

    public String getLinkedin() {
        return linkedin;
    }

    public String getTwitter() {
        return twitter;
    }

    public String getWebsiteUrl() {
        return websiteUrl;
    }

    public String getOrganization() {
        return organization;
    }

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
    // TODO: compare all fields for equals
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

    @Override
    public Config getConfig() {
        return config;
    }

    @Override
    public GitlabUser withConfig(Config config) {
        this.config = config;
        return this;
    }

    public static class Query extends GitlabQuery<GitlabUser> {
        Query(Config config) {
            super(config, GitlabUser[].class);
        }

        @Override
        public Query withPagination(Pagination pagination) {
            appendPagination(pagination);
            return this;
        }

        public Query withUsername(String username) {
            appendString("username", username);
            return this;
        }

        public Query withActive(boolean active) {
            appendBoolean("active", active);
            return this;
        }

        public Query withBlocked(boolean blocked) {
            appendBoolean("blocked", blocked);
            return this;
        }

        public Query withExcludeInternal(boolean excludeInternal) {
            appendBoolean("exclude_internal", excludeInternal);
            return this;
        }

        @Override
        public String getUrlSuffix() {
            return "/users";
        }

        @Override
        void bind(GitlabUser component) {

        }
    }

    public static class ProjectQuery extends GitlabQuery<GitlabUser> {
        private final int projectId;

        ProjectQuery(Config config, int projectId) {
            super(config, GitlabUser[].class);
            this.projectId = projectId;
        }

        @Override
        public ProjectQuery withPagination(Pagination pagination) {
            appendPagination(pagination);
            return this;
        }

        public ProjectQuery withSearch(String search) {
            appendString("search", search);
            return this;
        }

        public ProjectQuery withSkipUsers(List<Integer> skipUsers) {
            appendInts("skip_users", skipUsers);
            return this;
        }

        @Override
        public String getUrlSuffix() {
            return String.format("/projects/%d/users", projectId);
        }

        @Override
        void bind(GitlabUser component) {

        }
    }
}
