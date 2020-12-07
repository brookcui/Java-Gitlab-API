package org.gitlab.api.core;

import org.gitlab.api.http.Config;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GitlabMergeRequestTest {
    private static final GitlabAPIClient CLIENT =
            GitlabAPIClient.fromAccessToken("https://gitlab.com", System.getenv("TOKEN"));

    @Test // CRURDR
    void testSequentialCRUD() {
        GitlabProject project = CLIENT.newProject("test").create();
        GitlabBranch src = project.newBranch("branch1");
        GitlabBranch target = project.newBranch("branch2");

        GitlabMergeRequest mergeRequest = project.newMergeRequest(src.getName(), target.getName(), "req1").create();
        assertEquals("req1", project.getMergeRequest(mergeRequest.getIid()).getTitle());
        mergeRequest = mergeRequest.withDescription("a new merge request").update();
        assertEquals("a new merge request", project.getMergeRequest(mergeRequest.getIid()).getDescription());
        GitlabMergeRequest mergeRequestDeleted = mergeRequest.delete();
        assertThrows(GitlabException.class, ()->{project.getMergeRequest(mergeRequestDeleted.getIid());});

        src.delete();
        target.delete();
        project.delete();
    }

    @Test // CRDR
    void testSequentialCRD() {
        GitlabProject project = CLIENT.newProject("test").create();
        GitlabBranch src = project.newBranch("branch1");
        GitlabBranch target = project.newBranch("branch2");

        GitlabMergeRequest mergeRequest = project.newMergeRequest(src.getName(), target.getName(), "req1").create();
        assertEquals("req1", project.getIssue(mergeRequest.getIid()).getTitle());
        GitlabMergeRequest mergeRequestDeleted = mergeRequest.delete();
        assertThrows(GitlabException.class, ()->{project.getMergeRequest(mergeRequestDeleted.getIid());});

        src.delete();
        target.delete();
        project.delete();
    }

    @Test //CCRDR
    void testDuplicateCreate() {
        GitlabProject project = CLIENT.newProject("test").create();
        GitlabBranch src = project.newBranch("branch1");
        GitlabBranch target = project.newBranch("branch2");

        GitlabMergeRequest mergeRequest = project.newMergeRequest(src.getName(), target.getName(), "req1").create();
        assertThrows(GitlabException.class,
                ()->{project.newMergeRequest("req1", src.getName(), target.getName()).create();});
        assertEquals("issue1", project.getMergeRequest(mergeRequest.getIid()).getTitle());
        GitlabMergeRequest mergeRequestDeleted = mergeRequest.delete();
        assertThrows(GitlabException.class, ()->{project.getIssue(mergeRequestDeleted.getIid());});

        src.delete();
        target.delete();
        project.delete();
    }

    @Test //CRDDR
    void testDuplicateDelete() {
        GitlabProject project = CLIENT.newProject("test").create();
        GitlabBranch src = project.newBranch("branch1");
        GitlabBranch target = project.newBranch("branch2");

        GitlabMergeRequest mergeRequest = project.newMergeRequest(src.getName(), target.getName(), "req1").create();
        assertEquals("req1", project.getMergeRequest(mergeRequest.getIid()).getTitle());
        GitlabMergeRequest mergeRequestDeleted1 = mergeRequest.delete();
        assertNotNull(mergeRequestDeleted1);
        assertThrows(GitlabException.class, ()->{mergeRequest.delete();});
        assertThrows(GitlabException.class, ()->{project.getMergeRequest(mergeRequestDeleted1.getIid());});

        src.delete();
        target.delete();
        project.delete();
    }

    @Test //CRUURDR
    void testDuplicateUpdate() {
        GitlabProject project = CLIENT.newProject("test").create();
        GitlabBranch src = project.newBranch("branch1");
        GitlabBranch target = project.newBranch("branch2");

        GitlabMergeRequest mergeRequest = project.newMergeRequest(src.getName(), target.getName(), "req1").create();
        assertEquals("req1", project.getMergeRequest(mergeRequest.getIid()).getTitle());

        mergeRequest.withDescription("a new merge request").update();
        assertEquals("a new merge request", project.getMergeRequest(mergeRequest.getIid()).getDescription());
        mergeRequest.withTitle("req2").update();
        assertEquals("req2", project.getMergeRequest(mergeRequest.getIid()).getTitle());
        GitlabMergeRequest mergeRequestDeleted = mergeRequest.delete();
        assertThrows(GitlabException.class, ()->{project.getIssue(mergeRequestDeleted.getId());});

        src.delete();
        target.delete();
        project.delete();

    }

    @Test //UUU non-exist
    void testMultipleUpdate() {
        GitlabProject project = CLIENT.newProject("test").create();
        assertThrows(GitlabException.class, ()->{project.getMergeRequest(12345).withTitle("req1").update();});
        assertThrows(GitlabException.class, ()->{project.getIssue(12345).withTitle("req2").update();});
        assertThrows(GitlabException.class, ()->{project.getIssue(12345).withTitle("req3").update();});
        project.delete();

    }

    @Test
    void testEquals() {
        GitlabProject project = CLIENT.newProject("test").create();
        GitlabBranch src = project.newBranch("branch1");
        GitlabBranch target = project.newBranch("branch2");

        // same name
        GitlabMergeRequest mergeRequest1 = project.newMergeRequest(src.getName(), target.getName(), "req1").create();
        GitlabMergeRequest mergeRequest2 = project.newMergeRequest(src.getName(), target.getName(), "req1").create();
        assertFalse(mergeRequest1.equals(mergeRequest2));

        // different name, description
        mergeRequest1.withTitle("new title1").withDescription("new req1").update();
        mergeRequest2.withTitle("new title2").withDescription("new req2").update();
        assertFalse(mergeRequest1.equals(mergeRequest2));

        mergeRequest1.delete();
        mergeRequest2.delete();

        src.delete();
        target.delete();
        project.delete();
    }

    @Test
    void testToString() {
        GitlabProject project = CLIENT.newProject("test").create();
        GitlabBranch src = project.newBranch("branch1");
        GitlabBranch target = project.newBranch("branch2");

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
                ", title=" + mergeRequest.getMergeRequestCount() +
                ", updatedAt=" + mergeRequest.getCreatedAt() +
                ", createdAt=" + mergeRequest.getCreatedAt() +
                ", closedAt=" + mergeRequest.getClosedAt() +
                ", closedBy=" + mergeRequest.getClosedBy() +
                ", subscribed=" + mergeRequest.isSubscribed() +
                ", webUrl=" + mergeRequest.getWebUrl() +
                ", targetBranch=" + mergeRequest.getTargetBranch() +
                ", sourceBranch=" + mergeRequest.getSourceBranch() +
                ", labels=" + mergeRequest.getLabels() +
                '}';
        assertEquals("req", mergeRequest.toString());

        mergeRequest.delete();
        src.delete();
        target.delete();
        project.delete();
    }

    @Test
    void testGetter() {
        GitlabProject project = CLIENT.newProject("test").create();
        GitlabBranch src = project.newBranch("branch1");
        GitlabBranch target = project.newBranch("branch2");

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
        assertFalse(mergeRequest.isSubscribed());

        mergeRequest.delete();
        src.delete();
        target.delete();
        project.delete();
    }

    @Test
    void testChangeState() {
        GitlabProject project = CLIENT.newProject("test").create();
        GitlabBranch src = project.newBranch("branch1");
        GitlabBranch target = project.newBranch("branch2");
        GitlabMergeRequest mergeRequest = project.newMergeRequest(src.getName(), target.getName(), "req1").create();
        assertEquals("opened", mergeRequest.getState());
        mergeRequest.accept();
        assertEquals("merged", mergeRequest.getState());
        mergeRequest.delete();
        project.delete();
    }

    @Test
    void testQuery() {
        GitlabProject project = CLIENT.newProject("test").create();
        GitlabBranch src = project.newBranch("branch1");
        GitlabBranch target = project.newBranch("branch2");

        GitlabMergeRequest mergeRequest1 = project
                .newMergeRequest(src.getName(), target.getName(), "req1")
                .withDescription("new req1").create();
        GitlabMergeRequest mergeRequest2 = project
                .newMergeRequest(src.getName(), "master", "req2")
                .withDescription("new req2").create();
        GitlabMergeRequest mergeRequest3 = project
                .newMergeRequest(src.getName(), "master", "req3")
                .withDescription("new req3").create();

        // Query all issues
        List<GitlabMergeRequest> allMergeRequests = CLIENT.mergeRequests().query();
        assertEquals(3, allMergeRequests.size());

        // Valid query field
        Config config = project.getConfig();
        GitlabMergeRequest.Query q1 = new GitlabMergeRequest.Query(config).withTargetBranch(target.getName());
        List<GitlabMergeRequest> res1 = q1.query();
        assertEquals(1, res1.size());

        // Invalid query field
        GitlabMergeRequest.Query q2 = new GitlabMergeRequest.Query(config).withCreatedBefore(LocalDateTime.now());
        List<GitlabMergeRequest> res2 = q2.query();
        assertEquals(0, res2.size());

        // Sort
        GitlabMergeRequest.Query q3 = new GitlabMergeRequest.Query(config).withTargetBranch("master").withSort("desc");
        List<GitlabMergeRequest> res3 = q3.query();
        assertEquals(2, res3.size());
        assertEquals(mergeRequest3.getId(), res3.get(0).getId());
        assertEquals(mergeRequest2.getId(), res3.get(1).getId());

        // Order by
        GitlabMergeRequest.Query q4 = new GitlabMergeRequest
                .Query(config).
                withTargetBranch("master")
                .withOrderBy("created_at");
        List<GitlabMergeRequest> res4 = q4.query();
        assertEquals(2, res3.size());
        assertEquals(mergeRequest2.getId(), res4.get(0).getId());
        assertEquals(mergeRequest3.getId(), res4.get(1).getId());

        // Search
        GitlabMergeRequest.Query q5 = new GitlabMergeRequest.Query(config).withSearch("new req");
        List<GitlabMergeRequest> res5 = q5.query();
        assertEquals(3, res3.size());

        mergeRequest1.delete();
        mergeRequest2.delete();
        mergeRequest3.delete();
        src.delete();
        target.delete();
        project.delete();
    }
}
