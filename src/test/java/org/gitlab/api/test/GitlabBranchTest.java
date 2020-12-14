package org.gitlab.api.test;

import org.gitlab.api.GitlabAPIClient;
import org.gitlab.api.GitlabBranch;
import org.gitlab.api.GitlabException;
import org.gitlab.api.GitlabProject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.jupiter.api.Assertions.*;

class GitlabBranchTest {
    private static final GitlabAPIClient CLIENT =
            new GitlabAPIClient.Builder("https://gitlab.com").withAccessToken(System.getenv("TOKEN")).build();
    private GitlabProject project;

    @BeforeEach
    void setUp() {
        project = CLIENT.newProject("test" + ThreadLocalRandom.current()).create();
    }

    @AfterEach
    void tearDown() {
        project.delete();
    }

    @Test
    void testGetters() {
        GitlabBranch branch = project.newBranch("branch", "ref").create();
        assertNotNull(branch);
        assertEquals(branch, project.getBranch("branch"));
        assertEquals("branch", branch.getName());
        assertEquals("", branch.getWebUrl());
        assertEquals(project.getName(), branch.getProject().getName());
        GitlabBranch deletedBranch = branch.delete();
        assertNotNull(deletedBranch);
        assertEquals(branch, deletedBranch);
    }

    @Test
    void testSequentialCRD() { // CRDR
        GitlabBranch branch = project.newBranch("branch", "ref").create();
        assertNotNull(branch);
        assertEquals("branch", project.getBranch("branch"));
        GitlabBranch deletedBranch = branch.delete();
        assertNotNull(deletedBranch);
        assertEquals(branch, deletedBranch);
    }


    @Test
    void testDuplicateDelete() { //CRURDDR
        GitlabBranch branch = project.newBranch("branch", "ref").create();
        assertNotNull(branch);
        assertEquals(branch, project.getBranch("branch"));
        assertEquals("branch", branch.getName());
        GitlabBranch deletedBranch = branch.delete();
        assertNotNull(deletedBranch);
        assertEquals(branch, deletedBranch);
        assertThrows(GitlabException.class, () -> {
            branch.delete();
        });
    }

    @Test
    void testQuery() {
        List<GitlabBranch> branches = project.getBranchesQuery().query();
        assertEquals(0, branches.size());

        GitlabBranch branch1 = project.newBranch("branch1", "ref1").create();
        GitlabBranch branch2 = project.newBranch("branch2", "ref2").create();
        GitlabBranch branch3 = project.newBranch("branch3", "ref3").create();

        branches = project.getBranchesQuery().query();
        assertEquals(3, branches.size());
        assertEquals(branch1.getName(), branches.get(0).getName());
        assertEquals(branch2.getName(), branches.get(1).getName());
        assertEquals(branch3.getName(), branches.get(2).getName());

        branch1.delete();
        branch2.delete();
        branch3.delete();

        branches = project.getBranchesQuery().query();
        assertEquals(0, branches.size());
    }
}