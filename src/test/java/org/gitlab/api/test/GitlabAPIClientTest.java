package org.gitlab.api.test;

import org.gitlab.api.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class GitlabAPIClientTest {
    private static final GitlabAPIClient CLIENT = new GitlabAPIClient
            .Builder("https://gitlab.com")
            .withAccessToken(System.getenv("TOKEN"))
            .build();

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
        int projectSize1 = CLIENT.getUserProjectsQuery(currentUser.getUsername()).query().size();
        GitlabProject newProject = CLIENT.newProject("test2").create();
        int projectSize2 = CLIENT.getUserProjectsQuery(currentUser.getUsername()).query().size();
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
