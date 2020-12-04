package org.gitlab.api.models.query;

import org.gitlab.api.http.Config;
import org.gitlab.api.models.GitlabCommit;

import java.time.LocalDateTime;

public class CommitQuery extends NewQuery<GitlabCommit> {
    private final int projectId;

    public CommitQuery(int projectId, Config config) {
        super(GitlabCommit[].class, config);
        this.projectId = projectId;
    }

    public CommitQuery withRefName(String refName) {
        appendString("ref_name", refName);
        return this;
    }

    public CommitQuery withSince(LocalDateTime since) {
        appendDateTime("since", since);
        return this;
    }

    public CommitQuery withUntil(LocalDateTime until) {
        appendDateTime("until", until);
        return this;
    }

    public CommitQuery withPath(String path) {
        appendString("path", path);
        return this;
    }

    public CommitQuery withStats(boolean withStats) {
        appendBoolean("with_stats", withStats);
        return this;
    }

    public CommitQuery withFirstParent(boolean firstParent) {
        appendBoolean("first_parent", firstParent);
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
    public CommitQuery withOrder(String order) {
        appendString("order", order);
        return this;
    }

    @Override
    public String getUrlPrefix() {
        return String.format("/projects/%d/repository/commits", projectId);
    }
}
