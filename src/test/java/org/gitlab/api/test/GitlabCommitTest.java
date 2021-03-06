package org.gitlab.api.test;

import org.gitlab.api.GitlabAPIClient;
import org.gitlab.api.GitlabCommit;
import org.gitlab.api.GitlabException;
import org.gitlab.api.GitlabProject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GitlabCommitTest {
    private static final GitlabAPIClient CLIENT = new GitlabAPIClient
            .Builder("https://gitlab.com")
            .withAccessToken(System.getenv("TOKEN"))
            .build();
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
        assertDoesNotThrow(() -> {
            project.getCommitsQuery().query();
        });
        List<GitlabProject> projects = CLIENT.getUserProjectsQuery(CLIENT.getCurrentUser().getUsername()).query();
        for (GitlabProject p : projects) {
            assertDoesNotThrow(()->{p.getCommitsQuery().query();});
        }
    }
}
