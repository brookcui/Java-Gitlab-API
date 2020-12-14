package org.gitlab.api.test;

import org.gitlab.api.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class GitlabIssueTest {
    private static final GitlabAPIClient CLIENT = new GitlabAPIClient
            .Builder("https://gitlab.com")
            .withAccessToken(System.getenv("TOKEN"))
            .build();
    private GitlabProject project;

    @BeforeEach
    void setup() {
        Random ran = new Random();
        String num = String.valueOf(ran.nextInt(100));
        project = CLIENT.newProject("test" + num).create();

    }
    @AfterEach
    void cleanup() {
        project.delete();
    }

    @Test // CRURDR
    void testSequentialCRUD() {
        GitlabIssue issue1 = project.newIssue("issue1").create();
        assertEquals("issue1", project.getIssue(issue1.getIid()).getTitle());
        issue1 = issue1.withDescription("a new issue").update();
        assertEquals("a new issue", project.getIssue(issue1.getIid()).getDescription());
        GitlabIssue issueDeleted = issue1.delete();
        assertThrows(GitlabException.class, ()->{project.getIssue(issueDeleted.getIid());});
    }

    @Test // CRDR
    void testSequentialCRD() {
        GitlabIssue issue1 = project.newIssue("issue1").create();
        assertEquals("issue1", project.getIssue(issue1.getIid()).getTitle());
        GitlabIssue issueDeleted = issue1.delete();
        assertThrows(GitlabException.class, ()->{project.getIssue(issueDeleted.getIid());});
    }

    @Test //CRURDDR
    void testDuplicateDelete() {
        GitlabIssue issue1 = project.newIssue("issue1").create();
        assertEquals("issue1", project.getIssue(issue1.getIid()).getTitle());
        issue1.withDescription("a new issue").update();
        assertEquals("a new issue", project.getIssue(issue1.getIid()).getDescription());
        GitlabIssue issueDeleted1 = issue1.delete();
        assertNotNull(issueDeleted1);
        assertThrows(GitlabException.class, ()->{issue1.delete();});
        assertThrows(GitlabException.class, ()->{project.getIssue(issue1.getIid()).getTitle();});
    }

    @Test //CRUURDR
    void testDuplicateUpdate() {
        GitlabIssue issue1 = project.newIssue("issue1").create();
        assertEquals("issue1", project.getIssue(issue1.getIid()).getTitle());
        issue1.withDescription("a new issue1").update();
        assertEquals("a new issue1", project.getIssue(issue1.getIid()).getDescription());
        issue1.withDescription("a new issue2").update();
        assertEquals("a new issue2", project.getIssue(issue1.getIid()).getDescription());
        GitlabIssue issueDeleted1 = issue1.delete();
        assertNotNull(issueDeleted1);
        assertThrows(GitlabException.class, ()->{project.getIssue(issue1.getIid()).getTitle();});
    }

    @Test //Update a non-exist object
    void testMultipleUpdate() {
        assertThrows(GitlabException.class, ()->{project.getIssue(12345).withTitle("issue1").update();});
        assertThrows(GitlabException.class, ()->{project.getIssue(12345).withDescription("a new issue1").update();});
        assertThrows(GitlabException.class, ()->{project.getIssue(12345).withDueDate(LocalDate.now()).update();});
    }

    @Test
    void testEquals() {
        // same title
        GitlabIssue issue1 = project.newIssue("issue").create();
        GitlabIssue issue2 = project.newIssue("issue").create();
        assertFalse(issue1.equals(issue2));
        // different title and description
        issue1.withTitle("new title1").withDescription("new issue1").update();
        issue2.withTitle("new title2").withDescription("new issue2").update();
        assertFalse(issue1.equals(issue2));
        issue1.delete();
        issue2.delete();
    }

    @Test
    void testToString() {
        GitlabIssue issue1 = project.newIssue("issue1").create();
        issue1.withDescription("a new issue");
        String expected = "GitlabIssue{" +
                "id=" + issue1.getId() +
                ", iid=" + issue1.getIid() +
                ", projectId=" + issue1.getProjectId() +
                ", author=" + issue1.getAuthor() +
                ", description=" + issue1.getDescription() +
                ", state=" + issue1.getState() +
                ", assignees=" + issue1.getAssignees() +
                ", upvotes=" + issue1.getUpvotes() +
                ", downvotes=" + issue1.getDownvotes() +
                ", mergeRequestCount=" + issue1.getMergeRequestCount() +
                ", title=" + issue1.getTitle() +
                ", labels=" + issue1.getLabels() +
                ", updatedAt=" + issue1.getUpdatedAt() +
                ", createdAt=" + issue1.getCreatedAt() +
                ", closedAt=" + issue1.getClosedAt() +
                ", closedBy=" + issue1.getClosedBy() +
                ", subscribed=" + issue1.isSubscribed() +
                ", dueDate=" + issue1.getDueDate() +
                ", webUrl=" + issue1.getWebUrl() +
                ", hasTasks=" + issue1.hasTasks() +
                ", epicId=" + issue1.getEpicId() +
                '}';
        assertEquals(expected, issue1.toString());
        issue1.delete();
    }

    @Test
    void testGetter() {
        GitlabIssue issue1 = project.newIssue("issue1").create();
        assertEquals("issue1", issue1.getTitle());
        assertEquals(project.getId(), issue1.getProjectId());
        assertEquals(0, issue1.getUpvotes());
        assertEquals(0, issue1.getDownvotes());
        assertEquals(0, issue1.getMergeRequestCount());
        assertEquals("opened", issue1.getState());
        assertNull(issue1.getClosedAt());
        assertNull(issue1.getDueDate());
        assertFalse(issue1.hasTasks());
        assertTrue(issue1.isSubscribed());

        issue1.delete();
    }

    @Test
    void testChangeState() {
        GitlabIssue issue1 = project.newIssue("issue1").create();
        assertEquals("opened", issue1.getState());
        issue1.close();
        assertEquals("closed", issue1.getState());
        issue1.reopen();
        assertEquals("opened", issue1.getState());
        issue1.delete();
    }

    @Test
    void testQuery() {
        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);

        GitlabIssue issue1 = project.newIssue("issue1").withDescription("new issue1").create();
        GitlabIssue issue2 = project.newIssue("issue2").withDueDate(tomorrow).create();
        GitlabIssue issue3 = project.newIssue("issue3").withDueDate(tomorrow).create();

        // Query all merge requests visible to current user
        List<GitlabIssue> allIssues = CLIENT.getIssuesQuery().query();
        assertTrue(allIssues.size() >= 3);

        // Query all issues under this project
        List<GitlabIssue> allProjectIssues = project.getIssuesQuery().query();
        assertEquals(3, allProjectIssues.size());

        // Valid query field
        List<GitlabIssue> res1 = project.getIssuesQuery().withDueDate("0").query();
        assertEquals(1, res1.size());

        // Invalid query field
        List<GitlabIssue> res2 = project.getIssuesQuery().withAuthorUsername("invalid name").query();
        assertEquals(0, res2.size());

        // Sort
        List<GitlabIssue> res3 = project.getIssuesQuery().withDueDate("week").withSort("desc").query();
        assertEquals(2, res3.size());
        assertEquals(issue3.getIid(), res3.get(0).getIid());
        assertEquals(issue2.getIid(), res3.get(1).getIid());

        // Order by
        List<GitlabIssue> res4 = project.getIssuesQuery().withDueDate("week").withOrderBy("created_at").query();
        assertEquals(2, res4.size());
        assertEquals(issue3.getIid(), res4.get(0).getIid());
        assertEquals(issue2.getIid(), res4.get(1).getIid());

        // Search
        List<GitlabIssue> res5 = project.getIssuesQuery().withSearch("new issue1").query();
        assertEquals(1, res5.size());
        assertEquals(issue1.getIid(), res5.get(0).getIid());

        issue1.delete();
        issue2.delete();
        issue3.delete();
    }
}