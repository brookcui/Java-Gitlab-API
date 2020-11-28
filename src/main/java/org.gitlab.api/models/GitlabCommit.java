package models;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class GitlabCommit {

    private String id;
    private String shortId;
    private String title;
    private String authorName;
    private String authorEmail;
    private String committerName;
    private String committerEmail;
    private LocalDateTime createdAt;
    private String message;
    private LocalDateTime committedDate;
    private LocalDateTime authoredDate;
    private List<String> parentIds;
    private String status;
    private String webUrl;

    public GitlabCommit(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return title;
    }

    @Override
    public int hashCode() {
        return Objects.
                hash(id, shortId, title, authorName, authorEmail,
                        committerName, committerEmail, createdAt,
                        message, committedDate, parentIds, status,
                        webUrl);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof GitlabCommit)) {
            return false;
        }
        GitlabCommit commit = (GitlabCommit) o;
        return Objects.equals(commit.id, this.id) &&
                Objects.equals(commit.shortId, this.shortId) &&
                Objects.equals(commit.title, this.title) &&
                Objects.equals(commit.authorName, this.authorName) &&
                Objects.equals(commit.authorEmail, this.authorEmail) &&
                Objects.equals(commit.committerName, this.committerName) &&
                Objects.equals(commit.committerEmail, this.committerEmail) &&
                Objects.equals(commit.createdAt, this.createdAt) &&
                Objects.equals(commit.message, this.message) &&
                Objects.equals(commit.committedDate, this.committedDate) &&
                Objects.equals(commit.authoredDate, this.authoredDate) &&
                Objects.equals(commit.parentIds, this.parentIds) &&
                Objects.equals(commit.status, this.status) &&
                Objects.equals(commit.webUrl, webUrl);
    }

    /*
     * Getters
     */
    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthorName() {
        return authorName;
    }

    public String getAuthorEmail() {
        return authorEmail;
    }

    public String getCommitterName() {
        return committerName;
    }

    public String getCommitterEmail() {
        return committerEmail;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getCommittedDate() {
        return committedDate;
    }

    public LocalDateTime getAuthoredDate() {
        return authoredDate;
    }

    public List<String> getParentIds() {
        return parentIds;
    }

    public String getStatus() {
        return status;
    }

    private String getWebUrl() {
        return webUrl;
    }
}
