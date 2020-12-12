package org.gitlab.api.core;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
public class GitlabUserTest {
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
    void testCurrentUser() {
        GitlabUser currentUser = CLIENT.getCurrentUser();
        assertNotNull(currentUser);
    }

    @Test
    void testEqual() {
        GitlabUser currentUser = CLIENT.getCurrentUser();
        GitlabUser user = CLIENT.getUser(CLIENT.getProject(23057280).getOwner().getId());
        assertTrue(currentUser.equals(user));
    }

    @Test
    void testToString() {
        GitlabUser currentUser = CLIENT.getCurrentUser();
        assertEquals(currentUser.getUsername(), currentUser.toString()); //fail now, wait for impl-dev

    }

    @Test
    void testQuery() {
        GitlabUser currentUser = CLIENT.getCurrentUser();
        List<GitlabUser> users1 = project.users().query();
        assertEquals(1, users1.size());
        List<GitlabUser> users2 = project.users().withSearch(currentUser.getUsername()).query();
        assertEquals(1, users2.size());
        List<GitlabUser> users3 = CLIENT.users().query();
        assertTrue(users3.size() >= 1);
    }

}
