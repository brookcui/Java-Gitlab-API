package org.gitlab.api.core;

import org.gitlab.api.core.GitlabAPIClient;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class GitlabProjectTest {
    private static final String endpoint = "";
    private static final String accessToken = "";
    private static final GitlabAPIClient client =
            new GitlabAPIClient.Builder(endpoint).withAccessToken(accessToken).build();
    private static final int projectId = 0;
    private static final GitlabProject project = client.getProject(projectId);
    private static final GitlabProject newProject = client.newProject("newProjectName");

    @Test
    void testSequentialCRUD() {
        final GitlabProject[] projects = new GitlabProject[3];
        assertDoesNotThrow(() -> {
            projects[0] = newProject.create();
        });
        assertEquals("newProjectName", projects[0].getName());
        assertDoesNotThrow(() -> {
            projects[1] = projects[0].withPath("path").update();
        });
        assertEquals("newProjectName", projects[1].getName());
        assertEquals("path", projects[1].getPath());
        assertDoesNotThrow(() -> {
            projects[2] = projects[1].delete();
        });
        assertEquals("newProjectName", projects[2].getName());
    }

    @Test
    void testSequentialCRD() {
        final GitlabProject[] projects = new GitlabProject[2];
        assertDoesNotThrow(() -> {
            projects[0] = newProject.create();
            projects[1] = projects[0].delete();
        });
        assertEquals("newProjectName", projects[0].getName());
        assertEquals("newProjectName", projects[1].getName());
    }

    @Test
    void testDuplicateCreate() {
        final GitlabProject[] projects = new GitlabProject[3];
        assertDoesNotThrow(() -> {
            projects[0] = newProject.create();
        });
        assertEquals("newProjectName", projects[0].getName());
        assertThrows(IOException.class, () -> {
            projects[1] = projects[0].create(); // create existent project
        });
        assertEquals("newProjectName", projects[1].getName());
        assertDoesNotThrow(() -> {
            projects[2] = projects[1].delete();
        });
        assertEquals("newProjectName", projects[2].getName());
    }

    @Test
    void testDuplicateUpdate() {
        final GitlabProject[] projects = new GitlabProject[4];
        assertDoesNotThrow(() -> {
            projects[0] = newProject.create();
        });
        assertEquals("newProjectName", projects[0].getName());
        assertNotEquals("path", projects[0].getPath());
        assertNotEquals("desc", projects[0].getDescription());
        assertDoesNotThrow(() -> {
            projects[1] = projects[0].withPath("path").update();
            projects[2] = projects[1].withDescription("desc").update();
        });
        assertEquals("newProjectName", projects[1].getName());
        assertEquals("path", projects[1].getPath());
        assertNotEquals("desc", projects[1].getDescription());
        assertEquals("newProjectName", projects[2].getName());
        assertEquals("path", projects[2].getPath());
        assertEquals("desc", projects[2].getDescription());
        assertDoesNotThrow(() -> {
            projects[3] = projects[2].delete();
        });
        assertEquals("newProjectName", projects[3].getName());
    }

    @Test
    void testDuplicateDelete() {
        final GitlabProject[] projects = new GitlabProject[3];
        assertDoesNotThrow(() -> {
            projects[0] = newProject.create();
        });
        assertEquals("newProjectName", projects[0].getName());
        assertDoesNotThrow(() -> {
            projects[1] = projects[0].delete();
        });
        assertEquals("newProjectName", projects[1].getName());
        assertThrows(IOException.class, () -> {
            projects[2] = projects[1].delete(); // delete non-existent project
        });
        assertEquals("newProjectName", projects[2].getName());
    }
}