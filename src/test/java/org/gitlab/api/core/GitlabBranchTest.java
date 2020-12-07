package org.gitlab.api.models;

import org.gitlab.api.core.GitlabAPIClient;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class GitlabBranchTest {
    private static final String endpoint = "";
    private static final String accessToken = "";
    private static final GitlabAPIClient client =
            new GitlabAPIClient.Builder(endpoint).withAccessToken(accessToken).build();
    private static final int projectId = 0;
    private static final GitlabProject project = client.getProject(projectId);
    private static final GitlabBranch defaultBranch = project.newBranch("default", "ref");
    private static final GitlabBranch newBranch = project.newBranch("branchName", "ref");

    @Test
    void testGetName() {
        assertEquals("default", defaultBranch.getName());
    }

    @Test
    void testIsMerged() {
        assertFalse(defaultBranch.isMerged());
    }

    @Test
    void testIsProtected() {
        assertFalse(defaultBranch.isProtected());
    }

    @Test
    void testIsDefault() {
        assertFalse(defaultBranch.isDefault());
    }

    @Test
    void testCanPush() {
        assertFalse(defaultBranch.canPush());
    }

    @Test
    void testGetWebUrl() {
        assertNull(defaultBranch.getWebUrl());
    }

    @Test
    void testGetCommit() {
        assertNull(defaultBranch.getCommit());
    }

    @Test
    void testToString() {
        assertEquals("default", defaultBranch.toString());
    }

    @Test
    void testSequentialCRD() {
        final GitlabBranch[] branches = new GitlabBranch[2];
        assertDoesNotThrow(() -> {
            branches[0] = newBranch.create();
            branches[1] = branches[0].delete();
        });
        assertEquals("branchName", branches[0].getName());
        assertEquals("branchName", branches[1].getName());
    }

    @Test
    void testDuplicateCreate() {
        final GitlabBranch[] branches = new GitlabBranch[3];
        assertDoesNotThrow(() -> {
            branches[0] = newBranch.create();
        });
        assertEquals("branchName", branches[0].getName());
        assertThrows(IOException.class, () -> {
            branches[1] = branches[0].create(); // create existent branch
        });
        assertEquals("branchName", branches[1].getName());
        assertDoesNotThrow(() -> {
            branches[2] = branches[1].delete();
        });
        assertEquals("branchName", branches[2].getName());
    }

    @Test
    void testDuplicateDelete() {
        final GitlabBranch[] branches = new GitlabBranch[3];
        assertDoesNotThrow(() -> {
            branches[0] = newBranch.create();
        });
        assertEquals("branchName", branches[0].getName());
        assertDoesNotThrow(() -> {
            branches[1] = branches[0].delete();
        });
        assertEquals("branchName", branches[1].getName());
        assertThrows(IOException.class, () -> {
            branches[2] = branches[1].delete(); // delete non-existent branch
        });
        assertEquals("branchName", branches[2].getName());
    }
}