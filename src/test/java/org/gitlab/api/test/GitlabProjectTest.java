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
        String projectName = "test" + ThreadLocalRandom.current();
        GitlabProject project = CLIENT.newProject(projectName).create();
        assertEquals(projectName, project.getName());
        GitlabProject updatedProject = project.withDefaultBranch("default").withDescription("desc").update();
        assertEquals(project, updatedProject);
        assertEquals("default", updatedProject.getDefaultBranch());
        assertEquals("desc", updatedProject.getDescription());
        GitlabProject deletedProject = project.delete();
        assertEquals(project, deletedProject);
    }

    @Test
    void testDuplicateDelete() {
        String projectName = "test" + ThreadLocalRandom.current();
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
        String projectName = "test" + ThreadLocalRandom.current();
        for (GitlabProject project : projects) {
            assertNotEquals(projectName, project.getName());
        }
        int count = projects.size();
        GitlabProject createdProject = CLIENT.newProject(projectName).create();
        assertNotNull(createdProject);
        assertEquals(projectName, createdProject.getName());
        projects = CLIENT.getProjectsQuery().query();
        assertEquals(count+1, projects.size());
        GitlabProject deletedProject = createdProject.delete();
        assertEquals(createdProject, deletedProject);
    }
}