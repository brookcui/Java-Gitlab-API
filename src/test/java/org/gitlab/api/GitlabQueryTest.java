package org.gitlab.api;

import org.gitlab.api.models.GitlabAPIClient;
import org.gitlab.api.models.GitlabBranch;
import org.gitlab.api.models.GitlabProject;
import org.junit.jupiter.api.Test;

import java.util.List;

class GitlabQueryTest {
    private static final GitlabAPIClient CLIENT = GitlabAPIClient
            .fromAccessToken("https://gitlab.com", (System.getenv("TOKEN")));


    @Test
    void queryBranch() {
        GitlabProject project = CLIENT.getProject(22777636);
        List<GitlabBranch> branches = project.branches()
                                             .withSearch("test")
                                             .query();
        branches.forEach(System.out::println);
    }

    @Test
    void queryCommits() {
        GitlabProject project = CLIENT.getProject(22777636);
        List<GitlabBranch> branches = project.branches()
                                             .withSearch("test")
                                             .query();
        branches.forEach(System.out::println);
    }
}