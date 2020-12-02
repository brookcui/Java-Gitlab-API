package org.gitlab.api.models;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.gitlab.api.http.GitlabHTTPRequestor;
import org.gitlab.api.http.LocalDateTimeDeserializer;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY,
        getterVisibility = JsonAutoDetect.Visibility.NONE
)
public class GitlabCommit extends GitlabComponent {
    @JsonProperty( "id")
    private final String id;
    @JsonProperty( "short_id")
    private String shortId;
    @JsonProperty( "title")
    private String title;
    @JsonProperty( "author_name")
    private String authorName;
    @JsonProperty( "author_email")
    private String authorEmail;
    @JsonProperty( "committer_name")
    private String committerName;
    @JsonProperty( "committer_email")
    private String committerEmail;
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonProperty( "created_at")
    private LocalDateTime createdAt;
    @JsonProperty( "message")
    private String message;
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonProperty( "committed_date")
    private LocalDateTime committedDate;
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonProperty( "authored_date")
    private LocalDateTime authoredDate;
    @JsonProperty( "parent_ids")
    private final List<String> parentIds = new ArrayList<>();
    @JsonProperty( "status")
    private String status;
    @JsonProperty( "web_url")
    private String webUrl;

    @JsonIgnore
    private GitlabProject project;

    GitlabCommit withProject(GitlabProject project) {
        this.project = project;
        return this;
    }
    GitlabCommit(@JsonProperty("id") String id) {
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
