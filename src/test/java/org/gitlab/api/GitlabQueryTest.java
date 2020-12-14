package org.gitlab.api;

import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GitlabQueryTest {
    private static final GitlabAPIClient CLIENT =
            new GitlabAPIClient.Builder("https://gitlab.com").withAccessToken(System.getenv("TOKEN")).build();

    @Test
    void queryBranch() {
        GitlabProject project = CLIENT.getProject(22777636);
        List<GitlabBranch> branches = project.getBranchesQuery()
                                             .withSearch("test")
                                             .query();
        branches.forEach(System.out::println);
    }

    @Test
    void queryCommits() {
        GitlabProject project = CLIENT.getProject(22777636);
        List<GitlabCommit> commits = project.getCommitsQuery()
                                            .withFirstParent(true)
                                            .query();
        commits.forEach(System.out::println);
    }

    @Test
    void queryProjects() {
        List<GitlabProject> projects = CLIENT.getProjectsQuery().query();
        projects.forEach(System.out::println);
    }

    @Test
    void queryProjectIssues() {
        GitlabProject project = CLIENT.getProject(22777636);
        List<GitlabIssue> issues = project.getIssuesQuery()
                                          .withAuthorId(project.getCreatorId())
                                          .query();
        issues.forEach(System.out::println);
    }

    @Test
    void queryIssues() {
        List<GitlabIssue> issues = CLIENT.getIssuesQuery().query();
        issues.forEach(System.out::println);
    }

    @Test
    void queryProjectMergeRequest() {
        GitlabProject project = CLIENT.getProject(22777636);

        List<GitlabMergeRequest> mergeRequests =
                project.getMergeRequestsQuery().withLabels(Collections.singletonList("aaa")).query();
        mergeRequests.forEach(System.out::println);
    }

    @Test
    void queryMergeRequest() {
        List<GitlabMergeRequest> mergeRequests =
                CLIENT.getMergeRequestsQuery().withLabels(Collections.singletonList("aaa")).query();
        mergeRequests.forEach(System.out::println);
    }

    @Test
    void queryProjectUser() {
        GitlabProject project = CLIENT.getProject(22777636);
        List<GitlabUser> users = project.getUsersQuery().query();
        users.forEach(System.out::println);
    }

    @Test
    void testPagination() {
        Pagination pagination = Pagination.of(2, 10);
        List<GitlabProject> projects = CLIENT.getProjectsQuery().withPagination(pagination).query();
        assertEquals(10, projects.size());
    }

    @Test
    void testIssuePagination() {
        Pagination pagination = Pagination.of(1, 10);
        List<GitlabIssue> issues = CLIENT.getIssuesQuery().withPagination(pagination).query();
        assertEquals(10, issues.size());
    }

    @Test
    void testUserPagination() {
        GitlabProject project = CLIENT.getProject(22777636);
        List<GitlabUser> users = project.getUsersQuery().withPagination(Pagination.of(1, 1)).query();
        assertEquals(1, users.size());
    }

    @Test
    void testMergeRequest() {
        GitlabProject project = CLIENT.getProject(22777259);

       // List<GitlabMergeRequest> mrs = project.mergeRequests().query();
       // mrs.get(0).accept();
        project.getMergeRequest(1).accept();
    }

    @Test
    void getProjectWithoutToken() {
        GitlabAPIClient c =
                new GitlabAPIClient.Builder("https://gitlab.com").build();
        List<GitlabProject> projects = c.getProjectsQuery().query();
        System.out.println(projects);
    }
}