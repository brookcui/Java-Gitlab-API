package org.gitlab.api.test;

import org.gitlab.api.core.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class GitLabCommitTest {
    private static final GitlabAPIClient CLIENT =
            GitlabAPIClient.fromAccessToken("https://gitlab.com", System.getenv("TOKEN"));
    private GitlabProject project;
    @BeforeEach
    void setup() {
        Random ran = new Random();
        String num = String.valueOf(ran.nextInt(100));
        project = CLIENT.newProject("test" + num).create();
    }
    @AfterEach
    void cleanup() {
        project.delete();
    }

    @Test
    void testQuery() {
        assertDoesNotThrow(()->{project.commits().query();});
        List<GitlabProject> projects = CLIENT.getUserProjects(CLIENT.getCurrentUser().getUsername());
        for (GitlabProject p : projects) {
            List<GitlabCommit> commits = project.commits().query();
            assertTrue(commits.size() >= 0);
        }
    }
}
