package org.gitlab.api.models.query;

import org.gitlab.api.http.Config;
import org.gitlab.api.models.GitlabBranch;

public class BranchQuery extends NewQuery<GitlabBranch> {

    private final int projectId;

    public BranchQuery(int projectId, Config config) {
        super(GitlabBranch[].class, config);
        this.projectId = projectId;
    }


    public BranchQuery withSearch(String search) {
        appendString("search", search);
        return this;
    }

    @Override
    public String getUrlPrefix() {
        return String.format("/projects/%d/repository/branches", projectId);
    }
}
