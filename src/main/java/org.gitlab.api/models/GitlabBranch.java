package org.gitlab.api.models;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.gitlab.api.http.Body;
import org.gitlab.api.http.Config;
import org.gitlab.api.http.GitlabRestClient;

import java.io.IOException;
import java.util.Objects;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class GitlabBranch implements AuthComponent {
    @JsonIgnore
    private Config config;

    @JsonProperty("name")
    private final String name;
    @JsonProperty("merged")
    private boolean merged;
    @JsonProperty("protected")
    private boolean isProtected; // for "protected"
    @JsonProperty("default")
    private boolean isDefault; // for "default"
    @JsonProperty("can_push")
    private boolean canPush;
    @JsonProperty("web_url")
    private String webUrl;
    @JsonProperty("commit")
    private GitlabCommit commit; // corresponds to branch name or commit SHA to create branch from


    @JsonIgnore
    private GitlabProject project;

    GitlabBranch(@JsonProperty("name") String name) {
        this.name = name;
    }

    @Override
    public Config getConfig() {
        return config;
    }

    @Override
    public GitlabBranch withConfig(Config config) {
        this.config = config;
        if (commit != null) {
            commit.withConfig(config);
        }
        return this;
    }

    public String getName() {
        return name;
    }

    public boolean isMerged() {
        return merged;
    }

    public boolean isProtected() {
        return isProtected;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public boolean canPush() {
        return canPush;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public GitlabCommit getCommit() {
        return commit;
    }

    public GitlabBranch create(String ref) throws IOException {
        Body body = new Body()
                .putString("branch", name)
                .putString("ref", ref);
        return GitlabRestClient.post(config, String.format("/projects/%d/repository/branches", project.getId()), body,
                this);
    }

    public GitlabBranch delete() throws IOException {
        GitlabRestClient.delete(config, String.format("/projects/%d/repository/branches/%s", project.getId(), name));
        return this;
    }

    @Override
    public String toString() {
        return "GitlabBranch{" +
                "name=" + name +
                ", merged=" + merged +
                ", isProtected=" + isProtected +
                ", isDefault=" + isDefault +
                ", canPush=" + canPush +
                ", webUrl=" + webUrl +
                ", commit=" + commit +
                ", project=" + project +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GitlabBranch)) {
            return false;
        }
        GitlabBranch that = (GitlabBranch) o;
        return merged == that.merged &&
                isProtected == that.isProtected &&
                isDefault == that.isDefault &&
                canPush == that.canPush &&
                name.equals(that.name) &&
                webUrl.equals(that.webUrl) &&
                commit.equals(that.commit);
    }

    public GitlabProject getProject() {
        return project;
    }

    GitlabBranch withProject(GitlabProject project) {
        this.project = project;
        if (this.commit != null) {
            commit.withProject(project);
        }
        return this;
    }

}
