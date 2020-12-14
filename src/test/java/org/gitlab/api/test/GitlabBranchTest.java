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
        String projectName = "test" + ThreadLocalRandom.current().nextInt();
        project = CLIENT.newProject(projectName).create();
    }

    @AfterEach
    void tearDown() {
        project.delete();
    }

    @Test
    void testGetters() {
        GitlabBranch branch = project.newBranch("branch", "master").create();
        assertNotNull(branch);
        assertEquals("branch", branch.getName());
        assertEquals(project.getName(), branch.getProject().getName());
        assertEquals("master", branch.getRef());
        assertEquals(project.getName(), branch.getProject().getName());
        assertEquals(false, branch.isDefault());
        assertEquals(false, branch.isMerged());
        assertEquals(false, branch.isProtected());
        assertEquals(true, branch.canPush());
        GitlabBranch deletedBranch = branch.delete();
        assertNotNull(deletedBranch);
        assertEquals(branch, deletedBranch);
    }

    @Test
    void testSequentialCRD() { // CRDR
        GitlabBranch branch = project.newBranch("branch", "master").create();
        assertNotNull(branch);
        assertEquals(branch, project.getBranch("branch"));
        GitlabBranch deletedBranch = branch.delete();
        assertNotNull(deletedBranch);
        assertEquals(branch, deletedBranch);
    }


    @Test
    void testDuplicateDelete() { //CRURDDR
        GitlabBranch branch = project.newBranch("branch", "master").create();
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

        GitlabBranch branch1 = project.newBranch("branch1", "master").create();
        GitlabBranch branch2 = project.newBranch("branch2", "master").create();
        GitlabBranch branch3 = project.newBranch("branch3", "master").create();

        branches = project.getBranchesQuery().query();
        assertEquals(4, branches.size());
        assertEquals(branch1.getName(), branches.get(0).getName());
        assertEquals(branch2.getName(), branches.get(1).getName());
        assertEquals(branch3.getName(), branches.get(2).getName());

        branch1.delete();
        branch2.delete();
        branch3.delete();

        branches = project.getBranchesQuery().query();
        assertEquals(1, branches.size());
    }
}