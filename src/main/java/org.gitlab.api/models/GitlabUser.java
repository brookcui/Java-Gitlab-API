
package org.gitlab.api.models;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.gitlab.api.http.GitlabHTTPRequestor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY,
        getterVisibility = JsonAutoDetect.Visibility.NONE
)
public class GitlabUser extends GitlabComponent {
    @JsonProperty(value = "id", access = JsonProperty.Access.WRITE_ONLY)
    private final int id;
    @JsonProperty(value = "username", access = JsonProperty.Access.WRITE_ONLY)
    private String username;
    @JsonProperty(value = "name", access = JsonProperty.Access.WRITE_ONLY)
    private String name;
    @JsonProperty(value = "state", access = JsonProperty.Access.WRITE_ONLY)
    private String state;
    @JsonProperty(value = "avatar_url", access = JsonProperty.Access.WRITE_ONLY)
    private String avatarUrl;
    @JsonProperty(value = "web_url", access = JsonProperty.Access.WRITE_ONLY)
    private String webUrl;
    @JsonProperty(value = "created_at", access = JsonProperty.Access.WRITE_ONLY)
    private LocalDateTime createdAt;
    @JsonProperty(value = "bio", access = JsonProperty.Access.WRITE_ONLY)
    private String bio;
    @JsonProperty(value = "bio_html", access = JsonProperty.Access.WRITE_ONLY)
    private String bioHtml;
    @JsonProperty(value = "public_email", access = JsonProperty.Access.WRITE_ONLY)
    private String publicEmail;
    @JsonProperty(value = "skype", access = JsonProperty.Access.WRITE_ONLY)
    private String skype;
    @JsonProperty(value = "linkedin", access = JsonProperty.Access.WRITE_ONLY)
    private String linkedin;
    @JsonProperty(value = "twitter", access = JsonProperty.Access.WRITE_ONLY)
    private String twitter;
    @JsonProperty(value = "website_url", access = JsonProperty.Access.WRITE_ONLY)
    private String websiteUrl;
    @JsonProperty(value = "organization", access = JsonProperty.Access.WRITE_ONLY)
    private String organization;
    @JsonProperty(value = "job_title", access = JsonProperty.Access.WRITE_ONLY)
    private String jobTitle;

    public GitlabUser(@JsonProperty("id") int id) {
        this.id = id;
    }

    public List<GitlabProject> getUserProjects() {
        return null; // TODO
    }

    // TODO: Find a better way to maintain consistency between all other models
    public static GitlabUser fromId(int id) {
        return null;
    }

@Override
    public GitlabUser withHTTPRequestor(GitlabHTTPRequestor requestor) {
        super.withHTTPRequestor(requestor);
        return this;
    }


//
//     * Getters
//     */
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
}
