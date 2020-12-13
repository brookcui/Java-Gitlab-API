package org.gitlab.api.core;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class GitlabMergeRequestTest {
    private static final GitlabAPIClient CLIENT =
            GitlabAPIClient.fromAccessToken("https://gitlab.com", System.getenv("TOKEN"));

    private GitlabProject project;
    private GitlabBranch src, target;

    @BeforeEach
    void setup() {
        Random ran = new Random();
        String num = String.valueOf(ran.nextInt(100));
        project = CLIENT.newProject("test" + num).create();
        src = project.newBranch("branch1", "master").create();
        target = project.newBranch("branch2", "master").create();
    }
    @AfterEach
    void cleanup() {
        project.delete();
    }

    @Test // CRURDR
    void testSequentialCRUD() {
        GitlabMergeRequest mergeRequest = project.newMergeRequest(src.getName(), target.getName(), "req1").create();
        assertEquals("req1", project.getMergeRequest(mergeRequest.getIid()).getTitle());
        mergeRequest = mergeRequest.withDescription("a new merge request").update();
        assertEquals("a new merge request", project.getMergeRequest(mergeRequest.getIid()).getDescription());
        GitlabMergeRequest mergeRequestDeleted = mergeRequest.delete();
        assertThrows(GitlabException.class, ()->{project.getMergeRequest(mergeRequestDeleted.getIid());});
    }

    @Test // CRDR
    void testSequentialCRD() {
        GitlabMergeRequest mergeRequest = project.newMergeRequest(src.getName(), target.getName(), "req1").create();
        assertEquals("req1", project.getMergeRequest(mergeRequest.getIid()).getTitle());
        GitlabMergeRequest mergeRequestDeleted = mergeRequest.delete();
        assertThrows(GitlabException.class, ()->{project.getMergeRequest(mergeRequestDeleted.getIid());});
    }

    @Test //CCRDR
    void testDuplicateCreate() {
        GitlabMergeRequest mergeRequest = project.newMergeRequest(src.getName(), target.getName(), "req1").create();
        assertThrows(GitlabException.class,
                ()->{project.newMergeRequest(src.getName(), target.getName(), "req1").create();});
        assertEquals("req1", project.getMergeRequest(mergeRequest.getIid()).getTitle());
        GitlabMergeRequest mergeRequestDeleted = mergeRequest.delete();
        assertThrows(GitlabException.class, ()->{project.getMergeRequest(mergeRequestDeleted.getIid());});
    }

    @Test //CRDDR
    void testDuplicateDelete() {
        GitlabMergeRequest mergeRequest = project.newMergeRequest(src.getName(), target.getName(), "req1").create();
        assertEquals("req1", project.getMergeRequest(mergeRequest.getIid()).getTitle());
        GitlabMergeRequest mergeRequestDeleted1 = mergeRequest.delete();
        assertNotNull(mergeRequestDeleted1);
        assertThrows(GitlabException.class, ()->{mergeRequest.delete();});
        assertThrows(GitlabException.class, ()->{project.getMergeRequest(mergeRequestDeleted1.getIid());});
    }

    @Test //CRUURDR
    void testDuplicateUpdate() {
        GitlabMergeRequest mergeRequest = project.newMergeRequest(src.getName(), target.getName(), "req1").create();
        assertEquals("req1", project.getMergeRequest(mergeRequest.getIid()).getTitle());
        mergeRequest.withDescription("a new merge request").update();
        assertEquals("a new merge request", project.getMergeRequest(mergeRequest.getIid()).getDescription());
        mergeRequest.withTitle("req2").update();
        assertEquals("req2", project.getMergeRequest(mergeRequest.getIid()).getTitle());
        GitlabMergeRequest mergeRequestDeleted = mergeRequest.delete();
        assertThrows(GitlabException.class, ()->{project.getMergeRequest(mergeRequestDeleted.getIid());});
    }

    @Test //UUU non-exist
    void testMultipleUpdate() {
        assertThrows(GitlabException.class, ()->{project.getMergeRequest(12345).withTitle("req1").update();});
        assertThrows(GitlabException.class, ()->{project.getMergeRequest(12345).withTitle("req2").update();});
        assertThrows(GitlabException.class, ()->{project.getMergeRequest(12345).withTitle("req3").update();});
    }

    @Test
    void testEquals() {
        // same name, different branch
        GitlabMergeRequest mergeRequest1 = project.newMergeRequest(target.getName(), src.getName(), "req1").create();
        GitlabMergeRequest mergeRequest2 = project.newMergeRequest(src.getName(), target.getName(), "req1").create();
        assertFalse(mergeRequest1.equals(mergeRequest2));

        // different name and description
        mergeRequest1.withTitle("new title1").withDescription("new req1").update();
        mergeRequest2.withTitle("new title2").withDescription("new req2").update();
        assertFalse(mergeRequest1.equals(mergeRequest2));

        mergeRequest1.delete();
        mergeRequest2.delete();
    }

    @Test
    void testToString() {
        GitlabMergeRequest mergeRequest = project.newMergeRequest(src.getName(), target.getName(), "req1").create();
        String expected = "GitlabMergeRequest{" +
                "id=" + mergeRequest.getId() +
                ", iid=" + mergeRequest.getIid() +
                ", author=" + mergeRequest.getAuthor() +
                ", description=" + mergeRequest.getDescription() +
                ", state=" + mergeRequest.getState() +
                ", assignees=" + mergeRequest.getAssignees() +
                ", upvotes=" + mergeRequest.getUpvotes() +
                ", downvotes=" + mergeRequest.getDownvotes() +
                ", mergeRequestCount=" + mergeRequest.getMergeRequestCount() +
                ", title=" + mergeRequest.getTitle() +
                ", updatedAt=" + mergeRequest.getUpdatedAt() +
                ", createdAt=" + mergeRequest.getCreatedAt() +
                ", closedAt=" + mergeRequest.getClosedAt() +
                ", closedBy=" + mergeRequest.getClosedBy() +
                ", subscribed=" + mergeRequest.isSubscribed() +
                ", webUrl=" + mergeRequest.getWebUrl() +
                ", targetBranch=" + mergeRequest.getTargetBranch() +
                ", sourceBranch=" + mergeRequest.getSourceBranch() +
                ", labels=" + mergeRequest.getLabels() +
                '}';
        assertEquals(expected, mergeRequest.toString());

        mergeRequest.delete();
    }

    @Test
    void testGetter() {
        GitlabMergeRequest mergeRequest = project.newMergeRequest(src.getName(), target.getName(), "req1").create();
        assertEquals("req1", mergeRequest.getTitle());
        assertEquals(project.getId(), mergeRequest.getProject().getId());
        assertEquals(target.getName(), mergeRequest.getTargetBranch());
        assertEquals(src.getName(), mergeRequest.getSourceBranch());
        assertEquals(0, mergeRequest.getUpvotes());
        assertEquals(0, mergeRequest.getDownvotes());
        assertEquals(0, mergeRequest.getMergeRequestCount());
        assertEquals("opened", mergeRequest.getState());
        assertNull(mergeRequest.getClosedAt());
        assertNull(mergeRequest.getClosedBy());
        assertTrue(mergeRequest.isSubscribed());

        mergeRequest.delete();
    }

    @Test
    void testState() {
        GitlabMergeRequest mergeRequest = project.newMergeRequest(src.getName(), target.getName(), "req1").create();
        assertEquals("opened", mergeRequest.getState());
        mergeRequest.delete();
    }

    @Test
    void testQuery() {
        GitlabMergeRequest mergeRequest1 = project
                .newMergeRequest(src.getName(), target.getName(), "req1")
                .withDescription("new req1").create();
        GitlabMergeRequest mergeRequest2 = project
                .newMergeRequest(src.getName(), "master", "req2")
                .withDescription("new req2").create();
        GitlabMergeRequest mergeRequest3 = project
                .newMergeRequest(target.getName(), "master", "req3")
                .withDescription("new req3").create();

        // Query all merge requests visible to current user
        List<GitlabMergeRequest> allMergeRequests = CLIENT.mergeRequests().query();
        assertTrue(allMergeRequests.size() >= 3);

        // Query all merge requests under the current project
        List<GitlabMergeRequest> allProjectMergeRequests = project.mergeRequests().query();
        assertEquals(3, allProjectMergeRequests.size());

        // Valid query field
        List<GitlabMergeRequest> res1 = project.mergeRequests().withTargetBranch(target.getName()).query();
        assertEquals(1, res1.size());

        // Invalid query field
        List<GitlabMergeRequest> res2 = project.mergeRequests().withCreatedAfter(LocalDateTime.now()).query();
        assertEquals(0, res2.size());

        // Sort
        List<GitlabMergeRequest> res3 = project.mergeRequests().withTargetBranch("master").withSort("desc").query();
        assertEquals(2, res3.size());
        assertEquals(mergeRequest3.getIid(), res3.get(0).getIid());
        assertEquals(mergeRequest2.getIid(), res3.get(1).getIid());

        // Order by
        List<GitlabMergeRequest> res4 = project.mergeRequests().
                withTargetBranch("master").
                withOrderBy("created_at").
                query();
        assertEquals(2, res4.size());
        assertEquals(mergeRequest3.getIid(), res4.get(0).getIid());
        assertEquals(mergeRequest2.getIid(), res4.get(1).getIid());

        // Search
        List<GitlabMergeRequest> res5 = project.mergeRequests().withSearch("new req").query();
        assertEquals(3, res5.size());

        mergeRequest1.delete();
        mergeRequest2.delete();
        mergeRequest3.delete();
    }
}
