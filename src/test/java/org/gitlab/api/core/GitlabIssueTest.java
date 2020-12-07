package org.gitlab.api.core;

import org.gitlab.api.http.Config;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GitlabIssueTest {
    private static final GitlabAPIClient CLIENT =
            GitlabAPIClient.fromAccessToken("https://gitlab.com", System.getenv("TOKEN"));
    @Test // CRURDR
    void testSequentialCRUD() {
        GitlabProject project = CLIENT.newProject("test").create();
        GitlabIssue issue1 = project.newIssue("issue1").create();
        assertEquals("issue1", project.getIssue(issue1.getId()).getTitle());
        issue1 = issue1.withDescription("a new issue").update();
        assertEquals("a new issue", project.getIssue(issue1.getId()).getDescription());
        GitlabIssue issueDeleted = issue1.delete();
        assertThrows(GitlabException.class, ()->{project.getIssue(issueDeleted.getId());});
        project.delete();
    }

    @Test // CRDR
    void testSequentialCRD() {
        GitlabProject project = CLIENT.newProject("test").create();
        GitlabIssue issue1 = project.newIssue("issue1").create();
        assertEquals("issue1", project.getIssue(issue1.getId()).getTitle());
        GitlabIssue issueDeleted = issue1.delete();
        assertThrows(GitlabException.class, ()->{project.getIssue(issueDeleted.getId());});
        project.delete();
    }

    @Test //CRURDDR
    void testDuplicateDelete() {
        GitlabProject project = CLIENT.newProject("test").create();
        GitlabIssue issue1 = project.newIssue("issue1").create();
        assertEquals("issue1", project.getIssue(issue1.getId()).getTitle());
        issue1.withDescription("a new issue").update();
        assertEquals("a new issue", project.getIssue(issue1.getId()).getDescription());
        GitlabIssue issueDeleted1 = issue1.delete();
        assertNotNull(issueDeleted1);
        assertThrows(GitlabException.class, ()->{issue1.delete();});
        assertThrows(GitlabException.class, ()->{project.getIssue(issue1.getId()).getTitle();});
        project.delete();
    }

    @Test //CRUURDR
    void testDuplicateUpdate() {
        GitlabProject project = CLIENT.newProject("test").create();
        GitlabIssue issue1 = project.newIssue("issue1").create();
        assertEquals("issue1", project.getIssue(issue1.getId()).getTitle());
        issue1.withDescription("a new issue1").update();
        assertEquals("a new issue", project.getIssue(issue1.getId()).getDescription());
        issue1.withDescription("a new issue2").update();
        assertEquals("a new issue2", project.getIssue(issue1.getId()).getDescription());
        GitlabIssue issueDeleted1 = issue1.delete();
        assertNotNull(issueDeleted1);
        assertThrows(GitlabException.class, ()->{project.getIssue(issue1.getId()).getTitle();});
        project.delete();
    }

    @Test //Update a non-exist object
    void testMultipleUpdate() {
        GitlabProject project = CLIENT.newProject("test").create();
        assertThrows(GitlabException.class, ()->{project.getIssue(12345).withTitle("issue1").update();});
        assertThrows(GitlabException.class, ()->{project.getIssue(12345).withDescription("a new issue1").update();});
        assertThrows(GitlabException.class, ()->{project.getIssue(12345).withDueDate(LocalDate.now()).update();});
        project.delete();
    }

    @Test // Move issue to a new project
    void testMoveIssue() {
        GitlabProject project1 = CLIENT.newProject("test1").create();
        GitlabProject project2 = CLIENT.newProject("test2").create();
        GitlabIssue issue1 = project1.newIssue("issue1").create();
        assertEquals("issue1", project1.getIssue(issue1.getId()).getTitle());
        issue1.withProject(project2).update();
        assertThrows(GitlabException.class, ()->{project1.getIssue(issue1.getId());});
        assertEquals("issue1", project1.getIssue(issue1.getId()).getTitle());
        issue1.delete();
        project1.delete();
        project2.delete();
    }

    @Test
    void testEquals() {
        GitlabProject project = CLIENT.newProject("test").create();
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
        project.delete();
    }

    @Test
    void testToString() {
        GitlabProject project = CLIENT.newProject("test").create();
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
                ", hasTasks=" + issue1.isHasTasks() +
                ", epicId=" + issue1.getEpicId() +
                '}';
        assertEquals(expected, issue1.toString());
        issue1.delete();
        project.delete();
    }

    @Test
    void testGetter() {
        GitlabProject project = CLIENT.newProject("test").create();
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
        assertFalse(issue1.isSubscribed());

        issue1.delete();
        project.delete();
    }

    @Test
    void testChangeState() {
        GitlabProject project = CLIENT.newProject("test").create();
        GitlabIssue issue1 = project.newIssue("issue1").create();
        assertEquals("opened", issue1.getState());
        issue1.close();
        assertEquals("closed", issue1.getState());
        issue1.reopen();
        assertEquals("opened", issue1.getState());
        issue1.delete();
        project.delete();
    }

    @Test
    void testQuery() {

    }
}