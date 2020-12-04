package org.gitlab.api.models;

import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GitlabProjectTest {
    private static final GitlabAPIClient CLIENT = GitlabAPIClient
            .fromAccessToken("https://gitlab.com", System.getenv("TOKEN"));

    @Test
    void getIssue() {
        GitlabProject project = CLIENT.newProject("test").create();
        GitlabIssue issue = project.newIssue("new issue").create();
        assertEquals("new issue", project.getIssue(issue.getIid()).getTitle());
        project.delete();
    }

    @Test
    void query() {
        List<GitlabProject> projects = CLIENT.projects().withArchived(false).query();
        projects.forEach(System.out::println);

    }

    @Test
    void queryIssues() {
        GitlabProject project = CLIENT.getProject(22777636);
        List<GitlabIssue> issues = project.issues().withLabels(Collections.singletonList("ccc")).query();
        assertEquals(1, issues.size());
        assertEquals(22777636, issues.get(0).getProjectId());
        System.out.println(issues);
    }

    @Test
    void queryBranch() {
        GitlabProject project = CLIENT.getProject(22777636);
        List<GitlabBranch> branches = project.branches().withSearch("test1").query();
        assertEquals(2, branches.size());
        assertTrue(branches.get(0).getName().contains("test1"));
    }

    @Test
    void queryMergeRequest() {
        GitlabProject project = CLIENT.getProject(22777636);
        List<GitlabMergeRequest> mergeRequests =
                project.mergeRequests().withAuthorId(project.getCreatorId()).withState("closed").query();
        assertEquals(2, mergeRequests.size());
    }
}