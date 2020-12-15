package org.gitlab.api.test;

import org.gitlab.api.GitlabAPIClient;
import org.gitlab.api.GitlabProject;
import org.gitlab.api.GitlabUser;
import org.gitlab.api.Pagination;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GitlabAPIClientTest {
    private static final GitlabAPIClient CLIENT = new GitlabAPIClient
            .Builder("https://gitlab.com")
            .withAccessToken(System.getenv("TOKEN"))
            .build();

    @Test
    void testGetSingleProject() {
        GitlabProject newProject = CLIENT.newProject("test1").create();
        assertEquals(newProject.getName(), CLIENT.getProject(newProject.getId()).getName());
        newProject.delete();
    }

    @Test
    void testGetProjects() {
        GitlabUser currentUser = CLIENT.getCurrentUser();
        int projectSize1 = CLIENT.getUserProjectsQuery(currentUser.getUsername()).query().size();
        GitlabProject newProject = CLIENT.newProject("test2").create();
        int projectSize2 = CLIENT.getUserProjectsQuery(currentUser.getUsername())
                                 .withPagination(Pagination.of(1, 100))
                                 .query()
                                 .size();
        assertEquals(projectSize1 + 1, projectSize2);
        newProject.delete();
    }

    @Test
    void testGetUser() {
        GitlabUser currentUser = CLIENT.getCurrentUser();
        GitlabUser user = CLIENT.getUser(currentUser.getId());
        assertEquals(user, currentUser);
    }
}
