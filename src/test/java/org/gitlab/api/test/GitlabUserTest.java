package org.gitlab.api.test;

import org.gitlab.api.GitlabAPIClient;
import org.gitlab.api.GitlabProject;
import org.gitlab.api.GitlabUser;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class GitlabUserTest {
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
    void testCurrentUser() {
        GitlabUser currentUser = CLIENT.getCurrentUser();
        assertNotNull(currentUser);
    }

    @Test
    void testEqual() {
        GitlabUser currentUser = CLIENT.getCurrentUser();
        GitlabUser user = CLIENT.getUser(CLIENT.getProject(23057280).getOwner().getId());
        assertEquals(user, currentUser);
    }

    @Test
    void testQuery() {
        GitlabUser currentUser = CLIENT.getCurrentUser();
        List<GitlabUser> users1 = project.getUsersQuery().query();
        assertEquals(1, users1.size());
        List<GitlabUser> users2 = project.getUsersQuery().withSearch(currentUser.getUsername()).query();
        assertEquals(1, users2.size());
        List<GitlabUser> users3 = CLIENT.getUsersQuery().query();
        assertTrue(users3.size() >= 1);
    }

}
