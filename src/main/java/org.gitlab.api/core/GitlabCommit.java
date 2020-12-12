package org.gitlab.api.core;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.gitlab.api.http.Config;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class GitlabCommit implements GitlabComponent {
    @JsonProperty("id")
    private final String id;
    @JsonProperty("parent_ids")
    private final List<String> parentIds = new ArrayList<>();
    @JsonIgnore
    private Config config;
    @JsonProperty("short_id")
    private String shortId;
    @JsonProperty("title")
    private String title;
    @JsonProperty("author_name")
    private String authorName;
    @JsonProperty("author_email")
    private String authorEmail;
    @JsonProperty("committer_name")
    private String committerName;
    @JsonProperty("committer_email")
    private String committerEmail;
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonProperty("created_at")
    private LocalDateTime createdAt;
    @JsonProperty("message")
    private String message;
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonProperty("committed_date")
    private LocalDateTime committedDate;
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonProperty("authored_date")
    private LocalDateTime authoredDate;
    @JsonProperty("status")
    private String status;
    @JsonProperty("web_url")
    private String webUrl;
    @JsonIgnore
    private GitlabProject project;
    GitlabCommit(@JsonProperty("id") String id) {
        this.id = id;
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

    public String getWebUrl() {
        return webUrl;
    }

    public GitlabProject getProject() {
        return project;
    }


    @Override
    public Config getConfig() {
        return config;
    }

    @Override
    public GitlabCommit withConfig(Config config) {
        this.config = config;
        return this;
    }

    GitlabCommit withProject(GitlabProject project) {
        this.project = project;
        return this;
    }


    public static class ProjectQuery extends GitlabQuery<GitlabCommit> {
        private final GitlabProject project;

        ProjectQuery(Config config, GitlabProject project) {
            super(config, GitlabCommit[].class);
            this.project = project;
        }

        public ProjectQuery withRefName(String refName) {
            appendString("ref_name", refName);
            return this;
        }

        public ProjectQuery withSince(LocalDateTime since) {
            appendDateTime("since", since);
            return this;
        }

        public ProjectQuery withUntil(LocalDateTime until) {
            appendDateTime("until", until);
            return this;
        }

        public ProjectQuery withPath(String path) {
            appendString("path", path);
            return this;
        }

        public ProjectQuery withStats(boolean withStats) {
            appendBoolean("with_stats", withStats);
            return this;
        }

        public ProjectQuery withFirstParent(boolean firstParent) {
            appendBoolean("first_parent", firstParent);
            return this;
        }

        @Override
        public ProjectQuery withPagination(Pagination pagination) {
            appendPagination(pagination);
            return this;
        }

        /**
         * List commits in order.
         * <p>
         * Possible values: default, topo. Defaults to default, the commits are shown in reverse chronological order.
         *
         * @param order
         * @return
         */
        public ProjectQuery withOrder(String order) {
            appendString("order", order);
            return this;
        }


        @Override
        public String getUrlPrefix() {
            return String.format("/projects/%d/repository/commits", project.getId());
        }

        @Override
         void bind(GitlabCommit component) {
            component.withProject(project);
        }
    }



    private static class LocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {
        @Override
        public LocalDateTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) {
            try {
                return ZonedDateTime.parse(jsonParser.getText()).toLocalDateTime();
            } catch (IOException e) {
                throw new GitlabException(e);
            }
        }
    }

}
