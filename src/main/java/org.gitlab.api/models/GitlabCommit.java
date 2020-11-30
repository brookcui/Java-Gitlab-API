package org.gitlab.api.models;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.gitlab.api.http.GitlabHTTPRequestor;
import org.gitlab.api.http.LocalDateTimeDeserializer;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY,
        getterVisibility = JsonAutoDetect.Visibility.NONE
)
public class GitlabCommit extends GitlabComponent {
    @JsonProperty(value = "id", access = JsonProperty.Access.WRITE_ONLY)
    private final String id;
    @JsonProperty(value = "short_id", access = JsonProperty.Access.WRITE_ONLY)
    private String shortId;
    @JsonProperty(value = "title", access = JsonProperty.Access.WRITE_ONLY)
    private String title;
    @JsonProperty(value = "author_name", access = JsonProperty.Access.WRITE_ONLY)
    private String authorName;
    @JsonProperty(value = "author_email", access = JsonProperty.Access.WRITE_ONLY)
    private String authorEmail;
    @JsonProperty(value = "committer_name", access = JsonProperty.Access.WRITE_ONLY)
    private String committerName;
    @JsonProperty(value = "committer_email", access = JsonProperty.Access.WRITE_ONLY)
    private String committerEmail;
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonProperty(value = "created_at", access = JsonProperty.Access.WRITE_ONLY)
    private LocalDateTime createdAt;
    @JsonProperty(value = "message", access = JsonProperty.Access.WRITE_ONLY)
    private String message;
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonProperty(value = "committed_date", access = JsonProperty.Access.WRITE_ONLY)
    private LocalDateTime committedDate;
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonProperty(value = "authored_date", access = JsonProperty.Access.WRITE_ONLY)
    private LocalDateTime authoredDate;
    @JsonProperty(value = "parent_ids", access = JsonProperty.Access.WRITE_ONLY)
    private List<String> parentIds;
    @JsonProperty(value = "status", access = JsonProperty.Access.WRITE_ONLY)
    private String status;
    @JsonProperty(value = "web_url", access = JsonProperty.Access.WRITE_ONLY)
    private String webUrl;

    @JsonIgnore
    private GitlabProject project;

    GitlabCommit withProject(GitlabProject project) {
        this.project = project;
        return this;
    }
    public GitlabCommit(@JsonProperty("id") String id) {
        this.id = id;
    }

    @Override
    public GitlabCommit withHTTPRequestor(GitlabHTTPRequestor requestor) {
        super.withHTTPRequestor(requestor);
        return this;
    }

    @Override
    public String toString() {
        return "GitlabCommit{" +
                "id=" + id +
                ", shortId=" + shortId +
                ", title=" + title +
                ", authorName=" + authorName +
                ", authorEmail=" + authorEmail +
                ", committerName=" + committerName +
                ", committerEmail=" + committerEmail +
                ", createdAt=" + createdAt +
                ", message=" + message +
                ", committedDate=" + committedDate +
                ", authoredDate=" + authoredDate +
                ", parentIds=" + parentIds +
                ", status=" + status +
                ", webUrl=" + webUrl +
                '}';
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

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getShortId() {
        return shortId;
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

    public GitlabProject getProject() {
        return project;
    }

}
