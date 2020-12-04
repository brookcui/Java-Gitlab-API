package org.gitlab.api.models;

import org.gitlab.api.core.GitlabAPIClient;
import org.gitlab.api.models.query.ProjectQuery;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GitlabProjectTest {
    private static final GitlabAPIClient CLIENT = new GitlabAPIClient
            .Builder("https://gitlab.com")
            .withAccessToken(System.getenv("TOKEN"))
            .build();

    @Test
    void getIssue() throws IOException {
        GitlabProject project = CLIENT.newProject("test").create();
        GitlabIssue issue = project.newIssue("new issue").create();
        assertEquals("new issue", project.getIssue(issue.getIid()).getTitle());
        project.delete();
    }

    @Test
    void query() throws IOException {
//        List<GitlabIssue> issues = CLIENT.query(query);
//        issues.forEach(System.out::println);

//        CommitQuery query = new CommitQuery(22777636).withRefName("master")
//                                                     .withUntil(LocalDateTime.now().minusDays(1));
//        List<GitlabCommit> commits = CLIENT.query(query);
//        commits.forEach(System.out::println);
        ProjectQuery query = new ProjectQuery(CLIENT.getConfig()).withOwned(true);
        List<GitlabProject> projects = CLIENT.query(query);
        projects.forEach(System.out::println);


    }
}