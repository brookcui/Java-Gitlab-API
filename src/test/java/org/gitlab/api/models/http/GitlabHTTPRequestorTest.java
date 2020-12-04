package org.gitlab.api.models.http;

import org.gitlab.api.core.AuthMethod;
import org.gitlab.api.core.GitlabAPIClient;
import org.gitlab.api.http.Config;
import org.gitlab.api.models.GitlabBranch;
import org.gitlab.api.models.GitlabCommit;
import org.gitlab.api.models.GitlabIssue;
import org.gitlab.api.models.GitlabMergeRequest;
import org.gitlab.api.models.GitlabProject;
import org.gitlab.api.models.GitlabUser;
import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.junit.rules.ExpectedException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class GitlabHTTPRequestorTest {
    private static final String PERSONAL_TOKEN = System.getenv("TOKEN");
    private static final Config CONFIG = new Config("https://gitlab.com", PERSONAL_TOKEN, AuthMethod.ACCESS_TOKEN);
    private static final GitlabAPIClient CLIENT = new GitlabAPIClient
            .Builder("https://gitlab.com")
            .withAccessToken(PERSONAL_TOKEN)
            .build();
    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Test
    void testBranch() throws IOException {
        GitlabProject project = CLIENT.getProject(22777636);
        System.out.println(project);
        GitlabBranch branch = project.newBranch("test11112").create("master");
        System.out.println(branch);
        branch.delete();
        // the branch should ne deleted
        branch = project.getBranch("test1111");
        exception.expect(FileNotFoundException.class);
    }

    @Test
    void testProject() throws IOException {
        GitlabProject project = CLIENT.newProject("This is actual test prtoject23334").create();
        GitlabProject updated = project.withName("Updated projectWithNewName11123451").update();
        project = CLIENT.getProject(project.getId());
        assertEquals("Updated projectWithNewName11123451", project.getName());
        project.delete();
        assertThrows(FileNotFoundException.class, ()-> CLIENT.getProject(updated.getId()));
    }

    @Test
    void getCommit() throws IOException {
        GitlabProject project = CLIENT.getProject(22777636);
        String commitSha = "1a4bc91c7e040ef5b15447351181538b46e0d986";
        GitlabCommit commit = project.getCommit(commitSha);
        System.out.println(commit);
    }

//    @Test
//        // TODO
//    void getUser() throws IOException {
//        GitlabProject project = CLIENT.getProject(22761336);
//        List<GitlabUser> users = project.getUsers();
//        System.out.println(users);
//    }


    @Test
    void testIssue() throws IOException {
        GitlabProject project = CLIENT.newProject("test Project To be Deleted project223536").create();
        GitlabIssue issue = project.newIssue("TestIssue1").create();
        issue.withDescription("Updated description").update();
        int iid = issue.getIid();
        GitlabIssue updated = project.getIssue(iid);
        assertEquals("Updated description", updated.getDescription());
        updated.delete();
        GitlabProject finalProject = project;
        assertThrows(FileNotFoundException.class, ()-> finalProject.getIssue(iid));
        project.delete();
        assertThrows(FileNotFoundException.class, ()-> CLIENT.getProject(finalProject.getId()));

    }

    @Test
    void testCurrentUser() throws IOException {
        GitlabUser user = CLIENT.getCurrentUser();
        System.out.println(user);
        int id = user.getId();
        GitlabUser userById = CLIENT.getUser(id);
        assertEquals(user, userById);
    }

    @Test
    void testMergeRequest() throws IOException {
        GitlabProject project = CLIENT.getProject(22777636);
        GitlabMergeRequest request =
                project.newMergeRequest("test1", "master", "Test MR").create();
        request.withTitle("Test MR update").update();
        GitlabMergeRequest updated = project.getMergeRequest(request.getIid());
        assertEquals("Test MR update", updated.getTitle());
        updated.delete();
        try {
            project.getMergeRequest(request.getIid());

        }catch (Exception e) {
            System.out.println(e);
        }
        assertThrows(FileNotFoundException.class, ()-> project.getMergeRequest(request.getIid()));
    }

    @Test
    void testGetOwnedProjects() throws IOException {
        List<GitlabProject> projects = CLIENT.getGroupProjects("gitlab-org");
        projects.forEach(System.out::println);
    }

    @Test
    void testIssues() throws IOException {
        GitlabProject project = CLIENT.getProject(22777636);
        GitlabUser currentUser = CLIENT.getCurrentUser();
        GitlabIssue issue = project.getAllIssues().get(0);
        System.out.println(issue);
        issue.withAssignees(Collections.singletonList(currentUser))
             .withDueDate(LocalDate.now().plusMonths(1))
             .update();
        System.out.println(issue);
    }
}