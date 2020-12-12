package org.gitlab.api.core;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GitlabAPIClientTest {
    private static final GitlabAPIClient CLIENT =
            GitlabAPIClient.fromAccessToken("https://gitlab.com", System.getenv("TOKEN"));

    @Test
    void testGetSingleProject() {
        GitlabUser currentUser = CLIENT.getCurrentUser();
        GitlabProject newProject = CLIENT.newProject("test1").create();
        assertEquals(newProject.getName(), CLIENT.getProject(newProject.getId()).getName());
        newProject.delete();
    }
    @Test
    void testGetProjects() {
        GitlabUser currentUser = CLIENT.getCurrentUser();
        int projectSize1 = CLIENT.getUserProjects(currentUser.getUsername()).size();
        GitlabProject newProject = CLIENT.newProject("test2").create();
        int projectSize2 = CLIENT.getUserProjects(currentUser.getUsername()).size();
        assertEquals(projectSize1+1, projectSize2);
        newProject.delete();
    }

    @Test
    void testGetUser() {
        GitlabUser currentUser = CLIENT.getCurrentUser();
        GitlabUser user = CLIENT.getUser(currentUser.getId());
        assertTrue(currentUser.equals(user));
    }
}
