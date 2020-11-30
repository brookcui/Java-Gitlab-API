package org.gitlab.api.models;

import org.gitlab.api.core.GitlabAPIClient;
import org.junit.jupiter.api.Test;

import java.io.IOException;

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
}