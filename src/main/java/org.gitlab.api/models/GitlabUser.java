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

    // TODO: getters for all necessary public fields

    public List<GitlabProject> getUserProjects() {
        return null; // TODO
    }

    // TODO: Find a better way to maintain consistency between all other models
    public static GitlabUser fromId(int id){
        return null;
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
        GitlabUser user = (GitlabUser) o;
        return user.id == this.id;
    }
}
