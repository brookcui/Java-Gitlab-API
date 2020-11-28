package models;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class GitlabUser {
    private int id;
    private String username;
    private String name;
    private String state;
    private String avatarUrl;
    private String webUrl;
    private LocalDateTime createdAt;
    private String bio;
    private String bioHtml;
    private String publicEmail;
    private String skype;
    private String linkedin;
    private String twitter;
    private String websiteUrl;
    private String organization;
    private String jobTitle;

    public List<GitlabProject> getUserProjects() {
        return null; // TODO
    }

    // TODO: Find a better way to maintain consistency between all other models
    public static GitlabUser fromId(int id){
        return null;
    }


    /*
     * Getters
     */
    public int getId() { return id; }

    public String getUsername() { return username; }

    public String getState() { return state; }

    public String getAvatarUrl() { return avatarUrl; }

    public String getWebUrl() { return webUrl; }

    public LocalDateTime getCreatedAt() { return createdAt; }

    public String getBio() { return bio; }

    public String getBioHtml() { return bioHtml; }

    public String getPublicEmail() { return publicEmail; }

    public String getSkype() { return skype; }

    public String getLinkedin() { return linkedin; }

    public String getTwitter() { return twitter; }

    public String getWebsiteUrl() { return websiteUrl; }

    public String getOrganization() { return organization; }

    public String getJobTitle() { return jobTitle; }

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
