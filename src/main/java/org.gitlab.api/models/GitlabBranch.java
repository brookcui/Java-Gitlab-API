package org.gitlab.api.models;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.gitlab.api.http.GitlabHTTPRequestor;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY,
        getterVisibility = JsonAutoDetect.Visibility.NONE
)
public class GitlabBranch extends GitlabComponent {
    private final String name;
    @JsonProperty(value = "merged", access = JsonProperty.Access.WRITE_ONLY)
    private boolean merged;
    @JsonProperty(value = "protected", access = JsonProperty.Access.WRITE_ONLY)
    private boolean isProtected; // for "protected"
    @JsonProperty(value = "default", access = JsonProperty.Access.WRITE_ONLY)
    private boolean isDefault; // for "default"
    @JsonProperty(value = "can_push", access = JsonProperty.Access.WRITE_ONLY)
    private boolean canPush;
    @JsonProperty(value = "web_url", access = JsonProperty.Access.WRITE_ONLY)
    private String webUrl;
    private GitlabCommit commit; // corresponds to branch name or commit SHA to create branch from
    @JsonIgnore
    private final GitlabProject project;

    public GitlabBranch(@JsonProperty("project") GitlabProject project,
                        @JsonProperty("name") String name) {
        this.project = project;
        this.name = name;
        // TODO: convert ref to GitlabCommit and initialize field commit
    }

    @Override
    public GitlabBranch withHTTPRequestor(GitlabHTTPRequestor requestor) {
        super.withHTTPRequestor(requestor);
        if (commit != null) {
            commit.withHTTPRequestor(requestor);
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
        Map<String, String> map = new HashMap<>();
        map.put("branch", name);
        map.put("ref", ref);
        return getHTTPRequestor().post(String.format("/projects/%d/repository/branches", project.getId()), map, this);
    }

    public GitlabBranch delete() throws IOException {
        return this; // TODO
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
}
