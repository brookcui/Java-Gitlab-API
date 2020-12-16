package core;
import org.gitlab.api.GitlabAPIClient;
import org.gitlab.api.*;

import java.util.List;

public class GitlabProjectExample {
    public static void main(String[] args) {
        // Connect to Gitlab via access token
        GitlabAPIClient client = new GitlabAPIClient
                .Builder("https://gitlab.com")
                .withAccessToken(System.getenv("TOKEN"))
                .build();
        // create a new project
        GitlabProject project = client.newProject("project1").create();
        // get all projects of a specific user
        List<GitlabProject> projects = client.getUserProjectsQuery(client.getCurrentUser().getUsername()).query();
        for (GitlabProject pro : projects) {
            System.out.println("ProjectID: " + pro.getId() + " Title: " + pro.getName());
        }

        // update project field, add a default branch
        project.withDefaultBranch("master").update();
        project.newBranch("branch1", "master").create();

        // fork a project
        GitlabProject projectForked = project.fork();
        System.out.println("project " + projectForked.getId() + "forked from project " + project.getId());
        System.out.println("Project " +project.getId() + "is forked " + project.getForksCount() + " times");

        // query issues, branch under a project
        List<GitlabIssue> issues = project.getIssuesQuery().query();
        List<GitlabBranch> branches = project.getBranchesQuery().query();

        // query , mergeRequest, commits with pagination
        Pagination pagination = Pagination.of(1, 20);
        List<GitlabMergeRequest> mergeRequests = project.getMergeRequestsQuery().withPagination(pagination).query();
        List<GitlabCommit> commits = project.getCommitsQuery().withPagination(pagination).query();

        System.out.println(project.getName() + " has " + issues.size() + " issues.");
        System.out.println(project.getName() + " has " + branches.size() + " branches.");
        System.out.println(project.getName() + " has " + mergeRequests.size() + " merge requests.");
        System.out.println(project.getName() + " has " + commits.size() + " commits.");

        // delete a project
        projectForked.delete();
        project.delete();
    }
}
