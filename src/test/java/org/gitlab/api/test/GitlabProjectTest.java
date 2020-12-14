package org.gitlab.api.test;

import org.gitlab.api.GitlabAPIClient;
import org.gitlab.api.GitlabException;
import org.gitlab.api.GitlabProject;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.jupiter.api.Assertions.*;

class GitlabProjectTest {
    private static final GitlabAPIClient CLIENT =
            new GitlabAPIClient.Builder("https://gitlab.com").withAccessToken(System.getenv("TOKEN")).build();

    @Test
    void testCRUD() {
        String projectName = "test" + ThreadLocalRandom.current().nextInt();
        GitlabProject project = CLIENT.newProject(projectName).create();
        assertEquals(projectName, project.getName());
        GitlabProject updatedProject = project.withDescription("desc").update();
        assertEquals(project, updatedProject);
        assertEquals("desc", updatedProject.getDescription());
        GitlabProject deletedProject = project.delete();
        assertEquals(project, deletedProject);
    }

    @Test
    void testDuplicateDelete() {
        String projectName = "test" + ThreadLocalRandom.current().nextInt();
        GitlabProject project = CLIENT.newProject(projectName).create();
        assertEquals(projectName, project.getName());
        GitlabProject deletedProject = project.delete();
        assertEquals(project, deletedProject);
        assertThrows(GitlabException.class, () -> {
            project.delete();
        });
    }

    @Test
    void testQuery() {
        List<GitlabProject> projects = CLIENT.getProjectsQuery().query();
        String projectName = "test" + ThreadLocalRandom.current().nextInt();
        for (GitlabProject project : projects) {
            assertNotEquals(projectName, project.getName());
            System.out.println(project.getName());
        }
        GitlabProject createdProject = CLIENT.newProject(projectName).create();
        assertNotNull(createdProject);
        assertEquals(projectName, createdProject.getName());
        projects = CLIENT.getProjectsQuery().query();
        int count = 0;
        for (GitlabProject project : projects) {
            if (project.equals(createdProject)) {
                count += 1;
            }
        }
        assertEquals(1, count);
        GitlabProject deletedProject = createdProject.delete();
        assertNotNull(deletedProject);
        assertEquals(createdProject, deletedProject);
    }
}